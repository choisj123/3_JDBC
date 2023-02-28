package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.kh.emp.model.vo.Employee;

// DAO(Data Access Object, 데이터 접근 객체)
// -> 데이터베이스에 접근(연결)하는 객체
// --> JDBC 코드 작성
public class EmployeeDAO {
	

	
	private Connection conn; 
	private Statement stmt;  
	private ResultSet rs = null;	
							
	
	private PreparedStatement pstmt;

	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "kh";
	private String pw = "kh1234";
	
	
	
	/** 전체 사원 정보 조회 DAO
	 * @return
	 */
	public List<Employee> selectAll() {
		
		
	}
	
	
	/** 주민등록번호가 일치하는 사원 정보 조회 DAO
	 * @param 
	 * @return 
	 */
	public  selectEmpNo() {
		
	}
	
	
	
	/** 사원 정보 추가 DAO
	 * @param 
	 * @return 
	 */
	public insertEmployee() {
		
		
		
	}
	
	
	/** 사번이 일치하는 사원 정보 수정 DAO
	 * @param 
	 * @return 
	 */
	public  updateEmployee() {
	
	}
	
	
	/** 사번이 일치하는 사원 정보 삭제 DAO
	 * @param 
	 * @return 
	 */
	public  deleteEmployee() {
		

	}
	
	
	/** 입력 받은 부서와 일치하는 모든 사원 정보 조회 DAO
	 * @param 
	 * @return 
	 */
	public  selectDeptEmp() {
		
	
	}
	
	/** 사번이 일치하는 사원 정보 조회 DAO
	 * @param 
	 * @return 
	 */
	public selectEmpId( ) {
		
		
	}
	
	
	// alt + shift + j (메소드용 주석)
	/** 입력 받은 급여 이상을 받는 모든 사원 정보 조회 DAO
	 * @param 
	 * @return 
	 */
	public selectSalaryEmp() {
		

		
	}
	
	
	
	/** 직급별 급여 평균 조회 DAO
	 * @return 
	 */
	public selectJobAvgSalary() {
		
	}
	
}








