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
package org.tinygroup.logger;

/**
 * 日志访问接口<br>
 * 日志事务，表示，从startTransaction开始到endTransaction中间记录的所有日志都会拼成一个完整的日志块，连续输出
 */

import org.tinygroup.context.Context;
import org.tinygroup.logger.impl.LogBuffer;

import java.util.Locale;

/**
 * 日志接口
 *
 * @author luoguo
 */
public interface Logger {
    /**
     * 是否支持事务日志
     *
     * @return
     */
    boolean isSupportTransaction();

    /**
     * 获取日志缓冲
     *
     * @return
     */
    LogBuffer getLogBuffer();


    /**
     * 删除日志缓冲
     */
    void removeLogBuffer();

    /**
     * 设置事务日志支持方式
     *
     * @param supportBusiness true为支持，false为不支持，默认为支持
     */
    void setSupportTransaction(boolean supportBusiness);

    /**
     * 开始事务日志
     */
    void startTransaction();

    /**
     * 结束事务日志
     */
    void endTransaction();

    /**
     * 把事务日志缓冲中的内容写入到日志当中，<font color="red">慎用</font><br>
     * 由哪个日志记录器发起，则会用哪个日志记录器记录
     */
    void flushTransaction();

    /**
     * 重置事务日志，记数器归零，内容清空，一般在最上层调用，否则会导致缓冲内容丢失，<font color="red">慎用</font><br>
     */
    void resetTransaction();

    /**
     * 返回原生的日志记录器，，<font color="red">慎用</font><br>
     * 采用此日志记录器，则缓冲，国际化等都无法使用
     *
     * @return
     */
    org.slf4j.Logger getLogger();

    /**
     * 判定某个级别是否启用
     *
     * @param logLevel
     * @return
     */
    boolean isEnabled(LogLevel logLevel);

    /**
     * 不对message进行格式化直接输出
     *
     * @param logLevel
     * @param message
     */
    void logMessage(LogLevel logLevel, String message);
    
    void logMessage(LogLevel logLevel, String message,Throwable t);

    /**
     * 不对message进行格式化直接输出
     *
     * @param logLevel
     * @param message  采用{0} {1} 方式占位
     * @param args
     */
    void logMessage(LogLevel logLevel, String message, Object... args);

    void logMessage(LogLevel logLevel, String message, Throwable t, Object... args);
    
    /**
     * 不对message进行格式化直接输出
     *
     * @param logLevel
     * @param message  采用{name} {$age} 方式占位
     * @param context
     */
    void logMessage(LogLevel logLevel, String message, Context context);
    
    void logMessage(LogLevel logLevel, String message, Throwable t, Context context);

    /**
     * 通过国际化方式进行记录
     *
     * @param logLevel
     * @param code
     */
    void log(LogLevel logLevel, String code);

    /**
     * 通过国际化方式进行记录
     *
     * @param logLevel
     * @param locale
     * @param code
     * @param args
     */
    void log(LogLevel logLevel, Locale locale, String code, Object... args);

    /**
     * 通过国际化方式进行记录
     *
     * @param logLevel
     * @param code
     * @param args
     */
    void log(LogLevel logLevel, String code, Object... args);

    /**
     * 通过国际化方式进行记录
     *
     * @param logLevel
     * @param locale
     * @param code
     * @param context
     */
    void log(LogLevel logLevel, Locale locale, String code, Context context);

    /**
     * 通过国际化方式进行记录
     *
     * @param logLevel
     * @param code
     * @param context
     */
    void log(LogLevel logLevel, String code, Context context);

    void error(String code);

    void error(Throwable throwable);

    void error(String code, Throwable throwable);

    void error(String code, Throwable throwable, Object... args);

    void error(String code, Throwable throwable, Context context);

    void errorMessage(String message);

    void errorMessage(String message, Throwable throwable);

    void errorMessage(String message, Throwable throwable, Object... args);

    void errorMessage(String message, Throwable throwable, Context context);
    
    /**
     * 往MDC存放值
     * @param key
     * @param value
     */
    void putToMDC(String key, Object value);
    /**
     * 移除MDC中变量值
     * @param key
     */
    void removeFromMDC(String key);
    
    /**
	 * 返回缓存限制记录条数 <br>
	 * <br>
	 * 说明：该参数在日志对象被置为缓存模式后才有效，<br>
	 * 若缓存待输出日志条数超过该参数，插件将调用<br>
	 * flush方法输出日志并清空缓存。<br>
	 * 
	 * @return 返回限制记录数
	 */
    int getMaxBufferRecords();

	/**
	 * 设置缓存限制记录条数 <br>
	 * <br>
	 * 说明：当日志对象被置为缓存模式后，该参数用于<br>
	 * 限制缓存允许的最大记录条数。<br>
	 * 
	 * @param bufferRecords
	 *            设置限制记录数
	 */
    void setMaxBufferRecords(int bufferRecords);

}