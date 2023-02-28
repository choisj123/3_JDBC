package edu.kh.jdbc.main.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.member.model.vo.Member;

import static edu.kh.jdbc.common.JDBCTemplate.*;

//DAO(Data Access Object) : DB 연결용 객체
public class MainDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;
	// Map<String, String> 제한, XML 파일을 읽고 쓰는데 특화되어 있음
	
	// 기본생성자
	public MainDAO() {
		
		try {
			// Properties 객체 생성
			prop = new Properties();
			
			// main-query.xml 파일의 내용을 읽어와 Properties 객체에 저장
			prop.loadFromXML(new FileInputStream("main-query.xml"));
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
	
	/** 1. 로그인 DAO
	 * @param conn
	 * @param memberId
	 * @param memberPw
	 * @return loginMember
	 * @throws Exception
	 */
	public Member login(Connection conn, String memberId, String memberPw) throws Exception{
		
		// 1. 결과 저장용 변수 선언
		Member loginMember = null;
		
		try {
			// 2. SQL 얻어오기 (main-query.xml에 작성 SQL)
			String sql = prop.getProperty("login");
			/*
			 * SELECT MEMBER_NO , MEMBER_ID , MEMBER_NM , MEMBER_GENDER ,
				TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS') ENROLL_DATE 
				FROM "MEMBER" 
				WHERE MEMBER_ID = ? 
				AND MEMBER_PW = ? 
				AND SECESSION_FL = 'N'
			 */
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ?에 알맞은 값 대임
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			
			// 5. SQL(SELECT) 수행 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과가 있을 경우 컬럼값을 모두 얻어와 
			// Member 객체를 생성해서 loginMember에 대입
			if(rs.next()) {
				loginMember = new Member(rs.getInt("MEMBER_NO"),
						memberId, 
						rs.getString("MEMBER_NM"),
						rs.getString("MEMBER_GENDER"),
						rs.getString("ENROLL_DATE")
						);
			}
			
		}finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
			
		}
		
		// 8. 결과 반환
		return loginMember;
	}

	/** 아이디 중복 검사 DAO
	 * @param conn
	 * @param memberId
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(Connection conn, String memberId) throws Exception{
		// 1. 결과 저장용 변수 
		int result = 0;
				
		try {
			// 2. SQL 얻어오기
			String sql = prop.getProperty("idDupCheck");
			
			// 3. PreaparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 알맞은 값 세팅
			pstmt.setString(1, memberId);
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			// COUNT(*)
			// 		1
			// ==> ResultSet
			
			// 6. 조회 결과 옮겨 담기
			// 1행 조회 -> if
			// N행 조회 -> while
			if(rs.next()) {
				//result = rs.getInt("COUNT(*)");
				result = rs.getInt(1); // 컬럼 순서 --> 여러개 이상일 때는 안쓰는게 좋음(명시적으로 쓰기!)
			}
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
		}
		// 8. 결과 반환
		return result;
	}


	/** 2. 회원가입 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Connection conn, Member member) throws Exception{
		int result = 0;
		
		try {
			String sql = prop.getProperty("signUp");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getMemberGender());
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
			
		}
			
		return result;
			
	}
	



}
