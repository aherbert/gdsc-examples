/*-
 * #%L
 * Genome Damage and Stability Centre Core Package
 *
 * Contains core utilities for image analysis and is used by:
 *
 * GDSC ImageJ Plugins - Microscopy image analysis
 *
 * GDSC SMLM ImageJ Plugins - Single molecule localisation microscopy (SMLM)
 * %%
 * Copyright (C) 2011 - 2023 Alex Herbert
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

/**
 * Contains methods to compute the Euclidean distance matrix.
 */
public final class Edm {

  /** No public construction. */
  private Edm() {}

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
  public static double[][] edm2(double[][] x) {
    final int m = x.length;
    final double[][] d = new double[m][m];

    // Use the Gram matrix:
    // Dij = ||xi - xj||^2
    // = (xi - xj)^T (xi - xj)
    // = ||xi||^2 - 2 ||xi^T xj|| + ||xj||^2
    // Albanie, Samuel. Euclidean Distance Matrix Trick. June, 2019.
    // https://samuelalbanie.com/files/Euclidean_distance_trick.pdf

    final double[] t = new double[m];
    for (int i = 0; i < m; i++) {
      t[i] = dot(x[i]);
    }

    // Compute all-vs-all dot product and derive distance matrix
    for (int i = 0; i < m; i++) {
      for (int j = i + 1; j < m; j++) {
        // Avoid floating-point error
        d[j][i] = max0(t[i] + t[j] - 2 * dot(x[i], x[j]));
        d[i][j] = d[j][i];
      }
    }

    return d;
  }

  /**
   * Compute the dot product.
   *
   * @param x the x
   * @return the dot product
   */
  private static double dot(double[] x) {
    double d = 0;
    for (final double v : x) {
      d += v * v;
    }
    return d;
  }

  /**
   * Compute the dot product.
   *
   * @param x the x
   * @param y the y
   * @return the dot product
   */
  private static double dot(double[] x, double[] y) {
    double d = 0;
    for (int i = 0; i < x.length; i++) {
      d += x[i] * y[i];
    }
    return d;
  }

  /**
   * Maximum of zero or {@code x}.
   * Returns NaN if the input is NaN.
   * Returns 0.0 if the input is +/-0.0.
   *
   * @param x the x
   * @return max(0, x)
   */
  private static double max0(double x) {
    //return Double.MIN_VALUE > x ? 0 : x;
    return Math.max(0, x);
  }
}
