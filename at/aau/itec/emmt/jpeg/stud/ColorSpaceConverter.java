package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Component;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.ColorSpaceConverterI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

import java.awt.*;
import java.awt.image.PixelGrabber;

/**
 * Author: Gundacker Michael 1646765
 */

public class ColorSpaceConverter implements ColorSpaceConverterI {

    @Override
    public YUVImageI convertRGBToYUV(Image rgbImg) {

        PixelGrabber grabber = new PixelGrabber(rgbImg, 0, 0, -1, -1, false);
        try {
            if (grabber.grabPixels()) {

                if (grabber.getPixels() instanceof int[]) {
                    int[] data = (int[]) grabber.getPixels();
                    int width = grabber.getWidth();
                    return convertData(data, width);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private YUVImage convertData(int[] data, int width) {
        int[][] yData = new int[width][data.length / width];
        int[][] cbData = new int[width][data.length / width];
        int[][] crData = new int[width][data.length / width];

        for (int i = 0, row = 0, column = 0; i < data.length; i++, column++) {
            if (column > width - 1) {
                row++;
                column = 0;
            }
            Color c = new Color(data[i]);
            yData[column][row] = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
            cbData[column][row] = (int) (128 - 0.1687 * c.getRed() - 0.3313 * c.getGreen() + 0.5 * c.getBlue());
            crData[column][row] = (int) (128 + 0.5 * c.getRed() - 0.4187 * c.getGreen() - 0.0813 * c.getBlue());
        }
        Component yComponent = new Component(yData, 0);
        Component cbComponent = new Component(cbData, 1);
        Component crComponent = new Component(crData, 2);
        return new YUVImage(yComponent, cbComponent, crComponent, 0);
    }
}