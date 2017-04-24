package models.users;

import models.shopping.Basket;
import models.shopping.ShopOrder;
import models.shopping.OrderItem;
import models.shopping.PaymentCard;
import models.products.Review;
import javax.persistence.*;
import java.util.List;
import java.util.*;
import play.data.validation.*;

@Entity

// This is a User of type customer
@DiscriminatorValue("customer")

// Customer inherits from the User class
public class Customer extends User{
 
        @Constraints.Required
	private String number;
	@Constraints.Required
	private String street1;
	private String street2;
@Constraints.Required
    private String town;

    private String county;

@Constraints.Required
    private String postCode;
@Constraints.Required
    private String country;
@Constraints.Required
    private int LoyaltyPointsEarned;
    
     private int numOfOrders;
    

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Basket basket;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<PaymentCard> cards;
	
	public Customer(String email, String role, String title, String name, String surname, String password, String number, String street1, String street2, String town, String county, String postCode, String country, int LoyaltyPointsEarned, int numOfOrders)
	{
		super(email,role,title, name, surname, password);
        this.number = number;
        this.street1 = street1;
        this.street2 = street2;
        this.town = town;
	this.county = county;
        this.postCode = postCode;
        this.country = country;
	this.LoyaltyPointsEarned = LoyaltyPointsEarned;
	this.numOfOrders = numOfOrders;
   	
	}

    //Generic query helper for entity Computer with id Long
    public static Finder<String,Customer> find = new Finder<String,Customer>(Customer.class);

    public static List<Customer> findMostOrders() {
        return Customer.find.where()
                        .orderBy("numOfOrders desc")
			.setMaxRows(3)
                        .findList();
    }

    public static List<Customer> findMostPoints() {
        return Customer.find.where()
                        .orderBy("LoyaltyPointsEarned desc")
			.setMaxRows(3)
                        .findList();
    }


    public static List<String> countryOptions(){
        List<String> tmp = new ArrayList();

        tmp.add("Austria");
        tmp.add("Belgium");
        tmp.add("Croatia");
	tmp.add("Czech Republic");
        tmp.add("Denmark");
        tmp.add("Finland");
	tmp.add("France");
	tmp.add("Germany");
        tmp.add("Greece");
        tmp.add("Hungary");
	tmp.add("Iceland");
        tmp.add("Ireland");
        tmp.add("Italy");
	tmp.add("Netherlands");
        tmp.add("Poland");
	tmp.add("Portugal");
	tmp.add("Russia");
        tmp.add("Serbia");
        tmp.add("Spain");
	tmp.add("Sweden");
        tmp.add("Switzerland");
        tmp.add("Turkey");
	tmp.add("United Kingdom (UK)");


        return tmp;
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<PaymentCard> getCards() {
        return cards;
    }

    public void setCards(List<PaymentCard> cards) {
        this.cards = cards;
    }


    public int getLoyaltyPointsEarned() {
        
        return LoyaltyPointsEarned;
    }


    public void addLoyaltyPointsEarned(int LoyaltyPointsEarned) {
       this.LoyaltyPointsEarned += LoyaltyPointsEarned;
     }


    public void setLoyaltyPointsEarned(int LoyaltyPointsEarned) {
       this.LoyaltyPointsEarned = LoyaltyPointsEarned;
     }


   

}
