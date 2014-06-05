package org.tinygroup.template.impl.operator;

import org.tinygroup.template.JetTemplateException;
import org.tinygroup.template.O;
import org.tinygroup.template.Operator;

/**
 * Created by luoguo on 2014/6/5.
 */
public abstract class AbstractOperator implements Operator {
    protected boolean isType(Object left,String type) {
        return left.getClass().getSimpleName().equals(type);
    }

    public Object operation(Object... parameter) throws JetTemplateException {
        if (parameter == null || parameter.length != 2) {
            throw new JetTemplateException();
        }
        Object left = parameter[0];
        Object right = parameter[1];
        String typeLeft = left.getClass().getSimpleName();
        String typeRight = right.getClass().getSimpleName();
        if (O.isNumberic(typeLeft) && O.isNumberic(typeRight)) {
            //如果两个都是数字类型
            return operation(left, right, typeLeft, typeRight);
        } else {
            return left.toString() + right.toString();
        }
    }

    private Object operation(Object left, Object right, String type1, String type2) {
        if (!type1.equals(type2)) {
            if (O.compare(type1, type2) > 0) {
                right = O.convert(right, type2, type1);
            } else {
                left = O.convert(left, type1, type2);
            }
        }
        return operation(left, right);
    }

    protected abstract Object operation(Object left, Object right) ;

}
