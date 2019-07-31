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
    @Param({"rxsmxs", "rxsmxsUnmix", "rrmxmx", "rrmxmxUnmix", "rrxmrrxmsx0", "rrxmrrxmsx0b",
        "murmur3", "stafford13", "pelican"})
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
      } else if ("rxsmxsUnmix".equals(name)) {
        function = Mixers::rxsmxsUnmix;
      } else if ("rrmxmx".equals(name)) {
        function = Mixers::rrmxmx;
      } else if ("rrmxmxUnmix".equals(name)) {
        function = Mixers::rrmxmxUnmix;
      } else if ("rrxmrrxmsx0".equals(name)) {
        function = Mixers::rrxmrrxmsx0;
      } else if ("rrxmrrxmsx0b".equals(name)) {
        function = MixersBenchmark::rrxmrrxmsx0;
      } else if ("murmur3".equals(name)) {
        function = Mixers::murmur3;
      } else if ("stafford13".equals(name)) {
        function = Mixers::stafford13;
      } else if ("pelican".equals(name)) {
        function = MixersBenchmark::pelican;
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
        // The unxorshift function is used in rxsmxsUnmix in the range [5,37].
        // So test powers of 2 under the limits of that interval.
        // "1", "2",
        "4", "8", "16", "32"})
    private int shift;
    @Param({"recursive", "loop", "while", "if", "if2", "if3", "if4"})
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
        function = MixersBenchmark::loopUnxorshift;
      } else if ("while".equals(name)) {
        function = MixersBenchmark::whileUnxorshift;
      } else if ("if".equals(name)) {
        function = MixersBenchmark::ifUnxorshift;
      } else if ("if2".equals(name)) {
        function = MixersBenchmark::if2Unxorshift;
      } else if ("if3".equals(name)) {
        function = MixersBenchmark::if3Unxorshift;
      } else if ("if4".equals(name)) {
        function = MixersBenchmark::if4Unxorshift;
      }
    }
  }

  /**
   * Perform the 64-bit R-R-X-M-R-R-X-M-S-X (Rotate: Rotate; Xor; Multiply; Rotate: Rotate; Xor;
   * Multiply; Shift; Xor) mix function of Pelle Evensen.
   *
   * @param x the input value
   * @return the output value
   * @see <a
   *      href="http://mostlymangling.blogspot.com/2019/01/better-stronger-mixer-and-test-procedure.html">
   *      Better, stronger mixer and a test procedure.</a>
   */
  public static long rrxmrrxmsx0(long x) {
    // Testing the removal of Long.rotateRight
    // x ^= Long.rotateRight(x, 25) ^ Long.rotateRight(x, 50)
    x ^= (x >>> 25 | x << 39) ^ (x >>> 50 | x << 14);
    x *= 0xa24baed4963ee407L;
    // x ^= Long.rotateRight(x, 24) ^ Long.rotateRight(x, 49)
    x ^= (x >>> 24 | x << 40) ^ (x >>> 49 | x << 15);
    x *= 0x9fb21c651e98df25L;
    return x ^ x >>> 28;
  }

  /**
   * Perform the 64-bit mix function of Tommy Ettinger.
   *
   * @param x the input value
   * @return the output value
   * @see <a
   *      href="https://github.com/tommyettinger/sarong/blob/master/src/main/java/sarong/PelicanRNG.java">
   *      PelicanRNG.</a>
   */
  public static long pelican(long x) {
    x = (x ^ (x << 41 | x >>> 23) ^ (x << 17 | x >>> 47) ^ 0xD1B54A32D192ED03L)
        * 0xAEF17502108EF2D9L;
    x = (x ^ x >>> 43 ^ x >>> 31 ^ x >>> 23) * 0xDB4F0B9175AE2165L;
    return (x ^ x >>> 28);
  }

  /**
   * Perform an inversion of the remaining lower bits from a xor right-shift.
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
   * Helper to perform an inversion of the remaining lower bits from a xor right-shift.
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

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long loopUnxorshift(long value, int shift) {
    // Single operation if the shift is large
    if (shift >= 32) {
      return value ^ (value >>> shift);
    }
    if (shift >= 22) {
      return value ^ (value >>> shift) ^ (value >>> 2 * shift);
    }
    if (shift >= 16) {
      return value ^ (value >>> shift) ^ (value >>> 2 * shift) ^ (value >>> 3 * shift);
    }

    // Initialise the recovered value. This will have the correct top 2n-bits set.
    long recovered = value ^ (value >>> shift);
    for (int bits = 2 * shift; bits < 64; bits *= 2) {
      recovered = recovered ^ (recovered >>> bits);
    }
    return recovered;
  }

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long whileUnxorshift(long value, int shift) {
    // See:
    // https://stackoverflow.com/questions/31521910/simplify-the-inverse-of-z-x-x-y-function/31522122#31522122
    long recovered = value;
    while (shift < 64) {
      recovered ^= (recovered >>> shift);
      shift <<= 1;
    }
    return recovered;
  }

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long ifUnxorshift(long value, int shift) {
    // Single operation if the shift is large
    if (shift >= 32) {
      return value ^ (value >>> shift);
    }
    if (shift >= 16) {
      if (shift >= 22) {
        return value ^ (value >>> shift) ^ (value >>> 2 * shift);
      }
      return value ^ (value >>> shift) ^ (value >>> 2 * shift) ^ (value >>> 3 * shift);
    }

    // Initialise the recovered value. This will have the correct top 2n-bits set.
    long recovered = value ^ (value >>> shift);
    // Use an algorithm that requires the recovered bits to be xor'd in doubling steps.
    if (shift < 2) {
      recovered = recovered ^ (recovered >>> (shift <<= 1));
    }
    if (shift < 4) {
      recovered = recovered ^ (recovered >>> (shift <<= 1));
    }
    if (shift < 8) {
      recovered = recovered ^ (recovered >>> (shift <<= 1));
    }
    // Shift is under 16. Final 2 steps are always required.
    recovered = recovered ^ (recovered >>> (shift << 1));
    recovered = recovered ^ (recovered >>> (shift << 2));
    return recovered;
  }

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long if2Unxorshift(long value, int shift) {
    // Initialise the recovered value. This will have the correct top 2n-bits set.
    long recovered = value ^ (value >>> shift);
    // Use an algorithm that requires the recovered bits to be xor'd in doubling steps.
    if (shift < 32) {
      recovered = recovered ^ (recovered >>> (shift <<= 1));
      if (shift < 32) {
        recovered = recovered ^ (recovered >>> (shift <<= 1));
        if (shift < 32) {
          recovered = recovered ^ (recovered >>> (shift <<= 1));
          if (shift < 32) {
            recovered = recovered ^ (recovered >>> (shift <<= 1));
            if (shift < 32) {
              recovered = recovered ^ (recovered >>> (shift <<= 1));
            }
          }
        }
      }
    }
    return recovered;
  }

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long if3Unxorshift(long value, int shift) {
    // Initialise the recovered value. This will have the correct top 2n-bits set.
    long recovered = value ^ (value >>> shift);
    // Use an algorithm that requires the recovered bits to be xor'd in doubling steps.
    if (shift < 32) {
      recovered ^= (recovered >>> (shift << 1));
      if (shift < 16) {
        recovered ^= (recovered >>> (shift << 2));
        if (shift < 8) {
          recovered ^= (recovered >>> (shift << 3));
          if (shift < 4) {
            recovered ^= (recovered >>> (shift << 4));
            if (shift < 2) {
              recovered ^= (recovered >>> (shift << 5));
            }
          }
        }
      }
    }
    return recovered;
  }

  /**
   * Perform an inversion of a xor right-shift.
   *
   * @param value the value
   * @param shift the shift (must be strictly positive)
   * @return the inverted value (x)
   */
  public static long if4Unxorshift(long value, int shift) {
    // Initialise the recovered value. This will have the correct top 2n-bits set.
    long recovered = value ^ (value >>> shift);
    // Use an algorithm that requires the recovered bits to be xor'd in doubling steps.
    // Binary search of values [2, 4, 8, 16, 32]
    if (shift < 8) {
      recovered ^= (recovered >>> (shift << 1));
      recovered ^= (recovered >>> (shift << 2));
      recovered ^= (recovered >>> (shift << 3));
      if (shift < 4) {
        recovered ^= (recovered >>> (shift << 4));
        if (shift < 2) {
          recovered ^= (recovered >>> (shift << 5));
        }
      }
    } else if (shift < 32) {
      recovered ^= (recovered >>> (shift << 1));
      if (shift < 16) {
        recovered ^= (recovered >>> (shift << 2));
      }
    }
    return recovered;
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
  @Benchmark
  public long unxorshift(Samples samples, UnxorshiftFunction function) {
    long sum = 0;
    for (long value : samples.getSamples()) {
      sum += function.getFunction().applyAsLong(value, function.getShift());
    }
    return sum;
  }
}
