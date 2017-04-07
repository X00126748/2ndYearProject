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

        if (p.getStock() > 0){
        // Add product to the basket and save
        admin.getStockBasket().addProduct(p);
        admin.update();
        } else {

        return redirect(routes.HomeController.error());

        }
        // Show the basket contents     
        return ok(stockBasket.render(admin));
    }
    

     @Transactional
public Result showBasket(){
      return ok(stockBasket.render(getCurrentUser()));

}

    
      // Add an item to the basket
    @Transactional
    public Result addOne(Long itemId) {
        
        // Get the order item
        StockOrderItem item = StockOrderItem.find.byId(itemId);
        
        if (checkStock(itemId) == true){
           // Increment quantity
        item.increaseQty();

        } else {
           return redirect(routes.HomeController.error());

        }
       
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
        return ok(stockBasket.render(a));
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

    public boolean checkStock(Long itemId){

         // Get the order item
        StockOrderItem item = StockOrderItem.find.byId(itemId);

        if (item.getProduct().getStock() > 0){
           return true;

        } else {
           return false;

        }

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
return ok (orderConfirmed.render(a, order));
}




    // Empty Basket
    @Transactional
    public Result emptyBasket() {
        
       Administrator a = getCurrentUser();
        a.getStockBasket().removeAllItems();
        a.getStockBasket().update();

      // Set a success message in temporary flash
        flash("success", "Stock Basket has been emptied" );
        
        return ok(stockBasket.render(a));
    }


    
    // View an individual order
    @Transactional
    public Result viewOrder(long id) {
        StockOrder order = StockOrder.find.byId(id);
        return ok(orderConfirmed.render(getCurrentUser(), order));
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

      
        return ok(orderHistory.render(currentOrders,previousOrders, a));
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

          return orderHistory();
    } 
        

     

}
