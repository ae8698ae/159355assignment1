package assignment1;

import java.util.concurrent.Semaphore;

public class SummitStation implements Runnable {
    private int totalTourists = 0;
    final Semaphore touristsAtSummit;
    final Semaphore touristWaitingSummit;
    final Semaphore summitStationClosed;

    SummitStation(Semaphore touristWaitingSummit, Semaphore touristsAtSummit, Semaphore summitStationClosed){
        this.touristWaitingSummit = touristWaitingSummit;
        this.touristsAtSummit = touristsAtSummit;
        this.summitStationClosed = summitStationClosed;
    }

    @Override
    public void run() {
        while (totalTourists < 500 || touristWaitingSummit.availablePermits() != 0 || touristsAtSummit.availablePermits() != 50){
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e1) { }
            if (touristsAtSummit.availablePermits() != 50){
                int touristsWantingToLeave = (int) (Math.random() *
                        (((50 - touristsAtSummit.availablePermits()) - 1) + 1) + 1);
//                touristsAtSummit.release(touristsWantingToLeave);

                touristWaitingSummit.release(touristsWantingToLeave);
                totalTourists = totalTourists + touristsWantingToLeave;
                System.out.println(touristsWantingToLeave + " tourist(s) decide to leave the mountain and goes to " +
                        "the summit station");
            }
        }
        summitStationClosed.release();
        System.out.println("The summit station has the last tourists waiting and will close once then have left.");
    }
}
