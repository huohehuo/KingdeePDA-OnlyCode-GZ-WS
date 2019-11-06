package Utils;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

public class JDBCUtil {
	
	
	
	public static Connection getConn(String url,String password,String user) throws SQLException, ClassNotFoundException{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return DriverManager.getConnection(url, user, password);
		
	}
	public static Connection getConn(HttpServletRequest request) throws SQLException, ClassNotFoundException{
		System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return DriverManager.getConnection(getDataBaseUrl.getUrl(request), request.getParameter("sqluser"), request.getParameter("sqlpass"));
	}
	public static Connection getSQLiteConn() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite://c:/properties/dbsetfile.db");
		return conn;
	}

	public static Connection getSQLiteConn1() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite://c:/properties/dbother.db");
		return conn;
	}
	public static void close(ResultSet rs,PreparedStatement sta,Connection connection){
		if(rs!=null){
			try {

				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sta!=null){
			try {
				sta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
