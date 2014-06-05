package org.tinygroup.template.impl.convert;

import org.tinygroup.template.Converter;

/**
 * Created by luoguo on 2014/6/5.
 */
public class ByteDouble implements Converter<Byte, Double> {

    public Double convert(Byte object) {
        return (double) object.byteValue();
    }

    public String getType() {
        return "Double2Double";
    }
}
