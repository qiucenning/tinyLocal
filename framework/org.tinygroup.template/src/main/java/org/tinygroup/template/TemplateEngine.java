package org.tinygroup.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 模板引擎
 * Created by luoguo on 2014/6/6.
 */

public interface TemplateEngine extends TemplateContextOperator {
    public static final String DEFAULT = "default";

    TemplateEngine setEncode(String encode);

    /**
     * 设置
     *
     * @param i18nVistor
     */
    TemplateEngine setI18nVistor(I18nVistor i18nVistor);

    I18nVistor getI18nVistor();

    /**
     * 添加函数
     *
     * @param function
     */
    TemplateEngine addTemplateFunction(TemplateFunction function);

    /**
     * 返回注册的方法
     *
     * @param methodName 注册的方法名
     * @return
     */
    TemplateFunction getTemplateFunction(String methodName);

    /**
     * 返回注册的方法
     *
     * @param className  绑定的类名
     * @param methodName 注册的方法名
     * @return
     */
    TemplateFunction getTemplateFunction(String className, String methodName);

    /**
     * 返回编码
     *
     * @return
     */
    String getEncode();

    /**
     * 添加类型加载器
     *
     * @param templateLoader
     */
    TemplateEngine putTemplateLoader(String type, ResourceLoader templateLoader);

    Template getTemplate(String path) throws TemplateException;

    /**
     * 返回指定类型的加载器
     *
     * @return
     */
    ResourceLoader getTemplateLoader(String type) throws TemplateException;

    /**
     * 返回默认加载器
     *
     * @return
     * @throws TemplateException
     */
    ResourceLoader getDefaultTemplateLoader() throws TemplateException;

    /**
     * 返回所有的 Loader
     *
     * @return
     */
    Map<String, ResourceLoader> getTemplateLoaderMap();

    /**
     * 渲染宏
     *
     * @param macroName 要执行的宏名称
     * @param template  调用宏的模板
     * @param context   上下文
     * @param writer    输出器
     */
    void renderMacro(String macroName, Template template, TemplateContext context, Writer writer) throws IOException, TemplateException;

    /**
     * 直接渲染指定的模板
     *
     * @param macro    要执行的宏
     * @param template 调用宏的模板
     * @param context  上下文
     * @param writer   输出器
     * @throws IOException
     * @throws TemplateException
     */
    void renderMacro(Macro macro, Template template, TemplateContext context, Writer writer) throws IOException, TemplateException;


    /**
     * 根据路径渲染一个模板文件
     *
     * @param path    模板对应的路径
     * @param context 上下文
     * @param writer  输出器
     */
    void renderTemplate(String path, TemplateContext context, Writer writer) throws TemplateException;

    /**
     * 采用没有上下文，控制台输出方式进行渲染
     *
     * @param path
     * @throws TemplateException
     */
    void renderTemplate(String path) throws TemplateException;

    /**
     * 采用没有上下文，控制台输出方式进行渲染
     *
     * @param template
     * @throws TemplateException
     */
    void renderTemplate(Template template) throws TemplateException;

    /**
     * 直接渲染一个模板
     *
     * @param template 要渲染的模板
     * @param context  上下文
     * @param writer   输出器
     * @throws TemplateException
     */
    void renderTemplate(Template template, TemplateContext context, Writer writer) throws TemplateException;

    /**
     * 根据宏名查找要调用的宏
     *
     * @param macroName
     * @param template
     * @param $context
     * @return
     * @throws TemplateException
     */
    Macro findMacro(Object macroName, Template template, TemplateContext $context) throws TemplateException;

    /**
     * 执行方法
     *
     * @param functionName
     * @param parameters
     * @return
     * @throws TemplateException
     */
    Object executeFunction(String functionName, Object... parameters) throws TemplateException;

    /**
     * 获取资源对应的文本
     *
     * @param path
     * @return
     */
    String getResourceContent(String path, String encode) throws TemplateException;

    boolean isCacheEnabled();

    TemplateEngine setCacheEnabled(boolean cacheEnabled);
}
