import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver2{

    public static ArrayList<Integer> charFrequencies;
    public static ArrayList<String> characters;

    public static void main(String[] args) throws Exception{
        parseArgs(args);
        testRun(charFrequencies,characters);
    }

    public static void parseArgs(String[] args) throws Exception{
        parseFrequencies(args[0]);
    }

    public static void parseFrequencies(String inputFile) throws Exception{
        Scanner sc = new Scanner(new File(inputFile));
        String[] nextLine;
        charFrequencies = new ArrayList<Integer>();
        characters = new ArrayList<String>();
        while(sc.hasNextLine()){
            nextLine = sc.nextLine().split(" ");
            if(nextLine.length==2){
                characters.add(nextLine[0]);
                charFrequencies.add(Integer.parseInt(nextLine[1]));
            }
        }
        sc.close();
    }


    public static void testRun(ArrayList<Integer> charFrequencies,ArrayList<String> characters){
        Program2 program = new Program2();
        ArrayList<String> huffman_encoding = getEmptyHuffmanEncoding(charFrequencies.size());

        Node root = program.computeEncodingTree(charFrequencies);
        ArrayList<String> encoding = program.computeEncoding(root,huffman_encoding);

        System.out.println("The Huffman Encoding for the provided characters is:");
        for(int i=0;i<charFrequencies.size();i++){
            System.out.println(characters.get(i)+" "+encoding.get(i));
        }

        // System.out.println("Calculate the Average Encoded Word Length； ");
        long sum = 0;
        long freqSum = 0;
        for(int i = 0; i < charFrequencies.size(); i++){
            freqSum += charFrequencies.get(i);
            sum += charFrequencies.get(i) * encoding.get(i).length();
            // System.out.println("Freq: "+ charFrequencies.get(i) + " encoding length: " + encoding.get(i).length());
        }
        double result = sum*1.0/freqSum;
        System.out.println(result);
    }

    public static ArrayList<String> getEmptyHuffmanEncoding(int numberOfCharacters){
        ArrayList<String> huffman_encoding = new ArrayList<String>();
        for(int i=0;i<numberOfCharacters;i++){
            huffman_encoding.add("");
        }
        return huffman_encoding;
    }
}
