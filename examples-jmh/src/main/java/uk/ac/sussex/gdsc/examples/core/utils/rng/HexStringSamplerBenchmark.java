/*-
 * #%L
 * Code for running JMH benchmarks to assess performance.
 * %%
 * Copyright (C) 2018 Alex Herbert
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

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.codec.binary.Hex;
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

import uk.ac.sussex.gdsc.core.utils.rng.AsciiStringSampler;
import uk.ac.sussex.gdsc.core.utils.rng.RadixStringSampler;

/**
 * Executes benchmark to compare the speed of generation of Hex strings.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = { "-server", "-Xms128M", "-Xmx128M" })
public class HexStringSamplerBenchmark {
    /** Number of samples per run. */
    private static final int NUM_SAMPLES = 10000;

    /** The chars in a hex string. */
    private final char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Seed used to ensure the tests are the same. This can be different per
     * benchmark, but should be the same within the benchmark.
     */
    private static final int[] seed;

    /** The code point for the start of the 16 character range. */
    private static final int low;
    /** The code point for the end of the 16 character range. */
    private static final int high;

    static {
        seed = new int[128];
        UniformRandomProvider rng = RandomSource.create(RandomSource.WELL_44497_B);
        for (int i = seed.length; i-- > 0;)
            seed[i] = rng.nextInt();

        low = Character.codePointAt(new char[] { 'a' }, 0);
        high = Character.codePointAt(new char[] { 'q' }, 0);
        if (high - low != 16)
            throw new IllegalStateException("Not 16 code points");
    }

    /**
     * The benchmark state (retrieve the various "RandomSource"s).
     */
    @State(Scope.Benchmark)
    public static class Sources {
        /**
         * RNG providers. Use different speeds.
         * 
         * @see <a href=
         *      "https://commons.apache.org/proper/commons-rng/userguide/rng.html">Commons
         *      RNG user guide</a>
         */
        @Param({
                // "SPLIT_MIX_64",
                "MWC_256",
                // "KISS", "WELL_1024_A", "WELL_44497_B"
        })
        private String randomSourceName;

        /** RNG. */
        private RestorableUniformRandomProvider generator;

        /**
         * The state of the generator at the start of the test (for reproducible
         * results).
         */
        private RandomProviderState state;

        /**
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
                "1024" })
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
     * @param bh      Data sink.
     */
    private static void runSample(Supplier<String> sampler, Blackhole bh) {
        for (int i = 0; i < NUM_SAMPLES; i++) {
            bh.consume(sampler.get());
        }
    }

    // Benchmarks methods below.

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRadixStringSamplerHex(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        final RadixStringSampler s = new RadixStringSampler(r, len, 16);
        runSample(s::sample, bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    // May be slower than runRadixStringSampler due to creating the char[] each time
    @Benchmark
    public void runRadixStringSampler_nextHexString(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        runSample(() -> RadixStringSampler.nextHexString(r, len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runAsciiStringSampler(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        final AsciiStringSampler s = new AsciiStringSampler(r);
        runSample(() -> s.nextHex(len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomDataGenerator_nextHexStringModified(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        runSample(getNextHexStringModified(r, len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomDataGenerator_nextHexStringOriginal(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        runSample(getNextHexStringOriginal(r, len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomStringGeneratorWithCharArray(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        final RandomStringGenerator rss = new RandomStringGenerator.Builder().usingRandom(r::nextInt)
                // Do 16 characters
                .selectFrom(chars).build();
        runSample(() -> rss.generate(len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomStringGeneratorWith16CodePoints(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        final RandomStringGenerator rss = new RandomStringGenerator.Builder().usingRandom(r::nextInt)
                // Do 16 characters
                .withinRange(low, high).build();
        runSample(() -> rss.generate(len), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomStringUtilsWithCharArray(Sources sources, StringLength length, Blackhole bh) {
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
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runRandomStringUtilsWith16CodePoints(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        final Random random = new Random() {
            private static final long serialVersionUID = 1L;

            @Override
            public int nextInt(int max) {
                return r.nextInt(max);
            }
        };
        runSample(() -> RandomStringUtils.random(len, low, high, false, false, null, random), bh);
    }

    /**
     * @param sources Source of randomness.
     * @param length  the length
     * @param bh      Data sink.
     */
    @Benchmark
    public void runNextBytesAndHexEncodeHex(Sources sources, StringLength length, Blackhole bh) {
        final UniformRandomProvider r = sources.getGenerator();
        final int len = length.length;
        runSample(getNextBytesHexEncode(r, len), bh);
    }

    // Methods to generate strings. These are package level to allow JUnit tests.

    /**
     * Adapted from RandomDataGenerator to match the implementation of the
     * HexStringSampler. Original code is left commented out.
     *
     * @param rng    Generator of uniformly distributed random numbers.
     * @param length The length.
     * @return the random string.
     */
    static Supplier<String> getNextHexStringModified(UniformRandomProvider rng, int length) {

        // Get int(len/2)+1 random bytes
        // byte[] randomBytes = new byte[(len/2) + 1]; // ORIGINAL
        byte[] randomBytes = new byte[(length + 1) / 2];
        // Initialize output buffer
        StringBuilder outBuffer = new StringBuilder(length);

        return () -> {
            outBuffer.setLength(0);
            rng.nextBytes(randomBytes);

            // Convert each byte to 2 hex digits
            for (int i = 0; i < randomBytes.length; i++) {

                /*
                 * Add 128 to byte value to make interval 0-255 before doing hex conversion.
                 * This guarantees <= 2 hex digits from toHexString() toHexString would
                 * otherwise add 2^32 to negative arguments.
                 */
                // ORIGINAL
                // Integer c = Integer.valueOf(randomBytes[i]);
                // String hex = Integer.toHexString(c.intValue() + 128);

                String hex = Integer.toHexString(randomBytes[i] & 0xff);

                // Make sure we add 2 hex digits for each byte
                if (hex.length() == 1) {
                    outBuffer.append('0');
                }
                outBuffer.append(hex);
            }
            return outBuffer.toString().substring(0, length);
        };
    }

    /**
     * Adapted from RandomDataGenerator to use a UniformRandomProvider.
     *
     * @param rng    a random number generator
     * @param length the desired string length.
     * @return the random string.
     */
    static Supplier<String> getNextHexStringOriginal(UniformRandomProvider rng, int length) {

        // Get int(len/2)+1 random bytes
        byte[] randomBytes = new byte[(length / 2) + 1];
        // Initialize output buffer
        StringBuilder outBuffer = new StringBuilder(length);

        return () -> {
            outBuffer.setLength(0);
            rng.nextBytes(randomBytes);

            // Convert each byte to 2 hex digits
            for (int i = 0; i < randomBytes.length; i++) {
                Integer c = Integer.valueOf(randomBytes[i]);

                /*
                 * Add 128 to byte value to make interval 0-255 before doing hex conversion.
                 * This guarantees <= 2 hex digits from toHexString() toHexString would
                 * otherwise add 2^32 to negative arguments.
                 */
                String hex = Integer.toHexString(c.intValue() + 128);

                // Make sure we add 2 hex digits for each byte
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                outBuffer.append(hex);
            }
            return outBuffer.toString().substring(0, length);
        };
    }

    /**
     * Create a hex string using {@link UniformRandomProvider#nextBytes(byte[]) }
     * and {@link Hex#encodeHex(byte[], boolean)}.
     *
     * @param ran a random number generator
     * @param len the desired string length.
     * @return A hex string supplier.
     */
    static Supplier<String> getNextBytesHexEncode(UniformRandomProvider ran, int len) {
        final byte[] bytes = new byte[(len + 1) / 2];
        final boolean crop = len % 2 == 1;
        return () -> {
            ran.nextBytes(bytes);
            char[] c = Hex.encodeHex(bytes, true);
            if (crop) {
                c = Arrays.copyOf(c, len);
            }
            return new String(c);
        };
    }
}
