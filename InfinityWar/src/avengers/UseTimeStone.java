package avengers;
import java.util.*;

/**
 * Given a starting event and an Adjacency Matrix representing a graph of all possible 
 * events once Thanos arrives on Titan, determine the total possible number of timelines 
 * that could occur AND the number of timelines with a total Expected Utility (EU) at 
 * least the threshold value.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * UseTimeStoneInputFile name is passed through the command line as args[0]
 * Read from UseTimeStoneInputFile with the format:
 *    1. t (int): expected utility (EU) threshold
 *    2. v (int): number of events (vertices in the graph)
 *    3. v lines, each with 2 values: (int) event number and (int) EU value
 *    4. v lines, each with v (int) edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note 1: the last v lines of the UseTimeStoneInputFile is an ajacency matrix for a directed
 * graph. 
 * The rows represent the "from" vertex and the columns represent the "to" vertex.
 * 
 * The matrix below has only two edges: (1) from vertex 1 to vertex 3 and, (2) from vertex 2 to vertex 0
 * 0 0 0 0
 * 0 0 0 1
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * UseTimeStoneOutputFile name is passed through the command line as args[1]
 * Assume the starting event is vertex 0 (zero)
 * Compute all the possible timelines, output this number to the output file.
 * Compute all the posssible timelines with Expected Utility higher than the EU threshold,
 * output this number to the output file.
 * 
 * Note 2: output these number the in above order, one per line.
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for total number of timelines
 *     //Call StdOut.print() for number of timelines with EU >= threshold EU 
 * 
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/UseTimeStone usetimestone.in usetimestone.out
 * 
 * @author Yashas Ravi
 * 
 */

public class UseTimeStone {

    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java UseTimeStone <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);


        int thresh = StdIn.readInt();
        int vertices = StdIn.readInt();
        int EU = 0;
        int totalPath = 0;
        int euPaths = 0;
        int[] eu = new int [vertices];
        int [][] adjMat = new int[vertices][vertices];
        HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adjLists = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            adjLists[i] = new ArrayList<>();
        }

        for (int i = 0; i < vertices; i++){
            eu[StdIn.readInt()] = StdIn.readInt();
        }

        for (int i = 0; i < vertices; i++){
            for (int j = 0; j < vertices; j++){
                adjMat[i][j] = StdIn.readInt();
                if (adjMat[i][j] == 1) adjLists[i].add(j);
            }
        }
        map.putAll(dfs(0, thresh, EU, eu, adjLists, map));

        for (Integer key : map.keySet()){
            totalPath++;
            if (map.get(key) == true){
                euPaths++;
            }
        }
        StdOut.print(totalPath + "\n" + euPaths);
    }

    private static HashMap<Integer, Boolean> dfs(int vertex, int trsh, int EU, int[] eu, 
        ArrayList<Integer>[] adjLists, HashMap<Integer, Boolean> map){
            EU += eu[vertex];
            int key = (int) (Math.random() * Integer.MAX_VALUE) + 1;
            if (trsh<=EU){
                map.put(key, true);
            }else{
                map.put(key, false);
            }


            Iterator<Integer> iterator = adjLists[vertex].iterator();
            while (iterator.hasNext()){
                int adj = iterator.next();
                map.putAll(dfs(adj, trsh, EU, eu, adjLists, map));
            }
            return map;
    }
}
