package bgu.spl.mics.application;

import java.io.FileOutputStream;
import java.io.IOException;
import com.google.gson.Gson;

public class Printer {
    public static void print(Object obj, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);

            fos.write(new Gson().toJson(obj).getBytes());

            fos.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
            System.out.println("Couldn't right " + obj.getClass() + " to " + filename);
        }
    }
}
