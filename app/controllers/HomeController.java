package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;
import controllers.security.CheckIfCustomer;
import controllers.security.CheckIfAdmin;
import controllers.security.Secured;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import play.mvc.Security;


import views.html.*;


import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

// File upload and image editing dependencies
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;


// Import models
import models.users.*;
import models.products.*;
import models.shopping.*;
import models.*;

public class HomeController extends Controller {

    /** Dependency Injection **/

    /** http://stackoverflow.com/questions/15600186/play-framework-dependency-injection **/
    private FormFactory formFactory;

    /** http://stackoverflow.com/a/37024198 **/
    private play.api.Environment env;

    /** http://stackoverflow.com/a/10159220/6322856 **/
    @Inject
    public  HomeController(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }

    // Get a user - if logged in email will be set in the session
    @Transactional
	public User getCurrentUser() {
		User u = User.getLoggedIn(session().get("email"));
		return u;
	}

    @Transactional
    public Result home() {

        return ok(home.render(User.getLoggedIn(session().get("email"))));
    }

    @Transactional
    public Result about() {

        return ok(about.render(User.getLoggedIn(session().get("email"))));
    }

    @Transactional
    public Result contact() {

        return ok(contact.render(User.getLoggedIn(session().get("email"))));
    }

    @Transactional
    public Result error() {

        return ok(error.render("Product out of stock", User.getLoggedIn(session().get("email"))));
    }




     // Load the add Customer view
    // Display an empty form in the view
    @Transactional
    public Result addCustomer() {   
        // Instantiate a form object based on the Product class
        Form<Customer> addCustomerForm = formFactory.form(Customer.class);
        // Render the Add Product View, passing the form object
        return ok(addCustomer.render(addCustomerForm, getCurrentUser()));
    }

    // Handle the form data when a new Customer is submitted
    @Transactional
    public Result addCustomerSubmit() {

        String saveImageMsg;

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Customer> newCustomerForm = formFactory.form(Customer.class).bindFromRequest();

        // Check for errors (based on Product class annotations)	
        if(newCustomerForm.hasErrors()) {
            // Display the form again
            return badRequest(addCustomer.render(newCustomerForm, getCurrentUser()));
        }
     
        Customer newCustomer = newCustomerForm.get();

        List<User> users = User.findAll();

        for (User u : users) {
            if (newCustomer.getEmail().equals(u.getEmail())){
              // Add a warning message to the flash session
        flash("warning", "Customer email already exits");
            
        // Return to admin home
        return redirect(controllers.routes.HomeController.addCustomer());
            }
        }
        

        newCustomer.setRole("Customer");
        
        // Save product now to set id (needed to update manytomany)
        newCustomer.save();

        
        // Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");
        
        // Save the image file
        saveImageMsg = saveFile(newCustomer.getEmail(), image);

        // Set a success message in temporary flash
        flash("success", "Customer " + newCustomerForm.get().getName() + " has been created" + " " + saveImageMsg);
            
        
        // Redirect to the login
        return redirect(controllers.security.routes.LoginCtrl.login());
    }

    

     // Update a Customer by getCurrentUser
    // called when edit button is pressed
    @Transactional
    public Result updateCustomer() {
         // Retrieve the Customer by getCurrentUser
        Customer c = (Customer)getCurrentUser();
        // Create a form based on the Customer class and fill using c
        Form<Customer> customerForm = Form.form(Customer.class).fill(c);
        // Render the updateCustomer view
        // pass the form as a parameter
        return ok(updateCustomer.render(customerForm, User.getLoggedIn(session().get("email"))));		
    }


    // Handle the form data when an updated customer is submitted
    @Transactional
    public Result updateCustomerSubmit() {

	String saveImageMsg;

        // Create a Customer form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Customer> updateCustomerForm = formFactory.form(Customer.class).bindFromRequest();

        // Check for errors (based on Customer class annotations)	
        if(updateCustomerForm.hasErrors()) {
            // Display the form again
            return badRequest(updateCustomer.render(updateCustomerForm, getCurrentUser()));
        }
        
        // Update the Customer (using its ID to select the existing object))
        Customer c = updateCustomerForm.get();

        //c.setId(id);
       
        // update (save) this Customer           
        c.update();

        // Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(c.getEmail(), image);
      
        // Add a success message to the flash session
        flash("success", "Customer " + updateCustomerForm.get().getName() + " has been updated" + " " + saveImageMsg);
            
        // Return to admin home
        return redirect(controllers.routes.HomeController.accountDetails());
    }

        

          // Delete Customer
    @Transactional
    public Result deleteCustomer() {
         
          // Retrieve the Customer by getCurrentUser
        Customer c = (Customer)getCurrentUser();
       

        String email = c.getEmail();
        
        // Call delete method
        Customer.find.ref(email).delete();
        // Add message to flash session 
        flash("success", "Customer has been deleted");
        // Redirect home
        return redirect(controllers.security.routes.LoginCtrl.login());
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


             @Transactional
    public Result updateAddress() {
         
         // Retrieve the Customer by getCurrentUser
        Customer c = (Customer)getCurrentUser();
        // Create a form based on the Customer class and fill using c
        Form<Customer> customerForm = Form.form(Customer.class).fill(c);
        // Render the updateCustomer view
        // pass the form as a parameter
        return ok(updateAddress.render(customerForm, User.getLoggedIn(session().get("email"))));		
    }

    @Transactional
    public Result updateAddressSubmit() {

        // Create a Customer form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Customer> updateAddressForm = formFactory.form(Customer.class).bindFromRequest();

        // Check for errors (based on Customer class annotations)	
        if(updateAddressForm.hasErrors()) {
            // Display the form again
            return badRequest(updateAddress.render(updateAddressForm, getCurrentUser()));
        }
        
        // Update the Customer (using its ID to select the existing object))
        Customer c = updateAddressForm.get();

        //c.setId(id);
       
        // update (save) this Customer           
        c.update();

        
        // Add a success message to the flash session
        flash("success", "Delivery Address for " + updateAddressForm.get().getName() + " has been updated");
            
        // Return to admin home
        return redirect(controllers.routes.HomeController.selectcard());
    }



       @Transactional
     public Result selectcard(){
      // Retrieve the Customer by getCurrentUser
        Customer c = (Customer)getCurrentUser();
        // Create a form based on the PaymentCard class
       List<PaymentCard> cardList = PaymentCard.findAll();
        // Render the addPaymentCard view
        // pass the id and form as parameters
        
     return ok (selectcard.render(cardList,c));

   }


     // Add Review by product ID
    // called when leave review button is pressed
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
   @Transactional
    public Result addReview(Long id) {
        // Retrieve the product by id
        Product p = Product.find.byId(id);
        // Instantiate a form object based on the Review class
        Form<Review> addReviewForm = formFactory.form(Review.class);
        // Render the Add Review View, passing the form object
        return ok(addReview.render(id,addReviewForm, getCurrentUser()));	
    }


    // Handle the form data when a new Review is submitted
    @Transactional
    public Result addReviewSubmit(Long id) {

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Review> newReviewForm = formFactory.form(Review.class).bindFromRequest();

        // Check for errors (based on Review class annotations)	
        if(newReviewForm.hasErrors()) {
            // Display the form again
            return badRequest(addReview.render(id,newReviewForm, getCurrentUser()));
        }
     
        Review newReview = newReviewForm.get();

        // Retrieve the product by id
        Product p = Product.find.byId(id);

        newReview.setProduct(p);

        Customer c = (Customer)getCurrentUser();

        newReview.setCustomer(c);

        // Save product now to set id (needed to update manytomany)
        newReview.save();

        p.setRating();

        p.update();
        
        // Redirect to the admin home
        return redirect(routes.ProductCtrl.listProducts(0, ""));
    }


    @Transactional
    public Result viewReview(Long id) {

        // Retrieve the product by id
        Product p = Product.find.byId(id);

        List<Review> reviews = p.getReviews();
    
        return ok(viewReview.render(reviews, getCurrentUser()));
    }
    
     

    
      // Update a PaymentCard by ID
    // called when edit button is pressed
    @Transactional
    public Result addPaymentCard() {
        // Retrieve the Customer by getCurrentUser
        Customer c = (Customer)getCurrentUser();
        // Create a form based on the PaymentCard class
        Form<PaymentCard> paymentCardForm = formFactory.form(PaymentCard.class);
        // Render the addPaymentCard view
        // pass the id and form as parameters
        return ok(addPaymentCard.render(paymentCardForm, User.getLoggedIn(session().get("email"))));		
    }


    // Handle the form data when a PaymentCard is submitted
    @Transactional
    public Result addPaymentCardSubmit() {

        // Create a PaymentCard form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<PaymentCard> addPaymentCardForm = formFactory.form(PaymentCard.class).bindFromRequest();

        // Check for errors (based on PaymentCard class annotations)	
        if(addPaymentCardForm.hasErrors()) {
            // Display the form again
            return badRequest(addPaymentCard.render(addPaymentCardForm, getCurrentUser()));
        }
        

        // Retrieve the Customer by id
        Customer c = (Customer)getCurrentUser();
        // Update the PaymentCard (using its ID to select the existing object))
        PaymentCard p = addPaymentCardForm.get();
        p.setCustomer(c);
   
        
        // update (save) this PaymentCard            
        p.save();

        // Add a success message to the flash session
        flash("success", "PaymentCard has been Created" );
            
        // Return to admin home
        return redirect(controllers.routes.HomeController.selectcard());
    }

        
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
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
            
        
        // Redirect to the admin home
        return redirect(routes.HomeController.forum());

    }



        // Thumbs up message
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    @Transactional
    public Result like(Long messageId) {
        
        // Get the message
        ForumMessage m = ForumMessage.find.byId(messageId);
        
        User u = getCurrentUser();

        if(m.getUser() != u){
         
           //add like
        m.addLike();
}  else {
         // Add message to flash session 
        flash("warning", "Cannot like your own posts");
	return redirect(routes.HomeController.forum());
         }
       
        m.update();
        // Show updated forum
        return redirect(routes.HomeController.forum());
    }


    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result dislike(Long messageId) {
        
        // Get the message
        ForumMessage m = ForumMessage.find.byId(messageId);
        
	 User u = getCurrentUser();

        if(m.getUser() != u){
         
        //add dislike
        m.addDislike();
}  else {
         // Add message to flash session 
        flash("warning", "Cannot dislike your own posts");
	return redirect(routes.HomeController.forum());
         }

        m.update();
        // Show updated forum
        return redirect(routes.HomeController.forum());
    }


        // Thumbs up message
    
    @Transactional
    public Result liked(Long messageId) {

        Reaction r = new Reaction();
        
        // Get the message
        ForumMessage message = ForumMessage.find.byId(messageId);

        User u = getCurrentUser();
        
        r.setMessage(message);

	r.setUser(u);

        r.setLiked(true);

        r.save();
           //add like
        message.addLike();
       
        message.update();
        // Show updated forum
        return redirect(routes.HomeController.forum());
    }


        @Transactional
    public Result forum() {

	 List<ForumMessage> messages = new ArrayList<ForumMessage>();

         messages = ForumMessage.findAll();

    
        return ok(forum.render(env, messages, getCurrentUser()));
    }
    

         // Delete message
    @Transactional
    public Result deleteMessage(Long id) {
        // Call delete method
        ForumMessage.find.ref(id).delete();
        // Add message to flash session 
        flash("success", "Message has been deleted");
        // Redirect to admin forum
        return redirect(routes.HomeController.forum());
    }


      // Delete Review
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
        return redirect(routes.ProductCtrl.product(product));
    }



    @Transactional
    public Result accountDetails(){

        Customer c = (Customer)getCurrentUser();
        
        return ok(accountDetails.render(env, c));
    }




}
