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
package org.tinygroup.jspengine.compiler;

import org.tinygroup.jspengine.JasperException;

import javax.servlet.jsp.tagext.FunctionInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines internal representation for an EL Expression
 *
 * It currently only defines functions.  It can be expanded to define
 * all the components of an EL expression, if need to.
 *
 * @author Kin-man Chung
 */

abstract class ELNode {

    abstract public void accept(Visitor v) throws JasperException;

    /**
     * Child classes
     */


    /**
     * Represents an EL expression: anything in ${ and }.
     */
    public static class Root extends ELNode {

	private ELNode.Nodes expr;
        private boolean isDollarExpr;

	Root(ELNode.Nodes expr, boolean isDollarExpr) {
	    this.expr = expr;
            this.isDollarExpr = isDollarExpr;
	}

	public void accept(Visitor v) throws JasperException {
	    v.visit(this);
	}

	public ELNode.Nodes getExpression() {
	    return expr;
	}
    }

    /**
     * Represents text outside of EL expression.
     */
    public static class Text extends ELNode {

	private String text;

	Text(String text) {
	    this.text = text;
	}

	public void accept(Visitor v) throws JasperException {
	    v.visit(this);
	}

	public String getText() {
	    return text;
	}
    }

    /**
     * Represents anything in EL expression, other than functions, including
     * function arguments etc
     */
    public static class ELText extends ELNode {

	private String text;

	ELText(String text) {
	    this.text = text;
	}

	public void accept(Visitor v) throws JasperException {
	    v.visit(this);
	}

	public String getText() {
	    return text;
	}
    }

    /**
     * Represents a function
     * Currently only include the prefix and function name, but not its
     * arguments.
     */
    public static class Function extends ELNode {

	private String prefix;
	private String name;
	private String uri;
	private FunctionInfo functionInfo;
	private String methodName;
	private String[] parameters;

	Function(String prefix, String name) {
	    this.prefix = prefix;
	    this.name = name;
	}

	public void accept(Visitor v) throws JasperException {
	    v.visit(this);
	}

	public String getPrefix() {
	    return prefix;
	}

	public String getName() {
	    return name;
	}

	public void setUri(String uri) {
	    this.uri = uri;
	}

	public String getUri() {
	    return uri;
	}

	public void setFunctionInfo(FunctionInfo f) {
	    this.functionInfo = f;
	}

	public FunctionInfo getFunctionInfo() {
	    return functionInfo;
	}

	public void setMethodName(String methodName) {
	    this.methodName = methodName;
	}

	public String getMethodName() {
	    return methodName;
	}

	public void setParameters(String[] parameters) {
	    this.parameters = parameters;
	}

	public String[] getParameters() {
	    return parameters;
	}
    }

    /**
     * An ordered list of ELNode.
     */
    public static class Nodes {

	/* Name used for creating a map for the functions in this
	   EL expression, for communication to Generator.
	 */
	String mapName = null;	// The function map associated this EL
	private List list;

	public Nodes() {
	    list = new ArrayList();
	}

	public void add(ELNode en) {
	    list.add(en);
	}

	/**
	 * Visit the nodes in the list with the supplied visitor
	 * @param v The visitor used
	 */
	public void visit(Visitor v) throws JasperException {
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ELNode n = (ELNode) iter.next();
		n.accept(v);
	    }
	}

	public Iterator iterator() {
	    return list.iterator();
	}

	public boolean isEmpty() {
	    return list.size() == 0;
	}

	/**
	 * @return true if the expression contains a ${...} or #{}
	 */
	public boolean containsEL() {
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ELNode n = (ELNode) iter.next();
		if (n instanceof Root) {
		    return true;
		}
	    }
	    return false;
	}

        public boolean hasDollarExpression() {
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ELNode n = (ELNode) iter.next();
		if (n instanceof Root) {
		    if (((Root)n).isDollarExpr) {
                        return true;
                    }
		}
	    }
	    return false;
        }

        public boolean hasPoundExpression() {
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ELNode n = (ELNode) iter.next();
		if (n instanceof Root) {
		    if (!((Root)n).isDollarExpr) {
                        return true;
                    }
		}
	    }
	    return false;
        }

	public void setMapName(String name) {
	    this.mapName = name;
	}

	public String getMapName() {
	    return mapName;
	}
    }

    /*
     * A visitor class for traversing ELNodes
     */
    public static class Visitor {

	public void visit(Root n) throws JasperException {
	    n.getExpression().visit(this);
	}

	public void visit(Function n) throws JasperException {
	}

	public void visit(Text n) throws JasperException {
	}

	public void visit(ELText n) throws JasperException {
	}
    }
}

