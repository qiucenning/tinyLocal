package org.tinygroup.template.rumtime.operator;

/**
 * Created by luoguo on 2014/6/5.
 */
public class NotEqualsOperator extends TwoOperator {


    protected Object operation(Object left, Object right) {
        return !left.equals(right);
    }


    public String getOperation() {
        return "!=";
    }

}
