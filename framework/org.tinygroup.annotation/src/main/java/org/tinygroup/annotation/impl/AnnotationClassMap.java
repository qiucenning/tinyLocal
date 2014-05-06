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
package org.tinygroup.annotation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注解类规范映射 用于保存AnnotationProcessor解析的注解规范配置信息
 * @author renhui
 *
 */
public class AnnotationClassMap {
	
	private static Map<String, List<String>> classMap=new HashMap<String, List<String>>();
	
	public static void putAnnotationMap(String annotationId,String mapValue){
		List<String> classNameList=classMap.get(annotationId);
		if(classNameList==null){
			classNameList=new ArrayList<String>();
            classMap.put(annotationId, classNameList);
        }
        if(!classNameList.contains(mapValue)){
            classNameList.add(mapValue);
        }
	}
	
	public static List<String> getClassNamesById(String annotationId){
		return classMap.get(annotationId);
	}

	public static void clearAnnotationMap(){
		classMap.clear();
	}
}
