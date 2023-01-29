package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        // StdIn.setFile(args[0]);
        // StdOut.setFile(args[1]);

        StdIn.setFile("everything.in");
        StdOut.setFile("everything.out");

        // Use this file to test all methods
        Warehouse warehouse = new Warehouse();
        int totalProducts = StdIn.readInt();
        for (int i = 0; i < totalProducts; i++){
            String query = StdIn.readString();

            if (query.equalsIgnoreCase("add")){//add request
                int day = StdIn.readInt();
                warehouse.addProduct(StdIn.readInt(), StdIn.readString(), 
                    StdIn.readInt(), day, StdIn.readInt());

            }else if (query.equalsIgnoreCase("delete")){//delete request
                warehouse.deleteProduct(StdIn.readInt());

            }else if (query.equalsIgnoreCase("purchase")){//purchase request
                int day = StdIn.readInt();
                warehouse.purchaseProduct(StdIn.readInt(), day, StdIn.readInt());

            }else if (query.equalsIgnoreCase("restock")){//restock request
                warehouse.restockProduct(StdIn.readInt(), StdIn.readInt());
            }
        }
        StdOut.println(warehouse);//output to file
    }
}
