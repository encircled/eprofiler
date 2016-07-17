package cz.encircled.eprofiler.ui.fx.components;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author Vlad on 16-Jul-16.
 */
public class LongFormat extends DecimalFormat {

    @Override
    public Object parseObject(String source) throws ParseException {
        return StringUtils.isEmpty(source) ? null : Long.parseLong(source);
    }

}
