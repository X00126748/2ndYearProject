package models;

import java.util.*;
import javax.persistence.*;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;

// ForumMessage Entity managed by the ORM
@Entity
public class Reaction extends Model {

    
    // Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    // Other fields marked as being required (for validation purposes)

    @ManyToOne
    private User user;

    @ManyToOne
    private ForumMessage message;
   
    @Constraints.Required
    private boolean liked;
    @Constraints.Required
    private boolean disliked;

  
    // Default constructor
    public Reaction() {
          liked = false;
          disliked = false;
    }

    // Constructor to initialise object
    public Reaction(Long id, boolean liked, boolean disliked) {
        this.id = id;
        this.liked = liked;
        this.disliked = disliked; 
    }

    	//Generic query helper
    public static Finder<Long,Reaction> find = new Finder<Long,Reaction>(Reaction.class);

    //Find all ForumMessage in the database
    public static List<Reaction> findAll() {
        return Reaction.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean getDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
     public ForumMessage getMessage() {
        return message;
    }

    public void setMessage(ForumMessage message) {
        this.message = message;
    }
    

}
