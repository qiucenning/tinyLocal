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
package org.tinygroup.database;

import org.tinygroup.database.fileresolver.CustomSqlFileResolver;
import org.tinygroup.database.fileresolver.InitDataFileResolver;
import org.tinygroup.database.fileresolver.ProcedureFileResolver;
import org.tinygroup.database.fileresolver.ProcessorFileResolver;
import org.tinygroup.database.fileresolver.TableFileResolver;
import org.tinygroup.database.fileresolver.ViewFileResolver;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.SpringBeansFileProcessor;
import org.tinygroup.fileresolver.impl.XStreamFileProcessor;
import org.tinygroup.metadata.fileresolver.BusinessTypeFileResolver;
import org.tinygroup.metadata.fileresolver.ConstantFileResolver;
import org.tinygroup.metadata.fileresolver.ErrorMessageFileResolver;
import org.tinygroup.metadata.fileresolver.StandardFieldFileResolver;
import org.tinygroup.metadata.fileresolver.StandardTypeFileResolver;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class TestInit {

	public static void init() {
		AbstractTestUtil.init(null, true);
	}
}
