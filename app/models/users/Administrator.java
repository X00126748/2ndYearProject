package models.users;

import models.stock.*;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

@Entity
// This is a User of type admin
@DiscriminatorValue("admin")

// Administrator inherits from the User class
public class Administrator extends User{


    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private StockBasket stockbasket;

   @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
   private List<StockOrder> stockOrders;
	

	public Administrator() {

	}

	public Administrator(String email, String role, String title, String name, String surname, String password)
	{
		super(email,role,title, name, surname, password);
	}


         public StockBasket getStockBasket() {
        return stockbasket;
    }

    public void setStockBasket(StockBasket stockbasket) {
        this.stockbasket = stockbasket;
    }

    public List<StockOrder> getStockOrders() {
        return stockOrders;
    }

    public void setStockOrders(List<StockOrder> stockOrders) {
        this.stockOrders = stockOrders;
    }
	
} 
