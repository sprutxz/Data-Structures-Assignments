package avengers;
import java.util.*;

/**
 * Given an adjacency matrix, use a random() function to remove half of the nodes. 
 * Then, write into the output file a boolean (true or false) indicating if 
 * the graph is still connected.
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * PredictThanosSnapInputFile name is passed through the command line as args[0]
 * Read from PredictThanosSnapInputFile with the format:
 *    1. seed (long): a seed for the random number generator
 *    2. p (int): number of people (vertices in the graph)
 *    2. p lines, each with p edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note: the last p lines of the PredictThanosSnapInputFile is an ajacency matrix for
 * an undirected graph. 
 * 
 * The matrix below has two edges 0-1, 0-2 (each edge appear twice in the matrix, 0-1, 1-0, 0-2, 2-0).
 * 
 * 0 1 1 0
 * 1 0 0 0
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * Delete random vertices from the graph. You can use the following pseudocode.
 * StdRandom.setSeed(seed);
 * for (all vertices, go from vertex 0 to the final vertex) { 
 *     if (StdRandom.uniform() <= 0.5) { 
 *          delete vertex;
 *     }
 * }
 * Answer the following question: is the graph (after deleting random vertices) connected?
 * Output true (connected graph), false (unconnected graph) to the output file.
 * 
 * Note 1: a connected graph is a graph where there is a path between EVERY vertex on the graph.
 * 
 * Note 2: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, isConnected is true if the graph is connected,
 *   false otherwise):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(isConnected);
 * 
 * @author Yashas Ravi
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/PredictThanosSnap predictthanossnap.in predictthanossnap.out
*/

public class PredictThanosSnap {
	 
    public static void main (String[] args) {
 
        if ( args.length < 2 ) {
            StdOut.println("Execute: java PredictThanosSnap <INput file> <OUTput file>");
            return;
        }
        
    	// WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // StdIn.setFile("predictthanossnap.in");
        // StdOut.setFile("predictthanossnap.out");

        long seed = StdIn.readLong();
        StdRandom.setSeed(seed);
        int nodes = StdIn.readInt();
        int nonDelVertex = nodes;
        int[][] adjMat = new int[nodes][nodes];
        boolean[] vertexDel = new boolean[nodes];
        boolean[] visited = new boolean[nodes];
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] adjLists = new ArrayList[nodes];

        for (int i = 0; i < nodes; i++) {
            adjLists[i] = new ArrayList<>();
        }

        //populate adjacency matrix from file
        for(int i = 0; i < nodes; i++){
            for(int j = 0; j < nodes; j++){
                adjMat[i][j] = StdIn.readInt();
            }
        }

        //delete nodes based on random value
        for (int i = 0; i < nodes; i++){
            if (StdRandom.uniform() <= 0.5){
              removeVertex(vertexDel, adjMat, i);
              nonDelVertex--;
            }
        }

        //populate adjacent nodes list
        for (int i = 0; i < nodes; i++){
            for (int j = 0; j < nodes; j++){
                if(adjMat[i][j] == 1){
                    adjLists[i].add(j);
                    adjLists[j].add(i);
                }
            }
        }

        for (int i = 0; i < nodes; i++){
            if(!vertexDel[i]){
                visited = dfs(i, adjLists, visited);
                break;
            }
        }     
        
        int count = 0;
        for (int i = 0; i < nodes; i++){
            if(visited[i]){
                count++;
            }
        }
        StdOut.print(count == nonDelVertex);

    }

    public static void removeVertex(boolean[] vertexDel, int[][] adjMat, int vertex){

        for (int i = 0; i < adjMat.length; i++){
            for (int j = 0; j < adjMat[i].length; j++){

                if(i == vertex || j == vertex){
                    vertexDel[vertex] = true;
                    adjMat[i][j] = 0;
                }
            }
        }
    }

    private static boolean[] dfs(int vertex, ArrayList<Integer>[] adjLists, boolean[] visited){

        visited[vertex] = true;

        Iterator<Integer> iterator = adjLists[vertex].iterator();

        while (iterator.hasNext()){
            int adj = iterator.next();
            if (!visited[adj]){
                visited = dfs(adj, adjLists, visited);
            }
        }

        return visited;
    }
}
