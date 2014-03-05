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
package org.tinygroup.format.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 格式化正则表达式定义<br>
 * 通过格式定义可以修改匹配方式
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("format-pattern-define")
public class FormatPatternDefine {
	@XStreamAsAttribute
	@XStreamAlias("prefix-pattern-string")
	private String prefixPatternString;// 前置格式化串
	@XStreamAsAttribute
	@XStreamAlias("postfix-pattern-string")
	private String postfixPatternString;// 兵团格式化串
	@XStreamAsAttribute
	@XStreamAlias("pattern-string")
	private String patternString;// 匹配正则表达式

	public String getPrefixPatternString() {
		return prefixPatternString;
	}

	public void setPrefixPatternString(String prefixPatternString) {
		this.prefixPatternString = prefixPatternString;
	}

	public String getPostfixPatternString() {
		return postfixPatternString;
	}

	public void setPostfixPatternString(String postfixPatternString) {
		this.postfixPatternString = postfixPatternString;
	}

	public String getPatternString() {
		return patternString;
	}

	public void setPatternString(String patternString) {
		this.patternString = patternString;
	}

}
