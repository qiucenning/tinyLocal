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
package org.tinygroup.weblayer;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestPattern  extends TestCase{

	private List<Pattern> patterns=new ArrayList<Pattern>();
	
	
	public void testPattern(){
		initPattern();
		long startTime=System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			match("webstandard/index.page");
		}
		System.out.println(System.currentTimeMillis()-startTime);
	}

	private void initPattern() {
		for (int i = 0; i <100; i++) {
			Pattern pattern=Pattern.compile(".*/user"+i+"/index\\.page");
			patterns.add(pattern);
		}
		patterns.add(Pattern.compile(".*/index\\.page"));
	}
	
	private void match(String matchStr){
		for (Pattern pattern : patterns) {
			if(pattern.matcher(matchStr).matches()){
				return;
			}
		}
	}
	
	
	
}
