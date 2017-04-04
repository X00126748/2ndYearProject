package models.users;

import models.shopping.Basket;
import models.shopping.ShopOrder;

import javax.persistence.*;
import java.util.List;
import play.data.validation.*;

@Entity

// This is a User of type customer
@DiscriminatorValue("customer")

// Customer inherits from the User class
public class Customer extends User{
	@Constraints.Required
	private String street1;
	private String street2;
@Constraints.Required
    private String town;
@Constraints.Required
    private String postCode;
@Constraints.Required
    private String country;
    
     private int numOfOrders;



    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Basket basket;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;
	
	public Customer(String email, String role, String name, String password, String street1, String street2, String town, String postCode, String country, int numOfOrders)
	{
		super(email, role, name, password);
        this.street1 = street1;
        this.street2 = street2;
        this.town = town;
        this.postCode = postCode;
        this.country = country;
	this.numOfOrders = numOfOrders;	
	}

    //Generic query helper for entity Computer with id Long
    public static Finder<String,Customer> find = new Finder<String,Customer>(Customer.class);

    public static List<Customer> findMostOrders() {
        return Customer.find.where()
                        .orderBy("numOfOrders desc")
                        .findList();
    }


    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    
     public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

     public int getNumOfOrders() {
        return numOfOrders;
    }

    public void setNumOfOrders(int numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    // Add to amount NumOfOrders
    public void addNumOfOrders() {
        numOfOrders += 1;
    }


    // Add to amount NumOfOrders
    public void minusNumOfOrders() {
        numOfOrders -= 1;
    }


    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public List<ShopOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ShopOrder> orders) {
        this.orders = orders;
    }
}
