package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SquadTest {

    Squad sqa = null;
    @BeforeEach
    public void setUp(){
        sqa =Squad.getInstance();
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }

    @Test
    void getInstance() {
        Object check = Squad.getInstance();
        assertNotNull(check);
        assertTrue(check instanceof Squad);

        assertEquals(sqa, check);

    }

    @Test


    void load(){
        Agent[] check = new Agent[2];
        Agent ourHero = new Agent("002", "Inbal");
        Agent notourHero = new Agent("001", "Lilach");
        check[0] = notourHero;
        check[1] = ourHero;
        sqa.load(check);
        assertEquals(ourHero.getName(), "Inbal");
        assertEquals(ourHero.getSerialNumber(), "002");
        assertNotEquals(notourHero.getName(), "Maya");

    }
    @Test
    void releaseAgents() {
        Agent angel = new Agent("008","Dor");
        Agent Garbage = new Agent("007", "Devil");

        List<String> serials = new ArrayList<>();
        serials.add(angel.getName());
        serials.add(Garbage.getName());


        sqa.releaseAgents(serials);


        assertEquals(true, Garbage.isAvailable());
        assertNotEquals(false, angel.isAvailable());

    }

    @Test
    void sendAgents() {
        Agent check = new Agent("001", "Dani_Shovevani");
        List<String> serials=new ArrayList<>();
        int timeout=1000;
        serials.add(check.getSerialNumber());
        sqa.sendAgents(serials, timeout);
        assertFalse(sqa.getAgents(serials));
    }

    @Test
    void getAgents() {
        Agent check = new Agent("003", "SpairShuker");
        List<String> serials = new ArrayList<>();
        List<String> serials2 = new ArrayList<>();
        serials.add(check.getSerialNumber());

        assertEquals(true, sqa.getAgents(serials));
        assertNotEquals(true, sqa.getAgents(serials2));

    }

    @Test
    void getAgentsNames() {
        Agent check = new Agent ("009", "Amir");
        List<String> SerialNumbers = new ArrayList<>();
        List<String> SerialNames = new ArrayList<>();



        SerialNames.add(check.getName());
        SerialNumbers.add(check.getSerialNumber());

        List<String> SerialNames2 = new ArrayList<>();
        SerialNames2.add("yaeli");

        assertEquals(SerialNames, sqa.getAgentsNames(SerialNumbers));
        assertNotEquals(SerialNames2, sqa.getAgentsNames(SerialNumbers));
    }
}
