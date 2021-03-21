package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MyPair;

import java.util.List;


public class AgentAvailableEvent implements Event<MyPair<Integer, List<String>>> {

    private List<String> serials;
    private Future<Boolean> executeMission;
    private int timeOfMission;

    public AgentAvailableEvent(List<String> serials, Future<Boolean> future, int timeOfMission) {
        this.serials = serials;
        executeMission = future;
        this.timeOfMission = timeOfMission;
    }

    public List<String> getSerialsSquad() {
        return serials;
    }

    public Future<Boolean> shouldExecute() {
        return executeMission;
    }

    public int getTimeOfMission() {
        return timeOfMission;
    }
}




