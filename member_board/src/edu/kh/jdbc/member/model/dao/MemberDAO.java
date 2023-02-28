package edu.kh.jdbc.member.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.member.model.vo.Member;

import static edu.kh.jdbc.common.JDBCTemplate.*;

public class MemberDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;
	
	// 기본 생성자
	public MemberDAO() {
		try {
			prop = new Properties();
			
			prop.loadFromXML(new FileInputStream("member-query.xml"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	
	
	/** 2. 회원 목록 조회(아이디, 이름, 성별) DAO
	 * @param conn
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll(Connection conn) throws Exception{
		
		//결과 저장용 변수 선언
		List<Member> memberList = new ArrayList<>();
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("selectAll");
			
			// Statement 객체 생성
			stmt = conn.createStatement();
			
			// SQL(SELECT) 수행 후 결과(Result Set) 반환받기
			rs = stmt.executeQuery(sql);
			
			// 반복문을 이용해서 조회 결과의 각 행에 접근
			while(rs.next()) {
				//컬럼 값을 얻어와 Member 객체 저장 후 List에 추가
				
				String memberId = rs.getString("MEMBER_ID");
				String memberName = rs.getString("MEMBER_NM");
				String memberGender = rs.getString("MEMBER_GENDER");
				
				memberList.add(new Member(memberId, memberName, memberGender));
			}
			
		}finally {
			close(rs);
			close(stmt);
		}
		
		return memberList;
	}



	/** 3. 내 정보 수정(이름, 성별) DAO
	 * @param conn
	 * @param LoginMember
	 * @param memberName
	 * @param memberGender
	 * @return result
	 * @throws Exception
	 */
	public int updateMember(Connection conn, Member member) throws Exception{
		
		//결과 저장용 변수 생성
		int result = 0;
		
		try {
			//SQL 얻어오기
			String sql = prop.getProperty("updateMember");
			
			//PreparedfStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ? 알맞은 값 대입
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getMemberGender());
			pstmt.setInt(3, member.getMemberNo());
			
			// SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
			
		}
		return result;
	}




	/** 4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인) DAO
	 * @param conn
	 * @param LoginMember
	 * @param newPw
	 * @return result
	 * @throws Exception
	 */
	public int updatePw(Connection conn, int memberNo, String newPw) throws Exception{
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("updatePw");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, newPw);
			pstmt.setInt(2, memberNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
			
		}
		return result;
	}




	/** 현재 비밀번호 조회 DAO 
	 * @param conn
	 * @param LoginMember
	 * @return memberPw
	 * @throws Exception
	 */
	public String getPw(Connection conn, Member loginMember) throws Exception{
		String memberPw = null;
		
		try {
			String sql = prop.getProperty("selectPw");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, loginMember.getMemberNo());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memberPw = rs.getString("MEMBER_PW");
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		return memberPw;
	}




	/** 5. 회원 탈퇴 DAO
	 * @param conn
	 * @param LoginMember
	 * @return result
	 * @throws Exception
	 */
	public int secession(Connection conn, int memberNo) throws Exception{
		int result = 0;
		
		try {
		
			String sql = prop.getProperty("secession");

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, memberNo);
			
			result = pstmt.executeUpdate();
			
			
		}finally {
			close(pstmt);
			
		}
		
		return result;
	}

}
