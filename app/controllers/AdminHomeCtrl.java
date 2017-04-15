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

public class AdminHomeCtrl extends Controller {

    /** Dependency Injection **/

    /** http://stackoverflow.com/questions/15600186/play-framework-dependency-injection **/
    private FormFactory formFactory;

    /** http://stackoverflow.com/a/37024198 **/
    private Environment env;

    /** http://stackoverflow.com/a/10159220/6322856 **/
    @Inject
    public AdminHomeCtrl(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }



	
    // Get a user - if logged in email will be set in the session
	private User getCurrentUser() {
		User u = User.getLoggedIn(session().get("email"));
		return u;
	}
    
  
    
    // Load the add Administrator view
    // Display an empty form in the view
    @Transactional
    public Result addAdministrator() {   
        // Instantiate a form object based on the Product class
        Form<Administrator> addAdministratorForm = formFactory.form(Administrator.class);
        // Render the Add Administrator View, passing the form object
        return ok(addAdmin.render(addAdministratorForm, getCurrentUser()));
    }

    // Handle the form data when a new Administrator is submitted
    @Transactional
    public Result addAdministratorSubmit() {
     String saveImageMsg;

        // Create a Administrator form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Administrator> newAdministratorForm = formFactory.form(Administrator.class).bindFromRequest();

        // Check for errors (based on Administrator class annotations)	
        if(newAdministratorForm.hasErrors()) {
            // Display the form again
            return badRequest(addAdmin.render(newAdministratorForm, getCurrentUser()));
        }
     
        Administrator newAdministrator = newAdministratorForm.get();

          List<User> users = User.findAll();

        for (User u : users) {
            if (newAdministrator.getEmail().equals(u.getEmail())){
              // Add a warning message to the flash session
        flash("warning", "Administrator email already exits");
            
        // Return to admin home
        return redirect(controllers.routes.AdminHomeCtrl.addAdministrator());
            }
        }

        newAdministrator.setRole("admin");
        
        // Save Administrator now to set id ()
        newAdministrator.save();
        


	// Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(newAdministrator.getEmail(), image);

        // Set a success message in temporary flash
        flash("success", "Administrator " + newAdministrator.getName() + " has been created" + " " + saveImageMsg);
            
        // Redirect to the admin home
        return redirect(controllers.routes.AdminProductCtrl.index());
    }


         // Save an image file
    public String saveFile(String name, FilePart<File> image) {
        if (image != null) {
            // Get mimetype from image
            String mimeType = image.getContentType();
            // Check if uploaded file is an image
            if (mimeType.startsWith("image/")) {
                // Create file from uploaded image
                File file = image.getFile();
                // create ImageMagick command instance
                ConvertCmd cmd = new ConvertCmd();
                // create the operation, add images and operators/options
                IMOperation op = new IMOperation();
                // Get the uploaded image file
                op.addImage(file.getAbsolutePath());
                // Resize using height and width constraints
                op.resize(300,300);
                // Save the  image
                op.addImage("public/images/userIcons/" + name + ".png");
                // thumbnail
                //IMOperation thumb = new IMOperation();
                // Get the uploaded image file
                //thumb.addImage(file.getAbsolutePath());
                //thumb.thumbnail(60);
                // Save the  image
                //thumb.addImage("public/images/userIcons/thumbnails/" + name + ".jpg");
                // execute the operation
                try{
                    cmd.run(op);
                    //cmd.run(thumb);
                }
                catch(Exception e){
                    e.printStackTrace();
                }				
                return " and image saved";
            }
        }
        return "image file missing";	
    }


                // Update a Administrator by getCurrentUser
    // called when edit button is pressed
    @Transactional
    public Result updateAdministrator() {
         // Retrieve the Administrator by getCurrentUser
        Administrator a = (Administrator)getCurrentUser();
        // Create a form based on the Administrator class and fill using a
        Form<Administrator> administratorForm = Form.form(Administrator.class).fill(a);
        // Render the updateAdministrator view
        // pass the form as a parameter
        return ok(updateAdministrator.render(administratorForm, User.getLoggedIn(session().get("email"))));		
    }


    // Handle the form data when an updated Administrator is submitted
    @Transactional
    public Result updateAdministratorSubmit() {
 
	String saveImageMsg;

        // Create a Administrator form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Administrator> updateAdministratorForm = formFactory.form(Administrator.class).bindFromRequest();

        // Check for errors (based on Administrator class annotations)	
        if(updateAdministratorForm.hasErrors()) {
            // Display the form again
            return badRequest(updateAdministrator.render(updateAdministratorForm, getCurrentUser()));
        }
        
        // Update the Administrator (using its ID to select the existing object))
        Administrator a = updateAdministratorForm.get();

        //c.setId(id);
       
        // update (save) this Administrator          
        a.update();

        // Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(a.getEmail(), image);
      
        // Add a success message to the flash session
        flash("success", "Administrator " + updateAdministratorForm.get().getName() + " has been updated" + " " + saveImageMsg);
            
        // Return to admin home
        return redirect(controllers.routes.AdminHomeCtrl.accountDetails());
    }

        

          // Delete Administrator
    @Transactional
    public Result deleteAdministrator() {
         
          // Retrieve the Administrator by getCurrentUser
       Administrator a = (Administrator)getCurrentUser();
       

        String email = a.getEmail();
        
        // Call delete method
        Administrator.find.ref(email).delete();
        // Add message to flash session 
        flash("success", "Administrator has been deleted");
        // Redirect home
        return redirect(controllers.security.routes.LoginCtrl.login());
    }


         @Transactional
    public Result accountDetails(){

        Administrator a = (Administrator)getCurrentUser();
        
        return ok(accountDetails.render(env, a));
    }


      // Get a list of orders
    @Transactional
    public Result orders() {

        List<ShopOrder> ordersList = new ArrayList<ShopOrder>();

       // Get the list of ALL orders
            ordersList = ShopOrder.findAll();

	List<ShopOrder> currentOrders = new ArrayList<ShopOrder>();
        List<ShopOrder> previousOrders = new ArrayList<ShopOrder>();
        
	
        for (ShopOrder o: ordersList){

            if (o.getOrderStatus().equals("Processing Order")){

              currentOrders.add(o);

		} else { 

              previousOrders.add(o);

		}
 
			}	
        // Render the list orders view, passing parameters
        return ok(orders.render(env, currentOrders,previousOrders, getCurrentUser()));
    } 


        // Get a list of orders
    @Transactional
    public Result stockOrders() {

        List<StockOrder> ordersList = new ArrayList<StockOrder>();

         
            // Get the list of ALL products with filter
            ordersList = StockOrder.findAll();

	List<StockOrder> currentOrders = new ArrayList<StockOrder>();
        List<StockOrder> previousOrders = new ArrayList<StockOrder>();
        
	   for (StockOrder o: ordersList){

            if (o.getOrderStatus().equals("Processing Order")){

              currentOrders.add(o);

		} else { 

              previousOrders.add(o);

		}
 
			}	
        
        // Render the list orders view, passing parameters
        return ok(stockOrders.render(env, currentOrders,previousOrders, getCurrentUser()));
    } 
      


    @Transactional
    public Result setOrderForDelivery(Long id) {

         ShopOrder order = ShopOrder.find.byId(id);

         order.setOrderStatus("Order Complete");

         order.save();

	 order.update();

Product p = null;

for (OrderItem i: order.getItems()){
     
     p = i.getProduct();
     int quantity = i.getQuantity();

        
     p.addAmountSold(quantity);
     p.update();


}

         // Set a success message in temporary flash
        flash("success", "Order has been set for delivery" );

        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));
	 return redirect(routes.AdminHomeCtrl.orders());
          
    } 



      @Transactional
    public Result setStockOrderForDelivery(Long id) {

         StockOrder order = StockOrder.find.byId(id);

         order.setOrderStatus("Order Complete");

         order.save();

	 order.update();


        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));

          // Set a success message in temporary flash
        flash("success", "Stock Order has arrived" );

	 return redirect(routes.AdminHomeCtrl.stockOrders());
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



    @Transactional
    public Result addForumMessage() {
        // Retrieve the product by id
        User u = getCurrentUser();
        // Instantiate a form object based on the Review class
        Form<ForumMessage> forumMessageForm = formFactory.form(ForumMessage.class);
        // Render the Add Review View, passing the form object
        return ok(addForumMessage.render(forumMessageForm, User.getLoggedIn(session().get("email"))));	

    }


    // Handle the form data when a new Review is submitted
    @Transactional
    public Result addForumMessageSubmit() {

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<ForumMessage> addForumMessageForm = formFactory.form(ForumMessage.class).bindFromRequest();

        // Check for errors (based on Product class annotations)	
        if(addForumMessageForm.hasErrors()) {
            // Display the form again
            return badRequest(addForumMessage.render(addForumMessageForm, getCurrentUser()));
        }
     
        ForumMessage f = addForumMessageForm.get();

        // Retrieve the product by id
        User u = getCurrentUser();

        f.setUser(u);

        // Save product now to set id (needed to update manytomany)
        f.save();

         // Add a success message to the flash session
        flash("success", "Message has been Created" );
            
        
        // Redirect to the admin forum
        return redirect(routes.AdminHomeCtrl.forum());

    }

           // Delete message
    @Transactional
    public Result deleteMessage(Long id) {
        // Call delete method
        ForumMessage.find.ref(id).delete();
        // Add message to flash session 
        flash("success", "Message has been deleted");
        // Redirect to admin forum
        return redirect(routes.AdminHomeCtrl.forum());
    }


	
    // Delete Product
    @Transactional
    public Result deleteReview(Long id, Long product) {
        // Call delete method
        Review.find.ref(id).delete();

        // Retrieve the product by id
        Product p = Product.find.byId(product);

        p.setRating();

        p.update();
        
        // Add message to flash session 
        flash("success", "Review has been deleted");
        // Redirect home
        return redirect(routes.AdminProductCtrl.product(product));
    }

    

         @Transactional
         public Result forum() {

	 List<ForumMessage> messages = new ArrayList<ForumMessage>();

         messages = ForumMessage.findAll();

    
        return ok(forum.render(env, messages, getCurrentUser()));
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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

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
        return redirect(routes.AdminHomeCtrl.reports());

    }
 

    

}
