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
package org.tinygroup.weblayer.webcontext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当开始写http content的时候，通知其它request context提交其headers。在此之后将不能修改headers。
 *
 * @author renhui
 */
class CommittingAwareResponse extends HttpServletResponseWrapper {
    private final HeaderCommitter     committer;
    private       ServletOutputStream stream;
    private       PrintWriter         writer;

    public CommittingAwareResponse(HttpServletResponse response, HeaderCommitter committer) {
        super(response);
        this.committer = committer;
    }

    
    public ServletOutputStream getOutputStream() throws IOException {
        if (stream == null) {
            stream = new CommittingAwareServletOutputStream(committer, super.getOutputStream());
        }

        return stream;
    }

    
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new CommittingAwarePrintWriter(committer, super.getWriter());
        }

        return writer;
    }

    
    public String toString() {
        return getResponse().toString();
    }
}
