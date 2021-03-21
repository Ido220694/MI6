package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MissionInfo> listOfMissions;

    public Intelligence(List<MissionInfo> lst, int number) {
        super("Intelligence" + number+1);
        this.listOfMissions = lst;
        lst.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, c -> {
            int timeIssued = c.getTick();

            while (listOfMissions.size() > 0 && listOfMissions.get(0).getTimeIssued() == timeIssued){
                MissionReceivedEvent newMission = new MissionReceivedEvent(listOfMissions.get(0));
                getSimplePublisher().sendEvent(newMission);
                listOfMissions.remove(0);
            }
        });

        subscribeBroadcast(TickBroadcastTerminate.class, c -> {
            terminate();
        });
        subscribeBroadcast(TickBroadcastTerminate.class, c -> terminate());

    }
}
