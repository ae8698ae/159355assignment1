package assignment1;

import javax.swing.text.StyleContext;
import java.util.concurrent.Semaphore;

public class Runner {

    public static void main(String[] args){
        Semaphore touristWaitingBase = new Semaphore(500);
        Semaphore touristWaitingSummit = new Semaphore(50);
        Semaphore tramSeats = new Semaphore(10);
        Semaphore touristsAtSummit = new Semaphore(50);
        Semaphore summitTickets = new Semaphore(50);

        touristWaitingBase.drainPermits();
        touristWaitingSummit.drainPermits();

        BaseStation base = new BaseStation(touristWaitingBase);
        Thread baseThread = new Thread(base);
        baseThread.start();

        SummitStation summit = new SummitStation(touristWaitingSummit, touristsAtSummit, summitTickets);
        Thread summitThread = new Thread(summit);
        summitThread.start();

        Tram tram = new Tram(tramSeats, touristWaitingBase, touristWaitingSummit, touristsAtSummit, summitTickets);
        Thread tramThread = new Thread(tram);
        tramThread.start();
    }
}
