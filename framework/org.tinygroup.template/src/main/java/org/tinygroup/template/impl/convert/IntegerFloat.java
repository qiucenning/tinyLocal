package org.tinygroup.template.impl.convert;

import org.tinygroup.template.Converter;

/**
 * Created by luoguo on 2014/6/5.
 */
public class IntegerFloat implements Converter<Integer,Float> {
    public Float convert(Integer object) {
        return object.floatValue();
    }

    public String getType() {
        return "IntegerFloat";
    }
}
