package models.stock;

import com.avaje.ebean.Model;
import models.products.Product;
import models.users.Administrator;
import models.products.*;
import models.shopping.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Iterator;
import java.util.List;


// Product entity managed by Ebean
@Entity
public class StockBasket extends Model {

    @Id
    private Long id;
    
    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST)
    private List<StockOrderItem> basketItems;
    
    @OneToOne
    private Administrator admin;

    // Default constructor
    public StockBasket() {
    }
    

    

    public void removeAllItems() {
        for(StockOrderItem i: this.basketItems) {
            i.delete();
        }
        this.basketItems = null;
    }

    public double getBasketTotal() {
        
        double total = 0;
        
        for (StockOrderItem i: basketItems) {
            total += i.getItemTotal();
        }
        return total;
    }
	
	//Generic query helper
    public static Finder<Long,StockBasket> find = new Finder<Long,StockBasket>(StockBasket.class);

    //Find all Products in the database
    public static List<StockBasket> findAll() {
        return StockBasket.find.all();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StockOrderItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<StockOrderItem> basketItems) {
        this.basketItems = basketItems;
    }

    public Administrator getAdministrator() {
        return admin;
    }

    public void setAdministrator(Administrator admin) {
        this.admin = admin;
    }




    // Add product to basket
// Either update existing order item or ad a new one.
public void addProduct(Product p) {
    
    boolean itemFound = false;
    // Check if product already in this basket
    // Check if item in basket
    // Find orderitem with this product
    // if found increment quantity
    for (StockOrderItem i : basketItems) {
        if (i.getProduct().getId() == p.getId()) {
            i.increaseQty();
            itemFound = true;
            break;
        }
    }
    if (itemFound == false) {
        // Add orderItem to list
        StockOrderItem newItem = new StockOrderItem(p);
        // Add to items
        basketItems.add(newItem);
    }
}



public void removeItem(StockOrderItem item) {

        // Using an iterator ensures 'safe' removal of list objects
        // Removal of list items is unreliable as index can change if an item is added or removed elsewhere
        // iterator works with an object reference which does not change
        for (Iterator<StockOrderItem> iter = basketItems.iterator(); iter.hasNext();) {
            StockOrderItem i = iter.next();
            if (i.getId().equals(item.getId()))
            {
                // If more than one of these items in the basket then decrement
                if (i.getQuantity() > 1 ) {
                    i.decreaseQty();
                }
                // If only one left, remove this item from the basket (via the iterator)
                else {
                    // delete object from db
                    i.delete();
                    // remove object from list
                    iter.remove();
                    break;
                }             
            }
		}
    }






}

