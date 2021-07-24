package beans;

public class Statistics {
    private double value;
    private long timestamp;

    public Statistics() {
    }

    public Statistics(double value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t value:" + value +
                ", \n\t timestamp:" + timestamp +
                "\n}";
    }
}






