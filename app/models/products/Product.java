package models.products;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.shopping.*;

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

    // Default constructor
    public  Product() {
    }

    // Constructor to initialise object
    public  Product(Long id, String name, String description, int stock, double price, List<Review> reviews){
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.reviews = reviews;
    }
	
	//Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Product.class);

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

     public List<Review> getReviews() {
        return reviews;
    }
    


    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    

    public double getAvgStars() {
        double sum = 0;

       for (int i=0; i < reviews.size(); i++) {
        sum = sum + reviews.get(i).getStars();
        }
  
   double avgStars = sum / reviews.size();


        return avgStars;
    }
    

    
    public int getNumStars() {
        int stars = 0;
      
         if(getAvgStars() == 5) {
                      stars = 5;        
                    } 
                
       else if (getAvgStars() >= 4 && getAvgStars() < 5 ){
                     stars = 4;        
                    }
       else if (getAvgStars() >= 3 && getAvgStars() < 4 ){
                     stars = 3;        
                    }
       else if (getAvgStars() >= 2 && getAvgStars() < 3 ){
                     stars = 2;        
                    }
       else if (getAvgStars() >= 1 && getAvgStars() < 2 ){
                     stars = 1;        
                    }
       else  {
                    stars = 0;
        }


        return stars;
    }

}

