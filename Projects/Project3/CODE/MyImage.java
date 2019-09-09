import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MyImage{

    private BufferedImage image;
    Double[][] contents; 
    
    public MyImage(BufferedImage image){
        this.image = image;
        updateContentMatrix();
    }

    public BufferedImage getImage(){
        return this.image;
    }

    public Integer getHeight(){
        return this.image.getHeight();
    }

    public Integer getWidth(){
        return this.image.getWidth();
    }

    public Double[][] getContentMatrix(){
        return this.contents;
    }

    //Input: A vertical path, 
    //       represented as a vector with length = height of the image,
    //       and values in [0,width-1]
    //Result: The path is deleted from the image, 
    //        and the image and content fiels are updated accordingly
    public void deleteVerticalPath(int[] pixelIDs){
        boolean leftDrawn;
        boolean rightDrawn;
        boolean rowDrawn;
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        //Object below will finally contain the new image. 
        //It is like a canvas at which the new image is added row by row 
        BufferedImage resultImage = new BufferedImage(width-1,height,BufferedImage.TYPE_INT_RGB);
        //For each row of pixels in the original image,
        //pixelIDs[row] is the pixel of the path that is in that row
        //divide row into the part left of pixelIDs[row] and that right of pixelIDs[row]
        //concatenate the two subrows, and add them to the "canvas"
        for(int row=0;row<height;row++){
            if(pixelIDs[row]==0){
                BufferedImage temp = this.image.getSubimage(1, row, width-1, 1);
                rowDrawn = resultImage.createGraphics().drawImage(temp,0,row,null);
                if(!rowDrawn) System.out.println("Problems drawing rowimage");
            }
            else if(pixelIDs[row]==width-1){
                BufferedImage temp = this.image.getSubimage(0, row, width-1, 1);
                rowDrawn = resultImage.createGraphics().drawImage(temp,0,row,null);
                if(!rowDrawn) System.out.println("Problems drawing rowimage");
            }
            else{
                BufferedImage left = this.image.getSubimage(0, row, pixelIDs[row], 1);
                BufferedImage right = this.image.getSubimage(pixelIDs[row]+1, row, width-1-pixelIDs[row], 1);
                BufferedImage temp = new BufferedImage(width-1,1,BufferedImage.TYPE_INT_RGB);
                leftDrawn = temp.createGraphics().drawImage(left,0,0,null);
                if(!leftDrawn) System.out.println("Problems drawing left image");
                rightDrawn = temp.createGraphics().drawImage(right,pixelIDs[row],0,null);
                if(!rightDrawn) System.out.println("Problems drawing right image");
                rowDrawn = resultImage.createGraphics().drawImage(temp,0,row,null);
                if(!rowDrawn) System.out.println("Problems drawing rowimage");
            }
        }
        this.image = resultImage;
        updateContentMatrix();
    }

    //Result: It updates the content matrix to reflect the current image.
    //        Pixel-by-pixel, it computes and stores its content value in the matrix.
    private void updateContentMatrix(){
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int top;
        int bottom;
        int left;
        int right;
        contents = new Double[height][width];
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                if(y!=0){
                    top = image.getRGB(x, y-1);
                }
                else{
                    top = image.getRGB(x,y);
                }
                if(y!=height-1){
                    bottom = image.getRGB(x,y+1);
                }
                else{
                    bottom = image.getRGB(x,y);
                }
                if(x!=0){
                    left = image.getRGB(x-1, y);
                }
                else{
                    left = image.getRGB(x,y);
                }
                if(x!=width-1){
                    right = image.getRGB(x+1,y);
                }
                else{
                    right = image.getRGB(x, y);
                }
                contents[y][x] = computePixelContent(top,bottom,left,right);
            } 
        }
    }

    //Helper function for computing the contentMatrix
    private Double computePixelContent(int top, int bottom, int left, int right){
        return overallDistance(top,bottom)+overallDistance(left, right);
    }

    //Helper function for computing the contentMatrix
    private Double overallDistance(int pixel1,int pixel2){
        return colorDistance(pixel1, pixel2, "red")+colorDistance(pixel1, pixel2,"green")+colorDistance(pixel1, pixel2,"blue");
    }

    //Helper function for computing the contentMatrix
    public static Double colorDistance(int pixel1, int pixel2, String color){
        double result;
        if(color.equals("red")){
            int r1 = (pixel1>>16) & 0xff;
            int r2 = (pixel2>>16) & 0xff;
            result = Math.pow(r1-r2,2);
        }
        else if(color.equals("green")){
            int g1 = (pixel1>>8) & 0xff;
            int g2 = (pixel2>>8) & 0xff;
            result = Math.pow(g1-g2,2);
        }
        int b1 = pixel1 & 0xff;
        int b2 = pixel2 & 0xff;
        result =  Math.pow(b1-b2,2);
        return result;
    }
}