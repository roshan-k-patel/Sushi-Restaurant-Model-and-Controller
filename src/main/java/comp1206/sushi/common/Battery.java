package comp1206.sushi.common;

import java.util.Random;

public class Battery {

    int batteryPercentage;

    public Battery(){
        this.batteryPercentage = 100;
    }

    public void lowerBattery(){
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(15) + 5;
        this.batteryPercentage = this.batteryPercentage - randomInt;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }
}


