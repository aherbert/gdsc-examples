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

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.sussex.gdsc.test.utils.TestLogging;

@SuppressWarnings("javadoc")
class Base64StringSamplerBenchmarkTest {

  private static Logger logger;

  @BeforeAll
  static void beforeAll() {
    logger = Logger.getLogger(Base64StringSamplerBenchmarkTest.class.getName());
  }

  @AfterAll
  static void afterAll() {
    logger = null;
  }

  private final int lower1 = '0';
  private final int upper1 = '9';
  private final int lower2 = 'A';
  private final int upper2 = 'Z';
  private final int offset2 = lower2 - 10;
  private final int lower3 = 'a';
  private final int upper3 = 'z';
  private final int offset3 = lower3 - 36;

  private int map(char ch) {
    if (ch >= lower1 && ch <= upper1) {
      return ch - lower1;
    }
    if (ch >= lower2 && ch <= upper2) {
      return ch - offset2;
    }
    if (ch >= lower3 && ch <= upper3) {
      return ch - offset3;
    }
    if (ch == '+') {
      return 62;
    }
    if (ch == '/') {
      return 63;
    }
    throw new AssertionError("Unsupported character: '" + ch + "'");
  }

  @Test
  void testBase64StringUsing30of32() {
    testSamplesAreUniform(Base64StringSamplerBenchmark::getBase64StringUsing30of32);
  }

  @Test
  void testBase64ArrayThenNewAsciiString() {
    testSamplesAreUniform(Base64StringSamplerBenchmark::getBase64ArrayThenNewAsciiString);
  }

  @Test
  void testNextBytesAndBase64Encode() {
    testSamplesAreUniform(Base64StringSamplerBenchmark::getNextBytesAndBase64Encode);
  }

  private void
      testSamplesAreUniform(BiFunction<UniformRandomProvider, Integer, Supplier<String>> fun) {
    final UniformRandomProvider rng = RandomSource.MWC_256.create();
    final int length = 1000;
    final int repeats = 100;
    final long[] h = new long[64];
    final Supplier<String> s = fun.apply(rng, length);
    for (int i = 0; i < repeats; i++) {
      final String string = s.get();
      Assertions.assertEquals(length, string.length());
      // logger.info(string);
      for (int j = 0; j < length; j++) {
        h[map(string.charAt(j))]++;
      }
    }

    // double mean = (double) length * repeats / radix;
    // for (int i = 0; i < h.length; i++) {
    // System.out.printf("%2d = %d (%.2f)\n", i, h[i], h[i] / mean);
    // }

    // Statistical test
    final ChiSquareTest chi = new ChiSquareTest();
    final double[] expected = new double[h.length];
    Arrays.fill(expected, 1.0 / 64);
    final double p = chi.chiSquareTest(expected, h);
    final boolean reject = p < 0.001;
    logger.log(TestLogging.getResultRecord(!reject,
        () -> String.format("chiSq p = %s  (reject=%b)", p, reject)));
  }
}
