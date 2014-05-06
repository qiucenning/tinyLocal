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
package org.tinygroup.validate.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.validate.ErrorDescription;
import org.tinygroup.validate.ValidateResult;

/**
 * 验证结果实现类
 * @author renhui
 *
 */
public class ValidateResultImpl implements ValidateResult {
	private List<ErrorDescription> errorDescriptionList = new ArrayList<ErrorDescription>();

	public void addError(String name, String errorInfo) {
		errorDescriptionList.add(new ErrorDescription(name, errorInfo));

	}

	public void addError(ErrorDescription errorDescription) {
		errorDescriptionList.add(errorDescription);
	}

	public List<ErrorDescription> getErrorList() {
		return errorDescriptionList;
	}

	public boolean hasError() {
		return errorDescriptionList.size() > 0;
	}

	
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append("{");
		for (int i = 0; i < errorDescriptionList.size(); i++) {
			if(i>0){
				builder.append(";");
				builder.append("\n");
			}
			ErrorDescription errorDescription=errorDescriptionList.get(i);
			builder.append(errorDescription);
		}
		builder.append("}");
		return builder.toString();
		
	}
	

}
