import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private Integer index;
    private Integer frequency;
    private Node left;
    private Node right;
    private Node parent;
    private String biEnoding;

    public int compareTo(Node m)
    {
        return this.frequency - m.frequency;
    }

    public Node(Integer index, Integer frequency){
        this.index = index;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Node(Integer index, Integer frequency, Node left, Node right){
        this.index = index;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.parent = null;
    }

    public Integer getIndex(){
        return this.index;
    }

    public Integer getFrequency(){
        return this.frequency;
    }

    public Node getLeft(){
        return this.left;
    }

    public Node getRight(){
        return this.right;
    }

    public Node getParent(){
        return this.parent;
    }

    public void setParent(Node p){
        this.parent = p;
    }

    public void setBinaryEncoding(String s){
        this.biEnoding = s;
    }

    public String getBinaryEncoding(){
        return this.biEnoding;
    }

}
