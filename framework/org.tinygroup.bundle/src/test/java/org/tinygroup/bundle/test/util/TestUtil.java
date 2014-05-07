package org.tinygroup.bundle.test.util;

import org.tinygroup.bundle.BundleManager;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;



public class TestUtil {
	private static boolean init = false;
	public static void init(){
		if(!init){
			AbstractTestUtil.init("application.xml", true);
			init = true;
			BundleManager manager = SpringUtil.getBean(BundleManager.BEAN_NAME);
			manager.setCommonRoot("J:\\tinygroupgit\\tiny\\framework\\org.tinygroup.bundle\\src\\test\\resources");
			manager.setBundleRoot("J:\\tinygroupgit\\tiny\\framework\\org.tinygroup.bundle\\src\\test\\resources");
		}
		
	}
}
