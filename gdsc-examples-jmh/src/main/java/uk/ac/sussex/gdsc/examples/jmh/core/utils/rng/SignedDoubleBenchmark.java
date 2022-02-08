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

package uk.ac.sussex.gdsc.examples.jmh.core.utils.rng;

import java.util.concurrent.TimeUnit;
import org.apache.commons.rng.UniformRandomProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import uk.ac.sussex.gdsc.core.utils.rng.NumberUtils;
import uk.ac.sussex.gdsc.core.utils.rng.XoRoShiRo128PP;

/**
 * Executes benchmark to compare the speed of producing numbers in the range {@code [-1, 1)}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class SignedDoubleBenchmark {
  /** The value. Must NOT be final to prevent JVM optimisation! */
  private double doubleValue;
  /**
   * The rng.
   * Extend the benchmark to make this configurable as the type of RNG affects
   * the speed of methods that call the rng twice.
   */
  private final UniformRandomProvider rng = new XoRoShiRo128PP(System.currentTimeMillis());

  /**
   * Baseline for a JMH method call returning an {@code double}.
   *
   * @return the value
   */
  @Benchmark
  public double baselineDouble() {
    return doubleValue;
  }

  @Benchmark
  public double nextDoubleMinus1() {
    return rng.nextDouble() - (rng.nextBoolean() ? 1.0 : 0);
  }

  @Benchmark
  public double nextDoubleMinus1LoBit() {
    final long bits = rng.nextLong();
    return (bits & 0x1L) == 0x1L ? (bits * 0x1.0p-53) - 1.0 : (bits * 0x1.0p-53);
  }

  @Benchmark
  public double nextDouble2Minus1() {
    return 2 * rng.nextDouble() - 1.0;
  }

  @Benchmark
  public double nextSignedDoubleHiBit() {
    final long bits = rng.nextLong();
    // Uses more significant bit for the sign
    return ((bits >>> 11) * 0x1.0p-53d) - ((bits >>> 10) & 0x1);
  }

  @Benchmark
  public double nextSignedDoubleLoBit() {
    final long bits = rng.nextLong();
    // Uses least significant bit for the sign
    return ((bits >>> 11) * 0x1.0p-53d) - (bits & 0x1);
  }

  @Benchmark
  public double nextSignedDoubleLoBitInt() {
    final long bits = rng.nextLong();
    // Use more significant bit for the sign with integer arithmetic
    return ((bits >>> 11) * 0x1.0p-53d) - (int) ((bits >>> 10) & 0x1);
  }

  @Benchmark
  public double nextSignedDoubleHiBitInt() {
    final long bits = rng.nextLong();
    // Use least significant bit for the sign with integer arithmetic
    return ((bits >>> 11) * 0x1.0p-53d) - ((int) bits & 0x1);
  }

  @Benchmark
  public double nextSignedDoubleRng() {
    return rng.nextDouble() - rng.nextInt(1);
  }

  @Benchmark
  public double nextSignedDouble() {
    return NumberUtils.makeSignedDouble(rng.nextLong());
  }
}
