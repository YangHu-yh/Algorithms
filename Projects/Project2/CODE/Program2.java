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
            // create a node with its index and frequency
            // Node(Integer index, Integer frequency, Node left, Node right)
            Node nodei = new Node(i, charFrequencies.get(i));
            // add to priorityqueue
            pq.add(nodei);
        }

        // create a root node, nodes in the middle all have index -1 and frequency -1.
        Node root = new Node(-1, -1);

        // while queue  size is larger than 1, construct the tree
        while(pq.size() > 1){
            // poll smallest node from priorityqueue pq
            // then poll the second smallest again
            Node right = pq.peek();
            pq.poll();
            Node left = pq.peek();
            pq.poll();

            // Set a parent node of top two nodes (smallest two nodes) in the tree
            Node parent = new Node(-1, left.getFrequency() + right.getFrequency(), left, right);
            left.setParent(parent);
            right.setParent(parent);

            // Add the new parent back to the PriorityQueue
            pq.add(parent);
        }
        // the last node in the queue is the root node we are looking for to return
        root = pq.poll();
        return root;
    }

    public ArrayList<String> computeEncoding(Node root, ArrayList<String> huffman_encoding){
        // creating a queue to store nodes and poll nodes from left to right in each layer of the tree
        LinkedList<Node> queue = new LinkedList<Node>();
        // start with an empty string for the next layer of nodes to add 0(left) or 1(right)
        root.setBinaryEncoding("");
        // put the root in the queue to start
        queue.add(root);
        // Plan for this loop
        // In each loop, we first poll out one previous layer node(start with root)
        // then either end as a leaf and give the corresponding binary Encoding
        // or add an additional 0(left) or 1(right) in the field of biEnoding and continuously add its children to the queue
        while(!queue.isEmpty()){
            // poll the first node in the queue and record its binary encoding
            Node curnode = queue.poll();
            String curBiEncoding = curnode.getBinaryEncoding();
            // In case of leaf
            // set corresponding huffman encoding
            if(curnode.getLeft() == null && curnode.getRight() == null){
                huffman_encoding.set(curnode.getIndex(), curBiEncoding);
            }else{// In case no a leaf, has children
                // if has right child
                if(curnode.getRight() != null){
                    // Give encoding to this node and add to the queue
                    Node right = curnode.getRight();
                    String rightBiEncoding = curBiEncoding + "1";
                    right.setBinaryEncoding(rightBiEncoding);
                    queue.add(right);
                }
                // if has left child
                if(curnode.getLeft() != null){
                    // Give encoding to this node and add to the queue
                    Node left = curnode.getLeft();
                    String leftBiEncoding = curBiEncoding + "0";
                    left.setBinaryEncoding(leftBiEncoding);
                    queue.add(curnode.getLeft());
                }
            }
        }
        // By the end of the while loop, all nodes will be giving an encoding but only leaves contains corresponding huffman encoding for each character we need, all other nodes are helping leaf nodes to get its encoding. Return the encoding.
        return huffman_encoding;
    }
}

// Set comparator for priorityqueue to select node with the less frequent on the top of the tree
class MyComparator implements Comparator<Node> {
    public int compare(Node Node1, Node Node2){
        return Node1.getFrequency() - Node2.getFrequency();
    }
}
