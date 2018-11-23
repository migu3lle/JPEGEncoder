package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.DCTBlock;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.DCTI;

public class StandardDCT implements DCTI {

    /*
    Author: Michael Gundacker 1646765
     */

    @Override
    public DCTBlockI forward(BlockI b) {
        int[][] lsSamples = levelShift(b);
        double[][] dctCoeffs = new double[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {

                double firstFactor = C(u) * C(v) / 4;

                double outerSum = 0;
                for (int i = 0; i < 8; i++) {
                    double innerSum = 0;
                    for (int j = 0; j < 8; j++) {
                        innerSum += lsSamples[i][j] * Math.cos((2*i + 1) * u * Math.PI / 16) * Math.cos((2*j + 1) * v * Math.PI / 16);
                    }
                    outerSum += innerSum;
                }

                dctCoeffs[u][v] = firstFactor * outerSum;
            }
        }
        return new DCTBlock(dctCoeffs);
    }

    private double C(int x) {
        if(x == 0){
            return 1 / Math.sqrt(2);
        }
        return 1;
    }

    private int[][] levelShift(BlockI block) {
        int[][] data = block.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = data[i][j] - (int) Math.pow(2,7);
            }
        }
        return data;
    }

}
