package comp1206.sushi.common;

import comp1206.sushi.common.Staff;
import comp1206.sushi.server.Server;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.Random;


public class Staff extends Model implements Runnable, Serializable {

	private String name;
	private String status;
	private Number fatigue;
	static Stock stock;
	private Thread t1;
	private Server server;

	public Staff(String name, Stock stock) {


		t1 = new Thread(this);

		this.setName(name);
		this.setFatigue(0);
		this.setStatus("Idle");
		this.stock = stock;

		t1.start();


	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getFatigue() {
		return fatigue;
	}

	public void setFatigue(Number fatigue) {
		this.fatigue = fatigue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		notifyUpdate("status",this.status,status);
		this.status = status;
	}


	@Override
	public void run() {




		System.out.println();
		System.out.println(" A thread for start " + this.getName() + " has been created and run");




		//HAVE TO MAKE SURE THAT IT CHECKS THERE ARE INGREDIENTS IN THE FIRST PLACE
		try {
			if (this.getStatus().equals("Idle") && (int) this.getFatigue() < 100) {
				System.out.println();
				System.out.println(this.getName() + " is monitoring the dish stock");
				while ((int) this.getFatigue() < 100) {
					monitorDishStockLevels();
					//sleeps for a random amount of time when something has been updated

					Number newFatigue = (int) getFatigue() + 10;

					this.setFatigue(newFatigue);
					this.notifyUpdate();

					Random rand = new Random();
					int sleepTime = rand.nextInt(40) + 20;

					try {
						Thread.sleep(sleepTime * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


					//check to make sure staff aren't fatigued
					if ((int) this.getFatigue() >= 99) {
						System.out.println(this.getName() + " is fatigued, we are giving them a generous 20 second break!");
						this.setStatus("Resting");
						System.out.println();
						try {
							Thread.sleep(20000);
							this.setFatigue(50);
							this.setStatus("Idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}

			}

		} catch (NullPointerException e){
			t1.start();
		}

	}

	public synchronized boolean checkIngredients(){
		boolean decision = true;
		for (Map.Entry<Ingredient,Number> x : stock.ingredientStock.entrySet()){
			int i = (int)x.getValue();
			int k = (int)x.getKey().getRestockThreshold();
			if (i < k){
				decision = false;
			} else {
				decision = true;
			}
		}
		return true;
	}

	//checks to make sure all dishes are stocked and restocks them if there are ingredients
	public synchronized void monitorDishStockLevels(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		for (Map.Entry<Dish,Number> x : stock.dishStock.entrySet()){

			if ((int)x.getValue()<(int)x.getKey().getRestockThreshold()){

				Number currentStock;

				if (stock.getDishStock().get(x) == null){
					currentStock = 0;
				} else {
					currentStock = stock.getDishStock().get(x);
				}


				if (!checkIngredients()){
					try {
						System.out.println("Waiting for drones to deliver ingredients");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {

					System.out.println();
					System.out.println("current stock of " + x.getKey().getName() +  " is: " + currentStock);
					System.out.println("restock amount is " + x.getKey().getRestockAmount());
					Number updatedStock = (int) currentStock + (int) x.getKey().getRestockAmount();
					System.out.println(this.getName() + " is checking if he/she should restock dish " + x.getKey().getName() + " by " + x.getKey().getRestockAmount());
					x.setValue(updatedStock);
					System.out.println("stock replenished");
					this.notifyUpdate();


				}

			}
		}
	}
}
