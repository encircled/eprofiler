package cz.encircled.eprofiler.test.classes;

/**
 * @author Vlad on 25-Jun-16.
 */
public class RecursiveLoopWithNestedCall {

    public void loopStart() {
        for (int i = 0; i < 20; i++) {
            loopOuter();
        }
    }

    public void loopOuter() {
        firstLoopNested();
        secondLoopNested();
    }

    private void firstLoopNested() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void secondLoopNested() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
