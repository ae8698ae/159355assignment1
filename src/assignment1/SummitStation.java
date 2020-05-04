package assignment1;

import java.util.concurrent.Semaphore;

public class SummitStation implements Runnable {

    //Initiate some variables
    private int totalTourists = 0;
    Semaphore touristsAtSummit, touristWaitingSummit, summitStationClosed, tramLoading, baseStationClosed;

    SummitStation(Semaphore touristWaitingSummit, Semaphore touristsAtSummit, Semaphore summitStationClosed,
                  Semaphore tramLoading, Semaphore baseStationClosed){
        this.touristWaitingSummit = touristWaitingSummit;
        this.touristsAtSummit = touristsAtSummit;
        this.summitStationClosed = summitStationClosed;
        this.tramLoading = tramLoading;
        this.baseStationClosed = baseStationClosed;
    }

    @Override
    public void run() {

        // Loop through until 500 tourists have gone through the station and there are no tourists still at the summit
        // also it checks that the base station isn't closed indicating that there are no tourists waiting at the
        // bottom.
        while (totalTourists < 500 || touristWaitingSummit.availablePermits() != 0 ||
                touristsAtSummit.availablePermits() != 50 || baseStationClosed.availablePermits() == 0){

            // Sleep for a random amount of time so not all the tourists leave at once.
            try {
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e1) { }

            // If the tram is not loading tourists then proceed else do nothing. I added this lock as there were errors
            // with the calculations for the tourists wanting to leave as sometimes the tram would take tourists
            // resulting in a negative semaphore count i.e. there were more tourists waiting than were on the summit.
            // This wasn't so much of an issue if the semaphores were acquired one by one.
            if(tramLoading.tryAcquire()) {

                // Check that there are tourists on the summit and they aren't already waiting for the tram.
                if (touristsAtSummit.availablePermits() != 50 && ((50 - touristsAtSummit.availablePermits()) -
                        touristWaitingSummit.availablePermits()) != 0) {

                    // Generate a random number of tourists wanting to leave
                    int touristsWantingToLeave = (int) (Math.random() * (((((50 - touristsAtSummit.availablePermits()) -
                            touristWaitingSummit.availablePermits()) / 2) - 1) + 1) + 1);

                    // Release the tourists from the tourist waiting semaphore so the tram can collect them
                    touristWaitingSummit.release(touristsWantingToLeave);

                    // Increase the number of tourists that have transferred through the station.
                    totalTourists = totalTourists + touristsWantingToLeave;
                    System.out.println(touristsWantingToLeave + " tourist(s) decide to leave the mountain and go to " +
                            "the summit station");
                }
                // Release the tram loading semaphore so the tram can load the tourists.
                tramLoading.release();
            }
        }

        // Once 500 tourists have been entered the summit station and there are none waiting to leave then release
        // the summitStationClosed semaphore indicating that the station is closed.
        summitStationClosed.release();
        System.out.println("The summit station has the last tourists waiting and will close once then have left.");

    }
}
