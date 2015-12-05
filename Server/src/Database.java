import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Database {
	private static final int UPDATE = 0x01;
	private static final int QUERY = 0x02;
	
	Connection con;//db와 인터넷 연결
	Statement st;//상태를 나타내는 변수
	ResultSet rs;
	PreparedStatement pstmt;//쿼리를 넣고 다음에 나온 db 아웃풋 
	
	public Database(){
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		pstmt = null;
	}
	
	public boolean connect(){
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/AndroidDB","AndroidDB", "AndroidDB");
			st = con.createStatement();
		}catch(SQLException e){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			return false;
		}
		return true;
	}
	
	public Statement getStatement(){
		return st;
	}
	
	public Connection getConnection(){
		return con;
	}
	
	public void setPreparedStatement(String query){
		try{
			pstmt = con.prepareStatement(query);
		}catch(SQLException e){//db가 예외를 뱉었다면 있다면 이를 알려준다.
			printError(e, query);
		}
	}
	
	public PreparedStatement getPreparedStatement(){
		return pstmt;
	}
		
	public void printError(SQLException e, String query){
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("Query: " + query);
	}
	
	public void clear(){
		String query = "";
		try{
			query = "delete from login_data";
			
			st.executeQuery(query);
			
			query = "alter table login_data auto_increment = 1";
			
			st.executeQuery(query);
		}catch(SQLException e){
			printError(e, query);
		}
	}
}
