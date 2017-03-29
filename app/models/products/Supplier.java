package models.products;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

// Supplier Entity managed by the ORM
@Entity
public class Supplier extends Model {

    // Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    // Other fields marked as being required (for validation purposes)
    @Constraints.Required
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    @Constraints.Required
    @Constraints.Email
    private String email;


    @Constraints.Required
    private String number;

    

  
    // Default constructor
    public Supplier() {
    }

    // Constructor to initialise object
    public Supplier(Long id, String name, String number, List<Product> products){
        this.id = id;
        this.name = name;
        this.number = number;
        this.products = products;
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long,Supplier> find = new Finder<Long,Supplier>(Supplier.class);

    // Find all Supplier in the database
    // Filter product name 
    public static List<Supplier> findAll() {
        return Supplier.find.all();
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

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

     public List<Product> getProducts() {
        return products;
     }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
   

}
