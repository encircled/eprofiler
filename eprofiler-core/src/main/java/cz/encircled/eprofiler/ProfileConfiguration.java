package cz.encircled.eprofiler;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfileConfiguration {

    private String classNamePattern = "cz\\..*";

    private boolean isDebug;

    public ProfileConfiguration() {

    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(final String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    public void validate() {
        if (classNamePattern == null) {
            throw new IllegalStateException("Class name pattern must be not null");
        }
        System.out.println("Conf: class pattern " + classNamePattern);
    }

}
