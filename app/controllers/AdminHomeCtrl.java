package controllers;

import controllers.security.*;

import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import play.Logger;

// File upload and image editing dependencies
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;


// Import models and views
import models.users.*;
import models.products.*;
import models.shopping.*;

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

        // Create a Administrator form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Administrator> newAdministratorForm = formFactory.form(Administrator.class).bindFromRequest();

        // Check for errors (based on Administrator class annotations)	
        if(newAdministratorForm.hasErrors()) {
            // Display the form again
            return badRequest(addAdmin.render(newAdministratorForm, getCurrentUser()));
        }
     
        Administrator newAdministrator = newAdministratorForm.get();
        
        // Save Administrator now to set id (needed to update manytomany)
        newAdministrator.save();
        

        // Set a success message in temporary flash
        flash("success", "Administrator " + newAdministrator.getName() + " has been created");
            
        // Redirect to the admin home
        return redirect(controllers.routes.AdminProductCtrl.index());
    }

      // Get a list of orders
    @Transactional
    public Result orders() {

        List<ShopOrder> ordersList = new ArrayList<ShopOrder>();

         
            // Get the list of ALL products with filter
            ordersList = ShopOrder.findAll();
        
        // Render the list orders view, passing parameters
        return ok(orders.render(ordersList, getCurrentUser()));
    } 
        
   
}
