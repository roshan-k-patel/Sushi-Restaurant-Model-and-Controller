package comp1206.sushi.common;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPersistence {

    public HashMap<Ingredient,Number> ingredientStock;

    public HashMap<Dish,Number> dishStock;
    public ArrayList<Postcode> postcodes;
    public ArrayList<Staff> staff;
    public ArrayList<Drone> drones;
    public ArrayList<Dish> dishes;
    public Restaurant restaurant;
    public ArrayList<Order> orders;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Supplier> suppliers;
    public ArrayList<User> users;

    public DataPersistence(){
        users = new ArrayList<User>();
        dishes = new ArrayList<Dish>();
        staff = new ArrayList<Staff>();
        ingredients = new ArrayList<Ingredient>();
        drones = new ArrayList<Drone>();
        orders = new ArrayList<Order>();
        postcodes = new ArrayList<Postcode>();
        suppliers = new ArrayList<Supplier>();

    }


    public void setIngredientStock(HashMap<Ingredient, Number> ingredientStock) {
        this.ingredientStock = ingredientStock;
    }

    public void setDishStock(HashMap<Dish, Number> dishStock) {
        this.dishStock = dishStock;
    }

    public void setPostcodes(ArrayList<Postcode> postcodes) {
        this.postcodes = postcodes;
    }

    public void setStaff(ArrayList<Staff> staff) {
        this.staff = staff;
    }

    public void setDrones(ArrayList<Drone> drones) {
        this.drones = drones;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSuppliers(ArrayList<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public HashMap<Ingredient, Number> getIngredientStock() {
        return ingredientStock;
    }

    public HashMap<Dish, Number> getDishStock() {
        return dishStock;
    }

    public ArrayList<Postcode> getPostcodes() {
        return postcodes;
    }

    public ArrayList<Staff> getStaff() {
        return staff;
    }

    public ArrayList<Drone> getDrones() {
        return drones;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

}
