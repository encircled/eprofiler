package cz.encircled.eprofiler.test;

/**
 * @author Vlad on 23-May-16.
 */
public class TestClass {

    static {
        testStatic();
    }

    private static void testStatic() {
        System.out.println("testStatic");
    }

    public void firstMethod() {
        System.out.println("qwe");
    }

    public String someMethod() {
        System.out.println("SomeMethod");
        String ret = "returnVal";
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        anotherMethod();
        return ret;
    }

    public void anotherMethod() {
        System.out.println("AnotherMethod");
        try {
            Thread.sleep(2);
            System.out.println("AnotherMethodAfterSleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
