package home;

import home.simulation_src_2019.Buffer;
import home.simulation_src_2019.Measurement;
import singleton.Singleton_SlidingWindow;

public class SmartMeter implements Buffer {
    private Singleton_SlidingWindow instance = Singleton_SlidingWindow.getInstance();

    // CALCOLO STATISTICA OGNI 24 MISURAZIONI

    // 50% OVERLAP = SERVE A CATTURARE TRANSIZIONI TRA FINESTRE TEMPORALI CONSECUTIVE

    public SmartMeter() {
    }

    @Override
    public void addMeasurement(Measurement m) {
        instance.fillWindow(m);
    }

}
