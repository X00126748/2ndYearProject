package models.shopping;

import java.util.*;
import javax.persistence.*;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;

// PaymentCard Entity managed by the ORM
@Entity
public class PaymentCard extends Model {

    // Properties
    // Annotate id as the primary key
    @Id
    private Long cardNumber;

    // Other fields marked as being required (for validation purposes)
    @Constraints.Required
    private String expirationDate;

    @Constraints.Required
    private Integer securityCode;

    @Constraints.Required
    private String type;

    @ManyToOne
    private Customer customer;

  
    // Default constructor
    public PaymentCard() {
    }

    // Constructor to initialise object
    public PaymentCard(Long cardNumber, String expirationDate, Integer securityCode, String type) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.type = type;
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long,PaymentCard> find = new Finder<Long,PaymentCard>(PaymentCard.class);

    // Find all PaymentCard in the database
    // Filter PaymentCard name 
    public static List<PaymentCard> findAll() {
        return PaymentCard.find.all();
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
   

}
