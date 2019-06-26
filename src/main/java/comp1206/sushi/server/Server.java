package comp1206.sushi.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import comp1206.sushi.common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class Server implements ServerInterface {

    private static final Logger logger = LogManager.getLogger("Server");

    public Comms comms;
	public Restaurant restaurant;
	public ArrayList<Dish> dishes = new ArrayList<Dish>();
	public ArrayList<Drone> drones = new ArrayList<Drone>();
	public ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	public ArrayList<Order> orders = new ArrayList<Order>();
	public ArrayList<Staff> staff = new ArrayList<Staff>();
	public ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	public ArrayList<User> users = new ArrayList<User>();
	public ArrayList<Postcode> postcodes = new ArrayList<Postcode>();
	private ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
	static Stock stock;
	public DataPersistence dataPersistence;


	public Server() {
        logger.info("Starting up server...");

		try {
			comms = new Comms(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Postcode restaurantPostcode = new Postcode("SO17 1BJ");
		restaurant = new Restaurant("Mock Restaurant",restaurantPostcode);
		 stock = new Stock();
		 dataPersistence = new DataPersistence();


		Postcode postcode1 = addPostcode("SO17 1TJ");
		Postcode postcode2 = addPostcode("SO17 1BX");
		Postcode postcode3 = addPostcode("SO17 2NJ");
		Postcode postcode4 = addPostcode("SO17 1TW");
		Postcode postcode5 = addPostcode("SO17 2LB");
		Postcode postcode6 = addPostcode("SO17 1BJ");
		
		Supplier supplier1 = addSupplier("Supplier 1",postcode1);
		Supplier supplier2 = addSupplier("Supplier 2",postcode2);
		Supplier supplier3 = addSupplier("Supplier 3",postcode3);
		
		Ingredient ingredient1 = addIngredient("Ingredient 1","grams",supplier1,1,5,1);
		Ingredient ingredient2 = addIngredient("Ingredient 2","grams",supplier2,1,5,1);
		Ingredient ingredient3 = addIngredient("Ingredient 3","grams",supplier3,1,5,1);
		
		Dish dish1 = addDish("Dish 1","Dish 1",1,1,10);
		Dish dish2 = addDish("Dish 2","Dish 2",2,1,10);
		Dish dish3 = addDish("Dish 3","Dish 3",3,1,10);


		User bill = new User("Admin","Password","University Road",postcode1);
		users.add(bill);

		orders.add(new Order(users.get(0)));

		addIngredientToDish(dish1,ingredient1,1);
		addIngredientToDish(dish1,ingredient2,2);
		addIngredientToDish(dish2,ingredient2,3);
		addIngredientToDish(dish2,ingredient3,1);
		addIngredientToDish(dish3,ingredient1,2);
		addIngredientToDish(dish3,ingredient3,1);
		
		addStaff("Staff 1");
		addStaff("Staff 2");
		addStaff("Staff 3");
		
		addDrone(1);
		addDrone(2);
		addDrone(3);

		dataPersistence.setDishes(dishes);
		dataPersistence.setDishStock(stock.getDishStock());
		dataPersistence.setDrones(drones);
		dataPersistence.setIngredients(ingredients);
		dataPersistence.setIngredientStock(stock.getIngredientStock());
		dataPersistence.setOrders(orders);
		dataPersistence.setUsers(users);
		dataPersistence.setPostcodes(postcodes);
		dataPersistence.setRestaurant(restaurant);
		dataPersistence.setStaff(staff);
		dataPersistence.setSuppliers(suppliers);


	/*	while(true) {

			try {
			Object o =	comms.streamIn.readObject();

				if (o instanceof Order){
					System.out.println("An order has been found");
					this.orders.add((Order) o);
					System.out.println("Order successfully added");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}*/


	}




	@Override
	public List<Dish> getDishes() {
		return this.dishes;
	}

	@Override
	public void loadConfiguration(String filename) {
		Configuration config = new Configuration(filename);

		//calls the config methods to read everything in the file
        config.readPostcodes(filename);
        this.notifyUpdate();
		config.readRestaurants(filename);
		this.notifyUpdate();
		config.readStaff(filename, stock);
		this.notifyUpdate();
		config.readSuppliers(filename);
		this.notifyUpdate();
		config.readIngredients(filename);
		this.notifyUpdate();
		config.readDishes(filename);
		this.notifyUpdate();
		config.readUsers(filename);
		this.notifyUpdate();
		config.readDrones(filename,stock);
		this.notifyUpdate();
		config.readOrders(filename);
		this.notifyUpdate();
		config.readStock(filename);
		this.notifyUpdate();

		//copying all the stored information from the config file to the server
		for (Postcode x : config.getPostcodes()){
            if(!this.postcodes.contains(x)){
                this.postcodes.add(x);
				System.out.println(x.getName() + " loaded from config");

			}
        }

		System.out.println();
		this.notifyUpdate();


		this.restaurant = config.getRestaurants();
		System.out.println(this.restaurant.getName() + " Restaurant loaded from config");
		comms.sendMessage(this.restaurant);
		this.notifyUpdate();
		System.out.println();


		for (Supplier x : config.getSuppliers()){
            if(!this.suppliers.contains(x)){
                this.suppliers.add(x);
				System.out.println(x.getName() + " supplier loaded from config");

			}
        }

		System.out.println();
		this.notifyUpdate();


		for (Ingredient x : config.getIngredients()){
            if(!this.ingredients.contains(x)){
                this.ingredients.add(x);
				System.out.println(x.getName() + " ingredient loaded from config");
            }
        }

		System.out.println();
		this.notifyUpdate();


		for (Dish x : config.getDishes()){
                if(!this.dishes.contains(x)){
                    this.dishes.add(x);
                    this.setStock(x,0);											//CHANGE TO GET THE ACTUAL DISH STOCK FROM THE CONFIG LIST
					this.notifyUpdate();
					System.out.println(x.getName() + " dish loaded from config");
                }
            }

		System.out.println();
		this.notifyUpdate();

		for (User x : config.getUsers()){
            if(!this.users.contains(x)){
                this.users.add(x);
				System.out.println(x.getName() + " user loaded from config");
            }
        }

		System.out.println();
		this.notifyUpdate();

		for (Staff x : config.getStaff()){
            if(!this.staff.contains(x)){
                this.staff.add(x);
				System.out.println(x.getName() + " staff loaded from config");
            }
        }

		System.out.println();
		this.notifyUpdate();

		for (Drone x : config.getDrones()){
            if(!this.drones.contains(x)){
                this.drones.add(x);
				System.out.println(x.getName() + " loaded from config");
            }
        }

		System.out.println();
		this.notifyUpdate();






		for (Dish x : config.getDishStock().keySet()){
        	if(stock.getDishStock().entrySet().contains(x)) {
        		stock.setDishStock(x,config.getDishStock().get(x));
				System.out.println("The stock of the existing dish " + x.getName() + " has been set");
            } else {
        		stock.addDishStock(x,config.getDishStock().get(x));
				System.out.println("The stock of the new dish " + x.getName() + " has been added!!");
			}
        }

		System.out.println();
		this.notifyUpdate();


		for (Ingredient x : config.getIngredientStock().keySet()){
            if(stock.getIngredientStock().entrySet().contains(x)){
                stock.setIngredientStock(x,config.getIngredientStock().get(x));
				System.out.println("The stock of the existing ingredient " + x.getName() + " has been set");
            } else {
            	stock.addIngredientStock(x,config.getIngredientStock().get(x));
				System.out.println("The stock of the new ingredient " + x.getName() + " has been added!!");
			}
        }


		System.out.println();
		this.notifyUpdate();

		this.orders.equals(config.getOrders());

		for (Order x : this.orders){
				System.out.println("An order placed by " + x.getUser().getName() + " has been loaded at " + x.getName() + " from config");

		}

		System.out.println();
		this.notifyUpdate();



		for(Drone x : this.drones){
			x.run();
		}

		dataPersistence.setDishes(dishes);
		dataPersistence.setDishStock(stock.getDishStock());
		dataPersistence.setDrones(drones);
		dataPersistence.setIngredients(ingredients);
		dataPersistence.setIngredientStock(stock.getIngredientStock());
		dataPersistence.setOrders(orders);
		dataPersistence.setUsers(users);
		dataPersistence.setPostcodes(postcodes);
		dataPersistence.setRestaurant(restaurant);
		dataPersistence.setStaff(staff);
		dataPersistence.setSuppliers(suppliers);

		System.out.println("Configuration: " + filename + " has been properly loaded, let's check that the stock was added correctly");
		System.out.println();

		System.out.println("Dishes in stock object: " + stock.dishStock.entrySet());
		System.out.println();
		System.out.println("Ingredients in stock object: " + stock.ingredientStock.entrySet());

	}





	@Override
	public Dish addDish(String name, String description, Number price, Number restockThreshold, Number restockAmount) {
		Dish newDish = new Dish(name,description,price,restockThreshold,restockAmount);
		this.dishes.add(newDish);
		stock.addDishStock(newDish,0);
		try{comms.sendMessage(newDish);} catch (Exception e){

		}
		this.notifyUpdate();
		return newDish;
	}
	
	@Override
	public void removeDish(Dish dish) {
		this.dishes.remove(dish);
		stock.removeDishStock(dish);
		this.notifyUpdate();
	}

	@Override
	public Map<Dish, Number> getDishStockLevels() {
		return stock.dishStock;
	}
	
	@Override
	public void setRestockingIngredientsEnabled(boolean enabled) {
		
	}

	@Override
	public void setRestockingDishesEnabled(boolean enabled) {
		
	}
	
	@Override
	public void setStock(Dish dish, Number dishStock) {
	stock.setDishStock(dish,dishStock);

	}

	@Override
	public void setStock(Ingredient ingredient, Number ingredientStock) {
	stock.setIngredientStock(ingredient,ingredientStock);

	}

	@Override
	public List<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public Ingredient addIngredient(String name, String unit, Supplier supplier,
			Number restockThreshold, Number restockAmount, Number weight) {
		Ingredient mockIngredient = new Ingredient(name,unit,supplier,restockThreshold,restockAmount,weight);
		this.ingredients.add(mockIngredient);
		stock.addIngredientStock(mockIngredient,0);
		this.notifyUpdate();
		return mockIngredient;
	}

	@Override
	public void removeIngredient(Ingredient ingredient) {
		int index = this.ingredients.indexOf(ingredient);
		this.ingredients.remove(index);
		stock.removeIngredientStock(ingredient);
		this.notifyUpdate();
	}

	@Override
	public List<Supplier> getSuppliers() {
		return this.suppliers;
	}

	@Override
	public Supplier addSupplier(String name, Postcode postcode) {
		Supplier mock = new Supplier(name,postcode);
		this.suppliers.add(mock);
		return mock;
	}


	@Override
	public void removeSupplier(Supplier supplier) {
		int index = this.suppliers.indexOf(supplier);
		this.suppliers.remove(index);
		this.notifyUpdate();
	}

	@Override
	public List<Drone> getDrones() {
		return this.drones;
	}

	@Override
	public Drone addDrone(Number speed) {
		Drone mock = new Drone(speed,stock);
		this.drones.add(mock);
		return mock;
	}

	@Override
	public void removeDrone(Drone drone) {
		int index = this.drones.indexOf(drone);
		this.drones.remove(index);
		this.notifyUpdate();
	}

	@Override
	public List<Staff> getStaff() {
		return this.staff;
	}

	@Override
	public Staff addStaff(String name) {
		Staff mock = new Staff(name, stock);
		this.staff.add(mock);
		this.notifyUpdate();
		return mock;
	}

	@Override
	public void removeStaff(Staff staff) {
		this.staff.remove(staff);
		this.notifyUpdate();
	}

	@Override
	public List<Order> getOrders() {
		return this.orders;
	}

	@Override
	public void removeOrder(Order order) {
		int index = this.orders.indexOf(order);
		this.orders.remove(index);
		this.notifyUpdate();
	}
	
	@Override
	public Number getOrderCost(Order order) {
		return order.getBasketCost();

	}

	@Override
	public Map<Ingredient, Number> getIngredientStockLevels() {

		return stock.ingredientStock;
	}

	@Override
	public Number getSupplierDistance(Supplier supplier) {
		return supplier.getDistance();
	}

	@Override
	public Number getDroneSpeed(Drone drone) {
		return drone.getSpeed();
	}

	@Override
	public Number getOrderDistance(Order order) {
		Order mock = (Order)order;
		return mock.getDistance();
	}

	@Override
	public void addIngredientToDish(Dish dish, Ingredient ingredient, Number quantity) {
		if(quantity == Integer.valueOf(0)) {
			removeIngredientFromDish(dish,ingredient);
		} else {
			dish.getRecipe().put(ingredient,quantity);
		}
	}

	@Override
	public void removeIngredientFromDish(Dish dish, Ingredient ingredient) {
		dish.getRecipe().remove(ingredient);
		this.notifyUpdate();
	}

	@Override
	public Map<Ingredient, Number> getRecipe(Dish dish) {
		return dish.getRecipe();
	}

	@Override
	public List<Postcode> getPostcodes() {
		return this.postcodes;
	}

	@Override
	public Postcode addPostcode(String code) {
		Postcode mock = new Postcode(code);
		this.postcodes.add(mock);
		try {comms.sendMessage(mock);} catch (Exception e) {}
		this.notifyUpdate();
		return mock;
	}

	@Override
	public void removePostcode(Postcode postcode) throws UnableToDeleteException {
		this.postcodes.remove(postcode);
		this.notifyUpdate();
	}

	@Override
	public List<User> getUsers() {
		return this.users;
	}
	
	@Override
	public void removeUser(User user) {
		this.users.remove(user);
		this.notifyUpdate();
	}



	@Override
	public void setRecipe(Dish dish, Map<Ingredient, Number> recipe) {
		for(Entry<Ingredient, Number> recipeItem : recipe.entrySet()) {
			addIngredientToDish(dish,recipeItem.getKey(),recipeItem.getValue());
		}
		this.notifyUpdate();
	}

	@Override
	public boolean isOrderComplete(Order order) {
		return true;
	}

	@Override
	public String getOrderStatus(Order order) {
		Random rand = new Random();
		if(rand.nextBoolean()) {
			return "Complete";
		} else {
			return "Pending";
		}
	}
	
	@Override
	public String getDroneStatus(Drone drone) {
		Random rand = new Random();
		if(rand.nextBoolean()) {
			return "Idle";
		} else {
			return "Flying";
		}
	}
	
	@Override
	public String getStaffStatus(Staff staff) {
		Random rand = new Random();
		if(rand.nextBoolean()) {
			return "Idle";
		} else {
			return "Working";
		}
	}

	@Override
	public void setRestockLevels(Dish dish, Number restockThreshold, Number restockAmount) {
		dish.setRestockThreshold(restockThreshold);
		dish.setRestockAmount(restockAmount);
		this.notifyUpdate();
	}

	@Override
	public void setRestockLevels(Ingredient ingredient, Number restockThreshold, Number restockAmount) {
		ingredient.setRestockThreshold(restockThreshold);
		ingredient.setRestockAmount(restockAmount);
		this.notifyUpdate();
	}

	@Override
	public Number getRestockThreshold(Dish dish) {
		return dish.getRestockThreshold();
	}

	@Override
	public Number getRestockAmount(Dish dish) {
		return dish.getRestockAmount();
	}

	@Override
	public Number getRestockThreshold(Ingredient ingredient) {
		return ingredient.getRestockThreshold();
	}

	@Override
	public Number getRestockAmount(Ingredient ingredient) {
		return ingredient.getRestockAmount();
	}

	@Override
	public void addUpdateListener(UpdateListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void notifyUpdate() {
		this.listeners.forEach(listener -> listener.updated(new UpdateEvent()));
	}

	@Override
	public Postcode getDroneSource(Drone drone) {
		return drone.getSource();
	}

	@Override
	public Postcode getDroneDestination(Drone drone) {
		return drone.getDestination();
	}

	@Override
	public Number getDroneProgress(Drone drone) {
		return drone.getProgress();
	}

	@Override
	public String getRestaurantName() {
		return restaurant.getName();
	}

	@Override
	public Postcode getRestaurantPostcode() {
		return restaurant.getLocation();
	}
	
	@Override
	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void addUser(String name, String password, String address, Postcode postcode) {
		User user = new User(name,password,address,postcode);
		users.add(user);
	}

}
