package org.tinygroup.fileresolver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.FileSchemaProvider;

public class FileResolverUtil {
	private static Logger logger = LoggerFactory
			.getLogger(FileResolverUtil.class);

	public static void addClassPathPattern(FileResolver resolver) {
		resolver.addIncludePathPattern("[\\/]classes\\b");
		resolver.addIncludePathPattern("[\\/]test-classes\\b");
	}

	public static List<String> getClassPath(FileResolver resolver) {
		List<String> classPathFileObjects = new ArrayList<String>();
		String classPathProperty = System.getProperty("java.class.path")
				.toString();
		String operateSys = System.getProperty("os.name").toLowerCase();
		String[] classPaths = null;
		if (operateSys.indexOf("windows") >= 0) {
			classPaths = classPathProperty.split(";");
		} else {
			classPaths = classPathProperty.split(":");
		}
		if (classPaths != null) {
			for (String classPath : classPaths) {
				if (classPath.length() > 0) {
					FileObject fileObject = VFS.resolveFile(classPath);
					if (isInclude(fileObject, resolver)) {
						classPathFileObjects.add(fileObject.getAbsolutePath());
					}
				}
			}
		}
		return classPathFileObjects;
	}

	static boolean isInclude(FileObject fileObject, FileResolver resolver) {
		if (fileObject.getSchemaProvider() instanceof FileSchemaProvider) {
			return true;
		}
		Map<String, Pattern> includePathPatternMap = resolver
				.getIncludePathPatternMap();
		for (String patternString : includePathPatternMap.keySet()) {
			Pattern pattern = includePathPatternMap.get(patternString);
			Matcher matcher = pattern.matcher(fileObject.getFileName());
			if (matcher.find()) {
				// logger.logMessage(LogLevel.INFO,
				// "文件<{}>由于匹配了包含正则表达式<{}>而被扫描。", fileObject, patternString);
				return true;
			}
		}
		return false;
	}

	public static List<String> getWebLibJars(FileResolver resolver)
			throws Exception {
		List<String> classPaths = new ArrayList<String>();
		logger.logMessage(LogLevel.INFO, "查找Web工程中的jar文件列表开始...");
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> urls = loader.getResources("META-INF/MANIFEST.MF");
		Map<String, Pattern> includePathPatternMap = resolver
				.getIncludePathPatternMap();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			String path = url.toString();
			path = path.replaceAll("/./", "/");// weblogic中存在这种情况
			if (path.indexOf("!") > 0) {
				path = path.split("!")[0];
			} else {// 专门为JBOSS vfs开头的处理
				path = path.substring(0,
						path.length() - "META-INF/MANIFEST.MF".length() - 1);
				path = path.substring(path.indexOf(':') + 1);
			}
			FileObject fileObject = VFS.resolveFile(path);
			if (includePathPatternMap != null
					&& includePathPatternMap.size() > 0) {
				if (isInclude(fileObject, resolver)) {
					addJarFile(classPaths, fileObject.getAbsolutePath());
					continue;
				}
			}
			Manifest mf = new Manifest(url.openStream());
			Attributes attributes = mf.getMainAttributes();
			String isTinyProject = attributes.getValue("IsTinyProject");
			if ("true".equals(isTinyProject)) {
				logger.logMessage(LogLevel.INFO,
						"文件<{}>由于在MANIFEST.MF文件中声明了IsTinyProject: true而被扫描。",
						fileObject);
				addJarFile(classPaths, fileObject.getAbsolutePath());
			}
		}
		logger.logMessage(LogLevel.INFO, "查找Web工程中的jar文件列表完成。");
		return classPaths;
	}

	private static void addJarFile(List<String> classPaths, String path) {
		logger.logMessage(LogLevel.INFO, "扫描到jar文件<{}>。", path);
		classPaths.add(path);
	}

	public static List<String> getWebClasses() {
		List<String> allScanningPath = new ArrayList<String>();
		logger.logMessage(LogLevel.INFO, "查找WEB-INF/classes路径开始...");
		URL url = FileResolverImpl.class.getResource("/");
		String path = url.toString();
		logger.logMessage(LogLevel.INFO, "WEB-INF/classes路径是:{}", path);
		if (path.indexOf("!") < 0) {// 如果在目录中
			FileObject fileObject = VFS.resolveFile(path);
			allScanningPath.add(fileObject.getAbsolutePath());
			String libPath = path.replaceAll("/classes", "/lib");
			logger.logMessage(LogLevel.INFO, "WEB-INF/lib路径是:{}", libPath);
			FileObject libFileObject = VFS.resolveFile(libPath);

			allScanningPath.add(libFileObject.getAbsolutePath());
			int index = path.indexOf("/classes");
			if (index > 0) {
				String webInfPath = path.substring(0, index);
				if (webInfPath.endsWith("WEB-INF")) {
					logger.logMessage(LogLevel.INFO, "WEB-INF路径是:{}",
							webInfPath);
					FileObject webInfoFileObject = VFS.resolveFile(webInfPath);
					allScanningPath.add(webInfoFileObject.getAbsolutePath());
				}
			}

		} else {// 如果在jar包中
			path = url.getFile().split("!")[0];
			FileObject fileObject = VFS.resolveFile(path);
			allScanningPath.add(fileObject.getAbsolutePath());
			String libPath = path.substring(0, path.lastIndexOf('/'));
			logger.logMessage(LogLevel.INFO, "WEB-INF/lib路径是:{}", libPath);
			FileObject libFileObject = VFS.resolveFile(libPath);
			allScanningPath.add(libFileObject.getAbsolutePath());
		}
		logger.logMessage(LogLevel.INFO, "查找WEB-INF/classes路径完成。");

		String webinfPath = ConfigurationUtil.getConfigurationManager()
				.getConfiguration().get("TINY_WEBROOT");
		if (webinfPath == null || webinfPath.length() == 0) {
			logger.logMessage(LogLevel.WARN, "WEBROOT变量找不到");
			return allScanningPath;
		}
		FileObject fileObject = VFS.resolveFile(webinfPath);
		allScanningPath.add(fileObject.getAbsolutePath());
		return allScanningPath;
	}
}