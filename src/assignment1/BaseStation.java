package assignment1;

import java.util.concurrent.Semaphore;

public class BaseStation implements Runnable {
    private int totalTourists = 0;
    Semaphore touristWaitingBase;

    BaseStation(Semaphore touristWaitingBase){
        this.touristWaitingBase = touristWaitingBase;
    }

    @Override
    public void run() {
        while (totalTourists < 500) {

            try {
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e1) { }
            if (touristWaitingBase.availablePermits() != 500){
                touristWaitingBase.release();
                System.out.println("A tourist arrived at the base station of the cable car");
                totalTourists ++;
            }
        }
    }
}
