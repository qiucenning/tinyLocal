/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.metadata.bizdatatype;

import org.tinygroup.metadata.config.bizdatatype.BusinessType;
import org.tinygroup.metadata.config.bizdatatype.BusinessTypes;
import org.tinygroup.metadata.stddatatype.StandardTypeProcessor;

public interface BusinessTypeProcessor {

	String getType(String id, String language);

	void addBusinessTypes(BusinessTypes businessTypes);

	BusinessType getBusinessTypes(String id);

	void removeBusinessTypes(BusinessTypes businessTypes);

	StandardTypeProcessor getStandardTypeProcessor();

	void setStandardTypeProcessor(
			StandardTypeProcessor standardDataTypeProcessor);
}
