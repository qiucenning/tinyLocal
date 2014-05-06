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
package org.tinygroup.weblayer.webcontext.session.impl;

import javax.servlet.http.HttpSession;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;
import org.tinygroup.weblayer.webcontext.session.SessionLifecycleListener;



/**
 * 用来记录session日志生命期事件的listener。
 *
 * @author Michael Zhou
 */
public class SessionLifecycleLogger implements SessionLifecycleListener {
    private final static LogLevel          DEFAULT_LOG_LEVEL         = LogLevel.DEBUG;
    private final static LogLevel          DEFAULT_VISITED_LOG_LEVEL = LogLevel.TRACE;
    private final        Logger log                       = LoggerFactory.getLogger(SessionLifecycleLogger.class);
    private LogLevel logLevel;
    private LogLevel visitLogLevel;

 

    public void setLogLevel(LogLevel level) {
        this.logLevel = level;
    }

    public void setVisitLogLevel(LogLevel level) {
        this.visitLogLevel = level;
    }

    public void init(SessionConfig sessionConfig) {
        if (logLevel == null) {
            logLevel = DEFAULT_LOG_LEVEL;
        }

        if (visitLogLevel == null) {
            visitLogLevel = DEFAULT_VISITED_LOG_LEVEL;
        }
    }

    public void sessionCreated(HttpSession session) {
            log.logMessage(logLevel, "session created, id=" + session.getId());
    }

    public void sessionInvalidated(HttpSession session) {
            log.logMessage(logLevel, "session invalidated, id=" + session.getId());
    }

    public void sessionVisited(HttpSession session) {
            log.logMessage(visitLogLevel, "session visited, id=" + session.getId());
    }

}
