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
package org.tinygroup.dao.hibernate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

/* 
 * @Service用于标注业务层组件， 
 * @Controller用于标注控制层组件（如struts中的action）, 
 * @Repository用于标注数据访问组件，即DAO组件， 
 * 而@Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。     
 */
@Repository("basicDaoService")

public class HibernateBaseDaoImpl extends HibernateBaseDao {

	public Object save(Object entity) throws Exception {

		Object o = super.save(entity);
		return o;
	}

	public void delete(Object entity) throws Exception {
		super.delete(entity);
	}

	public Object update(Object entity) throws Exception  {
		return super.update(entity);
	}

	public Object[] save(Object[] entitys) throws Exception {
		for (Object entity : entitys) {
			save(entity);
		}
		return entitys;
	}

	public Object[] delete(Object[] entitys) throws Exception {
		for (Object entity : entitys) {
			delete(entity);
		}
		return entitys;
	}

	public Object[] update(Object[] entitys) throws Exception {
		for (Object entity : entitys) {
			update(entity);
		}
		return entitys;
	}

	public Collection<Object> save(Collection<Object> entitys) throws Exception {
		for (Object entity : entitys) {
			save(entity);
		}
		return entitys;
	}

	public Collection<Object> update(Collection<Object> entitys)
			throws Exception {
		for (Object entity : entitys) {
			update(entity);
		}
		return entitys;
	}

	public Collection<Object> delete(Collection<Object> entitys)
			throws Exception {
		for (Object entity : entitys) {
			delete(entity);
		}
		return entitys;
	}

	public Object deleteObject(Class clazz, Object id) throws Exception {
		return super.deleteObject(clazz, id);
	}
	
	public Collection<Object> deleteByKey(Class clazz,Collection<Object> keys) throws Exception {
		for(Object key:keys){
			super.deleteObject(clazz, key);
		}
		return keys;
	}

	public Object[] deleteByKey(Class clazz,Object[] keys) throws Exception {
		for(Object key:keys){
			super.deleteObject(clazz, key);
		}
		return keys;
	}
	
	public <T> T getObject(Class clazz, Object id) {
		return super.getObject(clazz, id);
	}
	

	public <T> Collection<T> getObjects(Class clazz, Collection ids) {
		List<T> list = new ArrayList<T>();
		for (Object id : ids) {
			T o = super.getObject(clazz, id);
			if (o != null)
				list.add(o);
		}
		return list;
	}

	public <T> T[] getObjects(Class clazz, Object[] ids) {
		Object[] objs = (Object[]) Array.newInstance(clazz, ids.length);
		for (int i = 0; i < ids.length; i++) {
			Object o = super.getObject(clazz, ids[i]);
			if (o != null)
				objs[i] = o;
		}
		return (T[]) objs;
	}
	
	
}