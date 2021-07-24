package beans;

public class Booster {
    private int id;
    private long requestTimestamp;

    public Booster() {
    }

    public Booster(int id, long requestTimestamp) {
        this.id = id;
        this.requestTimestamp = requestTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }
}
