package org.tinygroup.tinydb.sql.condition.impl;


/**
 * 字符串长度操作
 * @author renhui
 *
 */
public class LengthNotEqualsConditionGenerater extends AbstractConditionGenerater {

	public String generateCondition(String columnName) {
		return "length("+columnName+")!=?";
	}

	public String getConditionMode() {
		return "lengthNotEquals";
	}

}
