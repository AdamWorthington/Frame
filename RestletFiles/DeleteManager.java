package com.Simple;

import java.sql.Connection;
import java.sql.DriverManager;
import org.json.JSONObject;

public class DeleteManager {
	
	public DeleteManager(JSONObject obj){
		
	}
	
	public JSONObject sqlSelect(JSONObject jo) {
		JSONObject returnVal = null;
		Connection conn = getConnection();
		returnVal = SQLStatements.sqlDelete(conn, jo);
		return returnVal;
	}
	
	private Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.err.println("Failed to get connection in DeleteManager.");
		}
		return conn;
	}
}
