package warehouse;

public class PurchaseProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // Use this file to test purchaseProduct
        Warehouse warehouse = new Warehouse();
        int totalProducts = StdIn.readInt();
        for (int i = 0; i < totalProducts; i++){
            if (StdIn.readString().equalsIgnoreCase("add")){
                int day = StdIn.readInt();
                warehouse.addProduct(StdIn.readInt(), StdIn.readString(), 
                    StdIn.readInt(), day, StdIn.readInt());

            }else{
                int day = StdIn.readInt();
                warehouse.purchaseProduct(StdIn.readInt(), day, StdIn.readInt());
            }
        }
        StdOut.println(warehouse);
    }
}
