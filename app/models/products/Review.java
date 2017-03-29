package models.products;

import java.util.*;
import javax.persistence.*;
import java.util.Date;


import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

// Product Entity managed by the ORM
@Entity
public class Review extends Model {

    // Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    // Other fields marked as being required (for validation purposes)
    
    private String name;

    @ManyToOne
    private Product product;

    @Constraints.Required
    private String description;

    private Date reviewDate;

    
    private double stars;

   
    private Integer reviewCount = 0;

    // Default constructor
    public  Review() {
     reviewDate = new Date();
    }

    // Constructor to initialise object
    public  Review(Long id, String name, String description, Integer stars) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stars = stars;
        reviewCount++;
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long,Review> find = new Finder<Long,Review>(Review.class);

    // Find all Products in the database
    // Filter product name 
    public static List<Review> findAll() {
        return Review.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
