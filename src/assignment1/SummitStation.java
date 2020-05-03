package assignment1;

import java.util.concurrent.Semaphore;

public class SummitStation implements Runnable {
    private int totalTourists = 0;
    Semaphore touristsAtSummit, touristWaitingSummit, summitTickets;

    SummitStation(Semaphore touristWaitingSummit, Semaphore touristsAtSummit, Semaphore summitTickets){
        this.touristWaitingSummit = touristWaitingSummit;
        this.touristsAtSummit = touristsAtSummit;
        this.summitTickets = summitTickets;
    }

    @Override
    public void run() {
        while (totalTourists < 500){
            try {
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e1) { }
            if (touristsAtSummit.availablePermits() != 50){
                touristWaitingSummit.release();
                totalTourists ++;
                System.out.println("A tourist decides to leave the mountain and goes to the summit station");
            }
        }
    }
}
