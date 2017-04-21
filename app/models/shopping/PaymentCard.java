package models.shopping;

import java.util.*;
import javax.persistence.*;
import java.util.Date;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;
import models.products.*;
import models.shopping.*;

// PaymentCard Entity managed by the ORM
@Entity
public class PaymentCard extends Model {

    // Properties
    // Annotate id as the primary key
    @Constraints.Required
    @Id
    private String cardNumber;

    // Other fields marked as being required (for validation purposes)
    
    
    @Constraints.Required
    private Integer expirationMonth;

    @Constraints.Required
    private Integer expirationYear;

    @Constraints.Required
    @Constraints.Min(100)
    @Constraints.Max(999)
    private Integer securityCode;

    @Constraints.Required
    private String type;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;

  
    // Default constructor
    public PaymentCard() {
    }

    // Constructor to initialise object
    public PaymentCard(String cardNumber, Integer expirationMonth, Integer expirationYear, Integer securityCode, String type) {
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.securityCode = securityCode;
        this.type = type;
    }

     public String validate() {
        if (luhnCheck(cardNumber)==false) {
            return "Invalid card Deatils";
        }
        return null;
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<String,PaymentCard> find = new Finder<String,PaymentCard>(PaymentCard.class);

    // Find all PaymentCard in the database
    // Filter PaymentCard name 
    public static List<PaymentCard> findAll() {
        return PaymentCard.find.all();
    }


    public static List<String> monthOptions(){
        List<String> tmp = new ArrayList();

        tmp.add("01");
        tmp.add("02");
        tmp.add("03");
	tmp.add("04");
        tmp.add("05");
        tmp.add("06");
	tmp.add("07");
        tmp.add("08");
        tmp.add("09");
	tmp.add("10");
        tmp.add("11");
        tmp.add("12");

        return tmp;
    }

     public static List<String> yearOptions(){
        List<String> tmp = new ArrayList();

        tmp.add("2017");
        tmp.add("2018");
        tmp.add("2019");
	tmp.add("2020");
        tmp.add("2021");
        tmp.add("2022");
	tmp.add("2023");
        tmp.add("2024");
        tmp.add("2025");
	tmp.add("2026");
        tmp.add("2027");
        tmp.add("2028");

        return tmp;
    }

    public static List<String> cardOptions(){
        List<String> tmp = new ArrayList();

        tmp.add("Debit Card");
        tmp.add("Credit Card");
  

        return tmp;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getShortCardNumber() {
        String card = cardNumber.substring(12);
        return card;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }
    
    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
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

    public List<ShopOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ShopOrder> orders) {
        this.orders = orders;
    }


    public static boolean luhnCheck(String card) {
        if (card == null)
            return false;
        char checkDigit = card.charAt(card.length() - 1);
        String digit = calculateCheckDigit(card.substring(0, card.length() - 1));
        return checkDigit == digit.charAt(0);
    }
    
    /**
     * Calculates the last digits for the card number received as parameter     * 
     * @param card
     *           {@link String} number
     * @return {@link String} the check digit
     */
    public static String calculateCheckDigit(String card) {
        if (card == null)
            return null;
        String digit;
        /* convert to array of int for simplicity */
        int[] digits = new int[card.length()];
        for (int i = 0; i < card.length(); i++) {
            digits[i] = Character.getNumericValue(card.charAt(i));
        }
        
        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2)    {
            digits[i] += digits[i];
            
            /* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }
        /* multiply by 9 step */
        sum = sum * 9;
        
        /* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }
   


   

}
