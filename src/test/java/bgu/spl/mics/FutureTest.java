package bgu.spl.mics;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    Future<String> future = null;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    void get(){
        future.resolve("finish");
        assertEquals(future.get(), "finish");
    }

    @Test
    void resolve(){
        future.resolve("finish");
        assertEquals(future.get(), "finish");

    }

    @Test
    void isDone(){
        assertFalse(future.isDone());
        future.resolve("finish");
        assertTrue(future.isDone());
    }
    @Test
    void get2(){
        assertNull(future.get(3, TimeUnit.DAYS));
        future.resolve("finish");
        assertEquals(future.get(3, TimeUnit.DAYS), "finish");
    }

}
