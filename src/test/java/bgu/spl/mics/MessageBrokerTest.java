package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.Moneypenny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.subscribers.M;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {

    MessageBroker msg =null;

    @BeforeEach
    public void setUp(){
        msg = MessageBrokerImpl.getInstance();
    }


    @Test

    void subscribeEvent(){

        Subscriber m = new M(1);
        m.initialize();

        msg.subscribeEvent(Eventtest.class, m);
        Eventtest event = new Eventtest("mission");

        try {
            assertEquals(event, msg.awaitMessage(m));
        }

        catch (Exception ie){
            fail("the test of subscribe event failed");
        }


    }


    @Test
    void subscribeBroadcast(){

        Subscriber m2 = new M(2);
        msg.subscribeBroadcast(Brodcasttest.class, m2);
        Brodcasttest bro = new Brodcasttest("Action");

        try {
            assertEquals(bro, msg.awaitMessage(m2));

        }
        catch (Exception ie){
            fail("the test of subscribe broadcast failed");
        }
    }

    @Test
    void complete(){

        Subscriber m = new M(3);
        msg.subscribeEvent(Eventtest.class, m);
        Eventtest event = new Eventtest("mission");
        try {
            assertEquals(event , msg.awaitMessage(m));
        }
        catch (Exception ie){
            fail("the test of subscribe event or broadcast failed");
        }

        String result = "finish";
        try {
            msg.complete(event, result);
        }
        catch (Exception ie){
            fail("the test of complete failed");
        }

        try {
        assertNull(msg.awaitMessage(m));
        }
        catch (Exception ie){
            fail("the test of complete failed");
        }
    }

    @Test
    void sendBroadcast(){
        Subscriber m = new M(4);
        msg.subscribeBroadcast(Brodcasttest.class, m);
        Brodcasttest brodcast = new Brodcasttest("finding x");
        try {
            msg.sendBroadcast(brodcast);
        }
        catch (Exception ie){
            fail("the test of send broadcast fails");
        }
        try {
            assertEquals(brodcast, msg.awaitMessage(m));
        }
        catch (Exception ie){
            fail("the test of subscribe broadcast failed");
        }

    }

    @Test
    void sendEvent(){
        Subscriber m = new M(5);
        msg.subscribeEvent(Eventtest.class, m);
        Eventtest event = new Eventtest("mission");
        try {
            msg.sendEvent(event);
        }
        catch (Exception ie){
            fail("the test of send event fails");
        }
        try {
            assertEquals(event, msg.awaitMessage(m));
        }
        catch (Exception ie){
            fail("the test of subscribe event failed");
        }

    }

    @Test
    void register(){
        Subscriber penny = new Moneypenny(1);
        try {
            msg.register(penny);
        }
        catch (Exception ie){
            fail("the register is not working");
        }

    }

    @Test
    void unregister(){

        Subscriber penny2 = new Moneypenny(2);
        try {
            msg.unregister(penny2);
        }
        catch (Exception e){
            fail("the test of unregister has failed");
        }

        assertThrows(Exception.class ,()-> {msg.subscribeEvent(Eventtest.class, penny2);});

    }

    @Test
    void awaitMessage(){
        Subscriber penny3 = new Moneypenny(3);

        try {
            msg.awaitMessage(penny3);
        }
        catch (InterruptedException e){
            fail("you have exception that is not correct");
        }


    }
}
