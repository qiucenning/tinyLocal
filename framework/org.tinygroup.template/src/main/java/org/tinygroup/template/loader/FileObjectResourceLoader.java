package org.tinygroup.template.loader;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.Layout;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * Created by luoguo on 2014/6/9.
 */
public class FileObjectResourceLoader extends AbstractResourceLoader<FileObject> {
    private FileObject root = null;

    public FileObjectResourceLoader(String templateExtName,String layoutExtName, String root) {
        this(templateExtName,layoutExtName, VFS.resolveFile(root));
    }

    public FileObjectResourceLoader(String templateExtName,String layoutExtName, FileObject root) {
        super(templateExtName,layoutExtName);
        this.root = root;
    }

    public Template createTemplate(FileObject fileObject) throws TemplateException {
        if (fileObject != null) {
            return loadTemplate(fileObject, getClassLoader());
        }
        return null;
    }

    @Override
    public Layout createLayout(FileObject fileObject) throws TemplateException {
        if (fileObject != null) {
            return loadLayout(fileObject, getClassLoader());
        }
        return null;
    }

    protected Template loadTemplate(final String path) throws TemplateException {
        return createTemplate(root.getFileObject(path));
    }

    @Override
    protected Layout loadLayout(String path) throws TemplateException {
        return createLayout(root.getFileObject(path));
    }


    @Override
    public boolean isModified(String path) {
        return root.getFileObject(path).isModified();
    }

    public String getResourceContent(String path, String encode) throws TemplateException {
        FileObject fileObject = root.getFileObject(path);
        if (fileObject != null) {
            try {
                return IOUtils.readFromInputStream(fileObject.getInputStream(), encode);
            } catch (Exception e) {
                throw new TemplateException(e);
            }
        }
        return null;
    }

    private Template loadTemplate(FileObject fileObject, ClassLoader classLoader) {
        try {
            Template template = ResourceCompilerUtils.compileResource(classLoader, IOUtils.readFromInputStream(fileObject.getInputStream(), getTemplateEngine().getEncode()), fileObject.getPath());
            addTemplate(template);
            return template;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Layout loadLayout(FileObject fileObject, ClassLoader classLoader) {
        try {
            Layout layout = ResourceCompilerUtils.compileResource(classLoader, IOUtils.readFromInputStream(fileObject.getInputStream(), getTemplateEngine().getEncode()), fileObject.getPath());
            addLayout(layout);
            return layout;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}