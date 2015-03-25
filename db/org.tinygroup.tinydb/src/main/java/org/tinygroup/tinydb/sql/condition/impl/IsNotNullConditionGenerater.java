package org.tinygroup.tinydb.sql.condition.impl;

import java.util.List;


/**
 * 非Null比较操作
 * @author renhui
 *
 */
public class IsNotNullConditionGenerater extends AbstractConditionGenerater {


	public String generateCondition(String columnName) {
		return columnName+" is not null ";
	}

	public String getConditionMode() {
		return "isNotNull";
	}

	@Override
	public void paramValueProcess(Object value, List<Object> params) {//不用加入参数
	}
}