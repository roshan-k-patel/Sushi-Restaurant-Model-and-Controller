package comp1206.sushi.server;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Restaurant;
import comp1206.sushi.common.User;

import java.net.*;
import java.io.*;

    public class Comms extends Thread {
        private ServerSocket serverSocket;
        ObjectOutputStream streamOut;
        ObjectInputStream streamIn;
        Server initialServer;

        public Comms(Server server1) throws IOException {
            this.initialServer = server1;
            serverSocket = new ServerSocket(5000);
            this.start();
        }

        public void run() {
            while (true) {
                try {
                    System.out.println("Waiting for a customer to connect on port " + serverSocket.getLocalPort());

                    Socket server = serverSocket.accept();

                    streamOut = new ObjectOutputStream(server.getOutputStream());

                    streamIn = new ObjectInputStream(server.getInputStream());

                    System.out.println("The Server has successfully connected to a customer at the address:  " + server.getRemoteSocketAddress());


                    System.out.println(initialServer.getRestaurant());

                    Restaurant tempRestaurant = initialServer.getRestaurant();
                    streamOut.writeObject(tempRestaurant);

                    for(Dish x : initialServer.getDishes()){
                        streamOut.writeObject(x);
                    }

                    for(Postcode x : initialServer.getPostcodes()){
                        streamOut.writeObject(x);
                    }
                    for(User x : initialServer.getUsers()){
                        streamOut.writeObject(x);
                    }


                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }

        public void sendMessage (Restaurant restaurant){
 //          try {
//                streamOut.writeObject(new Restaurant(restaurant.getName(),restaurant.getLocation()));
  //              System.out.println("Restaurant " + restaurant.getName() +  " sent");
    //        } catch (IOException e) {
//            }
        }

        public void sendMessage(Dish dish){
            try {
                streamOut.writeObject(dish);
                System.out.println("Dish " + dish.getName() + " sent");
            } catch (IOException e){}

        }

        public void sendMessage(Postcode postcode) {
            try {
                streamOut.writeObject(postcode);
                System.out.println("Postcode " + postcode.getName() + "sent");
            } catch (IOException e) {
            }

        }





    }