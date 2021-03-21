package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;


public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    private int time;

    public GadgetAvailableEvent(String gadget) {
        this.gadget=gadget;

    }

    public String getGadget() {
        return gadget;
    }

    public int getGadgaetTime(){
        return time;
    }
}



