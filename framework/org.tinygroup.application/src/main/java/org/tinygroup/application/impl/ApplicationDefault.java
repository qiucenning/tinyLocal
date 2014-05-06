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
package org.tinygroup.application.impl;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationContext;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;
//TODO 仔细想一下，程序的启动过程,文件搜索，SpringBean,配置之间的关系

/**
 * 默认的应用实现<br>
 * Tiny框架认为，应用都是一个Application，无论是控制台App还是Web App。<br>
 * 只不过这个应用的复杂程度不同而已。
 */
public class ApplicationDefault implements Application {

    private static Logger logger = LoggerFactory
            .getLogger(ApplicationDefaultOld.class);
    private List<ApplicationProcessor> applicationProcessors = new ArrayList<ApplicationProcessor>();

    public void init(ApplicationContext applicationContext) {
        ConfigurationManager configurationManager = ConfigurationUtil
                .getConfigurationManager();
        logger.logMessage(LogLevel.INFO, "启动应用开始...");
        XmlNode node = configurationManager.getApplicationConfig();
        if (node != null) {
            List<XmlNode> processorConfigs = node
                    .getSubNodesRecursively("application-processor");
            if (processorConfigs != null) {
                for (XmlNode processorConfig : processorConfigs) {
                    String processorBean = processorConfig.getAttribute("bean");
                    ApplicationProcessor processor = SpringUtil
                            .getBean(processorBean);
                    applicationProcessors.add(processor);
                }
            }
        }
    }

    public void start(ApplicationContext applicationContext) {
        if (applicationProcessors != null) {
            // 对ApplicationProcessor列表进行排序
            OrderUtil.order(applicationProcessors);
            for (ApplicationProcessor applicationProcessor : applicationProcessors) {
                logger.logMessage(LogLevel.INFO, "应用处理器{}正在启动...",
                        applicationProcessor.getClass());
                applicationProcessor.start(applicationContext);
                logger.logMessage(LogLevel.INFO, "应用处理器{}启动完毕。",
                        applicationProcessor.getClass());
            }
        }
    }

    public void stop(ApplicationContext applicationContext) {
        for (ApplicationProcessor processorLoader : applicationProcessors) {
            logger.logMessage(LogLevel.INFO, "应用处理器{}正在停止...",
                    processorLoader.getClass());
            processorLoader.stop(applicationContext);
            logger.logMessage(LogLevel.INFO, "应用处理器{}停止完毕。",
                    processorLoader.getClass());
        }
    }
}
