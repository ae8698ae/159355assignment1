package assignment1;

import java.util.concurrent.Semaphore;

public class Tram implements Runnable {

    //Initiate some variables
    Semaphore touristWaitingBase, touristWaitingSummit, touristsAtSummit, tramSeats, baseStationClosed,
            summitStationClosed, tramLoading;

    Tram(Semaphore tramSeats, Semaphore touristWaitingBase, Semaphore touristWaitingSummit, Semaphore touristsAtSummit,
          Semaphore baseStationClosed, Semaphore summitStationClosed, Semaphore tramLoading) {
        this.touristWaitingBase = touristWaitingBase;
        this.touristsAtSummit = touristsAtSummit;
        this.touristWaitingSummit = touristWaitingSummit;
        this.tramSeats = tramSeats;
        this.baseStationClosed = baseStationClosed;
        this.summitStationClosed = summitStationClosed;
        this.tramLoading = tramLoading;
    }

    @Override
    public void run() {

        // Run loop until both stations are closed.
        while(baseStationClosed.availablePermits() == 0 || summitStationClosed.availablePermits() == 0) {

            // Sleep for 200 milli seconds to replicate the tram traveling
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
            System.out.println("The tram arrives at the base station");

            // Get all the tram seat permits so they can be released when acquiring passengers.
            tramSeats.drainPermits();

            // Loop through 10 times trying to fill the seats in the tram
            for (int i = 0; i < 10; i++) {

                // If there are tourists waiting at the base acquire one. If not able to acquire one wait 100ms to give
                // a chance for a tourist to arrive.
                if (touristWaitingBase.tryAcquire()){

                    //try and get a space at the summit
                    if(touristsAtSummit.tryAcquire()){

                        // If there is space at the summit then give a seat on the tram
                        tramSeats.release();

                    // If there is no space at the summit then put tourist back in the queue.
                    }else{
                        touristWaitingBase.release();
                    }

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                }
            }
            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "summit");

            // Have the thread sleep to replicate time the tram is travelling.
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }

            System.out.println("The tram arrives at the summit station");

            //Drain the tram seat permits so the tram can pick up more tourists.
            tramSeats.drainPermits();

            // try to get the tram loading semaphore and wait until it is available.
            tramLoading.acquireUninterruptibly();

            // Loop 10 times trying to fill the tram seats. If it is unable to get a tourist then wait before trying
            // again.
            for (int i = 0; i < 10; i++) {
                if (touristWaitingSummit.tryAcquire()){
                    tramSeats.release();
                    touristsAtSummit.release();

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){}
                }
            }

            // Release the tram loading semaphore so the summit can generate more tourists at the station.
            tramLoading.release();

            System.out.println("The cable car leaves with " + tramSeats.availablePermits() + " passenger(s) to the " +
                    "foot of the mountain");
        }
        System.out.println("The tram drops off the last passenger(s) and finishes for the day.");
        System.out.println("touristWaitingBase " + touristWaitingBase.availablePermits());
        System.out.println("touristWaitingSummit " + touristWaitingSummit.availablePermits());
        System.out.println("touristsAtSummit " + touristsAtSummit.availablePermits());
        System.out.println("tramSeats " + tramSeats.availablePermits());
        System.out.println("baseStationClosed " + baseStationClosed.availablePermits());
        System.out.println("summitStationClosed " + summitStationClosed.availablePermits());
        System.out.println("tramLoading " + tramLoading.availablePermits());
    }
}
