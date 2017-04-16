package controllers;

import controllers.security.*;

import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import play.Logger;
import java.util.Date;



// File upload and image editing dependencies
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;


// Import models and views
import models.users.*;
import models.products.*;
import models.shopping.*;
import models.stock.*;
import models.*;

import views.html.productAdmin.*;



/* - Docs -
http://superuser.com/questions/163818/how-to-install-rmagick-on-ubuntu-10-04
http://im4java.sourceforge.net/
*/

// Authenticate user
@Security.Authenticated(Secured.class)
// Authorise user (check if admin)
@With(CheckIfAdmin.class)

public class ReportCtrl extends Controller {

    /** Dependency Injection **/

    /** http://stackoverflow.com/questions/15600186/play-framework-dependency-injection **/
    private FormFactory formFactory;

    /** http://stackoverflow.com/a/37024198 **/
    private Environment env;

    /** http://stackoverflow.com/a/10159220/6322856 **/
    @Inject
    public ReportCtrl(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }



	
    // Get a user - if logged in email will be set in the session
	private User getCurrentUser() {
		User u = User.getLoggedIn(session().get("email"));
		return u;
	}
    
  
   
        
          // Get reports
    @Transactional
    public Result reports() {

       // Instantiate products, an Array list of products			
        List<Product> products = new ArrayList<Product>();
     
	products = Product.findAll("");
     

        // Instantiate products, an Array list of products			
        List<Product> lowSellers = new ArrayList<Product>();
	lowSellers = Product.findLowSellers();

        // Instantiate products, an Array list of products			
        List<Product> bestSellers = new ArrayList<Product>();
	bestSellers = Product.findBestSellers();


        // Instantiate products, an Array list of products			
        List<Product> lowRatings = new ArrayList<Product>();
	lowRatings = Product.findLowRatings();
       

        // Instantiate products, an Array list of products			
        List<Product> bestRatings = new ArrayList<Product>();
	bestRatings = Product.findBestRatings();

        List<Customer> customersPoints = new ArrayList<Customer>();
        customersPoints = Customer.findMostPoints();
     

	List<Customer> customersOrders = new ArrayList<Customer>();
        customersOrders = Customer.findMostOrders();


        List<ForumMessage> custMostLiked = new ArrayList<ForumMessage>();
        custMostLiked = ForumMessage.findMostLiked();

       
	List<ForumMessage> custMostDisiked = new ArrayList<ForumMessage>();
        custMostDisiked = ForumMessage.findMostDisliked();


        return ok(reports.render(env,bestSellers,lowSellers, bestRatings, lowRatings, customersOrders, customersPoints,  custMostLiked, custMostDisiked, getCurrentUser()));
    } 



        public Result printBestSellers() {

	 File outFile = new File("files/BestSellers", "Best Sellers " + new Date() +".txt");        


        // Instantiate products, an Array list of products			
        List<Product> BestSellers = new ArrayList<Product>();
	BestSellers = Product.findBestSellers();
       
         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nBest Sellers " + new Date());
            for (Product p : BestSellers){
            bWriter.write("\n********************");
	    bWriter.write("\nProduct Name: " + p.getName());
            bWriter.write("\nPrice: " + p.getPrice());
            bWriter.write("\nDescription: " + p.getDescription());
            bWriter.write("\nStock: " + p.getStock());
            bWriter.write("\nAmount Sold: " + p.getAmountSold());
	    bWriter.write("\nRating: " + p.getRating());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Best Sellers have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }


       public Result printWorstSellers() {

	 File outFile = new File("files/WorstSellers", "Worst Sellers " + new Date() +".txt");        


        // Instantiate products, an Array list of products			
        List<Product> WorstSellers = new ArrayList<Product>();
	WorstSellers = Product.findLowSellers();
       
         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nWorst Sellers " + new Date());
            for (Product p : WorstSellers){
            bWriter.write("\n********************");
	    bWriter.write("\nProduct Name: " + p.getName());
            bWriter.write("\nPrice: " + p.getPrice());
            bWriter.write("\nDescription: " + p.getDescription());
            bWriter.write("\nStock: " + p.getStock());
            bWriter.write("\nAmount Sold: " + p.getAmountSold());
	    bWriter.write("\nRating: " + p.getRating());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Worst Sellers have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }



       public Result printHighestRated() {

	 File outFile = new File("files/HighestRated", "Highest Rated " + new Date() +".txt");        


        // Instantiate products, an Array list of products			
        List<Product> HighestRated = new ArrayList<Product>();
	HighestRated = Product.findBestRatings();
       
         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nHighest Rated " + new Date());
            for (Product p : HighestRated){
            bWriter.write("\n********************");
	    bWriter.write("\nProduct Name: " + p.getName());
            bWriter.write("\nPrice: " + p.getPrice());
            bWriter.write("\nDescription: " + p.getDescription());
            bWriter.write("\nStock: " + p.getStock());
            bWriter.write("\nAmount Sold: " + p.getAmountSold());
	    bWriter.write("\nRating: " + p.getRating());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Highest Rated have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }


public Result printLowestRated() {

	 File outFile = new File("files/LowestRated", "Lowest Rated " + new Date() +".txt");        


        // Instantiate products, an Array list of products			
        List<Product> LowestRated = new ArrayList<Product>();
	LowestRated = Product.findLowRatings();
       
         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nLowest Rated " + new Date());
            for (Product p : LowestRated){
            bWriter.write("\n********************");
	    bWriter.write("\nProduct Name: " + p.getName());
            bWriter.write("\nPrice: " + p.getPrice());
            bWriter.write("\nDescription: " + p.getDescription());
            bWriter.write("\nStock: " + p.getStock());
            bWriter.write("\nAmount Sold: " + p.getAmountSold());
	    bWriter.write("\nRating: " + p.getRating());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Lowest Rated have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }




public Result printMostOrders() {

	 File outFile = new File("files/MostOrders", "Most Orders " + new Date() +".txt");        


        List<Customer> MostOrders = new ArrayList<Customer>();
        MostOrders = Customer.findMostOrders();

         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nMost Orders " + new Date());
            for (Customer c : MostOrders){
            bWriter.write("\n********************");
	    bWriter.write("\nCustomer: " + c.getName());
            bWriter.write("\nEmail: " + c.getEmail());
            bWriter.write("\nOrders Made: " + c.getNumOfOrders());
	    bWriter.write("\nLoyalty Points: " + c.getLoyaltyPointsEarned());


            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Most Orders have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }



 public Result printMostLoyaltyPoints() {

	 File outFile = new File("files/MostLoyaltyPoints", "Most Loyalty Points " + new Date() +".txt");        


        List<Customer> MostLoyaltyPoints = new ArrayList<Customer>();
        MostLoyaltyPoints = Customer.findMostPoints();

         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nMost Loyalty Points " + new Date());
            for (Customer c : MostLoyaltyPoints){
            bWriter.write("\n********************");
	    bWriter.write("\nCustomer: " + c.getName());
            bWriter.write("\nEmail: " + c.getEmail());
            bWriter.write("\nOrders Made: " + c.getNumOfOrders());
	    bWriter.write("\nLoyalty Points: " + c.getLoyaltyPointsEarned());


            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Most Loyalty Points have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }



    public Result printDislikedPosts() {

	 File outFile = new File("files/DislikedPosts", "Most Disliked Posts " + new Date() +".txt");        

	List<ForumMessage> custMostDisiked = new ArrayList<ForumMessage>();
        custMostDisiked = ForumMessage.findMostDisliked();

         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nMost Disliked Posts " + new Date());
            for (ForumMessage m : custMostDisiked){
            bWriter.write("\n********************");
	    bWriter.write("\nUser: " + m.getUser().getName());
            bWriter.write("\nSubject: " + m.getSubject());
            bWriter.write("\nMessage: " + m.getMessageContent());
	    bWriter.write("\nDate: " + m.getMessageDate());
	    bWriter.write("\nLikes: " + m.getLikes());
            bWriter.write("\nDislikes: " + m.getDislikes());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Disliked Posts have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }

              public Result printLikedPosts() {

	 File outFile = new File("files/LikedPosts", "Most Liked Posts " + new Date() +".txt");        

	List<ForumMessage> custLikedPosts = new ArrayList<ForumMessage>();
        custLikedPosts = ForumMessage.findMostLiked();

         try (BufferedWriter bWriter = new BufferedWriter
            (new FileWriter((outFile)))) {
	
	    bWriter.write("\nMost Liked Posts " + new Date());
            for (ForumMessage m : custLikedPosts){
            bWriter.write("\n********************");
	    bWriter.write("\nUser: " + m.getUser().getName());
            bWriter.write("\nSubject: " + m.getSubject());
            bWriter.write("\nMessage: " + m.getMessageContent());
	    bWriter.write("\nDate: " + m.getMessageDate());
	    bWriter.write("\nLikes: " + m.getLikes());
            bWriter.write("\nDislikes: " + m.getDislikes());

            }
        } catch (IOException ex) {
            System.out.println("Problem: " + ex.getMessage());
        }

        // Add message to flash session 
        flash("success", "Liked Posts have been written to file");
        // Redirect home
        return redirect(routes.ReportCtrl.reports());

    }
 

    

}
