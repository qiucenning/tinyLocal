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
package org.tinygroup.metadata.stdfield;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.metadata.config.stdfield.NickName;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.config.stdfield.StandardFields;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.xstream.XStreamFactory;

import java.util.ArrayList;
import java.util.List;

public class StandardFieldsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream stream = XStreamFactory.getXStream(MetadataUtil.METADATA_XSTREAM);
		stream.processAnnotations(StandardFields.class);
		StandardFields standardFields = new StandardFields();
		standardFields.setPackageName("aa");
		List<StandardField> standardFieldList = new ArrayList<StandardField>();
		standardFields.setStandardFieldList(standardFieldList);
		StandardField stdField = new StandardField();
		standardFieldList.add(stdField);
		stdField.setName("aa");
		stdField.setTitle("AA");
		List<NickName> nickNames = new ArrayList<NickName>();
		stdField.setNickNames(nickNames);
		stdField.setTypeId("hsvarcharid");
		NickName nickName = new NickName();
		nickName.setName("bb");
		nickName.setTitle("BB");
		nickNames.add(nickName);

		System.out.println(stream.toXML(standardFields));
	}

}
