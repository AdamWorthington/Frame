package com.Simple;

import java.sql.Connection;
import java.sql.DriverManager;
import org.json.JSONObject;

public class PostManager {

	public PostManager(JSONObject obj){
		
	}

	public JSONObject sqlInsertInto(JSONObject obj) {

		Connection conn = getConnection();
		SQLStatements.sqlPOST(conn, obj);
		return JSONMessage.isSuccess();
	}
	
	private Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.err.println("Failed to get connection in PostManager.");
		}
		return conn;
	}
}
