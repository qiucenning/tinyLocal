package org.tinygroup.template.impl.convert;

import org.tinygroup.template.Converter;

import java.math.BigDecimal;

/**
 * Created by luoguo on 2014/6/5.
 */
public class IntegerBigDecimal implements Converter<Integer,BigDecimal> {
    public BigDecimal convert(Integer object) {
        return new BigDecimal(object);
    }

    public String getType() {
        return "IntegerBigDecimal";
    }
}
