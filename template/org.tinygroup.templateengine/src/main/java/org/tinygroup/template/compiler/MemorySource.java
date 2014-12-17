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
package org.tinygroup.template.compiler;


/**
 * Java源代码描述
 *
 * @author luoguo
 */
public final class MemorySource  {
    /**
     * 完整类名
     */
    private String qualifiedClassName;
    /**
     * 类内容
     */
    private String content;
    public String getSimpleName(){
        String simpName=qualifiedClassName;
        int lastPositon = qualifiedClassName.lastIndexOf('.');
        if(lastPositon >=0){
            simpName=simpName.substring(lastPositon + 1);
        }

        return simpName;
    }
    public MemorySource(String qualifiedClassName, String content) {
        this.qualifiedClassName = qualifiedClassName;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

    public void setQualifiedClassName(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }
}
