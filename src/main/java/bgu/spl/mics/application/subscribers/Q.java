package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private static Inventory instance;
	private Integer currTime;

	// received time ticks
	// received an gadget availble event
	// has an acsses to the inventory

	public Q(String name) {
		super(name);
		instance=Inventory.getInstance();

	}

	@Override
	protected void initialize() {
		Callback <GadgetAvailableEvent>callbackName= new Callback<GadgetAvailableEvent>() {
			@Override
			public void call(GadgetAvailableEvent e) {
				boolean getGadget = instance.getItem(e.getGadget());
				if(getGadget){
					complete(e, currTime);
				}
				else {
					complete(e,-1);
				}
			}
		};
		this.subscribeEvent(GadgetAvailableEvent.class,callbackName);

		Callback <TickBroadcast>callbackName2= new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast e) {
				currTime=e.getTick();
			}
		};
		this.subscribeBroadcast(TickBroadcast.class,callbackName2);

		subscribeBroadcast(TickBroadcastTerminate.class, new Callback<TickBroadcastTerminate>() {
			@Override
			public void call(TickBroadcastTerminate c) {
				terminate();
			}
		});

	}

}
