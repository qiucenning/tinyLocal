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
package org.tinygroup.tinydb.convert;

import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.exception.TinyDbException;

/**
 * 表配置信息转化接口
 * @author renhui
 *
 */
public interface TableConfigConvert {
    /**
     * 表配置信息转换接口  
     * @return
     */
	public void convert()throws TinyDbException;
	
	public void setOperatorManager(BeanOperatorManager manager);
	
	public BeanOperatorManager getOperatorManager();
}
