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
package org.tinygroup.dbrouterjdbc3.jdbc.sample;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestRouterSelectPrimarySlave {
    public static void main(String[] args) throws Throwable {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        Router router = TestRouterUtil.getPrimarySlaveRouter();
        routerManager.addRouter(router);
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn =
                DriverManager.getConnection("jdbc:dbrouter://router1", "luog", "123456");
        Statement stmt = conn.createStatement();
        String sql;
        //插入100条数据
        for (int i = 1; i <= 100; i++) {
            sql = "select * from aaa";
            boolean result = stmt.execute(sql);
        }
    }
}
