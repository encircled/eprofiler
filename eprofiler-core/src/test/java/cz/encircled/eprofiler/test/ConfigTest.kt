package cz.encircled.eprofiler.test

import cz.encircled.eprofiler.core.ProfileConfiguration
import org.junit.Assert
import org.junit.Test

/**
 * @author Vlad on 25-May-16.
 */
class ConfigTest {

    @Test(expected = RuntimeException::class)
    fun testNullArgs() {
        ProfileConfiguration(null)
    }

    @Test(expected = RuntimeException::class)
    fun testWrongArgFormat() {
        ProfileConfiguration("classPattern=some=value;debug")
    }

    @Test(expected = RuntimeException::class)
    fun testClassPatternMissing() {
        ProfileConfiguration("debug;")
    }

    @Test(expected = RuntimeException::class)
    fun testClassPatternNotSet() {
        ProfileConfiguration("classPattern=")
    }

    @Test
    fun testClassPattern() {
        assertClassPatternBasicTest(ProfileConfiguration("classPattern=cz.eprofiler"), "cz.eprofiler")

        val c2 = ProfileConfiguration("classPattern=cz.eprofiler;")
        Assert.assertEquals(1, c2.getClassNamePatterns().size.toLong())
        Assert.assertEquals("cz.eprofiler", c2.getClassNamePatterns()[0])

        val c3 = ProfileConfiguration("debug;classPattern=org.some.*")
        Assert.assertEquals(1, c3.getClassNamePatterns().size.toLong())
        Assert.assertEquals("org.some.*", c3.getClassNamePatterns()[0])

        val c4 = ProfileConfiguration("debug;classPattern=org.some.*;")
        Assert.assertEquals(1, c4.getClassNamePatterns().size.toLong())
        Assert.assertEquals("org.some.*", c4.getClassNamePatterns()[0])
    }

    @Test
    fun testMultipleClassPattern() {
        assertClassPatternBasicTest(ProfileConfiguration("classPattern=cz.eprofiler;debug;classPattern=ex.ample;"), "cz.eprofiler", "ex.ample")
        assertClassPatternBasicTest(ProfileConfiguration("classPattern=cz.eprofiler;debug;classPattern=ex.ample;"), "cz.eprofiler", "ex.ample")
        assertClassPatternBasicTest(ProfileConfiguration("classPattern=cz.eprofiler;classPattern=ex.ample"), "cz.eprofiler", "ex.ample")
        assertClassPatternBasicTest(ProfileConfiguration("debug;classPattern=cz.eprofiler;classPattern=ex.ample"), "cz.eprofiler", "ex.ample")
    }

    private fun assertClassPatternBasicTest(conf: ProfileConfiguration, vararg patterns: String) {
        Assert.assertEquals(patterns.size.toLong(), conf.getClassNamePatterns().size.toLong())
        for (i in patterns.indices) {
            Assert.assertEquals(patterns[i], conf.getClassNamePatterns()[i])
        }
    }

    @Test
    fun testDebug() {
        Assert.assertTrue(ProfileConfiguration("classPattern=some;debug;").isDebug)
        Assert.assertTrue(ProfileConfiguration("classPattern=some;debug").isDebug)
        Assert.assertTrue(ProfileConfiguration("debug;classPattern=some;").isDebug)
        Assert.assertTrue(ProfileConfiguration("debug;classPattern=some").isDebug)
    }

    @Test
    fun testMinDuration() {
        Assert.assertEquals(10L, ProfileConfiguration("classPattern=some;").minDurationToLog)
        Assert.assertEquals(11L, ProfileConfiguration("classPattern=some;minDurationToLog=11").minDurationToLog)
        Assert.assertEquals(11L, ProfileConfiguration("classPattern=some;minDurationToLog=11;").minDurationToLog)
        Assert.assertEquals(12L, ProfileConfiguration("minDurationToLog=12;classPattern=some;").minDurationToLog)
    }

}
