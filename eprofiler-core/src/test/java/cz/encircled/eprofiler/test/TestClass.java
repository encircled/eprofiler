package cz.encircled.eprofiler.test;

/**
 * @author Vlad on 23-May-16.
 */
public class TestClass {

    public static long testStatic;

    public void someMethod() {
        System.out.println("SomeMethod");
        anotherMethod();
    }

    public void anotherMethod() {
        System.out.println("AnotherMethod");
        try {
            Thread.sleep(2000);
            System.out.println("AnotherMethodAfterSleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
