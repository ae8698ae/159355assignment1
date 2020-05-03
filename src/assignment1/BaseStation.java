package assignment1;

import java.util.concurrent.Semaphore;

public class BaseStation implements Runnable {
    private int totalTourists = 0;
    final Semaphore touristWaitingBase;
    final Semaphore baseStationClosed;

    BaseStation(Semaphore touristWaitingBase, Semaphore baseStationClosed){
        this.touristWaitingBase = touristWaitingBase;
        this.baseStationClosed = baseStationClosed;
    }

    @Override
    public void run() {
        while (totalTourists < 500) {

            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e1) { }
            if (touristWaitingBase.availablePermits() != 500){
                int touristsArriving = (int) (Math.random() * ((((500 - totalTourists) / 2) - 1) + 1) + 1);
                touristWaitingBase.release(touristsArriving);
                System.out.println(touristsArriving + " tourist(s) arrived at the base station of the cable car");
                totalTourists = totalTourists + touristsArriving;
            }
        }
        baseStationClosed.release();
        System.out.println("The base station has the last tourists waiting and will close once then have left.");
    }
}
