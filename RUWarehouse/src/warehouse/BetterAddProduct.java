package warehouse;

/*
 * Use this class to test the betterAddProduct method.
 */ 
public class BetterAddProduct {
    public static void main(String[] args) {
        // StdIn.setFile(args[0]);
        // StdOut.setFile(args[1]);
        StdIn.setFile("betteraddproduct.in");
        StdOut.setFile("betteraddproduct.out");
        
        // Use this file to test betterAddProduct
        Warehouse warehouse = new Warehouse();
        int totalProducts = StdIn.readInt();
        for (int i = 0; i < totalProducts; i++){

            //read values
            int day = StdIn.readInt();
            int id = StdIn.readInt();
            String name = StdIn.readString();
            int stock = StdIn.readInt();
            int demand = StdIn.readInt();

            warehouse.betterAddProduct(id, name, stock, day, demand);//add product to warehouse
        }
        StdOut.println(warehouse);
    }
}
