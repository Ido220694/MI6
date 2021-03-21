package bgu.spl.mics.application.passiveObjects;

public class MyPair<T,E> {
    T first;
    E second;

    public MyPair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public E getSecond() {
        return second;
    }

    public T getFirst() {
        return first;
    }
}
