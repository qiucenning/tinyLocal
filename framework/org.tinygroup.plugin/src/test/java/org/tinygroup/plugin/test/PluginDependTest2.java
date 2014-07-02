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
package org.tinygroup.plugin.test;

import junit.framework.TestCase;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.plugin.PluginManager;
import org.tinygroup.plugin.config.PluginConfig;
import org.tinygroup.plugin.impl.PluginManagerImpl;
import org.tinygroup.plugin.test.plugin.PluginCounter;

public class PluginDependTest2 extends TestCase {
	
	public void setUp() {
		FileResolver fileResolver = new FileResolverImpl();
		FileResolverUtil.addClassPathPattern(fileResolver);
		fileResolver
				.addResolvePath(FileResolverUtil.getClassPath(fileResolver));
		fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
		try {
			fileResolver.addResolvePath(FileResolverUtil
					.getWebLibJars(fileResolver));
		} catch (Exception e) {
			
		}
		fileResolver.addIncludePathPattern("org\\.tinygroup\\.(.)*\\.jar");
		fileResolver.resolve();
	}
	
	private PluginConfig getPluginConfigA(){
		PluginConfig config = new PluginConfig();
		config.setDependPlugins("");
		config.setPluginBean("pluginA");
		config.setPluginName("A");
		config.setPluginLevel(5);
		return config;
	}
	
	private PluginConfig getPluginConfigB(){
		PluginConfig config = new PluginConfig();
		config.setDependPlugins("A");
		config.setPluginBean("pluginB");
		config.setPluginName("B");
		config.setPluginLevel(5);
		return config;
	}
	
	public void testStart(){
		PluginManager manager = new PluginManagerImpl();
		manager.addPlugin(getPluginConfigA());
		manager.addPlugin(getPluginConfigB());
		try {
			PluginCounter.setCounter(1);
			manager.start();
			assertEquals(1, PluginCounter.getCounter());
		} catch (Exception e) {
			assertTrue(false);
		}
		
	}
	
	
	public void testStop() {
		PluginManager manager = new PluginManagerImpl();
		manager.addPlugin(getPluginConfigA());
		manager.addPlugin(getPluginConfigB());
		try {
			manager.start();
			PluginCounter.setCounter(1);
			manager.stop();
			assertEquals(0, PluginCounter.getCounter());
		} catch (Exception e) {
			assertTrue(false);
		}

	}

}
