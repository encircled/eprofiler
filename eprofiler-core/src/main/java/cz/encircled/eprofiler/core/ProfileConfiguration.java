package cz.encircled.eprofiler.core;

import cz.encircled.eprofiler.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad on 23-May-16.
 */
public class ProfileConfiguration {

    private List<String> classNamePatterns = new ArrayList<>(2);

    private boolean isDebug;

    private boolean isShowBytecode;

    public ProfileConfiguration(String args) {
        init(args);
        validate();
    }

    private void init(String args) {
        if (args != null) {
            for (String fullParam : args.split(";")) {
                String[] keyAndVal = fullParam.split("=");
                switch (keyAndVal.length) {
                    case 1:
                        applyParameter(keyAndVal[0], null);
                        break;
                    case 2:
                        applyParameter(keyAndVal[0], keyAndVal[1]);
                        break;
                    default:
                        throw new RuntimeException("Invalid argument " + fullParam);
                }
            }
        }
    }

    private void applyParameter(String key, String value) {
        switch (key) {
            case "classPattern":
                if (Util.isNotEmpty(value)) {
                    classNamePatterns.add(value);
                }
                break;
            case "debug":
                isDebug = true;
                break;
            case "showBytecode":
                isShowBytecode = true;
                break;
        }
    }

    public boolean isShowBytecode() {
        return isShowBytecode;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public List<String> getClassNamePatterns() {
        return classNamePatterns;
    }

    private void validate() {
        if (classNamePatterns.isEmpty()) {
            throw new RuntimeException("Class name pattern must be not null");
        }
    }

}
