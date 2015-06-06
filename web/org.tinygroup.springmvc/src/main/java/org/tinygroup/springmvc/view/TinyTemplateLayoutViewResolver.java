package org.tinygroup.springmvc.view;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.template.TemplateEngine;

/**
 * tinytemplate解析器
 * 
 * @author renhui
 * 
 */
public class TinyTemplateLayoutViewResolver extends
		AbstractTemplateViewResolver {

	private static final String VIEW_EXT_FILENAME = "page";// 视图扩展名
	private static final String LAYOUT_EXT_FILENAME = "layout";// 布局扩展名

	private String viewExtFileName = VIEW_EXT_FILENAME;
	private String layoutExtFileName = LAYOUT_EXT_FILENAME;
	
	private TemplateEngine templateEngine;
	
	public TinyTemplateLayoutViewResolver() {
		super();
		setViewClass(requiredViewClass());
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void setViewExtFileName(String viewExtFileName) {
		this.viewExtFileName = viewExtFileName;
	}

	public void setLayoutExtFileName(String layoutExtFileName) {
		this.layoutExtFileName = layoutExtFileName;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		TinyTemplateLayoutView layoutView = (TinyTemplateLayoutView) super
				.buildView(viewName);
		Assert.assertNotNull(templateEngine,"templateEngine must not be null");
		layoutView.setTemplateEngine(templateEngine);
		layoutView.setUrl("/template"+generateUrl(viewName));

		return layoutView;
	}

	private String generateUrl(String viewName) {
		StringBuffer url=new StringBuffer();
		if(viewName.startsWith("/")){
			url.append(viewName);
		}else{
			url.append("/").append(viewName);
		}
		int sepIndex = viewName.lastIndexOf(".");
		if(sepIndex==-1){
			url.append(".").append(viewExtFileName);
		}
		return url.toString();
	}

	@Override
	protected Class requiredViewClass() {
		return TinyTemplateLayoutView.class;
	}

}