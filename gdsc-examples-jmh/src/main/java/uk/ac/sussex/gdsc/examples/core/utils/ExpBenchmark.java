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

package uk.ac.sussex.gdsc.examples.core.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.rng.sampling.distribution.ContinuousUniformSampler;
import org.apache.commons.rng.simple.RandomSource;
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

/**
 * Executes benchmark to compare the speed of computing {@code exp(x)}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class ExpBenchmark {
  /**
   * The method to compute the exponential.
   */
  @State(Scope.Benchmark)
  public abstract static class Source {
    /** The method. */
    @Param({"baseline", "Math", "FastMath"})
    private String method;

    /** The generator to supply the next exponential. */
    private DoubleSupplier gen;

    /**
     * @return the next value
     */
    public double next() {
      return gen.getAsDouble();
    }

    /**
     * Create the function.
     */
    @Setup
    public void setup() {
      DoubleUnaryOperator fun;
      if ("baseline".equals(method)) {
        fun = x -> x;
      } else if ("Math".equals(method)) {
        fun = Math::exp;
      } else if ("FastMath".equals(method)) {
        fun = FastMath::exp;
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
      final DoubleSupplier nextValue = createValues();
      gen = () -> fun.applyAsDouble(nextValue.getAsDouble());
    }

    /**
     * Creates the values generator.
     *
     * @return the generator
     */
    protected abstract DoubleSupplier createValues();
  }

  /**
   * The method to compute the exponential from a uniform value.
   * Note: The approximate range for exp is [-746, 710]
   */
  @State(Scope.Benchmark)
  public static class UniformSource extends Source {
    /** The lower bound. */
    @Param({"0"})
    private double low;
    /** The higher bound. */
    @Param({"710", "-746"})
    private double high;

    @Override
    protected DoubleSupplier createValues() {
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), low, high)::sample;
    }
  }

  /**
   * Compute the exponential.
   *
   * @param source source of the function
   * @return the exponential
   */
  @Benchmark
  public double uniform(UniformSource source) {
    return source.next();
  }
}
