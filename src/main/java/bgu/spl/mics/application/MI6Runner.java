package bgu.spl.mics.application;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;

import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;


/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */

public class MI6Runner {

    public static void main(String[] args) {
        try {


            LinkedList<Thread> threads = new LinkedList<>();
            LinkedList<Subscriber> services = new LinkedList<>();
            int M;
            int MoneyPenny;
            int time;
            try {
                FileReader reader = new FileReader(args[0]);
                JsonElement jsonElement = new JsonParser().parse(reader);
                JsonObject jsonObject = jsonElement.getAsJsonObject();


                //get inventory
                JsonArray gadgets = jsonObject.get("inventory").getAsJsonArray();
                String[] gadgetsArray = new String[gadgets.size()];
                int i = 0;
                for (JsonElement element : gadgets) {
                    gadgetsArray[i] = element.toString().substring(1, element.toString().length() - 1);
                    i++;
                }
                Inventory inventory = Inventory.getInstance();
                inventory.load(gadgetsArray);

                // get Squad
                String name;
                String serialNumber;
                JsonArray squad = jsonObject.get("squad").getAsJsonArray();
                Agent[] agents = new Agent[squad.size()];
                int a = 0;
                for (JsonElement element : squad) {
                    name = element.getAsJsonObject().get("name").toString();
                    name = name.substring(1, name.length() - 1);
                    serialNumber = element.getAsJsonObject().get("serialNumber").toString();
                    serialNumber = serialNumber.substring(1, serialNumber.length() - 1);
                    Agent addagent = new Agent(serialNumber, name);
                    addagent.release();
                    agents[a] = addagent;
                    a++;
                }
                Squad.getInstance().load(agents);

                //get services
                M = jsonObject.get("services").getAsJsonObject().get("M").getAsInt();
                time = jsonObject.get("services").getAsJsonObject().get("time").getAsInt();
                MoneyPenny = jsonObject.get("services").getAsJsonObject().get("Moneypenny").getAsInt();
                JsonArray intelligenceMission = jsonObject.get("services").getAsJsonObject().get("intelligence").getAsJsonArray();
                int d = 0;
                for (JsonElement element1 : intelligenceMission) {
                    List<MissionInfo> intelligenceList = new LinkedList<>();
                    JsonArray mission = element1.getAsJsonObject().get("missions").getAsJsonArray();
                    for (JsonElement element2 : mission) {
                        JsonArray serialAgentNumbers = element2.getAsJsonObject().get("serialAgentsNumbers").getAsJsonArray();
                        List<String> serials = new LinkedList<>();
                        for (JsonElement element3 : serialAgentNumbers) {
                            String number;
                            number = element3.toString();
                            number = number.substring(1, number.length() - 1);
                            serials.add(number);
                        }
                        int duration = element2.getAsJsonObject().get("duration").getAsInt();
                        int timeExpired = element2.getAsJsonObject().get("timeExpired").getAsInt();
                        int timeIssued = element2.getAsJsonObject().get("timeIssued").getAsInt();
                        String missionName = element2.getAsJsonObject().get("name").getAsString();
                        String gadget = element2.getAsJsonObject().get("gadget").getAsString();
                        MissionInfo missionCreated = new MissionInfo();
                        missionCreated.setGadget(gadget);
                        missionCreated.setDuration(duration);
                        missionCreated.setMissionName(missionName);
                        missionCreated.setSerialAgentsNumbers(serials);
                        missionCreated.setTimeExpired(timeExpired);
                        missionCreated.setTimeIssued(timeIssued);
                        intelligenceList.add(missionCreated);
                    }
                    Intelligence newIntelligence = new Intelligence(intelligenceList, d);
                    threads.add(new Thread(newIntelligence, newIntelligence.getName()));
                    d++;
                }
                Q q = new Q("Q1");

                threads.add(new Thread(q, q.getName()));

                for (int k = 0; k < M; k++) {
                    Moneypenny newMoneyPenny = new Moneypenny(k + 1);
                    Thread thread = new Thread(newMoneyPenny, newMoneyPenny.getName());
                    threads.add(thread);
                    services.add(newMoneyPenny);
                }

                for (int j = 0; j < MoneyPenny; j++) {
                    M newM = new M(j + 1);
                    Thread thread = new Thread(newM, newM.getName());
                    threads.add(thread);
                    services.add(newM);
                }

                Latch.latch=new CountDownLatch(threads.size());

                for (Thread thread : threads) {
                    thread.start();
                }

                Latch.latch.await();
                TimeService newTimeService = new TimeService(time);
                Thread newthread = new Thread(newTimeService);
                newthread.start();
                newthread.join();

                for (Thread thread : threads) {
                    thread.join();

                }

                synchronized (newTimeService) {
                    while (!newTimeService.getReady()) {
                        try {
                            newTimeService.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

