/**
 *  Copyright (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * --------------------------------------------------------------------------
 *  版权 (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  本开源软件遵循 GPL 3.0 协议;
 *  如果您不遵循此协议，则不被允许使用此文件。
 *  你可以从下面的地址获取完整的协议文本
 *
 *       http://www.gnu.org/licenses/gpl.html
 */
package org.tinygroup.mongodb.engine.comparemode.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import org.bson.BSONObject;
import org.tinygroup.context.Context;

import java.util.regex.Pattern;

/**
 * 
 * 功能说明: 不包含值条件
 * <p>

 * 开发人员: wanggf <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public class NotContainsWithCompareMode extends AbstractNeedValueCompareMode {

	public BSONObject generateBSONObject(String propertyName, Object value, Context context) {
		Pattern pattern = Pattern.compile("^.*" + value + ".*$",
				Pattern.MULTILINE);
		BSONObject bsonObject= new BasicDBObject("$regex", pattern);
		return  new BasicDBObject(propertyName,new BasicDBObject(QueryOperators.NE, bsonObject));
	}

	public String getCompareKey() {
		return "notContainsWith";
	}
}
