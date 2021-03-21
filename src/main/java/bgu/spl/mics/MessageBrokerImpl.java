package bgu.spl.mics;


import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> MapSubscribers;
    ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> MapMessegesForSubscribers;
    ConcurrentHashMap<Event, Future> MapFutures;

    private static class SingletonHolder {
        private static MessageBroker instance = new MessageBrokerImpl();
    }

    private MessageBrokerImpl() {
        MapSubscribers = new ConcurrentHashMap<>();
        MapMessegesForSubscribers = new ConcurrentHashMap<>();
        MapFutures = new ConcurrentHashMap<>();
    }


    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        subscribeMessage(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        subscribeMessage(type, m);
    }


    private void subscribeMessage(Class<? extends Message> type, Subscriber s) {
        MapSubscribers.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<Subscriber> subs = MapSubscribers.get(type);
        subs.add(s);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        Future<T> f = MapFutures.get(e);
        f.resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<Subscriber> subs = MapSubscribers.get(b.getClass());
        if (subs == null) {
            return;
        }
        for (Subscriber s : subs) {
            BlockingQueue<Message> todoQ = MapMessegesForSubscribers.get(s);
            if (todoQ == null) {
                continue;
            }
            todoQ.add(b);
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> f = new Future<>();
        ConcurrentLinkedQueue<Subscriber> subs = MapSubscribers.get(e.getClass());
        if (subs == null) {
            return null;
        }
        Subscriber s;

        synchronized (e.getClass()) {

            s = subs.poll();
            if (s == null) {
                return null;
            }
            subs.add(s);
        }

        synchronized (s) {
            LinkedBlockingQueue<Message> todoQ = MapMessegesForSubscribers.get(s);
            if (todoQ == null) {
                return null;
            }
            MapFutures.put(e, f);
            todoQ.add(e);
        }

        return f;
    }

    @Override
    public void register(Subscriber m) {
        MapMessegesForSubscribers.putIfAbsent(m, new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(Subscriber s) {
        for (Entry<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> entry : MapSubscribers.entrySet()) {
            synchronized (entry.getKey()) {
                entry.getValue().remove(s);
            }
        }
        LinkedBlockingQueue<Message> todoQ;
        synchronized (s) {
            todoQ = MapMessegesForSubscribers.remove(s);
        }
        while (!todoQ.isEmpty()) {
            Message m = todoQ.poll();
            if (m instanceof Event) {
                Event<?> e = (Event<?>) m;
                Future<?> f = MapFutures.get(e);
                f.resolve(null);
            }
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        if (MapMessegesForSubscribers.get(m) == null) {
            throw new IllegalStateException("there are no messages for this subscriber");
        }
        return MapMessegesForSubscribers.get(m).take();
    }
}
