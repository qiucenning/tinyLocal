/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (tinygroup@126.com).
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
package org.tinygroup.dbrouter.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 用于描述标准表名与shard表名之间的关系
 * Created by luoguo on 13-12-15.
 */
@XStreamAlias("table-mapping")
public class TableMapping {
	@XStreamAlias("table-name")
	@XStreamAsAttribute
	private String tableName;
	@XStreamAlias("shard-table-name")
	@XStreamAsAttribute
	private String shardTableName;
	@XStreamAlias("physics_schema")
	@XStreamAsAttribute
	private String physicsSchema;
	@XStreamAlias("physics_catalog")
	@XStreamAsAttribute
	private String physicsCatalog;

    public TableMapping() {

    }

    public TableMapping(String tableName, String shardTableName) {
        this.tableName = tableName;
        this.shardTableName = shardTableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getShardTableName() {
        return shardTableName;
    }

    public void setShardTableName(String shardTableName) {
        this.shardTableName = shardTableName;
    }

	public String getPhysicsSchema() {
		return physicsSchema;
	}

	public void setPhysicsSchema(String physicsSchema) {
		this.physicsSchema = physicsSchema;
	}

	public String getPhysicsCatalog() {
		return physicsCatalog;
	}

	public void setPhysicsCatalog(String physicsCatalog) {
		this.physicsCatalog = physicsCatalog;
	}
    
}
