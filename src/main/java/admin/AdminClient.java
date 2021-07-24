package admin;

import beans.Home;
//import beans.HomesMap;
import com.google.gson.reflect.TypeToken;
import main.java.beans.DevAndMean;
import beans.Statistics;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AdminClient {
    private static String HOST = "localhost";
    private static int PORT = 6789;
    private static final String URI = "http://" + HOST + ":" + PORT + "/";
    private static boolean firstExec = true;
    private static Scanner scanner;

    public static void main(String args[]) throws IOException {

        if (firstExec) {
            scanner = new Scanner(System.in);
            firstExec = false;
        }

        displayStart();

        int opt = readInput(scanner);
        int n, home_id = -1;
        String path, homeConsumption, endPoint = "";
        ClientResponse clientResponse = null;

        switch (opt) {

            case 0:
                System.out.println("Invalid input \n\n");
                callMain();
                return;

            case 1:
                System.out.println("\n\n GET HOMES LIST");
                endPoint = "homes/";

                try {
                    clientResponse = doRequest(endPoint);
                }
                catch (ClientHandlerException e) {
                    System.out.println("Server not found");
                    return;
                }

                handleResponse(clientResponse, home_id, -1, opt);
                return;

            case 2:

                System.out.println("\n\n GET A HOME CONSUMPTION \n\n Insert the home id whose consumption you want to see: ");
                homeConsumption = scanner.next();
                endPoint = "statistics/";

                if (homeConsumption != null)
                    endPoint += homeConsumption + "/";

                try {
                    home_id = Integer.parseInt(homeConsumption);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

                while (true) {
                    n = askForN(scanner);

                    if (n != 0)
                        break;
                    else
                        System.out.println("\n\n Invalid input \n\n");
                }

                endPoint += n;

                try {
                    clientResponse = doRequest(endPoint);
                }
                catch (ClientHandlerException e) {
                    System.out.println("Server not found");
                    return;
                }

                handleResponse(clientResponse, home_id, n, opt);

                return;

            case 3:

                System.out.println("\n\n GET GLOBAL CONSUMPTION");
                endPoint = "statistics/";

                while (true) {
                    n = askForN(scanner);

                    if (n != 0)
                        break;
                    else
                        System.out.println("\n\n Invalid input \n\n");
                }

                endPoint += n;

                try {
                    clientResponse = doRequest(endPoint);
                }
                catch (ClientHandlerException e) {
                    System.out.println("Server not found");
                    return;
                }

                handleResponse(clientResponse, home_id, n, opt);

                return;

            case 4:

                System.out.println("\n\n ANALYZE A HOME CONSUMPTION \n\n Insert the home id you want to analyze: ");
                homeConsumption = scanner.next();
                endPoint = "statistics/";

                if (homeConsumption != null)
                    endPoint += homeConsumption + "/";

                try {
                    home_id = Integer.parseInt(homeConsumption);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                while (true) {
                    n = askForN(scanner);

                    if (n != 0)
                        break;
                    else
                        System.out.println("\n\n Invalid input \n\n");
                }

                endPoint += n + "/dev_mean";

                try {
                    clientResponse = doRequest(endPoint);
                }
                catch (ClientHandlerException e) {
                    System.out.println("Server not found");
                    return;
                }

                handleResponse(clientResponse, home_id, n, opt);

                return;

            case 5:

                System.out.println("\n\n ANALYZE GLOBAL CONSUMPTION \n\n Global analysis: ");
                endPoint = "statistics/";

                while (true) {
                    n = askForN(scanner);

                    if (n != 0)
                        break;
                    else
                        System.out.println("\n\n Invalid input \n\n");
                }

                endPoint += n + "/dev_mean";

                try {
                    clientResponse = doRequest(endPoint);
                }
                catch (ClientHandlerException e) {
                    System.out.println("Server not found");
                    return;
                }

                handleResponse(clientResponse, home_id, n, opt);

                return;

        }

    }

    public static int readInput(Scanner scanner) {
        String option;
        int opt;

        try {
            option = scanner.next();
            opt = Integer.parseInt(option);
            return opt;
        }

        catch (Exception e) {
            return 0;
        }
    }

    public static int askForN(Scanner scanner) {
        System.out.println("\n Insert the number of stats you want to see: ");
        return readInput(scanner);
    }

    public static HttpURLConnection createUrlConnection(String requestMethod, String path) {

        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
            return con;
        }

        catch (Exception e) {
            return null;
        }
    }

    public static void displayStart() {
        System.out.println("Welcome to the admin bash to monitor the network consumption \n");

        System.out.println("Which service do you want to enable? (insert 1, 2, 3, 4 or 5) \n");

        System.out.print("1: Get homes list");

        System.out.print("\t\t\t");

        System.out.print("2: Get a home consumption");

        System.out.print("\t\t\t");

        System.out.print("3: Get global consumption");

        System.out.print("\t\t\t");

        System.out.print("4: Analyze a home consumption");

        System.out.print("\t\t\t");

        System.out.print("5: Analyze global consumption \n\n");
    }

    public static void displayRequest(String path) {
        System.out.println("\n\n Sending request to: " + path);
        System.out.println(".");
        System.out.println(".");
        System.out.println(". \n");
    }

    public static void callMain() {
        System.out.println("\n\n Reloading bash...");
        System.out.println("\n\n\n\n\n");

        try {
            main(new String[] {});
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ClientResponse doRequest(String path) {
        path = URI + path;
        displayRequest(path);
        Client client = Client.create();
        WebResource webResource = client.resource(path);
        return webResource.accept("application/json").get(ClientResponse.class);
    }

    public static void handleResponse(ClientResponse response, int home_id, int number, int typeOfResponse) {

        Type statisticsType = new TypeToken<List<Statistics>>(){}.getType();
        Type homesType = new TypeToken<HashMap<Integer, Home>>(){}.getType();
        Gson gson = new Gson();
        String stringFromServer = response.getEntity(String.class);

        switch(response.getStatus()) {
            case 200:
                switch (typeOfResponse) {

                    case 1:
                        //HomesMap homesMap = gson.fromJson(stringFromServer, HomesMap.class);

                        HashMap<Integer, Home> homesMap = gson.fromJson(stringFromServer, homesType);

                        if (homesMap.size() == 0)
                            System.out.println("Empty list");
                        else
                            System.out.println("Homes list");

                        for (HashMap.Entry<Integer, Home> home : homesMap.entrySet()) {
                            System.out.println(" - " + home.getValue().getId() + ", " + home.getValue().getIp() + ", " + home.getValue().getNumeroPorta() + ";");
                        }

                        break;

                    case 2:
                        ArrayList<Statistics> localList = gson.fromJson(stringFromServer, statisticsType);

                        if (localList.size() == 0)
                            System.out.println("Empty list");

                        else {
                            System.out.println("Consumption of home with id " + home_id + " (last " + number + " statistics): ");

                            for (Statistics stat : localList)
                                System.out.println(" - " + stat.getValue() + ", " + stat.getTimestamp() + ";");
                        }

                        break;

                    case 3:
                        ArrayList<Statistics> globalList = gson.fromJson(stringFromServer, statisticsType);

                        if (globalList.size() == 0)
                            System.out.println("Empty list");

                        else {
                            System.out.println("Last " + number + " global statistics: ");

                            for (Statistics stat : globalList)
                                System.out.println(" - " + stat.getValue() + ", " + stat.getTimestamp() + ";");
                        }

                        break;

                    case 4:
                        DevAndMean localDevAndMean = gson.fromJson(stringFromServer, DevAndMean.class);

                        if ( localDevAndMean.getStdDev() == -1 && localDevAndMean.getMean() == -1)
                            System.out.println("No available measures for home " + home_id);

                        else {
                            System.out.println("Consumption of home with id " + home_id + " (last " + number + " statistics): ");
                            System.out.println("Mean: " + localDevAndMean.getMean());
                            System.out.println("Standard deviation: " + localDevAndMean.getStdDev());
                        }

                        break;

                    case 5:
                        DevAndMean globalDevAndMean = gson.fromJson(stringFromServer, DevAndMean.class);

                        if ( globalDevAndMean.getStdDev() == -1 && globalDevAndMean.getMean() == -1)
                            System.out.println("No available measures");

                        else {
                            System.out.println("Global consumption (last " + number + " statistics): ");

                            System.out.println("Mean: " + globalDevAndMean.getMean());
                            System.out.println("Standard deviation: " + globalDevAndMean.getStdDev());
                        }

                        break;

                }

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
        }

        // ATTENDO 4 SECONDI PER DARE IL TEMPO DI VEDERE IL RISULTATO
        try {
            Thread.sleep(4000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        callMain();
    }

}
