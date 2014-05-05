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
package org.tinygroup.weblayer.servlet;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.TinyProcessorManager;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TinyServlet入口
 * 
 * @author luoguo
 * 
 */
public class TinyServlet extends HttpServlet {
	
	private static Logger logger = LoggerFactory
	.getLogger(TinyServlet.class);
	TinyProcessorManager tinyProcessorManager;

	public void init() {
		tinyProcessorManager = SpringUtil.getBean(TinyProcessorManager.TINY_PROCESSOR_MANAGER);
		tinyProcessorManager.initTinyResources();
	}

	
	public void init(ServletConfig config) throws ServletException {
		init();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if(servletPath==null||servletPath.length()==0){
			servletPath=request.getPathInfo();
		}
		logger.logMessage(LogLevel.DEBUG, "servlet请求路径：<{}>",servletPath
				);
		WebContext context = new WebContextImpl();
		context.init(request, response,ServletContextHolder.getServletContext());
		tinyProcessorManager.execute(servletPath, context);
	}

	
	public void destroy() {
		tinyProcessorManager.destoryTinyResources();
		tinyProcessorManager=null;
	}

	
}
