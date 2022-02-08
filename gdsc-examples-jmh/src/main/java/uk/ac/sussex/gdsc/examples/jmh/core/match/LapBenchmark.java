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

package uk.ac.sussex.gdsc.examples.jmh.core.match;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import uk.ac.sussex.gdsc.core.match.JonkerVolgenantAssignment;
import uk.ac.sussex.gdsc.core.match.KuhnMunkresAssignment;

/**
 * Executes benchmark to compare the speed of Linear Assignment Problem (LAP) algorithms.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class LapBenchmark {
  /**
   * The matrix costs to analyse.
   */
  @State(Scope.Benchmark)
  public static class MatrixData {
    /**
     * Number of trials.
     */
    @Param({"10"})
    private int trials;

    /**
     * Size of matrix.
     */
    @Param({"256", "512"})
    private int rows;

    /** The costs. */
    private List<int[][]> costs;

    /**
     * Gets the costs.
     *
     * @return the costs
     */
    public List<int[][]> getCosts() {
      return costs;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      final Random rng = ThreadLocalRandom.current();
      costs = IntStream.range(0, trials).mapToObj(x -> {
        final int[][] c = new int[rows][];
        for (int i = 0; i < rows; i++) {
          c[i] = rng.ints(rows, 0, rows).toArray();
        }
        return c;
      }).collect(Collectors.toList());
    }
  }

  /**
   * The matrix costs to analyse.
   */
  @State(Scope.Benchmark)
  public abstract static class UnbalancedMatrixData {
    /**
     * Number of trials.
     */
    @Param({"10"})
    private int trials;

    /** The costs. */
    private List<int[][]> costs;

    /**
     * Gets the number of rows.
     *
     * @return the number of rows
     */
    public abstract int getRows();

    /**
     * Gets the number of columns.
     *
     * @return the number of columns
     */
    public abstract int getColumns();

    /**
     * Gets the costs.
     *
     * @return the costs
     */
    public List<int[][]> getCosts() {
      return costs;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      final Random rng = ThreadLocalRandom.current();
      costs = IntStream.range(0, trials).mapToObj(x -> {
        final int[][] c = new int[getRows()][];
        final int max = Math.max(getRows(), getColumns());
        for (int i = 0; i < getRows(); i++) {
          c[i] = rng.ints(getColumns(), 0, max).toArray();
        }
        return c;
      }).collect(Collectors.toList());
    }
  }

  /**
   * The matrix costs to analyse.
   */
  @State(Scope.Benchmark)
  public static class UnbalancedMatrixData1 extends UnbalancedMatrixData {
    /**
     * Size of matrix larger edge.
     */
    @Param({"256"})
    protected int rows;

    /**
     * Size of matrix smaller edge.
     */
    @Param({"2", "8", "16", "32", "64", "128"})
    protected int columns;

    @Override
    public int getColumns() {
      return columns;
    }

    @Override
    public int getRows() {
      return rows;
    }
  }

  /**
   * The matrix costs to analyse.
   */
  @State(Scope.Benchmark)
  public static class UnbalancedMatrixData2 extends UnbalancedMatrixData {
    /**
     * Size of matrix larger edge.
     */
    @Param({"256"})
    protected int columns;

    /**
     * Size of matrix smaller edge.
     */
    @Param({"2", "8", "16", "32", "64", "128"})
    protected int rows;

    @Override
    public int getColumns() {
      return columns;
    }

    @Override
    public int getRows() {
      return rows;
    }
  }

  /**
   * The algorithms.
   */
  @State(Scope.Benchmark)
  public static class LapAlgorithm {
    /**
     * Algorithm name.
     */
    @Param({"km", "jv"})
    private String name;

    /** The algorithm. */
    private Function<int[][], int[]> algorithm;

    /**
     * Gets the algorithm.
     *
     * @return the algorithm
     */
    public Function<int[][], int[]> getAlgorithm() {
      return algorithm;
    }

    /** Create the samples. */
    @Setup
    public void setup() {
      if ("km".equals(name)) {
        algorithm = KuhnMunkresAssignment::compute;
      } else if ("jv".equals(name)) {
        algorithm = JonkerVolgenantAssignment::compute;
      } else {
        throw new IllegalStateException("Unknown algorithm: " + name);
      }
    }
  }

  // Benchmarks methods below.

  /**
   * Benchmark a square matrix.
   *
   * @param costs the costs
   * @param bh the data sink
   * @param algorithm the algorithm
   */
  @Benchmark
  public void balanced(MatrixData costs, Blackhole bh, LapAlgorithm algorithm) {
    costs.getCosts().forEach(c -> bh.consume(algorithm.getAlgorithm().apply(c)));
  }

  /**
   * Benchmark an unbalanced rectangular matrix.
   *
   * @param costs the costs
   * @param bh the data sink
   * @param algorithm the algorithm
   */
  @Benchmark
  public void unbalanced1(UnbalancedMatrixData1 costs, Blackhole bh, LapAlgorithm algorithm) {
    costs.getCosts().forEach(c -> bh.consume(algorithm.getAlgorithm().apply(c)));
  }

  /**
   * Benchmark an unbalanced rectangular matrix.
   *
   * @param costs the costs
   * @param bh the data sink
   * @param algorithm the algorithm
   */
  @Benchmark
  public void unbalanced2(UnbalancedMatrixData2 costs, Blackhole bh, LapAlgorithm algorithm) {
    costs.getCosts().forEach(c -> bh.consume(algorithm.getAlgorithm().apply(c)));
  }
}
