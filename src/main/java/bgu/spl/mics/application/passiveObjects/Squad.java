package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private HashMap<String, Agent> agents;


    private static class SingletonHolder {
        private static Squad instance = new Squad();
    }

    public static Squad getInstance() {
        return SingletonHolder.instance;
    }

    private Squad() {
        agents = new HashMap<>();
    }
    /**
     * Retrieves the single instance of this class.
     */

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (Agent a : agents) {
            this.agents.put(a.getSerialNumber(), a);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (String s : serials) {
			Agent a = agents.get(s);
			if (a!=null && !a.isAvailable()) {
				releaseAgentAndNotify(s);
			}
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        try {
            Thread.sleep(time*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        releaseAgents(serials);

    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public boolean getAgents(List<String> serials) {

        serials.sort(String::compareTo);//solve the dead-lock problem! (philosofs)
            for (String s : serials) {
                if (!agents.containsKey(s)) {
                    return false;
                }
            }
            for (String sn : serials) {
                Agent a = agents.get(sn);
                try {
                    synchronized (a) {
                        while(!a.isAvailable()) {
                            a.wait();
                        }
                        a.acquire();

                    }
                } catch (InterruptedException ie) {
                    for (String sn1 : serials){
                        Agent a1 = agents.get(sn1);
                        if(sn1.compareTo(sn)<0){
                            releaseAgentAndNotify(sn1);
                        }
                    }
                }
            }

        return true;
    }
	private void releaseAgentAndNotify(String sn){
		Agent a = agents.get(sn);
		a.release();
		synchronized (a) {
            a.notifyAll();
		}
	}
    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
    	List<String> names=new LinkedList<>();
    	for(String sn:serials) {
    		Agent a=agents.get(sn);
    		if(a!=null)
				names.add(a.getName());
		}
        return names;
    }


}
