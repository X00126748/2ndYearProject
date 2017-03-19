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
public class ForumMessage extends Model {

    
    // Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    // Other fields marked as being required (for validation purposes)

    @Constraints.Required
    private String messageContent;

    @ManyToOne
    private User user;

  
    // Default constructor
    public ForumMessage() {
    }

    // Constructor to initialise object
    public ForumMessage(Long id, String messageContent) {
        this.id = id;
        this.messageContent = messageContent;

    }

    	//Generic query helper
    public static Finder<Long,ForumMessage> find = new Finder<Long,ForumMessage>(ForumMessage.class);

    //Find all ForumMessage in the database
    public static List<ForumMessage> findAll() {
        return ForumMessage.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
   

}
