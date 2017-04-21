package models.users;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;
import models.*;

//https://www.playframework.com/documentation/2.2.x/JavaGuide4

// Product entity managed by Ebean
@Entity
// specify mapped table name
@Table(name = "user")
// Map inherited classes to a single table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
// Discriminator column used to define user type
@DiscriminatorColumn(name = "role")
// This user type is user
@DiscriminatorValue("user") 

public class User extends Model {

    //@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) 
	//public Long id;
	
	@Constraints.Required
    @Constraints.Email
    @Id
    private String email;

    @Column(insertable=false, updatable=false)
    private String role;

    @Constraints.Required
    private String title;

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String surname;

    @Constraints.Required
    @Constraints.MinLength(5)
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ForumMessage> messages;


    // Default constructor
    public  User() {
    }
    // Constructor to initialise object
    public  User(String email, String role, String title, String name, String surname, String password) {
        this.email = email;
        this.role = role;
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

	//Generic query helper for entity User with unique id String
    public static Finder<String,User> find = new Finder<String,User>(User.class);

    //Find all customers in the database
    public static List<User> findAll() {
        return User.find.all();
    }

    
    // Static method to authenticate based on username and password
    // Returns user object if found, otherwise NULL
    public	static User authenticate(String email, String password) {
        // If found return the user object with matching username and password
        return find.where().eq("email", email).eq("password", password).findUnique();
    }

    // Check if a user is logged in (by id - email address)
    public static User getLoggedIn(String id) {
        if (id == null)
                return null;
        else
            // Find user by id and return object
            return find.byId(id);
    }


    public static List<String> titleOptions(){
        List<String> tmp = new ArrayList();

        tmp.add("Mr");
        tmp.add("Mrs");
        tmp.add("Miss");
	tmp.add("Ms");
        tmp.add("Dr");
        tmp.add("Rev");
	tmp.add("Other");

        return tmp;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ForumMessage> getMessages() {
        return messages;
    }

    public void setForumMessages(List<ForumMessage> messages) {
        this.messages = messages;
    }

 
}

