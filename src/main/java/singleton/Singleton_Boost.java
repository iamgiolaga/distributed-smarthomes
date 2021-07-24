package singleton;

import beans.Booster;
import grpc.CommunicationServiceOuterClass;

import java.util.ArrayList;

public class Singleton_Boost {
    private ArrayList<CommunicationServiceOuterClass.BoostResponse> boostAcks; // ACK DI CHI NON STA USANDO O NON VUOLE USARE BOOST
    private Boolean boosting; // TRUE SE STA USANDO IL CONSUMO EXTRA
    private Boolean requestedBoost; // TRUE SE HA RICHIESTO IL CONSUMO EXTRA
    private Booster myBoostInfo; // CONTIENE IL MIO ID E IL TIMESTAMP DI QUANDO HO FATTO LA RICHIESTA
    private static Singleton_Boost instance;

    public Singleton_Boost() {
        boostAcks = new ArrayList<>();
        boosting = false;
        requestedBoost = false;
        myBoostInfo = new Booster();
    }

    public static synchronized Singleton_Boost getInstance() {
        if (instance == null)
            instance = new Singleton_Boost();

        return instance;
    }

    public synchronized void waitToAnswer() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void finishToBoost() {
        boosting = false;
        notifyAll();
    }

    public synchronized void addToBoostAcks(CommunicationServiceOuterClass.BoostResponse ack) {
        boostAcks.add(ack);
    }

    public ArrayList<CommunicationServiceOuterClass.BoostResponse> getBoostAcks() {
        synchronized (boostAcks) {
            return new ArrayList<>(boostAcks);
        }
    }

    public boolean isBoosting() {
        synchronized (boosting) {
            return new Boolean(boosting);
        }
    }

    public boolean wantsToBoost() {
        synchronized (requestedBoost) {
            return new Boolean(requestedBoost);
        }
    }

    public void setBoosting(Boolean boosting) {
        synchronized (this.boosting) {
            this.boosting = boosting;
        }
    }

    public void setRequestedBoost(Boolean requestedBoost) {
        synchronized (this.requestedBoost) {
            this.requestedBoost = requestedBoost;
        }
    }

    public void setBoostAcks(ArrayList<CommunicationServiceOuterClass.BoostResponse> boostAcks) {
        synchronized (this.boostAcks) {
            this.boostAcks = boostAcks;
        }
    }

    public void emptyBoostAcks() {
        synchronized (boostAcks) {
            boostAcks.clear();
        }
    }

    public Booster getMyBoostInfo() {
        synchronized (myBoostInfo) {
            return new Booster(myBoostInfo.getId(), myBoostInfo.getRequestTimestamp());
        }

    }

    public void setMyBoostInfo(Booster myBoostInfo) {
        synchronized (this.myBoostInfo) {
            this.myBoostInfo = myBoostInfo;
        }
    }
}
