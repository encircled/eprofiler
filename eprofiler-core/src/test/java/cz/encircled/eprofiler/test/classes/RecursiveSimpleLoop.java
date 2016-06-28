package cz.encircled.eprofiler.test.classes;

/**
 * @author Vlad on 25-Jun-16.
 */
public class RecursiveSimpleLoop {

    public void recursiveLoopStart(boolean doLoop) {
        if (doLoop) {
            recursiveLoopFirstNested(doLoop);
        } else {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void recursiveLoopFirstNested(boolean doLoop) {
        if (doLoop) {
            recursiveLoopSecondNested(doLoop);
        }
    }

    public void recursiveLoopSecondNested(boolean doLoop) {
        if (doLoop) {
            for (int i = 0; i < 50; i++) {
                recursiveLoopStart(false);
            }
        }
    }

}
