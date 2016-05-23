package cz.encircled.eprofiler;

/**
 * @author Vlad on 23-May-16.
 */
public class Timer extends Thread {

    public static volatile long now;

    @Override
    public void run() {
        while (true) {
            now = System.currentTimeMillis();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
