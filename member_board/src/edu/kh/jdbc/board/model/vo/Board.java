package edu.kh.jdbc.board.model.vo;

import java.util.List;


// 게시글 1개 정보를 저장하는 VO
public class Board {
	private int boardNo; // 게시글 번호
	private String boardTitle;  // 제목
	private String boardContent; // 내용
	private String boardCreateDate; // 작성일
	private int readCount; // 조회수
	private String memberName; // 작성자명
	private int countComment; // 댓글 수
	
	private int memberNo;

	public List<Comment> getCommentList() {
		return commentList;
	}


	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}


	public int getMemberNo() {
		return memberNo;
	}


	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}


	private String boardDeleteFlag; // 삭제여부
	
	private List<Comment> commentList;// 댓글 목록
	
	
	//기본 생성자
	public Board() {}

	
	//1. 게시글 목록 조회(작성일 내림차순) 
	//	 (게시글 번호, 제목[댓글 수], 작성자명, 작성일, 조회수 )
	public Board(int boardNo, String boardTitle, int countComment, String memberName, String boardCreateDate, int readCount) {
		super();
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.countComment = countComment; 
		this.memberName = memberName;
		this.boardCreateDate = boardCreateDate;
		this.readCount = readCount;
	}
	
//	* 2. 게시글 상세 조회(게시글 번호 입력 받음)
//	 *    (게시글 번호, 제목, 내용, 작성자명, 작성일, 조회수, 
//	 *     댓글 목록(작성일 오름차순 )
	
	public Board(int boardNo, String boardTitle, String boardContent, String memberName,  String boardCreateDate,
			int readCount) {
		super();
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.memberName = memberName;
		this.boardCreateDate = boardCreateDate;
		this.readCount = readCount;
	}
	
	
	

	public int getCountComment() {
		return countComment;
	}




	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}


	public int getBoardNo() {
		return boardNo;
	}


	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}


	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getBoardContent() {
		return boardContent;
	}


	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}


	public String getBoardCreateDate() {
		return boardCreateDate;
	}


	public void setBoardCreateDate(String boardCreateDate) {
		this.boardCreateDate = boardCreateDate;
	}


	public int getReadCount() {
		return readCount;
	}


	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}


	public String getBoardDeleteFlag() {
		return boardDeleteFlag;
	}


	public void setBoardDeleteFlag(String boardDeleteFlag) {
		this.boardDeleteFlag = boardDeleteFlag;
	}


	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", boardTitle=" + boardTitle + ", memberName=" + memberName
				+ ", boardContent=" + boardContent + ", boardCreateDate=" + boardCreateDate + ", readCount=" + readCount
				+ ", boardDeleteFlag=" + boardDeleteFlag + "]";
	}
	
	
	

	
	
}


	