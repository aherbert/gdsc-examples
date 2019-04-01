/*-
 * #%L
 * Genome Damage and Stability Centre ImageJ Core Package
 *
 * Contains code used by:
 *
 * GDSC ImageJ Plugins - Microscopy image analysis
 *
 * GDSC SMLM ImageJ Plugins - Single molecule localisation microscopy (SMLM)
 * %%
 * Copyright (C) 2011 - 2019 Alex Herbert
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

/**
 * 3D-spline function using double precision float values to store the coefficients.
 *
 * <p>This class is immutable.
 */
public class DoubleCustomTricubicFunctionData {

  /** The 64 coefficients (coeff) for the tri-cubic function. */
  private final DoubleCubicSplineData coeff;

  /**
   * Create a new instance.
   *
   * @param coefficients Spline coefficients.
   */
  DoubleCustomTricubicFunctionData(DoubleCubicSplineData coefficients) {
    this.coeff = coefficients;
  }

  public double value000() {
    return coeff.x0y0z0;
  }

  public double value000(double[] derivative1) {
    derivative1[0] = coeff.x1y0z0;
    derivative1[1] = coeff.x0y1z0;
    derivative1[2] = coeff.x0y0z1;
    return coeff.x0y0z0;
  }

  public double value000(double[] derivative1, double[] derivative2) {
    derivative1[0] = coeff.x1y0z0;
    derivative1[1] = coeff.x0y1z0;
    derivative1[2] = coeff.x0y0z1;
    derivative2[0] = 2 * coeff.x2y0z0;
    derivative2[1] = 2 * coeff.x0y2z0;
    derivative2[2] = 2 * coeff.x0y0z2;
    return coeff.x0y0z0;
  }

  // Allow the working variables for the power computation
  // to be declared at the top of the method
  // CHECKSTYLE.OFF: VariableDeclarationUsageDistance
  // CHECKSTYLE.OFF: LocalVariableName

  protected double value0(final CubicSplinePosition x, final CubicSplinePosition y,
      final CubicSplinePosition z) {
    //@formatter:off
    return               (coeff.x0y0z0 + x.x1 * coeff.x1y0z0 + x.x2 * coeff.x2y0z0 + x.x3 * coeff.x3y0z0)
                + y.x1 * (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
                + y.x2 * (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
                + y.x3 * (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
        + z.x1 * (       (coeff.x0y0z1 + x.x1 * coeff.x1y0z1 + x.x2 * coeff.x2y0z1 + x.x3 * coeff.x3y0z1)
                + y.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
                + y.x2 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
                + y.x3 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1))
        + z.x2 * (       (coeff.x0y0z2 + x.x1 * coeff.x1y0z2 + x.x2 * coeff.x2y0z2 + x.x3 * coeff.x3y0z2)
                + y.x1 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
                + y.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
                + y.x3 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2))
        + z.x3 * (       (coeff.x0y0z3 + x.x1 * coeff.x1y0z3 + x.x2 * coeff.x2y0z3 + x.x3 * coeff.x3y0z3)
                + y.x1 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
                + y.x2 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3)
                + y.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3));
    //@formatter:on
  }

  protected double value1(final CubicSplinePosition x, final CubicSplinePosition y,
      final CubicSplinePosition z, final double[] derivative1) {
    //@formatter:off
    derivative1[0] =         (coeff.x1y0z0 + y.x1 * coeff.x1y1z0 + y.x2 * coeff.x1y2z0 + y.x3 * coeff.x1y3z0)
                    + z.x1 * (coeff.x1y0z1 + y.x1 * coeff.x1y1z1 + y.x2 * coeff.x1y2z1 + y.x3 * coeff.x1y3z1)
                    + z.x2 * (coeff.x1y0z2 + y.x1 * coeff.x1y1z2 + y.x2 * coeff.x1y2z2 + y.x3 * coeff.x1y3z2)
                    + z.x3 * (coeff.x1y0z3 + y.x1 * coeff.x1y1z3 + y.x2 * coeff.x1y2z3 + y.x3 * coeff.x1y3z3)
        + 2 * x.x1 * (       (coeff.x2y0z0 + y.x1 * coeff.x2y1z0 + y.x2 * coeff.x2y2z0 + y.x3 * coeff.x2y3z0)
                    + z.x1 * (coeff.x2y0z1 + y.x1 * coeff.x2y1z1 + y.x2 * coeff.x2y2z1 + y.x3 * coeff.x2y3z1)
                    + z.x2 * (coeff.x2y0z2 + y.x1 * coeff.x2y1z2 + y.x2 * coeff.x2y2z2 + y.x3 * coeff.x2y3z2)
                    + z.x3 * (coeff.x2y0z3 + y.x1 * coeff.x2y1z3 + y.x2 * coeff.x2y2z3 + y.x3 * coeff.x2y3z3))
        + 3 * x.x2 * (       (coeff.x3y0z0 + y.x1 * coeff.x3y1z0 + y.x2 * coeff.x3y2z0 + y.x3 * coeff.x3y3z0)
                    + z.x1 * (coeff.x3y0z1 + y.x1 * coeff.x3y1z1 + y.x2 * coeff.x3y2z1 + y.x3 * coeff.x3y3z1)
                    + z.x2 * (coeff.x3y0z2 + y.x1 * coeff.x3y1z2 + y.x2 * coeff.x3y2z2 + y.x3 * coeff.x3y3z2)
                    + z.x3 * (coeff.x3y0z3 + y.x1 * coeff.x3y1z3 + y.x2 * coeff.x3y2z3 + y.x3 * coeff.x3y3z3));

    derivative1[1] =         (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
                    + z.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
                    + z.x2 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
                    + z.x3 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
        + 2 * y.x1 * (       (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
                    + z.x1 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
                    + z.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
                    + z.x3 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3))
        + 3 * y.x2 * (       (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
                    + z.x1 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1)
                    + z.x2 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2)
                    + z.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3));

    // Note: the computation for value0 is arranged using zyx so precompute the factors for z
    final double factorZ1 =
                 (coeff.x0y0z1 + x.x1 * coeff.x1y0z1 + x.x2 * coeff.x2y0z1 + x.x3 * coeff.x3y0z1)
        + y.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
        + y.x2 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
        + y.x3 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1);
    final double factorZ2 =
                 (coeff.x0y0z2 + x.x1 * coeff.x1y0z2 + x.x2 * coeff.x2y0z2 + x.x3 * coeff.x3y0z2)
        + y.x1 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
        + y.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
        + y.x3 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2);
    final double factorZ3 =
                 (coeff.x0y0z3 + x.x1 * coeff.x1y0z3 + x.x2 * coeff.x2y0z3 + x.x3 * coeff.x3y0z3)
        + y.x1 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
        + y.x2 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3)
        + y.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3);
    derivative1[2] = factorZ1
        + 2 * z.x1 * factorZ2
        + 3 * z.x2 * factorZ3;

    return               (coeff.x0y0z0 + x.x1 * coeff.x1y0z0 + x.x2 * coeff.x2y0z0 + x.x3 * coeff.x3y0z0)
                + y.x1 * (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
                + y.x2 * (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
                + y.x3 * (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
        + z.x1 * factorZ1
        + z.x2 * factorZ2
        + z.x3 * factorZ3;
    //@formatter:on
  }

  protected double value2(final CubicSplinePosition x, final CubicSplinePosition y,
      final CubicSplinePosition z, final double[] derivative1, double[] derivative2) {
    //@formatter:off
    // Pre-compute the factors for x
    //    derivative1[0] =                     (coeff.x1y0z0 + y.x1 * coeff.x1y1z0 + y.x2 * coeff.x1y2z0 + y.x3 * coeff.x1y3z0)
    //                                + z.x1 * (coeff.x1y0z1 + y.x1 * coeff.x1y1z1 + y.x2 * coeff.x1y2z1 + y.x3 * coeff.x1y3z1)
    //                                + z.x2 * (coeff.x1y0z2 + y.x1 * coeff.x1y1z2 + y.x2 * coeff.x1y2z2 + y.x3 * coeff.x1y3z2)
    //                                + z.x3 * (coeff.x1y0z3 + y.x1 * coeff.x1y1z3 + y.x2 * coeff.x1y2z3 + y.x3 * coeff.x1y3z3)
    //                    + 2 * x.x1 * (       (coeff.x2y0z0 + y.x1 * coeff.x2y1z0 + y.x2 * coeff.x2y2z0 + y.x3 * coeff.x2y3z0)
    //                                + z.x1 * (coeff.x2y0z1 + y.x1 * coeff.x2y1z1 + y.x2 * coeff.x2y2z1 + y.x3 * coeff.x2y3z1)
    //                                + z.x2 * (coeff.x2y0z2 + y.x1 * coeff.x2y1z2 + y.x2 * coeff.x2y2z2 + y.x3 * coeff.x2y3z2)
    //                                + z.x3 * (coeff.x2y0z3 + y.x1 * coeff.x2y1z3 + y.x2 * coeff.x2y2z3 + y.x3 * coeff.x2y3z3))
    //                    + 3 * x.x2 * (       (coeff.x3y0z0 + y.x1 * coeff.x3y1z0 + y.x2 * coeff.x3y2z0 + y.x3 * coeff.x3y3z0)
    //                                + z.x1 * (coeff.x3y0z1 + y.x1 * coeff.x3y1z1 + y.x2 * coeff.x3y2z1 + y.x3 * coeff.x3y3z1)
    //                                + z.x2 * (coeff.x3y0z2 + y.x1 * coeff.x3y1z2 + y.x2 * coeff.x3y2z2 + y.x3 * coeff.x3y3z2)
    //                                + z.x3 * (coeff.x3y0z3 + y.x1 * coeff.x3y1z3 + y.x2 * coeff.x3y2z3 + y.x3 * coeff.x3y3z3));
    final double factorX1 =
                 (coeff.x1y0z0 + y.x1 * coeff.x1y1z0 + y.x2 * coeff.x1y2z0 + y.x3 * coeff.x1y3z0)
        + z.x1 * (coeff.x1y0z1 + y.x1 * coeff.x1y1z1 + y.x2 * coeff.x1y2z1 + y.x3 * coeff.x1y3z1)
        + z.x2 * (coeff.x1y0z2 + y.x1 * coeff.x1y1z2 + y.x2 * coeff.x1y2z2 + y.x3 * coeff.x1y3z2)
        + z.x3 * (coeff.x1y0z3 + y.x1 * coeff.x1y1z3 + y.x2 * coeff.x1y2z3 + y.x3 * coeff.x1y3z3);
    final double factorX2 =
                 (coeff.x2y0z0 + y.x1 * coeff.x2y1z0 + y.x2 * coeff.x2y2z0 + y.x3 * coeff.x2y3z0)
        + z.x1 * (coeff.x2y0z1 + y.x1 * coeff.x2y1z1 + y.x2 * coeff.x2y2z1 + y.x3 * coeff.x2y3z1)
        + z.x2 * (coeff.x2y0z2 + y.x1 * coeff.x2y1z2 + y.x2 * coeff.x2y2z2 + y.x3 * coeff.x2y3z2)
        + z.x3 * (coeff.x2y0z3 + y.x1 * coeff.x2y1z3 + y.x2 * coeff.x2y2z3 + y.x3 * coeff.x2y3z3);
    final double factorX3 =
                 (coeff.x3y0z0 + y.x1 * coeff.x3y1z0 + y.x2 * coeff.x3y2z0 + y.x3 * coeff.x3y3z0)
        + z.x1 * (coeff.x3y0z1 + y.x1 * coeff.x3y1z1 + y.x2 * coeff.x3y2z1 + y.x3 * coeff.x3y3z1)
        + z.x2 * (coeff.x3y0z2 + y.x1 * coeff.x3y1z2 + y.x2 * coeff.x3y2z2 + y.x3 * coeff.x3y3z2)
        + z.x3 * (coeff.x3y0z3 + y.x1 * coeff.x3y1z3 + y.x2 * coeff.x3y2z3 + y.x3 * coeff.x3y3z3);
    derivative1[2] = factorX1
        + 2 * x.x1 * factorX2
        + 3 * x.x2 * factorX3;
    derivative2[2] =
          2 *        factorX2
        + 6 * x.x1 * factorX3;

    // Pre-compute the factors for y
    //    derivative1[1] =                     (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
    //                                + z.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
    //                                + z.x2 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
    //                                + z.x3 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
    //                    + 2 * y.x1 * (       (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
    //                                + z.x1 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
    //                                + z.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
    //                                + z.x3 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3))
    //                    + 3 * y.x2 * (       (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
    //                                + z.x1 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1)
    //                                + z.x2 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2)
    //                                + z.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3));

    final double factorY1 =
                 (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
        + z.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
        + z.x2 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
        + z.x3 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3);
    final double factorY2 =
                 (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
        + z.x1 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
        + z.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
        + z.x3 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3);
    final double factorY3 =
                 (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
        + z.x1 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1)
        + z.x2 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2)
        + z.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3);
    derivative1[2] = factorY1
        + 2 * y.x1 * factorY2
        + 3 * y.x2 * factorY3;
    derivative2[2] =
          2 *        factorY2
        + 6 * y.x1 * factorY3;

    //derivative1[2]  =                    (coeff.x0y0z1 + x.x1 * coeff.x1y0z1 + x.x2 * coeff.x2y0z1 + x.x3 * coeff.x3y0z1)
    //                            + y.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
    //                            + y.x2 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
    //                            + y.x3 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1)
    //                + 2 * z.x1 * (       (coeff.x0y0z2 + x.x1 * coeff.x1y0z2 + x.x2 * coeff.x2y0z2 + x.x3 * coeff.x3y0z2)
    //                            + y.x1 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
    //                            + y.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
    //                            + y.x3 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2))
    //                + 3 * z.x2 * (       (coeff.x0y0z3 + x.x1 * coeff.x1y0z3 + x.x2 * coeff.x2y0z3 + x.x3 * coeff.x3y0z3)
    //                            + y.x1 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
    //                            + y.x2 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3)
    //                            + y.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3));

    // Pre-compute the factors for z
    final double factorZ1 =          (coeff.x0y0z1 + x.x1 * coeff.x1y0z1 + x.x2 * coeff.x2y0z1 + x.x3 * coeff.x3y0z1)
                            + y.x1 * (coeff.x0y1z1 + x.x1 * coeff.x1y1z1 + x.x2 * coeff.x2y1z1 + x.x3 * coeff.x3y1z1)
                            + y.x2 * (coeff.x0y2z1 + x.x1 * coeff.x1y2z1 + x.x2 * coeff.x2y2z1 + x.x3 * coeff.x3y2z1)
                            + y.x3 * (coeff.x0y3z1 + x.x1 * coeff.x1y3z1 + x.x2 * coeff.x2y3z1 + x.x3 * coeff.x3y3z1);
    final double factorZ2 =          (coeff.x0y0z2 + x.x1 * coeff.x1y0z2 + x.x2 * coeff.x2y0z2 + x.x3 * coeff.x3y0z2)
                            + y.x1 * (coeff.x0y1z2 + x.x1 * coeff.x1y1z2 + x.x2 * coeff.x2y1z2 + x.x3 * coeff.x3y1z2)
                            + y.x2 * (coeff.x0y2z2 + x.x1 * coeff.x1y2z2 + x.x2 * coeff.x2y2z2 + x.x3 * coeff.x3y2z2)
                            + y.x3 * (coeff.x0y3z2 + x.x1 * coeff.x1y3z2 + x.x2 * coeff.x2y3z2 + x.x3 * coeff.x3y3z2);
    final double factorZ3 =          (coeff.x0y0z3 + x.x1 * coeff.x1y0z3 + x.x2 * coeff.x2y0z3 + x.x3 * coeff.x3y0z3)
                            + y.x1 * (coeff.x0y1z3 + x.x1 * coeff.x1y1z3 + x.x2 * coeff.x2y1z3 + x.x3 * coeff.x3y1z3)
                            + y.x2 * (coeff.x0y2z3 + x.x1 * coeff.x1y2z3 + x.x2 * coeff.x2y2z3 + x.x3 * coeff.x3y2z3)
                            + y.x3 * (coeff.x0y3z3 + x.x1 * coeff.x1y3z3 + x.x2 * coeff.x2y3z3 + x.x3 * coeff.x3y3z3);
    derivative1[2] = factorZ1
        + 2 * z.x1 * factorZ2
        + 3 * z.x2 * factorZ3;
    derivative2[2] =
          2 *        factorZ2
        + 6 * z.x1 * factorZ3;

    // Note: The computation for value0 is arranged using zyx so reuse the factors for z
    // and compute the remaining polynomial.
    return               (coeff.x0y0z0 + x.x1 * coeff.x1y0z0 + x.x2 * coeff.x2y0z0 + x.x3 * coeff.x3y0z0)
                + y.x1 * (coeff.x0y1z0 + x.x1 * coeff.x1y1z0 + x.x2 * coeff.x2y1z0 + x.x3 * coeff.x3y1z0)
                + y.x2 * (coeff.x0y2z0 + x.x1 * coeff.x1y2z0 + x.x2 * coeff.x2y2z0 + x.x3 * coeff.x3y2z0)
                + y.x3 * (coeff.x0y3z0 + x.x1 * coeff.x1y3z0 + x.x2 * coeff.x2y3z0 + x.x3 * coeff.x3y3z0)
        + z.x1 * factorZ1
        + z.x2 * factorZ2
        + z.x3 * factorZ3;
    //@formatter:on
  }

  // CHECKSTYLE.ON: VariableDeclarationUsageDistance

  public double value(DoubleCubicSplineData table) {
    return coeff.x0y0z0
        +  table.x1y0z0 * coeff.x1y0z0
        +  table.x2y0z0 * coeff.x2y0z0
        +  table.x3y0z0 * coeff.x3y0z0
        +  table.x0y1z0 * (coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0 + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0)
        +  table.x0y2z0 * (coeff.x0y2z0 + table.x1y0z0 * coeff.x1y2z0 + table.x2y0z0 * coeff.x2y2z0 + table.x3y0z0 * coeff.x3y2z0)
        +  table.x0y3z0 * (coeff.x0y3z0 + table.x1y0z0 * coeff.x1y3z0 + table.x2y0z0 * coeff.x2y3z0 + table.x3y0z0 * coeff.x3y3z0)
        +  table.x0y0z1 * (coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1 + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1)
        +  table.x0y1z1 * (coeff.x0y1z1 + table.x1y0z0 * coeff.x1y1z1 + table.x2y0z0 * coeff.x2y1z1 + table.x3y0z0 * coeff.x3y1z1)
        +  table.x0y2z1 * (coeff.x0y2z1 + table.x1y0z0 * coeff.x1y2z1 + table.x2y0z0 * coeff.x2y2z1 + table.x3y0z0 * coeff.x3y2z1)
        +  table.x0y3z1 * (coeff.x0y3z1 + table.x1y0z0 * coeff.x1y3z1 + table.x2y0z0 * coeff.x2y3z1 + table.x3y0z0 * coeff.x3y3z1)
        +  table.x0y0z2 * (coeff.x0y0z2 + table.x1y0z0 * coeff.x1y0z2 + table.x2y0z0 * coeff.x2y0z2 + table.x3y0z0 * coeff.x3y0z2)
        +  table.x0y1z2 * (coeff.x0y1z2 + table.x1y0z0 * coeff.x1y1z2 + table.x2y0z0 * coeff.x2y1z2 + table.x3y0z0 * coeff.x3y1z2)
        +  table.x0y2z2 * (coeff.x0y2z2 + table.x1y0z0 * coeff.x1y2z2 + table.x2y0z0 * coeff.x2y2z2 + table.x3y0z0 * coeff.x3y2z2)
        +  table.x0y3z2 * (coeff.x0y3z2 + table.x1y0z0 * coeff.x1y3z2 + table.x2y0z0 * coeff.x2y3z2 + table.x3y0z0 * coeff.x3y3z2)
        +  table.x0y0z3 * (coeff.x0y0z3 + table.x1y0z0 * coeff.x1y0z3 + table.x2y0z0 * coeff.x2y0z3 + table.x3y0z0 * coeff.x3y0z3)
        +  table.x0y1z3 * (coeff.x0y1z3 + table.x1y0z0 * coeff.x1y1z3 + table.x2y0z0 * coeff.x2y1z3 + table.x3y0z0 * coeff.x3y1z3)
        +  table.x0y2z3 * (coeff.x0y2z3 + table.x1y0z0 * coeff.x1y2z3 + table.x2y0z0 * coeff.x2y2z3 + table.x3y0z0 * coeff.x3y2z3)
        +  table.x0y3z3 * (coeff.x0y3z3 + table.x1y0z0 * coeff.x1y3z3 + table.x2y0z0 * coeff.x2y3z3 + table.x3y0z0 * coeff.x3y3z3);
  }

  public double value(FloatCubicSplineData table) {
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(DoubleCubicSplineData table, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(FloatCubicSplineData table, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(DoubleCubicSplineData table, DoubleCubicSplineData table2,
      DoubleCubicSplineData table3, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + table2.x1y0z0 * coeff.x2y0z0
        + table3.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0 + table2.x1y1z0 * coeff.x2y1z0
        + table3.x2y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x1y2z0 + table2.x1y2z0 * coeff.x2y2z0
        + table3.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0 + table2.x1y3z0 * coeff.x2y3z0
        + table3.x2y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x1y0z1 + table2.x1y0z1 * coeff.x2y0z1
        + table3.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1 + table2.x1y1z1 * coeff.x2y1z1
        + table3.x2y1z1 * coeff.x3y1z1 + table.x0y2z1 * coeff.x1y2z1 + table2.x1y2z1 * coeff.x2y2z1
        + table3.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1 + table2.x1y3z1 * coeff.x2y3z1
        + table3.x2y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x1y0z2 + table2.x1y0z2 * coeff.x2y0z2
        + table3.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2 + table2.x1y1z2 * coeff.x2y1z2
        + table3.x2y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x1y2z2 + table2.x1y2z2 * coeff.x2y2z2
        + table3.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2 + table2.x1y3z2 * coeff.x2y3z2
        + table3.x2y3z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x1y0z3 + table2.x1y0z3 * coeff.x2y0z3
        + table3.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3 + table2.x1y1z3 * coeff.x2y1z3
        + table3.x2y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x1y2z3 + table2.x1y2z3 * coeff.x2y2z3
        + table3.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3 + table2.x1y3z3 * coeff.x2y3z3
        + table3.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0 + table2.x0y1z0 * coeff.x0y2z0
        + table2.x1y1z0 * coeff.x1y2z0 + table2.x2y1z0 * coeff.x2y2z0 + table2.x3y1z0 * coeff.x3y2z0
        + table3.x0y2z0 * coeff.x0y3z0 + table3.x1y2z0 * coeff.x1y3z0 + table3.x2y2z0 * coeff.x2y3z0
        + table3.x3y2z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1
        + table.x2y0z1 * coeff.x2y1z1 + table.x3y0z1 * coeff.x3y1z1 + table2.x0y1z1 * coeff.x0y2z1
        + table2.x1y1z1 * coeff.x1y2z1 + table2.x2y1z1 * coeff.x2y2z1 + table2.x3y1z1 * coeff.x3y2z1
        + table3.x0y2z1 * coeff.x0y3z1 + table3.x1y2z1 * coeff.x1y3z1 + table3.x2y2z1 * coeff.x2y3z1
        + table3.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2 + table.x1y0z2 * coeff.x1y1z2
        + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2 + table2.x0y1z2 * coeff.x0y2z2
        + table2.x1y1z2 * coeff.x1y2z2 + table2.x2y1z2 * coeff.x2y2z2 + table2.x3y1z2 * coeff.x3y2z2
        + table3.x0y2z2 * coeff.x0y3z2 + table3.x1y2z2 * coeff.x1y3z2 + table3.x2y2z2 * coeff.x2y3z2
        + table3.x3y2z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3
        + table.x2y0z3 * coeff.x2y1z3 + table.x3y0z3 * coeff.x3y1z3 + table2.x0y1z3 * coeff.x0y2z3
        + table2.x1y1z3 * coeff.x1y2z3 + table2.x2y1z3 * coeff.x2y2z3 + table2.x3y1z3 * coeff.x3y2z3
        + table3.x0y2z3 * coeff.x0y3z3 + table3.x1y2z3 * coeff.x1y3z3 + table3.x2y2z3 * coeff.x2y3z3
        + table3.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1 + table2.x0y0z1 * coeff.x0y0z2
        + table2.x1y0z1 * coeff.x1y0z2 + table2.x2y0z1 * coeff.x2y0z2 + table2.x3y0z1 * coeff.x3y0z2
        + table2.x0y1z1 * coeff.x0y1z2 + table2.x1y1z1 * coeff.x1y1z2 + table2.x2y1z1 * coeff.x2y1z2
        + table2.x3y1z1 * coeff.x3y1z2 + table2.x0y2z1 * coeff.x0y2z2 + table2.x1y2z1 * coeff.x1y2z2
        + table2.x2y2z1 * coeff.x2y2z2 + table2.x3y2z1 * coeff.x3y2z2 + table2.x0y3z1 * coeff.x0y3z2
        + table2.x1y3z1 * coeff.x1y3z2 + table2.x2y3z1 * coeff.x2y3z2 + table2.x3y3z1 * coeff.x3y3z2
        + table3.x0y0z2 * coeff.x0y0z3 + table3.x1y0z2 * coeff.x1y0z3 + table3.x2y0z2 * coeff.x2y0z3
        + table3.x3y0z2 * coeff.x3y0z3 + table3.x0y1z2 * coeff.x0y1z3 + table3.x1y1z2 * coeff.x1y1z3
        + table3.x2y1z2 * coeff.x2y1z3 + table3.x3y1z2 * coeff.x3y1z3 + table3.x0y2z2 * coeff.x0y2z3
        + table3.x1y2z2 * coeff.x1y2z3 + table3.x2y2z2 * coeff.x2y2z3 + table3.x3y2z2 * coeff.x3y2z3
        + table3.x0y3z2 * coeff.x0y3z3 + table3.x1y3z2 * coeff.x1y3z3 + table3.x2y3z2 * coeff.x2y3z3
        + table3.x3y3z2 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(FloatCubicSplineData table, FloatCubicSplineData table2,
      FloatCubicSplineData table3, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + table2.x1y0z0 * coeff.x2y0z0
        + table3.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0 + table2.x1y1z0 * coeff.x2y1z0
        + table3.x2y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x1y2z0 + table2.x1y2z0 * coeff.x2y2z0
        + table3.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0 + table2.x1y3z0 * coeff.x2y3z0
        + table3.x2y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x1y0z1 + table2.x1y0z1 * coeff.x2y0z1
        + table3.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1 + table2.x1y1z1 * coeff.x2y1z1
        + table3.x2y1z1 * coeff.x3y1z1 + table.x0y2z1 * coeff.x1y2z1 + table2.x1y2z1 * coeff.x2y2z1
        + table3.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1 + table2.x1y3z1 * coeff.x2y3z1
        + table3.x2y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x1y0z2 + table2.x1y0z2 * coeff.x2y0z2
        + table3.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2 + table2.x1y1z2 * coeff.x2y1z2
        + table3.x2y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x1y2z2 + table2.x1y2z2 * coeff.x2y2z2
        + table3.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2 + table2.x1y3z2 * coeff.x2y3z2
        + table3.x2y3z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x1y0z3 + table2.x1y0z3 * coeff.x2y0z3
        + table3.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3 + table2.x1y1z3 * coeff.x2y1z3
        + table3.x2y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x1y2z3 + table2.x1y2z3 * coeff.x2y2z3
        + table3.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3 + table2.x1y3z3 * coeff.x2y3z3
        + table3.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0 + table2.x0y1z0 * coeff.x0y2z0
        + table2.x1y1z0 * coeff.x1y2z0 + table2.x2y1z0 * coeff.x2y2z0 + table2.x3y1z0 * coeff.x3y2z0
        + table3.x0y2z0 * coeff.x0y3z0 + table3.x1y2z0 * coeff.x1y3z0 + table3.x2y2z0 * coeff.x2y3z0
        + table3.x3y2z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1
        + table.x2y0z1 * coeff.x2y1z1 + table.x3y0z1 * coeff.x3y1z1 + table2.x0y1z1 * coeff.x0y2z1
        + table2.x1y1z1 * coeff.x1y2z1 + table2.x2y1z1 * coeff.x2y2z1 + table2.x3y1z1 * coeff.x3y2z1
        + table3.x0y2z1 * coeff.x0y3z1 + table3.x1y2z1 * coeff.x1y3z1 + table3.x2y2z1 * coeff.x2y3z1
        + table3.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2 + table.x1y0z2 * coeff.x1y1z2
        + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2 + table2.x0y1z2 * coeff.x0y2z2
        + table2.x1y1z2 * coeff.x1y2z2 + table2.x2y1z2 * coeff.x2y2z2 + table2.x3y1z2 * coeff.x3y2z2
        + table3.x0y2z2 * coeff.x0y3z2 + table3.x1y2z2 * coeff.x1y3z2 + table3.x2y2z2 * coeff.x2y3z2
        + table3.x3y2z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3
        + table.x2y0z3 * coeff.x2y1z3 + table.x3y0z3 * coeff.x3y1z3 + table2.x0y1z3 * coeff.x0y2z3
        + table2.x1y1z3 * coeff.x1y2z3 + table2.x2y1z3 * coeff.x2y2z3 + table2.x3y1z3 * coeff.x3y2z3
        + table3.x0y2z3 * coeff.x0y3z3 + table3.x1y2z3 * coeff.x1y3z3 + table3.x2y2z3 * coeff.x2y3z3
        + table3.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1 + table2.x0y0z1 * coeff.x0y0z2
        + table2.x1y0z1 * coeff.x1y0z2 + table2.x2y0z1 * coeff.x2y0z2 + table2.x3y0z1 * coeff.x3y0z2
        + table2.x0y1z1 * coeff.x0y1z2 + table2.x1y1z1 * coeff.x1y1z2 + table2.x2y1z1 * coeff.x2y1z2
        + table2.x3y1z1 * coeff.x3y1z2 + table2.x0y2z1 * coeff.x0y2z2 + table2.x1y2z1 * coeff.x1y2z2
        + table2.x2y2z1 * coeff.x2y2z2 + table2.x3y2z1 * coeff.x3y2z2 + table2.x0y3z1 * coeff.x0y3z2
        + table2.x1y3z1 * coeff.x1y3z2 + table2.x2y3z1 * coeff.x2y3z2 + table2.x3y3z1 * coeff.x3y3z2
        + table3.x0y0z2 * coeff.x0y0z3 + table3.x1y0z2 * coeff.x1y0z3 + table3.x2y0z2 * coeff.x2y0z3
        + table3.x3y0z2 * coeff.x3y0z3 + table3.x0y1z2 * coeff.x0y1z3 + table3.x1y1z2 * coeff.x1y1z3
        + table3.x2y1z2 * coeff.x2y1z3 + table3.x3y1z2 * coeff.x3y1z3 + table3.x0y2z2 * coeff.x0y2z3
        + table3.x1y2z2 * coeff.x1y2z3 + table3.x2y2z2 * coeff.x2y2z3 + table3.x3y2z2 * coeff.x3y2z3
        + table3.x0y3z2 * coeff.x0y3z3 + table3.x1y3z2 * coeff.x1y3z3 + table3.x2y3z2 * coeff.x2y3z3
        + table3.x3y3z2 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(DoubleCubicSplineData table, double[] derivative1, double[] derivative2) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
    derivative2[0] = 2 * table.x0y0z0 * coeff.x2y0z0 + 6 * table.x1y0z0 * coeff.x3y0z0
        + 2 * table.x0y1z0 * coeff.x2y1z0 + 6 * table.x1y1z0 * coeff.x3y1z0
        + 2 * table.x0y2z0 * coeff.x2y2z0 + 6 * table.x1y2z0 * coeff.x3y2z0
        + 2 * table.x0y3z0 * coeff.x2y3z0 + 6 * table.x1y3z0 * coeff.x3y3z0
        + 2 * table.x0y0z1 * coeff.x2y0z1 + 6 * table.x1y0z1 * coeff.x3y0z1
        + 2 * table.x0y1z1 * coeff.x2y1z1 + 6 * table.x1y1z1 * coeff.x3y1z1
        + 2 * table.x0y2z1 * coeff.x2y2z1 + 6 * table.x1y2z1 * coeff.x3y2z1
        + 2 * table.x0y3z1 * coeff.x2y3z1 + 6 * table.x1y3z1 * coeff.x3y3z1
        + 2 * table.x0y0z2 * coeff.x2y0z2 + 6 * table.x1y0z2 * coeff.x3y0z2
        + 2 * table.x0y1z2 * coeff.x2y1z2 + 6 * table.x1y1z2 * coeff.x3y1z2
        + 2 * table.x0y2z2 * coeff.x2y2z2 + 6 * table.x1y2z2 * coeff.x3y2z2
        + 2 * table.x0y3z2 * coeff.x2y3z2 + 6 * table.x1y3z2 * coeff.x3y3z2
        + 2 * table.x0y0z3 * coeff.x2y0z3 + 6 * table.x1y0z3 * coeff.x3y0z3
        + 2 * table.x0y1z3 * coeff.x2y1z3 + 6 * table.x1y1z3 * coeff.x3y1z3
        + 2 * table.x0y2z3 * coeff.x2y2z3 + 6 * table.x1y2z3 * coeff.x3y2z3
        + 2 * table.x0y3z3 * coeff.x2y3z3 + 6 * table.x1y3z3 * coeff.x3y3z3;
    derivative2[1] = 2 * table.x0y0z0 * coeff.x0y2z0 + 2 * table.x1y0z0 * coeff.x1y2z0
        + 2 * table.x2y0z0 * coeff.x2y2z0 + 2 * table.x3y0z0 * coeff.x3y2z0
        + 6 * table.x0y1z0 * coeff.x0y3z0 + 6 * table.x1y1z0 * coeff.x1y3z0
        + 6 * table.x2y1z0 * coeff.x2y3z0 + 6 * table.x3y1z0 * coeff.x3y3z0
        + 2 * table.x0y0z1 * coeff.x0y2z1 + 2 * table.x1y0z1 * coeff.x1y2z1
        + 2 * table.x2y0z1 * coeff.x2y2z1 + 2 * table.x3y0z1 * coeff.x3y2z1
        + 6 * table.x0y1z1 * coeff.x0y3z1 + 6 * table.x1y1z1 * coeff.x1y3z1
        + 6 * table.x2y1z1 * coeff.x2y3z1 + 6 * table.x3y1z1 * coeff.x3y3z1
        + 2 * table.x0y0z2 * coeff.x0y2z2 + 2 * table.x1y0z2 * coeff.x1y2z2
        + 2 * table.x2y0z2 * coeff.x2y2z2 + 2 * table.x3y0z2 * coeff.x3y2z2
        + 6 * table.x0y1z2 * coeff.x0y3z2 + 6 * table.x1y1z2 * coeff.x1y3z2
        + 6 * table.x2y1z2 * coeff.x2y3z2 + 6 * table.x3y1z2 * coeff.x3y3z2
        + 2 * table.x0y0z3 * coeff.x0y2z3 + 2 * table.x1y0z3 * coeff.x1y2z3
        + 2 * table.x2y0z3 * coeff.x2y2z3 + 2 * table.x3y0z3 * coeff.x3y2z3
        + 6 * table.x0y1z3 * coeff.x0y3z3 + 6 * table.x1y1z3 * coeff.x1y3z3
        + 6 * table.x2y1z3 * coeff.x2y3z3 + 6 * table.x3y1z3 * coeff.x3y3z3;
    derivative2[2] = 2 * table.x0y0z0 * coeff.x0y0z2 + 2 * table.x1y0z0 * coeff.x1y0z2
        + 2 * table.x2y0z0 * coeff.x2y0z2 + 2 * table.x3y0z0 * coeff.x3y0z2
        + 2 * table.x0y1z0 * coeff.x0y1z2 + 2 * table.x1y1z0 * coeff.x1y1z2
        + 2 * table.x2y1z0 * coeff.x2y1z2 + 2 * table.x3y1z0 * coeff.x3y1z2
        + 2 * table.x0y2z0 * coeff.x0y2z2 + 2 * table.x1y2z0 * coeff.x1y2z2
        + 2 * table.x2y2z0 * coeff.x2y2z2 + 2 * table.x3y2z0 * coeff.x3y2z2
        + 2 * table.x0y3z0 * coeff.x0y3z2 + 2 * table.x1y3z0 * coeff.x1y3z2
        + 2 * table.x2y3z0 * coeff.x2y3z2 + 2 * table.x3y3z0 * coeff.x3y3z2
        + 6 * table.x0y0z1 * coeff.x0y0z3 + 6 * table.x1y0z1 * coeff.x1y0z3
        + 6 * table.x2y0z1 * coeff.x2y0z3 + 6 * table.x3y0z1 * coeff.x3y0z3
        + 6 * table.x0y1z1 * coeff.x0y1z3 + 6 * table.x1y1z1 * coeff.x1y1z3
        + 6 * table.x2y1z1 * coeff.x2y1z3 + 6 * table.x3y1z1 * coeff.x3y1z3
        + 6 * table.x0y2z1 * coeff.x0y2z3 + 6 * table.x1y2z1 * coeff.x1y2z3
        + 6 * table.x2y2z1 * coeff.x2y2z3 + 6 * table.x3y2z1 * coeff.x3y2z3
        + 6 * table.x0y3z1 * coeff.x0y3z3 + 6 * table.x1y3z1 * coeff.x1y3z3
        + 6 * table.x2y3z1 * coeff.x2y3z3 + 6 * table.x3y3z1 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(FloatCubicSplineData table, double[] derivative1, double[] derivative2) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
    derivative2[0] = 2 * table.x0y0z0 * coeff.x2y0z0 + 6 * table.x1y0z0 * coeff.x3y0z0
        + 2 * table.x0y1z0 * coeff.x2y1z0 + 6 * table.x1y1z0 * coeff.x3y1z0
        + 2 * table.x0y2z0 * coeff.x2y2z0 + 6 * table.x1y2z0 * coeff.x3y2z0
        + 2 * table.x0y3z0 * coeff.x2y3z0 + 6 * table.x1y3z0 * coeff.x3y3z0
        + 2 * table.x0y0z1 * coeff.x2y0z1 + 6 * table.x1y0z1 * coeff.x3y0z1
        + 2 * table.x0y1z1 * coeff.x2y1z1 + 6 * table.x1y1z1 * coeff.x3y1z1
        + 2 * table.x0y2z1 * coeff.x2y2z1 + 6 * table.x1y2z1 * coeff.x3y2z1
        + 2 * table.x0y3z1 * coeff.x2y3z1 + 6 * table.x1y3z1 * coeff.x3y3z1
        + 2 * table.x0y0z2 * coeff.x2y0z2 + 6 * table.x1y0z2 * coeff.x3y0z2
        + 2 * table.x0y1z2 * coeff.x2y1z2 + 6 * table.x1y1z2 * coeff.x3y1z2
        + 2 * table.x0y2z2 * coeff.x2y2z2 + 6 * table.x1y2z2 * coeff.x3y2z2
        + 2 * table.x0y3z2 * coeff.x2y3z2 + 6 * table.x1y3z2 * coeff.x3y3z2
        + 2 * table.x0y0z3 * coeff.x2y0z3 + 6 * table.x1y0z3 * coeff.x3y0z3
        + 2 * table.x0y1z3 * coeff.x2y1z3 + 6 * table.x1y1z3 * coeff.x3y1z3
        + 2 * table.x0y2z3 * coeff.x2y2z3 + 6 * table.x1y2z3 * coeff.x3y2z3
        + 2 * table.x0y3z3 * coeff.x2y3z3 + 6 * table.x1y3z3 * coeff.x3y3z3;
    derivative2[1] = 2 * table.x0y0z0 * coeff.x0y2z0 + 2 * table.x1y0z0 * coeff.x1y2z0
        + 2 * table.x2y0z0 * coeff.x2y2z0 + 2 * table.x3y0z0 * coeff.x3y2z0
        + 6 * table.x0y1z0 * coeff.x0y3z0 + 6 * table.x1y1z0 * coeff.x1y3z0
        + 6 * table.x2y1z0 * coeff.x2y3z0 + 6 * table.x3y1z0 * coeff.x3y3z0
        + 2 * table.x0y0z1 * coeff.x0y2z1 + 2 * table.x1y0z1 * coeff.x1y2z1
        + 2 * table.x2y0z1 * coeff.x2y2z1 + 2 * table.x3y0z1 * coeff.x3y2z1
        + 6 * table.x0y1z1 * coeff.x0y3z1 + 6 * table.x1y1z1 * coeff.x1y3z1
        + 6 * table.x2y1z1 * coeff.x2y3z1 + 6 * table.x3y1z1 * coeff.x3y3z1
        + 2 * table.x0y0z2 * coeff.x0y2z2 + 2 * table.x1y0z2 * coeff.x1y2z2
        + 2 * table.x2y0z2 * coeff.x2y2z2 + 2 * table.x3y0z2 * coeff.x3y2z2
        + 6 * table.x0y1z2 * coeff.x0y3z2 + 6 * table.x1y1z2 * coeff.x1y3z2
        + 6 * table.x2y1z2 * coeff.x2y3z2 + 6 * table.x3y1z2 * coeff.x3y3z2
        + 2 * table.x0y0z3 * coeff.x0y2z3 + 2 * table.x1y0z3 * coeff.x1y2z3
        + 2 * table.x2y0z3 * coeff.x2y2z3 + 2 * table.x3y0z3 * coeff.x3y2z3
        + 6 * table.x0y1z3 * coeff.x0y3z3 + 6 * table.x1y1z3 * coeff.x1y3z3
        + 6 * table.x2y1z3 * coeff.x2y3z3 + 6 * table.x3y1z3 * coeff.x3y3z3;
    derivative2[2] = 2 * table.x0y0z0 * coeff.x0y0z2 + 2 * table.x1y0z0 * coeff.x1y0z2
        + 2 * table.x2y0z0 * coeff.x2y0z2 + 2 * table.x3y0z0 * coeff.x3y0z2
        + 2 * table.x0y1z0 * coeff.x0y1z2 + 2 * table.x1y1z0 * coeff.x1y1z2
        + 2 * table.x2y1z0 * coeff.x2y1z2 + 2 * table.x3y1z0 * coeff.x3y1z2
        + 2 * table.x0y2z0 * coeff.x0y2z2 + 2 * table.x1y2z0 * coeff.x1y2z2
        + 2 * table.x2y2z0 * coeff.x2y2z2 + 2 * table.x3y2z0 * coeff.x3y2z2
        + 2 * table.x0y3z0 * coeff.x0y3z2 + 2 * table.x1y3z0 * coeff.x1y3z2
        + 2 * table.x2y3z0 * coeff.x2y3z2 + 2 * table.x3y3z0 * coeff.x3y3z2
        + 6 * table.x0y0z1 * coeff.x0y0z3 + 6 * table.x1y0z1 * coeff.x1y0z3
        + 6 * table.x2y0z1 * coeff.x2y0z3 + 6 * table.x3y0z1 * coeff.x3y0z3
        + 6 * table.x0y1z1 * coeff.x0y1z3 + 6 * table.x1y1z1 * coeff.x1y1z3
        + 6 * table.x2y1z1 * coeff.x2y1z3 + 6 * table.x3y1z1 * coeff.x3y1z3
        + 6 * table.x0y2z1 * coeff.x0y2z3 + 6 * table.x1y2z1 * coeff.x1y2z3
        + 6 * table.x2y2z1 * coeff.x2y2z3 + 6 * table.x3y2z1 * coeff.x3y2z3
        + 6 * table.x0y3z1 * coeff.x0y3z3 + 6 * table.x1y3z1 * coeff.x1y3z3
        + 6 * table.x2y3z1 * coeff.x2y3z3 + 6 * table.x3y3z1 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(DoubleCubicSplineData table, DoubleCubicSplineData table2,
      DoubleCubicSplineData table3, DoubleCubicSplineData table6, double[] derivative1,
      double[] derivative2) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + table2.x1y0z0 * coeff.x2y0z0
        + table3.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0 + table2.x1y1z0 * coeff.x2y1z0
        + table3.x2y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x1y2z0 + table2.x1y2z0 * coeff.x2y2z0
        + table3.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0 + table2.x1y3z0 * coeff.x2y3z0
        + table3.x2y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x1y0z1 + table2.x1y0z1 * coeff.x2y0z1
        + table3.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1 + table2.x1y1z1 * coeff.x2y1z1
        + table3.x2y1z1 * coeff.x3y1z1 + table.x0y2z1 * coeff.x1y2z1 + table2.x1y2z1 * coeff.x2y2z1
        + table3.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1 + table2.x1y3z1 * coeff.x2y3z1
        + table3.x2y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x1y0z2 + table2.x1y0z2 * coeff.x2y0z2
        + table3.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2 + table2.x1y1z2 * coeff.x2y1z2
        + table3.x2y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x1y2z2 + table2.x1y2z2 * coeff.x2y2z2
        + table3.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2 + table2.x1y3z2 * coeff.x2y3z2
        + table3.x2y3z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x1y0z3 + table2.x1y0z3 * coeff.x2y0z3
        + table3.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3 + table2.x1y1z3 * coeff.x2y1z3
        + table3.x2y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x1y2z3 + table2.x1y2z3 * coeff.x2y2z3
        + table3.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3 + table2.x1y3z3 * coeff.x2y3z3
        + table3.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0 + table2.x0y1z0 * coeff.x0y2z0
        + table2.x1y1z0 * coeff.x1y2z0 + table2.x2y1z0 * coeff.x2y2z0 + table2.x3y1z0 * coeff.x3y2z0
        + table3.x0y2z0 * coeff.x0y3z0 + table3.x1y2z0 * coeff.x1y3z0 + table3.x2y2z0 * coeff.x2y3z0
        + table3.x3y2z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1
        + table.x2y0z1 * coeff.x2y1z1 + table.x3y0z1 * coeff.x3y1z1 + table2.x0y1z1 * coeff.x0y2z1
        + table2.x1y1z1 * coeff.x1y2z1 + table2.x2y1z1 * coeff.x2y2z1 + table2.x3y1z1 * coeff.x3y2z1
        + table3.x0y2z1 * coeff.x0y3z1 + table3.x1y2z1 * coeff.x1y3z1 + table3.x2y2z1 * coeff.x2y3z1
        + table3.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2 + table.x1y0z2 * coeff.x1y1z2
        + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2 + table2.x0y1z2 * coeff.x0y2z2
        + table2.x1y1z2 * coeff.x1y2z2 + table2.x2y1z2 * coeff.x2y2z2 + table2.x3y1z2 * coeff.x3y2z2
        + table3.x0y2z2 * coeff.x0y3z2 + table3.x1y2z2 * coeff.x1y3z2 + table3.x2y2z2 * coeff.x2y3z2
        + table3.x3y2z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3
        + table.x2y0z3 * coeff.x2y1z3 + table.x3y0z3 * coeff.x3y1z3 + table2.x0y1z3 * coeff.x0y2z3
        + table2.x1y1z3 * coeff.x1y2z3 + table2.x2y1z3 * coeff.x2y2z3 + table2.x3y1z3 * coeff.x3y2z3
        + table3.x0y2z3 * coeff.x0y3z3 + table3.x1y2z3 * coeff.x1y3z3 + table3.x2y2z3 * coeff.x2y3z3
        + table3.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1 + table2.x0y0z1 * coeff.x0y0z2
        + table2.x1y0z1 * coeff.x1y0z2 + table2.x2y0z1 * coeff.x2y0z2 + table2.x3y0z1 * coeff.x3y0z2
        + table2.x0y1z1 * coeff.x0y1z2 + table2.x1y1z1 * coeff.x1y1z2 + table2.x2y1z1 * coeff.x2y1z2
        + table2.x3y1z1 * coeff.x3y1z2 + table2.x0y2z1 * coeff.x0y2z2 + table2.x1y2z1 * coeff.x1y2z2
        + table2.x2y2z1 * coeff.x2y2z2 + table2.x3y2z1 * coeff.x3y2z2 + table2.x0y3z1 * coeff.x0y3z2
        + table2.x1y3z1 * coeff.x1y3z2 + table2.x2y3z1 * coeff.x2y3z2 + table2.x3y3z1 * coeff.x3y3z2
        + table3.x0y0z2 * coeff.x0y0z3 + table3.x1y0z2 * coeff.x1y0z3 + table3.x2y0z2 * coeff.x2y0z3
        + table3.x3y0z2 * coeff.x3y0z3 + table3.x0y1z2 * coeff.x0y1z3 + table3.x1y1z2 * coeff.x1y1z3
        + table3.x2y1z2 * coeff.x2y1z3 + table3.x3y1z2 * coeff.x3y1z3 + table3.x0y2z2 * coeff.x0y2z3
        + table3.x1y2z2 * coeff.x1y2z3 + table3.x2y2z2 * coeff.x2y2z3 + table3.x3y2z2 * coeff.x3y2z3
        + table3.x0y3z2 * coeff.x0y3z3 + table3.x1y3z2 * coeff.x1y3z3 + table3.x2y3z2 * coeff.x2y3z3
        + table3.x3y3z2 * coeff.x3y3z3;
    derivative2[0] = table2.x0y0z0 * coeff.x2y0z0 + table6.x1y0z0 * coeff.x3y0z0
        + table2.x0y1z0 * coeff.x2y1z0 + table6.x1y1z0 * coeff.x3y1z0 + table2.x0y2z0 * coeff.x2y2z0
        + table6.x1y2z0 * coeff.x3y2z0 + table2.x0y3z0 * coeff.x2y3z0 + table6.x1y3z0 * coeff.x3y3z0
        + table2.x0y0z1 * coeff.x2y0z1 + table6.x1y0z1 * coeff.x3y0z1 + table2.x0y1z1 * coeff.x2y1z1
        + table6.x1y1z1 * coeff.x3y1z1 + table2.x0y2z1 * coeff.x2y2z1 + table6.x1y2z1 * coeff.x3y2z1
        + table2.x0y3z1 * coeff.x2y3z1 + table6.x1y3z1 * coeff.x3y3z1 + table2.x0y0z2 * coeff.x2y0z2
        + table6.x1y0z2 * coeff.x3y0z2 + table2.x0y1z2 * coeff.x2y1z2 + table6.x1y1z2 * coeff.x3y1z2
        + table2.x0y2z2 * coeff.x2y2z2 + table6.x1y2z2 * coeff.x3y2z2 + table2.x0y3z2 * coeff.x2y3z2
        + table6.x1y3z2 * coeff.x3y3z2 + table2.x0y0z3 * coeff.x2y0z3 + table6.x1y0z3 * coeff.x3y0z3
        + table2.x0y1z3 * coeff.x2y1z3 + table6.x1y1z3 * coeff.x3y1z3 + table2.x0y2z3 * coeff.x2y2z3
        + table6.x1y2z3 * coeff.x3y2z3 + table2.x0y3z3 * coeff.x2y3z3
        + table6.x1y3z3 * coeff.x3y3z3;
    derivative2[1] = table2.x0y0z0 * coeff.x0y2z0 + table2.x1y0z0 * coeff.x1y2z0
        + table2.x2y0z0 * coeff.x2y2z0 + table2.x3y0z0 * coeff.x3y2z0 + table6.x0y1z0 * coeff.x0y3z0
        + table6.x1y1z0 * coeff.x1y3z0 + table6.x2y1z0 * coeff.x2y3z0 + table6.x3y1z0 * coeff.x3y3z0
        + table2.x0y0z1 * coeff.x0y2z1 + table2.x1y0z1 * coeff.x1y2z1 + table2.x2y0z1 * coeff.x2y2z1
        + table2.x3y0z1 * coeff.x3y2z1 + table6.x0y1z1 * coeff.x0y3z1 + table6.x1y1z1 * coeff.x1y3z1
        + table6.x2y1z1 * coeff.x2y3z1 + table6.x3y1z1 * coeff.x3y3z1 + table2.x0y0z2 * coeff.x0y2z2
        + table2.x1y0z2 * coeff.x1y2z2 + table2.x2y0z2 * coeff.x2y2z2 + table2.x3y0z2 * coeff.x3y2z2
        + table6.x0y1z2 * coeff.x0y3z2 + table6.x1y1z2 * coeff.x1y3z2 + table6.x2y1z2 * coeff.x2y3z2
        + table6.x3y1z2 * coeff.x3y3z2 + table2.x0y0z3 * coeff.x0y2z3 + table2.x1y0z3 * coeff.x1y2z3
        + table2.x2y0z3 * coeff.x2y2z3 + table2.x3y0z3 * coeff.x3y2z3 + table6.x0y1z3 * coeff.x0y3z3
        + table6.x1y1z3 * coeff.x1y3z3 + table6.x2y1z3 * coeff.x2y3z3
        + table6.x3y1z3 * coeff.x3y3z3;
    derivative2[2] = table2.x0y0z0 * coeff.x0y0z2 + table2.x1y0z0 * coeff.x1y0z2
        + table2.x2y0z0 * coeff.x2y0z2 + table2.x3y0z0 * coeff.x3y0z2 + table2.x0y1z0 * coeff.x0y1z2
        + table2.x1y1z0 * coeff.x1y1z2 + table2.x2y1z0 * coeff.x2y1z2 + table2.x3y1z0 * coeff.x3y1z2
        + table2.x0y2z0 * coeff.x0y2z2 + table2.x1y2z0 * coeff.x1y2z2 + table2.x2y2z0 * coeff.x2y2z2
        + table2.x3y2z0 * coeff.x3y2z2 + table2.x0y3z0 * coeff.x0y3z2 + table2.x1y3z0 * coeff.x1y3z2
        + table2.x2y3z0 * coeff.x2y3z2 + table2.x3y3z0 * coeff.x3y3z2 + table6.x0y0z1 * coeff.x0y0z3
        + table6.x1y0z1 * coeff.x1y0z3 + table6.x2y0z1 * coeff.x2y0z3 + table6.x3y0z1 * coeff.x3y0z3
        + table6.x0y1z1 * coeff.x0y1z3 + table6.x1y1z1 * coeff.x1y1z3 + table6.x2y1z1 * coeff.x2y1z3
        + table6.x3y1z1 * coeff.x3y1z3 + table6.x0y2z1 * coeff.x0y2z3 + table6.x1y2z1 * coeff.x1y2z3
        + table6.x2y2z1 * coeff.x2y2z3 + table6.x3y2z1 * coeff.x3y2z3 + table6.x0y3z1 * coeff.x0y3z3
        + table6.x1y3z1 * coeff.x1y3z3 + table6.x2y3z1 * coeff.x2y3z3
        + table6.x3y3z1 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public double value(FloatCubicSplineData table, FloatCubicSplineData table2,
      FloatCubicSplineData table3, FloatCubicSplineData table6, double[] derivative1,
      double[] derivative2) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + table2.x1y0z0 * coeff.x2y0z0
        + table3.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0 + table2.x1y1z0 * coeff.x2y1z0
        + table3.x2y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x1y2z0 + table2.x1y2z0 * coeff.x2y2z0
        + table3.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0 + table2.x1y3z0 * coeff.x2y3z0
        + table3.x2y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x1y0z1 + table2.x1y0z1 * coeff.x2y0z1
        + table3.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1 + table2.x1y1z1 * coeff.x2y1z1
        + table3.x2y1z1 * coeff.x3y1z1 + table.x0y2z1 * coeff.x1y2z1 + table2.x1y2z1 * coeff.x2y2z1
        + table3.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1 + table2.x1y3z1 * coeff.x2y3z1
        + table3.x2y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x1y0z2 + table2.x1y0z2 * coeff.x2y0z2
        + table3.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2 + table2.x1y1z2 * coeff.x2y1z2
        + table3.x2y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x1y2z2 + table2.x1y2z2 * coeff.x2y2z2
        + table3.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2 + table2.x1y3z2 * coeff.x2y3z2
        + table3.x2y3z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x1y0z3 + table2.x1y0z3 * coeff.x2y0z3
        + table3.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3 + table2.x1y1z3 * coeff.x2y1z3
        + table3.x2y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x1y2z3 + table2.x1y2z3 * coeff.x2y2z3
        + table3.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3 + table2.x1y3z3 * coeff.x2y3z3
        + table3.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0 + table2.x0y1z0 * coeff.x0y2z0
        + table2.x1y1z0 * coeff.x1y2z0 + table2.x2y1z0 * coeff.x2y2z0 + table2.x3y1z0 * coeff.x3y2z0
        + table3.x0y2z0 * coeff.x0y3z0 + table3.x1y2z0 * coeff.x1y3z0 + table3.x2y2z0 * coeff.x2y3z0
        + table3.x3y2z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1
        + table.x2y0z1 * coeff.x2y1z1 + table.x3y0z1 * coeff.x3y1z1 + table2.x0y1z1 * coeff.x0y2z1
        + table2.x1y1z1 * coeff.x1y2z1 + table2.x2y1z1 * coeff.x2y2z1 + table2.x3y1z1 * coeff.x3y2z1
        + table3.x0y2z1 * coeff.x0y3z1 + table3.x1y2z1 * coeff.x1y3z1 + table3.x2y2z1 * coeff.x2y3z1
        + table3.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2 + table.x1y0z2 * coeff.x1y1z2
        + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2 + table2.x0y1z2 * coeff.x0y2z2
        + table2.x1y1z2 * coeff.x1y2z2 + table2.x2y1z2 * coeff.x2y2z2 + table2.x3y1z2 * coeff.x3y2z2
        + table3.x0y2z2 * coeff.x0y3z2 + table3.x1y2z2 * coeff.x1y3z2 + table3.x2y2z2 * coeff.x2y3z2
        + table3.x3y2z2 * coeff.x3y3z2 + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3
        + table.x2y0z3 * coeff.x2y1z3 + table.x3y0z3 * coeff.x3y1z3 + table2.x0y1z3 * coeff.x0y2z3
        + table2.x1y1z3 * coeff.x1y2z3 + table2.x2y1z3 * coeff.x2y2z3 + table2.x3y1z3 * coeff.x3y2z3
        + table3.x0y2z3 * coeff.x0y3z3 + table3.x1y2z3 * coeff.x1y3z3 + table3.x2y2z3 * coeff.x2y3z3
        + table3.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1 + table2.x0y0z1 * coeff.x0y0z2
        + table2.x1y0z1 * coeff.x1y0z2 + table2.x2y0z1 * coeff.x2y0z2 + table2.x3y0z1 * coeff.x3y0z2
        + table2.x0y1z1 * coeff.x0y1z2 + table2.x1y1z1 * coeff.x1y1z2 + table2.x2y1z1 * coeff.x2y1z2
        + table2.x3y1z1 * coeff.x3y1z2 + table2.x0y2z1 * coeff.x0y2z2 + table2.x1y2z1 * coeff.x1y2z2
        + table2.x2y2z1 * coeff.x2y2z2 + table2.x3y2z1 * coeff.x3y2z2 + table2.x0y3z1 * coeff.x0y3z2
        + table2.x1y3z1 * coeff.x1y3z2 + table2.x2y3z1 * coeff.x2y3z2 + table2.x3y3z1 * coeff.x3y3z2
        + table3.x0y0z2 * coeff.x0y0z3 + table3.x1y0z2 * coeff.x1y0z3 + table3.x2y0z2 * coeff.x2y0z3
        + table3.x3y0z2 * coeff.x3y0z3 + table3.x0y1z2 * coeff.x0y1z3 + table3.x1y1z2 * coeff.x1y1z3
        + table3.x2y1z2 * coeff.x2y1z3 + table3.x3y1z2 * coeff.x3y1z3 + table3.x0y2z2 * coeff.x0y2z3
        + table3.x1y2z2 * coeff.x1y2z3 + table3.x2y2z2 * coeff.x2y2z3 + table3.x3y2z2 * coeff.x3y2z3
        + table3.x0y3z2 * coeff.x0y3z3 + table3.x1y3z2 * coeff.x1y3z3 + table3.x2y3z2 * coeff.x2y3z3
        + table3.x3y3z2 * coeff.x3y3z3;
    derivative2[0] = table2.x0y0z0 * coeff.x2y0z0 + table6.x1y0z0 * coeff.x3y0z0
        + table2.x0y1z0 * coeff.x2y1z0 + table6.x1y1z0 * coeff.x3y1z0 + table2.x0y2z0 * coeff.x2y2z0
        + table6.x1y2z0 * coeff.x3y2z0 + table2.x0y3z0 * coeff.x2y3z0 + table6.x1y3z0 * coeff.x3y3z0
        + table2.x0y0z1 * coeff.x2y0z1 + table6.x1y0z1 * coeff.x3y0z1 + table2.x0y1z1 * coeff.x2y1z1
        + table6.x1y1z1 * coeff.x3y1z1 + table2.x0y2z1 * coeff.x2y2z1 + table6.x1y2z1 * coeff.x3y2z1
        + table2.x0y3z1 * coeff.x2y3z1 + table6.x1y3z1 * coeff.x3y3z1 + table2.x0y0z2 * coeff.x2y0z2
        + table6.x1y0z2 * coeff.x3y0z2 + table2.x0y1z2 * coeff.x2y1z2 + table6.x1y1z2 * coeff.x3y1z2
        + table2.x0y2z2 * coeff.x2y2z2 + table6.x1y2z2 * coeff.x3y2z2 + table2.x0y3z2 * coeff.x2y3z2
        + table6.x1y3z2 * coeff.x3y3z2 + table2.x0y0z3 * coeff.x2y0z3 + table6.x1y0z3 * coeff.x3y0z3
        + table2.x0y1z3 * coeff.x2y1z3 + table6.x1y1z3 * coeff.x3y1z3 + table2.x0y2z3 * coeff.x2y2z3
        + table6.x1y2z3 * coeff.x3y2z3 + table2.x0y3z3 * coeff.x2y3z3
        + table6.x1y3z3 * coeff.x3y3z3;
    derivative2[1] = table2.x0y0z0 * coeff.x0y2z0 + table2.x1y0z0 * coeff.x1y2z0
        + table2.x2y0z0 * coeff.x2y2z0 + table2.x3y0z0 * coeff.x3y2z0 + table6.x0y1z0 * coeff.x0y3z0
        + table6.x1y1z0 * coeff.x1y3z0 + table6.x2y1z0 * coeff.x2y3z0 + table6.x3y1z0 * coeff.x3y3z0
        + table2.x0y0z1 * coeff.x0y2z1 + table2.x1y0z1 * coeff.x1y2z1 + table2.x2y0z1 * coeff.x2y2z1
        + table2.x3y0z1 * coeff.x3y2z1 + table6.x0y1z1 * coeff.x0y3z1 + table6.x1y1z1 * coeff.x1y3z1
        + table6.x2y1z1 * coeff.x2y3z1 + table6.x3y1z1 * coeff.x3y3z1 + table2.x0y0z2 * coeff.x0y2z2
        + table2.x1y0z2 * coeff.x1y2z2 + table2.x2y0z2 * coeff.x2y2z2 + table2.x3y0z2 * coeff.x3y2z2
        + table6.x0y1z2 * coeff.x0y3z2 + table6.x1y1z2 * coeff.x1y3z2 + table6.x2y1z2 * coeff.x2y3z2
        + table6.x3y1z2 * coeff.x3y3z2 + table2.x0y0z3 * coeff.x0y2z3 + table2.x1y0z3 * coeff.x1y2z3
        + table2.x2y0z3 * coeff.x2y2z3 + table2.x3y0z3 * coeff.x3y2z3 + table6.x0y1z3 * coeff.x0y3z3
        + table6.x1y1z3 * coeff.x1y3z3 + table6.x2y1z3 * coeff.x2y3z3
        + table6.x3y1z3 * coeff.x3y3z3;
    derivative2[2] = table2.x0y0z0 * coeff.x0y0z2 + table2.x1y0z0 * coeff.x1y0z2
        + table2.x2y0z0 * coeff.x2y0z2 + table2.x3y0z0 * coeff.x3y0z2 + table2.x0y1z0 * coeff.x0y1z2
        + table2.x1y1z0 * coeff.x1y1z2 + table2.x2y1z0 * coeff.x2y1z2 + table2.x3y1z0 * coeff.x3y1z2
        + table2.x0y2z0 * coeff.x0y2z2 + table2.x1y2z0 * coeff.x1y2z2 + table2.x2y2z0 * coeff.x2y2z2
        + table2.x3y2z0 * coeff.x3y2z2 + table2.x0y3z0 * coeff.x0y3z2 + table2.x1y3z0 * coeff.x1y3z2
        + table2.x2y3z0 * coeff.x2y3z2 + table2.x3y3z0 * coeff.x3y3z2 + table6.x0y0z1 * coeff.x0y0z3
        + table6.x1y0z1 * coeff.x1y0z3 + table6.x2y0z1 * coeff.x2y0z3 + table6.x3y0z1 * coeff.x3y0z3
        + table6.x0y1z1 * coeff.x0y1z3 + table6.x1y1z1 * coeff.x1y1z3 + table6.x2y1z1 * coeff.x2y1z3
        + table6.x3y1z1 * coeff.x3y1z3 + table6.x0y2z1 * coeff.x0y2z3 + table6.x1y2z1 * coeff.x1y2z3
        + table6.x2y2z1 * coeff.x2y2z3 + table6.x3y2z1 * coeff.x3y2z3 + table6.x0y3z1 * coeff.x0y3z3
        + table6.x1y3z1 * coeff.x1y3z3 + table6.x2y3z1 * coeff.x2y3z3
        + table6.x3y3z1 * coeff.x3y3z3;
    return table.x0y0z0 * coeff.x0y0z0 + table.x1y0z0 * coeff.x1y0z0 + table.x2y0z0 * coeff.x2y0z0
        + table.x3y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x0y1z0 + table.x1y1z0 * coeff.x1y1z0
        + table.x2y1z0 * coeff.x2y1z0 + table.x3y1z0 * coeff.x3y1z0 + table.x0y2z0 * coeff.x0y2z0
        + table.x1y2z0 * coeff.x1y2z0 + table.x2y2z0 * coeff.x2y2z0 + table.x3y2z0 * coeff.x3y2z0
        + table.x0y3z0 * coeff.x0y3z0 + table.x1y3z0 * coeff.x1y3z0 + table.x2y3z0 * coeff.x2y3z0
        + table.x3y3z0 * coeff.x3y3z0 + table.x0y0z1 * coeff.x0y0z1 + table.x1y0z1 * coeff.x1y0z1
        + table.x2y0z1 * coeff.x2y0z1 + table.x3y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x0y1z1
        + table.x1y1z1 * coeff.x1y1z1 + table.x2y1z1 * coeff.x2y1z1 + table.x3y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x0y2z1 + table.x1y2z1 * coeff.x1y2z1 + table.x2y2z1 * coeff.x2y2z1
        + table.x3y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x0y3z1 + table.x1y3z1 * coeff.x1y3z1
        + table.x2y3z1 * coeff.x2y3z1 + table.x3y3z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y0z2
        + table.x1y0z2 * coeff.x1y0z2 + table.x2y0z2 * coeff.x2y0z2 + table.x3y0z2 * coeff.x3y0z2
        + table.x0y1z2 * coeff.x0y1z2 + table.x1y1z2 * coeff.x1y1z2 + table.x2y1z2 * coeff.x2y1z2
        + table.x3y1z2 * coeff.x3y1z2 + table.x0y2z2 * coeff.x0y2z2 + table.x1y2z2 * coeff.x1y2z2
        + table.x2y2z2 * coeff.x2y2z2 + table.x3y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x0y3z2
        + table.x1y3z2 * coeff.x1y3z2 + table.x2y3z2 * coeff.x2y3z2 + table.x3y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y0z3 + table.x1y0z3 * coeff.x1y0z3 + table.x2y0z3 * coeff.x2y0z3
        + table.x3y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x0y1z3 + table.x1y1z3 * coeff.x1y1z3
        + table.x2y1z3 * coeff.x2y1z3 + table.x3y1z3 * coeff.x3y1z3 + table.x0y2z3 * coeff.x0y2z3
        + table.x1y2z3 * coeff.x1y2z3 + table.x2y2z3 * coeff.x2y2z3 + table.x3y2z3 * coeff.x3y2z3
        + table.x0y3z3 * coeff.x0y3z3 + table.x1y3z3 * coeff.x1y3z3 + table.x2y3z3 * coeff.x2y3z3
        + table.x3y3z3 * coeff.x3y3z3;
  }

  public void gradient(DoubleCubicSplineData table, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
  }

  public void gradient(FloatCubicSplineData table, double[] derivative1) {
    derivative1[0] = table.x0y0z0 * coeff.x1y0z0 + 2 * table.x1y0z0 * coeff.x2y0z0
        + 3 * table.x2y0z0 * coeff.x3y0z0 + table.x0y1z0 * coeff.x1y1z0
        + 2 * table.x1y1z0 * coeff.x2y1z0 + 3 * table.x2y1z0 * coeff.x3y1z0
        + table.x0y2z0 * coeff.x1y2z0 + 2 * table.x1y2z0 * coeff.x2y2z0
        + 3 * table.x2y2z0 * coeff.x3y2z0 + table.x0y3z0 * coeff.x1y3z0
        + 2 * table.x1y3z0 * coeff.x2y3z0 + 3 * table.x2y3z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x1y0z1 + 2 * table.x1y0z1 * coeff.x2y0z1
        + 3 * table.x2y0z1 * coeff.x3y0z1 + table.x0y1z1 * coeff.x1y1z1
        + 2 * table.x1y1z1 * coeff.x2y1z1 + 3 * table.x2y1z1 * coeff.x3y1z1
        + table.x0y2z1 * coeff.x1y2z1 + 2 * table.x1y2z1 * coeff.x2y2z1
        + 3 * table.x2y2z1 * coeff.x3y2z1 + table.x0y3z1 * coeff.x1y3z1
        + 2 * table.x1y3z1 * coeff.x2y3z1 + 3 * table.x2y3z1 * coeff.x3y3z1
        + table.x0y0z2 * coeff.x1y0z2 + 2 * table.x1y0z2 * coeff.x2y0z2
        + 3 * table.x2y0z2 * coeff.x3y0z2 + table.x0y1z2 * coeff.x1y1z2
        + 2 * table.x1y1z2 * coeff.x2y1z2 + 3 * table.x2y1z2 * coeff.x3y1z2
        + table.x0y2z2 * coeff.x1y2z2 + 2 * table.x1y2z2 * coeff.x2y2z2
        + 3 * table.x2y2z2 * coeff.x3y2z2 + table.x0y3z2 * coeff.x1y3z2
        + 2 * table.x1y3z2 * coeff.x2y3z2 + 3 * table.x2y3z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x1y0z3 + 2 * table.x1y0z3 * coeff.x2y0z3
        + 3 * table.x2y0z3 * coeff.x3y0z3 + table.x0y1z3 * coeff.x1y1z3
        + 2 * table.x1y1z3 * coeff.x2y1z3 + 3 * table.x2y1z3 * coeff.x3y1z3
        + table.x0y2z3 * coeff.x1y2z3 + 2 * table.x1y2z3 * coeff.x2y2z3
        + 3 * table.x2y2z3 * coeff.x3y2z3 + table.x0y3z3 * coeff.x1y3z3
        + 2 * table.x1y3z3 * coeff.x2y3z3 + 3 * table.x2y3z3 * coeff.x3y3z3;
    derivative1[1] = table.x0y0z0 * coeff.x0y1z0 + table.x1y0z0 * coeff.x1y1z0
        + table.x2y0z0 * coeff.x2y1z0 + table.x3y0z0 * coeff.x3y1z0
        + 2 * table.x0y1z0 * coeff.x0y2z0 + 2 * table.x1y1z0 * coeff.x1y2z0
        + 2 * table.x2y1z0 * coeff.x2y2z0 + 2 * table.x3y1z0 * coeff.x3y2z0
        + 3 * table.x0y2z0 * coeff.x0y3z0 + 3 * table.x1y2z0 * coeff.x1y3z0
        + 3 * table.x2y2z0 * coeff.x2y3z0 + 3 * table.x3y2z0 * coeff.x3y3z0
        + table.x0y0z1 * coeff.x0y1z1 + table.x1y0z1 * coeff.x1y1z1 + table.x2y0z1 * coeff.x2y1z1
        + table.x3y0z1 * coeff.x3y1z1 + 2 * table.x0y1z1 * coeff.x0y2z1
        + 2 * table.x1y1z1 * coeff.x1y2z1 + 2 * table.x2y1z1 * coeff.x2y2z1
        + 2 * table.x3y1z1 * coeff.x3y2z1 + 3 * table.x0y2z1 * coeff.x0y3z1
        + 3 * table.x1y2z1 * coeff.x1y3z1 + 3 * table.x2y2z1 * coeff.x2y3z1
        + 3 * table.x3y2z1 * coeff.x3y3z1 + table.x0y0z2 * coeff.x0y1z2
        + table.x1y0z2 * coeff.x1y1z2 + table.x2y0z2 * coeff.x2y1z2 + table.x3y0z2 * coeff.x3y1z2
        + 2 * table.x0y1z2 * coeff.x0y2z2 + 2 * table.x1y1z2 * coeff.x1y2z2
        + 2 * table.x2y1z2 * coeff.x2y2z2 + 2 * table.x3y1z2 * coeff.x3y2z2
        + 3 * table.x0y2z2 * coeff.x0y3z2 + 3 * table.x1y2z2 * coeff.x1y3z2
        + 3 * table.x2y2z2 * coeff.x2y3z2 + 3 * table.x3y2z2 * coeff.x3y3z2
        + table.x0y0z3 * coeff.x0y1z3 + table.x1y0z3 * coeff.x1y1z3 + table.x2y0z3 * coeff.x2y1z3
        + table.x3y0z3 * coeff.x3y1z3 + 2 * table.x0y1z3 * coeff.x0y2z3
        + 2 * table.x1y1z3 * coeff.x1y2z3 + 2 * table.x2y1z3 * coeff.x2y2z3
        + 2 * table.x3y1z3 * coeff.x3y2z3 + 3 * table.x0y2z3 * coeff.x0y3z3
        + 3 * table.x1y2z3 * coeff.x1y3z3 + 3 * table.x2y2z3 * coeff.x2y3z3
        + 3 * table.x3y2z3 * coeff.x3y3z3;
    derivative1[2] = table.x0y0z0 * coeff.x0y0z1 + table.x1y0z0 * coeff.x1y0z1
        + table.x2y0z0 * coeff.x2y0z1 + table.x3y0z0 * coeff.x3y0z1 + table.x0y1z0 * coeff.x0y1z1
        + table.x1y1z0 * coeff.x1y1z1 + table.x2y1z0 * coeff.x2y1z1 + table.x3y1z0 * coeff.x3y1z1
        + table.x0y2z0 * coeff.x0y2z1 + table.x1y2z0 * coeff.x1y2z1 + table.x2y2z0 * coeff.x2y2z1
        + table.x3y2z0 * coeff.x3y2z1 + table.x0y3z0 * coeff.x0y3z1 + table.x1y3z0 * coeff.x1y3z1
        + table.x2y3z0 * coeff.x2y3z1 + table.x3y3z0 * coeff.x3y3z1
        + 2 * table.x0y0z1 * coeff.x0y0z2 + 2 * table.x1y0z1 * coeff.x1y0z2
        + 2 * table.x2y0z1 * coeff.x2y0z2 + 2 * table.x3y0z1 * coeff.x3y0z2
        + 2 * table.x0y1z1 * coeff.x0y1z2 + 2 * table.x1y1z1 * coeff.x1y1z2
        + 2 * table.x2y1z1 * coeff.x2y1z2 + 2 * table.x3y1z1 * coeff.x3y1z2
        + 2 * table.x0y2z1 * coeff.x0y2z2 + 2 * table.x1y2z1 * coeff.x1y2z2
        + 2 * table.x2y2z1 * coeff.x2y2z2 + 2 * table.x3y2z1 * coeff.x3y2z2
        + 2 * table.x0y3z1 * coeff.x0y3z2 + 2 * table.x1y3z1 * coeff.x1y3z2
        + 2 * table.x2y3z1 * coeff.x2y3z2 + 2 * table.x3y3z1 * coeff.x3y3z2
        + 3 * table.x0y0z2 * coeff.x0y0z3 + 3 * table.x1y0z2 * coeff.x1y0z3
        + 3 * table.x2y0z2 * coeff.x2y0z3 + 3 * table.x3y0z2 * coeff.x3y0z3
        + 3 * table.x0y1z2 * coeff.x0y1z3 + 3 * table.x1y1z2 * coeff.x1y1z3
        + 3 * table.x2y1z2 * coeff.x2y1z3 + 3 * table.x3y1z2 * coeff.x3y1z3
        + 3 * table.x0y2z2 * coeff.x0y2z3 + 3 * table.x1y2z2 * coeff.x1y2z3
        + 3 * table.x2y2z2 * coeff.x2y2z3 + 3 * table.x3y2z2 * coeff.x3y2z3
        + 3 * table.x0y3z2 * coeff.x0y3z3 + 3 * table.x1y3z2 * coeff.x1y3z3
        + 3 * table.x2y3z2 * coeff.x2y3z3 + 3 * table.x3y3z2 * coeff.x3y3z3;
  }
}
