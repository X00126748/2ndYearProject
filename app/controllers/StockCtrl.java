package controllers;

import controllers.security.*;

import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import models.products.*;
import models.stock.*;
import models.users.*;


import views.html.productAdmin.*;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

// Import models
// Import security controllers


@Security.Authenticated(Secured.class)
@With(CheckIfAdmin.class)
public class StockCtrl extends Controller {

        /** http://stackoverflow.com/a/37024198 **/
    private Environment env;

    /** http://stackoverflow.com/a/10159220/6322856 **/
    @Inject
    public StockCtrl(Environment e) {
        this.env = e;
    }
    
    
    // Get a user - if logged in email will be set in the session
	private Administrator getCurrentUser() {
		return (Administrator)User.getLoggedIn(session().get("email"));
	}


    @Transactional
    public Result addToBasket(Long id) {
        
        // Find the product
        Product p = Product.find.byId(id);
        
        // Get basket for logged in Administrator
        Administrator admin = getCurrentUser();
        
        // Check if item in basket
        if (admin.getStockBasket() == null) {
            // If no basket, create one
            admin.setStockBasket(new StockBasket());
            admin.getStockBasket().setAdministrator(admin);
            admin.update();
        }

     
        // Add product to the basket and save
        admin.getStockBasket().addProduct(p);
        admin.update();
        
        // Show the basket contents     
        return ok(stockBasket.render(env, admin));
    }



       @Transactional
    public Result addAmountToBasket(Long id, int amount) {
        
        // Find the product
        Product p = Product.find.byId(id);
        
        // Get basket for logged in Administrator
        Administrator admin = getCurrentUser();
        
        // Check if item in basket
        if (admin.getStockBasket() == null) {
            // If no basket, create one
            admin.setStockBasket(new StockBasket());
            admin.getStockBasket().setAdministrator(admin);
            admin.update();
        }

        for (int i=0; i < amount; i++){
        // Add product to the basket and save
        admin.getStockBasket().addProduct(p);
	}
	
	 admin.update();
        // Show the basket contents     
        return ok(stockBasket.render(env, admin));
    }
    

         @Transactional
    public Result addAllAmountToBasket(int amount) {
        
        // Get basket for logged in Administrator
        Administrator admin = getCurrentUser();
        
        // Check if item in basket
        if (admin.getStockBasket() == null) {
            // If no basket, create one
            admin.setStockBasket(new StockBasket());
            admin.getStockBasket().setAdministrator(admin);
            admin.update();
        }


        if(amount < 1){
           // Add message to flash session 
        flash("warning", "Cannot add stock less than 1");
        // Redirect home
        return redirect(routes.AdminProductCtrl.lowStock(0, ""));
        }
        
        // Instantiate products, an Array list of products			
        List<Product> products = new ArrayList<Product>();
        // Instantiate lowStock, an Array list of low stock products			
        List<Product> lowProducts = new ArrayList<Product>();

            // Get the list of ALL products with filter
            products = Product.findAll("");
        

        for (Product p : products) {
            if (p.getStock() < 10){
             lowProducts.add(p);
            }
        }

        for (Product p : lowProducts) {
            
              for (int i=0; i < amount; i++){
        // Add product to the basket and save
        admin.getStockBasket().addProduct(p);
	}

        }

	 admin.update();

        // Add message to flash session 
        flash("success", "All Product Stock added");

      // Show the basket contents     
       return redirect(routes.StockCtrl.showBasket());

        
    }
    


     @Transactional
public Result showBasket(){

      // Get basket for logged in Administrator
        Administrator admin = getCurrentUser();
        
        // Check if item in basket
        if (admin.getStockBasket() == null) {
            // If no basket, create one
            admin.setStockBasket(new StockBasket());
            admin.getStockBasket().setAdministrator(admin);
            admin.update();
        }

      return ok(stockBasket.render(env, getCurrentUser()));

}

    
      // Add an item to the basket
    @Transactional
    public Result addOne(Long itemId) {
        
        // Get the order item
        StockOrderItem item = StockOrderItem.find.byId(itemId);
        
       
        item.increaseQty();

       
        item.update();
        // Show updated basket
        return redirect(routes.StockCtrl.showBasket());
    }

    @Transactional
    public Result removeOne(Long itemId) {
        
        // Get the order item
        StockOrderItem item = StockOrderItem.find.byId(itemId);
        // Get user
        Administrator a = getCurrentUser();
        // Call basket remove item method
        a.getStockBasket().removeItem(item);
        a.getStockBasket().update();
        // back to basket
        return ok(stockBasket.render(env, a));
    }


       // Add an item to the basket
    @Transactional
    public Result setItemSize(Long itemId, String size) {
        
        // Get the order item
        StockOrderItem item = StockOrderItem.find.byId(itemId);

        // Get user
        Administrator a = getCurrentUser();

        //c.getBasket().removeItem(item);
        //c.getBasket().update();
    
           // Set Size
        item.setSize(size);
 
        item.update();
        a.getStockBasket().update();
        // Show updated basket
        return redirect(routes.StockCtrl.showBasket());
    }


    @Transactional
    public Result placeOrder() {
        Administrator a = getCurrentUser();
    

        StockOrder order = new StockOrder();

        order.setAdministrator(a);

        order.setItems(a.getStockBasket().getBasketItems());

        order.setOrderStatus("Processing Order");

        order.save();

for (StockOrderItem i: order.getItems()){
     
     i.setOrder(order);
     
     i.setBasket(null);

     i.update();
}

order.update();

a.getStockBasket().setBasketItems(null);
a.getStockBasket().update();

 // Set a success message in temporary flash
        flash("success", "Stock Order has been created" );
return ok (orderConfirmed.render(env, a, order));
}




    // Empty Basket
    @Transactional
    public Result emptyBasket() {
        
       Administrator a = getCurrentUser();
        a.getStockBasket().removeAllItems();
        a.getStockBasket().update();

      // Set a success message in temporary flash
        flash("success", "Stock Basket has been emptied" );
        return redirect(routes.StockCtrl.showBasket());
        
    }


    
    // View an individual order
    @Transactional
    public Result viewOrder(long id) {
        StockOrder order = StockOrder.find.byId(id);
        return ok(orderConfirmed.render(env, getCurrentUser(), order));
    }

   
	// Get a list of orders
    @Transactional
    public Result orderHistory() {

        Administrator a = getCurrentUser();

        List<StockOrder> currentOrders = new ArrayList<StockOrder>();
        List<StockOrder> previousOrders = new ArrayList<StockOrder>();
        
       // List<ShopOrder> orders = c.getOrders();

        for (StockOrder o: a.getStockOrders()){

            if (o.getOrderStatus().equals("Processing Order")){

              currentOrders.add(o);

		} else { 

              previousOrders.add(o);

		}
 
			}	

      
        return ok(orderHistory.render(env, currentOrders,previousOrders, a));
    }
 

        @Transactional
    public Result cancelOrder(Long id) {

         StockOrder order = StockOrder.find.byId(id);

         order.setOrderStatus("Order Cancelled");

         order.save();

	 order.update();

	 int quantity;

        Product p;

         for (StockOrderItem i: order.getItems()){
      
	quantity = i.getQuantity();

        p = i.getProduct();
        p.deStock(quantity);
	p.update();
}


        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));

          // Set a success message in temporary flash
        flash("success", "Stock Order has been Cancelled" );
           return redirect(routes.StockCtrl.orderHistory());
    } 
        

     

}
