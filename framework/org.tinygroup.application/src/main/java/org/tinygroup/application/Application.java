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
package org.tinygroup.application;

import java.util.List;

/**
 * 不管是Web应用还是其它类型的应用，都可以看成是一个Application
 */
public interface Application {
   
    void addApplicationProcessor(ApplicationProcessor applicationProcessor);

    List<ApplicationProcessor> getApplicationProcessors();

    /**
     * 对Application进行初始化
     */
    void init();

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();

}