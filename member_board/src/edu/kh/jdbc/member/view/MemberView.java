package edu.kh.jdbc.member.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static edu.kh.jdbc.main.view.MainView.*;

import edu.kh.jdbc.main.view.MainView;
import edu.kh.jdbc.member.model.service.MemberService;
import edu.kh.jdbc.member.model.vo.Member;
//import edu.kh.jdbc.main.view.MainView;


public class MemberView {
	
	private Scanner sc = new Scanner(System.in);
	
	// 로그인 회원 정보 저장용 변수
	private Member loginMember = null;

	// 회원 관련 서비스를 제공하는 객체 생성
	private MemberService service = new MemberService();
	
	//private MainView mainView = new MainView();
	
	// 메뉴 번호를 입력 받기 위한 변수
	int input = -1;
	
	
	/** 회원 기능 메뉴
	 * @param loginMember
	 */
	public void memberMenu(Member loginMember) {

		// 전달 받은 로그인 회원 정보를 필드에 저장
		this.loginMember = loginMember; // 로그인한 member 넣어주기

		
		/* 회원기능 (Member View, Service, DAO, member-query.xml)
		 * 
		 * 1. 내 정보 조회
		 * 2. 회원 목록 조회(아이디, 이름, 성별)
		 * 3. 내 정보 수정(이름, 성별)
		 * 4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)
		 * 5. 회원 탈퇴
		 * */
		
		do {
			try {
				System.out.println("<< 회원 기능 >>");
				System.out.println("1. 내 정보 조회");
				System.out.println("2. 회원 목록 조회(아이디, 이름, 성별)");
				System.out.println("3. 내 정보 수정(이름, 성별)");
				System.out.println("4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)");
				System.out.println("5. 회원 탈퇴");
				
				System.out.println("0. 메인메뉴로 이동");
				System.out.print("\n메뉴 선택 >> ");
				input = sc.nextInt();
				
				switch(input) {
				case 1: selectMyInfo(); break;
				case 2: selectAll(); break;
				case 3: updateMember(); break;
				case 4: updatePw(); break;
				case 5: secession(); break;
				case 0: System.out.println("<<메인메뉴로 이동>>"); return;
				default: System.out.println("메뉴에 작성된 번호만 입력해주세요.");
			
			}
			}catch(InputMismatchException e) {
				System.out.println("<<입력 형식이 올바르지 않습니다>>\n");
				sc.nextLine(); 
			}
			
			
		}while(input != 0);
		
	}
	
	
	/** 1. 내 정보 조회 화면
	 * @param LoginMember
	 */
	private void selectMyInfo() {
		System.out.println("\n[내 정보 조회]");
		
		try {
			
			System.out.println("----------------------------------------------------------------");
			System.out.println(" 회원번호| 아이디 |  이름  | 성별 |         가입일              ");
			System.out.println("----------------------------------------------------------------");
			System.out.printf(" %4d    | %5s | %2s | %2s  | %5s\n", 
					loginMember.getMemberNo(), 
					loginMember.getMemberId(), 
					loginMember.getMemberName(), 
					getGender(), 
					loginMember.getEnrollDate());
			System.out.println();
			
			
		}catch(Exception e) {
			System.out.println("\n<<내 정보 조회 중 예외 발생>>\n");
			e.printStackTrace();
		}
		

		
	}
	
	/** 성별 변환 출력
	 * @return String
	 */
	private String getGender(){
		
		if(loginMember.getMemberGender().equals("M")){
			return "남";
		}else {
			return "여";
		}
		
	}

	/**
	 * 2. 회원 목록 조회(아이디, 이름, 성별) 화면
	 */
	private void selectAll() {
		System.out.println("\n[회원 목록 조회]");
		
		// DB에서 회원 목록 조회(탈퇴 회원 미포함)
		// + 가입일 내림차순
		
		try {
			
			// 회원 목록 조회 서비스 호출 후 결과 반환 받기
			List<Member> memberList = service.selectAll();
			
			
			if(memberList.isEmpty()) {
				System.out.println("\n[등록된 회원이 없습니다]\n");
			}else {
				System.out.println("--------------------------");
				System.out.println(" 아이디 |  이름  | 성별 ");
				System.out.println("--------------------------");
				for(Member mem : memberList) {
					System.out.printf(" %5s | %2s |  %2s\n" , 
							mem.getMemberId(),
							mem.getMemberName(),
							mem.getMemberGender());
				}
				System.out.println();
			}
			
		}catch(Exception e) {
			System.out.println("\n<<회원 목록 조회 중 예외 발생>>\n");
			e.printStackTrace();
		}
	}
	
	/** 3. 내 정보 수정 화면
	 * @param LoginMember
	 * @return result
	 */
	private void updateMember() {
		System.out.println("\n[내 정보 수정]");
		
		
		try {
			System.out.print("수정할 이름 : ");
			String memberName = sc.next();
			
			String memberGender = null;
			
			while(true) {
				System.out.print("수정할 성별 (M/F): ");
				memberGender = sc.next().toUpperCase();
				
				if(memberGender.equals("M") || memberGender.equals("F")) {
					break;
				}else {
					System.out.println("[M 또는 F만 입력해주세요]\n");
				}
			}
			
			// 서비스로 전달할 Member 객체 생성
			Member member = new Member();
			member.setMemberNo(loginMember.getMemberNo());
			member.setMemberName(memberName); 
			member.setMemberGender(memberGender);
			
			
			int result = service.updateMember(member);
			
			if(result > 0) {
				loginMember.setMemberName(memberName);
				loginMember.setMemberGender(memberGender);
				
				System.out.println("\n수정이 완료되었습니다.\n");
			} else {
				System.out.println("\n[수정 실패...]\n");
			}
			
			
		}catch(Exception e) {
			System.out.println("\n<<내 정보 수정 중 예외 발생>>\n");
			e.printStackTrace();
		}
		
	}
	
	/** 4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인) 화면
	 * @param LoginMember
	 * @return result
	 */
	private int updatePw() {
		System.out.println("\n[비밀번호 변경]");
		
		int result = 0;
		
		String newPw = null;
		String newPw2 = null;
		
		try {
			while(true) {
				System.out.print("현재 비밀번호 : ");
				String nowPw = sc.next();
				
				// 현재 비밀번호 DB에서 가져오기
				String memberPw = service.getPw(loginMember);
				
				if(nowPw.equals(memberPw)) {
					break;
				}else{
					System.out.println("[비밀번호가 일치하지 않습니다]\n");
				}
				
			}
			while(true) {
				System.out.print("새 비밀번호 : ");
				newPw = sc.next();
				
				System.out.print("새 비밀번호 확인 : ");
				newPw2 = sc.next();
				
				if(newPw.equals(newPw2)) {
					break;
				}else {
					System.out.println("[새 비밀번호가 일치하지 않습니다]\n");
					
				}
			}
			
			result = service.updatePw(loginMember.getMemberNo(), newPw);
			
			if(result > 0) {
				System.out.println("\n비밀번호가 변경되었습니다.\n");
			}else{
				System.out.println("\n[비밀번호 변경을 실패했습니다]\n");
			}
			
			
		}catch(Exception e) {
			System.out.println("\n<<비밀번호 변경 중 예외 발생>>\n");
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	
	

	/** 5. 회원 탈퇴 화면
	 * @param LoginMember
	 * @return result
	 */
	private void secession() {
		System.out.println("\n[회원 탈퇴]");
		
		int result = 0;
		
		try {
			
			while(true) {
				System.out.print("현재 비밀번호 : ");
				String nowPw = sc.next();
				
				// 현재 비밀번호 DB에서 가져오기
				String memberPw = service.getPw(loginMember);
				
				if(nowPw.equals(memberPw)) {
					break;
				}else{
					System.out.println("[비밀번호가 일치하지 않습니다]\n");
				}
			}
			
			while(true) {
				
				System.out.print("정말로 탈퇴하시겠습니까? (Y/N) >> ");
				String inputYN = sc.next().toUpperCase();
				
				if(inputYN.equals("Y")) {
					result = service.secession(loginMember.getMemberNo());
					
					if(result > 0) {
						System.out.println("\n탈퇴 완료...\n");
						
						//메인메뉴로 이동
						input = 0; //안돌아가짐
						
						//로그아웃
						MainView.loginMember = null;
						
						break; //회원메뉴로 돌아가짐
						
					}else {
						System.out.println("\n[탈퇴 실패]\n");
					}
					
				}else if(inputYN.equals("N")){
					System.out.println("\n[취소되었습니다]\n");
					break;
				} else {
					System.out.println("\n[Y 또는 N만 입력해주세요]\n");
				}
				
			}
			
		}catch(Exception e) {
			System.out.println("\n<<회원 탈퇴 중 예외 발생>>\n");
			e.printStackTrace();
		}
		
	}

}
