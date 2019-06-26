package comp1206.sushi.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import comp1206.sushi.common.Order;

public class Order extends Model implements Serializable {

	private String status;
	private User user;
	private Map<Dish,Number> order;
	
	public Order(User user) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		this.name = dtf.format(now);
		this.user = user;
		this.order = new HashMap<Dish,Number>();
	}

	public Number getDistance() {
		return 1;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		notifyUpdate("status",this.status,status);
		this.status = status;
	}

	public void setOrder (Map<Dish,Number> order){
		this.order = order;
	}

	public void setUser (User user){
		this.user = user;
	}

	public Number returnAmount(Dish dish){
		for (Map.Entry<Dish, Number> x : this.order.entrySet()){
			if (dish.getName().equals(x.getKey())){
				return x.getValue();
			}

		}

		return null;
	}

	public Map<Dish, Number> getBasket(){
		return this.order;
	}

	public Number getBasketCost(){
		int cost =0;
		for (Map.Entry<Dish, Number> x : this.order.entrySet()){
		    int amount = (int) x.getValue();
		    int price = (int) x.getKey().getPrice();
			cost = cost + (amount*price);
		}

		return cost;
	}

	public User getUser(){
		return this.user;
	}

    public void addDishToBasket(Dish dish, Number quantity){
	    this.order.put(dish,quantity);
    }

    public void updateDishInBasket(Dish dish, Number quantity){
	    this.order.replace(dish,quantity);
    }

    public void clearBasket(){
	    this.order.clear();
    }



}
