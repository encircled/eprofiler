package cz.encircled.eprofiler.ui.fx.components;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vlad on 25-Jun-16.
 */
public class NumberTextField extends TextField {

    private Matcher matcher = Pattern.compile("\\d").matcher("");

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    public void bindBidirectional(Property<? extends Number> other) {
        textProperty().bindBidirectional(other, new LongFormat());
    }

    private boolean validate(String text) {
        return StringUtils.isEmpty(text) || matcher.reset(text).matches();
    }

}
