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
import java.util.Random;
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


    @Transactional
    public Result help() {

        return ok(help.render(User.getLoggedIn(session().get("email"))));
    }

    
  
    
    // Load the add Administrator view
    // Display an empty form in the view
    @Transactional
    public Result addAdministrator() {   

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
         
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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        

        Administrator a = (Administrator)getCurrentUser();
        
        return ok(accountDetails.render(env, a));
    }


      // Get a list of orders
    @Transactional
    public Result orders() {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        

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


        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        

         ShopOrder order = ShopOrder.find.byId(id);

         order.setOrderStatus("Order Complete");

         order.save();

	 order.update();

Product p = null;

  AddLoyaltyPoints(id);

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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        

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




    @Transactional
    public Result addForumMessage() {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
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

        // Check for errors (based on message class annotations)	
        if(addForumMessageForm.hasErrors()) {
            // Display the form again
            return badRequest(addForumMessage.render(addForumMessageForm, getCurrentUser()));
        }
     
        ForumMessage f = addForumMessageForm.get();

        Date dte=new Date();
        long milliSeconds = dte.getTime();
        
        f.setId(milliSeconds);
        
        // Retrieve the user
        User u = getCurrentUser();

        f.setUser(u);

        // Save message now to set id
        f.save();

         // Add a success message to the flash session
        flash("success", "Message has been Created" );
            
        
        // Redirect to the admin forum
        return redirect(routes.AdminHomeCtrl.forum());

    }

   public Result updateForumMessage(Long id) {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
        // Retrieve the ForumMessage by id
        ForumMessage f = ForumMessage.find.byId(id);
        // Create a form based on the ForumMessage class and fill using r
        Form<ForumMessage> ForumMessageForm = Form.form(ForumMessage.class).fill(f);
        // Render the updateForumMessage view
        // pass the id and form as parameters
        return ok(updateForumMessage.render(id, ForumMessageForm, User.getLoggedIn(session().get("email"))));		
    }


    // Handle the form data when an updated message is submitted
    @Transactional
    public Result updateForumMessageSubmit(Long id) {


        // Create a ForumMessage form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<ForumMessage> updateForumMessageForm = formFactory.form(ForumMessage.class).bindFromRequest();

        // Check for errors (based on ForumMessage class annotations)	
        if(updateForumMessageForm.hasErrors()) {
            // Display the form again
            return badRequest(updateForumMessage.render(id, updateForumMessageForm, getCurrentUser()));
        }
        
        // Update the ForumMessage (using its ID to select the existing object))
        ForumMessage f = updateForumMessageForm.get();
        f.setId(id);
        

        // update (save) this ForumMessage            
        f.update();


        // Add a success message to the flash session
        flash("success", "Forum Message has been updated");
            
        // Return to admin home
        return redirect(routes.AdminHomeCtrl.forum());
    }


           // Delete message
    @Transactional
    public Result deleteMessage(Long id) {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
        // Call delete method
        ForumMessage.find.ref(id).delete();
        // Add message to flash session 
        flash("success", "Message has been deleted");
        // Redirect to admin forum
        return redirect(routes.AdminHomeCtrl.forum());
    }


	
    // Delete Review
    @Transactional
    public Result deleteReview(Long id, Long product) {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
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

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        

	 List<ForumMessage> messages = new ArrayList<ForumMessage>();

         messages = ForumMessage.findAll();

    
        return ok(forum.render(env, messages, getCurrentUser()));
    }

            // Thumbs up message
    @Transactional
    public Result like(Long messageId) {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
        
        // Get the message
        ForumMessage m = ForumMessage.find.byId(messageId);
        
        User u = getCurrentUser();

        if(m.getUser() != u){
         
           //add like
        m.addLike();
}  else {
         // Add message to flash session 
        flash("warning", "Cannot like your own posts");
	return redirect(routes.AdminHomeCtrl.forum());
         }
       
        m.update();
        // Show updated forum
        return redirect(routes.AdminHomeCtrl.forum());
    }


    @Transactional
    public Result dislike(Long messageId) {

        if(getCurrentUser() == null){
           flash("warning", "Session has timed out, You've been logged out");
        return redirect(controllers.security.routes.LoginCtrl.login());

        }
        
        
        // Get the message
        ForumMessage m = ForumMessage.find.byId(messageId);
        
	 User u = getCurrentUser();

        if(m.getUser() != u){
         
        //add dislike
        m.addDislike();
}  else {
         // Add message to flash session 
        flash("warning", "Cannot dislike your own posts");
	return redirect(routes.AdminHomeCtrl.forum());
         }

        m.update();
        // Show updated forum
        return redirect(routes.AdminHomeCtrl.forum());
    }

       @Transactional
    public void AddLoyaltyPoints(Long id) {


        ShopOrder order = ShopOrder.find.byId(id);
        Customer c = order.getCustomer();
        int LoyaltyPointsEarned = 0;
        



        for (ShopOrder o: c.getOrders()) {

            LoyaltyPointsEarned =(c.getLoyaltyPointsEarned()+ o.getLoyaltyPointsEarned()); 

        }
        
        c.setLoyaltyPointsEarned(LoyaltyPointsEarned);
        c.update();

   }
    

}
