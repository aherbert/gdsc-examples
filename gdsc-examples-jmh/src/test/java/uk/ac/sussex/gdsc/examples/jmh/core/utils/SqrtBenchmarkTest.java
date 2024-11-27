package uk.ac.sussex.gdsc.examples.jmh.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class SqrtBenchmarkTest {

  static int[] values() {
    return new int[] {0, 1, 2, 3, 4, 5, 14, 15, 16, 17, 123, 1236789, 234234234};
  }

  @ParameterizedTest
  @MethodSource(value = "values")
  void testIsqrt1(int x) {
    Assertions.assertEquals((int) Math.sqrt(x), SqrtBenchmark.sqrt1(x));
  }

  @ParameterizedTest
  @MethodSource(value = "values")
  void testIsqrt3(int x) {
    Assertions.assertEquals((int) Math.sqrt(x), SqrtBenchmark.sqrt3(x));
  }

  @ParameterizedTest
  @MethodSource(value = "values")
  void testIsqrt4(int x) {
    Assertions.assertEquals((int) Math.sqrt(x), SqrtBenchmark.sqrt4(x));
  }
}
