package edu.kh.jdbc.board.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;
import edu.kh.jdbc.member.model.vo.Member;

import static edu.kh.jdbc.common.JDBCTemplate.*;

/**
 * @author sujinchoi
 *
 */
public class BoardDAO {

	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs; 
	
	private Properties prop;
	
	// 기본 생성자
	public BoardDAO() {
		try {
			prop = new Properties();
			
			prop.loadFromXML(new FileInputStream("board-query.xml"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**1. 게시글 목록 조회 DAO
	 * @param conn
	 * @param loginMember
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard(Connection conn) throws Exception{
	
		List<Board> boardList = new ArrayList<>();
		
		try {
			String sql = prop.getProperty("selectAllBoard");
			
		
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				int countComment = rs.getInt("COUNT_COMMENT");
				String memberName = rs.getString("MEMBER_NM");
				String boardCreateDate = rs.getString("CREATE_DT");
				int readCount = rs.getInt("READ_COUNT");
				
			
				boardList.add(new Board(boardNo, boardTitle, countComment, memberName, boardCreateDate, readCount));
			
			}
			
		}finally {
			close(rs);
			close(stmt);
		}
		
		return boardList;
	}

	

	/** 게시글 상세 조회 DAO
	 * @param conn
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(Connection conn, int boardNo) throws Exception {
		// 결과 저장용 변수 선언
		Board board = null;
		
		try {
			String sql = prop.getProperty("selectBoard");	// SQL 얻어오기
			
			pstmt = conn.prepareStatement(sql);	// PreparedStatement 생성
			
			pstmt.setInt(1, boardNo); // ? 알맞은 값 대입
			
			rs = pstmt.executeQuery(); // SQL(SELECT) 수행 후 결과(ResultSet) 반환 받기
			
			if(rs.next()) { // 조회 결과가 있을 경우
				board = new Board(); // Board 객체 생성 == board는 null 아님
				
				board.setBoardNo( 		rs.getInt	("BOARD_NO") 		);
				board.setBoardTitle( 	rs.getString("BOARD_TITLE") 	);
				board.setBoardContent( 	rs.getString("BOARD_CONTENT") 	);
				board.setMemberNo( 		rs.getInt	("MEMBER_NO") 		);
				board.setMemberName( 	rs.getString("MEMBER_NM")		);
				board.setReadCount( 	rs.getInt	("READ_COUNT") 		);
				board.setBoardCreateDate(	rs.getString("CREATE_DT") 		);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return board; // 조회 결과 결과
	}

	
	/** 조회 수 증가 DAO
	 * @param conn 
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int boardNo) throws Exception {
		
			
			int result = 0;
			
			try {
				String sql = prop.getProperty("increaseReadCount");
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, boardNo);
				
				result = pstmt.executeUpdate();
				
			}finally {
				close(pstmt);
			}
			
			return result;
		}


	



	/** 게시글 작성 서비스 with  썜
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Board board) throws Exception{
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("insertBoard");
//			INSERT INTO BOARD 
//			VALUES (SEQ_BOARD_NO.NEXTVAL, ?, ?, DEFAULT, DEFAULT, ?)
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, board.getBoardNo());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setInt(4, board.getMemberNo());
			
			result = pstmt.executeUpdate();
			

			
		}finally {
			close(pstmt);
			
		}
		return result;
	}


	/** 다음 게시글 번호 생성 DAQ
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int nextBoardNo(Connection conn) throws Exception {
		int boardNo = 0;
		
		try {
			String sql = prop.getProperty("nextBoardNo");
			
			//그냥 pstmt 사용
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) { //  조회 결과 1행!
				boardNo = rs.getInt(1); // 첫번째 컬럼 값을 얻어와서 boardNo에 저장
			}
			
		}finally {
			close(rs);
			close(pstmt);
			
		}
		return boardNo;
	}

		
		
		
	/** 게시글 수정 DAO
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Board board) throws Exception{
		int result = 0;
		
		try {
			String sql = prop.getProperty("updateBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardNo());
			
			result = pstmt.executeUpdate();
			
			
		}finally {
			close(pstmt);
		}

		return result;
	}

	
	/** 게시글 삭제 DAO
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteBoard(Connection conn, int boardNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("deleteBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}

		return result;
	}		
	

	/** 게시글 검색 DAO
	 * @param conn
	 * @param condition
	 * @param query
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> searchBoard(Connection conn, int condition, String query) throws Exception{
		return null;

		

	}
	
	
	
	
	
}
