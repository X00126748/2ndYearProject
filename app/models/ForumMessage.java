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
    private Customer customer;

  
    // Default constructor
    public ForumMessage() {
    }

    // Constructor to initialise object
    public ForumMessage(Long id, String messageContent, Customer customer) {
        this.id = id;
        this.messageContent = messageContent;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
   

}
