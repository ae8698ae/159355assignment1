package assignment1;

import java.util.concurrent.Semaphore;

public class Runner {

    public static void main(String[] args){
        Semaphore touristWaitingBase = new Semaphore(500);
        Semaphore touristWaitingSummit = new Semaphore(50);
        Semaphore tramSeats = new Semaphore(10);
        Semaphore touristsAtSummit = new Semaphore(50);
        Semaphore summitTickets = new Semaphore(50);
        Semaphore baseStationClosed = new Semaphore(1);
        Semaphore summitStationClosed = new Semaphore(1);

        touristWaitingBase.drainPermits();
        touristWaitingSummit.drainPermits();
        summitStationClosed.tryAcquire();
        baseStationClosed.tryAcquire();

        BaseStation base = new BaseStation(touristWaitingBase, baseStationClosed);
        Thread baseThread = new Thread(base);
        baseThread.start();

        SummitStation summit = new SummitStation(touristWaitingSummit, touristsAtSummit, summitStationClosed);
        Thread summitThread = new Thread(summit);
        summitThread.start();

        Tram tram = new Tram(tramSeats, touristWaitingBase, touristWaitingSummit, touristsAtSummit, summitTickets,
                baseStationClosed, summitStationClosed);
        Thread tramThread = new Thread(tram);
        tramThread.start();
    }
}
