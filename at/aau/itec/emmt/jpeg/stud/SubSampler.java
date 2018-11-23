package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Component;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.SubSamplerI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

public class SubSampler implements SubSamplerI {

    /*
    Author: Gundacker Michael 1646765
    */

    @Override
    public YUVImageI downSample(YUVImageI yuvImg, int samplingRatio) {
        int yuvHeight = (int) (yuvImg.getComponent(YUVImageI.CB_COMP).getSize().getHeight());
        int yuvWidth= (int) (yuvImg.getComponent(YUVImageI.CB_COMP).getSize().getWidth());

        int[][] cbDataOld = yuvImg.getComponent(YUVImageI.CB_COMP).getData();
        int[][] crDataOld = yuvImg.getComponent(YUVImageI.CR_COMP).getData();

        int[][] cbDataNew;
        int[][] crDataNew;

        if(samplingRatio == 1){
            //Sample each second pixel in x-dimension and write to new data array
            cbDataNew = new int[yuvHeight][yuvWidth / 2];
            crDataNew = new int[yuvHeight][yuvWidth / 2];
            //Loop through new array and assign values
            for (int i = 0; i < cbDataNew.length; i++) {
                for (int j = 0, x = 0; j < cbDataNew[0].length; j++, x+=2) {
                    cbDataNew[i][j] = cbDataOld[i][x];
                    crDataNew[i][j] = crDataOld[i][x];
                }
            }
        }
        else if(samplingRatio == 2){
            //Sample the median of four-block pixels and write to new data array
            cbDataNew = new int[yuvHeight / 2][yuvWidth / 2];
            crDataNew = new int[yuvHeight / 2][yuvWidth / 2];
            //Loop through new array and assign values
            for (int i = 0, y = 0; i < cbDataNew.length; i++, y+=2) {
                for (int j = 0, x = 0; j < cbDataNew[0].length; j++, x+=2) {
                    //Assign average chrominanz value of a four-block
                    cbDataNew[i][j] = (cbDataOld[y][x] + cbDataOld[y][x+1] + cbDataOld[y+1][x] + cbDataOld[y+1][x+1]) / 4;
                    crDataNew[i][j] = (crDataOld[y][x] + crDataOld[y][x+1] + crDataOld[y+1][x] + crDataOld[y+1][x+1]) / 4;
                }
            }
        }
        else{
            cbDataNew = yuvImg.getComponent(YUVImageI.CB_COMP).getData();
            crDataNew = yuvImg.getComponent(YUVImageI.CR_COMP).getData();
        }
        return  buildYuvImage(yuvImg, cbDataNew, crDataNew, samplingRatio);
    }

    //Helper method to build new YUV Image
    private YUVImageI buildYuvImage(YUVImageI yuvImgOld, int[][] cbDataNew, int[][] crDataNew, int samplingRatio) {
        Component yComponent = new Component(yuvImgOld.getComponent(0).getData(), YUVImageI.Y_COMP);
        Component cbComponent = new Component(cbDataNew, YUVImageI.CB_COMP);
        Component crComponent = new Component(crDataNew, YUVImageI.CR_COMP);

        return new YUVImage(yComponent, cbComponent, crComponent, samplingRatio);
    }
}
