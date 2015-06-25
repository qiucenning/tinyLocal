/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.weblayer.webcontext.session;

/**
 * 将session model转换为store中可存储的对象，或者将store中取得的对象转换回session model。
 *
 * @author renhui
 */
public interface SessionModelEncoder {
    /** 将session model转换成store中可存储的对象。 */
    Object encode(SessionModel model);

    /**
     * 将store中取得的数据转换成session model。
     * <p>
     * 如果返回<code>null</code>，则数据格式不支持。框架将会尝试用其余的<code>SessionModelEncoder</code>
     * 来解码。
     * </p>
     */
    SessionModel decode(Object data, SessionModel.Factory factory);
}
