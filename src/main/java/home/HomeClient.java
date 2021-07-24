package home;
import beans.Booster;
import beans.Home;
import beans.Statistics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import grpc.CommunicationServiceGrpc.*;
import grpc.CommunicationServiceOuterClass;
import grpc.CommunicationServiceOuterClass.*;
import home.simulation_src_2019.SmartMeterSimulator;
import io.grpc.*;
import grpc.CommunicationServiceImpl;
import io.grpc.stub.StreamObserver;
import singleton.Singleton_Boost;
import singleton.Singleton_Display;
import singleton.Singleton_Home;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static grpc.CommunicationServiceGrpc.newBlockingStub;
import static grpc.CommunicationServiceGrpc.newStub;

public class HomeClient {
    // INFO DI CONTATTO DEL SERVER AMMINISTRATORE
    private static String HOST = "localhost";
    private static int PORT = 6789;
    private static final String URI = "http://" + HOST + ":" + PORT + "/";
    //
    private static boolean firstExec = true;
    private static Scanner scanner;
    private static boolean registrationAuth = false;
    private static boolean inTheNetwork = false;
    private static SmartMeterSimulator smartMeterSimulator;

    public static void main(String args[]) throws IOException {
        int home_id, portNumber, opt;
        String ip;
        MeasurementExtraction measurementExtractionThread = new MeasurementExtraction();

        if (firstExec) {
            scanner = new Scanner(System.in);
            firstExec = false;
        }

        if (!registrationAuth) {
            displayStart();
        }

        if ( readYesOrNo(scanner) ) {

            // REGISTRAZIONE DELLA CASA
            while (true) {
                home_id = askForId(scanner);

                if (home_id != 0)
                    break;
                else
                    System.out.println("\n\n Invalid input \n\n");
            }

            while (true) {
                portNumber = askForPortNumber(scanner);

                if (portNumber != -1)
                    break;
            }

            while (true) {
                ip = askForIp(scanner);

                if ( !ip.equals("noIp") )
                    break;
            }

            // INIZIALIZZO SINGLETON CONTENENTE LA VISIONE CHE QUESTA CASA HA DELLA RETE
            Singleton_Home instance = Singleton_Home.getInstance();

            // INIZIALIZZO SINGLETON CONTENENTE LE INFORMAZIONI SUL CONSUMO EXTRA
            Singleton_Boost boostInstance = Singleton_Boost.getInstance();

            // INIZIALIZZAZIONE DELLA CASA + URI CONTENENTE HOST E PORTA DEL SERVER
            Home myHome = initializeHome(home_id, ip, portNumber);

            // SALVO LE MIE INFORMAZIONI (MI SARANNO UTILI PER COMUNICAZIONE GRPC)
            instance.setMyHome(myHome);

            // REGISTRAZIONE DELLA CASA
            String endPoint = "homes/";

            ClientResponse clientResponse;

            try {
                clientResponse = doRegistrationRequest(endPoint, myHome);
            }
            catch (ClientHandlerException e) {
                System.out.println("Server not found");
                return;
            }

            HashMap<Integer, Home> homesMap = handleRegistrationResponse(clientResponse);

            if (registrationAuth) {

                // CREAZIONE DELLA RETE
                // sfrutto lista restituita da server per presentarmi alle altre case

                System.out.println("\n Entering the network... ");

                // AGGIUNGO LA LISTA DELLE CASE CHE LA CASA HA RICEVUTO DURANTE LA REGISTRAZIONE
                instance.setMyVisionOfNetwork(homesMap);

                // HOME SI METTE IN ASCOLTO SULLA PORTA INDICATA
                Server grpcServer = startGrpcService(portNumber);

                if (homesMap.size() != 0) {

                    // HOME CONTATTA IN BROADCAST TUTTE LE CASE DELLA LISTA TRANNE SE' STESSA
                    for (HashMap.Entry<Integer, Home> homeEntry : homesMap.entrySet()) {

                        if (homeEntry.getValue().getId() != myHome.getId()) {
                            synchronousCall(myHome, homeEntry.getValue(), 0);
                        }

                    }

                    if (homesMap.size() == 1) {
                        System.out.println("\n You are the first in the network, hence you are the coordinator!");
                        instance.setCoordinator(true);
                    }

                }

                System.out.println("\n ...Done!");

                inTheNetwork = true;

                if (inTheNetwork) {
                    // SE LA CASA E' REGISTRATA E FA PARTE DELLA RETE

                    //START DELLO SMART METER
                    smartMeterSimulator = initializeSmartMeter();

                    // PARADIGMA PRODUTTORE-CONSUMATORE:
                    // PRODUTTORE: THREAD CHE RIEMPIE LA SLIDING WINDOW
                    // CONSUMATORE: THREAD CHE VUOLE LEGGERE LA SLIDING WINDOW PER CALCOLARE STATISTICHE LOCALI

                    // FACCIO PARTIRE THREAD PER LA LETTURA DEI DATI DELLO SMART METER E PER EVITARE BUSY WAITING USO WAIT E NOTIFY
                    measurementExtractionThread.start();

                }

                while (registrationAuth) {

                    displayLogoutOrBoost();

                    opt = readInput(scanner);

                    switch (opt) {

                        case 0:
                            System.out.println("\n Invalid input \n");
                            continue;

                        case 1:
                            Singleton_Display displayInstance = Singleton_Display.getInstance();

                            if (!boostInstance.isBoosting()) {

                                // COMUNICAZIONE IN BROADCAST DELLA RICHIESTA DI BOOST ALLE CASE DELLA RETE (TRANNE SE' STESSA)
                                for (HashMap.Entry<Integer, Home> homeEntry : instance.getMyVisionOfNetwork().entrySet()) {
                                    asynchronousCall(myHome, homeEntry.getValue());
                                }

                            }

                            else {
                                System.out.println("\n You are already boosting! You can use one boost at a time. \n");
                            }

                            displayInstance.waitToDisplay();

                            break;

                        case 2:

                            // INVIO DELL'ULTIMA STATISTICA GLOBALE (SOLO SE SONO COORDINATORE PERO')
//                            System.out.println("Sending last available global statistics to server...");
//                            endPoint = "statistics";
//                            clientResponse = MeasurementExtraction.doGlobalStatRequest(endPoint, Singleton_Home.getInstance().getGlobalStat());
//                            //MeasurementExtraction.handleGlobalStatResponse(clientResponse);

                            // RICHIESTA DI RIMOZIONE DELLA CASA AL SERVER
                            endPoint = "homes/" + home_id;
                            clientResponse = doRemoveRequest(endPoint);
                            handleRemoveResponse(clientResponse);

                            // RICHIESTA DI RIMOZIONE DELLE SUE STATISTICHE
                            endPoint = "statistics/" + home_id;
                            clientResponse = doRemoveRequest(endPoint);
                            handleLocalStatsRemoveResponse(clientResponse, measurementExtractionThread);

                            if (!registrationAuth) {
                                // COMUNICAZIONE IN BROADCAST DELL'USCITA ALLE CASE DELLA RETE (TRANNE SE' STESSA)
                                for (HashMap.Entry<Integer, Home> homeEntry : instance.getMyVisionOfNetwork().entrySet()) {

                                    if (homeEntry.getValue().getId() != myHome.getId()) {
                                        synchronousCall(myHome, homeEntry.getValue(), 2);
                                    }

                                }

                                // SE VOGLIO USCIRE MA SONO COORDINATORE DEVO INDIRE ELEZIONE
                                if (instance.isCoordinator()) {

                                    Set<Integer> range = instance.getMyVisionOfNetwork().keySet();
                                    int max = Collections.max(range);
                                    Home electionCandidate = instance.getMyVisionOfNetwork().get(max);

                                    if (myHome.getId() != max) {
                                        // IL MIO ID NON E' IL MASSIMO, CONTATTO QUINDI IL CANDIDATO CHE HA ID MAX
                                        synchronousCall(myHome, electionCandidate, 1);
                                    }

                                    else {

                                        // SE SONO IL MAX MA VOGLIO USCIRE CONTATTO IL SECONDO PIU' ALTO
                                        // LO FACCIO SOLO SE NON SONO L'ULTIMO DELLA RETE

                                        if (instance.getMyVisionOfNetwork().size() > 1) {
                                            instance.getMyVisionOfNetwork().remove(max);
                                            range = instance.getMyVisionOfNetwork().keySet();
                                            max = Collections.max(range);
                                            electionCandidate = instance.getMyVisionOfNetwork().get(max);
                                            synchronousCall(myHome, electionCandidate, 1);
                                        }

                                    }

                                    instance.setCoordinator(false);
                                }
                                // NON CI SONO ALTRI CASI DI ELEZIONE PERCHE' LE USCITE SONO CONTROLLATE
                                homesMap = instance.getMyVisionOfNetwork();
                                homesMap.remove(myHome.getId());
                                instance.setMyVisionOfNetwork(homesMap);
                                inTheNetwork = false;

                                // IMPORTANTE PERCHE' SE IN FUTURO ENTRA NUOVA CASA CON PORTA GIA' USATA IN PRECEDENZA SOLLEVA ECCEZIONE
                                grpcServer.shutdown();

                                return;
                            }

                            break;

                    }

                }

            }
        }

        else {
            callMain();
        }
    }

    public static void displayRequest(String path) {
        System.out.println("\n\n Sending request to: " + path);
        System.out.println(".");
        System.out.println(".");
        System.out.println(".");
    }

    public static ClientResponse doRegistrationRequest(String path, Home home) {
        path = URI + path;
        displayRequest(path);
        Gson gson = new Gson();
        String homeString = gson.toJson(home, Home.class);
        Client client = Client.create();
        WebResource webResource = client.resource(path);
        return webResource.type("application/json").accept("application/json").post(ClientResponse.class, homeString);
    }

    public static ClientResponse doRemoveRequest(String path) {
        path = URI + path;
        displayRequest(path);
        Client client = Client.create();
        WebResource webResource = client.resource(path);
        return webResource.delete(ClientResponse.class);
    }

    public static HashMap<Integer, Home> handleRegistrationResponse(ClientResponse response) {

        Gson gson = new Gson();
        HashMap<Integer, Home> homesMap = new HashMap<>();
        String stringFromServer = response.getEntity(String.class);

        System.out.println(response.getStatus() + "\n");

        switch(response.getStatus()) {
            case 200:
                registrationAuth = true;
                Type type = new TypeToken<HashMap<Integer, Home>>(){}.getType();
                homesMap = gson.fromJson(stringFromServer, type);

                System.out.println("Registration done. Homes list: ");

                if (homesMap.size() == 0)
                    System.out.println("Empty list");

                else {
                    displayNetwork(homesMap);
                }

                break;
            case 400:
                System.out.println("Bad request");
                callMain();
                break;
            case 404:
                System.out.println("Not found");
                callMain();
                break;
            case 405:
                System.out.println("Not authorized");
                callMain();
                break;
            case 409:
                System.out.println("Conflict");
                callMain();
                break;
            case 415:
                System.out.println("Unsupported media type");
                break;
        }

        return homesMap;
    }

    public static void handleRemoveResponse(ClientResponse response) {

        switch (response.getStatus()) {
            case 200:
                // RIMOZIONE DELLA CASA
                registrationAuth = false;
                inTheNetwork = false;
                System.out.println("Server approved your logout");
                break;
            case 400:
                break;
            case 404:
                break;
            case 405:
                break;
            case 409:
                break;
            case 415:
                System.out.println("Unsupported media type");
        }

    }

    public static void handleLocalStatsRemoveResponse(ClientResponse response, MeasurementExtraction measurementExtractionThread) {
        switch (response.getStatus()) {
            case 200:
                // RIMOZIONE DELLE STATISTICHE
                smartMeterSimulator.stopMeGently();
                measurementExtractionThread.stopMeasuring();
                System.out.println("And your statistics got removed, bye!");
                break;
            case 400:
                break;
            case 404:
                break;
            case 405:
                break;
            case 409:
                break;
            case 415:
                System.out.println("Unsupported media type");
        }
    }

    public static Home initializeHome(int id, String ip, int portNumber) {
        Home home = new Home(id, ip, portNumber);
        return home;
    }

    public static SmartMeterSimulator initializeSmartMeter() {
        SmartMeter smartMeter = new SmartMeter();
        SmartMeterSimulator smartMeterSimulator = new SmartMeterSimulator(smartMeter);
        smartMeterSimulator.start();
        System.out.println("\n Smart meter started");

        return smartMeterSimulator;
    }

    public static void displayNetwork(HashMap<Integer, Home> homesMap) {

        // STAMPO LA LISTA DELLE CASE REGISTRATE
        for (HashMap.Entry<Integer, Home> homeEntry : homesMap.entrySet()) {
            System.out.println(" - " + homeEntry.getValue().getId() + ", " + homeEntry.getValue().getIp() + ", " + homeEntry.getValue().getNumeroPorta() + ";");
        }

    }

    public static void displayStart() { // STATO DELLA CASA FUORI DALLA RETE, NON REGISTRATO
        System.out.println("Home client started \n");
        System.out.println("Actually you don't belong to any network, would you like to register? (y/n)");
    }

    public static void displayLogoutOrBoost() {
        System.out.println("\n Welcome to the home bash \n");

        System.out.println("Which action do you want to perform? (insert 1 or 2) \n");

        System.out.print("1: Request boost");

        System.out.print("\t\t\t");

        System.out.print("2: Exit \n");
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

    public static boolean readYesOrNo(Scanner scanner) {
        String option;

        try {
            option = scanner.next();

            switch (option) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("\n\n Invalid input \n\n");
            }

        }

        catch (Exception e) {
            System.out.println("\n\n Invalid input \n\n");
        }

        return false;
    }

    public static int askForId(Scanner scanner) {
        int home = 0;
        System.out.println("\n Insert the home id you want to set for this home: ");
        String home_id = scanner.next();

        try {
            home = Integer.parseInt(home_id);
        }

        catch (Exception e) {
            System.out.println("\n\n Invalid input \n\n");
        }

        return home;
    }

    public static int askForPortNumber(Scanner scanner) {
        int portNumber = -1;
        System.out.println("\n Insert the port number you want to set for this home: ");
        String home_id = scanner.next();

        try {
            portNumber = Integer.parseInt(home_id);
        }

        catch (Exception e) {
            System.out.println("\n\n Invalid input \n\n");
        }

        return portNumber;
    }

    public static String askForIp(Scanner scanner) {
        String ip;
        System.out.println("\n Insert the ip you want to set for this home: ");
        ip = scanner.next().toLowerCase();

        if (!ip.equals("localhost")) {
            System.out.println("\n\n Invalid input \n\n");
            ip = "noIp";
        }

        return ip;
    }

    public static void callMain() {
        System.out.println("Reloading bash...");
        System.out.println("\n\n\n\n\n");

        try {
            main(new String[] {});
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Server startGrpcService(int port) {

        //faccio partire il servizio sulla porta inserita dall'utente
        try {
            Server server = ServerBuilder.forPort(port).addService(new CommunicationServiceImpl()).build();

            server.start();

            // RICEZIONE DEI MESSAGGI CHE CONTATTANO LA HOME SU QUESTA PORTA
            System.out.println("\n From this moment on, you are able to receive requests on port " + port + "...");

            //server.awaitTermination();

            return server;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //calling a synchronous rpc operation
    public static void synchronousCall(Home myHome, Home targetHome, int messageType) {
        Singleton_Home instance = Singleton_Home.getInstance();

        switch (messageType) {
            case 0: // HelloRequest
                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel helloChannel = ManagedChannelBuilder.forTarget(
                        targetHome.getIp() + ":" + targetHome.getNumeroPorta())
                        .usePlaintext(true).build();

                //creating a blocking stub on the channel
                CommunicationServiceBlockingStub helloStub = newBlockingStub(helloChannel);

                //creating the HelloRequest object which will be provided as input to the RPC method
                HelloRequest helloRequest = HelloRequest.newBuilder()
                        .setId(myHome.getId())
                        .setIp(myHome.getIp())
                        .setPort(myHome.getNumeroPorta())
                        .build();

                //calling the method. it returns an instance of HelloResponse

                try {
                    HelloResponse response = helloStub.contact(helloRequest);

                    //printing the answer
                    //System.out.println("\n Response: \n" + response);

                    // AGGIUNGO ALLA VISIONE DELLA RETE LE CASE CHE HANNO RISPOSTO A QUESTA CASA
                    Home peer = new Home(helloRequest.getId(), helloRequest.getIp(), helloRequest.getPort());
                    instance.addHomeToMyList(peer);
                }

                catch (StatusRuntimeException e) {
                    System.out.println("No response");

                    // CHI NON HA RISPOSTO VIENE TOLTO DALLA LISTA DELLE CASE
                    // (PERCHE' LE USCITE SONO CONTROLLATE)
                    // SE NON HA RISPOSTO SIGNIFICA CHE E' USCITO PRIMA CHE IL SERVER RESTITUISSE LA LISTA AGGIORNATA

                    int noResponseId = helloRequest.getId();
                    instance.removeHomeFromMyList(noResponseId);
                }

                //closing the channel
                helloChannel.shutdown();
                break;

            case 1: // Election
                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel electionChannel = ManagedChannelBuilder.forTarget(
                        targetHome.getIp() + ":" + targetHome.getNumeroPorta())
                        .usePlaintext(true).build();

                //creating a blocking stub on the channel
                CommunicationServiceBlockingStub electionStub = newBlockingStub(electionChannel);

                //creating the Election object which will be provided as input to the RPC method
                Election electionRequest = Election.newBuilder()
                        .setId(myHome.getId())
                        .setStatus("Election")
                        .build();

                //calling the method. it returns an instance of HelloResponse

                try {
                    Elected response = electionStub.election(electionRequest);

                    //printing the answer
                    //System.out.println("\n Response: \n" + response);

                }

                catch (StatusRuntimeException e) {
                    // TODO: GESTIRE CASI LIMITE
//                    // SE NON MI RISPONDE HO DUE CASI:
//                    // 1. SONO L'ULTIMO DELLA RETE, PERTANTO POSSO SMETTERE SUBITO DI ESSERE COORDINATORE E USCIRE
//                    // 2. IL CANDIDATO CON ID MAX E' USCITO DALLA RETE PROPRIO ADESSO, PER CUI RITENTO FINCHE' C'E' QUALCUNO NELLA RETE
//
//                    Singleton_Home instance = Singleton_Home.getInstance();
//
//                    HomesMap homesMap = instance.getMyVisionOfNetwork();
//
//                    if (homesMap.getHomes().size() > 1) {
//                        // CASO 2
//
//                        // RIMUOVO DALLA RETE CHI NON HA RISPOSTO
//                        Set<Integer> range = homesMap.getHomes().keySet();
//                        int max = Collections.max(range);
//                        Home electionCandidate = homesMap.getHomes().get(max);
//
//                    }

                    System.out.println("No response");
                }

                //closing the channel
                electionChannel.shutdown();
                break;

            case 2: // Exit
                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel exitChannel = ManagedChannelBuilder.forTarget(
                        targetHome.getIp() + ":" + targetHome.getNumeroPorta())
                        .usePlaintext(true).build();

                //creating a blocking stub on the channel
                CommunicationServiceBlockingStub exitStub = newBlockingStub(exitChannel);

                //creating the Exit object which will be provided as input to the RPC method
                Exit exitRequest = Exit.newBuilder()
                        .setId(myHome.getId())
                        .build();

                //calling the method. it returns an instance of HelloResponse

                try {
                    HelloResponse exitResponse = exitStub.exit(exitRequest);

                    //printing the answer
                    //System.out.println("\n Response: \n" + exitResponse);
                }

                catch (StatusRuntimeException e) {
                    System.out.println("No response");
                }

                //closing the channel
                exitChannel.shutdown();
                break;

            case 3: // LocalStat
                //plaintext channel on the address (ip/port) which offers the GreetingService service
                final ManagedChannel localStatChannel = ManagedChannelBuilder.forTarget(
                        targetHome.getIp() + ":" + targetHome.getNumeroPorta())
                        .usePlaintext(true).build();

                // INVIO
                Statistics myLocalStat = instance.getMyLocalStat(); // STATISTICA LOCALE CORRENTE APPENA CALCOLATA

                //creating a blocking stub on the channel
                CommunicationServiceBlockingStub localStatStub = newBlockingStub(localStatChannel);

                //creating the Measurement object which will be provided as input to the RPC method
                CommunicationServiceOuterClass.Measurement localStatRequest = CommunicationServiceOuterClass.Measurement.newBuilder()
                        .setValue(myLocalStat.getValue())
                        .setTimestamp(myLocalStat.getTimestamp())
                        .build();

                //calling the method. it returns an instance of LocalStatResponse

                try {
                    LocalStatsResponse response = localStatStub.localStat(localStatRequest);

                    //System.out.println("Risposta: " + response.getAck());
                }

                catch (StatusRuntimeException e) {
                    //System.out.println("No response");
                    e.printStackTrace();
                }

                //closing the channel
                localStatChannel.shutdown();
                break;

            default:
                break;
        }

    }

    public static void asynchronousCall(Home myHome, Home targetHome) {
        final ManagedChannel boostChannel = ManagedChannelBuilder.forTarget(
                        targetHome.getIp() + ":" + targetHome.getNumeroPorta())
                        .usePlaintext(true).build();

        CommunicationServiceStub boostStub = newStub(boostChannel);

        Singleton_Boost boostInstance = Singleton_Boost.getInstance();
        Singleton_Home homeInstance = Singleton_Home.getInstance();
        HashMap<Integer, Home> homesMap = homeInstance.getMyVisionOfNetwork();

        Long ts = System.currentTimeMillis();

        boostInstance.emptyBoostAcks();

        BoostRequest boostRequest = BoostRequest.newBuilder()
                .setId(myHome.getId())
                .setResource("Boost")
                .setTimestamp(ts)
                .build();

        boostInstance.setRequestedBoost(true);
        Booster booster = new Booster(myHome.getId(), ts);
        boostInstance.setMyBoostInfo(booster);

        boostStub.boost(boostRequest, new StreamObserver<BoostResponse>() {
            @Override
            public void onNext(BoostResponse boostResponse) {
                ArrayList<BoostResponse> boostAcks;

                boostInstance.addToBoostAcks(boostResponse);
                boostAcks = boostInstance.getBoostAcks();

                if (homesMap.size() == 1) {
                    try {
//                        System.out.println("\n Starting boost... \n");
//                        displayCurrentTime();
                        boostInstance.setBoosting(true);
                        boostInstance.setRequestedBoost(false);
                        boostInstance.emptyBoostAcks();
                        smartMeterSimulator.boost();

                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    boostInstance.finishToBoost();
//                    System.out.println("\n ...finished boost. \n");
//                    displayCurrentTime();
                    Singleton_Display.getInstance().nowYouCanDisplayMenu();
                }

                else {
                    if (boostAcks.size() == homesMap.size() - 1) {

                        if (boostInstance.wantsToBoost() && !boostInstance.isBoosting()) {

                            try {
                                System.out.println("\n Starting boost... \n");
//                                displayCurrentTime();
                                boostInstance.setBoosting(true);
                                boostInstance.setRequestedBoost(false);
                                boostInstance.emptyBoostAcks();
                                smartMeterSimulator.boost();

                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            boostInstance.finishToBoost();
                           System.out.println("\n ...finished boost. \n");
//                            displayCurrentTime();
                            Singleton_Display.getInstance().nowYouCanDisplayMenu();

                        }

                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                boostChannel.shutdown();
            }

        });

    }

    public static void displayCurrentTime() {
        Date date;
        Timestamp ts;
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        String dateFormatted;

        ts = new Timestamp(System.currentTimeMillis());
        date = new Date(ts.getTime());
        dateFormatted = df.format(date);
        System.out.println(dateFormatted);
    }

}
