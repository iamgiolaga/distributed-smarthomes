package singleton;


import beans.Statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Singleton_Statistics {
    @XmlElement(name = "statistics")

    private HashMap<Integer, ArrayList<Statistics>> localList;
    private ArrayList<Statistics> globalList;

    private static Singleton_Statistics statistics;

    private Singleton_Statistics() {
         localList = new HashMap<>();
         globalList = new ArrayList<>();

    }

    public static synchronized Singleton_Statistics getInstance() {
        if (statistics == null)
            statistics = new Singleton_Statistics();

        return statistics;
    }

    public boolean addLocalStat(Statistics stat, int home_id) {

        synchronized (localList) {
            try {
                localList.get(home_id).add(stat);
                return true;
            }

            catch (NullPointerException e) {
                return false;
            }
        }

    }

    public boolean addGlobalStat(Statistics stat) {

        synchronized (globalList) {
            try {
                globalList.add(stat);
                return true;
            }

            catch (Exception e) {
                return false;
            }
        }

    }

    public HashMap<Integer, ArrayList<Statistics>> getLocalList() {
        synchronized (localList) {
            return new HashMap<>(localList);
        }
    }

    public void setLocalList(HashMap<Integer, ArrayList<Statistics>> localList) {
        synchronized (localList) {
            this.localList = localList;
        }
    }

    public ArrayList<Statistics> getGlobalList() {
        synchronized (globalList) {
            return new ArrayList<>(globalList);
        }
    }

    public void setGlobalList(ArrayList<Statistics> globalList) {
        synchronized (globalList) {
            this.globalList = globalList;
        }
    }

    public boolean removeLocalStats(int home_id) {

        synchronized (localList) {
            try {
                localList.remove(home_id);
                return true;
            }

            catch (Exception e) {
                return false;
            }
        }

    }

    public double mean(List<Double> dataset) {

        double sum = 0;

        for (int i = 0; i < dataset.size(); i++) {
            sum += dataset.get(i);
        }

        return sum / (double) dataset.size();

    }

    public double stdDev(List<Double> dataset) {

        double mean = mean(dataset);
        double stdDev = 0.0;
        List<Double> array = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            array.add(i, Math.pow(dataset.get(i) - mean, 2)); // (X - E(X))^2
            stdDev += array.get(i); // sommatoria
        }

        return Math.sqrt(stdDev / (double) dataset.size()); // Rad quadrata della sommatoria diviso n

    }
}
