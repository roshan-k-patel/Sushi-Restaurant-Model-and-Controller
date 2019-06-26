package comp1206.sushi.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import comp1206.sushi.server.Comms;
import com.sun.tools.corba.se.idl.constExpr.Or;
import comp1206.sushi.common.*;
import comp1206.sushi.server.Server;
import comp1206.sushi.client.Comms;
import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client implements ClientInterface {

    private static final Logger logger = LogManager.getLogger("Client");
	public Comms comms;
	public Restaurant restaurantTemp;
	public ArrayList<User> users = new ArrayList<User>();
	public ArrayList<Postcode> postcodes = new ArrayList<Postcode>();
	public ArrayList<Dish> dishes = new ArrayList<Dish>();
	public ArrayList<Order> orders = new ArrayList<Order>();
 	public HashMap<Dish,Number> basket = new HashMap<>();
	private ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
	public HashMap<Order,Number> ordersAndCost = new HashMap<>();
	int cost;




	public Client(){
        logger.info("Starting up client...");

			comms = new Comms(this);


	}

	@Override
	public Restaurant getRestaurant() {
	return restaurantTemp;
	}
	
	@Override
	public String getRestaurantName() {
		return restaurantTemp.getName();
	}

	@Override
	public Postcode getRestaurantPostcode() {
		return restaurantTemp.getLocation();
	}
	
	@Override
	public User register(String username, String password, String address, Postcode postcode) {
		User newUser = new User(username, password, address, postcode);
		users.add(newUser);

		return	newUser;
	}

	@Override
	public User login(String username, String password) {

		for (User x : this.users){
			if (x.getName().equals(username) && x.getPassword().equals(password)){
				return x;
			}
		}

		return null;
	}

	@Override
	public List<Postcode> getPostcodes() {
		return this.postcodes;
	}

	@Override
	public List<Dish> getDishes() {
		return this.dishes;
	}

	@Override
	public String getDishDescription(Dish dish) {
		for (Dish x : this.dishes){
			if (x.getName().equals(dish.getName())){
				return dish.getDescription();
			}
		}
		return null;
	}

	@Override
	public Number getDishPrice(Dish dish) {
		for (Dish x : this.dishes){
			if (x.getName().equals(dish.getName())){
				return dish.getPrice();
			}
		}
		return null;
	}

	@Override
	public Map<Dish, Number> getBasket(User user) {
		return this.basket;

	}

	@Override
	public Number getBasketCost(User user) {

		 cost = 0;

		for (Map.Entry<Dish,Number> x : this.basket.entrySet()){
			cost = cost + (int) x.getKey().getPrice() * (int) x.getValue();
		}

		Number finalCost = cost;
		return  finalCost;

	}

	@Override
	public void addDishToBasket(User user, Dish dish, Number quantity) {
		basket.put(dish,quantity);

	}

	@Override
	public void updateDishInBasket(User user, Dish dish, Number quantity) {

		//if the number is set to 0 remove the dish from the basket
		if ((int) quantity == 0){
			for (Map.Entry<Dish,Number> x : this.basket.entrySet()){
				if(x.getKey().equals(dish)){
					this.basket.remove(dish);
				}
			}
		} else {

			for (Map.Entry<Dish, Number> x : this.basket.entrySet()) {


				if (x.getKey().equals(dish)) {
					x.setValue(quantity);
				}
			}

		}

	}

	@Override
	public Order checkoutBasket(User user) {

		//creates the new order and sets all the statuses
		Order order = new Order(user);
		order.setOrder(order.getBasket());
		orders.add(order);
		ordersAndCost.put(order,this.getBasketCost(user));
		order.setStatus("Pending");
		basket.clear();


		//sends the object to the server socket
		try {
			comms.streamOut.writeObject(order);
			System.out.println();
			System.out.println("An order worth $" + this.cost +  " has been successfully sent to the Server");
			this.notifyUpdate();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return order;
	}

	@Override
	public void clearBasket(User user) {
	for (Order x : this.orders) {
			if (x.getUser() == user) {
			x.clearBasket();
			}
		}
	}

	@Override
	public List<Order> getOrders(User user) {
		List<Order> orderList = new ArrayList<>();
		for (Order x : this.orders) {
			if (x.getUser() == user) {
				orderList.add(x);
			}
		}		return orderList;
	}

	@Override
	public boolean isOrderComplete(Order order) {
		if (order.getStatus().equals("Complete")){
			return true;
		}
		return false;
	}

	@Override
	public String getOrderStatus(Order order) {
		return order.getStatus();
	}

	@Override
	public Number getOrderCost(Order order) {
		for(Map.Entry<Order, Number> x : ordersAndCost.entrySet()){
			if (order == x.getKey()){
				return x.getValue();
			}
		}

		return 0;
	}

	@Override
	public void cancelOrder(Order order) {
		orders.remove(order);
		this.notifyUpdate();
	}

	@Override
	public void addUpdateListener(UpdateListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void notifyUpdate() {
		this.listeners.forEach(listener -> listener.updated(new UpdateEvent()));
	}

}
