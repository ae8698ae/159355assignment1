package assignment1;

import java.util.concurrent.Semaphore;

public class Tram implements Runnable {
    Semaphore touristWaitingBase, touristWaitingSummit, touristsAtSummit, tramSeats, summitTickets;

    Tram(Semaphore tramSeats, Semaphore touristWaitingBase, Semaphore touristWaitingSummit, Semaphore touristsAtSummit,
         Semaphore summitTickets) {
        this.touristWaitingBase = touristWaitingBase;
        this.touristsAtSummit = touristsAtSummit;
        this.touristWaitingSummit = touristWaitingSummit;
        this.tramSeats = tramSeats;
        this.summitTickets = summitTickets;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }

            tramSeats.drainPermits();

            for (int i = 0; i < 10; i++) {
                if (summitTickets.tryAcquire()) {
                    if (touristWaitingBase.tryAcquire()){
                        tramSeats.release();
                    }else{
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {}
                    }
                }
            }
            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "summit");


            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }

            touristsAtSummit.tryAcquire(tramSeats.availablePermits());
            tramSeats.drainPermits();

            for (int i = 0; i < 10; i++) {
                if (touristWaitingSummit.tryAcquire()){
                    tramSeats.release();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){}
                }
            }

            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "foot of the mountain");
        }
    }
}
