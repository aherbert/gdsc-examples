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

import uk.ac.sussex.gdsc.core.utils.rng.Mixers;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.LongUnaryOperator;

/**
 * Executes benchmark to compare the speed of generation of 64-bit mix functions.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class MixersBenchmark {
  /**
   * The samples to mix.
   */
  @State(Scope.Benchmark)
  public static class Samples {
    /**
     * Number of samples to mix.
     */
    @Param({"1024"})
    private String size;

    /** The sample values. */
    private long[] values;

    /**
     * Gets the samples.
     *
     * @return the samples
     */
    public long[] getSamples() {
      return values;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      final Random rng = ThreadLocalRandom.current();
      int count = Integer.parseInt(size);
      values = new long[count];
      for (int i = 0; i < count; i++) {
        values[i] = rng.nextLong();
      }
    }
  }

  /**
   * The mix function.
   */
  @State(Scope.Benchmark)
  public static class MixFunction {
    @Param({"rxsmxs", "rrmxmx", "rrxmrrxmsx0", "murmur3", "stafford13",})
    private String name;

    /** The function. */
    private LongUnaryOperator function;

    /**
     * Gets the funcion.
     *
     * @return the funcion
     */
    public LongUnaryOperator getFunction() {
      return function;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      if ("rxsmxs".equals(name)) {
        function = Mixers::rxsmxs;
      } else if ("rrmxmx".equals(name)) {
        function = Mixers::rrmxmx;
      } else if ("rrxmrrxmsx0".equals(name)) {
        function = Mixers::rrxmrrxmsx0;
      } else if ("murmur3".equals(name)) {
        function = Mixers::murmur3;
      } else if ("stafford13".equals(name)) {
        function = Mixers::stafford13;
      }
    }
  }

  /**
   * The Unxorshift operator.
   */
  @FunctionalInterface
  interface UnxorshiftOperator {
    /**
     * Reverse the xorshift.
     *
     * @param value the value
     * @param shift the shift
     * @return the result
     */
    long applyAsLong(long value, int shift);
  }

  /**
   * The unxorshift function.
   */
  @State(Scope.Benchmark)
  public static class UnxorshiftFunction {
    @Param({
        // The unmix function is used in rxsmxsUnmix in the range [5,37]
        "5", "13", "21", "29", "37",
        // "1", "2", "4", "8", "16", "32"
    })
    private int shift;
    @Param({"recursive", "loop"})
    private String name;

    /** The function. */
    private UnxorshiftOperator function;

    /**
     * Gets the shift.
     *
     * @return the shift
     */
    public int getShift() {
      return shift;
    }

    /**
     * Gets the function.
     *
     * @return the function
     */
    public UnxorshiftOperator getFunction() {
      return function;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      if ("recursive".equals(name)) {
        function = MixersBenchmark::recursiveUnxorshift;
      } else if ("loop".equals(name)) {
        function = Mixers::unxorshift;
      }
    }
  }

  /**
   * Perform an inversion of the remaining lower bits from a right xorshift.
   *
   * <pre>
   * value = (x >>> shift) ^ x
   * </pre>
   *
   * @param value the value
   * @param shift the shift
   * @return the inverted value (x)
   */
  private static long recursiveUnxorshift(long value, int shift) {
    return recursiveUnxorshift(value, Long.SIZE, shift);
  }

  /**
   * Helper to perform an inversion of the remaining lower bits from a right xorshift.
   *
   * <pre>
   * value = (x >>> shift) ^ x
   * </pre>
   *
   * <p>Code Copyright 2014 Melissa O'Neill from the PCG C++ source code, distributed under the
   * Apache V2 licence.
   *
   * @param value the value
   * @param bits the remaining lower bits
   * @param shift the shift
   * @return the inverted value (x)
   * @see <a href="http://www.pcg-random.org/"> PCG, A Family of Better Random Number Generators</a>
   */
  private static long recursiveUnxorshift(long value, int bits, int shift) {
    if (2 * shift >= bits) {
      return value ^ (value >>> shift);
    }
    final long lowmask1 = (1L << (bits - shift * 2)) - 1;
    final long highmask1 = ~lowmask1;
    long top1 = value;
    final long bottom1 = value & lowmask1;
    top1 ^= top1 >>> shift;
    top1 &= highmask1;
    value = top1 | bottom1;
    final long lowmask2 = (1L << (bits - shift)) - 1;
    long bottom2 = value & lowmask2;
    bottom2 = recursiveUnxorshift(bottom2, bits - shift, shift);
    bottom2 &= lowmask1;
    return top1 | bottom2;
  }

  // Benchmarks methods below.

  /**
   * Run the mixer on the samples.
   *
   * @param samples the samples
   * @param function the function
   * @return the sum
   */
  @Benchmark
  public long mix(Samples samples, MixFunction function) {
    long sum = 0;
    for (long value : samples.getSamples()) {
      sum += function.getFunction().applyAsLong(value);
    }
    return sum;
  }

  /**
   * Run the unshift operation on the samples.
   *
   * @param samples the samples
   * @param function the function
   * @return the sum
   */
  // @Benchmark
  public long unxorshift(Samples samples, UnxorshiftFunction function) {
    long sum = 0;
    for (long value : samples.getSamples()) {
      sum += function.getFunction().applyAsLong(value, function.getShift());
    }
    return sum;
  }
}
