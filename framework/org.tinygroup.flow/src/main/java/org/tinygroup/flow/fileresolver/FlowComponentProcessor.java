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
package org.tinygroup.flow.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.IOException;

/**
 * 扫描组件的文件处理器
 *
 * @author renhui
 */
public class FlowComponentProcessor extends AbstractFileProcessor {

    /**
     * 扫描的文件后缀
     */
    private static final String FLOW_COMPONENT_EXT_FILENAME = ".fc.xml";

    public boolean isMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(FLOW_COMPONENT_EXT_FILENAME);
    }

    public void process() {
        FlowExecutor flowExecutor = SpringUtil.getBean(FlowExecutor.FLOW_BEAN);
        XStream stream = XStreamFactory.getXStream(FlowExecutor.FLOW_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            logger.logMessage(LogLevel.INFO, "正在删除逻辑组件fc文件[{0}]", fileObject.getAbsolutePath());
            ComponentDefines components = (ComponentDefines) caches.get(fileObject.getAbsolutePath());
            if (components != null) {
                flowExecutor.removeComponents(components);
                caches.remove(fileObject.getAbsolutePath());
            }
            logger.logMessage(LogLevel.INFO, "删除逻辑组件fc文件[{0}]结束", fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            logger.logMessage(LogLevel.INFO, "正在读取逻辑组件fc文件[{0}]", fileObject.getAbsolutePath());
            ComponentDefines oldDefines = (ComponentDefines) caches.get(fileObject.getAbsolutePath());
            if (oldDefines != null) {
                flowExecutor.removeComponents(oldDefines);
            }
            ComponentDefines components = null;
            try {
                components = (ComponentDefines) stream.fromXML(fileObject.getInputStream());
                flowExecutor.addComponents(components);
                caches.put(fileObject.getAbsolutePath(), components);
                logger.logMessage(LogLevel.INFO, "读取逻辑组件fc文件[{0}]结束", fileObject.getAbsolutePath());
            } catch (IOException e) {
                logger.errorMessage("读取逻辑组件fc文件[{0}]错误", e, fileObject.getAbsolutePath());
            }
        }

    }

    public void setFileResolver(FileResolver fileResolver) {

    }

}
