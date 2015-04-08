import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.sql.SQLException;
import java.io.File;
import java.sql.ResultSet;



/*
 *  USAGE: For each retrieval type (picture, video, text, comments) call one of these methods with the resultset
 *  	   recived from the database to get the corresponding JSON packet
 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
 *    -Posts		:	 1		:	TODO	:	ResultSet rs
 *    -Text			:	 2		:	TODO	:	ResultSet rs
 *    -Comment		:	 3		:	TODO	:	ResultSet rs
 *    
 *    -@param ResultSet rs: the result set returned by the database after the SQL query
 */



public class DatabaseReturnManager {

	/*
	 * @param ResultSet rs: The ResultSet containing all the info for a Picture TODO
	 * 					  : ResultSet will contain Picture/Video, ID, User, Votes, Flags
	 */
	public static JSONObject postsToJSON(ResultSet rs) {
		int i = 0;
		int type = 0;	//0 for picture, 1 for video
		String[] media = new String[10];
		String[] users = new String[10];
		int[] postID = new int[10];
		int[] votes = new int[10];
		int[] flags = new int[10];
		String[] tags = new String[10];
		String[] date = new String[10];
		try {
			while (rs.next() && i < 10) {

				Blob blob = rs.getBlob("Picture");
				if (blob != null) {
					byte[] bdata = blob.getBytes(1,  (int) blob.length());
					media[i] = new String(bdata);
				}
				else {	
					blob = rs.getBlob("Video");
					byte[] bdata = blob.getBytes(1,  (int) blob.length());
					media[i] = new String(bdata);
					type = 1;
				}
				postID[i] = rs.getInt("ID");
				users[i]  = rs.getString("User");
				votes[i]  = rs.getInt("Votes");
				flags[i]  = rs.getInt("Flags");
				tags[i] = rs.getString("Tag");
				date[i] = rs.getString("Date");

				i++;
			}
		}
		catch (SQLException e) {
			System.err.println("SQL Error in postsToJSON");
			return null;
		}
		
		//TODO: Need json methods that return posts to client to take arrays of things, not individual ones
		JSONObject returnVal;
		if (type == 0) {
			returnVal = serverPictureToJson(media, date, postID, tags, votes);
		}
		else {
			returnVal = serverVideoToJson(media, date, postID, votes, tags);
		}
		
		
		
		return returnVal;
	}

	/*
	 * @param ResultSet rs: The ResultSet containing all the info for text TODO
	 * 					  : Text, ID, User, Votes, Flags
	 */
	public static JSONObject textToJSON(ResultSet rs) {

		int i = 0;
		String[] text = new String[10];
		String[] users = new String[10];
		int[] postID = new int[10];
		int[] votes = new int[10];
		int[] flags = new int[10];
		try {
			while (rs.next() && i < 10) {

				text[i] = rs.getString("Text_String");
				postID[i] = rs.getInt("ID");
				users[i]  = rs.getString("User");
				votes[i]  = rs.getInt("Votes");
				flags[i]  = rs.getInt("Flags");

				i++;
			}
		}
		catch (SQLException e) {
			System.err.println("SQL Error in postsToJSON");
			return null;
		}
		
		//TODO: Need json methods that return posts to client to take arrays of things, not individual ones
		JSONObject returnVal = new JSONObject();
		
		
		
		return returnVal;
	}

	/*
	 * @param ResultSet rs: The ResultSet containing all the info for comments TODO
	 */
	public static JSONObject commentsToJSON(ResultSet rs) {

		int i = 0;
		String[] comments = new String[10];
		String[] users = new String[10];
		int[] postID = new int[10];
		int[] flags = new int[10];
		try {
			while (rs.next() && i < 10) {

				comments[i] = rs.getString("Comment");
				postID[i] = rs.getInt("Comment_ID");
				users[i]  = rs.getString("Comment_User");
				flags[i]  = rs.getInt("Comment_Flags");

				i++;
			}
		}
		catch (SQLException e) {
			System.err.println("SQL Error in postsToJSON");
			return null;
		}
		
		//TODO: Need json methods that return posts to client to take arrays of things, not individual ones
		JSONObject returnVal = new JSONObject();
		
		
		
		return returnVal;
	}

}
