package grpc;

import beans.*;
import grpc.CommunicationServiceGrpc.CommunicationServiceImplBase;
import grpc.CommunicationServiceOuterClass.*;
import io.grpc.stub.StreamObserver;
import singleton.Singleton_Boost;
import singleton.Singleton_Home;

import java.util.ArrayList;

public class CommunicationServiceImpl extends CommunicationServiceImplBase {

    private Singleton_Home homeInstance = Singleton_Home.getInstance();
    private Singleton_Boost boostInstance = Singleton_Boost.getInstance();

    @Override
    public void contact(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        int id = request.getId();
        String ip = request.getIp();
        int port = request.getPort();

        Home home = new Home(id, ip, port);

        homeInstance.addHomeToMyList(home);

        int myId = homeInstance.getMyHome().getId();

        //la richiesta è di tipo HelloRequest (definito in .src.main.resources.proto)
        //System.out.println("\n Request: \n" + request);

        //costruisco la risposta di tipo HelloResponse (sempre definito in .src.main.resources.proto)
        HelloResponse response = HelloResponse.newBuilder()
                .setId(myId)
                .setAck(true)
                .build();

        // OGNI VOLTA CHE RICEVO UN MESSAGGIO RISTAMPO LA NETWORK PER RILEVARE EVENTUALI CAMBIAMENTI
        //System.out.println("\n Network's current state: ");
        //HomesMap homesMap = Singleton_Home.getInstance().getMyVisionOfNetwork();
        //displayNetwork(homesMap);

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void exit(Exit request, StreamObserver<HelloResponse> responseObserver) {
        int id = request.getId();
        boolean boostAck = false;

        homeInstance.removeHomeFromMyList(id);
        ArrayList<BoostResponse> boostAcks = boostInstance.getBoostAcks();

        for (BoostResponse b : boostAcks) { // POTREBBE NON ESSERE ARRIVATO L'ACK DA UNA CASA USCENTE
            if (b.getId() == request.getId()) {
                boostAck = true;
                break;
            }
        }

        if (!boostAck) {
            BoostResponse boostResponse = BoostResponse.newBuilder()
                    .setId(request.getId())
                    .setAck(true)
                    .build();

            boostInstance.addToBoostAcks(boostResponse);
        }

        int myId = homeInstance.getMyHome().getId();

        //la richiesta è di tipo Exit (definito in .src.main.resources.proto)
        //System.out.println("\n Request: \n" + request);

        //costruisco la risposta di tipo HelloResponse (sempre definito in .src.main.resources.proto)
        HelloResponse response = HelloResponse.newBuilder()
                .setId(myId)
                .setAck(true)
                .build();

        // OGNI VOLTA CHE RICEVO UN MESSAGGIO RISTAMPO LA NETWORK PER RILEVARE EVENTUALI CAMBIAMENTI
        //System.out.println("\n Network's current state: ");
        //HomesMap homesMap = Singleton_Home.getInstance().getMyVisionOfNetwork();
        //displayNetwork(homesMap);

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void localStat(Measurement request, StreamObserver<LocalStatsResponse> responseObserver) {

        Statistics receivedLocalStat = new Statistics(request.getValue(), request.getTimestamp());

        homeInstance.addLocalStatToAllLocalStats(receivedLocalStat);

        //costruisco la risposta di tipo LocalStatsResponse (sempre definito in .src.main.resources.proto)
        LocalStatsResponse response = LocalStatsResponse.newBuilder()
                .setAck(true)
                .build();

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();
    }

    @Override
    public void election(Election request, StreamObserver<Elected> responseObserver) {

        int myId = homeInstance.getMyHome().getId();
        int requestId = request.getId();

        //la richiesta è di tipo Election (definito in .src.main.resources.proto)
        //System.out.println("\n Request: \n" + request);

        //costruisco la risposta di tipo Elected (sempre definito in .src.main.resources.proto)
        Elected response = Elected.newBuilder()
                .setId(myId)
                .setStatus("Elected")
                .setAck(true)
                .build();

        homeInstance.setCoordinator(true);

        //passo la risposta nello stream
        responseObserver.onNext(response);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

    @Override
    public void boost(BoostRequest request, StreamObserver<BoostResponse> responseObserver) {

        Booster myInfoAboutBoosting = boostInstance.getMyBoostInfo();

        int id = homeInstance.getMyHome().getId();
        BoostResponse boostResponse;

        if (boostInstance.isBoosting()) { // SE STO USANDO LA RISORSA

            boostInstance.waitToAnswer();

            boostResponse = BoostResponse.newBuilder()
                    .setId(id)
                    .setAck(true)
                    .build();
        }

        else if (boostInstance.wantsToBoost() && myInfoAboutBoosting.getRequestTimestamp() < request.getTimestamp()) {
            // SE NON STO USANDO LA RISORSA, MA L'HO CHIESTA E LA MIA RICHIESTA E' MENO RECENTE

            boostInstance.waitToAnswer();

            boostResponse = BoostResponse.newBuilder()
                    .setId(id)
                    .setAck(true)
                    .build();
        }

        else { // SE NON LA STO USANDO E NON L'HO RICHIESTA
            boostResponse = BoostResponse.newBuilder()
                    .setId(id)
                    .setAck(true)
                    .build();
        }

        //passo la risposta nello stream
        responseObserver.onNext(boostResponse);

        //completo e finisco la comunicazione
        responseObserver.onCompleted();

    }

}
