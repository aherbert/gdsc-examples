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

import java.util.concurrent.TimeUnit;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.rng.sampling.distribution.ContinuousUniformSampler;
import org.apache.commons.rng.sampling.distribution.DiscreteUniformSampler;
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
 * Executes benchmark to compare the speed of computing {@code pow(x, y)}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class PowBenchmark {

  /**
   * Define a double-int consuming function.
   */
  interface DoubleIntOperator {
    /**
     * Applies this operator to the given operands.
     *
     * @param left the first operand
     * @param right the second operand
     * @return the operator result
     */
    double applyAsDouble(double left, int right);
  }

  /**
   * The method to compute the power function.
   */
  @State(Scope.Benchmark)
  public abstract static class Source {
    /** The method. */
    @Param({"baseline", "Math", "FastMath"})
    private String method;

    /** The generator to supply the next value. */
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
      gen = createFunction(method);
    }

    /**
     * Creates the generator function.
     *
     * @param method the method
     * @return the generator
     */
    protected abstract DoubleSupplier createFunction(String method);
  }

  /**
   * The method to compute the power function.
   */
  @State(Scope.Benchmark)
  public abstract static class DoubleDoubleSource extends Source {
    @Override
    protected DoubleSupplier createFunction(String method) {
      DoubleBinaryOperator fun;
      if ("baseline".equals(method)) {
        fun = (x, y) -> x;
      } else if ("Math".equals(method)) {
        fun = Math::pow;
      } else if ("FastMath".equals(method)) {
        fun = FastMath::pow;
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
      final DoubleSupplier nextX = createXValues();
      final DoubleSupplier nextY = createYValues();
      return () -> fun.applyAsDouble(nextX.getAsDouble(), nextY.getAsDouble());
    }

    /**
     * Creates the x-values generator.
     *
     * @return the generator
     */
    protected abstract DoubleSupplier createXValues();

    /**
     * Creates the y-values generator.
     *
     * @return the generator
     */
    protected abstract DoubleSupplier createYValues();
  }

  /**
   * The method to compute the power function from uniform values.
   */
  @State(Scope.Benchmark)
  public static class DoubleDoubleUniformSource extends DoubleDoubleSource {
    /** The lower x bound. */
    @Param({"0"})
    private double lowx;
    /** The higher x bound. */
    @Param({"10"})
    private double highx;
    /** The lower y bound. */
    @Param({"0"})
    private double lowy;
    /** The higher y bound. */
    @Param({"10"})
    private double highy;

    @Override
    protected DoubleSupplier createXValues() {
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), lowx,
          highx)::sample;
    }

    @Override
    protected DoubleSupplier createYValues() {
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), lowy,
          highy)::sample;
    }
  }

  /**
   * The method to compute the power function with an integer exponent.
   */
  @State(Scope.Benchmark)
  public abstract static class DoubleIntSource extends Source {
    @Override
    protected DoubleSupplier createFunction(String method) {
      DoubleIntOperator fun;
      if ("baseline".equals(method)) {
        fun = (x, y) -> x;
      } else if ("Math".equals(method)) {
        fun = Math::pow;
      } else if ("FastMath".equals(method)) {
        fun = FastMath::pow;
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
      final DoubleSupplier nextX = createXValues();
      final IntSupplier nextY = createYValues();
      return () -> fun.applyAsDouble(nextX.getAsDouble(), nextY.getAsInt());
    }

    /**
     * Creates the x-values generator.
     *
     * @return the generator
     */
    protected abstract DoubleSupplier createXValues();

    /**
     * Creates the y-values generator.
     *
     * @return the generator
     */
    protected abstract IntSupplier createYValues();
  }

  /**
   * The method to compute the power function from uniform values.
   */
  @State(Scope.Benchmark)
  public static class DoubleIntUniformSource extends DoubleIntSource {
    /** The lower x bound. */
    @Param({"0"})
    private double lowx;
    /** The higher x bound. */
    @Param({"10"})
    private double highx;
    /** The lower y bound. */
    @Param({"0"})
    private int lowy;
    /** The higher y bound. */
    @Param({"10"})
    private int highy;

    @Override
    protected DoubleSupplier createXValues() {
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), lowx,
          highx)::sample;
    }

    @Override
    protected IntSupplier createYValues() {
      final int a = Math.min(lowy, highy);
      final int b = Math.max(lowy, highy);
      return DiscreteUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), a, b)::sample;
    }
  }

  /**
   * Compute the power function.
   *
   * @param source source of the function
   * @return the power function
   */
  @Benchmark
  public double uniformDoubleDouble(DoubleDoubleUniformSource source) {
    return source.next();
  }

  /**
   * Compute the power function.
   *
   * @param source source of the function
   * @return the power function
   */
  @Benchmark
  public double uniformDoubleInt(DoubleIntUniformSource source) {
    return source.next();
  }
}
