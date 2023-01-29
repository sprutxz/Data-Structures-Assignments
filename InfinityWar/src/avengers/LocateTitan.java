package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        // if ( args.length < 2 ) {
        //     StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
        //     return;
        // }

    	// // WRITE YOUR CODE HERE
        // StdIn.setFile(args[0]);
        // StdOut.setFile(args[1]);
        StdIn.setFile("locatetitan.in");
        StdOut.setFile("locatetitan.out");

        int generators = StdIn.readInt();
        double[] func = new double[generators];
        int[][] adjMatrix = new int[generators][generators];

        for (int i = 0; i < generators; i++)
        {
            func[StdIn.readInt()] = StdIn.readDouble();
        }

        for (int i = 0; i < generators; i++)
        {
            for (int j = 0; j < generators; j++){
                adjMatrix[i][j] = StdIn.readInt();
            }
        }
        
        for (int i = 0; i < generators; i++)
        {
            for (int j = 0; j < generators; j++){
                adjMatrix[i][j] = (int) (adjMatrix[i][j]/(func[i]*func[j]));
            }
        }
        
        StdOut.print(dijkAlgo(adjMatrix));
    }

    private static int dijkAlgo (int[][] adjMat){
        int gen = adjMat.length;
        int [] minCost = new int[gen];//create min cost array
        boolean [] dijkSet = new boolean[gen];

        for (int i = 0; i < gen; i++)
        {
            minCost[i] = Integer.MAX_VALUE;

            if (i == 0) minCost[i] = 0;
        }

        for(int i = 0; i < gen-1; i++)
        {
            int curr = minCost(minCost, dijkSet);   
            dijkSet[curr] = true; 

            for (int j = 0; j < gen ; j++){
                if (adjMat[curr][j] != 0){
                    if (dijkSet[j] == false && minCost[curr] != Integer.MAX_VALUE 
                        && minCost[j] > minCost[curr] + adjMat[curr][j]){
                            minCost[j] = minCost[curr] + adjMat[curr][j];
                    } 
                }
            }
        }

        return minCost[adjMat.length-1];
    }

    public static int minCost (int[] minCost, boolean[] dijkSet){
        int min = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < minCost.length; i++){
            if (minCost[i] < min && !dijkSet[i]){
                min = minCost[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
