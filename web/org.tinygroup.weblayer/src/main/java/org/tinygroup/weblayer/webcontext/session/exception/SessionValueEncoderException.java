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
package org.tinygroup.weblayer.webcontext.session.exception;


/**
 * 代表值编码、解码时发生的错误。
 *
 * @author renhui
 */
public class SessionValueEncoderException extends SessionFrameworkException {
    private static final long serialVersionUID = 2481903644930846849L;

    public SessionValueEncoderException() {
        super();
    }

    public SessionValueEncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionValueEncoderException(String message) {
        super(message);
    }

    public SessionValueEncoderException(Throwable cause) {
        super(cause);
    }
}
