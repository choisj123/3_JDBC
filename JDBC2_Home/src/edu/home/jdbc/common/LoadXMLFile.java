package edu.home.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadXMLFile {

	public static void main(String[] args) {
		
		try {
			Properties prop = new Properties();	
		
			FileInputStream fis = new FileInputStream("driver.xml");
			
			prop.loadFromXML(fis);
			
			System.out.println(prop);
			
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			
			System.out.println();

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);			
			
			System.out.println(conn);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
