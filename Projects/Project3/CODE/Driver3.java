import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver3{

    public static MyImage imageObject;
    public static Integer desiredWidth;
    public static String outputFileName;
    public static String fileType;
    public static ArrayList<Integer> values;
    public static ArrayList<Integer> weights;
    public static int capacity;
    public static Boolean suitcase;
    public static ArrayList<Integer> selectedItems;

    public static void main(String[] args) throws Exception{
        parseArgs(args);
        testRun();
        printOrSaveResult();
    }

    public static void parseArgs(String[] args) throws Exception{
        //Verify that there are either one or three inputs given
        if(args.length!=3 && args.length!=1){
            System.out.println("Input Error: Wrong amount of input arguments provided.");
            System.exit(0);
        }
        if(args.length==3){
            suitcase = false;
            //Verify that image type is either .png or .jpg
            fileType = args[1];
            if(!fileType.equals("png") && !fileType.equals("jpg")) {
                System.out.println("Input Error: Image file type must be either png or jpg");
                System.exit(0);
            }
            //Verify that image file provided exists.
            File imageFile = new File(args[0]+"."+args[1]);
            if(!imageFile.exists()){
                System.out.println("Input Error: Image file not found.");
                System.exit(0);
            }
            outputFileName = args[0]+"_reduced."+args[1];
            //Read file to a BufferedImage object.
            BufferedImage inputImage = ImageIO.read(imageFile);
            imageObject = new MyImage(inputImage);
            //Verify that desired width provided is an integer.
            try {
                desiredWidth = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException e) {
                System.out.println("Input Error: Desired width must be an int value.");
                System.exit(0);
            }
            if(desiredWidth>inputImage.getWidth()){
                System.out.println("Input Error: Desired width must be smaller than original width.");
                System.exit(0);
            }
            else if(desiredWidth<=0){
                System.out.println("Input Error: Desired width must be a positive integer.");
                System.exit(0);
            }
        }
        else{
            suitcase = true;
            values = new ArrayList<Integer>();
            weights = new ArrayList<Integer>();
            Scanner sc = new Scanner(new File(args[0]));
            String[] nextLine;
            if(sc.hasNextLine()){
                nextLine = sc.nextLine().split(" ");
                capacity = Integer.parseInt(nextLine[0]);
            }
            else{
                System.out.println("Input Error: Input file has the wrong format / first line is empty.");
                System.exit(0);
            }
            while(sc.hasNextLine()){
                nextLine = sc.nextLine().split(" ");
                if(nextLine.length==2){
                    values.add(Integer.parseInt(nextLine[0]));
                    weights.add(Integer.parseInt(nextLine[1]));
                }
            }
            sc.close();
        }

    }

    public static void testRun(){
        Program3 program = new Program3();
        if(suitcase){
            selectedItems = program.maximizeSentimentalValue(weights, values, capacity);
        }
        else{
            ShrinkImage prog = new ShrinkImage();
            prog.cleverWidthReduction(imageObject,desiredWidth);

        }
    }

    public static void printOrSaveResult() throws IOException, Exception{
        if(suitcase){
            System.out.println("The indices of the selected items are: ");
            for(int i=0;i<selectedItems.size();i++){
                System.out.println(String.valueOf(selectedItems.get(i)));
            }

        }
        else{
            File finalImageFile = new File(outputFileName);
            boolean finalImageDrawing = ImageIO.write(imageObject.getImage(), fileType, finalImageFile);
            if(!finalImageDrawing){
                System.out.println("Drawing final image failed.");
            }
            System.out.println("Result has successfuly been saved at "+outputFileName);
        }

    }
}
