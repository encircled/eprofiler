package cz.encircled.eprofiler.test;

import cz.encircled.eprofiler.core.ProfileConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vlad on 25-May-16.
 */
public class ConfigTest {

    @Test(expected = RuntimeException.class)
    public void testNullArgs() {
        new ProfileConfiguration(null);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongArgFormat() {
        new ProfileConfiguration("classPattern=some=value;debug");
    }

    @Test(expected = RuntimeException.class)
    public void testClassPatternMissing() {
        new ProfileConfiguration("debug;");
    }

    @Test(expected = RuntimeException.class)
    public void testClassPatternNotSet() {
        new ProfileConfiguration("classPattern=");
    }

    @Test
    public void testClassPattern() {
        assertClassPatternBasicTest(new ProfileConfiguration("classPattern=cz.eprofiler"), "cz.eprofiler");

        ProfileConfiguration c2 = new ProfileConfiguration("classPattern=cz.eprofiler;");
        Assert.assertEquals(1, c2.getClassNamePatterns().size());
        Assert.assertEquals("cz.eprofiler", c2.getClassNamePatterns().get(0));

        ProfileConfiguration c3 = new ProfileConfiguration("debug;classPattern=org.some.*");
        Assert.assertEquals(1, c3.getClassNamePatterns().size());
        Assert.assertEquals("org.some.*", c3.getClassNamePatterns().get(0));

        ProfileConfiguration c4 = new ProfileConfiguration("debug;classPattern=org.some.*;");
        Assert.assertEquals(1, c4.getClassNamePatterns().size());
        Assert.assertEquals("org.some.*", c4.getClassNamePatterns().get(0));
    }

    @Test
    public void testMultipleClassPattern() {
        assertClassPatternBasicTest(new ProfileConfiguration("classPattern=cz.eprofiler;debug;classPattern=ex.ample;"), "cz.eprofiler", "ex.ample");
        assertClassPatternBasicTest(new ProfileConfiguration("classPattern=cz.eprofiler;debug;classPattern=ex.ample;"), "cz.eprofiler", "ex.ample");
        assertClassPatternBasicTest(new ProfileConfiguration("classPattern=cz.eprofiler;classPattern=ex.ample"), "cz.eprofiler", "ex.ample");
        assertClassPatternBasicTest(new ProfileConfiguration("debug;classPattern=cz.eprofiler;classPattern=ex.ample"), "cz.eprofiler", "ex.ample");
    }

    private void assertClassPatternBasicTest(ProfileConfiguration conf, String... patterns) {
        Assert.assertEquals(patterns.length, conf.getClassNamePatterns().size());
        for (int i = 0; i < patterns.length; i++) {
            Assert.assertEquals(patterns[i], conf.getClassNamePatterns().get(i));
        }
    }

    @Test
    public void testDebug() {
        Assert.assertTrue(new ProfileConfiguration("classPattern=some;debug;").isDebug());
        Assert.assertTrue(new ProfileConfiguration("classPattern=some;debug").isDebug());
        Assert.assertTrue(new ProfileConfiguration("debug;classPattern=some;").isDebug());
        Assert.assertTrue(new ProfileConfiguration("debug;classPattern=some").isDebug());
    }

    @Test
    public void testMinDuration() {
        Assert.assertEquals(10L, new ProfileConfiguration("classPattern=some;").getMinDurationToLog());
        Assert.assertEquals(11L, new ProfileConfiguration("classPattern=some;minDurationToLog=11").getMinDurationToLog());
        Assert.assertEquals(11L, new ProfileConfiguration("classPattern=some;minDurationToLog=11;").getMinDurationToLog());
        Assert.assertEquals(12L, new ProfileConfiguration("minDurationToLog=12;classPattern=some;").getMinDurationToLog());
    }

}
