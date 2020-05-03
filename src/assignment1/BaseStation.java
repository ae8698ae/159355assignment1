package assignment1;

import java.util.concurrent.Semaphore;

public class BaseStation implements Runnable {
    private int totalTourists = 0;
    Semaphore touristWaitingBase, baseStationComplete;

    BaseStation(Semaphore touristWaitingBase, Semaphore baseStationComplete){
        this.touristWaitingBase = touristWaitingBase;
        this.baseStationComplete = baseStationComplete;
    }

    @Override
    public void run() {
        while (totalTourists < 500 & touristWaitingBase.availablePermits() != 500) {

            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e1) { }
            if (touristWaitingBase.availablePermits() != 500){
                int touristsArriving = (int) (Math.random() * 9) + 1;
                touristWaitingBase.release(touristsArriving);
                System.out.println(touristsArriving + " tourist(s) arrived at the base station of the cable car");
                totalTourists = totalTourists + touristsArriving;
            }
        }
        baseStationComplete.release();
        System.out.println("Base station total tourists = " + totalTourists);
    }
}
