package org.tinygroup.bundle.util;

import org.tinygroup.bundle.BundleLoader;

import java.io.File;

public final class BundleUtil {
	private BundleUtil() {
	}

	public static String getKey(String bundleId, String bundleVersion) {
		return String.format("%s-%s", bundleId, bundleVersion);
	}
	/**
	 * 获取插件jar包存放根目录
	 * @return
	 */
	public static String getPlguinDir(){
		File file = new File(BundleUtil.class.getClassLoader()
				.getResource("").getFile());
		return file.getAbsolutePath() + File.separator + BundleLoader.BUNDLE_FOLDER;
		 
	}
}