package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {

	 private String SerialNubmer= null;
	 private String Name = null;
	 private Boolean available=true;


	public Agent(String serialNumber, String Name){
		this.Name = Name;
		this.SerialNubmer = serialNumber;
	}

    /**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumberNew) {
		SerialNubmer=serialNumberNew;
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {
		return SerialNubmer;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		Name=name;
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		return  Name;
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		return available;
	}

	/**
	 *
	 * Acquires an agent.
	 */
	public void acquire(){
		available = false;
	}

	/**
	 * Releases an agent.
	 */
	public void release(){
		available = true;
	}
}
