import java.util.ArrayList;
import java.util.PriorityQueue;
// ?
import java.util.*;

public class Program2{

    public Node computeEncodingTree(ArrayList<Integer> charFrequencies){
        // transform all char and frequencies to nodes
        // and add each Node to an priorityqueue
        PriorityQueue<Node> pq = new PriorityQueue<Node>(charFrequencies.size(), new MyComparator());

        for(int i = 0; i < charFrequencies.size(); i++){
            // create a node
            // Node(Integer index, Integer frequency, Node left, Node right)
            Node nodei = new Node(i, charFrequencies.get(i));
            // add to priorityqueue
            pq.add(nodei);
            // System.out.println("Added node"+i +" with freq " + nodei.getFrequency());
            // System.out.println("Test:");
            // System.out.println("First Node index " + pq.peek().getIndex());
            // System.out.println("First Node Freq "+pq.peek().getFrequency());
        }

        // create a root node, nodes in the middle all have index -1 and frequency -1.
        Node root = new Node(-1, -1);
        // System.out.println("Test:");
        // System.out.println("First Node index" + pq.peek().getIndex());
        // System.out.println("First Node "+pq.peek().getFrequency());

        // while queue not empty, construct the tree
        while(pq.size() > 1){
            Node right = pq.peek();
            System.out.println("A Node right, index " + right.getIndex() + " and freq = "+ right.getFrequency());
            pq.poll();
            Node left = pq.peek();
            System.out.println("A Node left, index " + left.getIndex() + " and freq = "+ left.getFrequency());
            pq.poll();

            // A parent node of above two
            Node parent = new Node(-1, left.getFrequency() + right.getFrequency(), left, right);
            left.setParent(parent);
            right.setParent(parent);
            System.out.println("Parent total freq = "+parent.getFrequency());

            // Add the new parent back to the PriorityQueue
            pq.add(parent);
        }
        root = pq.poll();
        System.out.println("root freq = "+root.getFrequency());
        return root;
    }

    public ArrayList<String> computeEncoding(Node root, ArrayList<String> huffman_encoding){

        LinkedList<Node> queue = new LinkedList<Node>();
        root.setBinaryEncoding("");
        queue.add(root);
        while(!queue.isEmpty()){
            Node curnode = queue.poll();
            String curBiEncoding = curnode.getBinaryEncoding();
            if(curnode.getLeft() == null && curnode.getRight() == null){
                huffman_encoding.set(curnode.getIndex(), curBiEncoding);
                System.out.println("Set Index = " + curnode.getIndex() + " binary encoding: "+ curBiEncoding);
            }else{
                if(curnode.getRight() != null){
                    Node right = curnode.getRight();
                    String rightBiEncoding = curBiEncoding + "1";
                    right.setBinaryEncoding(rightBiEncoding);
                    queue.add(right);
                    System.out.println("Mark Index = " + curnode.getRight().getIndex() + " binary encoding: "+ curBiEncoding);
                }
                if(curnode.getLeft() != null){
                    Node left = curnode.getLeft();
                    String leftBiEncoding = curBiEncoding + "0";
                    left.setBinaryEncoding(leftBiEncoding);
                    queue.add(curnode.getLeft());
                    System.out.println("Mark Index = " + curnode.getLeft().getIndex() + " binary encoding: "+ leftBiEncoding);
                }
            }
        }
        return huffman_encoding;
    }
}

class MyComparator implements Comparator<Node> {
    public int compare(Node Node1, Node Node2){
        return Node1.getFrequency() - Node2.getFrequency();
    }
}
