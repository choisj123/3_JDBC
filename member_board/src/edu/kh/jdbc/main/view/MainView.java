package edu.kh.jdbc.main.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.board.view.BoardView;
import edu.kh.jdbc.main.model.service.MainService;
import edu.kh.jdbc.member.model.vo.Member;
import edu.kh.jdbc.member.view.MemberView;

public class MainView {
	
	
	private Scanner sc = new Scanner(System.in);
	
	private MainService service = new MainService();
	
	// 로그인된 회원 정보를 저장한 객체를 참조하는 참조 변수
	public static Member loginMember = null; // static 전달하지 않아도 어디서든 사용가능
	// -> 로그인 X == null
	// -> 로그인 O != null
	
	
	//회원기능 메뉴 객체 생성
	private MemberView memberView = new MemberView();
	
	//게시판 기능 메뉴 객체 생성
	private BoardView boardView = new BoardView();
	
	
	
	
	/**
	 * 메인 메뉴 출력 메서드
	 */
	public void mainMenu() {
		/*
		 * <비회원용 - 로그인 안했을 때>
		 * 1. 로그인
		 * 2. 회원가입
		 * 0. 프로그램 종료
		 * 
		 * <회원용 - 로그인 했을 때>
		 * 1. 회원 기능
		 * 2. 게시판 기능
		 * 0. 로그아웃
		 * 99. 프로그램 종료
		 * 
		 */
		
		int input = -1;
		
		do {
			
			try {
				if(loginMember == null) { //로그인 X
				
					System.out.println("<<<< 회원제 게시판 프로그램 >>>>");
					System.out.println("1. 로그인");
					System.out.println("2. 회원가입");
					System.out.println("0. 프로그램 종료");
					
					System.out.print("\n메뉴 선택 >> ");
					input = sc.nextInt();
					sc.nextLine(); //입력 버퍼 개행문자 제거
					System.out.println();
					
					switch(input) {
					case 1: login(); break; //로그인
					case 2: signUp(); break; //회원가입
					case 0:  
						System.out.println("프로그램을 종료합니다..."); break; //프로그램 종료
					default: 
						System.out.println("메뉴에 작성된 번호만 입력해주세요.");
					}
					
				}else {  //로그인 O
					System.out.println("\n<<회원 메뉴>>");
					System.out.println("1. 회원 기능");
					System.out.println("2. 게시판 기능");
					System.out.println("0. 로그아웃");
					System.out.println("99. 프로그램 종료");
					
					System.out.print("\n메뉴 선택 >> ");
					input = sc.nextInt();
					
					System.out.println();
					
					switch(input) {
					// 회원 기능 서브 메뉴 출력
					case 1: memberView.memberMenu(loginMember); break;  // MemberView
					// 게시판 기능 서브 메뉴 출력
					case 2: boardView.boardMenu(); break;  // BoardView
						//-->회원 정보가 필요할 경우 static에서 얻어와 사용할 예정
					
					case 0: 
						// 로그아웃 == loginMember가 참조하는 객체 없음(==null)
						loginMember = null; 
						System.out.println("[로그아웃 되었습니다]"); 
						input = -1; // do~while문이 종료되지 않도록 0이 아닌 값으로 변경
						break;
					case 99: 
						System.out.println("프로그램을 종료합니다...");  // 프로그램 종료
						System.exit(0); // JVM 종료, 매개변수는 0, 아니면 오류를 의미
						break;
					default: System.out.println("메뉴에 작성된 번호만 입력해주세요.");
					}
					
					System.out.println();
					
				}
				
			}catch(InputMismatchException e) {
				System.out.println("<<입력 형식이 올바르지 않습니다>>\n");
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자열 제거
				
			}
			
			
		}while(input != 0);
		
	}

	/**
	 * 1. 로그인 화면
	 */
	private void login() {
		System.out.println("[로그인]");
		
		
		try {
			System.out.print("아이디 : ");
			String memberId = sc.next();
			
			System.out.print("비밀번호 : ");
			String memberPw = sc.next();
			
			// 로그인 서비스 호출 후 조회 결과를 LoginMember에 저장
			loginMember = service.login(memberId, memberPw);
			
			System.out.println();
			
			if(loginMember != null) { //로그인 성공 시
				System.out.println(loginMember.getMemberName() + "님 환영합니다!");
				
			}else { //로그인 실패 시
				System.out.println("[아이디 또는 비밀번호 비일치] \n로그인에 실패하였습니다.\n");
			}
			
			
		} catch(Exception e) {
			System.out.println("\n<<로그인 중 예외 발생>>\n");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 2. 회원가입 화면
	 */
	private void signUp() {
		System.out.println("[회원가입]");
		
		String memberId = null;
		
		String memberPw = null;
		String memberPw2 = null;
		
		String memberName = null;
	    String memberGender = null;
		
		try {
			
			//아이디를 입력 받아 중복이 아닐 때 까지 반복
			while(true) {
				
				System.out.print("아이디 : ");
				memberId = sc.next();
				// 입력받은 아이디를 매개변수로 전달하여
				// 중복 여부를 검사하는 서비스 호출 후 결과 반환 받기
				// 있으면 1 없으면 0
				
				int result = service.idDupCheck(memberId);
				// 1 / 0
				
				
				if(result == 0) { // 중복이 아닌 경우
					System.out.println("[사용가능한 아이디 입니다]\n");
					break;
					
				}else { // 중복인 경우
					System.out.println("[이미 사용 중인 아이디입니다]\n");
				}
				
				System.out.println();
			}
			
			
			// 비밀번호 입력
			// 비밀번호 / 비밀번호 확인이 일치할 때까지 무한 반복
			
			while(true) {
				
				System.out.print("비밀번호 : ");
				memberPw = sc.next();
				
				System.out.print("비밀번호 확인: ");
				memberPw2 = sc.next();
				
				if(memberPw.equals(memberPw2)) { //일치할 경우
					System.out.println("[비밀번호 일치]\n");
					break;
					
				} else { //일치하지 않을 경우
					System.out.println("[비밀번호가 일치하지 않습니다]\n");
				}
				System.out.println();
			}
			
			// 이름 입력
			System.out.print("이름 : ");
			memberName = sc.next();
			System.out.println();
				
			// 성별 입력
			// M 또는 F가 입력될 때까지 무한 반복
			while(true) {
				
				System.out.print("성별 (M/F) : ");
				memberGender = sc.next().toUpperCase();
				
				if(memberGender.equals("M") || memberGender.equals("F")) {
					break;
					
				}else {
					System.out.println("[M 또는 F로 입력해주세요]\n");
				}
				
			}
			
			// 아이디 , 비밀번호, 이름, 성별 입력 완료
			// -> 하나의 VO에 담아서 서비스 호출 후 결과 반환 받기
			Member member = new Member(memberId, memberPw, memberName, memberGender);
					
			int result = service.signUp(member);
			
			// 서비스 처리 결과에 따른 출력 화면 제어
			System.out.println();
			
			if(result > 0 ) {
				System.out.println("[회원가입 성공!]");
				System.out.println(memberName + "님 환영합니다!");
			} else {
				System.out.println("[회원가입 실패...]");
			} 
			System.out.println();
			
		}catch(Exception e) {
			System.out.println("<<회원가입중 예외 발생 >>");
			e.printStackTrace();
		
		}
	
	}

	/* 회원기능 (Member View, Service, DAO, member-query.xml)
	 * 
	 * 1. 내 정보 조회
	 * 2. 회원 목록 조회(아이디, 이름, 성별)
	 * 3. 내 정보 수정(이름, 성별)
	 * 4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)
	 * 5. 회원 탈퇴
	 * 
	 * ------------------------------------------------------------------
	 * 
	 * 게시판 기능 (Board View, Service, DAO, board-query.xml)
	 * 
	 * 1. 게시글 목록 조회(작성일 내림차순) --> 빡쎌듯
	 * 	  (게시글 번호, 제목[댓글 수], 작성자명, 작성일, 조회수 )
	 * 
	 * 2. 게시글 상세 조회(게시글 번호 입력 받음)
	 *    (게시글 번호, 제목, 내용, 작성자명, 작성일, 조회수, 
	 *     댓글 목록(작성일 오름차순 )
	 *     
	 *     2-1. 댓글 작성
	 *     2-2. 댓글 수정 (자신의 댓글만)
	 *     2-3. 댓글 삭제 (자신의 댓글만)
	 * 
	 *     // 자신이 작성한 글 일때만 메뉴 노출
	 *     2-4. 게시글 수정
	 *     2-5. 게시글 삭제
	 *     
	 *     
	 * 3. 게시글 작성(제목, 내용 INSERT) 
	 * 	-> 작성 성공 시 상세 조회 수행
	 * 
	 * 4. 게시글 검색(제목, 내용, 제목+내용, 작성자)
	 * 
	 * */

	// board-query.xml
	// comment-query.xml
	// member-query.xml

}


