package org.tinygroup.tinydb.sql.condition.impl;

import java.util.List;


/**
 * 非空比较操作
 * @author renhui
 *
 */
public class IsNullConditionGenerater extends AbstractConditionGenerater {


	public String generateCondition(String columnName) {
		return columnName+" is null ";
	}

	public String getConditionMode() {
		return "isNull";
	}

	@Override
	public void paramValueProcess(List<Object> params) {//不用加入参数
	}
}
