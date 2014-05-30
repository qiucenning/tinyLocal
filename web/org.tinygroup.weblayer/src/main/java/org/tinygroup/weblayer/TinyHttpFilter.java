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
package org.tinygroup.weblayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class TinyHttpFilter implements Filter {
    private static final String EXCLUDE_PATH = "excludePath";
    private static final Logger logger = LoggerFactory
            .getLogger(TinyHttpFilter.class);
    private TinyProcessorManager tinyProcessorManager;
    private TinyFilterManager tinyFilterManager;

    private List<Pattern> excludePatterns = new ArrayList<Pattern>();
    
    private static final String POST_DATA_PROCESS="post-data-process";
    
    private static final String HOSTS="hosts";
    
    private static final String POST_DATA_KEY="post-data-key";
    
    public static final String DEFAULT_POST_DATA_KEY="$_post_data_key";
    
    private String[] hosts;
    
    private String postDataKey;

    private FilterWrapper wrapper;
    private FullContextFileRepository fullContextFileRepository;
    private static String[] defaultFiles = {"index.page", "index.htm", "index.html", "index.jsp"};

    public void destroy() {
        destoryTinyProcessors();
        destoryTinyFilters();
        wrapper = null;
    }

    private void destoryTinyFilters() {
        tinyFilterManager.destoryTinyResources();
        tinyFilterManager = null;
    }

    /**
     * 销毁tiny-processors
     */
    private void destoryTinyProcessors() {
        tinyProcessorManager.destoryTinyResources();
        tinyProcessorManager = null;
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        WebContext context = new WebContextImpl();
        context.put("springUtil", SpringUtil.class);
        postDataProcess(request, context);
        context.put("context", context);
        context.putSubContext("applicationproperties", new ContextImpl(ConfigurationUtil.getConfigurationManager().getApplicationPropertiesMap()));
        putRequstInfo(request, context);

        context.init(request, response,
                ServletContextHolder.getServletContext());
        String servletPath = context.get(WebContextUtil.TINY_SERVLET_PATH);
        if (servletPath.endsWith("/")) {
            for (String defaultFile : defaultFiles) {
                String tmpPath = servletPath + defaultFile;
                FileObject fileObject = fullContextFileRepository.getFileObject(tmpPath);
                if (fileObject != null && fileObject.isExist()) {
                    servletPath = tmpPath;
                    break;
                }
            }
        }
        if (isExcluded(servletPath)) {
            logger.logMessage(LogLevel.DEBUG, "请求路径:<{}>,被拒绝", servletPath);
            filterChain.doFilter(request, response);
            return;
        }
        TinyFilterHandler hander = new TinyFilterHandler(servletPath,
                filterChain, context, tinyFilterManager, tinyProcessorManager);
        if (wrapper != null) {
            wrapper.filterWrapper(context, hander);
        } else {
            hander.tinyFilterProcessor(request, response);
        }
    }

	private void postDataProcess(HttpServletRequest request, WebContext context) throws IOException {
		if(isPostMethod(request)&&!ObjectUtil.isEmptyObject(hosts)){
			String remoteHost=request.getRemoteHost();
			for (String host : hosts) {
				if(host.equals(remoteHost)){
					context.put(postDataKey,StreamUtil.readBytes(request.getInputStream(), true).toByteArray());
					break;
				}
			}
		}
		
	}

	private boolean isPostMethod(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("post");
	}

	private void initPostDataProcess(){
		ConfigurationManager appConfigManager = ConfigurationUtil.getConfigurationManager();
		XmlNode parserNode = appConfigManager.getApplicationConfig().getSubNode(POST_DATA_PROCESS);
		if(parserNode!=null){
			postDataKey=StringUtil.defaultIfBlank(parserNode.getAttribute(POST_DATA_KEY),DEFAULT_POST_DATA_KEY);
			String hostsContent=parserNode.getAttribute(HOSTS);
			if(!StringUtil.isBlank(hostsContent)){
				hosts= hostsContent.split(",");
			}
		}else{
			hosts=new String[0];
		}
		
	}

    private void putRequstInfo(HttpServletRequest request, WebContext context) {
        context.put(WebContextUtil.TINY_CONTEXT_PATH, request.getContextPath());
        context.put(WebContextUtil.TINY_REQUEST_URI, request.getRequestURI());
        String servletPath = request.getServletPath();
        if (servletPath == null || servletPath.length() == 0) {
            servletPath = request.getPathInfo();
        }
        context.put(WebContextUtil.TINY_SERVLET_PATH, servletPath);
    }

    /**
     * 请求是否被排除
     *
     * @param servletPath
     * @return
     */
    private boolean isExcluded(String servletPath) {
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(servletPath).matches()) {
                return true;
            }
        }
        return false;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.logMessage(LogLevel.INFO, "filter初始化开始...");
        fullContextFileRepository = SpringUtil
                .getBean("fullContextFileRepository");
        initExcludePattern(filterConfig);

        initTinyFilters();

        initTinyFilterWrapper();

        initTinyProcessors();
        
        initPostDataProcess();
        
        logger.logMessage(LogLevel.INFO, "filter初始化结束...");

    }

    private void initTinyFilterWrapper() {
        wrapper = tinyFilterManager.getFilterWrapper();
    }

    private void initExcludePattern(FilterConfig filterConfig) {
        excludePatterns.clear();//先清空
        String excludePath = filterConfig.getInitParameter(EXCLUDE_PATH);
        if (excludePath != null) {
            String[] excludeArray = excludePath.split(",");
            for (String path : excludeArray) {
                excludePatterns.add(Pattern.compile(path));
            }
        }
    }

    private void initTinyFilters() {
        tinyFilterManager = SpringUtil
                .getBean(TinyFilterManager.TINY_FILTER_MANAGER);
        tinyFilterManager.initTinyResources();
    }

    /**
     * tiny-processors初始化
     */
    private void initTinyProcessors() {
        tinyProcessorManager = SpringUtil
                .getBean(TinyProcessorManager.TINY_PROCESSOR_MANAGER);
        tinyProcessorManager.initTinyResources();
    }
}
