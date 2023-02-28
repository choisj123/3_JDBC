package edu.home.jdbc1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExHome {

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String type = "jdbc:oracle:thin:@";
			String ip = "127.0.0.1";
			String port = ":1521";
			String sid = ":XE";
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY, HIRE_DATE FROM EMPLOYEE";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				Date hireDate = rs.getDate("HIRE_DATE");
				
				System.out.printf("사번 : %s / 이름 : %s / 급여 : %d / 입사일 : %s\n", empId, empName, salary, hireDate);
				
			}
			
		}catch(ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 경로가 잘못 작성되었습니다.");
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			
					try {
						if(rs != null) rs.close();
						if(stmt != null )stmt.close();
						if(conn != null) conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		}
				
			
			
			
		}
		
		
		

	}


