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
package org.tinygroup.commons.cpu;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 返回当前java进程占用的CPU情况
 *
 * @author luoguo
 */
public final class MonitorUtil {
    static OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    static RuntimeMXBean runbean = (RuntimeMXBean) ManagementFactory.getRuntimeMXBean();
    static long cpuTimes = 0;
    static long cpuTimeFetch = 0;

    /*
    * 获取CPU利用率
    */
    public static double getCpuUsage() {
        long cpuNumber = Runtime.getRuntime().availableProcessors();
        long currentTimeMillis = System.currentTimeMillis();
        int sum = 0;
        long catchTime = 0;
        long current = 0;
        long previous = cpuTimes;
        current = osbean.getProcessCpuTime();
        catchTime = runbean.getUptime();
        sum += (current - previous);
        cpuTimes = current;
        cpuTimeFetch = currentTimeMillis;
        return (double) sum / (double) ((currentTimeMillis - catchTime) * cpuNumber * 1000);
    }

}
