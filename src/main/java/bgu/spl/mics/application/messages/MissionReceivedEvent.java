package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;


public class MissionReceivedEvent implements Event<Integer> {
    MissionInfo mi;
    public MissionReceivedEvent(MissionInfo info){
        mi=info;
    }
    public MissionInfo getInfo() {
        return mi;
    }
}
