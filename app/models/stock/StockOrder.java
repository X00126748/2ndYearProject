package models.stock;

import java.util.*;
import javax.persistence.*;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;

// ShopOrder entity managed by Ebean
@Entity
public class StockOrder extends Model {

    @Id
    private Long id;
    
    private Date OrderDate;

    
    private String orderStatus;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<StockOrderItem> items;
    
    //private List<Product> items;

    @ManyToOne
    private Administrator admin;


    // Default constructor
    public  StockOrder() {
        OrderDate = new Date();
    }
    
    public double getOrderTotal() {
        
        double total = 0;
        
        for (StockOrderItem i: items) {
            total += i.getItemTotal();
        }
        return total;
    }
	
	//Generic query helper
    public static Finder<Long,StockOrder> find = new Finder<Long,StockOrder>(StockOrder.class);

    //Find all orders in the database
    public static List<StockOrder> findAll() {
        return StockOrder.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<StockOrderItem> getItems() {
        return items;
    }

    public void setItems(List<StockOrderItem> items) {
      this.items = items;
   }


    public Administrator getAdministrator() {
        return admin;
    }

    public void setAdministrator(Administrator admin) {
        this.admin = admin;
    }
}

