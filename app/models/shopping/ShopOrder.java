package models.shopping;

import java.util.*;
import javax.persistence.*;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import models.users.*;
import models.products.*;
import models.users.*;

// ShopOrder entity managed by Ebean
@Entity
public class ShopOrder extends Model {

    @Id
    private Long id;

    private int LoyaltyPointsEarned;
    
    private Date OrderDate;

    
    private String orderStatus;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    @ManyToOne
    private Customer customer;

    @ManyToOne
    private PaymentCard card;

    // Default constructor
    public  ShopOrder() {
        OrderDate = new Date();
    }
    
    public double getOrderTotal() {
        
        double total = 0;
	double delivery = 5.00;
        
        for (OrderItem i: items) {
            total += i.getItemTotal();
             
        }

        if (total <= 30.00){
         total += delivery;

       }
        return total;
    }

       
      public double getOrderTotalLoyalty() {
         int points = 0;
         double total = 0;
 	double delivery = 5.00;
 
         Customer customer = getCustomer();
         
         for (OrderItem i: items) {
             total += i.getItemTotal();
              
         }
 
         if (total <= 30.00){
          total += delivery;
 
        }
        
         points = customer.getLoyaltyPointsEarned();
         total = (total - points);
         return total;
     }
	
	//Generic query helper
    public static Finder<Long,ShopOrder> find = new Finder<Long,ShopOrder>(ShopOrder.class);

    //Find all orders in the database
    public static List<ShopOrder> findAll() {
        return ShopOrder.find.all();
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentCard getCard() {
        return card;
    }

    public void setCard(PaymentCard card) {
        this.card = card;
    }

    public int getLoyaltyPointsEarned() {
        int total = 0;
        int temp = 0;
        

        for (OrderItem i: items) {
            total += i.getItemTotal();
        }
         
        temp = (total/10);
         
        LoyaltyPointsEarned = temp;
        
        return LoyaltyPointsEarned;

        
   }



}

