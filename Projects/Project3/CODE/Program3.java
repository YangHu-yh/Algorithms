import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.*;

public class Program3{

    // Knapsack problem
    //Input: The weights and values of each item, and the maximum allowed weight
    //Output: The indices of the items to be selected
    //        so as to maximize value without exceeding the capacity
    public ArrayList<Integer> maximizeSentimentalValue(ArrayList<Integer> weights, ArrayList<Integer> values, int capacity){
        int n = weights.size();
        ArrayList<Integer> selectedItems = new ArrayList<>();
        ArrayList<ArrayList<Integer>> DP = new ArrayList<>(n+1);
        for(int i = 0; i <= n; i++){
            DP.add(new ArrayList<>(capacity+1));
        }
        // int[][] DP = new int[n+1][W+1];
        for (int i = 0; i <= n; i++){
            for (int j = 0; j <= capacity; j++){
                if(i==0 || j==0){
                    DP.get(i).add(0);
                }else{
                    DP.get(i).add(DP.get(i-1).get(j));
                    if(weights.get(i-1) <= j){
                        int val = DP.get(i-1).get(j-weights.get(i-1)) + values.get(i-1);
                        if(DP.get(i).get(j) < val){
                            DP.get(i).set(j, val);
                        }
                    }
                // System.out.println("i = "+i);
                // System.out.println("j = "+j);
                // System.out.println("DP[i][j] = "+DP.get(i).get(j));
                }
            }
        }
        // System.out.println("i = "+i);
        // System.out.println("DP[i][capacity] = "+DP.get(i).get(capacity));
        // max value in DP[n][capacity];
        int maxValue = DP.get(n).get(capacity);
        int nowValue = DP.get(n).get(capacity);
        int itemIndex = n;
        int nowWeight = capacity;
        while(nowValue > 0){
            if(nowValue == DP.get(itemIndex - 1).get(nowWeight)){
                itemIndex -= 1;
            }else{
                selectedItems.add(itemIndex-1);
                nowWeight -= weights.get(itemIndex-1);
                nowValue -= values.get(itemIndex-1);
                itemIndex -= 1;
            }
        }


        return selectedItems;
    }

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
