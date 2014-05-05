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
package org.tinygroup.entity;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.springutil.SpringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 功能说明:比较模式的容器对象 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-4 <br>
 * <br>
 */
public class CompareModeContain {
	
	public static final String COMPARE_MODE_CONTAIN="compareModeContain";

	private  Map<String, CompareMode> compareModeMap = new HashMap<String, CompareMode>();
	
	public CompareModeContain(){
		 Collection<CompareMode> compareModes=SpringUtil.getBeansOfType(CompareMode.class);
		 if(!CollectionUtil.isEmpty(compareModes)){
			 for (CompareMode compareMode : compareModes) {
				compareModeMap.put(compareMode.getCompareKey(), compareMode);
			}
		 }
	}


	public CompareMode getCompareMode(String compareModeName){
		return compareModeMap.get(compareModeName);
	}
	
	public void addCompareMode(String compareModeName,CompareMode compareMode){
		compareModeMap.put(compareModeName, compareMode);
	}
	
	
	
	
}
