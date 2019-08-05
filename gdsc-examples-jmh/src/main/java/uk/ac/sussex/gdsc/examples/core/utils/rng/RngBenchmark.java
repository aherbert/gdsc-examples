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

import uk.ac.sussex.gdsc.core.utils.rng.MiddleSquareWeylSequence;
import uk.ac.sussex.gdsc.core.utils.rng.Pcg32;
import uk.ac.sussex.gdsc.core.utils.rng.SplitMix;

import org.apache.commons.rng.UniformRandomProvider;
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

/**
 * Executes benchmark to compare the speed of random number generators.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class RngBenchmark {
  /** The value. Must NOT be final to prevent JVM optimisation! */
  private int intValue;

  /** The value. Must NOT be final to prevent JVM optimisation! */
  private long longValue;

  /** The value. Must NOT be final to prevent JVM optimisation! */
  private double doubleValue;

  /**
   * The source generator.
   */
  @State(Scope.Benchmark)
  public static class Source {
    @Param({"xshrs", "xshrr", "mix32", "mix64", "msws"})
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
      final Random seed = ThreadLocalRandom.current();
      if ("xshrs".equals(name)) {
        rng = Pcg32.xshrs(seed.nextLong(), seed.nextLong());
      } else if ("xshrr".equals(name)) {
        rng = Pcg32.xshrr(seed.nextLong(), seed.nextLong());
      } else if ("mix32".equals(name)) {
        rng = SplitMix.new32(seed.nextLong());
      } else if ("mix64".equals(name)) {
        rng = SplitMix.new64(seed.nextLong());
      } else if ("msws".equals(name)) {
        rng = MiddleSquareWeylSequence.newInstance(seed.nextLong());
      }
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
   * Baseline for a JMH method call returning an {@code long}.
   *
   * @return the value
   */
  // @Benchmark
  public long baselineLong() {
    return longValue;
  }

  /**
   * Baseline for a JMH method call returning an {@code double}.
   *
   * @return the value
   */
  // @Benchmark
  public double baselineDouble() {
    return doubleValue;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method.
   *
   * @param source Source of randomness.
   * @return the int
   */
  @Benchmark
  public int nextInt(Source source) {
    return source.getRng().nextInt();
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextLong()} method.
   *
   * @param source Source of randomness.
   * @return the long
   */
  @Benchmark
  public long nextLong(Source source) {
    return source.getRng().nextLong();
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextDouble()} method.
   *
   * <p>Note: There may not be any reason to benchmark this method as all implementations currently
   * generate a long and then multiply by a double. Performance of nextLong() is the critical step.
   *
   * @param source Source of randomness.
   * @return the double
   */
  // @Benchmark
  public double nextDouble(Source source) {
    return source.getRng().nextDouble();
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(1024)
  public int nextInt1024(Source source) {
    int sum = 0;
    for (int i = 0; i < 1024; i++) {
      sum += source.getRng().nextInt();
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(4096)
  public int nextInt4096(Source source) {
    int sum = 0;
    for (int i = 0; i < 4096; i++) {
      sum += source.getRng().nextInt();
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextInt()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the int
   */
  // @Benchmark
  @OperationsPerInvocation(65536)
  public int nextInt65536(Source source) {
    int sum = 0;
    for (int i = 0; i < 65536; i++) {
      sum += source.getRng().nextInt();
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextLong()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the long
   */
  // @Benchmark
  @OperationsPerInvocation(1024)
  public long nextLong1024(Source source) {
    long sum = 0;
    for (long i = 0; i < 1024; i++) {
      sum += source.getRng().nextLong();
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextLong()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the long
   */
  // @Benchmark
  @OperationsPerInvocation(4096)
  public long nextLong4096(Source source) {
    long sum = 0;
    for (long i = 0; i < 4096; i++) {
      sum += source.getRng().nextLong();
    }
    return sum;
  }

  /**
   * Exercise the {@link UniformRandomProvider#nextLong()} method in a loop.
   *
   * @param source Source of randomness.
   * @return the long
   */
  // @Benchmark
  @OperationsPerInvocation(65536)
  public long nextLong65536(Source source) {
    long sum = 0;
    for (long i = 0; i < 65536; i++) {
      sum += source.getRng().nextLong();
    }
    return sum;
  }
}
