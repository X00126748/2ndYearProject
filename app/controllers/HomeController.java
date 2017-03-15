package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;


import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import play.mvc.Security;


import views.html.*;

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

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Customer> newCustomerForm = formFactory.form(Customer.class).bindFromRequest();

        // Check for errors (based on Product class annotations)	
        if(newCustomerForm.hasErrors()) {
            // Display the form again
            return badRequest(addCustomer.render(newCustomerForm, getCurrentUser()));
        }
     
        Customer newCustomer = newCustomerForm.get();

        newCustomer.setRole("Customer");
        
        // Save product now to set id (needed to update manytomany)
        newCustomer.save();
        
        // Redirect to the admin home
        return redirect(controllers.security.routes.LoginCtrl.login());
    }


     // Add Review by product ID
    // called when leave review button is pressed
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

        // Check for errors (based on Product class annotations)	
        if(newReviewForm.hasErrors()) {
            // Display the form again
            return badRequest(addReview.render(id,newReviewForm, getCurrentUser()));
        }
     
        Review newReview = newReviewForm.get();

        // Retrieve the product by id
        Product p = Product.find.byId(id);

        newReview.setProduct(p);

        // Save product now to set id (needed to update manytomany)
        newReview.save();
        
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
        // Retrieve the Customer by id
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
        return redirect(controllers.routes.HomeController.home());
    }


        @Transactional
    public Result addForumMessage() {
        // Retrieve the product by id
        Customer c = (Customer)getCurrentUser();
        // Instantiate a form object based on the Review class
        Form<ForumMessage> addForumMessageForm = formFactory.form(ForumMessage.class);
        // Render the Add Review View, passing the form object
        return ok(addForumMessage.render(addForumMessageForm, (Customer)getCurrentUser()));	
    }


    // Handle the form data when a new Review is submitted
    @Transactional
    public Result addForumMessageSubmit() {

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<ForumMessage> newForumMessageForm = formFactory.form(ForumMessage.class).bindFromRequest();

        // Check for errors (based on Product class annotations)	
        if(newForumMessageForm.hasErrors()) {
            // Display the form again
            return badRequest(addForumMessage.render(newForumMessageForm, (Customer)getCurrentUser()));
        }
     
        ForumMessage newForumMessage = newForumMessageForm.get();

        // Retrieve the product by id
        Customer c = (Customer)getCurrentUser();

        newForumMessage.setCustomer(c);

        // Save product now to set id (needed to update manytomany)
        newForumMessage.save();
        
        // Redirect to the admin home
        return redirect(routes.HomeController.home());

    }


}
