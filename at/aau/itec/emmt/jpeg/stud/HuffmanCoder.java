package at.aau.itec.emmt.jpeg.stud;

import java.util.ArrayList;

import at.aau.itec.emmt.jpeg.impl.AbstractHuffmanCoder;
import at.aau.itec.emmt.jpeg.impl.RunLevel;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.EntropyCoderI;
import at.aau.itec.emmt.jpeg.spec.RunLevelI;

public class HuffmanCoder extends AbstractHuffmanCoder {

    @Override
    public RunLevelI[] runLengthEncode(BlockI quantBlock) {

    	 ArrayList<RunLevel> temp = new ArrayList<>();
    	 
         int[] quantArray = new int[quantBlock.getData().length * quantBlock.getData()[0].length];

         for (int y = 0; y < quantBlock.getData().length; y++) {
             for (int x = 0; x < quantBlock.getData()[y].length; x++) {
                 quantArray[quantBlock.getData().length * y + x] = quantBlock.getData()[y][x];
             }
         }

         int[] zigZag = EntropyCoderI.ZIGZAG_ORDER;
         int zeros = 0;

         for (int i = 1; i < quantArray.length; i++) {
             if (quantArray[zigZag[i]] == 0) {
                 zeros++;
             } else {
                 temp.add(new RunLevel(zeros, quantArray[zigZag[i]]));
                 zeros = 0;
             }
         }

         if (zeros > 0) {
             temp.add(new RunLevel(zeros, 0));
         }

         RunLevel[] out = new RunLevel[temp.size()];
         temp.toArray(out);

         return out;
       
    }
}
