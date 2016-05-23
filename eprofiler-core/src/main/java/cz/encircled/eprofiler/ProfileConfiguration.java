package cz.encircled.eprofiler;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfileConfiguration {

    private String classNamePattern = "cz\\..*";

    public String getClassNamePattern() {
        return classNamePattern;
    }

    public void validate() {
        if (classNamePattern == null) {
            throw new IllegalStateException("Class name pattern must be not null");
        }
    }

}
