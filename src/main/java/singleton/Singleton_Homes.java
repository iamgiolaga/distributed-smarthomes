package singleton;




import beans.Home;
//import beans.HomesMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Singleton_Homes {
    //@XmlElement(name = "homes")
    private HashMap<Integer, Home> homesList;
    private static Singleton_Homes homes;

    private Singleton_Homes() {
        homesList = new HashMap<>();
    }

    public static synchronized Singleton_Homes getInstance() {
        if (homes == null)
            homes = new Singleton_Homes();

        return homes;
    }

    public synchronized HashMap<Integer, Home> addHome(Home home) {

        boolean duplicateKey = false; // VARIABILE BOOLEANA VERA QUANDO SI VERIFICA UNA REGISTRAZIONE CON ID GIA' PRESENTE

        int home_id = home.getId();
        int portNumber = home.getNumeroPorta();

        // CASO IN CUI ESISTE GIA' UNA CASA CON QUELL'ID
        if (homesList.containsKey(home_id))
            duplicateKey = true;

        if (!homesList.isEmpty()) {
            for (HashMap.Entry<Integer, Home> homeEntry : homesList.entrySet()) {
                if (duplicateKey || homeEntry.getValue().getNumeroPorta() == portNumber) {
                    return null;
                }
            }
            homesList.put(home_id, home);

        }

        else {
            homesList.put(home_id, home);
        }

        return homesList;

    }

    public synchronized boolean removeHome(int home_id) {

        try {
            homesList.remove(home_id);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }

    public Home getHomeById(int home_id) {

        try {
            return homesList.get(home_id);
        }

        catch (Exception e) {
            return null;
        }

    }

    public HashMap<Integer, Home> getHomesList() {
        synchronized (homesList) {
            return new HashMap<>(homesList); // RESTITUISCE UNA COPIA
        }
    }

    public synchronized void setHomesList(HashMap<Integer, Home> homesList) {
        this.homesList = homesList;
    }
}
