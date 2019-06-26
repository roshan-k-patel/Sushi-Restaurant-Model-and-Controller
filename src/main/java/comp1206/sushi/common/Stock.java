package comp1206.sushi.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stock extends Model implements Serializable {

    public HashMap<Ingredient,Number> ingredientStock;
    public HashMap<Dish,Number> dishStock;

    public Stock(){
        ingredientStock = new HashMap<>();
        dishStock = new HashMap<>();
    }

    public  HashMap<Ingredient, Number> getIngredientStock() {
        return ingredientStock;
    }

    public HashMap<Dish, Number> getDishStock() {
        return dishStock;
    }

    public void setIngredientStock(Ingredient ingredient, Number ingredientStock) {
        this.ingredientStock.replace(ingredient,ingredientStock);
    }

    public void setDishStock(Dish dish, Number dishStock) {
        this.dishStock.replace(dish,dishStock);
    }

    public void addDishStock(Dish dish, Number dishStock){
        this.dishStock.put(dish,dishStock);
    }

    public void addIngredientStock(Ingredient ingredient, Number ingredientStock){
        this.ingredientStock.put(ingredient,ingredientStock);
    }

    public void removeDishStock(Dish dish){
        this.dishStock.remove(dish);
    }

    public void removeIngredientStock(Ingredient ingredient){
        this.ingredientStock.remove(ingredient);
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean dishStockChecker(){
        for (Map.Entry<Dish, Number> x : this.dishStock.entrySet()){
            if ((int)x.getValue() < (int)x.getKey().getRestockThreshold()){

            }
        }

        return true;
    }

    public boolean ingredientInStockCheck(Ingredient ingredient){
        for (Map.Entry<Ingredient, Number> x : this.ingredientStock.entrySet()){
            if (ingredient.getName().equals(x.getKey().getName())){
                if ((int)x.getValue() < (int)x.getKey().getRestockThreshold()){
                    return false;
                }

            }


        }

        return true;
    }

}

