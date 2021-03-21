package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.MyPair;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private static Squad instance;
    private List<String> serials;
    private int uniqueNumber;

    public Moneypenny(int number) {
        super("MoneyPenny" + number);
        instance = Squad.getInstance();
        uniqueNumber = number;

    }

    @Override
    protected void initialize() {
        Callback<AgentAvailableEvent> callbackName = new Callback<AgentAvailableEvent>() {
            @Override
            public void call(AgentAvailableEvent c) {
                serials = c.getSerialsSquad();

                boolean agentsGood = instance.getAgents(serials);
                int id = agentsGood ? uniqueNumber : -1;
                MyPair<Integer,List<String>> result = new MyPair<>(id,instance.getAgentsNames(serials));
                complete(c, result);

                if(c.shouldExecute().get()) {
                    instance.sendAgents(serials, c.getTimeOfMission());
                }
                else if(agentsGood){
                    instance.releaseAgents(serials);
                }
            }
        };
        this.subscribeEvent(AgentAvailableEvent.class, callbackName);

        subscribeEvent(ReleaseAgentsEvent.class, c -> {
            List<String> toRelease = c.getAgentsList();
            instance.releaseAgents(toRelease);
            complete(c, true);
        });


        subscribeBroadcast(TickBroadcastTerminate.class, c -> {
            terminate();
        });
        subscribeBroadcast(TickBroadcastTerminate.class, c -> terminate());

    }
}
