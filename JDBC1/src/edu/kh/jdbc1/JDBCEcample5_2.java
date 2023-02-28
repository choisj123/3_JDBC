package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc1.model.vo.Employee5_2;

public class JDBCEcample5_2 {
	

	public static void main(String[] args) {
		// 입사일을 입력("2022-09-06) 받아
		// 입력 받은 값보다 먼저 입사한 사람의
		// 이름, 입사일, 성별(M,F) 조회
		
		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		try {
			System.out.print("입사일을 입력하세요 (YYYY-MM-DD) : ");
			String input = sc.next();
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(url, user, pw);
			
			String sql = "SELECT EMP_NAME , TO_CHAR(HIRE_DATE, 'YYYY\"년\" MM\"월\" DD\"일\"') AS \"HIRE_DATE\" , \n"
						+ "		DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') AS GENDER \n"
						+ "		FROM EMPLOYEE \n"
						+ "		WHERE HIRE_DATE < TO_DATE('" + input + "')";
						
			// 문자열 내부에 쌍따옴표 작성시 \"로 작성해야 한다 (excape 문자)
			
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			List<Employee5_2> list = new ArrayList<Employee5_2>();
			
			while(rs.next()) {
				
				String empName = rs.getString("EMP_NAME");
				String hireDate = rs.getString("HIRE_DATE");
				String gender = rs.getString("GENDER");
				
				list.add(new Employee5_2(empName, hireDate, gender));
				
			}
				if(list.isEmpty()) {
					System.out.println("조회 결과 없음");
				} else {
					for(Employee5_2 emp : list) {
						System.out.println(emp);
					}
				}
				
				
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			try {
				
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		
		
	}


}
