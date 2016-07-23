package cz.encircled.eprofiler.test;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Vlad on 23-Jul-16.
 */
public class SpringContextListenerTest extends AbstractProfilerTest {

    @Test
    public void test() throws NoSuchMethodException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("cz.encircled.eprofiler");
    }

}
