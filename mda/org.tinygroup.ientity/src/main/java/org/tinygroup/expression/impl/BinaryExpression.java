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
package org.tinygroup.expression.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.expression.Expression;

/**
 * 
 * 功能说明:二元运算符的抽象类 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-14 <br>
 * <br>
 */
public abstract class BinaryExpression extends AbstractSqlExpression {
	@XStreamAlias("left-expression")
	private Expression left;
	@XStreamAlias("right-expression")
	private Expression right;
	
	public BinaryExpression() {
		super();
	}
	public BinaryExpression(Expression left,Expression right) {
		super();
		this.left=left;
        this.right=right;	
	}
	public Expression getLeft() {
		return left;
	}
	public Expression getRight() {
		return right;
	}
	public void setLeft(Expression left) {
		this.left = left;
	}
	public void setRight(Expression right) {
		this.right = right;
	}
	
}