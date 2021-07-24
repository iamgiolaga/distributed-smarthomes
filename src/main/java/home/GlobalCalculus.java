package home;

import beans.Statistics;
import com.sun.jersey.api.client.ClientResponse;
import singleton.Singleton_Home;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static home.MeasurementExtraction.calculateGlobalStat;
import static home.MeasurementExtraction.doGlobalStatRequest;

public class GlobalCalculus extends Thread {

    private ArrayList<Statistics> allLocalStats;
    private Statistics myGlobalStat;
    private Date date;
    private Timestamp ts;
    private DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    private String dateFormatted;
    private String endpoint;
    private ClientResponse globalStatRequest;

    @Override
    public void run() {
        Singleton_Home homeInstance = Singleton_Home.getInstance();

        // TODO: SISTEMARE CHE CI SONO CASI IN CUI NON E' UGUALE
        allLocalStats = homeInstance.getAllLocalStats();
        myGlobalStat = calculateGlobalStat(allLocalStats);

        ts = new Timestamp(myGlobalStat.getTimestamp());
        date = new Date(ts.getTime());
        dateFormatted = df.format(date);

        System.out.println("\n Global consumption: " + myGlobalStat.getValue() + " - " + dateFormatted + "\n");

        //homeInstance.setGlobalStat(myGlobalStat);

        if (homeInstance.isCoordinator()) {

            // INVIO DELLA STATISTICA GLOBALE SOLO DA PARTE DEL COORDINATORE (PER EVITARE RIDONDANZA SULL'INVIO)
            endpoint = "statistics";
            globalStatRequest = doGlobalStatRequest(endpoint, myGlobalStat);
            //handleGlobalStatResponse(globalStatRequest);

        }

        allLocalStats.clear();
        homeInstance.setAllLocalStats(allLocalStats);
    }
}
