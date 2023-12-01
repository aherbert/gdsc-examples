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

import java.util.SplittableRandom;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.ac.sussex.gdsc.test.api.Predicates;
import uk.ac.sussex.gdsc.test.api.TestAssertions;

/**
 * Test for {@link Edm}.
 */
@SuppressWarnings({"javadoc"})
class EdmTest {
  @ParameterizedTest
  @CsvSource({"3, 3", "3, 10", "6, 7",})
  void canComputeEdm2(int m, int n) {
    final double[][] x = EdmBenchmark.PointData.createData(m, n, new SplittableRandom(12637846128364L));
    final double[][] e = EdmBenchmark.edm2(x);
    final double[][] a = Edm.edm2(x);
    TestAssertions.assertArrayTest(e, a, Predicates.doublesAreUlpClose(20));
  }
}
