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
package org.tinygroup.docgen.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.tinygroup.commons.io.ByteArray;
import org.tinygroup.context.Context;
import org.tinygroup.docgen.DocumentGenerater;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.template.loader.StringResourceLoader;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * 
 * 功能说明:文档生成器
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public class DocumentGeneraterImpl implements DocumentGenerater<TemplateEngine> {
	private TemplateEngine templateGenerater;
    private StringResourceLoader stringResourceLoader;
	public TemplateEngine getTemplateGenerater() {
		return templateGenerater;
	}

	public void setTemplateGenerater(TemplateEngine generater) {
		this.templateGenerater = generater;
	}

	public void generate(FileObject fileObject, Context context, OutputStream writer) {
		try {
			Template template =templateGenerater.findTemplate(fileObject.getPath());
			generate(template,context,writer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void generate(String path, Context context, OutputStream writer) {
		try {
			FileObject fileObject = VFS.resolveFile(path);
			generate(fileObject,context,writer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void generate(Template template, Context context, OutputStream writer){
		try {
			if(context instanceof TemplateContext){
			   templateGenerater.renderTemplate(template, (TemplateContext)context, writer);
			}else{
			   TemplateContext newContext = new TemplateContextDefault();
			   newContext.setParent(context);
			   templateGenerater.renderTemplate(template, newContext, writer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addMacroFile(FileObject fileObject) {
		try {
			FileObjectResourceLoader resourceLoader = new FileObjectResourceLoader(null,null,fileObject.getExtName(),fileObject.getParent());
			templateGenerater.addResourceLoader(resourceLoader);
			templateGenerater.registerMacroLibrary(fileObject.getPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeMacroFile(FileObject fileObject) {
		//TemplateEngine暂时不支持卸载宏
	}
	

	public void generate(String path, Context context, File outputFile) {
		try {
			generate(path,context,new FileOutputStream(outputFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String evaluteString(Context context, String inputString) {
		if(stringResourceLoader==null){
			initStringResourceLoader();
		}
		ByteArrayOutputStream writer=new ByteArrayOutputStream();
		try {
			Template template =stringResourceLoader.createTemplate(inputString);
			generate(template,context,writer);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}
	
	private void initStringResourceLoader(){
		stringResourceLoader = new StringResourceLoader();
		stringResourceLoader.setTemplateEngine(templateGenerater);
	}

}
