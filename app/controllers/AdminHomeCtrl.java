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

        newAdministrator.setRole("admin");
        
        // Save Administrator now to set id ()
        newAdministrator.save();
        


	// Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(newAdministrator.getName(), image);

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

        saveImageMsg = saveFile(a.getName(), image);
      
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

         
            // Get the list of ALL products with filter
            ordersList = ShopOrder.findAll();
        
        // Render the list orders view, passing parameters
        return ok(orders.render(ordersList, getCurrentUser()));
    } 


        // Get a list of orders
    @Transactional
    public Result stockOrders() {

        List<StockOrder> ordersList = new ArrayList<StockOrder>();

         
            // Get the list of ALL products with filter
            ordersList = StockOrder.findAll();
        
        // Render the list orders view, passing parameters
        return ok(stockOrders.render(ordersList, getCurrentUser()));
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


        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));

          return orders();
    } 



      @Transactional
    public Result setStockOrderForDelivery(Long id) {

         StockOrder order = StockOrder.find.byId(id);

         order.setOrderStatus("Order Complete");

         order.save();

	 order.update();


        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));

          return stockOrders();
    } 

        
          // Get reports
    @Transactional
    public Result reports() {

       // Instantiate products, an Array list of products			
        List<Product> products = new ArrayList<Product>();
     
	products = Product.findAll("");
     
        List<Product> mostPopular = new ArrayList<Product>(3);
        mostPopular.add(null);
        mostPopular.add(null);
	mostPopular.add(null);
       
	 //Product mostPopular[] = new Product[3];

        //Product mostPopular = products.get(0);
       // List<Product> leastPopular = new ArrayList<Product>();

        List<Product> leastPopularFind = doSelectionSort(products);

        List<Product> leastPopular = new ArrayList<Product>(3);
        leastPopular.add(leastPopularFind.get(0));
        leastPopular.add(leastPopularFind.get(1));
	leastPopular.add(leastPopularFind.get(2));
        
        
        //Product leastPopular = products.get(0);
        int mpIndex = 0;
	int mp2Index = 0;
	int mp3Index = 0;

        int lpIndex = 0;

        Product mostSold = products.get(0);
        Product leastSold = products.get(0);
        int msIndex = 0;
        int lsIndex = 0;
        
        for (int i=0; i < products.size(); i++) {
        
        if (products.get(i).getAvgStars() > products.get(mpIndex).getAvgStars()){
           mpIndex = i;
           mostPopular.set(0, products.get(i));
        } 

        //else if (products.get(i).getAvgStars() == products.get(mpIndex).getAvgStars()){
         //  mp2Index = i;
           //mostPopular.set(1, products.get(i));
        //}

        
        if (products.get(i).getAvgStars() > products.get(mp2Index).getAvgStars() && products.get(i).getAvgStars() < products.get(mpIndex).getAvgStars()){
           mp2Index = i;
           mostPopular.set(1, products.get(i));
        } 

        if (products.get(i).getAvgStars() > products.get(mp3Index).getAvgStars() && products.get(i).getAvgStars() < products.get(mp2Index).getAvgStars()){
           mp3Index = i;
           mostPopular.set(2, products.get(i));
        }

       /*
        if (products.get(i).getAvgStars() < products.get(lpIndex).getAvgStars()){
           lpIndex = i;
           leastPopular = products.get(i);
        }

        */
        if (products.get(i).getAmountSold() > products.get(msIndex).getAmountSold()){
           msIndex = i;
           mostSold = products.get(i);
        }

        if (products.get(i).getAmountSold() < products.get(lsIndex).getAmountSold()){
           lsIndex = i;
           leastSold = products.get(i);
        }
  }
       

        return ok(reports.render(env,mostPopular,leastPopular, mostSold, leastSold,  getCurrentUser()));
    } 

        // Get reports
    @Transactional
    public Result mostPopProducts() {

       // Instantiate products, an Array list of products			
        List<Product> products = new ArrayList<Product>();
     
	products = Product.findAll("");
     
        List<Product> mostPopular = new ArrayList<Product>(3);
        mostPopular.add(null);
        mostPopular.add(null);
	mostPopular.add(null);
       
	 //Product mostPopular[] = new Product[3];

        //Product mostPopular = products.get(0);
      
        
        //Product leastPopular = products.get(0);
        int mpIndex = 0;
	int mp2Index = 0;
	int mp3Index = 0;

        
        for (int i=0; i < products.size(); i++) {
        
        if (products.get(i).getAvgStars() > products.get(mpIndex).getAvgStars()){
           mpIndex = i;
           mostPopular.set(0, products.get(i));
        } 

        else if (products.get(i).getAvgStars() == products.get(mpIndex).getAvgStars()){
           mp2Index = i;
           mostPopular.set(1, products.get(i));
        } else if (products.get(i).getAvgStars() == products.get(mp2Index).getAvgStars()){
           mp3Index = i;
           mostPopular.set(2, products.get(i));
        }


        
        if (products.get(i).getAvgStars() > products.get(mp2Index).getAvgStars() && products.get(i).getAvgStars() < products.get(mpIndex).getAvgStars()){
           mp2Index = i;
           mostPopular.set(1, products.get(i));
        } 

        if (products.get(i).getAvgStars() > products.get(mp3Index).getAvgStars() && products.get(i).getAvgStars() < products.get(mp2Index).getAvgStars()){
           mp3Index = i;
           mostPopular.set(2, products.get(i));
        }

   
  }
       

        return ok(mostPopProducts.render(env, mostPopular, getCurrentUser()));
    } 

	      // Get reports
    @Transactional
    public Result leastPopProducts() {

       // Instantiate products, an Array list of products			
        List<Product> products = new ArrayList<Product>();
     
	products = Product.findAll("");
     
        List<Product> leastPopularFind = doSelectionSort(products);

        List<Product> leastPopular = new ArrayList<Product>(3);
        leastPopular.add(leastPopularFind.get(0));
        leastPopular.add(leastPopularFind.get(1));
	leastPopular.add(leastPopularFind.get(2));
       

        return ok(leastPopProducts.render(env, leastPopularFind, getCurrentUser()));
    } 


       public static List<Product> doSelectionSort(List<Product> arr){
         
        int min;
    for (int i = 0; i < arr.size(); i++) {
        // Assume first element is min
        min = i;
        for (int j = i + 1; j < arr.size(); j++) {
            if (arr.get(j).getAvgStars() < arr.get(min).getAvgStars()) {
                min = j;

            }
        }
       
            if (min != i){
            Product temp = arr.get(i);
            arr.set(min, arr.get(i));
            arr.set(i, temp);
        }

	}

       return arr;
     
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


         @Transactional
         public Result forum() {

	 List<ForumMessage> messages = new ArrayList<ForumMessage>();

         messages = ForumMessage.findAll();

    
        return ok(forum.render(env, messages, getCurrentUser()));
    }
    

}
