package assignment1;

import java.util.concurrent.Semaphore;

public class Tram implements Runnable {
    final Semaphore touristWaitingBase;
    final Semaphore touristWaitingSummit;
    final Semaphore touristsAtSummit;
    final Semaphore tramSeats;
    final Semaphore summitTickets;
    final Semaphore baseStationClosed;
    final Semaphore summitStationClosed;

    Tram(Semaphore tramSeats, Semaphore touristWaitingBase, Semaphore touristWaitingSummit, Semaphore touristsAtSummit,
         Semaphore summitTickets, Semaphore baseStationClosed, Semaphore summitStationClosed) {
        this.touristWaitingBase = touristWaitingBase;
        this.touristsAtSummit = touristsAtSummit;
        this.touristWaitingSummit = touristWaitingSummit;
        this.tramSeats = tramSeats;
        this.summitTickets = summitTickets;
        this.baseStationClosed = baseStationClosed;
        this.summitStationClosed = summitStationClosed;
    }

    @Override
    public void run() {
        while(touristWaitingBase.availablePermits() != 0 || touristsAtSummit.availablePermits() != 50 ||
                baseStationClosed.availablePermits() == 0 || summitStationClosed.availablePermits() ==0 ||
                touristWaitingSummit.availablePermits() != 0) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }
            System.out.println("The tram arrives at the base station");
            tramSeats.drainPermits();

            for (int i = 0; i < 10; i++) {
                if (touristWaitingBase.tryAcquire()){
                    if(summitTickets.tryAcquire()){
                        tramSeats.release();
//                        System.out.println("A tourist sits down in the tram waiting to go up.");
                    }else{
                        touristWaitingBase.release();
                    }
                }
            }
            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "summit");


            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }

            System.out.println("The tram arrives at the summit station");
            touristsAtSummit.tryAcquire(tramSeats.availablePermits());
            tramSeats.drainPermits();

            for (int i = 0; i < 10; i++) {
                if (touristWaitingSummit.tryAcquire()){
                    tramSeats.release();
                    summitTickets.release();
//                    System.out.println("A tourist sits down in the tram waiting to go down.");
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){}
                }
            }

            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "foot of the mountain");
        }
        System.out.println("The tram drops off the last passenger(s) and finishes for the day.");
    }
}
