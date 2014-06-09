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
package org.tinygroup.dbrouterjdbc3.jdbc.sample;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.dbrouter.PartitionRule;
import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.config.DataSourceConfig;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.config.TableMapping;
import org.tinygroup.dbrouter.impl.keygenerator.RouterKeyGeneratorLong;
import org.tinygroup.dbrouter.impl.partionrule.PartionRuleByTableName;
import org.tinygroup.dbrouter.impl.shardrule.ShardRuleByIdDifferentSchema;
import org.tinygroup.dbrouter.impl.shardrule.ShardRuleByIdSameSchema;

public class TestRouterUtil {

    public static Router getPrimarySlaveRouter() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds0");
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        for (int i = 0; i <= 2; i++) {
            DataSourceConfig dataSource = new DataSourceConfig("ds" + i, "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test" + i, "root", "123456");
            dataSourceList.add(dataSource);
        }
        List<Partition> partitions = new ArrayList<Partition>();
        Partition patition = new Partition("abc", Partition.MODE_PRIMARY_SLAVE);
        partitions.add(patition);
        List<Shard> shards = new ArrayList<Shard>();
        shards.add(new Shard("shard1", "ds0"));
        shards.add(new Shard("shard2", "ds1", 5));
        shards.add(new Shard("shard3", "ds2", 8));
        patition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }




    public static Router getDifferentSchemaPrimarySlaveRouter() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds0");
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        for (int i = 0; i <= 2; i++) {
            DataSourceConfig dataSource = new DataSourceConfig("ds" + i, "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test" + i, "root", "123456");
            dataSourceList.add(dataSource);
        }
        List<Partition> partitions = new ArrayList<Partition>();
        Partition patition = new Partition("abc", Partition.MODE_PRIMARY_SLAVE);
        partitions.add(patition);
        List<Shard> shards = new ArrayList<Shard>();
        shards.add(new Shard("shard1", "ds0"));
        shards.add(new Shard("shard2", "ds1", 5));
        shards.add(new Shard("shard3", "ds2", 8));
        patition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }

    public static Router getSameSchemaDiffrentTableRouter() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds1");
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        DataSourceConfig dataSource = new DataSourceConfig("ds1", "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test", "root", "123456");
        dataSourceList.add(dataSource);
        List<Partition> partitions = new ArrayList<Partition>();
        Partition patition = new Partition("abc", Partition.MODE_SHARD);
        List<PartitionRule> partitionRules = new ArrayList<PartitionRule>();
        partitionRules.add(new PartionRuleByTableName("aaa"));
        patition.setPartitionRules(partitionRules);
        partitions.add(patition);
        List<Shard> shards = new ArrayList<Shard>();
        for (int i = 0; i <= 2; i++) {
            Shard shard = new Shard("shard" + i, "ds1");
            shards.add(shard);
            List<TableMapping> tableMappings=new ArrayList<TableMapping>();
            tableMappings.add(new TableMapping("aaa","aaa"+i));
            shard.setTableMappings(tableMappings);
            List<ShardRule> rules = new ArrayList<ShardRule>();
            shard.setShardRules(rules);
            rules.add(new ShardRuleByIdSameSchema("aaa", "id", i));
        }
        patition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }
    public static Router getDifferentSchemaRouter() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds0");
        keyGenerator.setIncrement(1);
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        for (int i = 0; i <= 2; i++) {
            DataSourceConfig dataSource = new DataSourceConfig("ds" + i, "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test" + i, "root", "123456");
            dataSourceList.add(dataSource);
        }
        List<Partition> partitions = new ArrayList<Partition>();
        Partition patition = new Partition("abc", Partition.MODE_SHARD);
        List<PartitionRule> partitionRules = new ArrayList<PartitionRule>();
        partitionRules.add(new PartionRuleByTableName("aaa"));
        patition.setPartitionRules(partitionRules);
        partitions.add(patition);
        List<Shard> shards = new ArrayList<Shard>();
        for (int i = 0; i <= 2; i++) {
            Shard shard = new Shard("shard" + i, "ds" + i);
            shards.add(shard);
            List<ShardRule> rules = new ArrayList<ShardRule>();
            shard.setShardRules(rules);
            rules.add(new ShardRuleByIdDifferentSchema("aaa", "id", i));
        }
        patition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }

    public static Router getDifferentSchemaRouterGroupBy() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds0");
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        for (int i = 0; i <= 2; i++) {
            DataSourceConfig dataSource = new DataSourceConfig("ds" + i, "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test" + i, "root", "123456");
            dataSourceList.add(dataSource);
        }
        List<Partition> partitions = new ArrayList<Partition>();
        Partition patition = new Partition("abc", Partition.MODE_SHARD);
        List<PartitionRule> partitionRules = new ArrayList<PartitionRule>();
        partitionRules.add(new PartionRuleByTableName("score"));
        patition.setPartitionRules(partitionRules);
        partitions.add(patition);
        List<Shard> shards = new ArrayList<Shard>();
        for (int i = 0; i <= 2; i++) {
            Shard shard = new Shard("shard" + i, "ds" + i);
            shards.add(shard);
            List<ShardRule> rules = new ArrayList<ShardRule>();
            shard.setShardRules(rules);
            rules.add(new ShardRuleByIdDifferentSchema("aaa", "id", i));
        }
        patition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }

    public static Router getSameSchemaDiffrentTableRouterWithTableShard() {
        Router router = new Router("router1", "luog", "123456");
        RouterKeyGeneratorLong keyGenerator=new RouterKeyGeneratorLong();
        keyGenerator.setDataSourceId("ds1");
        router.setKeyGenerator(keyGenerator);
        List<DataSourceConfig> dataSourceList = new ArrayList<DataSourceConfig>();
        router.setDataSource(dataSourceList);
        DataSourceConfig dataSource = new DataSourceConfig("ds1", "com.mysql.jdbc.Driver", "jdbc:mysql://mysqldb:3306/test0", "root", "123456");
        dataSourceList.add(dataSource);
        List<Partition> partitions = new ArrayList<Partition>();
        Partition partition = new Partition("abc", Partition.MODE_SHARD);
        List<PartitionRule> partitionRules = new ArrayList<PartitionRule>();
        partitionRules.add(new PartionRuleByTableName("user"));
        partition.setPartitionRules(partitionRules);
        partitions.add(partition);
        List<Shard> shards = new ArrayList<Shard>();
        for (int i = 1; i <= 3; i++) {
            Shard shard = new Shard("shard" + i, "ds1");
            shards.add(shard);
            List<ShardRule> rules = new ArrayList<ShardRule>();
            shard.setShardRules(rules);
            rules.add(new ShardRuleByIdSameSchema("user", "id", i-1));
        }
        partition.setShards(shards);
        router.setPartitions(partitions);
        return router;
    }
}
