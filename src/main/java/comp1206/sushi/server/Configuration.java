//TO DO - Check that Orders and Users is working properly
// Proof read method logic

package comp1206.sushi.server;


import comp1206.sushi.common.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Configuration {
      public HashMap<Ingredient,Number> ingredientStock = new HashMap<>();
      public HashMap<Dish,Number> dishStock = new HashMap<>();
      private ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
        BufferedReader bufferedReader;
        public ArrayList<Postcode> postcodes;
        public ArrayList<Staff> staff;
        public ArrayList<Drone> drones;
        public ArrayList<Dish> dishes;
        public Restaurant restaurant;
        public ArrayList<Order> orders;
        public ArrayList<Ingredient> ingredients;
        public ArrayList<Supplier> suppliers;
        public ArrayList<User> users;


       public Configuration(String textFile){

         // creates all the new lists and makes sure that the file is readable
          try {
             bufferedReader = new BufferedReader(new FileReader(textFile));
             System.out.println("The config is readable");

                users = new ArrayList<User>();
                dishes = new ArrayList<Dish>();
                staff = new ArrayList<Staff>();
               ingredients = new ArrayList<Ingredient>();
              drones = new ArrayList<Drone>();
               orders = new ArrayList<Order>();
            postcodes = new ArrayList<Postcode>();
              suppliers = new ArrayList<Supplier>();


        }

        catch (FileNotFoundException e) {
            System.out.println("Cannot locate file");
            e.printStackTrace();
        }


    }


          protected void readStock(String configFile) {

        String lineBeingRead;

             try {

                    bufferedReader = new BufferedReader(new FileReader(configFile));

             while ((lineBeingRead = bufferedReader.readLine()) != null) {

                  String[] sections = lineBeingRead.split(":");
                if (sections[0].equals("STOCK") | sections[0].equals("stock")) {



                       for (Dish x : this.dishes){
                                if(x.getName().equals(sections[1])){
                            dishStock.replace(x,Integer.parseInt(sections[2]));
                        }
                     }



                        for (Ingredient x : this.ingredients){
                           if (x.getName().equals(sections[1])){
                            ingredientStock.replace(x,Integer.parseInt(sections[2]));
                           }
                     }

                  }

                    }
           } catch (Exception e) {

           }

     }


           protected void readRestaurants(String configFile) {
           String lineBeingRead;
               boolean restaurantLoaded;

               try {
                  bufferedReader = new BufferedReader(new FileReader(configFile));


                  while ((lineBeingRead = bufferedReader.readLine()) != null) {

                    String[] sections = lineBeingRead.split(":");
                       if (sections[0].equals("RESTAURANT")| sections[0].equals("restaurant")) {
                        this.restaurant = new Restaurant(sections[1],new Postcode (sections[2]));
                        restaurantLoaded = true;
                      }
                  }
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
            }


    }

         protected void readSuppliers(String configFile) {
             String lineBeingRead;
             try {
                 bufferedReader = new BufferedReader(new FileReader(configFile));

                while ((lineBeingRead = bufferedReader.readLine()) != null) {


                  String[] sections = lineBeingRead.split(":");
                       if (sections[0].equals("SUPPLIER") | sections[0].equals("supplier")) {


                         for (Postcode code : this.postcodes) {
                              if (code.getName().equals(sections[2])) {

                             Supplier s = new Supplier(sections[1],code);
                                 System.out.println();
                             suppliers.add(s);
                        }
                        else {}
                    }
                }
            }
        }

        catch (Exception e ) {

        }
    }
         protected void readIngredients(String configFile) {
            String lineBeingRead;
             try {
                bufferedReader = new BufferedReader(new FileReader(configFile));


                  while ((lineBeingRead = bufferedReader.readLine()) != null) {

                 String[] sections = lineBeingRead.split(":");

                      if (sections[0].equals("INGREDIENT")| sections[0].equals("ingredient")) {


                          for (Supplier s : this.suppliers) {


                               if (s.getName().equals(sections[3])) {

                                     String ingName = sections[1];

                                      String IngUnit = sections[2];
                                    Number threshold = Integer.parseInt(sections[4]);

                                  Number amount = Integer.parseInt(sections[5]);

                                  Number weight = Integer.parseInt(sections[6]);

                                 Ingredient i = new Ingredient(ingName,IngUnit,s,threshold,amount,weight);

                                ingredients.add(i);

                              ingredientStock.put(i,1);
                        }
                        }
                }
            }
             }
             catch (Exception e){

        }
    }


    protected void readDishes(String configFile) {
             String lineBeingRead;
              HashMap<Ingredient,Number> dishRecipe = new HashMap<>();
             try {
                bufferedReader = new BufferedReader(new FileReader(configFile));

                while ((lineBeingRead = bufferedReader.readLine()) != null) {

                 String sections[] = lineBeingRead.split(":");


                  if (sections[0].equals("DISH")| sections[0].equals("dish")) {
                      String dishName = sections[1];

                        String dishDescription = sections[2];

                        Number dishPrice = Integer.parseInt(sections[3]);

                      Number threshold = Integer.parseInt(sections[4]);

                       Number amount = Integer.parseInt(sections[5]);

                             Dish d = new Dish(dishName,dishDescription,dishPrice,threshold,amount);

                             String[] secondStringSet = sections[6].split("\\*|,");


                       for (int i = 0; i < (secondStringSet.length-1); i++) {
                           if (i%2 != 0) {
                               for (Ingredient x : ingredients) {
                                    if (x.getName().equals(secondStringSet[i])) {
                                        dishRecipe.put(x,Integer.parseInt(secondStringSet[i-1]));
                                }
                             }

                        }
                    }

                    d.setRecipe(dishRecipe);

                    dishes.add(d);

                    dishStock.put(d,1);



                  }
              }
            }
        catch (Exception e ) {
        }




    }


      protected void readOrders(String configFile) {
                String lineBeingRead;
                HashMap<Dish,Number> order = new HashMap<>();
                    try {
                       bufferedReader = new BufferedReader(new FileReader(configFile));

                       while ((lineBeingRead = bufferedReader.readLine()) != null) {

                       String sections[] = lineBeingRead.split(":");


                       if (sections[0].equals("ORDER") | sections[0].equals("order")) {
                             String username = sections[1];


                             String[] secondStringSet = sections[2].split("\\*|,");


                       for (int i = 0; i < secondStringSet.length-1; i++) {

                          if (i%2 != 0) {


                              for (Dish d : dishes) {

                                if (d.getName().equals(secondStringSet[i])) {

                                    order.put(d,Integer.parseInt(secondStringSet[i-1]));
                                }
                            }

                        }
                    }

                    Order test;

                     for(User u : this.users){

                        if (u.getName().equals(username)){

                               test = new Order(u);

                             test.setOrder(order);
                        }
                    }


                }
            }
        }
        catch (Exception e ) {
        }

    }

      protected void readUsers(String configFile) {
           String lineBeingRead;
          try {
              bufferedReader = new BufferedReader(new FileReader(configFile));

            while ((lineBeingRead = bufferedReader.readLine()) != null) {

                     String sections[] = lineBeingRead.split(":");
                   if (sections[0].equals("USER")| sections[0].equals("user")) {


                        for (Postcode p : postcodes) {
                            if (p.getName().equals(sections[4])) {

                               User u = new User(sections[1],sections[2],sections[3],p);

                                users.add(u);
                        }

                        else {}
                    }
                }
            }
        }
        catch (Exception e) {

        }

    }

    protected void readPostcodes(String configFile){

           String lineBeingRead;

         try {
               bufferedReader = new BufferedReader(new FileReader(configFile));


                 while ((lineBeingRead = bufferedReader.readLine()) != null) {
                   String[] sections = lineBeingRead.split(":");

                   if (sections[0].equals("POSTCODE")| sections[0].equals("postcode")) {

                            Postcode p = new Postcode(sections[1]);

                            postcodes.add(p);

                 }

            }


        }
        catch (Exception e){

        }

    }

    protected void readDrones(String configFile, Stock stock) {

        String lineBeingRead;


          try {
              bufferedReader = new BufferedReader(new FileReader(configFile));

               while ((lineBeingRead = bufferedReader.readLine()) != null) {
                  String[] sections = lineBeingRead.split(":");


                   if (sections[0].equals("DRONE")| sections[0].equals("drone")) {
                       Drone d = new Drone(Integer.parseInt(sections[1]),stock);
                       drones.add(d);
                   }


              }

        }
        catch (Exception e) {

        }

    }

         protected void readStaff(String configFile, Stock stock) {

            String lineBeingRead;

                try {

                   bufferedReader = new BufferedReader(new FileReader(configFile));

                   while ((lineBeingRead = bufferedReader.readLine()) != null) {

                    String[] sections = lineBeingRead.split(":");

                     if (sections[0].equals("STAFF")| sections[0].equals("staff")) {

                          Staff s = new Staff(sections[1],stock );
                           staff.add(s);

                }


            }

        }
        catch (Exception e) {
        }

    }



    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public HashMap<Ingredient,Number> getIngredientStock(){
        return this.ingredientStock;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public ArrayList<Drone> getDrones() {
        return drones;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }


    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public ArrayList<Staff> getStaff() {
        return staff;
    }

    public ArrayList<Postcode> getPostcodes() {
        return postcodes;
    }

    public Restaurant getRestaurants() {
        return restaurant;
    }

    public HashMap<Dish,Number> getDishStock(){
        return this.dishStock;
    }




}


