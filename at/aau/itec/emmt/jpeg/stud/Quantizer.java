package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.QuantizationI;

public class Quantizer implements QuantizationI {

    protected int qualityFactor;

    public Quantizer() {
        this(DEFAULT_QUALITY_FACTOR);
    }

    public Quantizer(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }
   
    @Override
    public int[] getQuantumLuminance() {
    	int scalingFactor;
    	
    	 if (qualityFactor < 50) {
            scalingFactor = 5000 / qualityFactor;
         }else{
          scalingFactor= 200 - 2 * qualityFactor;
          }   	
    	 
    	  int[] out = new int[QUANTUM_LUMINANCE.length];

          for (int i = 0; i < QUANTUM_LUMINANCE.length; i++) {
             out[i] = Math.min(255, Math.max(1, (QUANTUM_LUMINANCE[i] * scalingFactor + 50) /100));
            }
       	 
    	return out;
       
    }

    @Override
    public int[] getQuantumChrominance() {
    	
     int scalingFactor;
    	
   	 if (qualityFactor < 50) {
   		 
           scalingFactor = 5000 / qualityFactor;
        }else{
         scalingFactor= 200 - 2 * qualityFactor;
         }   	
   	 
   	  int[] out = new int[QUANTUM_CHROMINANCE.length];

         for (int i = 0; i < QUANTUM_CHROMINANCE.length; i++) {
            out[i] = Math.min(255, Math.max(1, (QUANTUM_CHROMINANCE[i] * scalingFactor + 50) /100));
           }
      	 
   	return out;
      
    }

    @Override
    public BlockI quantizeBlock(DCTBlockI dctBlock, int compType) {
      
    	 int[][] out = new int[dctBlock.getData().length][dctBlock.getData()[0].length];
    	 
    	 int[] quantum;
    	 if(compType == YUVImage.Y_COMP) {
    		 
            quantum=getQuantumLuminance();		 
    	 }else{
    		 
    		 quantum = getQuantumChrominance();
    	 }
    	
         for (int y = 0; y < dctBlock.getData().length; y++) {
             for (int x = 0; x < dctBlock.getData()[y].length; x++) {
                 out[y][x] = (int) Math.round(dctBlock.getData()[y][x] / quantum[y * dctBlock.getData().length + x]);
             }
         }
         return new Block(out);
    }

    @Override
    public void setQualityFactor(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

}
