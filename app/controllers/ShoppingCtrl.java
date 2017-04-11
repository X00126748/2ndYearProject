package controllers;

import controllers.security.CheckIfCustomer;
import controllers.security.Secured;
import models.products.Product;
import models.shopping.Basket;
import models.shopping.OrderItem;
import models.shopping.ShopOrder;
import models.shopping.PaymentCard;
import models.users.Customer;
import models.users.User;
import play.db.ebean.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.*;

import play.api.Environment;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

// Import models
// Import security controllers


@Security.Authenticated(Secured.class)
@With(CheckIfCustomer.class)
public class ShoppingCtrl extends Controller {


        /** http://stackoverflow.com/a/37024198 **/
    private Environment env;

    /** http://stackoverflow.com/a/10159220/6322856 **/
    @Inject
    public ShoppingCtrl(Environment e) {
        this.env = e;
    }
    
    // Get a user - if logged in email will be set in the session
	private Customer getCurrentUser() {
		return (Customer)User.getLoggedIn(session().get("email"));
	}


    @Transactional
    public Result addToBasket(Long id) {
        
        // Find the product
        Product p = Product.find.byId(id);
        
        // Get basket for logged in customer
        Customer customer = (Customer)User.getLoggedIn(session().get("email"));
        
        // Check if item in basket
        if (customer.getBasket() == null) {
            // If no basket, create one
            customer.setBasket(new Basket());
            customer.getBasket().setCustomer(customer);
            customer.update();
        }

        if (p.getStock() > 0){
        // Add product to the basket and save
        customer.getBasket().addProduct(p);
        customer.update();
        } else {

        return redirect(routes.HomeController.error());

        }
        // Show the basket contents     
        return ok(basket.render(env, customer));
    }
    

     @Transactional
public Result showBasket(){

        Customer customer = (Customer)User.getLoggedIn(session().get("email"));
        
        // Check if item in basket
        if (customer.getBasket() == null) {
            // If no basket, create one
            customer.setBasket(new Basket());
            customer.getBasket().setCustomer(customer);
            customer.update();
        }

      return ok(basket.render(env, getCurrentUser()));

}

    
      // Add an item to the basket
    @Transactional
    public Result addOne(Long itemId) {
        
        // Get the order item
        OrderItem item = OrderItem.find.byId(itemId);
        
        if (checkStock(itemId) == true){
           // Increment quantity
        item.increaseQty();

        } else {
           return redirect(routes.HomeController.error());

        }
       
        item.update();
        // Show updated basket
        return redirect(routes.ShoppingCtrl.showBasket());
    }

    @Transactional
    public Result removeOne(Long itemId) {
        
        // Get the order item
        OrderItem item = OrderItem.find.byId(itemId);
        // Get user
        Customer c = getCurrentUser();
        // Call basket remove item method
        c.getBasket().removeItem(item);
        c.getBasket().update();
        // back to basket
        return ok(basket.render(env, c));
    }


       // Add an item to the basket
    @Transactional
    public Result setItemSize(Long itemId, String size) {
        
        // Get the order item
        OrderItem item = OrderItem.find.byId(itemId);

        // Get user
        Customer c = getCurrentUser();

        //c.getBasket().removeItem(item);
        //c.getBasket().update();
    
           // Set Size
        item.setSize(size);
 
        item.update();
        c.getBasket().update();
        // Show updated basket
        return redirect(routes.ShoppingCtrl.showBasket());
    }

    public boolean checkStock(Long itemId){

         // Get the order item
        OrderItem item = OrderItem.find.byId(itemId);

        if (item.getProduct().getStock() > 0){
           return true;

        } else {
           return false;

        }

    }

    @Transactional
    public Result placeOrder(String cards) {
        Customer c = getCurrentUser();
        
        ShopOrder order = new ShopOrder();

        order.setCustomer(c);

        order.setItems(c.getBasket().getBasketItems());

        order.setOrderStatus("Processing Order");

        order.save();

for (OrderItem i: order.getItems()){
     
     i.setOrder(order);
     
      i.setselectedCard(cards);

     i.setBasket(null);

     i.update();
}

order.update();

c.getBasket().setBasketItems(null);
c.getBasket().update();

AddLoyaltyPoints();

c.addNumOfOrders();

c.update();

 // Set a success message in temporary flash
        flash("success", "Order has been Created" );

return ok (orderConfirmed.render(c, order));
}




    // Empty Basket
    @Transactional
    public Result emptyBasket() {
        
        Customer c = getCurrentUser();
        c.getBasket().removeAllItems();
        c.getBasket().update();

         // Set a success message in temporary flash
        flash("success", "Basket has been emptied" );
        
        return ok(basket.render(env, c));
    }


    
    // View an individual order
    @Transactional
    public Result viewOrder(long id) {
        ShopOrder order = ShopOrder.find.byId(id);
        return ok(orderConfirmed.render(getCurrentUser(), order));
    }

   
	// Get a list of orders
    @Transactional
    public Result orderHistory() {

        Customer c = getCurrentUser();

        List<ShopOrder> currentOrders = new ArrayList<ShopOrder>();
        List<ShopOrder> previousOrders = new ArrayList<ShopOrder>();
        
       // List<ShopOrder> orders = c.getOrders();

        for (ShopOrder o: c.getOrders()){

            if (o.getOrderStatus().equals("Processing Order")){

              currentOrders.add(o);

		} else { 

              previousOrders.add(o);

		}
 
			}	

      
        return ok(orderHistory.render(currentOrders,previousOrders, c));
    }
 

        @Transactional
    public Result cancelOrder(Long id, int points) {

         ShopOrder order = ShopOrder.find.byId(id);

         order.setOrderStatus("Order Cancelled");

         //order.save();

	 order.update();

        int quantity;

        Product p;

        for (OrderItem i: order.getItems()){
      
	quantity = i.getQuantity();

        p = i.getProduct();
        p.reStock(quantity);
	p.update();
}

order.update();

   
	DeleteLoyaltyPoints(points);

         Customer c = getCurrentUser();

         c.minusNumOfOrders();

         c.update();
        // Render the list orders view, passing parameters
       // return ok(orders.render(ordersList, getCurrentUser()));

	 // Set a success message in temporary flash
        flash("success", "Order has been Cancelled" );

          return orderHistory();
    } 
        


	@Transactional
    public void DeleteLoyaltyPoints(int points) {

         
         
        Customer c = getCurrentUser();
        int LoyaltyPointsEarned = c.getLoyaltyPointsEarned();
        int LoyaltyPointsLost =0;
        int pointsLeft=0;
         
        LoyaltyPointsLost = points;
        pointsLeft =(LoyaltyPointsEarned - LoyaltyPointsLost);

        
        c.setLoyaltyPointsEarned(pointsLeft);
        c.update();
  
         
        

     }




         @Transactional
    public void AddLoyaltyPoints() {

        
        Customer c = getCurrentUser();
        int LoyaltyPointsEarned = 0;
        



        for (ShopOrder o: c.getOrders()) {

            LoyaltyPointsEarned =(c.getLoyaltyPointsEarned()+ o.getLoyaltyPointsEarned()); 

        }
        
        c.setLoyaltyPointsEarned(LoyaltyPointsEarned);
        c.update();

   }




     

}
