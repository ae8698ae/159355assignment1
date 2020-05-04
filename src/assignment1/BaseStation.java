package assignment1;

import java.util.concurrent.Semaphore;

public class BaseStation implements Runnable {

    // Initiate some variables
    private int totalTourists = 0;
    final Semaphore touristWaitingBase, baseStationClosed;

    BaseStation(Semaphore touristWaitingBase, Semaphore baseStationClosed){
        this.touristWaitingBase = touristWaitingBase;
        this.baseStationClosed = baseStationClosed;
    }

    @Override
    public void run() {

        //Continue loop until 500 tourists have been generated and there are no tourists waiting for the tram.
        while (totalTourists < 500 || touristWaitingBase.availablePermits() != 0) {

            //Sleep for random amount of time so not all tourists are generated at once.
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e1) { }

            // Check that there are not more than 500 tourists waiting
//            if (touristWaitingBase.availablePermits() != 500){

            // Generate a random number of tourists which is max half of remaining total tourists. I had them coming
            // one by one however it was taking too lok=ng for the program to finish. Once the random number is
            // generated then the same amount of semaphores is released so the tram can acquire them.
                int touristsArriving = (int) (Math.random() * ((((500 - totalTourists) / 2) - 1) + 1) + 1);
                touristWaitingBase.release(touristsArriving);
                System.out.println(touristsArriving + " tourist(s) arrived at the base station of the cable car");

                //Increment the total tourists processed by the base station by the number of tourists that have arrived.
                totalTourists = totalTourists + touristsArriving;
//            }
        }
        // Once 500 tourists have been gereated then release the baseStationClosed semaphore indicating that the station
        // has finished generating tourists.
        baseStationClosed.release();
        System.out.println("The base station is now closed.");
    }
}
