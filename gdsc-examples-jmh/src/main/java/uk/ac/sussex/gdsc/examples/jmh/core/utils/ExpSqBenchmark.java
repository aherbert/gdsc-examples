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
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import org.apache.commons.rng.sampling.distribution.ContinuousUniformSampler;
import org.apache.commons.rng.sampling.distribution.ZigguratSampler;
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
 * Executes benchmark to compare the speed of computing {@code exp(-0.5*x*x)}.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class ExpSqBenchmark {
  /**
   * The multiplier used to split the double value into high and low parts. From Dekker (1971): "The
   * constant should be chosen equal to 2^(p - p/2) + 1, where p is the number of binary digits in
   * the mantissa". Here p is 53 and the multiplier is {@code 2^27 + 1}.
   */
  private static final double MULTIPLIER = 1.0 + 0x1.0p27;

  /**
   * The method to compute the exponential.
   */
  @State(Scope.Benchmark)
  public abstract static class Source {
    /** The method. */
    @Param({"baseline", "exp", "expmhxx"})
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
      } else if ("exp".equals(method)) {
        fun = x -> Math.exp(-0.5 * x * x);
      } else if ("expmhxx".equals(method)) {
        fun = ExpSqBenchmark::expmhxx;
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
   */
  @State(Scope.Benchmark)
  public static class UniformSource extends Source {
    /** The lower bound. */
    @Param({"0"})
    private double low;
    /** The higher bound. */
    @Param({"1", "10", "30", "100"})
    private double high;

    @Override
    protected DoubleSupplier createValues() {
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), low, high)::sample;
    }
  }

  /**
   * The method to compute the exponential from a normally distributed value.
   */
  @State(Scope.Benchmark)
  public static class NormalSource extends Source {
    @Override
    protected DoubleSupplier createValues() {
      return ZigguratSampler.NormalizedGaussian.of(RandomSource.XO_RO_SHI_RO_128_PP.create())::sample;
    }
  }

  /**
   * Compute {@code exp(-0.5*x*x)} with high accuracy. This is performed using information in the
   * round-off from {@code x*x}.
   *
   * <p>This is accurate at large x to 1 ulp until exp(-0.5*x*x) is close to sub-normal. For very
   * small exp(-0.5*x*x) the adjustment is sub-normal and bits can be lost in the adjustment for a
   * max observed error of {@code < 2} ulp.
   *
   * <p>At small x the accuracy cannot be improved over using exp(-0.5*x*x). This occurs at
   * {@code x <= sqrt(2)}.
   *
   * @param x Value
   * @return exp(-0.5*x*x)
   */
  private static double expmhxx(double x) {
    final double a = x * x;
    if (a <= 2) {
      return Math.exp(-0.5 * a);
    } else if (a > 1491) {
      return 0;
    }
    final double b = squareLowUnscaled(x, a);
    return expxx(-0.5 * a, -0.5 * b);
  }

  /**
   * Compute {@code exp(a+b)} with high accuracy assuming {@code a+b = a}.
   *
   * <p>This is accurate at large positive a to 1 ulp. If a is negative and exp(a) is close to
   * sub-normal a bit of precision may be lost when adjusting result as the adjustment is sub-normal
   * (max observed error {@code < 2} ulp). For the use case of multiplication of a number less than
   * 1 by exp(-x*x), a = -x*x, the result will be sub-normal and the rounding error is lost.
   *
   * <p>At small |a| the accuracy cannot be improved over using exp(a) as the round-off is too small
   * to create terms that can adjust the standard result by more than 0.5 ulp. This occurs at
   * {@code |a| <= 1}.
   *
   * @param a High bits of a split number
   * @param b Low bits of a split number
   * @return exp(a+b)
   */
  private static double expxx(double a, double b) {
    // exp(a+b) = exp(a) * exp(b)
    // = exp(a) * (exp(b) - 1) + exp(a)
    // Assuming:
    // 1. -746 < a < 710 for no under/overflow of exp(a)
    // 2. a+b = a
    // As b -> 0 then exp(b) -> 1; expm1(b) -> b
    // The round-off b is limited to ~ 0.5 * ulp(746) ~ 5.68e-14
    // and we can use an approximation for expm1 (x/1! + x^2/2! + ...)
    // The second term is required for the expm1 result but the
    // bits are not significant to change the product with exp(a)

    final double ea = Math.exp(a);
    // b ~ expm1(b)
    return ea * b + ea;
  }

  /**
   * Compute the low part of the double length number {@code (z,zz)} for the exact square of
   * {@code x} using Dekker's mult12 algorithm. The standard precision product {@code x*x} must be
   * provided. The number {@code x} is split into high and low parts using Dekker's algorithm.
   *
   * <p>Warning: This method does not perform scaling in Dekker's split and large finite numbers can
   * create NaN results.
   *
   * @param x Number to square
   * @param xx Standard precision product {@code x*x}
   * @return the low part of the square double length number
   */
  private static double squareLowUnscaled(double x, double xx) {
    // Split the numbers using Dekker's algorithm without scaling
    final double hx = highPartUnscaled(x);
    final double lx = x - hx;

    return squareLow(hx, lx, xx);
  }

  /**
   * Implement Dekker's method to split a value into two parts. Multiplying by (2^s + 1) creates a
   * big value from which to derive the two split parts.
   * 
   * <pre>
   * c = (2^s + 1) * a
   * a_big = c - a
   * a_hi = c - a_big
   * a_lo = a - a_hi
   * a = a_hi + a_lo
   * </pre>
   *
   * <p>The multiplicand allows a p-bit value to be split into (p-s)-bit value {@code a_hi} and a
   * non-overlapping (s-1)-bit value {@code a_lo}. Combined they have (p-1) bits of significand but
   * the sign bit of {@code a_lo} contains a bit of information. The constant is chosen so that s is
   * ceil(p/2) where the precision p for a double is 53-bits (1-bit of the mantissa is assumed to be
   * 1 for a non sub-normal number) and s is 27.
   *
   * <p>This conversion does not use scaling and the result of overflow is NaN. Overflow may occur
   * when the exponent of the input value is above 996.
   *
   * <p>Splitting a NaN or infinite value will return NaN.
   *
   * @param value Value.
   * @return the high part of the value.
   * @see Math#getExponent(double)
   */
  private static double highPartUnscaled(double value) {
    final double c = MULTIPLIER * value;
    return c - (c - value);
  }

  /**
   * Compute the low part of the double length number {@code (z,zz)} for the exact square of
   * {@code x} using Dekker's mult12 algorithm. The standard precision product {@code x*x} must be
   * provided. The number {@code x} should already be split into low and high parts.
   *
   * <p>Note: This uses the high part of the result {@code (z,zz)} as {@code x * x} and not
   * {@code hx * hx + hx * lx + lx * hx} as specified in Dekker's original paper. See Shewchuk
   * (1997) for working examples.
   *
   * @param hx High part of factor.
   * @param lx Low part of factor.
   * @param xx Square of the factor.
   * @return <code>lx * ly - (((xy - hx * hy) - lx * hy) - hx * ly)</code>
   * @see <a href="http://www-2.cs.cmu.edu/afs/cs/project/quake/public/papers/robust-arithmetic.ps">
   *      Shewchuk (1997) Theorum 18</a>
   */
  private static double squareLow(double hx, double lx, double xx) {
    // Compute the multiply low part:
    // err1 = xy - hx * hy
    // err2 = err1 - lx * hy
    // err3 = err2 - hx * ly
    // low = lx * ly - err3
    return lx * lx - ((xx - hx * hx) - 2 * lx * hx);
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

  /**
   * Compute the exponential.
   *
   * @param source source of the function
   * @return the exponential
   */
  @Benchmark
  public double normal(NormalSource source) {
    return source.next();
  }
}
