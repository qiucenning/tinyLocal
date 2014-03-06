package org.tinygroup.dbrouterjdbc3.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.threadgroup.MultiThreadProcessor;

import junit.framework.TestCase;

public class CommonJDBCTest extends TestCase {

	public static String driverName = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.51.29:3306/testA";
	public static String user = "root";
	public static String password = "123456";
	public static StandardXADataSource dataSource = new StandardXADataSource();

	public static Random rand = new Random();
	public static int COUNT = 0;
	public static int SUM = 100000;

	static {
		try {
			dataSource.setUrl(url);
			dataSource.setDriverName(driverName);
			dataSource.setUser(user);
			dataSource.setPassword(password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection connect() throws SQLException {
		return dataSource.getXAConnection().getConnection();
	}

	public static int getId() throws SQLException {
		return Math.abs(rand.nextInt());
	}

	public void testCommonJDBC() throws SQLException, ClassNotFoundException {
		Class.forName(driverName);
		Connection connection = DriverManager
				.getConnection(url, user, password);
		Statement statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE,
				102);
		ResultSet rs = null;

		statement.executeUpdate("delete from teacher");
		statement
				.executeUpdate("insert into teacher(id,name) values(1,'zhang')");
		statement
				.executeUpdate("insert into teacher(id,name) values(2,'qian')");
		statement.executeUpdate("insert into teacher(id,name) values(3,'sun')");
		statement
				.executeUpdate("insert into teacher(id,name) values(4,'wang')");
		statement
				.executeUpdate("insert into teacher(id,name) values(5,'chen')");

		rs = statement
				.executeQuery("select avg(id),sum(id),max(id),min(id) from teacher");
		rs.first();
		assertEquals(3, rs.getInt(1));
		assertEquals(15, rs.getInt(2));
		assertEquals(5, rs.getInt(3));
		assertEquals(1, rs.getInt(4));
	}

	public static void close(Connection conn, Statement st, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MultiThreadProcessor processors = new MultiThreadProcessor(
				"thread-group1");
		for (int i = 0; i < 10; i++) {
			InsertThread processor = new InsertThread("thread" + i); // 添加线程
			processors.addProcessor(processor);
		}
		processors.start(); // 启动线程组
	}

	static class InsertThread extends AbstractProcessor {

		public InsertThread(String name) {
			super(name);
		}

		protected void action() throws Exception {
			Connection conn = connect();
			for (; COUNT++ < SUM;) {
				Statement st = conn.createStatement();
				st.executeUpdate("insert into teacher(id,name) values("
						+ getId() + ",'zhang')");
				close(conn, st, null);
			}
		}

	}

}
