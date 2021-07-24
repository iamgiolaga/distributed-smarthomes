package singleton;

import beans.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Singleton_Home {

    private Home myHome; // ISTANZA DELLA CLASSE LE CUI INFORMAZIONI SONO SALVATE IN QUESTO SINGLETON
    private HashMap<Integer, Home> myVisionOfNetwork;
    private Statistics myLocalStat; // LE STATISTICHE LOCALI
    private Statistics globalStat; // LA STATISTICA GLOBALE
    private ArrayList<Statistics> allLocalStats; // LE STATISTICHE LOCALI ACCUMULATE PER CALCOLARE GLOBALE
    private Boolean coordinator; // TRUE SE E' IL COORDINATORE
    private static Singleton_Home instance;

    private Singleton_Home() {
        myHome = new Home();
        myVisionOfNetwork = new HashMap<>();
        myLocalStat = new Statistics();
        globalStat = new Statistics();
        allLocalStats = new ArrayList<>();
        coordinator = false;
    }

    public static synchronized Singleton_Home getInstance() {
        if (instance == null)
            instance = new Singleton_Home();

        return instance;
    }

    public Home getMyHome() {
        synchronized (myHome) {
            return new Home(myHome.getId(), myHome.getIp(), myHome.getNumeroPorta());
        }
    }

    public void setMyHome(Home myHome) {
        synchronized (this.myHome) {
            this.myHome = myHome;
        }
    }

    public HashMap<Integer, Home> getMyVisionOfNetwork() {
        synchronized (myVisionOfNetwork) {
            return new HashMap<>(myVisionOfNetwork);
        }
    }

    public void setMyVisionOfNetwork(HashMap<Integer, Home> myVisionOfNetwork) {
        synchronized (this.myVisionOfNetwork) {
            this.myVisionOfNetwork = myVisionOfNetwork;
        }
    }

    public void addHomeToMyList(Home home) {
        synchronized (myVisionOfNetwork) {
            this.myVisionOfNetwork.put(home.getId(), home);
        }
    }

    public void removeHomeFromMyList(int idToRemove) {
        synchronized (myVisionOfNetwork) {
            this.myVisionOfNetwork.remove(idToRemove);
        }
    }

    public boolean isCoordinator() {
        synchronized (coordinator) {
            return new Boolean(coordinator);
        }
    }

    public void setCoordinator(boolean coordinator) {
        synchronized (this.coordinator) {
            this.coordinator = coordinator;
        }
    }

    public Statistics getMyLocalStat() {
        synchronized (myLocalStat) {
            return new Statistics(myLocalStat.getValue(), myLocalStat.getTimestamp());
        }
    }

    public void setMyLocalStat(Statistics myLocalStat) {
        synchronized(this.myLocalStat) {
            this.myLocalStat = myLocalStat;
        }
    }

//    public Statistics getGlobalStat() {
//        synchronized (globalStat) {
//            try {
//                globalStat.wait(); // SERVE PER QUANDO CASA ESCE E ASPETTA A FARLO PER INVIARE L'ULTIMA GLOBALE DISPONIBILE
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return new Statistics(globalStat.getValue(), globalStat.getTimestamp());
//        }
//
//    }

//    public void setGlobalStat(Statistics globalStat) {
//        synchronized (this.globalStat) {
//            this.globalStat = globalStat;
//            this.globalStat.notify();
//        }
//    }

    public void setGlobalStat(Statistics globalStat) {
        synchronized(this.globalStat) {
            this.globalStat = globalStat;
        }
    }

    public synchronized ArrayList<Statistics> getAllLocalStats() {

        while (allLocalStats.size() < myVisionOfNetwork.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>(allLocalStats);
    }

    public synchronized void setAllLocalStats(ArrayList<Statistics> allLocalStats) {
        this.allLocalStats = allLocalStats;
    }

    public synchronized void addLocalStatToAllLocalStats(Statistics localStat) {

        allLocalStats.add(localStat);

        if (allLocalStats.size() == myVisionOfNetwork.size())
            notify();

    }

}
