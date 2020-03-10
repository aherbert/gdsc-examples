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

package uk.ac.sussex.gdsc.examples.core.utils.rng;

import uk.ac.sussex.gdsc.core.utils.rng.AsciiStringSampler;
import uk.ac.sussex.gdsc.core.utils.rng.RadixStringSampler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.RandomStringGenerator;
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
import org.openjdk.jmh.infra.Blackhole;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Executes benchmark to compare the speed of generation of Base64 strings.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class Base64StringSamplerBenchmark {
  /** Number of samples per run. */
  private static final int NUM_SAMPLES = 10000;

  /** The chars in a Base64 string. */
  //@formatter:off
  private static final char[] chars = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
  };
  //@formatter:on

  /**
   * Seed used to ensure the tests are the same. This can be different per benchmark, but should be
   * the same within the benchmark.
   */
  private static final int[] seed;

  static {
    seed = new int[128];
    final UniformRandomProvider rng = RandomSource.create(RandomSource.WELL_44497_B);
    for (int i = seed.length; i-- > 0;) {
      seed[i] = rng.nextInt();
    }
  }

  /**
   * The benchmark state (retrieve the various "RandomSource"s).
   */
  @State(Scope.Benchmark)
  public static class Sources {
    /**
     * RNG providers. Use different speeds.
     *
     * @see <a href= "https://commons.apache.org/proper/commons-rng/userguide/rng.html">Commons RNG
     *      user guide</a>
     */
    @Param({
        // "MWC_256",
        "SPLIT_MIX_64",
        // "KISS", "WELL_1024_A","WELL_44497_B"
    })
    private String randomSourceName;

    /** RNG. */
    private RestorableUniformRandomProvider generator;

    /**
     * The state of the generator at the start of the test (for reproducible results).
     */
    private RandomProviderState state;

    /**
     * Gets the random generator.
     *
     * @return the RNG.
     */
    public UniformRandomProvider getGenerator() {
      generator.restoreState(state);
      return generator;
    }

    /** Instantiates generator. */
    @Setup
    public void setup() {
      final RandomSource randomSource = RandomSource.valueOf(randomSourceName);
      // Use the same seed
      generator = RandomSource.create(randomSource, seed);
      state = generator.saveState();
    }
  }

  /**
   * The string length for testing.
   */
  @State(Scope.Benchmark)
  public static class StringLength {
    @Param({
        // "1", "2", "4", "8", "16", "32"
        // "32",
        "1024"})
    private int length;

    /**
     * Gets the length.
     *
     * @return the length
     */
    public double getLength() {
      return length;
    }
  }

  /**
   * Exercises a discrete sampler.
   *
   * @param sampler Sampler.
   * @param bh Data sink.
   */
  private static void runSample(Supplier<String> sampler, Blackhole bh) {
    for (int i = 0; i < NUM_SAMPLES; i++) {
      bh.consume(sampler.get());
    }
  }

  // Benchmarks methods below.

  /**
   * Run {@link RadixStringSampler#sample()} using a radix of 16.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  // Added for a reference 8 samples per integer rather than 5 1/3 per integer
  @Benchmark
  public void runRadixStringSamplerHex(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    final RadixStringSampler s = new RadixStringSampler(r, len, 16);
    runSample(s::sample, bh);
  }

  /**
   * Run {@link RadixStringSampler#sample()} using a radix of 64.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  @Benchmark
  public void runRadixStringSamplerBase64(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    final RadixStringSampler s = new RadixStringSampler(r, len, 64);
    runSample(s::sample, bh);
  }

  /**
   * Run {@link RadixStringSampler#nextBase64String(UniformRandomProvider, int)}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  // May be slower than runRadixStringSampler due to creating the char[] each time
  @Benchmark
  public void runRadixStringSamplerNextBase64String(Sources sources, StringLength length,
      Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    runSample(() -> RadixStringSampler.nextBase64String(r, len), bh);
  }

  /**
   * Run {@link #getBase64ArrayThenNewAsciiString(UniformRandomProvider, int)}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  @Benchmark
  public void runBase64ArrayThenNewAsciiString(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    runSample(getBase64ArrayThenNewAsciiString(r, len), bh);
  }

  /**
   * Run {@link #getBase64StringUsing30of32(UniformRandomProvider, int)}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  @Benchmark
  public void runBase64StringUsing30of32(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    runSample(getBase64StringUsing30of32(r, len), bh);
  }

  /**
   * Run {@link AsciiStringSampler#nextBase64(int)}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  @Benchmark
  public void runAsciiStringSampler(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    final AsciiStringSampler s = new AsciiStringSampler(r);
    runSample(() -> s.nextBase64(len), bh);
  }

  /**
   * Run {@link RandomStringGenerator#generate(int)} from the Commons Text library using a fixed set
   * of characters in a {@code char[]}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  // This is slow due to the conversion of the char to and then from an int code
  // point for validation
  // @Benchmark
  public void runRandomStringGeneratorWithCharArray(Sources sources, StringLength length,
      Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    final RandomStringGenerator rss =
        new RandomStringGenerator.Builder().usingRandom(r::nextInt).selectFrom(chars).build();
    runSample(() -> rss.generate(len), bh);
  }

  /**
   * Run {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} from the
   * Commons Lang library using a fixed set of characters in a {@code char[]}.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  // This is slow since it does Character.charCount (which will always be 1 for
  // base64
  // and then checks flags that are set to false (so negating the check). It also
  // check input args for edge cases this call does not hit.
  // @Benchmark
  public void runRandomStringUtilsWithCharArray(Sources sources, StringLength length,
      Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    final Random random = new Random() {
      private static final long serialVersionUID = 1L;

      @Override
      public int nextInt(int max) {
        return r.nextInt(max);
      }
    };
    runSample(() -> RandomStringUtils.random(len, 0, 0, false, false, chars, random), bh);
  }

  /**
   * Run {@link #getNextBytesAndBase64Encode(UniformRandomProvider, int)} using the Commons Codec
   * library.
   *
   * @param sources Source of randomness.
   * @param length the length
   * @param bh Data sink.
   */
  // This is slow due to supporting generic base N encoding and string line length
  // wrapping.
  // It is surprising how slow it is ...
  @Benchmark
  public void runNextBytesAndBase64Encode(Sources sources, StringLength length, Blackhole bh) {
    final UniformRandomProvider r = sources.getGenerator();
    final int len = length.length;
    runSample(getNextBytesAndBase64Encode(r, len), bh);
  }

  // Methods to generate strings. These are package level to allow JUnit tests.

  /**
   * This array is a lookup table that translates 6-bit positive integer index values into their
   * "Base64 Alphabet" equivalents as specified in Table 1 of RFC 2045.
   *
   * <p>Taken from org.apache.commons.codec.binary.Base64.
   */
  //@formatter:off
  private static final byte[] BTABLE64 = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
  };
  //@formatter:on

  /**
   * Generate a random Base64 string of the given length.
   *
   * <p>The string uses MIME's Base64 table (A-Z, a-z, 0-9, +, /).
   *
   * <p>Uses the {@link String#String(byte[], java.nio.charset.Charset)} constructor to encode as
   * {@link StandardCharsets#US_ASCII}.
   *
   * @param rng Generator of uniformly distributed random numbers.
   * @param length The length.
   * @return A random Base64 string supplier.
   * @throws NegativeArraySizeException If length is negative
   * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
   */
  static Supplier<String> getBase64ArrayThenNewAsciiString(UniformRandomProvider rng, int length) {

    // -----
    // NOTE:
    // This method is as RadixStringSampler_nextBase64String but creates a byte
    // array for encoding.
    // Since the String(char[]) constructor will copy the char[] array
    // this means that this method has the overhead of creating an
    // a byte[] rather than char[] that is discarded once the string is created.
    // However this then has to be encoded by Charset.
    // -----

    // Process blocks of 6 bits as an index in the range 0-63
    // for each base64 character.
    // There are 16 samples per 3 ints (16 * 6 = 3 * 32 = 96 bits).
    final byte[] out = new byte[length];
    return () -> {
      // Run the loop without checking index i by producing characters
      // up to the size below the desired length.
      int index = 0;
      for (int loopLimit = length / 16; loopLimit-- > 0;) {
        final int i1 = rng.nextInt();
        final int i2 = rng.nextInt();
        final int i3 = rng.nextInt();
        // 0x3F == 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20
        // Extract 4 6-bit samples from the first 24 bits of each int
        out[index++] = BTABLE64[(i1 >>> 18) & 0x3F];
        out[index++] = BTABLE64[(i1 >>> 12) & 0x3F];
        out[index++] = BTABLE64[(i1 >>> 6) & 0x3F];
        out[index++] = BTABLE64[i1 & 0x3F];
        out[index++] = BTABLE64[(i2 >>> 18) & 0x3F];
        out[index++] = BTABLE64[(i2 >>> 12) & 0x3F];
        out[index++] = BTABLE64[(i2 >>> 6) & 0x3F];
        out[index++] = BTABLE64[i2 & 0x3F];
        out[index++] = BTABLE64[(i3 >>> 18) & 0x3F];
        out[index++] = BTABLE64[(i3 >>> 12) & 0x3F];
        out[index++] = BTABLE64[(i3 >>> 6) & 0x3F];
        out[index++] = BTABLE64[i3 & 0x3F];
        // Combine the remaining 8-bits from each int
        // to get 4 more samples
        final int i4 = (i1 >>> 24) | ((i2 >>> 24) << 8) | ((i3 >>> 24) << 16);
        out[index++] = BTABLE64[(i4 >>> 18) & 0x3F];
        out[index++] = BTABLE64[(i4 >>> 12) & 0x3F];
        out[index++] = BTABLE64[(i4 >>> 6) & 0x3F];
        out[index++] = BTABLE64[i4 & 0x3F];
      }
      // The final characters
      if (index < length) {
        // For simplicity there are 5 samples per int (with two unused bits).
        while (index < length) {
          int i1 = rng.nextInt();
          out[index++] = BTABLE64[i1 & 0x3F];
          for (int j = 0; j < 4 && index < length; j++) {
            i1 >>>= 6;
            out[index++] = BTABLE64[i1 & 0x3F];
          }
        }
      }
      return new String(out, 0, length, StandardCharsets.US_ASCII);
    };
  }

  /**
   * This array is a lookup table that translates 6-bit positive integer index values into their
   * "Base64 Alphabet" equivalents as specified in Table 1 of RFC 2045.
   *
   * <p>Taken from org.apache.commons.codec.binary.Base64.
   */
  //@formatter:off
  private static final char[] TABLE64 = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
  };
  //@formatter:on

  /**
   * Generate a random Base64 string of the given length.
   *
   * <p>The string uses MIME's Base64 table (A-Z, a-z, 0-9, +, /).
   *
   * <p>Performs sub-optimal sampling of nextInt() using only 30 of the 32 bits for 5 6-bit samples
   * per integer.
   *
   * @param rng Generator of uniformly distributed random numbers.
   * @param length The length.
   * @return A random Base64 string supplier.
   * @throws NegativeArraySizeException If length is negative
   * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
   */
  static Supplier<String> getBase64StringUsing30of32(UniformRandomProvider rng, int length) {

    // Process blocks of 6 bits as an index in the range 0-63
    // for each base64 character.
    // For simplicity there are 5 samples per int (with two unused bits).
    final char[] out = new char[length];
    return () -> {
      // Run the loop without checking index i by producing characters
      // up to the size below the desired length.
      int index = 0;
      for (int loopLimit = length / 5; loopLimit-- > 0;) {
        final int i1 = rng.nextInt();
        // 0x3F == 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20
        // Extract 5 6-bit samples from the first 30 bits of each int
        out[index++] = TABLE64[(i1 >>> 24) & 0x3F];
        out[index++] = TABLE64[(i1 >>> 18) & 0x3F];
        out[index++] = TABLE64[(i1 >>> 12) & 0x3F];
        out[index++] = TABLE64[(i1 >>> 6) & 0x3F];
        out[index++] = TABLE64[i1 & 0x3F];
      }
      // The final characters
      if (index < length) {
        // For simplicity there are 5 samples per int (with two unused bits).
        while (index < length) {
          int i1 = rng.nextInt();
          out[index++] = TABLE64[i1 & 0x3F];
          for (int j = 0; j < 4 && index < length; j++) {
            i1 >>>= 6;
            out[index++] = TABLE64[i1 & 0x3F];
          }
        }
      }
      return new String(out);
    };
  }

  /**
   * Create a base 64 encoded string using {@link UniformRandomProvider#nextBytes(byte[]) } and
   * {@link Base64#encode(byte[], int, int)}.
   *
   * @param rng Generator of uniformly distributed random numbers.
   * @param length The length.
   * @return A random Base64 string supplier.
   */
  static Supplier<String> getNextBytesAndBase64Encode(UniformRandomProvider rng, int length) {
    // 3 bytes to 4 letters
    final byte[] bytes = new byte[3 * (int) Math.ceil(length / 4.0)];
    final int lineLength = 0;
    final byte[] lineSeparator = null;
    final boolean urlSafe = false;
    final Base64 b64 = new Base64(lineLength, lineSeparator, urlSafe);
    return () -> {
      rng.nextBytes(bytes);
      byte[] encodedBytes = b64.encode(bytes, 0, bytes.length);
      if (encodedBytes.length > length) {
        encodedBytes = Arrays.copyOf(encodedBytes, length);
      }
      return new String(encodedBytes, StandardCharsets.US_ASCII);
    };
  }
}
