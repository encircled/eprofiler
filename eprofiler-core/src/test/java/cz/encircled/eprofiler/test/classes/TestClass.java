package cz.encircled.eprofiler.test.classes;

/**
 * @author Vlad on 23-May-16.
 */
public class TestClass {

    static {
        testStatic();
    }

    public static void testStatic() {
        System.out.println("testStatic");
    }

    public void firstMethod() {
        System.out.println("qwe");
    }

    public String someMethod() {
        String ret = "returnVal";
        try {
            Thread.sleep(20);
            yetAnotherMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        anotherMethod("");
        return ret;
    }

    public void anotherMethod(String any) {
        try {
            Thread.sleep(20);
            yetAnotherMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void yetAnotherMethod() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
