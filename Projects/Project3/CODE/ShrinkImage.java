import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.*;

public class ShrinkImage{
    //Input: An image and its corresponding content matrix stored in an imageObject,
    //       a goal width
    //Result: The original image reduced to the desired width,
    //        by successive removals of the minimum vertical path.
    public void cleverWidthReduction(MyImage imageObject, int desiredWidth){
        while(imageObject.getWidth() != desiredWidth){
            int[] pixelIDs = getPixelIDs(imageObject);
            imageObject.deleteVerticalPath(pixelIDs);
        }
        return;
    }

    public int[] getPixelIDs(MyImage imageObject){
        Double[][] contents = imageObject.getContentMatrix();
        int H = imageObject.getHeight();
        int W = imageObject.getWidth();
        Double[][] DP = new Double[H][W];

        for(int i = 0; i < H; i++){
            for(int j = 0; j < W; j++){
                if(i == 0) DP[i][j] = contents[i][j];
                else if(j == 0) DP[i][j] = contents[i][j] + Math.min(DP[i - 1][j], DP[i - 1][j + 1]);
                else if(j == W - 1) DP[i][j] = contents[i][j] + Math.min(DP[i - 1][j], DP[i - 1][j - 1]);
                else{
                    DP[i][j] = contents[i][j] + Math.min(DP[i - 1][j - 1], Math.min(DP[i - 1][j], DP[i - 1][j + 1]));
                }
                // System.out.println("i = "+ i +" j = "+ j +" " + DP[i][j] + " ");
            }
        }

        Double minSumPixel = DP[H - 1][W - 1];
        int minSumPixelIndex = W - 1;
        for(int j = 0; j < W; j++){
            if(DP[H - 1][j] < minSumPixel){
                minSumPixel = DP[H - 1][j];
                minSumPixelIndex = j;
            }
        }
        // System.out.println("minSumPixel = " + minSumPixel + "; Index = " + minSumPixelIndex);
        int[] pixelIDs = new int[H];
        pixelIDs[H - 1] = minSumPixelIndex;
        for(int i = H - 2; i > 0; i--){
            minSumPixel = minSumPixel - contents[i + 1][minSumPixelIndex];
            // System.out.print("target Sum = " + minSumPixel + "; ");
            // if(minSumPixelIndex - 1 >= 0) System.out.print(DP[i][minSumPixelIndex - 1] + " ");
            // System.out.print(DP[i][minSumPixelIndex] + " ");
            // System.out.print(DP[i][minSumPixelIndex + 1] + " ");
            if(minSumPixelIndex - 1 >= 0 && minSumPixel.equals(DP[i][minSumPixelIndex - 1])){
                minSumPixelIndex -= 1;
                pixelIDs[i] = minSumPixelIndex;
            }else if(minSumPixel.equals(DP[i][minSumPixelIndex])){
                pixelIDs[i] = minSumPixelIndex;
            }else if(minSumPixel.equals(DP[i][minSumPixelIndex + 1])){
                minSumPixelIndex += 1;
                pixelIDs[i] = minSumPixelIndex;
            }
            // System.out.println("pixelIDs[" + i + "] = " + pixelIDs[i]);
        }


        return pixelIDs;
    }

}
