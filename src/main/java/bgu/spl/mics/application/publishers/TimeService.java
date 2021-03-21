package bgu.spl.mics.application.publishers;

import bgu.spl.mics.*;

import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;



/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int duration;
    private int currentTime;
    boolean terminate;
    boolean getReady;

    public TimeService(int duration) {
        super("timeService " + duration);
        this.duration = duration;
        this.currentTime = 0;
        terminate = false;
        getReady = false;

    }

    @Override
    protected void initialize() {
        synchronized (this) {
            getReady = true;
            notifyAll();
        }
    }

    @Override
    public void run() {
        initialize();
        try {

            while (duration > currentTime) {
                TickBroadcast newtime = new TickBroadcast(currentTime++);
                getSimplePublisher().sendBroadcast(newtime);
                Thread.sleep(100);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TickBroadcastTerminate close = new TickBroadcastTerminate();
        getSimplePublisher().sendBroadcast(close);

    }

    public boolean getReady() {
        return getReady;
    }


}
