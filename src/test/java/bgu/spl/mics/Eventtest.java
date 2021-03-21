package bgu.spl.mics;


public class Eventtest implements Event<String> {

    private String event;

    public Eventtest (String event){
        this.event = event;
    }

    public String getEvent(){
        return event;
    }
}
