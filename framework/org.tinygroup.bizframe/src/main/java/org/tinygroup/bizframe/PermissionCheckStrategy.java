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
package org.tinygroup.bizframe;

/**
 * 权限检查策略，权限检查策略只能有一个生效
 * 
 * @author luoguo
 * 
 */
public interface PermissionCheckStrategy<K extends Comparable<K>> {

	/**
	 * 用于管理器把自己注入给检查器
	 * 
	 * @param permissionManager
	 */
	void setPermissionManager(PermissionManager<K> permissionManager);

	/**
	 * 是否禁止
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isBlock(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否允许
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isAllow(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject);

	/**
	 * 是否禁止
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isBlock(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId);

	/**
	 * 是否允许
	 * 
	 * @param permissionSubject
	 * @param permissionObject
	 * @return
	 */
	boolean isAllow(String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId);

}
