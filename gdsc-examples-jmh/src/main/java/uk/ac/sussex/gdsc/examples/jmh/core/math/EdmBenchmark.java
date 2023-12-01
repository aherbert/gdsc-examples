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

package uk.ac.sussex.gdsc.examples.jmh.core.math;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Executes benchmark to compare the speed of pairwise Euclidean distance algorithms.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class EdmBenchmark {
  /**
   * The points to analyse.
   */
  @State(Scope.Benchmark)
  public static class PointData {
    /**
     * Number of points.
     */
    @Param({"10", "100", "1000"})
    private int m;
    /**
     * Size of points.
     */
    @Param({"2", "3", "10"})
    private int n;

    /** The points. */
    private double[][] points;

    /**
     * Gets the points.
     *
     * @return the points
     */
    public double[][] getPoints() {
      return points;
    }

    /** Create the samples. */
    @Setup(value = Level.Iteration)
    public void setup() {
      points = createData(m, n, new SplittableRandom());
    }

    /**
     * Creates the data.
     *
     * @param m the number of points
     * @param n the length of each point
     * @param rng the rng
     * @return the points
     */
    static double[][] createData(int m, int n, SplittableRandom rng) {
      return IntStream.range(0, m).mapToObj(i -> rng.doubles(n).toArray()).toArray(double[][]::new);
    }
  }

  /**
   * Compute the all-vs-all squared Euclidean distance matrix (EDM).
   *
   * <pre>
   * Dij = sum_n(xi[n] - xj[n]) ^ 2
   * </pre>
   *
   * <p>where xi is the vector at position i, and n is the dimension of the vector.
   *
   * @param x the x
   * @return the squared distance matrix
   */
  static double[][] edm2(double[][] x) {
    final int m = x.length;
    final double[][] d = new double[m][m];
    for (int i = 0; i < m; i++) {
      for (int j = i + 1; j < m; j++) {
        d[j][i] = distance2(x[i], x[j]);
        d[i][j] = d[j][i];
      }
    }
    return d;
  }

  /**
   * Compute the squared Euclidean distance between the two vectors.
   *
   * @param x the x
   * @param y the y
   * @return the distance
   */
  private static double distance2(double[] x, double[] y) {
    double d = 0;
    for (int i = 0; i < x.length; i++) {
      final double v = x[i] - y[i];
      d += v * v;
    }
    return d;
  }

  // Benchmarks methods below.

  /**
   * Run the an all-vs-all algorithm using a simple distance computation.
   *
   * @param points the points
   * @return the EDM
   */
  @Benchmark
  public Object simpleEdm2(PointData points) {
    return edm2(points.getPoints());
  }

  /**
   * Run the an all-vs-all algorithm using a Gram matrix computation.
   *
   * @param points the points
   * @return the EDM
   */
  @Benchmark
  public Object gramMatrixEdm2(PointData points) {
    return Edm.edm2(points.getPoints());
  }
}
