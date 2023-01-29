package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        sectors[id%10].add(new Product(id, name, stock, day, demand));
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        int idx = id%10;//find index

        sectors[idx].swim(sectors[idx].getSize());//use swim
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD
       int idx = id%10;
       if (sectors[idx].getSize() == 5){
        sectors[idx].swap(1,sectors[idx].getSize());
        sectors[idx].deleteLast(); 
        sectors[idx].sink(1); 
       }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHODwar
        int idx = id%10;
        for (int i = 1; i <= sectors[idx].getSize(); i++){
            if (sectors[idx].get(i).getId() == id){
                sectors[idx].get(i).updateStock(amount);
            }
        }
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        int idx = id%10;
        int size  = sectors[idx].getSize();
        for (int i = 1; i <= size; i++){
            if (sectors[idx].get(i).getId() == id){
                sectors[idx].swap(i,size);
                sectors[idx].deleteLast();
                sectors[idx].sink(i);
                break;
            }
        }
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        int idx = id%10;
        for (int i = 1; i <= sectors[idx].getSize(); i++){
            if (sectors[idx].get(i).getId() == id && sectors[idx].get(i).getStock()>amount){
                Product product = sectors[idx].get(i);

                product.setLastPurchaseDay(day);
                product.updateStock(-amount);
                product.updateDemand(amount);

                sectors[idx].set(i, product);
                sectors[idx].sink(i);

                break;
            }
        }


    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        int idx = id%10;
        if (sectors[idx].getSize() == 5){
            idx++;
            if (idx == 10) {
                idx = 0;
            }

            while(sectors[idx].getSize() == 5 && idx != id%10){
                idx++;
                if (idx == 10) idx = 0;
            }

            if(idx != id%10){
                sectors[idx].add(new Product(id, name, stock, day, demand));//add to end

                sectors[idx].swim(sectors[idx].getSize());//fix heap
            }else{
                addProduct(id, name, stock, day, demand);
            }
        }else{
            addToEnd(id, name, stock, day, demand);
            fixHeap(id);
        }

        // IMPLEMENT THIS METHOD

    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
