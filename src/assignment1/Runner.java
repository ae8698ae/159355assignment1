package assignment1;

import java.util.concurrent.Semaphore;

public class Runner {

    public static void main(String[] args){
        //Create some semaphores
        Semaphore touristWaitingBase = new Semaphore(500);
        Semaphore touristWaitingSummit = new Semaphore(50);
        Semaphore tramSeats = new Semaphore(10);
        Semaphore touristsAtSummit = new Semaphore(50);
        Semaphore summitTickets = new Semaphore(50);
        Semaphore baseStationClosed = new Semaphore(1);
        Semaphore summitStationClosed = new Semaphore(1);

        //Aquire all the permits for tourists at base and summit
        touristWaitingBase.drainPermits();
        touristWaitingSummit.drainPermits();

        //Aquire permits for the station states indicating the stations are open
        summitStationClosed.tryAcquire();
        baseStationClosed.tryAcquire();

        //Create base station thread and start it
        BaseStation base = new BaseStation(touristWaitingBase, baseStationClosed);
        Thread baseThread = new Thread(base);
        baseThread.start();

        //Create summit station thread and start it
        SummitStation summit = new SummitStation(touristWaitingSummit, touristsAtSummit, summitStationClosed);
        Thread summitThread = new Thread(summit);
        summitThread.start();

        //Create tram thread and start it
        Tram tram = new Tram(tramSeats, touristWaitingBase, touristWaitingSummit, touristsAtSummit, summitTickets,
                baseStationClosed, summitStationClosed);
        Thread tramThread = new Thread(tram);
        tramThread.start();
    }
}
