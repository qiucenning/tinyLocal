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
package org.tinygroup.jdbctemplatedslsession.execute;

import static org.tinygroup.jdbctemplatedslsession.CustomTable.CUSTOM;
import static org.tinygroup.jdbctemplatedslsession.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.Insert.insertInto;
import static org.tinygroup.tinysqldsl.Select.select;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.Update.update;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import static org.tinygroup.tinysqldsl.select.Join.leftJoin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.tinygroup.jdbctemplatedslsession.Custom;
import org.tinygroup.jdbctemplatedslsession.CustomScore;
import org.tinygroup.jdbctemplatedslsession.SimpleDslSession;
import org.tinygroup.tinysqldsl.Delete;
import org.tinygroup.tinysqldsl.DslSession;
import org.tinygroup.tinysqldsl.Insert;
import org.tinygroup.tinysqldsl.Pager;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.Update;

public class DslSqlTest extends BaseTest {

	public void testDsl() throws SQLException {

		Delete delete = delete(CUSTOM);
		DslSession session = new SimpleDslSession(dataSource);
		int affect = session.execute(delete);

		delete = delete(TSCORE);
		affect = session.execute(delete);

		Insert customInsert = insertInto(CUSTOM).values(
				CUSTOM.ID.value("10001"), CUSTOM.NAME.value("悠悠然然"),
				CUSTOM.AGE.value(22));
		affect = session.execute(customInsert);
		assertEquals(1, affect);
		
		Insert scoreInsert = insertInto(TSCORE).values(
				TSCORE.ID.value("10002"), TSCORE.NAME.value("悠悠然然"),
				TSCORE.SCORE.value(98), TSCORE.COURSE.value("shuxue"));
		affect = session.execute(scoreInsert);
		assertEquals(1, affect);

		Select select = selectFrom(CUSTOM).where(CUSTOM.NAME.like("悠"));
		Custom custom = session.fetchOneResult(select, Custom.class);
		assertEquals("悠悠然然", custom.getName());

		select = selectFrom(CUSTOM).where(CUSTOM.ID.gt(0));
		List<Map> maplist = session.fetchList(select, Map.class);
		assertEquals(maplist.size(),1);
		assertEquals(maplist.get(0).get("name"), "悠悠然然");
		assertEquals(maplist.get(0).get("NAME"), "悠悠然然");

		select = select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE, TSCORE.COURSE)
				.from(CUSTOM).join(
						leftJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)));
		CustomScore customScore = session.fetchOneResult(select,
				CustomScore.class);
		assertEquals("悠悠然然", customScore.getName());
		assertEquals(98, customScore.getScore());
		assertEquals(22, customScore.getAge());
		assertEquals("shuxue", customScore.getCourse());

		//返回map类型
		Map<String,Object> customMap = session.fetchOneResult(select,
				Map.class);
		assertEquals("悠悠然然",customMap.get("NAME"));
		assertEquals(22,customMap.get("AGE"));
		assertEquals(98,customMap.get("SCORE"));
		assertEquals("shuxue",customMap.get("COURSE"));

		select = select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE, TSCORE.COURSE)
				.from(CUSTOM).where(CUSTOM.NAME.eq("悠悠然然")).join(
						leftJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)));
		CustomScore customScore2 = session.fetchOneResult(select,
				CustomScore.class);
		assertEquals("悠悠然然", customScore2.getName());
		assertEquals(98, customScore2.getScore());
		assertEquals(22, customScore2.getAge());
		assertEquals("shuxue", customScore2.getCourse());
		
		select=select(CUSTOM.AGE.max()).from(CUSTOM);
        int max =session.fetchOneResult(select, Integer.class);

		select=select(CUSTOM.AGE.max()).from(CUSTOM);
		Map map =session.fetchOneResult(select, Map.class);
        assertEquals(22, map.get("1"));
        
        select=selectFrom(CUSTOM).where(CUSTOM.AGE.in(1,5,10,22,25));
        custom=session.fetchOneResult(select,Custom.class);
        assertEquals("悠悠然然", custom.getName());

		Update update = update(CUSTOM).set(CUSTOM.NAME.value("flank"),
				CUSTOM.AGE.value(30)).where(CUSTOM.NAME.eq("悠悠然然"));
		affect = session.execute(update);
		assertEquals(1, affect);
		
		update=update(CUSTOM).set(CUSTOM.NAME.value(null),
				CUSTOM.ID.value(223),CUSTOM.AGE.value(null)).where(CUSTOM.NAME.eq("flank"));
		affect = session.execute(update);
		assertEquals(1, affect);
		
		select = selectFrom(CUSTOM).where(CUSTOM.NAME.eq("flank"));
		custom=session.fetchOneResult(select, Custom.class);
		assertNotNull(custom);
        assertEquals("flank", custom.getName());
        
        update=update(CUSTOM).set(CUSTOM.NAME.value(null),
				CUSTOM.AGE.value(30)).where(CUSTOM.NAME.eq("flank"));
        affect = session.execute(update,false);
		assertEquals(1, affect);
		select=select(CUSTOM.AGE.count()).from(CUSTOM).where(CUSTOM.NAME.eq("flank"));
		int count=session.count(select);
		assertEquals(0, count);
		
		
		update=update(CUSTOM).set(CUSTOM.NAME.value("flank")).where(CUSTOM.AGE.eq(30));
		affect = session.execute(update);
		assertEquals(1, affect);
        
		
		delete=delete(CUSTOM).where(and(CUSTOM.NAME.leftLike("a"), CUSTOM.AGE.between(1, 10)));
		affect = session.execute(delete);
		assertEquals(0, affect);
		
		
		delete=delete(CUSTOM).where(and(CUSTOM.NAME.leftLike(null), CUSTOM.AGE.between(1, 10)));
		affect = session.execute(delete);
		assertEquals(0, affect);

		delete = delete(CUSTOM).where(CUSTOM.NAME.eq("flank"));
		affect = session.execute(delete);
		assertEquals(1, affect);
		delete = delete(TSCORE).where(TSCORE.NAME.eq("悠悠然然"));
		affect = session.execute(delete);
	
	}
	
	public void testPage(){
		
		Delete delete = delete(CUSTOM);
		DslSession session = new SimpleDslSession(dataSource);
		session.execute(delete);

		for (int i = 0; i < 22; i++) {
			Insert customInsert = insertInto(CUSTOM).values(
					CUSTOM.ID.value("10001"+i), CUSTOM.NAME.value("悠悠然然"+i),
					CUSTOM.AGE.value(22+i));
			session.execute(customInsert);
		}
		
		Select select=selectFrom(CUSTOM);
		Pager<Custom> page=session.fetchPage(select, 0, 5, false, Custom.class);
		assertEquals(5, page.getRecords().size());
		assertEquals(22, page.getTotalCount());
		assertEquals(1, page.getCurrentPage());
		assertEquals(5, page.getTotalPages());
		page=session.fetchPage(select, 5, 5, false, Custom.class);
		assertEquals(5, page.getRecords().size());
		assertEquals(22, page.getTotalCount());
		assertEquals(2, page.getCurrentPage());
		assertEquals(5, page.getTotalPages());
		
		page=session.fetchPage(select,15, 5, false, Custom.class);
		assertEquals(5, page.getRecords().size());
		assertEquals(22, page.getTotalCount());
		assertEquals(4, page.getCurrentPage());
		assertEquals(5, page.getTotalPages());
		
		page=session.fetchPage(select, 20, 5, false, Custom.class);
		assertEquals(2, page.getRecords().size());
		assertEquals(22, page.getTotalCount());
		assertEquals(5, page.getCurrentPage());
		assertEquals(5, page.getTotalPages());
		
	}
	
}
