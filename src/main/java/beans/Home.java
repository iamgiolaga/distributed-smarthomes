package beans;

public class Home {

    private int id;
    private String ip;
    private int numeroPorta;

    public Home() {
    }

    public Home(int id, String ip, int numeroPorta) {
        this.id = id;
        this.ip = ip;
        this.numeroPorta = numeroPorta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getNumeroPorta() {
        return numeroPorta;
    }

    public void setNumeroPorta(int numeroPorta) {
        this.numeroPorta = numeroPorta;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t id:" + id +
                ",\n\t ip:'" + ip + '\'' +
                ",\n\t numeroPorta:" + numeroPorta +
                "\n}";
    }
}
