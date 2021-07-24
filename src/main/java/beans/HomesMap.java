//package beans;
//
//import java.util.HashMap;
//
//public class HomesMap {
//    private HashMap<Integer, Home> homes;
//
//    public HomesMap() {
//    }
//
//    public HomesMap(HashMap<Integer, Home> homes) {
//        synchronized (this.homes) {
//            this.homes = homes;
//        }
//    }
//
//    public synchronized HashMap<Integer, Home> getHomes() {
//        return homes;
//    }
//
//    public void setHomes(HashMap<Integer, Home> homes) {
//        this.homes = homes;
//    }
//}
