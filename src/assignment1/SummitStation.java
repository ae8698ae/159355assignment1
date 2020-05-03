package assignment1;

import java.util.concurrent.Semaphore;

public class SummitStation implements Runnable {
    private int totalTourists = 0;
    Semaphore touristsAtSummit, touristWaitingSummit, summitTickets, summitStationComplete;

    SummitStation(Semaphore touristWaitingSummit, Semaphore touristsAtSummit, Semaphore summitTickets,
                  Semaphore summitStationComplete){
        this.touristWaitingSummit = touristWaitingSummit;
        this.touristsAtSummit = touristsAtSummit;
        this.summitTickets = summitTickets;
        this.summitStationComplete = summitStationComplete;
    }

    @Override
    public void run() {
        while (totalTourists < 500 & touristsAtSummit.availablePermits() != 0){
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e1) { }
            if (touristsAtSummit.availablePermits() != 50){
                touristsAtSummit.release();
                touristWaitingSummit.release();
                totalTourists ++;
                System.out.println("A tourist decides to leave the mountain and goes to the summit station");
            }
        }
        summitStationComplete.release();
        System.out.println("Summit station total tourists = " + totalTourists);
    }
}
