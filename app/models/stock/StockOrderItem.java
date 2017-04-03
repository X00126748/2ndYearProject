package models.stock;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;

// OrderItem entity managed by Ebean
@Entity
public class StockOrderItem extends Model {

    @Id
    private Long id;

    @ManyToOne
    private StockOrder order;
    
    @ManyToOne
    private StockBasket basket;
    
    @ManyToOne
    private Product product;
    
    private int quantity;
    private double price;
  
    private String size;

    // Default constructor
    public  StockOrderItem() {
    }
    
    public StockOrderItem(Product p) {
            product = p;
            quantity = 1;
            product.increaseStock();
            product.update();
            price = p.getPrice();
            size = "No size selected";
    }
    
    // Increment quantity
    public void increaseQty() {
        quantity += 1;
        product.increaseStock();
         product.update();
    }
    
    // Decrement quantity
    public void decreaseQty() {
        quantity -= 1;
        product.decreaseStock();
         product.update();
    }
    
    // Calculate and return total price for this order item
    public double getItemTotal() {
        return this.price * this.quantity;
    }
	
	//Generic query helper
    public static Finder<Long,StockOrderItem> find = new Finder<Long,StockOrderItem>(StockOrderItem.class);

    //Find all Products in the database
    public static List<StockOrderItem> findAll() {
        return StockOrderItem.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockOrder getOrder() {
        return order;
    }

    public void setOrder(StockOrder order) {
        this.order = order;
    }

    public StockBasket getBasket() {
        return basket;
    }

    public void setBasket(StockBasket basket) {
        this.basket = basket;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

