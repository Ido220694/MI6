package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private final int tick;

    public TickBroadcast(int tick) {
        super();
        this.tick=tick;
    }

    public int getTick() {
        return tick;
    }
}
