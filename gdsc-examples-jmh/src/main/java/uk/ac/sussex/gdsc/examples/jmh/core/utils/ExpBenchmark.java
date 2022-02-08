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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;
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
   * The method to compute the exponential from a uniform value. Note: The approximate range for exp
   * is [-746, 710]
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
      return ContinuousUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), low,
          high)::sample;
    }
  }

  /**
   * Load example exponential values from file.
   */
  @State(Scope.Benchmark)
  public static class FileSource extends Source {
    /** The values. */
    private static DoubleSupplier values;

    /** The filename. */
    @Param({"ExpBenchmark.txt"})
    private String filename;

    @Override
    protected DoubleSupplier createValues() {
      DoubleSupplier v = values;
      if (v == null) {
        values = v = readValues();
      }
      return v;
    }

    private DoubleSupplier readValues() {
      try (Stream<String> lines = lines(filename)) {
        final double[] data = lines.mapToDouble(Double::parseDouble).toArray();
        final int len = data.length;
        if (len == 0) {
          throw new IllegalStateException("No exp file data: " + filename);
        }

        // Power of 2 is efficient
        if ((len & (len - 1)) == 0) {
          return new DoubleSupplier() {
            private final int mask = len - 1;
            private int i;

            @Override
            public double getAsDouble() {
              return data[i++ & mask];
            }
          };
        }

        // Non-power of 2 requires index checks
        return new DoubleSupplier() {
          private int i;

          @Override
          public double getAsDouble() {
            final int j = i++;
            if (j == data.length) {
              // Reset next position
              i = 1;
              // Return first value
              return data[0];
            }
            return data[j];
          }
        };
      }
    }

    /**
     * Get the lines from the resource file. This method opens resources that are closed if the
     * stream is closed. The returned stream should be used in try-with-resources to ensure the
     * close operation occurs.
     *
     * @return the stream
     */
    @SuppressWarnings("resource")
    private static Stream<String> lines(String filename) {
      // Load from file
      final Path path = Paths.get(filename);
      if (Files.exists(path)) {
        try {
          return Files.lines(path);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }

      // Load from class resources.
      // Prepend package name to workaround JMH generated packages
      final String packageName = ExpBenchmark.class.getPackage().getName().replace('.', '/');
      final String name = "/" + packageName + "/" + filename;
      final InputStream is = ExpBenchmark.class.getResourceAsStream(name);
      assert is != null : name;
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      try {
        // Close the reader when the stream is closed
        return br.lines().onClose(() -> {
          try {
            br.close();
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        });
      } catch (Error | RuntimeException e) {
        try {
          br.close();
        } catch (IOException ex) {
          try {
            e.addSuppressed(ex);
          } catch (Throwable ignore) {
            // Ignore
          }
        }
        throw e;
      }
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

  /**
   * Compute the exponential.
   *
   * @param source source of the function
   * @return the exponential
   */
  @Benchmark
  public double file(FileSource source) {
    return source.next();
  }
}
