package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
	// Uset his file to test restock
        Warehouse warehouse = new Warehouse();
        int totalProducts = StdIn.readInt();
        for (int i = 0; i < totalProducts; i++){
            if (StdIn.readString().equalsIgnoreCase("add")){
                int day = StdIn.readInt();
                warehouse.addProduct(StdIn.readInt(), StdIn.readString(), 
                    StdIn.readInt(), day, StdIn.readInt());

            }else{
                warehouse.restockProduct(StdIn.readInt(), StdIn.readInt());
            }
        }
        StdOut.println(warehouse);
    }
}
