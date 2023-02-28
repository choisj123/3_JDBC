package edu.home.jdbc.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {

	public static void main(String[] args) {

		try {
		
			Scanner sc = new Scanner(System.in);
			
			Properties prop = new Properties();
			
			System.out.print("생성할 파일 이름 : ");
			String fileName = sc.next();
			
			FileOutputStream fos = new FileOutputStream(fileName + ".xml");
			
			prop.storeToXML(fos, fileName + ".xml");
			System.out.println(fileName + ".xml 파일 생성완료");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
