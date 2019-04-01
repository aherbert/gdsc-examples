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

package uk.ac.sussex.gdsc.examples.core.math.interpolation;

import uk.ac.sussex.gdsc.core.utils.SimpleArrayUtils;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Executes benchmark to compare the speed of generation of Base64 strings.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms256M", "-Xmx256M"})
public class TricubicFunctionBenchmark {
  /** Number of functions per run. */
  private static final int NUM_FUNCTIONS = 100;
  /** Number of samples per run. */
  private static final int NUM_SAMPLES = 100;

  // TODO - update this so x, y and z are different because the JIT compiler may know they are the
  // same and optimise the function call.

  /** The x position. */
  private static final CubicSplinePosition[] x;
  /** Powers for the x position. */
  private static final double[][] powerX;
  /** Powers for the x position as float. */
  private static final float[][] powerXF;
  /** The cubic spline function using array data. */
  private static final DoubleCustomTricubicFunctionArray[] arrayF;
  /** The cubic spline function using float array data. */
  private static final FloatCustomTricubicFunctionArray[] arrayFF;
  /** The cubic spline function using custom object data. */
  private static final DoubleCustomTricubicFunctionData[] dataF;
  /** Power tables. */
  private static final double[][] arrayTables;
  /** Power tables. */
  private static final double[][] arrayTables2;
  /** Power tables * 2. */
  private static final double[][] arrayTables3;
  /** Power tables * 3. */
  private static final double[][] arrayTables6;
  /** Power tables * 6. */
  private static final DoubleCubicSplineData[] dataTables;
  /** Power tables * 2. */
  private static final DoubleCubicSplineData[] dataTables2;
  /** Power tables * 3. */
  private static final DoubleCubicSplineData[] dataTables3;
  /** Power tables * 6. */
  private static final DoubleCubicSplineData[] dataTables6;


  static {
    ThreadLocalRandom rng = ThreadLocalRandom.current();
    x = new CubicSplinePosition[NUM_SAMPLES];
    powerX = new double[NUM_SAMPLES][];
    powerXF = new float[NUM_SAMPLES][];
    arrayTables = new double[NUM_SAMPLES][];
    arrayTables2 = new double[NUM_SAMPLES][];
    arrayTables3 = new double[NUM_SAMPLES][];
    arrayTables6 = new double[NUM_SAMPLES][];
    dataTables = new DoubleCubicSplineData[NUM_SAMPLES];
    dataTables2 = new DoubleCubicSplineData[NUM_SAMPLES];
    dataTables3 = new DoubleCubicSplineData[NUM_SAMPLES];
    dataTables6 = new DoubleCubicSplineData[NUM_SAMPLES];
    for (int i = 0; i < NUM_SAMPLES; i++) {
      x[i] = new CubicSplinePosition(rng.nextDouble());
      powerX[i] = new double[] {x[i].x1, x[i].x2, x[i].x3};
      powerXF[i] = SimpleArrayUtils.toFloat(powerX[i]);
      double[] tmp = computePowerTable(powerX[i], powerX[i], powerX[i]);
      arrayTables[i] = tmp;
      arrayTables2[i] = multiply(tmp, 2);
      arrayTables3[i] = multiply(tmp, 3);
      arrayTables6[i] = multiply(tmp, 6);
      dataTables[i] = new DoubleCubicSplineData(x[i], x[i], x[i]);
      dataTables2[i] = dataTables[i].scale(2);
      dataTables3[i] = dataTables[i].scale(3);
      dataTables6[i] = dataTables[i].scale(6);
    }

    arrayF = new DoubleCustomTricubicFunctionArray[NUM_FUNCTIONS];
    arrayFF = new FloatCustomTricubicFunctionArray[NUM_FUNCTIONS];
    dataF = new DoubleCustomTricubicFunctionData[NUM_FUNCTIONS];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      double[] tmp = createCoefficients(rng);
      arrayF[i] = new DoubleCustomTricubicFunctionArray(tmp);
      arrayFF[i] = new FloatCustomTricubicFunctionArray(SimpleArrayUtils.toFloat(tmp));
      dataF[i] = new DoubleCustomTricubicFunctionData(new DoubleCubicSplineData(tmp));
    }
  }

  private static double[] createCoefficients(ThreadLocalRandom rng) {
    double[] tmp = new double[64];
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = rng.nextDouble(456);
    }
    return tmp;
  }

  /**
   * Compute the power table.
   *
   * @param powerX x-coordinate powers of the interpolation point.
   * @param powerY y-coordinate powers of the interpolation point.
   * @param powerZ z-coordinate powers of the interpolation point.
   * @return the power table.
   */
  private static double[] computePowerTable(final double[] powerX, final double[] powerY,
      final double[] powerZ) {
    final double[] table = new double[64];

    table[0] = 1;
    table[1] = powerX[0];
    table[2] = powerX[1];
    table[3] = powerX[2];
    table[4] = powerY[0];
    table[5] = powerY[0] * powerX[0];
    table[6] = powerY[0] * powerX[1];
    table[7] = powerY[0] * powerX[2];
    table[8] = powerY[1];
    table[9] = powerY[1] * powerX[0];
    table[10] = powerY[1] * powerX[1];
    table[11] = powerY[1] * powerX[2];
    table[12] = powerY[2];
    table[13] = powerY[2] * powerX[0];
    table[14] = powerY[2] * powerX[1];
    table[15] = powerY[2] * powerX[2];
    table[16] = powerZ[0];
    table[17] = powerZ[0] * powerX[0];
    table[18] = powerZ[0] * powerX[1];
    table[19] = powerZ[0] * powerX[2];
    table[20] = powerZ[0] * powerY[0];
    table[21] = table[20] * powerX[0];
    table[22] = table[20] * powerX[1];
    table[23] = table[20] * powerX[2];
    table[24] = powerZ[0] * powerY[1];
    table[25] = table[24] * powerX[0];
    table[26] = table[24] * powerX[1];
    table[27] = table[24] * powerX[2];
    table[28] = powerZ[0] * powerY[2];
    table[29] = table[28] * powerX[0];
    table[30] = table[28] * powerX[1];
    table[31] = table[28] * powerX[2];
    table[32] = powerZ[1];
    table[33] = powerZ[1] * powerX[0];
    table[34] = powerZ[1] * powerX[1];
    table[35] = powerZ[1] * powerX[2];
    table[36] = powerZ[1] * powerY[0];
    table[37] = table[36] * powerX[0];
    table[38] = table[36] * powerX[1];
    table[39] = table[36] * powerX[2];
    table[40] = powerZ[1] * powerY[1];
    table[41] = table[40] * powerX[0];
    table[42] = table[40] * powerX[1];
    table[43] = table[40] * powerX[2];
    table[44] = powerZ[1] * powerY[2];
    table[45] = table[44] * powerX[0];
    table[46] = table[44] * powerX[1];
    table[47] = table[44] * powerX[2];
    table[48] = powerZ[2];
    table[49] = powerZ[2] * powerX[0];
    table[50] = powerZ[2] * powerX[1];
    table[51] = powerZ[2] * powerX[2];
    table[52] = powerZ[2] * powerY[0];
    table[53] = table[52] * powerX[0];
    table[54] = table[52] * powerX[1];
    table[55] = table[52] * powerX[2];
    table[56] = powerZ[2] * powerY[1];
    table[57] = table[56] * powerX[0];
    table[58] = table[56] * powerX[1];
    table[59] = table[56] * powerX[2];
    table[60] = powerZ[2] * powerY[2];
    table[61] = table[60] * powerX[0];
    table[62] = table[60] * powerX[1];
    table[63] = table[60] * powerX[2];

    return table;
  }

  private static double[] multiply(double[] x, double factor) {
    x = x.clone();
    for (int i = 0; i < x.length; i++) {
      x[i] *= factor;
    }
    return x;
  }

  private static class Sink64 {
    long counter;

    void put(double value) {
      counter += Double.doubleToRawLongBits(value);
    }

    void put(double[] derivative) {
      put(derivative[0]);
      put(derivative[1]);
      put(derivative[2]);
    }
  }

  // Benchmarks methods below.

  // Non precomputed powers

  @Benchmark
  public void arrayValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value0(powerX[j], powerX[j], powerX[j]));
      }
    }
    bh.consume(sink.counter);
  }

  @Benchmark
  public void arrayDFValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      FloatCustomTricubicFunctionArray f = arrayFF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value0(powerX[j], powerX[j], powerX[j]));
      }
    }
    bh.consume(sink.counter);
  }

  @Benchmark
  public void arrayFFValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      FloatCustomTricubicFunctionArray f = arrayFF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value0(powerXF[j], powerXF[j], powerXF[j]));
      }
    }
    bh.consume(sink.counter);
  }

  @Benchmark
  public void dataValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value0(x[j], x[j], x[j]));
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void arrayValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value1(powerX[j], powerX[j], powerX[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value1(x[j], x[j], x[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void arrayValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value2(powerX[j], powerX[j], powerX[j], derivative1, derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value2(x[j], x[j], x[j], derivative1, derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }

  // Pre-computed powers

  //@Benchmark
  public void arrayPreValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(arrayTables[j]));
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataPreValue0(Blackhole bh) {
    final Sink64 sink = new Sink64();
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(dataTables[j]));
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void arrayPreValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(arrayTables[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataPreValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(dataTables[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void arrayPreValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(arrayTables[j], derivative1, derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataPreValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(dataTables[j], derivative1, derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }

  // Pre-computed powers with scaled tables

  //@Benchmark
  public void arrayPreScaledValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(arrayTables[j], arrayTables2[j], arrayTables3[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataPreScaledValue1(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(dataTables[j], dataTables2[j], dataTables3[j], derivative1));
        sink.put(derivative1);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void arrayPreScaledValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionArray f = arrayF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(arrayTables[j], arrayTables2[j], arrayTables3[j], arrayTables6[j],
            derivative1, derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }

  //@Benchmark
  public void dataPreScaledValue2(Blackhole bh) {
    final Sink64 sink = new Sink64();
    final double[] derivative1 = new double[3];
    final double[] derivative2 = new double[3];
    for (int i = 0; i < NUM_FUNCTIONS; i++) {
      DoubleCustomTricubicFunctionData f = dataF[i];
      for (int j = 0; j < NUM_SAMPLES; j++) {
        sink.put(f.value(dataTables[j], dataTables2[j], dataTables3[j], dataTables6[j], derivative1,
            derivative2));
        sink.put(derivative1);
        sink.put(derivative2);
      }
    }
    bh.consume(sink.counter);
  }
}
