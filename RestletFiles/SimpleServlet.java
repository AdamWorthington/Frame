package com.Simple;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SimpleServlet extends ServerResource {

	@Get("json")
	public JSONObject represent() {
		return sqlSelect();
	}

	@Post("json")
	public int postText(Representation r) throws IOException { 
		System.err.println("Wrong hole!");
		return sqlInsertInto(r);// returns 0 on success, other if fail.
	}

	public JSONObject sqlSelect() {
		int po = 0;
		
		String query = "SELECT TOP 10 * FROM Test_Store";
		Statement stmt = null;
		String url = "";
		String[] textFields = new String[10];
		String[] dateFields = new String[10];
		int[] idFields = new int[10];
		JSONObject returnVal = new JSONObject();
		System.err.println(po++);
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			System.err.println(po++);
			url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			System.err.println(po++);
			Connection conn = DriverManager.getConnection(url);
			System.err.println(po++);
			stmt = conn.createStatement();
			System.err.println(po++);
			ResultSet rs = stmt.executeQuery(query);
			System.err.println(po++);
			
			int i = 0;
			while (rs.next() && i < 10) {
				textFields[i] = rs.getString("Text");
				dateFields[i] = rs.getString("Date");
				idFields[i] = rs.getInt("ID");
				i++;
			}
			System.err.println(po++ + "iterated this many times: " + i);
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
		System.err.println(po++);
		return returnVal;
	}

	public int sqlInsertInto(Representation r) {

		int returnVal = 0;
		// ------------Parses the text field---------------//
		String s = "";
		try {
			s = r.getText();
		} catch (IOException e2) {
			System.err.println("Couldn't get text");
			returnVal = 1;
		}
		JSONObject obj = null;
		try {
			obj = new JSONObject(s);
		} catch (JSONException e1) {
			System.err.println("Couldn't make JSONObject from string: " + s);
			returnVal = 2;
		}

		String value = "";

		try {
			value = obj.getString("Text");
		} catch (JSONException e) {
			System.err.println("Couldn't find key");
			returnVal = 3;
		}
		// -----------creates a new entry in the database-------------//
		String url = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			Connection conn = DriverManager.getConnection(url);
			String statement = "INSERT INTO Text_Store (ID, Text, Date) VALUES (null, \""
					+ value + "\", null)";
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.err.println("Something didn't work" + e.getMessage());
			e.printStackTrace();
			returnVal = 4;
		}
		return returnVal;
	}
}
