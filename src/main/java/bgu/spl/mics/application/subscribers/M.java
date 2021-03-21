package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.MyPair;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private int UniqueNumber;
    private int currentTime;

    public M(int uniqueNumber) {
        super("M" + uniqueNumber);
        this.UniqueNumber = uniqueNumber;
    }


    @Override
    protected void initialize() {
        Callback<MissionReceivedEvent> callbackName = new Callback<MissionReceivedEvent>() {
            @Override
            public void call(MissionReceivedEvent c) {
                MissionInfo mission = c.getInfo();
                Diary.getInstance().incrementTotal();
                Future<Boolean> forMoneyPenny = new Future<>();

                List<String> serialNumbers = mission.getSerialAgentsNumbers();
                AgentAvailableEvent AgentsEvent = new AgentAvailableEvent(serialNumbers, forMoneyPenny,mission.getDuration());
                Future<MyPair<Integer,List<String>>> future1 = getSimplePublisher().sendEvent(AgentsEvent);

                if (future1 == null || future1.get() == null || future1.get().getFirst() == null || future1.get().getSecond() == null) {
                    complete(c, null);
                    terminate();
                    return;
                }
                if (future1.get().getFirst() != -1) {
                    int moneyPennyName = future1.get().getFirst();
                    List<String> agentsNames = future1.get().getSecond();

                    String newGadget = mission.getGadget();
                    GadgetAvailableEvent GadgetEvent = new GadgetAvailableEvent(newGadget);
                    Future<Integer> future2 = getSimplePublisher().sendEvent(GadgetEvent);
                    if (future2 == null || future2.get() == null) {
                        setThemFree(agentsNames);
                        forMoneyPenny.resolve(false);
                        complete(c, null);
                        terminate();
                        return;
                    }
                    int Qtime = future2.get();
                    if (Qtime >= 0) {
                        if (currentTime < mission.getTimeExpired()) {
                            forMoneyPenny.resolve(true);

                            //Report
                            if (agentsNames != null) {
                                Report report = new Report();
                                report.setAgentsNames(agentsNames);
                                report.setAgentsSerialNumbersNumber(serialNumbers);
                                report.setMissionName(mission.getMissionName());
                                report.setM(UniqueNumber);
                                report.setGadgetName(mission.getGadget());
                                report.setQTime(Qtime);
                                report.setMoneypenny(moneyPennyName);
                                report.setTimeIssued(mission.getTimeIssued());
                                report.setTimeCreated(currentTime);
                                // add the report to the diary
                                Diary.getInstance().addReport(report);
                                complete(c, null);
                            }
                            //endReport
                            else {
                                setThemFree(serialNumbers);
                                forMoneyPenny.resolve(false);
                                complete(c, null);
                                return;
                            }
                        }
                    }
                }
                if(!forMoneyPenny.isDone())
                    forMoneyPenny.resolve(false);
                    setThemFree(mission.getSerialAgentsNumbers());
                    complete(c, null);
            }
        };
        this.subscribeEvent(MissionReceivedEvent.class, callbackName);

        Callback<TickBroadcast> callbackName2 = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                currentTime = c.getTick();
            }
        };
        this.subscribeBroadcast(TickBroadcast.class, callbackName2);

        subscribeBroadcast(TickBroadcastTerminate.class, new Callback<TickBroadcastTerminate>() {
            @Override
            public void call(TickBroadcastTerminate c) {
                terminate();
            }
        });
    }

    private void setThemFree(List<String> sn) {
        ReleaseAgentsEvent releaseThose = new ReleaseAgentsEvent(sn);
        Future<Boolean> f7 = getSimplePublisher().sendEvent(releaseThose);
    }

}



