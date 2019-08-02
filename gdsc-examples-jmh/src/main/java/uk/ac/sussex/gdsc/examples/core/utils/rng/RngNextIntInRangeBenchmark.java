/*-
 * #%L
 * Code for running JMH benchmarks to assess performance.
 * %%
 * Copyright (C) 2018 - 2019 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package uk.ac.sussex.gdsc.examples.core.utils.rng;

import uk.ac.sussex.gdsc.core.utils.rng.Pcg32;
import uk.ac.sussex.gdsc.core.utils.rng.RandomUtils;
import uk.ac.sussex.gdsc.core.utils.rng.SplitMix;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source32.IntProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Executes benchmark to compare the speed of random number generators to create an int value in a
 * range.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class RngNextIntInRangeBenchmark {
  /** The value of 2^32. */
  private static long MAX_32 = 1L << 32;

  /** The value. Must NOT be final to prevent JVM optimisation! */
  private int intValue;

  /**
   * The upper range for the {@code int} generation.
   */
  @State(Scope.Benchmark)
  public static class IntRange {
    /**
     * The upper range for the {@code int} generation.
     *
     * <p>Note that the while loop uses a rejection algorithm. From the Javadoc for
     * java.util.Random:</p>
     *
     * <pre>
     * "The probability of a value being rejected depends on n. The
     * worst case is n=2^30+1, for which the probability of a reject is 1/2,
     * and the expected number of iterations before the loop terminates is 2."
     * </pre>
     */
    @Param({"256", // Even: 1 << 8
        "257", // Prime number
        "1073741825", // Worst case: (1 << 30) + 1
    })
    private int upperBound;

    /**
     * Gets the upper bound.
     *
     * @return the upper bound
     */
    public int getUpperBound() {
      return upperBound;
    }
  }

  /**
   * The data to shuffle.
   */
  @State(Scope.Benchmark)
  public static class IntData {
    /**
     * The size of the data.
     */
    @Param({"1337"})
    private int size;

    /** The data. */
    private int[] data;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public int[] getData() {
      return data;
    }

    /**
     * Create the data.
     */
    @Setup
    public void setup() {
      data = IntStream.range(0, size).toArray();
    }
  }

  /**
   * The source generator.
   */
  @State(Scope.Benchmark)
  public static class Source {
    @Param({
        // "xshrs", "xshrr", "mix32", "mix64",
        "splitRng", "split1", "split2", "split3",})
    private String name;

    /** The random generator. */
    private UniformRandomProvider rng;

    /**
     * Gets the random generator.
     *
     * @return the generator
     */
    public UniformRandomProvider getRng() {
      return rng;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      Random seed = ThreadLocalRandom.current();
      if ("xshrs".equals(name)) {
        rng = Pcg32.xshrs(seed.nextLong(), seed.nextLong());
      } else if ("xshrr".equals(name)) {
        rng = Pcg32.xshrr(seed.nextLong(), seed.nextLong());
      } else if ("mix32".equals(name)) {
        rng = SplitMix.new32(seed.nextLong());
      } else if ("mix64".equals(name)) {
        rng = SplitMix.new64(seed.nextLong());
      } else if ("splitRng".equals(name)) {
        rng = new TestSplitMix(seed.nextLong());
      } else if ("split1".equals(name)) {
        rng = new TestSplitMix1(seed.nextLong());
      } else if ("split2".equals(name)) {
        rng = new TestSplitMix2(seed.nextLong());
      } else if ("split3".equals(name)) {
        rng = new TestSplitMix3(seed.nextLong());
      }
    }
  }

  /**
   * Implement the SplitMix algorithm from {@link java.util.SplittableRandom SplittableRandom} to
   * output 32-bit int values. This uses the default nextInt(int) method of Commons RNG.
   */
  public static class TestSplitMix extends IntProvider {
    /**
     * The golden ratio, phi, scaled to 64-bits and rounded to odd.
     *
     * <pre>
     * phi = (sqrt(5) - 1) / 2) * 2^64
     *     ~ 0.61803 * 2^64
     *     = 11400714819323198485 (unsigned 64-bit integer)
     * </pre>
     */
    private static final long GOLDEN_RATIO = 0x9e3779b97f4a7c15L;

    /** The state. */
    long state;

    /**
     * Create a new instance.
     *
     * @param seed the seed
     */
    TestSplitMix(long seed) {
      state = seed;
    }

    @Override
    public int next() {
      long key = state += GOLDEN_RATIO;
      // 32 high bits of Stafford variant 4 mix64 function as int:
      // http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html
      key = (key ^ (key >>> 33)) * 0x62a9d9ed799705f5L;
      return (int) (((key ^ (key >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
    }
  }

  /**
   * Implement this uses the nextInt(int) method of SplittableRandom.
   */
  public static class TestSplitMix1 extends TestSplitMix {
    /**
     * Create a new instance.
     *
     * @param seed the seed
     */
    TestSplitMix1(long seed) {
      super(seed);
    }

    @Override
    public int nextInt(int n) {
      if (n <= 0) {
        throw new IllegalArgumentException("Not positive: " + n);
      }
      final int nm1 = n - 1;
      if ((n & nm1) == 0) {
        // Power of 2
        return next() & nm1;
      }
      int bits;
      int val;
      do {
        bits = next() >>> 1;
        val = bits % n;
      } while (bits - val + nm1 < 0);

      return val;
    }
  }

  /**
   * Implement this uses the nextInt(int) method of Lemire (2019).
   *
   * @see <a href="https://lemire.me/blog/2016/06/30/fast-random-shuffling/">Fast random
   *      shuffling</a>
   * @see <a href="https://arxiv.org/abs/1805.10941">Fast Random Integer Generation in an
   *      Interval</a>
   */
  public static class TestSplitMix2 extends TestSplitMix {
    /**
     * Create a new instance.
     *
     * @param seed the seed
     */
    TestSplitMix2(long seed) {
      super(seed);
    }

    @Override
    public int nextInt(int n) {
      if (n <= 0) {
        throw new IllegalArgumentException("Not positive: " + n);
      }

      // Compute 64-bit unsigned product of n * [0, 2^32 - 1)
      long result = n * (next() & 0xffffffffL);
      // Uniformity is ensured by rejecting samples which are represented ceil(2^32 / n) times
      // when the range is evenly divided by n.
      // The threshold is set using 2^32 % n. This is always less than n.
      long leftover = result & 0xffffffffL;
      if (leftover < n) {
        long threshold = MAX_32 % n;
        while (leftover < threshold) {
          result = n * (next() & 0xffffffffL);
          leftover = result & 0xffffffffL;
        }
      }

      return (int) (result >>> 32);
    }
  }

  /**
   * Implement this uses the nextInt(int) method of Lemire (2019) with edge case for power of 2.
   *
   * @see <a href="https://lemire.me/blog/2016/06/30/fast-random-shuffling/">Fast random
   *      shuffling</a>
   * @see <a href="https://arxiv.org/abs/1805.10941">Fast Random Integer Generation in an
   *      Interval</a>
   */
  public static class TestSplitMix3 extends TestSplitMix {
    /**
     * Create a new instance.
     *
     * @param seed the seed
     */
    TestSplitMix3(long seed) {
      super(seed);
    }

    @Override
    public int nextInt(int n) {
      if (n <= 0) {
        throw new IllegalArgumentException("Not positive: " + n);
      }
      final int nm1 = n - 1;
      if ((n & nm1) == 0) {
        // Power of 2
        return next() & nm1;
      }

      // Compute 64-bit unsigned product of n * [0, 2^32 - 1)
      long result = n * (next() & 0xffffffffL);
      long leftover = result & 0xffffffffL;
      if (leftover < n) {
        long threshold = MAX_32 % n;
        while (leftover < threshold) {
          result = n * (next() & 0xffffffffL);
          leftover = result & 0xffffffffL;
        }
      }
      return (int) (result >>> 32);
    }
  }

  /**
   * Baseline for a JMH method call returning an {@code int}.
   *
   * @return the value
   */
  // @Benchmark
  public int baselineInt() {
    return intValue;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method.
   *
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  public int nextInt(Source source) {
    return source.getRng().nextInt();
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param range the range
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(1024)
  public int nextInt1024(IntRange range, Source source) {
    int sum = 0;
    for (int i = 0; i < 1024; i++) {
      sum += source.getRng().nextInt(range.getUpperBound());
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param range the range
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(4096)
  public int nextInt4096(IntRange range, Source source) {
    int sum = 0;
    for (int i = 0; i < 4096; i++) {
      sum += source.getRng().nextInt(range.getUpperBound());
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param range the range
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(65536)
  public int nextInt65536(IntRange range, Source source) {
    int sum = 0;
    for (int i = 0; i < 65536; i++) {
      sum += source.getRng().nextInt(range.getUpperBound());
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param range the range
   * @param source Source of randomness.
   * @return the int
   */
  @Benchmark
  @OperationsPerInvocation(262144)
  public int nextInt262144(IntRange range, Source source) {
    int sum = 0;
    for (int i = 0; i < 262144; i++) {
      sum += source.getRng().nextInt(range.getUpperBound());
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt(int)} method by shuffling data.
   *
   * @param data the data
   * @param source Source of randomness.
   * @return the shuffle data
   */
  // @Benchmark
  public int[] shuffle(IntData data, Source source) {
    final int[] array = data.getData();
    RandomUtils.shuffle(array, source.getRng());
    return array;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt(int)} method by creating random indices for
   * shuffling data.
   *
   * @param data the data
   * @param source Source of randomness.
   * @return the sum
   */
  // @Benchmark
  public int pseudoShuffle(IntData data, Source source) {
    int sum = 0;
    for (int i = data.getData().length; i > 1; i--) {
      sum += source.getRng().nextInt(i);
    }
    return sum;
  }
}
