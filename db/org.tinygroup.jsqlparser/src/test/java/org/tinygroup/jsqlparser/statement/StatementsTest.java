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
package org.tinygroup.jsqlparser.statement;

import org.junit.*;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserUtil;
import org.tinygroup.jsqlparser.statement.select.Select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author toben
 */
public class StatementsTest {
    
    public StatementsTest() {
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

    /**
     * Test of toString method, of class Statements.
     */
    @Test
    public void testStatements() throws JSQLParserException {
        String sqls = "select * from mytable; select * from mytable2;";
        Statements parseStatements = CCJSqlParserUtil.parseStatements(sqls);
        
        assertEquals("SELECT * FROM mytable;\nSELECT * FROM mytable2;\n", parseStatements.toString());
        
        assertTrue(parseStatements.getStatements().get(0) instanceof Select);
        assertTrue(parseStatements.getStatements().get(1) instanceof Select);
    }
    
}
