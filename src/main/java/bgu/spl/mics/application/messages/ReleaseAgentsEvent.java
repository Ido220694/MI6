package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<Boolean> {
    List<String> Serials;

    public ReleaseAgentsEvent(List<String> lst){
    Serials = lst;
    }
    public List<String> getAgentsList(){
        return Serials;
    }

}
