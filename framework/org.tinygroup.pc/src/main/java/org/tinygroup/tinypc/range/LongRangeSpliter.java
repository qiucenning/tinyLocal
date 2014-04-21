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
package org.tinygroup.tinypc.range;

import org.tinygroup.tinypc.Range;
import org.tinygroup.tinypc.RangeSpliter;

import java.util.ArrayList;
import java.util.List;

/**
 * 长整型数的平均分配
 * Created by luoguo on 14-3-3.
 */
public class LongRangeSpliter implements RangeSpliter<Long> {
    public List<Range<Long>> split(Long start, Long end, int pieces) {
        List<Range<Long>> pairList = new ArrayList<Range<Long>>();
        double step = (end - start + 1) / (double) pieces;
        for (double i = start; i < end; i += step) {
            Range<Long> range = new Range<Long>(Math.round(i), Math.round(i + step - 1));
            pairList.add(range);
        }
        return pairList;
    }

    public List<Range<Long>> split(Range<Long> range, int pieces) {
        return split(range.getStart(), range.getEnd(), pieces);
    }
}
