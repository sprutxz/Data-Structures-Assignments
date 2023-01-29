package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

	    /* Your code goes here */

        sortedCharFreqList = new ArrayList<CharFreq>();
        char ch;
        int totalChar = 0;
        int [] freq = new int [128];

        //increase frequency in freq[] array and calculates total characters
        while (StdIn.hasNextChar()){
            ch = StdIn.readChar();
            freq[ch]++;
            totalChar++;
        }

        //populated arrayList
        for (int i = 0; i < 128; i++){
            if(freq[i] != 0){
                sortedCharFreqList.add(new CharFreq((char) i, (double) freq[i]/totalChar));
            }
        }

        //handle only 1 unique char
        if (sortedCharFreqList.size() == 1){
            int charInList = (int)sortedCharFreqList.get(0).getCharacter();

            if (charInList!=127){
                charInList++;
            }else{
                charInList = 0;
            }
            sortedCharFreqList.add(new CharFreq((char) charInList, (double) 0));
        }

        Collections.sort(sortedCharFreqList); //sort the ArrayList
            
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

        Queue<CharFreq> source = new Queue<CharFreq>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        for (int i = 0; i < sortedCharFreqList.size(); i++){
            source.enqueue(sortedCharFreqList.get(i));
        }

        while(!source.isEmpty() || target.size() != 1){
            nodeMaker(source, target);
        }
        huffmanRoot = target.dequeue(); //set huffman root
    }

    //A private method to make a huffman tree 
    private void nodeMaker(Queue<CharFreq> src, Queue<TreeNode> trg){
        TreeNode newNode1 = new TreeNode();
        TreeNode newNode2 = new TreeNode();
        TreeNode rootNode = new TreeNode();
        
        if (trg.isEmpty()){// if target is empty

            newNode1.setData(src.dequeue());
            newNode2.setData(src.dequeue());

            //make treeNode root and add it to target queue
            CharFreq root = new CharFreq();
            root.setCharacter(null);
            root.setProbOcc(newNode1.getData().getProbOcc()+newNode2.getData().getProbOcc());

            rootNode.setData(root);
            rootNode.setLeft(newNode1);
            rootNode.setRight(newNode2);

            trg.enqueue(rootNode);
        }else if(src.isEmpty()){// if src empty

            newNode1 = trg.dequeue();
            newNode2 = trg.dequeue();

            //make treeNode root and add it to target queue
            CharFreq root = new CharFreq();
            root.setCharacter(null);
            root.setProbOcc(newNode1.getData().getProbOcc()+newNode2.getData().getProbOcc());

            rootNode.setData(root);
            rootNode.setLeft(newNode1);
            rootNode.setRight(newNode2);

            trg.enqueue(rootNode);            
        }else{
            //first dequeue
            if(src.peek().getProbOcc() <= trg.peek().getData().getProbOcc()){
                newNode1.setData(src.dequeue());
            }else{
                newNode1 = trg.dequeue();
            }

            //second dequeue
            if(!src.isEmpty() && !trg.isEmpty()){
                if(src.peek().getProbOcc() <= trg.peek().getData().getProbOcc()){
                    newNode2.setData(src.dequeue());
                }else{
                    newNode2 = trg.dequeue();
                }
            }else if (trg.isEmpty()){
                newNode2.setData(src.dequeue());
            }else{
                newNode2 = trg.dequeue();
            }

            //make treeNode root and add it to target queue
            CharFreq root = new CharFreq();
            root.setCharacter(null);
            root.setProbOcc(newNode1.getData().getProbOcc()+newNode2.getData().getProbOcc());

            rootNode.setData(root);
            rootNode.setLeft(newNode1);
            rootNode.setRight(newNode2);

            trg.enqueue(rootNode);
        }

    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

	    /* Your code goes here */
        encodings = new String[128];
        String encoding = "";
        TreeNode head = huffmanRoot;

        recursive(head, encoding);// use the recursive algorithm
        
    }
     
    // recursive method
    private void recursive (TreeNode root, String enc){

        //base case
        if(root.getLeft() == null && root.getRight() == null){
            encodings[root.getData().getCharacter()] = enc;
            return;
        }

        recursive(root.getLeft(), enc + "0");
        recursive(root.getRight(), enc + "1");

    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	    /* Your code goes here */
        String encodedString = "";

        //input from file to encoded String
        while(StdIn.hasNextChar()){
            encodedString += encodings[StdIn.readChar()];
        }

        writeBitString(encodedFile, encodedString);//write String to bits
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	    /* Your code goes here */
        int i = 0;
        TreeNode traverse = huffmanRoot;
        String bitString = readBitString(encodedFile);
        String decodedString = "";

        while(i < bitString.length()){
            //traverse tree
            if(bitString.charAt(i) == '0'){
                traverse = traverse.getLeft();
            }else{
                traverse = traverse.getRight();
            }
            i++;//increase char counter
            
            //leaf node reached
            if(traverse.getLeft() == null && traverse.getRight() == null){
                decodedString += traverse.getData().getCharacter();
                traverse = huffmanRoot; 
            }
        }
        StdOut.print(decodedString);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
