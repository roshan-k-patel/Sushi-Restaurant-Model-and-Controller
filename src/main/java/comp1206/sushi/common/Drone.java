package comp1206.sushi.common;

import comp1206.sushi.common.Drone;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

public class Drone extends Model implements Serializable, Runnable {

	private Number speed;
	private Number progress;
	
	private Number capacity;
	private Number battery;
	
	private String status;
	
	private Postcode source;
	private Postcode destination;

	static Stock stock;

	private Thread t;
	private Battery meter;

	private int numberOfDrones = 0;

	public Drone(Number speed, Stock stock) {

        t = new Thread(this);


		this.setSpeed(speed);
		this.setCapacity(100);
		this.setBattery(100);
		this.setStatus("Idle");
		this.numberOfDrones++;


		this.meter = new Battery();

        this.stock = stock;

        t.start();
	}

	public Number getSpeed() {
		return speed;
	}

	
	public Number getProgress() {
		return progress;
	}
	
	public void setProgress(Number progress) {
		this.progress = progress;
	}
	
	public void setSpeed(Number speed) {
		this.speed = speed;
	}
	
	@Override
	public String getName() {
		return "Drone (" + getSpeed() + " speed)";
	}

	public Postcode getSource() {
		return source;
	}

	public void setSource(Postcode source) {
		this.source = source;
	}

	public Postcode getDestination() {
		return destination;
	}

	public void setDestination(Postcode destination) {
		this.destination = destination;
	}

	public Number getCapacity() {
		return capacity;
	}

	public void setCapacity(Number capacity) {
		this.capacity = capacity;
	}

	public Number getBattery() {
	   Number battery = meter.getBatteryPercentage();
		return battery;
	}

	public void setBattery(Number battery) {
		this.battery = battery;
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
		System.out.println("Drone " + this.getName() + " is running as it's own Thread and making sure our ingredients stay in stock!");

       System.out.println("The battery percentage is of " + this.getName() + " is " + this.meter.getBatteryPercentage() + " and status is " + this.getStatus());


           monitorIngredientStockLevels();


    }



	public synchronized void monitorIngredientStockLevels() {
		for (Map.Entry<Ingredient, Number> x : stock.getIngredientStock().entrySet()) {

            Number currentStock;

            if (stock.getIngredientStock().get(x) == null) {
                currentStock = 0;
            } else {
                currentStock = stock.getIngredientStock().get(x);
            }

			if (((int) x.getValue()) < ((int) x.getKey().getRestockThreshold())) {


				System.out.println();
				System.out.println("current stock of " + x.getKey().getName() +  " is: " + currentStock);
				System.out.println("restock amount is " + x.getKey().getRestockAmount());
				Number updatedStock = (int) currentStock + (int) x.getKey().getRestockAmount();

				System.out.println("Fetching ingredients from supplier: " + x.getKey().getSupplier());

				int timeTaken = (30 / (int)this.speed);


				try {
				    this.setStatus("Delivering");
                    System.out.println("A Drone has just left for delivery");
                    this.notifyUpdate();
                    Thread.sleep( 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

                System.out.println();
                System.out.println("A Drone has returned from delivery");
				this.setStatus("Idle");

                stock.setIngredientStock(x.getKey(),updatedStock);
                this.meter.lowerBattery();
				System.out.println("Drone has delivered ingredient stock, battery remaining is " + this.meter.getBatteryPercentage() + "%" );
                this.notifyUpdate();


			}

            if (this.meter.getBatteryPercentage()<50){
                this.setStatus("Charging");
                System.out.println(this.getName() + " is charging for 10 seconds");
                try {
                    Thread.sleep(1000);
                    this.meter.batteryPercentage=100;
                    this.setStatus("Idle");
                    this.notifyUpdate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

		}

	}

    public boolean halfChance(){
        Random r = new Random();
        int k = r.nextInt(10);

        if (k > 5){
           return true;
        }

        return false;
    }

}



