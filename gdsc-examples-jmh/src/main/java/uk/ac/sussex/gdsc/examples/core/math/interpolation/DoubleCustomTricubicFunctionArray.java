package uk.ac.sussex.gdsc.examples.core.math.interpolation;

/**
 * 3D-spline function using double precision float values to store the coefficients.
 */
public class DoubleCustomTricubicFunctionArray {
  /** The 64 coefficients (coeff) for the tri-cubic function. */
  private final double[] coeff;

  /**
   * Instantiates a new double custom tricubic function.
   *
   * @param coefficients List of spline coefficients.
   */
  DoubleCustomTricubicFunctionArray(double[] coefficients) {
    // Use the table directly
    this.coeff = coefficients;
  }

  public double value000() {
    return coeff[0];
  }

  public double value000(double[] derivative1) {
    derivative1[0] = coeff[1];
    derivative1[1] = coeff[4];
    derivative1[2] = coeff[16];
    return coeff[0];
  }

  public double value000(double[] derivative1, double[] derivative2) {
    derivative1[0] = coeff[1];
    derivative1[1] = coeff[4];
    derivative1[2] = coeff[16];
    derivative2[0] = 2 * coeff[2];
    derivative2[1] = 2 * coeff[8];
    derivative2[2] = 2 * coeff[32];
    return coeff[0];
  }

  // Allow the working variables for the power computation
  // to be declared at the top of the method
  // CHECKSTYLE.OFF: VariableDeclarationUsageDistance

  protected double value0(final double[] powerX, final double[] powerY, final double[] powerZ) {
    //@formatter:off
    return                        (coeff[ 0] + powerX[0] * coeff[ 1] + powerX[1] * coeff[ 2] + powerX[2] * coeff[ 3])
                    + powerY[0] * (coeff[ 4] + powerX[0] * coeff[ 5] + powerX[1] * coeff[ 6] + powerX[2] * coeff[ 7])
                    + powerY[1] * (coeff[ 8] + powerX[0] * coeff[ 9] + powerX[1] * coeff[10] + powerX[2] * coeff[11])
                    + powerY[2] * (coeff[12] + powerX[0] * coeff[13] + powerX[1] * coeff[14] + powerX[2] * coeff[15])
            + powerZ[0] * (       (coeff[16] + powerX[0] * coeff[17] + powerX[1] * coeff[18] + powerX[2] * coeff[19])
                    + powerY[0] * (coeff[20] + powerX[0] * coeff[21] + powerX[1] * coeff[22] + powerX[2] * coeff[23])
                    + powerY[1] * (coeff[24] + powerX[0] * coeff[25] + powerX[1] * coeff[26] + powerX[2] * coeff[27])
                    + powerY[2] * (coeff[28] + powerX[0] * coeff[29] + powerX[1] * coeff[30] + powerX[2] * coeff[31]))
            + powerZ[1] * (       (coeff[32] + powerX[0] * coeff[33] + powerX[1] * coeff[34] + powerX[2] * coeff[35])
                    + powerY[0] * (coeff[36] + powerX[0] * coeff[37] + powerX[1] * coeff[38] + powerX[2] * coeff[39])
                    + powerY[1] * (coeff[40] + powerX[0] * coeff[41] + powerX[1] * coeff[42] + powerX[2] * coeff[43])
                    + powerY[2] * (coeff[44] + powerX[0] * coeff[45] + powerX[1] * coeff[46] + powerX[2] * coeff[47]))
            + powerZ[2] * (       (coeff[48] + powerX[0] * coeff[49] + powerX[1] * coeff[50] + powerX[2] * coeff[51])
                    + powerY[0] * (coeff[52] + powerX[0] * coeff[53] + powerX[1] * coeff[54] + powerX[2] * coeff[55])
                    + powerY[1] * (coeff[56] + powerX[0] * coeff[57] + powerX[1] * coeff[58] + powerX[2] * coeff[59])
                    + powerY[2] * (coeff[60] + powerX[0] * coeff[61] + powerX[1] * coeff[62] + powerX[2] * coeff[63]));
    //@formatter:on

  }

  protected double value1(final double[] powerX, final double[] powerY, final double[] powerZ,
      final double[] derivative1) {
    //@formatter:off
    derivative1[0] =                          (coeff[ 1] + powerY[0] * coeff[ 5] + powerY[1] * coeff[ 9] + powerY[2] * coeff[13])
                               +  powerZ[0] * (coeff[17] + powerY[0] * coeff[21] + powerY[1] * coeff[25] + powerY[2] * coeff[29])
                               +  powerZ[1] * (coeff[33] + powerY[0] * coeff[37] + powerY[1] * coeff[41] + powerY[2] * coeff[45])
                               +  powerZ[2] * (coeff[49] + powerY[0] * coeff[53] + powerY[1] * coeff[57] + powerY[2] * coeff[61])
                    + 2 * powerX[0] * (       (coeff[ 2] + powerY[0] * coeff[ 6] + powerY[1] * coeff[10] + powerY[2] * coeff[14])
                                + powerZ[0] * (coeff[18] + powerY[0] * coeff[22] + powerY[1] * coeff[26] + powerY[2] * coeff[30])
                                + powerZ[1] * (coeff[34] + powerY[0] * coeff[38] + powerY[1] * coeff[42] + powerY[2] * coeff[46])
                                + powerZ[2] * (coeff[50] + powerY[0] * coeff[54] + powerY[1] * coeff[58] + powerY[2] * coeff[62]))
                    + 3 * powerX[1] * (       (coeff[ 3] + powerY[0] * coeff[ 7] + powerY[1] * coeff[11] + powerY[2] * coeff[15])
                                + powerZ[0] * (coeff[19] + powerY[0] * coeff[23] + powerY[1] * coeff[27] + powerY[2] * coeff[31])
                                + powerZ[1] * (coeff[35] + powerY[0] * coeff[39] + powerY[1] * coeff[43] + powerY[2] * coeff[47])
                                + powerZ[2] * (coeff[51] + powerY[0] * coeff[55] + powerY[1] * coeff[59] + powerY[2] * coeff[63]));

    derivative1[1] =                     (coeff[ 4] + powerX[0] * coeff[ 5] + powerX[1] * coeff[ 6] + powerX[2] * coeff[ 7])
                                + powerZ[0] * (coeff[20] + powerX[0] * coeff[21] + powerX[1] * coeff[22] + powerX[2] * coeff[23])
                                + powerZ[1] * (coeff[36] + powerX[0] * coeff[37] + powerX[1] * coeff[38] + powerX[2] * coeff[39])
                                + powerZ[2] * (coeff[52] + powerX[0] * coeff[53] + powerX[1] * coeff[54] + powerX[2] * coeff[55])
                    + 2 * powerY[0] * (       (coeff[ 8] + powerX[0] * coeff[ 9] + powerX[1] * coeff[10] + powerX[2] * coeff[11])
                                + powerZ[0] * (coeff[24] + powerX[0] * coeff[25] + powerX[1] * coeff[26] + powerX[2] * coeff[27])
                                + powerZ[1] * (coeff[40] + powerX[0] * coeff[41] + powerX[1] * coeff[42] + powerX[2] * coeff[43])
                                + powerZ[2] * (coeff[56] + powerX[0] * coeff[57] + powerX[1] * coeff[58] + powerX[2] * coeff[59]))
                    + 3 * powerY[1] * (       (coeff[12] + powerX[0] * coeff[13] + powerX[1] * coeff[14] + powerX[2] * coeff[15])
                                + powerZ[0] * (coeff[28] + powerX[0] * coeff[29] + powerX[1] * coeff[30] + powerX[2] * coeff[31])
                                + powerZ[1] * (coeff[44] + powerX[0] * coeff[45] + powerX[1] * coeff[46] + powerX[2] * coeff[47])
                                + powerZ[2] * (coeff[60] + powerX[0] * coeff[61] + powerX[1] * coeff[62] + powerX[2] * coeff[63]));

    // Note: the computation for value0 is arranged using zyx so precompute the factors for z
    final double factorZ1 =               (coeff[16] + powerX[0] * coeff[17] + powerX[1] * coeff[18] + powerX[2] * coeff[19])
                            + powerY[0] * (coeff[20] + powerX[0] * coeff[21] + powerX[1] * coeff[22] + powerX[2] * coeff[23])
                            + powerY[1] * (coeff[24] + powerX[0] * coeff[25] + powerX[1] * coeff[26] + powerX[2] * coeff[27])
                            + powerY[2] * (coeff[28] + powerX[0] * coeff[29] + powerX[1] * coeff[30] + powerX[2] * coeff[31]);
    final double factorZ2 =          (coeff[32] + powerX[0] * coeff[33] + powerX[1] * coeff[34] + powerX[2] * coeff[35])
                            + powerY[0] * (coeff[36] + powerX[0] * coeff[37] + powerX[1] * coeff[38] + powerX[2] * coeff[39])
                            + powerY[1] * (coeff[40] + powerX[0] * coeff[41] + powerX[1] * coeff[42] + powerX[2] * coeff[43])
                            + powerY[2] * (coeff[44] + powerX[0] * coeff[45] + powerX[1] * coeff[46] + powerX[2] * coeff[47]);
    final double factorZ3 =          (coeff[48] + powerX[0] * coeff[49] + powerX[1] * coeff[50] + powerX[2] * coeff[51])
                            + powerY[0] * (coeff[52] + powerX[0] * coeff[53] + powerX[1] * coeff[54] + powerX[2] * coeff[55])
                            + powerY[1] * (coeff[56] + powerX[0] * coeff[57] + powerX[1] * coeff[58] + powerX[2] * coeff[59])
                            + powerY[2] * (coeff[60] + powerX[0] * coeff[61] + powerX[1] * coeff[62] + powerX[2] * coeff[63]);
    derivative1[2] =                  factorZ1
                    + 2 * powerZ[0] * factorZ2
                    + 3 * powerZ[1] * factorZ3;

    return                        (coeff[ 0] + powerX[0] * coeff[ 1] + powerX[1] * coeff[ 2] + powerX[2] * coeff[ 3])
                    + powerY[0] * (coeff[ 4] + powerX[0] * coeff[ 5] + powerX[1] * coeff[ 6] + powerX[2] * coeff[ 7])
                    + powerY[1] * (coeff[ 8] + powerX[0] * coeff[ 9] + powerX[1] * coeff[10] + powerX[2] * coeff[11])
                    + powerY[2] * (coeff[12] + powerX[0] * coeff[13] + powerX[1] * coeff[14] + powerX[2] * coeff[15])
            + powerZ[0] * factorZ1
            + powerZ[1] * factorZ2
            + powerZ[2] * factorZ3;
    //@formatter:on
  }

  protected double value2(final double[] powerX, final double[] powerY, final double[] powerZ,
      final double[] derivative1, double[] derivative2) {
    //@formatter:off
    // Pre-compute the factors for x
    final double factorX1 =               (coeff[ 1] + powerY[0] * coeff[ 5] + powerY[1] * coeff[ 9] + powerY[2] * coeff[13])
                            + powerZ[0] * (coeff[17] + powerY[0] * coeff[21] + powerY[1] * coeff[25] + powerY[2] * coeff[29])
                            + powerZ[1] * (coeff[33] + powerY[0] * coeff[37] + powerY[1] * coeff[41] + powerY[2] * coeff[45])
                            + powerZ[2] * (coeff[49] + powerY[0] * coeff[53] + powerY[1] * coeff[57] + powerY[2] * coeff[61]);
    final double factorX2 =               (coeff[ 2] + powerY[0] * coeff[ 6] + powerY[1] * coeff[10] + powerY[2] * coeff[14])
                            + powerZ[0] * (coeff[18] + powerY[0] * coeff[22] + powerY[1] * coeff[26] + powerY[2] * coeff[30])
                            + powerZ[1] * (coeff[34] + powerY[0] * coeff[38] + powerY[1] * coeff[42] + powerY[2] * coeff[46])
                            + powerZ[2] * (coeff[50] + powerY[0] * coeff[54] + powerY[1] * coeff[58] + powerY[2] * coeff[62]);
    final double factorX3 =               (coeff[ 3] + powerY[0] * coeff[ 7] + powerY[1] * coeff[11] + powerY[2] * coeff[15])
                            + powerZ[0] * (coeff[19] + powerY[0] * coeff[23] + powerY[1] * coeff[27] + powerY[2] * coeff[31])
                            + powerZ[1] * (coeff[35] + powerY[0] * coeff[39] + powerY[1] * coeff[43] + powerY[2] * coeff[47])
                            + powerZ[2] * (coeff[51] + powerY[0] * coeff[55] + powerY[1] * coeff[59] + powerY[2] * coeff[63]);
    derivative1[2] =                  factorX1
                    + 2 * powerX[0] * factorX2
                    + 3 * powerX[1] * factorX3;
    derivative2[2] =  2 *             factorX2
                    + 6 * powerX[0] * factorX3;

    // Pre-compute the factors for y
    final double factorY1 =               (coeff[ 4] + powerX[0] * coeff[ 5] + powerX[1] * coeff[ 6] + powerX[2] * coeff[ 7])
                            + powerZ[0] * (coeff[20] + powerX[0] * coeff[21] + powerX[1] * coeff[22] + powerX[2] * coeff[23])
                            + powerZ[1] * (coeff[36] + powerX[0] * coeff[37] + powerX[1] * coeff[38] + powerX[2] * coeff[39])
                            + powerZ[2] * (coeff[52] + powerX[0] * coeff[53] + powerX[1] * coeff[54] + powerX[2] * coeff[55]);
    final double factorY2 =               (coeff[ 8] + powerX[0] * coeff[ 9] + powerX[1] * coeff[10] + powerX[2] * coeff[11])
                            + powerZ[0] * (coeff[24] + powerX[0] * coeff[25] + powerX[1] * coeff[26] + powerX[2] * coeff[27])
                            + powerZ[1] * (coeff[40] + powerX[0] * coeff[41] + powerX[1] * coeff[42] + powerX[2] * coeff[43])
                            + powerZ[2] * (coeff[56] + powerX[0] * coeff[57] + powerX[1] * coeff[58] + powerX[2] * coeff[59]);
    final double factorY3 =               (coeff[12] + powerX[0] * coeff[13] + powerX[1] * coeff[14] + powerX[2] * coeff[15])
                            + powerZ[0] * (coeff[28] + powerX[0] * coeff[29] + powerX[1] * coeff[30] + powerX[2] * coeff[31])
                            + powerZ[1] * (coeff[44] + powerX[0] * coeff[45] + powerX[1] * coeff[46] + powerX[2] * coeff[47])
                            + powerZ[2] * (coeff[60] + powerX[0] * coeff[61] + powerX[1] * coeff[62] + powerX[2] * coeff[63]);
    derivative1[2] =                  factorY1
                    + 2 * powerY[0] * factorY2
                    + 3 * powerY[1] * factorY3;
    derivative2[2] =  2 *             factorY2
                    + 6 * powerY[0] * factorY3;

    // Pre-compute the factors for z
    final double factorZ1 =               (coeff[16] + powerX[0] * coeff[17] + powerX[1] * coeff[18] + powerX[2] * coeff[19])
                            + powerY[0] * (coeff[20] + powerX[0] * coeff[21] + powerX[1] * coeff[22] + powerX[2] * coeff[23])
                            + powerY[1] * (coeff[24] + powerX[0] * coeff[25] + powerX[1] * coeff[26] + powerX[2] * coeff[27])
                            + powerY[2] * (coeff[28] + powerX[0] * coeff[29] + powerX[1] * coeff[30] + powerX[2] * coeff[31]);
    final double factorZ2 =               (coeff[32] + powerX[0] * coeff[33] + powerX[1] * coeff[34] + powerX[2] * coeff[35])
                            + powerY[0] * (coeff[36] + powerX[0] * coeff[37] + powerX[1] * coeff[38] + powerX[2] * coeff[39])
                            + powerY[1] * (coeff[40] + powerX[0] * coeff[41] + powerX[1] * coeff[42] + powerX[2] * coeff[43])
                            + powerY[2] * (coeff[44] + powerX[0] * coeff[45] + powerX[1] * coeff[46] + powerX[2] * coeff[47]);
    final double factorZ3 =               (coeff[48] + powerX[0] * coeff[49] + powerX[1] * coeff[50] + powerX[2] * coeff[51])
                            + powerY[0] * (coeff[52] + powerX[0] * coeff[53] + powerX[1] * coeff[54] + powerX[2] * coeff[55])
                            + powerY[1] * (coeff[56] + powerX[0] * coeff[57] + powerX[1] * coeff[58] + powerX[2] * coeff[59])
                            + powerY[2] * (coeff[60] + powerX[0] * coeff[61] + powerX[1] * coeff[62] + powerX[2] * coeff[63]);
    derivative1[2] =                  factorZ1
                    + 2 * powerZ[0] * factorZ2
                    + 3 * powerZ[1] * factorZ3;
    derivative2[2] =  2 *             factorZ2
                    + 6 * powerZ[0] * factorZ3;

    // Note: The computation for value0 is arranged using zyx so reuse the factors for z
    // and compute the remaining polynomial.
    return                        (coeff[ 0] + powerX[0] * coeff[ 1] + powerX[1] * coeff[ 2] + powerX[2] * coeff[ 3])
                    + powerY[0] * (coeff[ 4] + powerX[0] * coeff[ 5] + powerX[1] * coeff[ 6] + powerX[2] * coeff[ 7])
                    + powerY[1] * (coeff[ 8] + powerX[0] * coeff[ 9] + powerX[1] * coeff[10] + powerX[2] * coeff[11])
                    + powerY[2] * (coeff[12] + powerX[0] * coeff[13] + powerX[1] * coeff[14] + powerX[2] * coeff[15])
            + powerZ[0] * factorZ1
            + powerZ[1] * factorZ2
            + powerZ[2] * factorZ3;
    //@formatter:on
  }

  // CHECKSTYLE.ON: VariableDeclarationUsageDistance

  public double value(double[] table) {
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];
  }

  public double value(float[] table) {
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];
  }

  public double value(double[] table, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];
  }

  public double value(float[] table, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];
  }

  public double value(double[] table, double[] table2, double[] table3, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + table2[1] * coeff[2] + table3[2] * coeff[3]
        + table[4] * coeff[5] + table2[5] * coeff[6] + table3[6] * coeff[7] + table[8] * coeff[9]
        + table2[9] * coeff[10] + table3[10] * coeff[11] + table[12] * coeff[13]
        + table2[13] * coeff[14] + table3[14] * coeff[15] + table[16] * coeff[17]
        + table2[17] * coeff[18] + table3[18] * coeff[19] + table[20] * coeff[21]
        + table2[21] * coeff[22] + table3[22] * coeff[23] + table[24] * coeff[25]
        + table2[25] * coeff[26] + table3[26] * coeff[27] + table[28] * coeff[29]
        + table2[29] * coeff[30] + table3[30] * coeff[31] + table[32] * coeff[33]
        + table2[33] * coeff[34] + table3[34] * coeff[35] + table[36] * coeff[37]
        + table2[37] * coeff[38] + table3[38] * coeff[39] + table[40] * coeff[41]
        + table2[41] * coeff[42] + table3[42] * coeff[43] + table[44] * coeff[45]
        + table2[45] * coeff[46] + table3[46] * coeff[47] + table[48] * coeff[49]
        + table2[49] * coeff[50] + table3[50] * coeff[51] + table[52] * coeff[53]
        + table2[53] * coeff[54] + table3[54] * coeff[55] + table[56] * coeff[57]
        + table2[57] * coeff[58] + table3[58] * coeff[59] + table[60] * coeff[61]
        + table2[61] * coeff[62] + table3[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + table2[4] * coeff[8] + table2[5] * coeff[9] + table2[6] * coeff[10]
        + table2[7] * coeff[11] + table3[8] * coeff[12] + table3[9] * coeff[13]
        + table3[10] * coeff[14] + table3[11] * coeff[15] + table[16] * coeff[20]
        + table[17] * coeff[21] + table[18] * coeff[22] + table[19] * coeff[23]
        + table2[20] * coeff[24] + table2[21] * coeff[25] + table2[22] * coeff[26]
        + table2[23] * coeff[27] + table3[24] * coeff[28] + table3[25] * coeff[29]
        + table3[26] * coeff[30] + table3[27] * coeff[31] + table[32] * coeff[36]
        + table[33] * coeff[37] + table[34] * coeff[38] + table[35] * coeff[39]
        + table2[36] * coeff[40] + table2[37] * coeff[41] + table2[38] * coeff[42]
        + table2[39] * coeff[43] + table3[40] * coeff[44] + table3[41] * coeff[45]
        + table3[42] * coeff[46] + table3[43] * coeff[47] + table[48] * coeff[52]
        + table[49] * coeff[53] + table[50] * coeff[54] + table[51] * coeff[55]
        + table2[52] * coeff[56] + table2[53] * coeff[57] + table2[54] * coeff[58]
        + table2[55] * coeff[59] + table3[56] * coeff[60] + table3[57] * coeff[61]
        + table3[58] * coeff[62] + table3[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + table2[16] * coeff[32]
        + table2[17] * coeff[33] + table2[18] * coeff[34] + table2[19] * coeff[35]
        + table2[20] * coeff[36] + table2[21] * coeff[37] + table2[22] * coeff[38]
        + table2[23] * coeff[39] + table2[24] * coeff[40] + table2[25] * coeff[41]
        + table2[26] * coeff[42] + table2[27] * coeff[43] + table2[28] * coeff[44]
        + table2[29] * coeff[45] + table2[30] * coeff[46] + table2[31] * coeff[47]
        + table3[32] * coeff[48] + table3[33] * coeff[49] + table3[34] * coeff[50]
        + table3[35] * coeff[51] + table3[36] * coeff[52] + table3[37] * coeff[53]
        + table3[38] * coeff[54] + table3[39] * coeff[55] + table3[40] * coeff[56]
        + table3[41] * coeff[57] + table3[42] * coeff[58] + table3[43] * coeff[59]
        + table3[44] * coeff[60] + table3[45] * coeff[61] + table3[46] * coeff[62]
        + table3[47] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];

  }

  public double value(float[] table, float[] table2, float[] table3, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + table2[1] * coeff[2] + table3[2] * coeff[3]
        + table[4] * coeff[5] + table2[5] * coeff[6] + table3[6] * coeff[7] + table[8] * coeff[9]
        + table2[9] * coeff[10] + table3[10] * coeff[11] + table[12] * coeff[13]
        + table2[13] * coeff[14] + table3[14] * coeff[15] + table[16] * coeff[17]
        + table2[17] * coeff[18] + table3[18] * coeff[19] + table[20] * coeff[21]
        + table2[21] * coeff[22] + table3[22] * coeff[23] + table[24] * coeff[25]
        + table2[25] * coeff[26] + table3[26] * coeff[27] + table[28] * coeff[29]
        + table2[29] * coeff[30] + table3[30] * coeff[31] + table[32] * coeff[33]
        + table2[33] * coeff[34] + table3[34] * coeff[35] + table[36] * coeff[37]
        + table2[37] * coeff[38] + table3[38] * coeff[39] + table[40] * coeff[41]
        + table2[41] * coeff[42] + table3[42] * coeff[43] + table[44] * coeff[45]
        + table2[45] * coeff[46] + table3[46] * coeff[47] + table[48] * coeff[49]
        + table2[49] * coeff[50] + table3[50] * coeff[51] + table[52] * coeff[53]
        + table2[53] * coeff[54] + table3[54] * coeff[55] + table[56] * coeff[57]
        + table2[57] * coeff[58] + table3[58] * coeff[59] + table[60] * coeff[61]
        + table2[61] * coeff[62] + table3[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + table2[4] * coeff[8] + table2[5] * coeff[9] + table2[6] * coeff[10]
        + table2[7] * coeff[11] + table3[8] * coeff[12] + table3[9] * coeff[13]
        + table3[10] * coeff[14] + table3[11] * coeff[15] + table[16] * coeff[20]
        + table[17] * coeff[21] + table[18] * coeff[22] + table[19] * coeff[23]
        + table2[20] * coeff[24] + table2[21] * coeff[25] + table2[22] * coeff[26]
        + table2[23] * coeff[27] + table3[24] * coeff[28] + table3[25] * coeff[29]
        + table3[26] * coeff[30] + table3[27] * coeff[31] + table[32] * coeff[36]
        + table[33] * coeff[37] + table[34] * coeff[38] + table[35] * coeff[39]
        + table2[36] * coeff[40] + table2[37] * coeff[41] + table2[38] * coeff[42]
        + table2[39] * coeff[43] + table3[40] * coeff[44] + table3[41] * coeff[45]
        + table3[42] * coeff[46] + table3[43] * coeff[47] + table[48] * coeff[52]
        + table[49] * coeff[53] + table[50] * coeff[54] + table[51] * coeff[55]
        + table2[52] * coeff[56] + table2[53] * coeff[57] + table2[54] * coeff[58]
        + table2[55] * coeff[59] + table3[56] * coeff[60] + table3[57] * coeff[61]
        + table3[58] * coeff[62] + table3[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + table2[16] * coeff[32]
        + table2[17] * coeff[33] + table2[18] * coeff[34] + table2[19] * coeff[35]
        + table2[20] * coeff[36] + table2[21] * coeff[37] + table2[22] * coeff[38]
        + table2[23] * coeff[39] + table2[24] * coeff[40] + table2[25] * coeff[41]
        + table2[26] * coeff[42] + table2[27] * coeff[43] + table2[28] * coeff[44]
        + table2[29] * coeff[45] + table2[30] * coeff[46] + table2[31] * coeff[47]
        + table3[32] * coeff[48] + table3[33] * coeff[49] + table3[34] * coeff[50]
        + table3[35] * coeff[51] + table3[36] * coeff[52] + table3[37] * coeff[53]
        + table3[38] * coeff[54] + table3[39] * coeff[55] + table3[40] * coeff[56]
        + table3[41] * coeff[57] + table3[42] * coeff[58] + table3[43] * coeff[59]
        + table3[44] * coeff[60] + table3[45] * coeff[61] + table3[46] * coeff[62]
        + table3[47] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];

  }

  public double value(double[] table, double[] derivative1, double[] derivative2) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
    derivative2[0] = 2 * table[0] * coeff[2] + 6 * table[1] * coeff[3] + 2 * table[4] * coeff[6]
        + 6 * table[5] * coeff[7] + 2 * table[8] * coeff[10] + 6 * table[9] * coeff[11]
        + 2 * table[12] * coeff[14] + 6 * table[13] * coeff[15] + 2 * table[16] * coeff[18]
        + 6 * table[17] * coeff[19] + 2 * table[20] * coeff[22] + 6 * table[21] * coeff[23]
        + 2 * table[24] * coeff[26] + 6 * table[25] * coeff[27] + 2 * table[28] * coeff[30]
        + 6 * table[29] * coeff[31] + 2 * table[32] * coeff[34] + 6 * table[33] * coeff[35]
        + 2 * table[36] * coeff[38] + 6 * table[37] * coeff[39] + 2 * table[40] * coeff[42]
        + 6 * table[41] * coeff[43] + 2 * table[44] * coeff[46] + 6 * table[45] * coeff[47]
        + 2 * table[48] * coeff[50] + 6 * table[49] * coeff[51] + 2 * table[52] * coeff[54]
        + 6 * table[53] * coeff[55] + 2 * table[56] * coeff[58] + 6 * table[57] * coeff[59]
        + 2 * table[60] * coeff[62] + 6 * table[61] * coeff[63];
    derivative2[1] = 2 * table[0] * coeff[8] + 2 * table[1] * coeff[9] + 2 * table[2] * coeff[10]
        + 2 * table[3] * coeff[11] + 6 * table[4] * coeff[12] + 6 * table[5] * coeff[13]
        + 6 * table[6] * coeff[14] + 6 * table[7] * coeff[15] + 2 * table[16] * coeff[24]
        + 2 * table[17] * coeff[25] + 2 * table[18] * coeff[26] + 2 * table[19] * coeff[27]
        + 6 * table[20] * coeff[28] + 6 * table[21] * coeff[29] + 6 * table[22] * coeff[30]
        + 6 * table[23] * coeff[31] + 2 * table[32] * coeff[40] + 2 * table[33] * coeff[41]
        + 2 * table[34] * coeff[42] + 2 * table[35] * coeff[43] + 6 * table[36] * coeff[44]
        + 6 * table[37] * coeff[45] + 6 * table[38] * coeff[46] + 6 * table[39] * coeff[47]
        + 2 * table[48] * coeff[56] + 2 * table[49] * coeff[57] + 2 * table[50] * coeff[58]
        + 2 * table[51] * coeff[59] + 6 * table[52] * coeff[60] + 6 * table[53] * coeff[61]
        + 6 * table[54] * coeff[62] + 6 * table[55] * coeff[63];
    derivative2[2] = 2 * table[0] * coeff[32] + 2 * table[1] * coeff[33] + 2 * table[2] * coeff[34]
        + 2 * table[3] * coeff[35] + 2 * table[4] * coeff[36] + 2 * table[5] * coeff[37]
        + 2 * table[6] * coeff[38] + 2 * table[7] * coeff[39] + 2 * table[8] * coeff[40]
        + 2 * table[9] * coeff[41] + 2 * table[10] * coeff[42] + 2 * table[11] * coeff[43]
        + 2 * table[12] * coeff[44] + 2 * table[13] * coeff[45] + 2 * table[14] * coeff[46]
        + 2 * table[15] * coeff[47] + 6 * table[16] * coeff[48] + 6 * table[17] * coeff[49]
        + 6 * table[18] * coeff[50] + 6 * table[19] * coeff[51] + 6 * table[20] * coeff[52]
        + 6 * table[21] * coeff[53] + 6 * table[22] * coeff[54] + 6 * table[23] * coeff[55]
        + 6 * table[24] * coeff[56] + 6 * table[25] * coeff[57] + 6 * table[26] * coeff[58]
        + 6 * table[27] * coeff[59] + 6 * table[28] * coeff[60] + 6 * table[29] * coeff[61]
        + 6 * table[30] * coeff[62] + 6 * table[31] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];

  }

  public double value(float[] table, double[] derivative1, double[] derivative2) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
    derivative2[0] = 2 * table[0] * coeff[2] + 6 * table[1] * coeff[3] + 2 * table[4] * coeff[6]
        + 6 * table[5] * coeff[7] + 2 * table[8] * coeff[10] + 6 * table[9] * coeff[11]
        + 2 * table[12] * coeff[14] + 6 * table[13] * coeff[15] + 2 * table[16] * coeff[18]
        + 6 * table[17] * coeff[19] + 2 * table[20] * coeff[22] + 6 * table[21] * coeff[23]
        + 2 * table[24] * coeff[26] + 6 * table[25] * coeff[27] + 2 * table[28] * coeff[30]
        + 6 * table[29] * coeff[31] + 2 * table[32] * coeff[34] + 6 * table[33] * coeff[35]
        + 2 * table[36] * coeff[38] + 6 * table[37] * coeff[39] + 2 * table[40] * coeff[42]
        + 6 * table[41] * coeff[43] + 2 * table[44] * coeff[46] + 6 * table[45] * coeff[47]
        + 2 * table[48] * coeff[50] + 6 * table[49] * coeff[51] + 2 * table[52] * coeff[54]
        + 6 * table[53] * coeff[55] + 2 * table[56] * coeff[58] + 6 * table[57] * coeff[59]
        + 2 * table[60] * coeff[62] + 6 * table[61] * coeff[63];
    derivative2[1] = 2 * table[0] * coeff[8] + 2 * table[1] * coeff[9] + 2 * table[2] * coeff[10]
        + 2 * table[3] * coeff[11] + 6 * table[4] * coeff[12] + 6 * table[5] * coeff[13]
        + 6 * table[6] * coeff[14] + 6 * table[7] * coeff[15] + 2 * table[16] * coeff[24]
        + 2 * table[17] * coeff[25] + 2 * table[18] * coeff[26] + 2 * table[19] * coeff[27]
        + 6 * table[20] * coeff[28] + 6 * table[21] * coeff[29] + 6 * table[22] * coeff[30]
        + 6 * table[23] * coeff[31] + 2 * table[32] * coeff[40] + 2 * table[33] * coeff[41]
        + 2 * table[34] * coeff[42] + 2 * table[35] * coeff[43] + 6 * table[36] * coeff[44]
        + 6 * table[37] * coeff[45] + 6 * table[38] * coeff[46] + 6 * table[39] * coeff[47]
        + 2 * table[48] * coeff[56] + 2 * table[49] * coeff[57] + 2 * table[50] * coeff[58]
        + 2 * table[51] * coeff[59] + 6 * table[52] * coeff[60] + 6 * table[53] * coeff[61]
        + 6 * table[54] * coeff[62] + 6 * table[55] * coeff[63];
    derivative2[2] = 2 * table[0] * coeff[32] + 2 * table[1] * coeff[33] + 2 * table[2] * coeff[34]
        + 2 * table[3] * coeff[35] + 2 * table[4] * coeff[36] + 2 * table[5] * coeff[37]
        + 2 * table[6] * coeff[38] + 2 * table[7] * coeff[39] + 2 * table[8] * coeff[40]
        + 2 * table[9] * coeff[41] + 2 * table[10] * coeff[42] + 2 * table[11] * coeff[43]
        + 2 * table[12] * coeff[44] + 2 * table[13] * coeff[45] + 2 * table[14] * coeff[46]
        + 2 * table[15] * coeff[47] + 6 * table[16] * coeff[48] + 6 * table[17] * coeff[49]
        + 6 * table[18] * coeff[50] + 6 * table[19] * coeff[51] + 6 * table[20] * coeff[52]
        + 6 * table[21] * coeff[53] + 6 * table[22] * coeff[54] + 6 * table[23] * coeff[55]
        + 6 * table[24] * coeff[56] + 6 * table[25] * coeff[57] + 6 * table[26] * coeff[58]
        + 6 * table[27] * coeff[59] + 6 * table[28] * coeff[60] + 6 * table[29] * coeff[61]
        + 6 * table[30] * coeff[62] + 6 * table[31] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];

  }

  public double value(double[] table, double[] table2, double[] table3, double[] table6,
      double[] derivative1, double[] derivative2) {
    derivative1[0] = table[0] * coeff[1] + table2[1] * coeff[2] + table3[2] * coeff[3]
        + table[4] * coeff[5] + table2[5] * coeff[6] + table3[6] * coeff[7] + table[8] * coeff[9]
        + table2[9] * coeff[10] + table3[10] * coeff[11] + table[12] * coeff[13]
        + table2[13] * coeff[14] + table3[14] * coeff[15] + table[16] * coeff[17]
        + table2[17] * coeff[18] + table3[18] * coeff[19] + table[20] * coeff[21]
        + table2[21] * coeff[22] + table3[22] * coeff[23] + table[24] * coeff[25]
        + table2[25] * coeff[26] + table3[26] * coeff[27] + table[28] * coeff[29]
        + table2[29] * coeff[30] + table3[30] * coeff[31] + table[32] * coeff[33]
        + table2[33] * coeff[34] + table3[34] * coeff[35] + table[36] * coeff[37]
        + table2[37] * coeff[38] + table3[38] * coeff[39] + table[40] * coeff[41]
        + table2[41] * coeff[42] + table3[42] * coeff[43] + table[44] * coeff[45]
        + table2[45] * coeff[46] + table3[46] * coeff[47] + table[48] * coeff[49]
        + table2[49] * coeff[50] + table3[50] * coeff[51] + table[52] * coeff[53]
        + table2[53] * coeff[54] + table3[54] * coeff[55] + table[56] * coeff[57]
        + table2[57] * coeff[58] + table3[58] * coeff[59] + table[60] * coeff[61]
        + table2[61] * coeff[62] + table3[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + table2[4] * coeff[8] + table2[5] * coeff[9] + table2[6] * coeff[10]
        + table2[7] * coeff[11] + table3[8] * coeff[12] + table3[9] * coeff[13]
        + table3[10] * coeff[14] + table3[11] * coeff[15] + table[16] * coeff[20]
        + table[17] * coeff[21] + table[18] * coeff[22] + table[19] * coeff[23]
        + table2[20] * coeff[24] + table2[21] * coeff[25] + table2[22] * coeff[26]
        + table2[23] * coeff[27] + table3[24] * coeff[28] + table3[25] * coeff[29]
        + table3[26] * coeff[30] + table3[27] * coeff[31] + table[32] * coeff[36]
        + table[33] * coeff[37] + table[34] * coeff[38] + table[35] * coeff[39]
        + table2[36] * coeff[40] + table2[37] * coeff[41] + table2[38] * coeff[42]
        + table2[39] * coeff[43] + table3[40] * coeff[44] + table3[41] * coeff[45]
        + table3[42] * coeff[46] + table3[43] * coeff[47] + table[48] * coeff[52]
        + table[49] * coeff[53] + table[50] * coeff[54] + table[51] * coeff[55]
        + table2[52] * coeff[56] + table2[53] * coeff[57] + table2[54] * coeff[58]
        + table2[55] * coeff[59] + table3[56] * coeff[60] + table3[57] * coeff[61]
        + table3[58] * coeff[62] + table3[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + table2[16] * coeff[32]
        + table2[17] * coeff[33] + table2[18] * coeff[34] + table2[19] * coeff[35]
        + table2[20] * coeff[36] + table2[21] * coeff[37] + table2[22] * coeff[38]
        + table2[23] * coeff[39] + table2[24] * coeff[40] + table2[25] * coeff[41]
        + table2[26] * coeff[42] + table2[27] * coeff[43] + table2[28] * coeff[44]
        + table2[29] * coeff[45] + table2[30] * coeff[46] + table2[31] * coeff[47]
        + table3[32] * coeff[48] + table3[33] * coeff[49] + table3[34] * coeff[50]
        + table3[35] * coeff[51] + table3[36] * coeff[52] + table3[37] * coeff[53]
        + table3[38] * coeff[54] + table3[39] * coeff[55] + table3[40] * coeff[56]
        + table3[41] * coeff[57] + table3[42] * coeff[58] + table3[43] * coeff[59]
        + table3[44] * coeff[60] + table3[45] * coeff[61] + table3[46] * coeff[62]
        + table3[47] * coeff[63];
    derivative2[0] = table2[0] * coeff[2] + table6[1] * coeff[3] + table2[4] * coeff[6]
        + table6[5] * coeff[7] + table2[8] * coeff[10] + table6[9] * coeff[11]
        + table2[12] * coeff[14] + table6[13] * coeff[15] + table2[16] * coeff[18]
        + table6[17] * coeff[19] + table2[20] * coeff[22] + table6[21] * coeff[23]
        + table2[24] * coeff[26] + table6[25] * coeff[27] + table2[28] * coeff[30]
        + table6[29] * coeff[31] + table2[32] * coeff[34] + table6[33] * coeff[35]
        + table2[36] * coeff[38] + table6[37] * coeff[39] + table2[40] * coeff[42]
        + table6[41] * coeff[43] + table2[44] * coeff[46] + table6[45] * coeff[47]
        + table2[48] * coeff[50] + table6[49] * coeff[51] + table2[52] * coeff[54]
        + table6[53] * coeff[55] + table2[56] * coeff[58] + table6[57] * coeff[59]
        + table2[60] * coeff[62] + table6[61] * coeff[63];
    derivative2[1] = table2[0] * coeff[8] + table2[1] * coeff[9] + table2[2] * coeff[10]
        + table2[3] * coeff[11] + table6[4] * coeff[12] + table6[5] * coeff[13]
        + table6[6] * coeff[14] + table6[7] * coeff[15] + table2[16] * coeff[24]
        + table2[17] * coeff[25] + table2[18] * coeff[26] + table2[19] * coeff[27]
        + table6[20] * coeff[28] + table6[21] * coeff[29] + table6[22] * coeff[30]
        + table6[23] * coeff[31] + table2[32] * coeff[40] + table2[33] * coeff[41]
        + table2[34] * coeff[42] + table2[35] * coeff[43] + table6[36] * coeff[44]
        + table6[37] * coeff[45] + table6[38] * coeff[46] + table6[39] * coeff[47]
        + table2[48] * coeff[56] + table2[49] * coeff[57] + table2[50] * coeff[58]
        + table2[51] * coeff[59] + table6[52] * coeff[60] + table6[53] * coeff[61]
        + table6[54] * coeff[62] + table6[55] * coeff[63];
    derivative2[2] = table2[0] * coeff[32] + table2[1] * coeff[33] + table2[2] * coeff[34]
        + table2[3] * coeff[35] + table2[4] * coeff[36] + table2[5] * coeff[37]
        + table2[6] * coeff[38] + table2[7] * coeff[39] + table2[8] * coeff[40]
        + table2[9] * coeff[41] + table2[10] * coeff[42] + table2[11] * coeff[43]
        + table2[12] * coeff[44] + table2[13] * coeff[45] + table2[14] * coeff[46]
        + table2[15] * coeff[47] + table6[16] * coeff[48] + table6[17] * coeff[49]
        + table6[18] * coeff[50] + table6[19] * coeff[51] + table6[20] * coeff[52]
        + table6[21] * coeff[53] + table6[22] * coeff[54] + table6[23] * coeff[55]
        + table6[24] * coeff[56] + table6[25] * coeff[57] + table6[26] * coeff[58]
        + table6[27] * coeff[59] + table6[28] * coeff[60] + table6[29] * coeff[61]
        + table6[30] * coeff[62] + table6[31] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];

  }

  public double value(float[] table, float[] table2, float[] table3, float[] table6,
      double[] derivative1, double[] derivative2) {
    derivative1[0] = table[0] * coeff[1] + table2[1] * coeff[2] + table3[2] * coeff[3]
        + table[4] * coeff[5] + table2[5] * coeff[6] + table3[6] * coeff[7] + table[8] * coeff[9]
        + table2[9] * coeff[10] + table3[10] * coeff[11] + table[12] * coeff[13]
        + table2[13] * coeff[14] + table3[14] * coeff[15] + table[16] * coeff[17]
        + table2[17] * coeff[18] + table3[18] * coeff[19] + table[20] * coeff[21]
        + table2[21] * coeff[22] + table3[22] * coeff[23] + table[24] * coeff[25]
        + table2[25] * coeff[26] + table3[26] * coeff[27] + table[28] * coeff[29]
        + table2[29] * coeff[30] + table3[30] * coeff[31] + table[32] * coeff[33]
        + table2[33] * coeff[34] + table3[34] * coeff[35] + table[36] * coeff[37]
        + table2[37] * coeff[38] + table3[38] * coeff[39] + table[40] * coeff[41]
        + table2[41] * coeff[42] + table3[42] * coeff[43] + table[44] * coeff[45]
        + table2[45] * coeff[46] + table3[46] * coeff[47] + table[48] * coeff[49]
        + table2[49] * coeff[50] + table3[50] * coeff[51] + table[52] * coeff[53]
        + table2[53] * coeff[54] + table3[54] * coeff[55] + table[56] * coeff[57]
        + table2[57] * coeff[58] + table3[58] * coeff[59] + table[60] * coeff[61]
        + table2[61] * coeff[62] + table3[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + table2[4] * coeff[8] + table2[5] * coeff[9] + table2[6] * coeff[10]
        + table2[7] * coeff[11] + table3[8] * coeff[12] + table3[9] * coeff[13]
        + table3[10] * coeff[14] + table3[11] * coeff[15] + table[16] * coeff[20]
        + table[17] * coeff[21] + table[18] * coeff[22] + table[19] * coeff[23]
        + table2[20] * coeff[24] + table2[21] * coeff[25] + table2[22] * coeff[26]
        + table2[23] * coeff[27] + table3[24] * coeff[28] + table3[25] * coeff[29]
        + table3[26] * coeff[30] + table3[27] * coeff[31] + table[32] * coeff[36]
        + table[33] * coeff[37] + table[34] * coeff[38] + table[35] * coeff[39]
        + table2[36] * coeff[40] + table2[37] * coeff[41] + table2[38] * coeff[42]
        + table2[39] * coeff[43] + table3[40] * coeff[44] + table3[41] * coeff[45]
        + table3[42] * coeff[46] + table3[43] * coeff[47] + table[48] * coeff[52]
        + table[49] * coeff[53] + table[50] * coeff[54] + table[51] * coeff[55]
        + table2[52] * coeff[56] + table2[53] * coeff[57] + table2[54] * coeff[58]
        + table2[55] * coeff[59] + table3[56] * coeff[60] + table3[57] * coeff[61]
        + table3[58] * coeff[62] + table3[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + table2[16] * coeff[32]
        + table2[17] * coeff[33] + table2[18] * coeff[34] + table2[19] * coeff[35]
        + table2[20] * coeff[36] + table2[21] * coeff[37] + table2[22] * coeff[38]
        + table2[23] * coeff[39] + table2[24] * coeff[40] + table2[25] * coeff[41]
        + table2[26] * coeff[42] + table2[27] * coeff[43] + table2[28] * coeff[44]
        + table2[29] * coeff[45] + table2[30] * coeff[46] + table2[31] * coeff[47]
        + table3[32] * coeff[48] + table3[33] * coeff[49] + table3[34] * coeff[50]
        + table3[35] * coeff[51] + table3[36] * coeff[52] + table3[37] * coeff[53]
        + table3[38] * coeff[54] + table3[39] * coeff[55] + table3[40] * coeff[56]
        + table3[41] * coeff[57] + table3[42] * coeff[58] + table3[43] * coeff[59]
        + table3[44] * coeff[60] + table3[45] * coeff[61] + table3[46] * coeff[62]
        + table3[47] * coeff[63];
    derivative2[0] = table2[0] * coeff[2] + table6[1] * coeff[3] + table2[4] * coeff[6]
        + table6[5] * coeff[7] + table2[8] * coeff[10] + table6[9] * coeff[11]
        + table2[12] * coeff[14] + table6[13] * coeff[15] + table2[16] * coeff[18]
        + table6[17] * coeff[19] + table2[20] * coeff[22] + table6[21] * coeff[23]
        + table2[24] * coeff[26] + table6[25] * coeff[27] + table2[28] * coeff[30]
        + table6[29] * coeff[31] + table2[32] * coeff[34] + table6[33] * coeff[35]
        + table2[36] * coeff[38] + table6[37] * coeff[39] + table2[40] * coeff[42]
        + table6[41] * coeff[43] + table2[44] * coeff[46] + table6[45] * coeff[47]
        + table2[48] * coeff[50] + table6[49] * coeff[51] + table2[52] * coeff[54]
        + table6[53] * coeff[55] + table2[56] * coeff[58] + table6[57] * coeff[59]
        + table2[60] * coeff[62] + table6[61] * coeff[63];
    derivative2[1] = table2[0] * coeff[8] + table2[1] * coeff[9] + table2[2] * coeff[10]
        + table2[3] * coeff[11] + table6[4] * coeff[12] + table6[5] * coeff[13]
        + table6[6] * coeff[14] + table6[7] * coeff[15] + table2[16] * coeff[24]
        + table2[17] * coeff[25] + table2[18] * coeff[26] + table2[19] * coeff[27]
        + table6[20] * coeff[28] + table6[21] * coeff[29] + table6[22] * coeff[30]
        + table6[23] * coeff[31] + table2[32] * coeff[40] + table2[33] * coeff[41]
        + table2[34] * coeff[42] + table2[35] * coeff[43] + table6[36] * coeff[44]
        + table6[37] * coeff[45] + table6[38] * coeff[46] + table6[39] * coeff[47]
        + table2[48] * coeff[56] + table2[49] * coeff[57] + table2[50] * coeff[58]
        + table2[51] * coeff[59] + table6[52] * coeff[60] + table6[53] * coeff[61]
        + table6[54] * coeff[62] + table6[55] * coeff[63];
    derivative2[2] = table2[0] * coeff[32] + table2[1] * coeff[33] + table2[2] * coeff[34]
        + table2[3] * coeff[35] + table2[4] * coeff[36] + table2[5] * coeff[37]
        + table2[6] * coeff[38] + table2[7] * coeff[39] + table2[8] * coeff[40]
        + table2[9] * coeff[41] + table2[10] * coeff[42] + table2[11] * coeff[43]
        + table2[12] * coeff[44] + table2[13] * coeff[45] + table2[14] * coeff[46]
        + table2[15] * coeff[47] + table6[16] * coeff[48] + table6[17] * coeff[49]
        + table6[18] * coeff[50] + table6[19] * coeff[51] + table6[20] * coeff[52]
        + table6[21] * coeff[53] + table6[22] * coeff[54] + table6[23] * coeff[55]
        + table6[24] * coeff[56] + table6[25] * coeff[57] + table6[26] * coeff[58]
        + table6[27] * coeff[59] + table6[28] * coeff[60] + table6[29] * coeff[61]
        + table6[30] * coeff[62] + table6[31] * coeff[63];
    return table[0] * coeff[0] + table[1] * coeff[1] + table[2] * coeff[2] + table[3] * coeff[3]
        + table[4] * coeff[4] + table[5] * coeff[5] + table[6] * coeff[6] + table[7] * coeff[7]
        + table[8] * coeff[8] + table[9] * coeff[9] + table[10] * coeff[10] + table[11] * coeff[11]
        + table[12] * coeff[12] + table[13] * coeff[13] + table[14] * coeff[14]
        + table[15] * coeff[15] + table[16] * coeff[16] + table[17] * coeff[17]
        + table[18] * coeff[18] + table[19] * coeff[19] + table[20] * coeff[20]
        + table[21] * coeff[21] + table[22] * coeff[22] + table[23] * coeff[23]
        + table[24] * coeff[24] + table[25] * coeff[25] + table[26] * coeff[26]
        + table[27] * coeff[27] + table[28] * coeff[28] + table[29] * coeff[29]
        + table[30] * coeff[30] + table[31] * coeff[31] + table[32] * coeff[32]
        + table[33] * coeff[33] + table[34] * coeff[34] + table[35] * coeff[35]
        + table[36] * coeff[36] + table[37] * coeff[37] + table[38] * coeff[38]
        + table[39] * coeff[39] + table[40] * coeff[40] + table[41] * coeff[41]
        + table[42] * coeff[42] + table[43] * coeff[43] + table[44] * coeff[44]
        + table[45] * coeff[45] + table[46] * coeff[46] + table[47] * coeff[47]
        + table[48] * coeff[48] + table[49] * coeff[49] + table[50] * coeff[50]
        + table[51] * coeff[51] + table[52] * coeff[52] + table[53] * coeff[53]
        + table[54] * coeff[54] + table[55] * coeff[55] + table[56] * coeff[56]
        + table[57] * coeff[57] + table[58] * coeff[58] + table[59] * coeff[59]
        + table[60] * coeff[60] + table[61] * coeff[61] + table[62] * coeff[62]
        + table[63] * coeff[63];
  }

  public void gradient(double[] table, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
  }

  public void gradient(float[] table, double[] derivative1) {
    derivative1[0] = table[0] * coeff[1] + 2 * table[1] * coeff[2] + 3 * table[2] * coeff[3]
        + table[4] * coeff[5] + 2 * table[5] * coeff[6] + 3 * table[6] * coeff[7]
        + table[8] * coeff[9] + 2 * table[9] * coeff[10] + 3 * table[10] * coeff[11]
        + table[12] * coeff[13] + 2 * table[13] * coeff[14] + 3 * table[14] * coeff[15]
        + table[16] * coeff[17] + 2 * table[17] * coeff[18] + 3 * table[18] * coeff[19]
        + table[20] * coeff[21] + 2 * table[21] * coeff[22] + 3 * table[22] * coeff[23]
        + table[24] * coeff[25] + 2 * table[25] * coeff[26] + 3 * table[26] * coeff[27]
        + table[28] * coeff[29] + 2 * table[29] * coeff[30] + 3 * table[30] * coeff[31]
        + table[32] * coeff[33] + 2 * table[33] * coeff[34] + 3 * table[34] * coeff[35]
        + table[36] * coeff[37] + 2 * table[37] * coeff[38] + 3 * table[38] * coeff[39]
        + table[40] * coeff[41] + 2 * table[41] * coeff[42] + 3 * table[42] * coeff[43]
        + table[44] * coeff[45] + 2 * table[45] * coeff[46] + 3 * table[46] * coeff[47]
        + table[48] * coeff[49] + 2 * table[49] * coeff[50] + 3 * table[50] * coeff[51]
        + table[52] * coeff[53] + 2 * table[53] * coeff[54] + 3 * table[54] * coeff[55]
        + table[56] * coeff[57] + 2 * table[57] * coeff[58] + 3 * table[58] * coeff[59]
        + table[60] * coeff[61] + 2 * table[61] * coeff[62] + 3 * table[62] * coeff[63];
    derivative1[1] = table[0] * coeff[4] + table[1] * coeff[5] + table[2] * coeff[6]
        + table[3] * coeff[7] + 2 * table[4] * coeff[8] + 2 * table[5] * coeff[9]
        + 2 * table[6] * coeff[10] + 2 * table[7] * coeff[11] + 3 * table[8] * coeff[12]
        + 3 * table[9] * coeff[13] + 3 * table[10] * coeff[14] + 3 * table[11] * coeff[15]
        + table[16] * coeff[20] + table[17] * coeff[21] + table[18] * coeff[22]
        + table[19] * coeff[23] + 2 * table[20] * coeff[24] + 2 * table[21] * coeff[25]
        + 2 * table[22] * coeff[26] + 2 * table[23] * coeff[27] + 3 * table[24] * coeff[28]
        + 3 * table[25] * coeff[29] + 3 * table[26] * coeff[30] + 3 * table[27] * coeff[31]
        + table[32] * coeff[36] + table[33] * coeff[37] + table[34] * coeff[38]
        + table[35] * coeff[39] + 2 * table[36] * coeff[40] + 2 * table[37] * coeff[41]
        + 2 * table[38] * coeff[42] + 2 * table[39] * coeff[43] + 3 * table[40] * coeff[44]
        + 3 * table[41] * coeff[45] + 3 * table[42] * coeff[46] + 3 * table[43] * coeff[47]
        + table[48] * coeff[52] + table[49] * coeff[53] + table[50] * coeff[54]
        + table[51] * coeff[55] + 2 * table[52] * coeff[56] + 2 * table[53] * coeff[57]
        + 2 * table[54] * coeff[58] + 2 * table[55] * coeff[59] + 3 * table[56] * coeff[60]
        + 3 * table[57] * coeff[61] + 3 * table[58] * coeff[62] + 3 * table[59] * coeff[63];
    derivative1[2] = table[0] * coeff[16] + table[1] * coeff[17] + table[2] * coeff[18]
        + table[3] * coeff[19] + table[4] * coeff[20] + table[5] * coeff[21] + table[6] * coeff[22]
        + table[7] * coeff[23] + table[8] * coeff[24] + table[9] * coeff[25] + table[10] * coeff[26]
        + table[11] * coeff[27] + table[12] * coeff[28] + table[13] * coeff[29]
        + table[14] * coeff[30] + table[15] * coeff[31] + 2 * table[16] * coeff[32]
        + 2 * table[17] * coeff[33] + 2 * table[18] * coeff[34] + 2 * table[19] * coeff[35]
        + 2 * table[20] * coeff[36] + 2 * table[21] * coeff[37] + 2 * table[22] * coeff[38]
        + 2 * table[23] * coeff[39] + 2 * table[24] * coeff[40] + 2 * table[25] * coeff[41]
        + 2 * table[26] * coeff[42] + 2 * table[27] * coeff[43] + 2 * table[28] * coeff[44]
        + 2 * table[29] * coeff[45] + 2 * table[30] * coeff[46] + 2 * table[31] * coeff[47]
        + 3 * table[32] * coeff[48] + 3 * table[33] * coeff[49] + 3 * table[34] * coeff[50]
        + 3 * table[35] * coeff[51] + 3 * table[36] * coeff[52] + 3 * table[37] * coeff[53]
        + 3 * table[38] * coeff[54] + 3 * table[39] * coeff[55] + 3 * table[40] * coeff[56]
        + 3 * table[41] * coeff[57] + 3 * table[42] * coeff[58] + 3 * table[43] * coeff[59]
        + 3 * table[44] * coeff[60] + 3 * table[45] * coeff[61] + 3 * table[46] * coeff[62]
        + 3 * table[47] * coeff[63];
  }
}
