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
package org.tinygroup.database.view;

import org.tinygroup.database.config.view.View;
import org.tinygroup.database.config.view.Views;

import java.util.List;

public interface ViewProcessor {

	void addViews(Views views);
	void removeViews(Views views);
	View getView(String name);
	List<View> getViews();
	String getCreateSql(String name, String language);
	String getCreateSql(View view, String language);
	List<String> getCreateSql(String language);
	
	String getDropSql(String name, String language);
	String getDropSql(View view, String language);
	List<String> getDropSql(String language);
	
	
}
