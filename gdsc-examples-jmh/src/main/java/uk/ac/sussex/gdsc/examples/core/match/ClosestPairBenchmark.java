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

package uk.ac.sussex.gdsc.examples.core.match;

import uk.ac.sussex.gdsc.core.match.ClosestPairCalculator;

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

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Executes benchmark to compare the speed of closest point algorithms.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class ClosestPairBenchmark {
  /**
   * The points to analyse.
   */
  @State(Scope.Benchmark)
  public static class PointData {
    /**
     * Number of points.
     */
    @Param({"256", "512"})
    private int size;

    /** The points. */
    private Point2D[] points;

    /**
     * Gets the points.
     *
     * @return the points
     */
    public Point2D[] getPoints() {
      return points;
    }

    /**
     * Gets the points list.
     *
     * @return the points list
     */
    public List<Point2D> getPointsList() {
      return Arrays.asList(points);
    }

    /** Create the samples. */
    @Setup(value = Level.Iteration)
    public void setup() {
      final Random rng = ThreadLocalRandom.current();
      points = new Point2D[size];
      for (int i = 0; i < size; i++) {
        points[i] = new Point2D.Double(rng.nextDouble(), rng.nextDouble());
      }
    }
  }

  // Benchmarks methods below.

  /**
   * Run the an all-vs-all algorithm using an array.
   *
   * @param points the points
   * @return the pair
   */
  @Benchmark
  public Object allVsAllAsArray(PointData points) {
    return ClosestPairCalculator.closestPairAllVsAll(points.getPoints());
  }

  /**
   * Run the an all-vs-all algorithm using a generic list.
   *
   * @param points the points
   * @return the pair
   */
  @Benchmark
  public Object allVsAllAsList(PointData points) {
    return ClosestPairCalculator.closestPairAllVsAll(points.getPointsList(), Point2D::getX,
        Point2D::getY);
  }

  /**
   * Run the partitioned algorithm using an array.
   *
   * @param points the points
   * @return the pair
   */
  @Benchmark
  public Object partitionedAsArray(PointData points) {
    return ClosestPairCalculator.closestPairPartitioned(points.getPoints());
  }

  /**
   * Run the partitioned algorithm using a generic list.
   *
   * @param points the points
   * @return the pair
   */
  @Benchmark
  public Object partitionedAsList(PointData points) {
    return ClosestPairCalculator.closestPairPartitioned(points.getPointsList(), Point2D::getX,
        Point2D::getY);
  }
}
