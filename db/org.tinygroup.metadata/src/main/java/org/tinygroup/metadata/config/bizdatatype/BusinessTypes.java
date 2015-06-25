/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
 */
package org.tinygroup.metadata.config.bizdatatype;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.metadata.config.BaseObject;

import java.util.List;

/**
 * 业务类型定义 
 * @author luoguo
 *
 */
@XStreamAlias("business-types")
public class BusinessTypes extends BaseObject {
	@XStreamAlias("package-name")
	@XStreamAsAttribute
	private String packageName;// 包名
	@XStreamImplicit
	private List<BusinessType> businessTypeList;// 业务类型列表

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<BusinessType> getBusinessTypeList() {
		return businessTypeList;
	}

	public void setBusinessTypeList(List<BusinessType> businessTypeList) {
		this.businessTypeList = businessTypeList;
	}

}
