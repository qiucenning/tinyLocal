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
package org.tinygroup.weblayer.filter;

import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.buffered.exception.BufferCommitFailedException;
import org.tinygroup.weblayer.webcontext.buffered.impl.BufferedWebContextImpl;

import java.io.IOException;

/**
 * 对写入response中的数据进行缓存，以便于实现嵌套的页面。
 * 
 * @author renhui
 * 
 */
public class BufferedTinyFilter extends AbstractTinyFilter {

	
	public void initTinyFilter() {
		super.initTinyFilter();

	}

	
	public void preProcess(WebContext context) {

	}

	
	public void postProcess(WebContext context) {
		if (context instanceof BufferedWebContextImpl) {
			BufferedWebContextImpl buffered = (BufferedWebContextImpl) context;
			if (buffered.isBuffering()) {
				try {
					buffered.getBufferedResponse().commitBuffer();
				} catch (IOException e) {
					throw new BufferCommitFailedException(e);
				}
			}
		}
	}

	
	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new BufferedWebContextImpl(wrappedContext);
	}

	
	public int getOrder() {
		return BUFFERED_FILTER_PRECEDENCE;
	}

}
