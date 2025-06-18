package mpp;

import mpp.gui.StartProtoClientFX;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        //StartJsonClientFX.main(args);
        StartProtoClientFX.main(args);
    }

    public static Properties props = new Properties();

    static {
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config " + e);
        }
    }
}