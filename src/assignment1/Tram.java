package assignment1;

import java.util.concurrent.Semaphore;

public class Tram implements Runnable {
    Semaphore touristWaitingBase, touristWaitingSummit, touristsAtSummit, tramSeats, summitTickets, baseStationComplete,
    summitStationComplete;

    Tram(Semaphore tramSeats, Semaphore touristWaitingBase, Semaphore touristWaitingSummit, Semaphore touristsAtSummit,
         Semaphore summitTickets, Semaphore baseStationComplete, Semaphore summitStationComplete) {
        this.touristWaitingBase = touristWaitingBase;
        this.touristsAtSummit = touristsAtSummit;
        this.touristWaitingSummit = touristWaitingSummit;
        this.tramSeats = tramSeats;
        this.summitTickets = summitTickets;
        this.baseStationComplete = baseStationComplete;
        this.summitStationComplete = summitStationComplete;
    }

    @Override
    public void run() {
        while(baseStationComplete.availablePermits() == 0 & summitStationComplete.availablePermits() == 0) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }
            System.out.println("The tram arrives at the base station");
            tramSeats.drainPermits();

            for (int i = 0; i < 10; i++) {
                if (touristWaitingBase.tryAcquire()){
                    if(summitTickets.tryAcquire()){
                        tramSeats.release();
                        System.out.println("A tourist sits down in the tram waiting to go up.");
                    }else{
                        touristWaitingBase.release();
                    }
                }
//                Boolean spaceOnSummit = summitTickets.tryAcquire();
//                if (spaceOnSummit) {
//                    if (touristWaitingBase.tryAcquire()){
//                        System.out.println(i + " " + summitTickets.availablePermits());
//                        tramSeats.release();
//                    }else{
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {}
//                    }
//                } else {
//                    summitTickets.release();
//                }
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
                    System.out.println("A tourist sits down in the tram waiting to go down.");
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){}
                }
            }

            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "foot of the mountain");
        }
        System.out.println("There are " + touristWaitingBase.availablePermits() + " available touristWaitingBase permits");
        System.out.println("There are " + touristWaitingSummit.availablePermits() + " available touristWaitingSummit permits");
        System.out.println("There are " + touristsAtSummit.availablePermits() + " available touristsAtSummit permits");
        System.out.println("There are " + tramSeats.availablePermits() + " available tramSeats permits");
        System.out.println("There are " + summitTickets.availablePermits() + " available summitTickets permits");
        System.out.println("There are " + baseStationComplete.availablePermits() + " available baseStationComplete permits");
        System.out.println("There are " + summitStationComplete.availablePermits() + " available summitStationComplete permits");
    }
}
