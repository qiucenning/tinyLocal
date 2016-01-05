package org.tinygroup.dbrouter.impl.shardrule;

import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.parser.SqlParserResult;
import org.tinygroup.dbrouter.parser.base.ColumnInfo;
import org.tinygroup.dbrouter.parser.base.Condition;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 具有sql解析结果对象的分片规则
 * @author renhui
 *
 */
public class DefaultParserResultShardRule extends AbstractParserResultShardRule {
	
	@XStreamAlias("expression")
	private String expression;
	

	@Override
	protected boolean internalMatch(Partition partition, Shard shard,
			SqlParserResult parserResult) {
		if(parserResult.isDDL()){//如果是ddl语句，直接返回true，匹配此分片
			return true;
		}
        List<Condition> conditions=parserResult.getConditions();
        if(CollectionUtil.isEmpty(conditions)){//没有参数信息的sql，也认为匹配此分片
        	return true;
        }
        //TODO 组装参数map,作为groovy函数的参数，根据expression创建groovy编写的class代码
		for (Condition condition : conditions) {
			ColumnInfo columnInfo=condition.getColumn();
		}
		return false;
	}


	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}
	

}
