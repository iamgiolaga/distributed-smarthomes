package singleton;

import home.simulation_src_2019.Measurement;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Singleton_SlidingWindow {
    private ArrayList<Measurement> buffer;
    private ArrayList<Measurement> window;
    private static Singleton_SlidingWindow instance;

    private Singleton_SlidingWindow() {
        buffer = new ArrayList<>();
    }

    public static synchronized Singleton_SlidingWindow getInstance() {
        if (instance == null)
            instance = new Singleton_SlidingWindow();

        return instance;
    }

    // METODO USATO DAL PRODUCER
    public synchronized void fillWindow(Measurement measurement) {
        buffer.add(measurement);

        if (buffer.size() == 24) {
            notify();
        }

    }

    // METODO USATO DAL CONSUMER
    public synchronized ArrayList<Measurement> extractWindow() {

        while (buffer.size() < 24) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (buffer.size() == 24) {
            window = new ArrayList<>(buffer);
        }

        for (int i = 0; i <= 11; i++) {
            buffer.remove(0);
        }


        return window;

    }

    public void displayMeasurements(ArrayList<Measurement> window) {
        Date date;
        Timestamp ts;
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        String dateFormatted;
        int c = 0;

        for (Measurement me : window) {
            ts = new Timestamp(me.getTimestamp());
            date = new Date(ts.getTime());
            dateFormatted = df.format(date);

            if (c == 12)
                System.out.println("-------------------------");

            System.out.println(c + " : " + dateFormatted + " - " + me.getValue());
            c++;
        }
    }

}
