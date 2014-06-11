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
package org.tinygroup.vfs;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * fileObject的测试用例
 * @author renhui
 *
 */
public class TestFileObjectImpl extends TestCase {
	
	public void testFileObject() throws IOException {
		String path=getClass().getResource("/test/0.html").getFile();
		FileObject fileObject= VFS.resolveFile(path);
		FileUtils.printFileObject(fileObject);
	}
	
	
	public void testGetSubFile(){
		
		String projectPath=System.getProperty("user.dir");
		String testResourcePath=projectPath+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator;
		FileObject fileObject=VFS.resolveFile(testResourcePath);
		FileObject subFile= fileObject.getFileObject("/");
		assertEquals(fileObject, subFile);
		subFile=fileObject.getFileObject("/test");
		assertTrue(subFile!=null);
		subFile=fileObject.getFileObject("/test/0.html");
		assertTrue(subFile!=null);
		
	}
	
}
