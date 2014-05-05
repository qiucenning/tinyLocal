package org.tinygroup.jsqlparser.util;

import org.junit.*;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.select.Select;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author tw
 */
public class AddAliasesVisitorTest {

	public AddAliasesVisitorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	/**
	 * Test of visit method, of class AddAliasesVisitor.
	 */
	@Test
	public void testVisit_PlainSelect() throws JSQLParserException {
		String sql = "select a,b,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a AS A1, b AS A2, c AS A3 FROM test", select.toString());
	}

	@Test
	public void testVisit_PlainSelect_duplicates() throws JSQLParserException {
		String sql = "select a,b as a1,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a AS A2, b AS a1, c AS A3 FROM test", select.toString());
	}

	@Test
	public void testVisit_PlainSelect_expression() throws JSQLParserException {
		String sql = "select 3+4 from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT 3 + 4 AS A1 FROM test", select.toString());
	}

	/**
	 * Test of visit method, of class AddAliasesVisitor.
	 */
	@Test
	public void testVisit_SetOperationList() throws JSQLParserException {
		String sql = "select 3+4 from test union select 7+8 from test2";
		Select setOpList = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		setOpList.getSelectBody().accept(instance);

		assertEquals("(SELECT 3 + 4 AS A1 FROM test) UNION (SELECT 7 + 8 AS A1 FROM test2)", setOpList.toString());
	}
}
