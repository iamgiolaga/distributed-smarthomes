package home;

import beans.Home;
import beans.Statistics;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import home.simulation_src_2019.Measurement;
import singleton.Singleton_Home;
import singleton.Singleton_SlidingWindow;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MeasurementExtraction extends Thread {
    // INFO DI CONTATTO DEL SERVER AMMINISTRATORE
    private static String HOST = "localhost";
    private static int PORT = 6789;
    private static final String URI = "http://" + HOST + ":" + PORT + "/";
    //
    private Home myHome;
    private ArrayList<Measurement> measurements;
    private HashMap<Integer, Home> myVisionOfNetwork;
    private Date date;
    private Timestamp ts;
    private DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    private String dateFormatted;
    private String endpoint;
    private Statistics myLocalStat;
    private Statistics myGlobalStat;
    private ArrayList<Statistics> allLocalStats;
    private ClientResponse localStatRequest, globalStatRequest;
    private boolean exitCondition = false;

    @Override
    public void run() {
        while (!exitCondition) {
            Singleton_SlidingWindow slidingWindowInstance = Singleton_SlidingWindow.getInstance();
            Singleton_Home homeInstance = Singleton_Home.getInstance();
            measurements = slidingWindowInstance.extractWindow();

            myHome = homeInstance.getMyHome();

            endpoint = "statistics/" + myHome.getId();
            //System.out.println("\n Sliding window values: \n");
            //displayMeasurements(measurements);

            myLocalStat = calculateLocalStat(measurements);
            homeInstance.setMyLocalStat(myLocalStat);
            homeInstance.addLocalStatToAllLocalStats(myLocalStat);

            // INVIO DELLA STATISTICA LOCALE APPENA CALCOLATA
            localStatRequest = doLocalStatRequest(endpoint, myLocalStat);
            //handleLocalStatResponse(localStatRequest);

            myVisionOfNetwork = homeInstance.getMyVisionOfNetwork();


            if (myVisionOfNetwork.size() != 0) {

                // HOME CONTATTA IN BROADCAST TUTTE LE CASE DELLA LISTA PER INVIARE STATISTICHE (TRANNE SE' STESSA)
                for (HashMap.Entry<Integer, Home> homeEntry : myVisionOfNetwork.entrySet()) {

                    if (homeEntry.getValue().getId() != myHome.getId()) {
                        HomeClient.synchronousCall(myHome, homeEntry.getValue(), 3);
                    }

                }

            }

            GlobalCalculus globalCalculus = new GlobalCalculus();
            globalCalculus.start();

//            allLocalStats = homeInstance.getAllLocalStats();
//            myGlobalStat = calculateGlobalStat(allLocalStats);
//
//            ts = new Timestamp(myGlobalStat.getTimestamp());
//            date = new Date(ts.getTime());
//            dateFormatted = df.format(date);
//
//            System.out.println("\n Global consumption: " + myGlobalStat.getValue() + " - " + dateFormatted + "\n");
//
//            //homeInstance.setGlobalStat(myGlobalStat);
//
//            if (homeInstance.isCoordinator()) {
//
//                // INVIO DELLA STATISTICA GLOBALE SOLO DA PARTE DEL COORDINATORE (PER EVITARE RIDONDANZA SULL'INVIO)
//                endpoint = "statistics";
//                globalStatRequest = doGlobalStatRequest(endpoint, myGlobalStat);
//                //handleGlobalStatResponse(globalStatRequest);
//
//            }

//            allLocalStats.clear();
//            homeInstance.setAllLocalStats(allLocalStats);

        }
    }

    public void stopMeasuring() {
        exitCondition = true;
    }

    public void displayMeasurements(ArrayList<Measurement> window) {
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

    public static Statistics calculateLocalStat(ArrayList<Measurement> window) {
        double length = 0.0;
        double sum = 0.0;

        if (!window.isEmpty()) {
            length = window.size();

            for (Measurement m : window) {
                sum += m.getValue();
            }

            return new Statistics(
                    sum / length,
                    System.currentTimeMillis() - computeMidnightMilliseconds()
            );
        }

        return new Statistics(length,0L);
    }

    public static Statistics calculateGlobalStat(ArrayList<Statistics> localStats) {
        double sum = 0.0;
        ArrayList<Long> timestampList = new ArrayList<>();

        if (!localStats.isEmpty()) {

            for (Statistics s : localStats) {
                sum += s.getValue();
                timestampList.add(s.getTimestamp());
            }

            return new Statistics(
                    sum,
                    Collections.max(timestampList) // ALLA GLOBALE ASSEGNO IL MAX DEI TIMESTAMP
            );
        }

        return new Statistics(sum,0L);
    }

    public static long computeMidnightMilliseconds(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static ClientResponse doLocalStatRequest(String path, Statistics localStat) {
        path = URI + path;
        Gson gson = new Gson();
        String localStatString = gson.toJson(localStat, Statistics.class);
        Client client = Client.create();
        WebResource webResource = client.resource(path);
        return webResource.type("application/json").post(ClientResponse.class, localStatString);
    }

    public static ClientResponse doGlobalStatRequest(String path, Statistics globalStat) {
        path = URI + path;
        Gson gson = new Gson();
        String globalStatString = gson.toJson(globalStat, Statistics.class);
        Client client = Client.create();
        WebResource webResource = client.resource(path);
        return webResource.type("application/json").post(ClientResponse.class, globalStatString);
    }

    public static void handleLocalStatResponse(ClientResponse response) {

        System.out.println(response.getStatus());
        switch(response.getStatus()) {
            case 200:
                System.out.println("Local stat sent");
                break;
            case 400:
                System.out.println("Bad request");
                break;
            case 404:
                System.out.println("Not found");
                break;
            case 405:
                System.out.println("Not authorized");
                break;
            case 409:
                System.out.println("Conflict");
                break;
            case 415:
                System.out.println("Unsupported media type");
                break;
        }

    }

    public static void handleGlobalStatResponse(ClientResponse response) {

        System.out.println(response.getStatus());
        switch(response.getStatus()) {
            case 200:
                System.out.println("Global stat sent");
                break;
            case 400:
                System.out.println("Bad request");
                break;
            case 404:
                System.out.println("Not found");
                break;
            case 405:
                System.out.println("Not authorized");
                break;
            case 409:
                System.out.println("Conflict");
                break;
            case 415:
                System.out.println("Unsupported media type");
                break;
        }

    }

}
