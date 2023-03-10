package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.kh.emp.model.vo.Employee;

// DAO(Data Access Object, 데이터 접근 객체)
// -> 데이터베이스에 접근(연결)하는 객체
// --> JDBC 코드 작성

// Run - View - (Service) - DAO - VO
public class EmployeeDAO {
	
	// JDBC 객체 참조 변수 선언 --> 필드 (class 내부에 공통 사용 가능)
	
	// Heap 영역에 생성
	
	private Connection conn; // null로 초기화 안해도 됨 -> Heap 영역은 초기값 자동 생성 (JVM)
	private Statement stmt;
	private ResultSet rs = null; // null로 초기화해도 상관없음!
	//필드 (Heap, 변수가 비어있을 수 없음)
	//-> JVM이 지정한 기본값으로 초기화
	//--> 참조형의 초기값은 null!! 별도 초기화 안해도 된다!
	
	/*
	public void method() {
		Connection conn2; // 지역변수(Stack, 변수가 비어있을 수 있음)
	}
	*/
	
	private PreparedStatement pstmt;
	// Statement의 자식으로 향상된 기능 제공
	// -> ? 기호 (placeholder / 위치 홀더)를 이용해서
	// SQL에 작성되어지는 리터럴을 동적으로 제어함
	
	// SQL ? 기호에 추가되는 값은
	// 숫자인 경우 ''없이 대입
	// 문자열인 경우 ''가 자동으로 추가되어 대입
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "kh";
	private String pw = "kh1234";
	
	
	
	/** 2. 전체 사원 정보 조회 DAO
	 * @return empList
	 */
	public List<Employee> selectAll(){
		// 1. 결과 저장용 변수 선언
		List<Employee> empList = new ArrayList<Employee>();
		
		try {
			// 2. JDBD 참조 변수에 객체 대입
			// -> conn, stmt, rs에 객체 대입
			Class.forName(driver); // oracle jdbc driver 객체 메모리로드
			
			conn = DriverManager.getConnection(url, user, pw);
			// oracle jdbc driver 객체를 이용하 db 접속 방법 생성
			
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, "
					+ "NVL(PHONE, '번호 입력 X') AS PHONE, "
						+ "NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, "
						+ "JOB_NAME, SALARY "
						+ "FROM EMPLOYEE "
						+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
						+ "JOIN JOB USING(JOB_CODE) "
						+ "ORDER BY EMP_ID";
			
			// Statement 객체 생성
			stmt = conn.createStatement();
			
			// SQL을 수행 후 결과(ReslutSet) 반환 받음
			rs = stmt.executeQuery(sql);
			
			// 3. 조회 결과를 얻어와 한 행씩 접근하여
			// Employee 객체 생성 후 컬럼값 옮겨 담기
			while(rs.next()) {
				int empId = rs.getInt("EMP_ID");
				// EMP_ID 컬럼은 문자열 컬럼이지만 
				// 저장된 값들이 모두 숫자형태
				// -> DB에서 자동으로 형변환 진행해서 얻어옴
				
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
				empList.add(emp); // List 담기
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			// 4. JDBC 객체 자원 반환
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 5. 결과 반환
		return empList;
	}
	
	
	//opt + comm + j
	/** 9. 주민등록번호가 일치하는 사원 정보 조회 DAO
	 * @param empNo
	 * @return emp
	 * 
	 */
	public Employee selectEmpNo(String empNo) {
		
		// 결과 저장용 변수 선언
		Employee emp = null;
		
		try {
			// Connection 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, "
					+ "NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, "
					+ "JOB_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
					+ "JOIN JOB USING(JOB_CODE) "
					+ "WHERE EMP_NO = ?";
								// placeholder
			// Statement 객체 사용 시 순서
			// SQL 작성 - Statement 생성 -> SQL 수행 후 결과 반환
			
			// PreparedStatement 객체 사용 시 순서
			// SQL 작성
			// -> PreparedStatement 객체 생성( ? 가 포함된 SQL을 매개변수로 사용)
			// -> ? 에 알맞은 값 대입
			// -> SQL 수행 후 결과 반입
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?(위치홀더)에 알맞은 값 대입
			pstmt.setString(1, empNo);
			
			// SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			// PreparedStatment는 객체 생성 시 이미 SQL이 담겨져 있는 상태이므로
			// SQL 수행(executeQuery) 시 매개변수로 전달할 필요가 없다!
			
			// pstmt.executeQuery(sql);
			// ?에 작성되어있던 값이 모두 사라져 수행 시 오류 발생
			
			// DB상에서 1명만 데려오므로 while문 필요 없음
			if(rs.next()) {
				int empId = rs.getInt("EMP_ID");
				
				String empName = rs.getString("EMP_NAME");
				//String empNo = rs.getString("EMP_NO"); --> 파라미터와 같은 값이므로 불필요
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return emp;
		
	}
	
	
	/**1. 사원 정보 추가
	 * @param emp
	 * @return result(Insert 성공한 행의 개수 반환)
	 */
	public int insertEmployee(Employee emp) {
		
		// 결과 저장용 변수 선언
		int result = 0;
		
		try{
			// 커넥션 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw); 
			// --> autoCommit 자동 생성됨
			
			// **DML 수행할 예정**
			// 트랜잭션에 DML 구문이 임시 저장
			// --> 정상적인 DML인지를 판별해서 개발자가 직접 commit, rollback을 수행
			
			// Connection 객체 생성 시 
			// AutoCommit이 활성화되어 있는 상태이기 때문에 이를 해제하는 코드를 추가!
			conn.setAutoCommit(false); // AutoCommit 비활성화
			
			// AutoCommit 비활성화 해도
			// conn.close(); 구문이 수행되면 자동으로 Commit이 수행됨
			// --> close() 수행 전에 트랜잭션 제어 코드를 작성해야 한다
			
			// DML
			
			String sql = "INSERT INTO EMPLOYEE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, NULL, DEFAULT)";
			// 퇴사 여부 컬럼의 DEFAULT == 'N'
			
			// PreparedStatement 객체 생성(매개변수에 SQL 추가)
			pstmt = conn.prepareStatement(sql);
			
			// ?(placeholder)에 알맞은 값 대입
			pstmt.setInt(1, emp.getEmpId());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getEmpNo());
			pstmt.setString(4, emp.getEmail());
			pstmt.setString(5, emp.getPhone());
			pstmt.setString(6, emp.getDeptCode());
			pstmt.setString(7, emp.getJobCode());
			pstmt.setString(8, emp.getSalLevel());
			pstmt.setInt(9, emp.getSalary());
			pstmt.setDouble(10, emp.getBonus());
			pstmt.setInt(11, emp.getManagerId());
			
			// SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate(); 
			// executeQuery() : SELECT 수행 후 ResultSet 반환
			// executeUpdate() : DML(INSERT,UPDATE,DELETE) 수행 후 결과 행 개수 반환
			
			
			// **트랜잭션 제어 처리**
			// -> DML 성공 여부에 따라서 commit, rollback 제어
			if(result > 0) conn.commit(); // DML 성공 시 commit
			else conn.rollback(); //DML 실패 시 rollback
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
		
		
	}
	
	
	/** 4. 사번이 일치하는 사원 정보 수정 DAO
	 * @param emp
	 * @return result
	 */
	public int updateEmployee(Employee emp) {
		int result = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false); //AutoCommit 비활성화
			
			String sql = "UPDATE EMPLOYEE SET "
						+ "EMAIL = ?, PHONE = ?, SALARY = ? "
						+ "WHERE EMP_ID = ? ";
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
					
			// ? 에 알맞은 값 세팅
			pstmt.setString(1, emp.getEmail());
			pstmt.setString(2, emp.getPhone());
			pstmt.setInt(3, emp.getSalary());
			pstmt.setInt(4, emp.getEmpId());
			
			result = pstmt.executeUpdate(); // 반영된 행의 개수 반환
			
			// 트랜잭션 제어 처리
			if(result == 0) conn.rollback();
			else conn.commit();
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	
	/** 5. 사번이 일치하는 사원 정보 삭제 DAO
	 * @param empId
	 * @return result
	 */
	public int deleteEmployee(int empId) {
		int result = 0;
		
		try {
			Class.forName(driver);
			
			conn = DriverManager.getConnection(url, user, pw);
			
			conn.setAutoCommit(false); //AutoCommit 비활성화
			
			String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
					
			// ? 에 알맞은 값 세팅
			pstmt.setInt(1, empId);
			
			result = pstmt.executeUpdate(); // 반영된 행의 개수 반환
			
			
			// 트랜잭션 제어 처리
			if(result == 0) conn.rollback();
			else conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
				
			}catch(SQLException e) {
				
			}
			
			
		}
		
		return result;
	}
	
	
	/** 6. 입력 받은 부서와 일치하는 모든 사원 정보 조회 DAO
	 * @param departmentTitle
	 * @return empList
	 */
	public List<Employee> selectDeptEmp(String departmentTitle) {
		
		List<Employee> empList = new ArrayList<>();
		
		try {
			// JDBC 드라이버 메모리 로드
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, "
					+ "NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, "
					+ "JOB_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
					+ "JOIN JOB USING(JOB_CODE)"
					+ "WHERE DEPT_TITLE = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, departmentTitle);
			
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO"); 
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				//String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
				// 리스트에 담기
				empList.add(emp);
				// 반환
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
			
		}
		
		return empList;
		
	}
	
	
	/** 6-1. 입력 받은 글자가 포함된 부서의 모든 사원 정보 조회 DAO
	 * @param departmentTitle
	 * @return empList
	 */
	public List<Employee> selectDeptEmp2(String inputDept) {
			
		List<Employee> empList = new ArrayList<>();
		
		try {
			// JDBC 드라이버 메모리 로드
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, "
					+ "NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, "
					+ "JOB_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
					+ "JOIN JOB USING(JOB_CODE)"
					+ "WHERE DEPT_TITLE LIKE '" + inputDept+ "%'";
			
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery(sql);
		
			while(rs.next()) {
				
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO"); 
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
				// 리스트에 담기
				empList.add(emp);
				// 반환
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
			
		}
		
	return empList;
			
	}
	
	
	/** 3. 사번이 일치하는 사원 정보 조회 DAO
	 * @param empId
	 * @return emp
	 */
	public Employee selectEmpId(int empId) {
		
		// 결과 저장용 변수 선언
		Employee emp = null;
		// 만약 조회 결과가 있으면 Employee 객체를 생성해서 emp에 대입(null이 아님)
		// 만약 조회 결과가 없으면 emp에 아무것도 대입하지 않음(null)
		
		try {
			// 오라클 JDBC 드라이버 메모리 생성
			// Connection 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, "
					+ "NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, "
					+ "JOB_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
					+ "JOIN JOB USING(JOB_CODE) "
					+ "WHERE EMP_ID = ?";
					// "WHERE EMP_ID =" + empId; // View에서 입력받은 사번
					// --> stmt 사용
				
			pstmt = conn.prepareStatement(sql);
			//stmt = conn.createStatement();

			pstmt.setInt(1, empId);
			
			// SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			// rs = stmt.excuteQuery(sql);
			
			// **조회 결과가 최대 1행인 경우
			//	 불필요한 조건 검사를 줄이기 위해서 if문 사용 권장!
			
			
			if(rs.next()) {
				
				//empId = rs.getInt("EMP_ID"); //--> 파라미터와 같은 값이므로 불필요
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_No"); 
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				// 객체 생성 역순으로 닫기! (권장사항)
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return emp;
	}

	
	/** 7. 입력 받은 급여 이상을 받는 모든 사원 정보 조회 DAO
	 * @param salary
	 * @return empList
	 */
	public List<Employee> selectSalaryEmp(int inputSalary){
		
		List<Employee> empList = new ArrayList<>();
		
		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, "
					+ "NVL(DEPT_TITLE, '부서없음') AS DEPT_TITLE, "
					+ "JOB_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID) "
					+ "JOIN JOB USING(JOB_CODE)"
					+ "WHERE SALARY >= ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, inputSalary);
			
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO"); 
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				
				empList.add(emp);
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
			
		}
		
		return empList;
		
		
	}
	
	/** 8. 부서별 급여 합 전체 조회 DAO
	 * @return empMap
	 */
	public Map<String, Integer> selectDeptTotalSalary(){
		
		//		K,   V
		//Map<String, Integer> empMap = new HashMap<>();
		Map<String, Integer> empMap = new LinkedHashMap<>();
		// LinkedHashMap : key 순서가 유지되는 HashMap
		// -> (ORDER BY절 정렬 결과를 그대로 저장 가능)
		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT NVL(DEPT_CODE, '부서없음') AS DEPT_CODE, SUM(SALARY) AS SALARY "
					+ "FROM EMPLOYEE "
					+ "GROUP BY DEPT_CODE "
					+ "ORDER BY DEPT_CODE"; // Map에서는 ORDER BY 적용 X -> LinkedHashMap 사용!
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				String deptCode = rs.getString("DEPT_CODE");
				int salary = rs.getInt("SALARY");
				empMap.put(deptCode, salary);
				
				
				//empMap.put(rs.getString("DEPT_CODE"), rs.getInt("SALARY"));
			
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
		
		//return map;
		return empMap;
		
	}
	
	
	/** 10. 직급별 급여 평균 조회 DAO
	 * @return empMap
	 */
	public Map<String, Double> selectJobAvgSalary(){
		
		Map<String, Double> empMap = new LinkedHashMap<>();
		
		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			String sql = "SELECT JOB_NAME, ROUND(AVG(SALARY),1) AS SALARY "
					+ "FROM EMPLOYEE "
					+ "JOIN JOB USING(JOB_CODE) "
					+ "GROUP BY JOB_NAME, JOB_CODE "
					+ "ORDER BY JOB_CODE";
			// GROUP BY절에 JOB_CODE를 포함시켜
			// ORDER BY절 정렬 컬럼으로 사용 가능하게 만든다!
			
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				String jobName = rs.getString("JOB_NAME");
				double salary = rs.getDouble("SALARY");
				
				empMap.put(jobName, salary);
			
			
			}
			
			/*
			// SQL 작성
			String sql = "SELECT JOB_CODE, ROUND(AVG(SALARY),1) AS SALARY "
					+ "FROM EMPLOYEE "
					+ "GROUP BY JOB_CODE "
					+ "ORDER BY JOB_CODE";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				empMap.put(rs.getString("JOB_CODE"), rs.getDouble("SALARY"));
			
			}
			*/
			
		}catch(Exception e) {
			
			
		}finally {
			
			try {
				
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
				
			}
		}
		
		return empMap;
		
	}
	
	
}








