package util;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class JDBCUtil {

	public static Connection conn = null;
	public static final String url = "jdbc:mysql://localhost:3306/spider";
	public static final String username = "root";
	public static final String password = "123456";


	private JDBCUtil() {

	}

	public static Connection getConn(){
		if (conn == null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = (Connection) DriverManager.getConnection(url, username, password);
				conn.setEncoding("UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}

}
