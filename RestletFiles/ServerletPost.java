package com.Simple;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ServerletPost extends ServerResource implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Post("json")
	public void postText(Representation r) throws IOException { 
		
		sqlInsertInto(r);
		return;// returns 0 on success, other if fail.
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
