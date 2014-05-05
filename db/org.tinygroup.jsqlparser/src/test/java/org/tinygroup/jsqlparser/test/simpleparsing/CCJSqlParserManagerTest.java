package org.tinygroup.jsqlparser.test.simpleparsing;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.test.TestException;
import org.tinygroup.jsqlparser.test.create.CreateTableTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

public class CCJSqlParserManagerTest extends TestCase {

	public CCJSqlParserManagerTest(String arg0) {
		super(arg0);
	}

	public void testParse() throws Exception {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		BufferedReader in = new BufferedReader(new InputStreamReader(CreateTableTest.class.getResourceAsStream("/simple_parsing.txt")));

		String statement = "";
		while (true) {
			try {
				statement = CCJSqlParserManagerTest.getStatement(in);
				if (statement == null) {
					break;
				}

				parserManager.parse(new StringReader(statement));
			} catch (JSQLParserException e) {
				throw new TestException("impossible to parse statement: " + statement, e);
			}
		}
	}

	public static String getStatement(BufferedReader in) throws Exception {
		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = CCJSqlParserManagerTest.getLine(in)) != null) {

			if (line.length() == 0) {
				break;
			}

			buf.append(line);
			buf.append("\n");

		}

		if (buf.length() > 0) {
			return buf.toString();
		} else {
			return null;
		}

	}

	public static String getLine(BufferedReader in) throws Exception {
		String line = null;
		while (true) {
			line = in.readLine();
			if (line != null) {
				line.trim();
				// if ((line.length() != 0) && ((line.length() < 2) || (line.length() >= 2) && !(line.charAt(0) == '/'
				// && line.charAt(1) == '/')))
				if (((line.length() < 2) || (line.length() >= 2) && !(line.charAt(0) == '/' && line.charAt(1) == '/'))) {
					break;
				}
			} else {
				break;
			}

		}

		return line;
	}
}
