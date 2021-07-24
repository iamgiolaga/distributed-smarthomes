package main.java.beans;

public class DevAndMean {
    private double stdDev;
    private double mean;

    public DevAndMean() {
    }

    public DevAndMean(double stdDev, double mean) {
        this.stdDev = stdDev;
        this.mean = mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }
}
