/*-
 * #%L
 * Code for running JMH benchmarks to assess performance.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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

package uk.ac.sussex.gdsc.examples.jmh.core.utils;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Executes benchmark to compare the speed of computing {@code sqrt(x)}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class SqrtBenchmark {
  /**
   * The values to analyse.
   */
  @State(Scope.Benchmark)
  public static class Data {
    /**
     * Number of values.
     */
    @Param({"1000"})
    private int m;
    /**
     * Upper bound of values (power of 2).
     */
    @Param({"4", "8", "16", "31"})
    private int n;

    /** The values. */
    private int[] values;

    /**
     * Gets the values.
     *
     * @return the values
     */
    public int[] getValues() {
      return values;
    }

    /** Create the data. */
    @Setup(value = Level.Iteration)
    public void setup() {
      // Mask out bits below the power of 2
      final int mask = (1 << n) - 1;
      values = new SplittableRandom().ints(m).map(i -> i & mask).toArray();
    }
  }

  /**
   * Integer sqrt.
   *
   * <p>Note: This returns {@code x} for negative {@code x}.
   *
   * @param x the value
   * @return the root
   */
  static int sqrt1(int x) {
    // From Hacker's Delight 2.0, figure 11-1
    // sqrt via Newton's method
    if (x <= 1) {
      return x;
    }
    // This variant is slower without a hardware based instruction
    // final int s = 16 - Integer.numberOfLeadingZeros(x - 1) >>> 1;
    int s = 1;
    s = 1;
    int x1 = x - 1;
    if (x1 > 65535) {
      s = s + 8;
      x1 = x1 >> 16;
    }
    if (x1 > 255) {
      s = s + 4;
      x1 = x1 >> 8;
    }
    if (x1 > 15) {
      s = s + 2;
      x1 = x1 >> 4;
    }
    if (x1 > 3) {
      s = s + 1;
    }

    int g0 = 1 << s; // g0 = 2**s.
    int g1 = (g0 + (x >>> s)) >>> 1; // g1 = (g0 + x/g0)/2.
    // Do while approximations strictly decrease.
    while (g1 < g0) {
      g0 = g1;
      g1 = (g0 + (x / g0)) >>> 1;
    }
    return g0;
  }

  /**
   * Integer sqrt.
   *
   * <p>Note: This returns {@code x} for negative {@code x}.
   *
   * @param x the value
   * @return the root
   */
  static int sqrt3(int x) {
    // From Hacker's Delight 2.0, figure 11-3
    // sqrt via binary search
    if (x <= 1) {
      return x;
    }
    int a = 1;
    int b = (x >>> 5) + 8;
    if (b > 65535)
      b = 65535;
    do {
      int m = (a + b) >> 1;
      if (m * m > x) {
        b = m - 1;
      } else {
        a = m + 1;
      }
    } while (b >= a);
    return a - 1;
  }

  /**
   * Integer sqrt.
   *
   * <p>Note: This returns {@code x} for negative {@code x}.
   *
   * @param x the value
   * @return the root
   */
  static int sqrt4(int x) {
    // From Hacker's Delight 2.0, figure 11-4
    // sqrt via hardware algorithm
    if (x <= 1) {
      return x;
    }
    int m = 0x40000000;
    int y = 0;
    while (m != 0) {
      int b = y | m;
      y = y >> 1;
      if (x >= b) {
        x = x - b;
        y = y | m;
      }
      m = m >> 2;
    }
    return y;
  }

  // Benchmarks methods below.

  /**
   * Benchmark the integer sqrt computation.
   *
   * @param data the data
   * @return the sum
   */
  @Benchmark
  public int isqrt1(Data data) {
    int s = 0;
    for (final int i : data.getValues()) {
      s += sqrt1(i);
    }
    return s;
  }

  /**
   * Benchmark the integer sqrt computation.
   *
   * @param data the data
   * @return the sum
   */
  @Benchmark
  public int isqrt3(Data data) {
    int s = 0;
    for (final int i : data.getValues()) {
      s += sqrt3(i);
    }
    return s;
  }

  /**
   * Benchmark the integer sqrt computation.
   *
   * @param data the data
   * @return the sum
   */
  @Benchmark
  public int isqrt4(Data data) {
    int s = 0;
    for (final int i : data.getValues()) {
      s += sqrt4(i);
    }
    return s;
  }

  /**
   * Benchmark the {@link Math#sqrt(double)} computation.
   *
   * @param data the data
   * @return the sum
   */
  @Benchmark
  public int sqrt(Data data) {
    int s = 0;
    for (final int i : data.getValues()) {
      s += (int) Math.sqrt(i);
    }
    return s;
  }
}
