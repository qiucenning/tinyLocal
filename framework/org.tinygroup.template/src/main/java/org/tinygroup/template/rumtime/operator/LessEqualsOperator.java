package org.tinygroup.template.rumtime.operator;

/**
 * Created by luoguo on 2014/6/5.
 */
public class LessEqualsOperator extends TwoConvertOperator {


    protected Object operation(Object left, Object right) {
        if(left instanceof Comparable&&right instanceof Comparable){
            return ((Comparable) left).compareTo(right)<=0;
        }
        throw getUnsupportedOperationException(left,right);
    }

    public String getOperation() {
        return "<=";
    }

}
