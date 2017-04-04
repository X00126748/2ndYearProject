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
    private String subject;
   
    @Constraints.Required
    private String messageContent;

    @ManyToOne
    private User user;
   
    private Integer likes;
    
    private Integer dislikes;

    private Date messageDate;

  
    // Default constructor
    public ForumMessage() {
        messageDate = new Date();
         likes = 0;
        dislikes = 0;
    }

    // Constructor to initialise object
    public ForumMessage(Long id, String subject, String messageContent) {
        this.id = id;
        this.subject = subject;
        this.messageContent = messageContent; 
        messageDate = new Date();
        likes = 0;
        dislikes = 0;
    }

    	//Generic query helper
    public static Finder<Long,ForumMessage> find = new Finder<Long,ForumMessage>(ForumMessage.class);

    //Find all ForumMessage in the database
    public static List<ForumMessage> findAll() {
        return ForumMessage.find.all();
    }

     public static List<ForumMessage> findMostLiked() {
        return ForumMessage.find.where()
                        .orderBy("likes desc")
                        .findList();
    }

     public static List<ForumMessage> findMostDisliked() {
        return ForumMessage.find.where()
                        .orderBy("dislikes desc")
                        .findList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
    

     public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void addLike() {
        likes += 1;
    }

    public void addDislike() {
        dislikes += 1;
    }

   

}
