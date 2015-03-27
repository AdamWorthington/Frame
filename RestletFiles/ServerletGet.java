package com.Simple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ServerletGet extends ServerResource {

	@Get
	public JSONObject represent() {
		return sqlSelect();
	}

	public JSONObject sqlSelect() {
		String query = "SELECT * FROM Text_Store LIMIT 10";
		Statement stmt = null;
		String url = "";
		String[] textFields = new String[10];
		String[] dateFields = new String[10];
		int[] idFields = new int[10];
		JSONObject returnVal = new JSONObject();
		
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			
			url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			
			Connection conn = DriverManager.getConnection(url);
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			
			int i = 0;
			while (rs.next() && i < 10) {
				textFields[i] = rs.getString("Text");
				dateFields[i] = rs.getString("Date");
				idFields[i] = rs.getInt("ID");
				i++;
			}
			returnVal.put("total", i);
			returnVal.put("Text", textFields);
			returnVal.put("Date", dateFields);
			returnVal.put("ID", idFields);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					
				} catch (SQLException e) {
					System.err.println("Failed to close statement");
				}
			}
		}
		
		return returnVal;
	}
}
