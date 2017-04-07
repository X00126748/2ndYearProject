package models.products;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


import models.shopping.*;
import models.products.*;

// Product entity managed by Ebean
@Entity
public class Product extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Review> reviews;

    // many to many mapping
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
    private List<Category> categories = new ArrayList<Category>();

    @ManyToOne
    private Supplier supplier;

    //@OneToOne(mappedBy="product")
    //public OrderItem item = new OrderItem();
    
    // List of category ids - this will be bound to checkboxes in the view form
    private List<Long> catSelect = new ArrayList<Long>();

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String description;

    @Constraints.Required
    private int	stock;

    @Constraints.Required
    private double price;

    private int	amountSold;

     private double rating;

    // Default constructor
    public  Product() {
    }

    // Constructor to initialise object
    public  Product(Long id, String name, String description, int stock, double price, List<Review> reviews, int amountSold){
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.reviews = reviews;
        this.amountSold = amountSold;
        this.rating = getRating();
    }
	
	//Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Product.class);


    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static PagedList<Product> page(int page, int pageSize, String sortBy, String order, String filter, Long catID) {
        
        if (catID != 0) {
           return 
            find.where()
                 .eq("categories.id", catID)
                .ilike("name", "%" + filter + "%")
            .orderBy(sortBy + " " + order)
            .fetch("categories")
            .findPagedList(page, pageSize);
          }

	 return 
            find.where()
                .ilike("name", "%" + filter + "%")
            .orderBy(sortBy + " " + order)
            .fetch("categories")
            .findPagedList(page, pageSize);
    }
    


    // Find all Products in the database
    // Filter product name 
    public static List<Product> findAll(String filter) {
        return Product.find.where()
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }
    
    // Find all Products for a category
    // Filter product name 
    public static List<Product> findFilter(Long catID, String filter) {
        return Product.find.where()
                        // Only include products from the matching cat ID
                        // In this case search the ManyToMany relation
                        .eq("categories.id", catID)
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }


     // Find all Products in the database
    public static List<Product> findLowSellers() {
        return Product.find.where()
                        .orderBy("amountSold asc")
                        .setMaxRows(3)
                        .findList();
    }

    // Find all Products in the database
    public static List<Product> findBestSellers() {
        return Product.find.where()
                        .orderBy("amountSold desc")
			.setMaxRows(3)
                        .findList();
    }


    // Find all Products in the database
    public static List<Product> findLowRatings() {
        return Product.find.where()
                        .orderBy("rating asc")
			.setMaxRows(3)
                        .findList();
    }

    // Find all Products in the database
    public static List<Product> findBestRatings() {
        return Product.find.where()
                        .orderBy("rating desc")
			.setMaxRows(3)
                        .findList();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }


    public boolean checkIfShoe() {
      boolean check = false;
        for (Category c : categories){
         if(c.getName().equalsIgnoreCase("Shoes")){
	   check = true;
          }
     }
	return check;
    }

    public List<Long> getCatSelect() {
        return catSelect;
    }

    public void setCatSelect(List<Long> catSelect) {
        this.catSelect = catSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Increment Stock
    public void increaseStock() {
        stock += 1;
       
    }
    
    // Decrement Stock
    public void decreaseStock() {
        stock -= 1;
    }


    // Add Stock
    public void addStock(Long amount) {
        stock += amount;
    }

    // Add Stock
    public void reStock(int amount) {
        stock += amount;
    }


    // Decrease Stock
    public void deStock(int amount) {
        stock -= amount;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }


    // Add to amount sold
    public void addAmountSold(int amount) {
        amountSold += amount;
    }


     public List<Review> getReviews() {
        return reviews;
    }
    


    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
      public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public double getAvgStars() {
        double sum = 0;

       for (int i=0; i < reviews.size(); i++) {
        sum = sum + reviews.get(i).getStars();
        }
  
   double avgStars = sum / reviews.size();


        return avgStars;
    }
    

    
    public double getNumStars() {
        double stars = 0;
      
         if(getAvgStars() == 5) {
                      stars = 5;        
                    } 

       else if (getAvgStars() >= 4.75 && getAvgStars() < 5 ){
                     stars = 5;        
                    }

       else if (getAvgStars() >= 4.25 && getAvgStars() < 4.75 ){
                     stars = 4.5;        
                    }
                
       else if (getAvgStars() >= 3.75 && getAvgStars() < 4.25 ){
                     stars = 4;        
                    }
       else if (getAvgStars() >= 3.25 && getAvgStars() < 3.75 ){
                     stars = 3.5;        
                    }
	else if (getAvgStars() >= 2.75 && getAvgStars() < 3.25 ){
                     stars = 3;        
                    }
       else if (getAvgStars() >= 2.25 && getAvgStars() < 2.75 ){
                     stars = 2.5;        
                    }
	else if (getAvgStars() >= 1.75 && getAvgStars() < 2.25 ){
                     stars = 2;        
                    }
       else if (getAvgStars() >= 1.25 && getAvgStars() < 1.75 ){
                     stars = 1.5;        
                    }
	else if (getAvgStars() >= 0.75 && getAvgStars() < 1.25 ){
                     stars = 1;        
                    }
       else if (getAvgStars() >= 0.25 && getAvgStars() < 0.75 ){
                     stars = 0.5;        
                    }
       else  {
                    stars = 0;
        }


        return stars;
    }


        public double getRating() {


        rating = getAvgStars();
     
        return rating;
    }

    public void setRating() {
        this.rating = getAvgStars();
    }


}

