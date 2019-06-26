package comp1206.sushi.client;

import comp1206.sushi.common.*;

import java.net.*;
import java.io.*;

public class Comms extends Thread{

    ObjectOutputStream streamOut;
    ObjectInputStream streamIn;
    Client client;

    public Comms(Client client){
        this.client = client;
        this.start();
    }

    public void run(){

        String serverName = "localhost";
        int port = 5000;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            streamOut = new ObjectOutputStream(client.getOutputStream());
            streamIn = new ObjectInputStream(client.getInputStream());

        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        while (true){
            receieveMessage();
        }

    }

    public void receieveMessage() {
        try {
            Object o = streamIn.readObject();
            if (o instanceof Restaurant) {
        //        client.restaurantTemp.equals(o);
          //      client.restaurantTemp.setName(((Restaurant) o).getName());
            //    client.restaurantTemp.setLocation(((Restaurant) o).getLocation());

                client.notifyUpdate();
                System.out.println("Restaurant object has been set");

            } else if (o instanceof Postcode) {
                client.postcodes.add((Postcode) o);
                client.notifyUpdate();
                System.out.println("Postcode " + ((Postcode) o).getName() + " received");
            } else if (o instanceof User) {
                client.users.add((User) o);
                client.notifyUpdate();
                System.out.println("User " + ((User) o).getName() + " received");
            } else if (o instanceof Dish) {
                client.dishes.add((Dish) o);
                client.notifyUpdate();
                System.out.println("Dish " + ((Dish) o).getName() + " received");
            } else if (o instanceof Order) {
                client.orders.add((Order) o);
                client.notifyUpdate();
                System.out.println("Order " + ((Order) o).getName() + " received");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class not found exception");
        }
    }

}

