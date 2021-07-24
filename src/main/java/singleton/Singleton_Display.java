package singleton;

public class Singleton_Display {
    // QUESTO SINGLETON VIENE USATO PER METTERE IN WAIT IL THREAD FINCHE' NON HA FINITO IL BOOST PRIMA
    // DI RIMOSTRARE IL MENU DI BOOST O LOGOUT
    private static Singleton_Display instance;

    public Singleton_Display() {

    }

    public static Singleton_Display getInstance() {
        if (instance == null)
            instance = new Singleton_Display();

        return instance;
    }

    public synchronized void waitToDisplay() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void nowYouCanDisplayMenu() {
        notify();
    }
}
