package com.Simple;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

		
		/*
		try {
			value = obj.getString("Text");
		} catch (JSONException e) {
			System.err.println("Couldn't find key");
			returnVal = 3;
		}
		*/
		// -----------creates a new entry in the database-------------//
		Connection conn = null;
		String url = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.err.println("Something didn't work" + e.getMessage());
			e.printStackTrace();
			returnVal = 4;
		}
		if(JSONMessage.isPic(obj)){
			PreparedStatement stmt = SQLStatements.sqlPOST(conn, obj);
			System.err.println(stmt.toString());
			
			//PreparedStatement stmt = null;
			
			String query = "INSERT INTO MEDIA (ID, User, Latitude, Longitude, Picture, Video, Media_Type, Date, Votes, Flags)"
					   + " VALUES (NULL, \"DAN\", 1, 1, NULL, NULL, 0, NULL, 0, 0);";
			
			String query1 = "INSERT INTO Text_Store (ID, Text, Date) VALUES (NULL, \"TESTESTESTESTEST\", NULL)";
			
			String query2 = "INSERT INTO Test (ID, Media) VALUES (NULL, ?)";
			String test = "Test";
			/*try {
				stmt = conn.prepareStatement();
				Blob blob = conn.createBlob();
				blob.setBytes(1,  test.getBytes());
				stmt.setBlob(1, blob);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			System.err.println(conn.toString());
			
			try {
				System.err.println("di____+++");
				//Statement stmt = conn.createStatement();
				int work = stmt.executeUpdate();
				//int work = stmt.executeUpdate();
				if (work == 1) {
					System.err.println("-------------------WORKED-----------------");
				}
				else {
					System.err.println("-------------------FAILED-----------------");
				}
				System.err.println("----------------------%%%%%%%%%%%%%%%%--------------");
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(JSONMessage.isVid(obj)){
			
		}
		else{
			System.err.println("not supported");
			returnVal = 69;
		}
		
		return returnVal;
	}
}
