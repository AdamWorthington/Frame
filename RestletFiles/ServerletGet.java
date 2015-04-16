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

	@Get("json")
	public JSONObject represent() {
		
		return sqlSelect();
	}

	public JSONObject sqlSelect() {
		String query = "SELECT Media FROM Media WHERE ID=(SELECT max(ID) FROM Media)";
		
		String query2 = "SELECT ID, Flags, Votes "
				+ "FROM Media_Attributes "
			  	+ "WHERE ID=(SELECT max(ID) FROM Test)";
		
		Statement stmt = null;
		Statement stmt2 = null;
		String url = "";
		String[] textFields = new String[10];
		int[] id = new int[10];
		int[] flags = new int[10];
		int[] votes = new int[10];
		JSONObject returnVal = new JSONObject();
		
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			
			url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			
			Connection conn = DriverManager.getConnection(url);
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			
			
			int i = 0;
			while (rs.next() && i < 1) {
				textFields[i] = rs.getString("Media");
				System.err.println(textFields[i]);
				//dateFields[i] = rs.getString("Date");
				//idFields[i] = rs.getInt("ID");
				i++;
			}
			
			returnVal.put("total", i);
			returnVal.put("Picture", textFields);
			
			Connection conn2 = DriverManager.getConnection(url);
			stmt2 = conn2.createStatement();
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			i = 0;
			while(rs2.next() && i < 1) {
				id[i] = rs.getInt("ID");
				flags[i] = rs.getInt("Flags");
				votes[i] = rs.getInt("Votes");
			}
			
			
			returnVal.put("Flags", flags);
			returnVal.put("ID", id);
			returnVal.put("Votes", votes);
			
			
			
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