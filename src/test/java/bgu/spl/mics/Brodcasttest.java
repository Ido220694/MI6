package bgu.spl.mics;

public class Brodcasttest implements Broadcast {
    private String brodcast;

    public Brodcasttest (String brodcast){
        this.brodcast = brodcast;
    }

    public String getBrodcast(){
        return brodcast;
    }
}

