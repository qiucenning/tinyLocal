package org.tinygroup.template.impl.convert;

import org.tinygroup.template.Converter;

/**
 * Created by luoguo on 2014/6/5.
 */
public class ByteInteger implements Converter<Byte, Integer> {

    public Integer convert(Byte object) {
        return (int) object.byteValue();
    }

    public String getType() {
        return "FloatDouble";
    }
}
