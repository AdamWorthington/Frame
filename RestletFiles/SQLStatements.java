package com.Simple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;


/*
 * USAGE: Depending on integer received from front end, call one of these two methods
 * 	-sqlGET:  int methodID, int postID, int lastPost, double lat, double lon, String tag, String timestamp, int sort
 * 	-sqlPOST: int methodID, int postID, String user , double lat, double lon, String text_or_comment, String date, File picture, File video
 * 
 *  SEE BELOW FOR EACH ACTION'S REQUIRED PARAMETERS
 *  FOR PARAMETERS NOT NEEDED, PASS NULL OR ZERO
 * 
 * 
 * Get information: sqlGET (return value is String):
 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
 *  -Get Picture     :   1		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
 *  -Get Video       :   2		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
 *  -Get Text        :   3		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
 *  -Get Comments    :   4		:	sqlGET	:	int postID
 *  
 * Post information: sqlPOST (return value is String):
 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
 *  -Post Picture    :   5		:	sqlPOST	:	File picture, String user, int lat, int lon, String date
 *  -Post Video      :   6		:	sqlPOST	:	File video,   String user, int lat, int lon, String date
 *  -Post Text       :   7		:	sqlPOST	:	String text,  String user, int lat, int lon, String date
 *  -Vote			 :	 8		:	sqlPOST :	int postID, int vote
 *  -Flag            :   9		:	sqlPOST	:	int postID
 *  -UnFlag          :   10		:	sqlPOST	:	int postID
 *  -Commenting      :   11		:	sqlPOST	:	String user, int postID, String comment
 *  -Remove Media    :   12		:	sqlPOST	:	int postID, String user
 *  -Remove Comment  :   13		:	sqlPOST	:	int commentID, String user
 *  
 *  Obsolete:
 *  -Upvote          :   8		:	sqlPOST	:	int postID
 *  -Downvote        :   9		:	sqlPOST	:	int postID
 *  -UnUpvote        :   10		:	sqlPOST	:	int postID
 *  -UnDownvote      :   11		:	sqlPOST	:	int postID
 *  
 *  NOTE: Removal of pictures and videos use same method ( as we only need 
 *        the post ID to remove, the type does not matter.
 */
public class SQLStatements {

	/*
	 * GENERAL NOTES: DELETE WHEN CODE IS COMPLETE
	 * 
	 * To display on phone: picture, video, text, upvote, downvote, flags, comments
	 * upvote, downvote, flags can be returned as integers with the requested media/pictures
	 * 
	 * Make all strings passed in safe (need to find SQL or other methods to do so)
	 *      -Use of Prepared Statements (Parameterized Queries)
	 *      -Use of Stored Procedures
	 *      -Escaping all User Supplied Input
	 *      -Enforce Least Privilege
	 *      -Perform White List Input Validation
	 *      -See https://www.owasp.org/index.php/SQL_Injection_Prevention_Cheat_Sheet
	 *      
	 *      Possibilities:
	 *       -Have SimpleServlet pass in a Connection, and this code returns a prepared statement (BEST)
	 *       -Modify SimpleServlet to use prepared statements (GOOD)
	 *       -Still return a string, and have the SimpleServlet do the combining of the actual values with the SQL string (NOT SO GOOD)
	 * 
	 * 
	 *    If want to sort by new, oldest, newest, is actually easy, just do an ORDER sql call
	 *    
	 *    
	 *    
	 *    
	 *  Things that need to change in SimpleServlet
	 *   -Check for null return, indicates an error in the parameters passed
	 *   -Rip apart json packet to pass values to these methods (grant's json api)
	 *   -
	 *   -
	 *   -
	 */



	/*
	 * PARAMETERS FOR GET METHODS
	 *  -@param conn: the connection, for PreparedStatements				(Connection)
	 *  -@param jo: the json object to be dealt with						(JSONObjec)
	 *  -@param sort: the way to sort the data, 0 = most recent, 1 = most votes
	 */




	/*
	 * Master method for GET
	 */
	public static JSONObject sqlGET(Connection conn, JSONObject jo) {

		JSONObject query = null;

		
		/*
		 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
		 *  -Get Picture     :   1		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
		 *  -Get Video       :   2		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
		 *  -Get Text        :   3		:	sqlGET	:	int lastPost, double lat, double lon, String tag, String timestamp, int sort
		 *  -Get Comments    :   4		:	sqlGET	:	int postID
		 */
		
		if (JSONMessage.isPostRequest(jo)) {
			int lastPost = 0;
			double lat = JSONMessage.getLat(jo);
			double lon = JSONMessage.getLon(jo);
			int sort   = JSONMessage.getSort(jo);
			String tag = JSONMessage.getFilter(jo);
			
			if (tag == null || tag.trim().isEmpty()) {
				query = getPosts(conn, lastPost, lat, lon, sort);
			}
			else {
				query = getPostsByTag(conn, lastPost, lat, lon, tag, sort);
			}
		}
		else if(JSONMessage.isComment(jo)) {
			int id = JSONMessage.getID(jo);
			query = getComments(conn, id);
		}
		else {
			System.err.println("Error in sqlGET, unknown request");
			System.err.println(jo);
		}

		return query;
	}
	
	public static JSONObject getPostsByTag(Connection conn, int lastPost, double lat, double lon, String tag, int sort) {
		JSONObject ret = null;
		
		PreparedStatement stmt  = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		
		String query  =   "SELECT Post_ID FROM Tags "
						+ "WHERE Tag = ? AND "						//1
						+ 		"Latitude  < ? + 1.5 AND "			//2
						+ 		"Latitude  > ? - 1.5 AND "			//3
						+ 		"Longitude < ? + 1.5 AND "			//4
						+ 		"Longitude > ? - 1.5 "				//5
						+ "ORDER BY Post_ID DESC LIMIT 5;";
		
		String query2 =   "SELECT ID, User, Votes, Flags, Date "
				  		+ "FROM Media_Attributes "
				  		+ "WHERE (ID = ? OR "						//1
				  		+ 		 "ID = ? OR "						//2
				  		+ 		 "ID = ? OR "						//3
				  		+ 		 "ID = ? OR "						//4
				  		+ 		 "ID = ?) "							//5
				  		+ "ORDER BY ID DESC LIMIT 5;";
		
		String query3 = "SELECT Media "
		  		+ 		"FROM Media "
		  		+ 		"WHERE (ID = ? OR "						//1
		  		+ 			   "ID = ? OR "						//2
		  		+ 			   "ID = ? OR "						//3
		  		+ 			   "ID = ? OR "						//4
		  		+ 			   "ID = ?) "							//5
		  		+ 		"ORDER BY ID DESC LIMIT 5;";
		
		//Get the post ID's corresponding to the tags from the database
		int[] Post_IDs = {-1, -1, -1, -1, -1};
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, tag);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.setDouble(5, lon);
			
			ResultSet rs = stmt.executeQuery();
			
			int i = 0;
			while (rs.next() && i < 5) {
				Post_IDs[i] = rs.getInt("Post_ID");
				i++;
			}
			rs.close();
			
		}
		catch (SQLException e) {
			System.err.println("Error creating/executing the SELECT FROM Tags query");
			return null;
		}
		
		
		//Get the attributes of the pictures from the database
		Connection conn2 = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn2 = DriverManager.getConnection(url);
		}
		catch (Exception e) {
			System.err.println("Error generating connection 2 in getPostsByTag");
		}
		
		int[] id = new int[5];
		int[] flags = new int[5];
		int[] votes = new int[5];
		String[] users = new String[5];
		String[] dates = new String[5];
		try {
			stmt2 = conn2.prepareStatement(query2);
			for(int i = 0; i < 5; i++) {
				stmt2.setInt(i + 1, Post_IDs[i]);
			}
			
			ResultSet rs = stmt2.executeQuery();
			
			int i = 0;
			while (rs.next() && i < 5) {
				id[i]    = rs.getInt("ID");
				flags[i] = rs.getInt("Flags");
				votes[i] = rs.getInt("Votes");
				users[i] = rs.getString("User");
				dates[i] = rs.getString("Date");
				i++;
			}
			rs.close();
			
		}
		catch (SQLException e) {
			System.err.println("Error creating/executing the SELECT FROM Media_Attributes query");
			return null;
		}
		

		//Get the actual pictures from the database
		String[] media = new String[5];
		
		Connection conn3 = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn3 = DriverManager.getConnection(url);
		}
		catch (Exception e) {
			System.err.println("Error generating connection 3 in getPostsByTag");
		}
		
		try {
			stmt3 = conn3.prepareStatement(query3);
			for(int i = 0; i < 5; i++) {
				stmt3.setInt(i + 1, Post_IDs[i]);
			}
			
			ResultSet rs = stmt3.executeQuery();
			
			int i = 0;
			while (rs.next() && i < 5) {
				media[i] = rs.getString("Media");
				i++;
			}
			rs.close();
		}
		catch (SQLException e) {
			System.err.println("Error creating/executing the SELECT FROM Media query");
			return null;
		}
		
		ret = JSONMessage.serverPictureToJson(media, dates, id, null, votes);
		return ret;
	}

	//TODO: Remove tag stuffs from this method
	public static JSONObject getPosts(Connection conn, int lastPost, double lat, double lon,  int sort) {
		PreparedStatement stmt = null;

		//Check parameters for correctness
		if (lastPost < 0) {
			System.err.println("Incorrect parameters to obtain posts, lastPost < 0");
			return null;
		}

		String query = "SELECT ID, Flags, Votes, User, Date "
					 + "FROM Media_Attributes "
					 + "WHERE Latitude  < ? + 1.5 AND "			//1
					 + 		 "Latitude  > ? - 1.5 AND "			//2
					 + 		 "Longitude < ? + 1.5 AND "			//3
					 + 		 "Longitude > ? - 1.5 AND "			//4
					 + 		 "ID < ? "							//5
					 + "ORDER BY ID DESC LIMIT 5;";
			  	//+ "WHERE ID=(SELECT max(ID) FROM Test)";
			  	//+ "ID >  ? AND "					//5
				//+ "ID < (? + 10) AND "				//6


		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lon);
			stmt.setDouble(4, lon);
			stmt.setInt(5, lastPost);
			//stmt.setInt(5, lastPost);
			//stmt.setInt(6, lastPost);
		
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getPosts");
			return null;
		}


		int[] id = {-1, -1, -1, -1, -1};
		int[] flags = new int[5];
		int[] votes = new int[5];
		String[] users = new String[5];
		String[] dates = new String[5];
		String[] media = new String[5];
		
		try {
			ResultSet rs = stmt.executeQuery();
		
			int i = 0;
			while (rs.next() && i < 5) {
				id[i]    = rs.getInt("ID");
				flags[i] = rs.getInt("Flags");
				votes[i] = rs.getInt("Votes");
				users[i] = rs.getString("User");
				dates[i] = rs.getString("Date");
				i++;
			}
			rs.close();
		}
		catch(SQLException e) {
			System.err.println("ERROR IN GET POSTS RETRIEVING QUERY 1 DATA");
		}
		
		String query2 = "SELECT Media "
				+ 		"FROM Media "
				+ 		"WHERE ID IN (?, ?, ?, ?, ?) "
				+ 		"ORDER BY ID DESC LIMIT 5";
		PreparedStatement stmt2 = null;
		
		Connection conn2 = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn2 = DriverManager.getConnection(url);
		}
		catch (Exception e) {
			System.err.println("Error generating connection 2 in getPosts");
		}
		try {
			stmt2 = conn2.prepareStatement(query2);
			for(int i = 0; i < 5; i++) {
				stmt2.setInt(i + 1, id[i]);
			}
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getPosts");
			return null;
		}
		
		try {
			ResultSet rs2 = stmt2.executeQuery();
			
			int i = 0;
			while (rs2.next() && i < 5) {
				media[i] = rs2.getString("Media");
				i++;
			}
			rs2.close();
		}
		catch(SQLException e) {
			System.err.println("ERROR GETTING DATA FROM QUERY 2");
		}
		
		JSONObject returnVal;
		returnVal = JSONMessage.serverPictureToJson(media, dates, id, null, votes);
		
		try {
			stmt.close();
			stmt2.close();
		} catch (SQLException e) {
			System.err.println("ERROR CLOSING STMTS IN GET PICTURE");
		}
		
		return returnVal;
		
	}
	
	
	
	
	/*
	 * 	1
	 */
	@SuppressWarnings("unused")
	private static PreparedStatement getPicture(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
		PreparedStatement stmt = null;;

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain a Picture");
			return null;
		}

		String query = null;

		//If the tag is not null then that takes priority over all other parameters
		if (tag != null || tag == "") {
			query = "SELECT Picture, ID, User, Votes, Flags "
				  + "FROM Media_and_Tags "
				  + "WHERE Latitude  < ? + 1.5 AND "	//1
				  + "Latitude  > ? - 1.5 AND "			//2
				  + "Longitude < ? + 1.5 AND "			//3
				  + "Longitude > ? - 1.5 AND "			//4
				  + "ID >  ? AND "						//5
				  + "ID < (? + 10) AND "				//6
				  + "Date > \"?\" AND "					//7
				  + "Tag = \"?\" AND "					//8
				  + "Media_Type = 0;";
			
			try {
				stmt = conn.prepareStatement(query);
				stmt.setDouble(1, lat);
				stmt.setDouble(2, lat);
				stmt.setDouble(3, lon);
				stmt.setDouble(4, lon);
				stmt.setInt(5, lastPost);
				stmt.setInt(6, lastPost);
				stmt.setString(7, timestamp);
				stmt.setString(8, tag);
				
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement in getPicture");
				return null;
			}

			return stmt;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "SELECT Picture, ID, User, Votes, Flags "
				  + "FROM Media_and_Tags "
				  + "WHERE Latitude  < ? + 1.5 AND "	//1
				  + "Latitude  > ? - 1.5 AND "			//2
				  + "Longitude < ? + 1.5 AND "			//3
				  + "Longitude > ? - 1.5 AND "			//4
				  + "ID >  ? AND "						//5
				  + "ID < (? + 10) AND "			//6
				  + "Date > \"?\" AND "					//7
				  + "Media_Type = 0;";


		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lon);
			stmt.setDouble(4, lon);
			stmt.setInt(5, lastPost);
			stmt.setInt(6, lastPost);
			stmt.setString(7, timestamp);
			
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getPicture");
			return null;
		}

		return stmt;

	}

	/*
	 * 	2
	 */
	@SuppressWarnings("unused")
	private static PreparedStatement getVideo(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
		PreparedStatement stmt = null;

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain a Video");
			return null;
		}

		String query = null;


		//If the tag is not null then that takes priority over all other parameters
		if (tag != null || tag == "") {
			query = "SELECT Video, ID, User, Votes, Flags "
					+ "FROM Media_and_Tags "
					+ "WHERE Latitude  < ? + 1.5 AND "		//1
					+ "Latitude  > ? - 1.5 AND "			//2
					+ "Longitude < ? + 1.5 AND "			//3
					+ "Longitude > ? - 1.5 AND "			//4
					+ "ID >  ? AND "						//5
					+ "ID < (? + 10 + 1) AND "				//6
					+ "Date > \"?\" AND "					//7
					+ "Tag = \"?\" AND "					//8
				    + "Media_Type = 1;";
				
			try {
				stmt = conn.prepareStatement(query);
				stmt.setDouble(1, lat);
				stmt.setDouble(2, lat);
				stmt.setDouble(3, lon);
				stmt.setDouble(4, lon);
				stmt.setInt(5, lastPost);
				stmt.setInt(6, lastPost);
				stmt.setString(7, timestamp);
				stmt.setString(8, tag);
				
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement in getVideo");
				return null;
			}

			return stmt;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "SELECT Video, ID, User, Votes, Flags "
				+ "FROM Media "
				+ "WHERE Latitude  < ? + 1.5 AND "	//1
				+ "Latitude  > ? - 1.5 AND "		//2
				+ "Longitude < ? + 1.5 AND "		//3
				+ "Longitude > ? - 1.5 AND "		//4
				+ "ID >  ? AND "					//5
				+ "ID < (? + 10 + 1) AND "			//6
				+ "Date > \"?\" AND "				//7
				+ "Media_Type = 1;";


		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lon);
			stmt.setDouble(4, lon);
			stmt.setInt(5, lastPost);
			stmt.setInt(6, lastPost);
			stmt.setString(7, timestamp);
			
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getVideo");
			return null;
		}

		return stmt;
	}

	/*
	 * 	3
	 */
	@SuppressWarnings("unused")
	private static PreparedStatement getText(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
		PreparedStatement stmt = null;

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain Text");
			return null;
		}

		String query = null;


		//If the tag is not null then that takes priority over all other parameters
		if (tag != null) {
			query = "SELECT TOP 10 Text_String, ID, User, Votes, Flags "
					+ "FROM Text_and_Tags "
					+ "WHERE Latitude  < ? + 1.5 AND "		//1
					+ "Latitude  > ? - 1.5 AND "			//2
					+ "Longitude < ? + 1.5 AND "			//3
					+ "Longitude > ? - 1.5 AND "			//4
					//+ "ID >  ? AND "						//5
					//+ "ID < (? + 10 + 1) AND "				//6
					//+ "Date > \"?\" AND "					//7
					+ "Tag = \"?\" AND "					//8
					+ "Media_Type = 2;";

			try {
				stmt = conn.prepareStatement(query);
				stmt.setDouble(1, lat);
				stmt.setDouble(2, lat);
				stmt.setDouble(3, lon);
				stmt.setDouble(4, lon);
				stmt.setInt(5, lastPost);
				stmt.setInt(6, lastPost);
				stmt.setString(7, timestamp);
				stmt.setString(8, tag);

			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement in getText");
				return null;
			}

			return stmt;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "SELECT TOP 10 Text_String, ID, User, Votes, Flags "
				+ "FROM Text "
				+ "WHERE Latitude  < ? + 1.5 AND "	//1
				+ "Latitude  > ? - 1.5 AND "		//2
				+ "Longitude < ? + 1.5 AND "		//3
				+ "Longitude > ? - 1.5 AND "		//4
				//+ "ID >  ? AND "					//5
				//+ "ID < (? + 10 + 1) AND "			//6
				//+ "Date > \"?\" AND "				//7
				+ "Media_Type = 2;";


		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lon);
			stmt.setDouble(4, lon);
			stmt.setInt(5, lastPost);
			stmt.setInt(6, lastPost);
			stmt.setString(7, timestamp);

		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getText");
			return null;
		}

		return stmt;
	}

	/*
	 * 	4
	 */
	private static JSONObject getComments(Connection conn, int postID) {
		
		PreparedStatement stmt = null;
		
		if (postID < 0) {
			System.err.println("Incorrect postID to obtain Comments : postID needs to be greater than zero");
			return null;
		}
		
		String query = "SELECT Comment, Comment_ID "
					 + "FROM Comments "
					 + "WHERE Post_ID = ?;";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in getComments");
			return null;
		}
		

		String[] comments = new String[10];
		int[] id = new int[10];
		int numComments = 0;
		try {
			ResultSet rs = stmt.executeQuery();
		
		
			int i = 0;
			while (rs.next() && i < 10) {
				String commentString = rs.getString("Comment");
				if (commentString != null) numComments++;
				comments[i] = commentString;
				id[i++] = rs.getInt("Comment_ID");
			}

			stmt.close();
		}
		catch(SQLException e) {
			System.err.println("ERROR GETTING COMMENTS");
		}

		JSONObject returnVal = JSONMessage.serverComments(comments, postID, numComments);
		
		return returnVal;
	}








	/*
	 * PARAMETERS FOR POST METHODS:
	 *  -@param methodID: the ID of the action						(int)
	 *  -@param postID: the ID of the post being added/modified		(int)
	 *  -@param vote: the number of votes (for upvote/downvote)		(int)
	 *  -@param user: the user who is doing the adding/modifying	(String)
	 *  -@param lat: the latitude of the post						(double)
	 *  -@param lon: the longitude of the post						(double)
	 *  -@param text_or_comment: text to be posted in the database	(String)
	 *  -@param date: the timestamp of add/modification				(String)
	 *  -@param video: the file that represents the video			(File)
	 *  -@param picture: the file that represents the picture		(File)
	 *
	 * NUMBERING FOR POST METHODS:
	 *  Post Picture    :   5
	 *  Post Video      :   6
	 *  Post Text       :   7
	 *  Vote			:	8
	 *  Flag            :   9
	 *  UnFlag          :   10
	 *  Commenting      :   11
	 *  Remove Post     :   12
	 *  Remove Comment  :   13
	 *  
	 * Obsolete:
	 *  Upvote          :   8
	 *  Downvote        :   9
	 *  UnUpvote        :   10
	 *  UnDownvote      :   11
	 */


	
	public static int sqlPOST(Connection conn, JSONObject jo) {
			//int methodID, int postID, int vote, String user, double lat, double lon, String text_or_comment, String date, File picture, File video) {
		
		int query = 0;
		
		/*
			Post information: sqlPOST (return value is String):
		 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
		 *  -Post Picture    :   5		:	sqlPOST	:	File picture, String user, double lat, double lon, String date
		 *  -Post Video      :   6		:	sqlPOST	:	File video,   String user, double lat, double lon, String date
		 *  -Post Text       :   7		:	sqlPOST	:	String text,  String user, double lat, double lon, String date
		 *  -Vote			 :	 8		:	sqlPOST :	int postID, int vote
		 *  -Flag            :   9		:	sqlPOST	:	int postID
		 *  -UnFlag          :   10		:	sqlPOST	:	int postID
		 *  -Commenting      :   11		:	sqlPOST	:	String user, int postID, String comment
		 *  -Remove Media    :   12		:	sqlPOST	:	int postID, String user
		 *  -Remove Comment  :   13		:	sqlPOST	:	int commentID, String user
		 */
		if (JSONMessage.isPic(jo)) {
			String picture = (String) JSONMessage.getImage(jo);
			String user    = JSONMessage.getUser(jo);
			double lat     = JSONMessage.getLat (jo);
			double lon     = JSONMessage.getLon (jo);
			String tags  = JSONMessage.getTags(jo);
			
			query = postPicture(conn, picture, user, lat, lon, tags);
		}
		else if (JSONMessage.isVid(jo)) {
			String video  = (String) JSONMessage.getVideo(jo);
			String user   = JSONMessage.getUser(jo);
			double lat    = JSONMessage.getLat (jo);
			double lon    = JSONMessage.getLon (jo);
			
			query = postVideo(conn, video, user, lat, lon);
		}
		/*else if (JSONMessage.isText(jo)) {
			String text = JSONMessage.getText(jo);
			String user = JSONMessage.getUser(jo);
			double lat  = JSONMessage.getLat (jo);
			double lon  = JSONMessage.getLon (jo);
			String date = JSONMessage.getDate(jo);
			
			query = postText(conn, text, user, lat, lon, date);
		}*/
		else if (JSONMessage.isVote(jo)) {
			int postID = JSONMessage.getID  (jo);
			int vote   = JSONMessage.getVote(jo);
			query = setVote(conn, postID, vote);
		}
		else if (JSONMessage.isFlag(jo)) {
			int postID = JSONMessage.getID(jo);
			
			query = flag(conn, postID);
		}
		else if (JSONMessage.isUnFlag(jo)) {
			int postID = JSONMessage.getID(jo);
			
			query = unFlag(conn, postID);
		}
		else if (JSONMessage.isComment(jo)) {
			int postID 			   = JSONMessage.getID	   (jo);
			String text_or_comment = JSONMessage.getComment(jo);
			String user 		   = JSONMessage.getUser   (jo);
			
			query = postComment(conn, postID, text_or_comment, user);
		}
		/*else if (JSONMessage) {			//For removal of media	TODO
			
		}
		else if (JSONMessage) {				//For removal of comments	TODO
			
		}*/
		else{
			System.err.println("Error in sqlPOST, unrecognized JSON packet");
			System.err.println(jo);
		}
		
		return query;
		
	}

	/*
	 * 	5
	 */
	@SuppressWarnings("unused")
	private static int postPicture(Connection conn, String picture, String user, double lat, double lon, String tagList) {
		
		int work;
		PreparedStatement stmt  = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;

		String query = "INSERT INTO Media_Attributes (ID, Latitude, Longitude, Flags, Votes, User, Date)"
							   + " VALUES (?, ?, ?, 0, 0, ?, NULL);";
		
		String query2 = "INSERT INTO Media (ID, Media, Type) VALUES (NULL, ?, 0)";
		
		String query3 = "SELECT ID FROM Media WHERE ID=(SELECT max(ID) FROM Media)";
		
		//System.err.println("1_______");
		
		//Posting the picture itself
		try{
			stmt2 = conn.prepareStatement(query2);
			stmt2.setString(1, picture);
			
			work = stmt2.executeUpdate();
		}
		catch(SQLException e){
			System.err.println("Error creating posting picture to table Media");
			System.err.println(e.getMessage());
			
			return 0;
		}
		
		Connection conn2 = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn2 = DriverManager.getConnection(url);
		}
		catch (Exception e) {
			System.err.println("Error generating connection 2 in postPicture");
		}
		
		//Retrieving that Picture's ID
		int id = 0;
		try {
			stmt3 = conn2.prepareStatement(query3);
			ResultSet rs = stmt3.executeQuery();

			int i = 0;
			while (rs.next() && i < 1) {
				id = rs.getInt("ID");
			}
		}
		catch(SQLException e) {
			System.err.println("Error creating retriveing assigned ID");
			System.err.println(e.getMessage());
			
			return 0;
		}
		
		
		Connection conn3 = null;
		try {
			Class.forName("com.mysql.jdbc.GoogleDriver");
			String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
			conn3 = DriverManager.getConnection(url);
		}
		catch (Exception e) {
			System.err.println("Error generating connection 3 in postPicture");
		}
		
		//Storing the picture's Attributes
		try {
			stmt = conn3.prepareStatement(query);
			stmt.setInt(1, id);
			stmt.setDouble(2,  lat);
			stmt.setDouble(3, lon);
			stmt.setString(4, user);
			
			work = stmt.executeUpdate();
		}
		catch (SQLException e) {
			
			System.err.println("Error creating posting picture to table Media_Attributes");
			System.err.println(e.getMessage());
			
			return 0;
		}
		
		String query4 = "INSERT INTO Tags (Post_ID, Tag, Tag_ID, Latitude, Longitude) VALUES (?, ?, null, ?, ?);";
		
		
		if (tagList != null) {
			String[] tags = null;
			if (tagList.contains(", ")) {
				tags = tagList.split(", ");
			}
			else {
				tags = new String[1];
				tags[0] = tagList;
			}
			
			for (int i = 0; i < tags.length; i++) {
				Connection conn4 = null;
				try {
					Class.forName("com.mysql.jdbc.GoogleDriver");
					String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
					conn4 = DriverManager.getConnection(url);
				}
				catch (Exception e) {
					System.err.printf("Error generating connection %d in postPicture", 4 + i);
				}
			
				try {
					stmt4 = conn4.prepareStatement(query4);
					stmt4.setInt(1, id);
					stmt4.setString(2, tags[i]);
					stmt4.setDouble(3, lat);
					stmt4.setDouble(4, lon);
					
					work = stmt4.executeUpdate();
				}
				catch (SQLException e) {
					
					System.err.println("Error posting tags in postPicture");
					System.err.println(e.getMessage());
					
					return 0;
				}
			}
		}
			
		return 1;

	}

	/*
	 * 	6
	 */
	@SuppressWarnings("unused")
	private static int postVideo(Connection conn, String video, String user, double lat, double lon) {
		
		int work;
		PreparedStatement stmt  = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;

		String query = "INSERT INTO Media_Attributes (ID, Latitude, Longitude, Flags, Votes, User, Date)"
							   + " VALUES (?, ?, ?, 0, 0, ?, NULL);";
		
		String query2 = "INSERT INTO Media (ID, Media, Type) VALUES (NULL, ?, 1)";
		
		String query3 = "SELECT ID FROM Media WHERE ID=(SELECT max(ID) FROM Media)";
		//System.err.println("1_______");
		
		//Posting the picture itself
		try{
			stmt2 = conn.prepareStatement(query2);
			stmt2.setString(1, video);
			
			work = stmt2.executeUpdate();
		}
		catch(SQLException e){
			System.err.println("Error creating posting video to table Media");
			System.err.println(e.getMessage());
			
			return 0;
		}
		
		//Retrieving that Picture's ID
		int id = 0;
		try {
			stmt3 = conn.prepareStatement(query3);
			ResultSet rs = stmt3.executeQuery();

			int i = 0;
			while (rs.next() && i < 1) {
				id = rs.getInt("ID");
			}
		}
		catch(SQLException e) {
			System.err.println("Error creating retriveing assigned ID");
			System.err.println(e.getMessage());
			
			
			return 0;
		}
		
		//Storing the picture's Attributes
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			stmt.setDouble(2,  lat);
			stmt.setDouble(3, lon);
			stmt.setString(4, user);
			
			work = stmt.executeUpdate();
		}
		catch (SQLException e) {
			
			System.err.println("Error creating posting video to table Media_Attributes");
			System.err.println(e.getMessage());
			
			return 0;
		}

		return 1;

	}

	/*
	 * 	7
	 */
	@SuppressWarnings("unused")
	private static PreparedStatement postText(Connection conn, String text, String user, double lat, double lon, String date) {
		
		PreparedStatement stmt = null;

		if (user == null || user == "" || text == null || text == "" || date == null || date == "") {
			System.err.println("Parameter error in postText");
			return null;
		}
		
		String query = "INSERT INTO MEDIA (ID, User, Latitude, Longitude, Picture, Video, Text, Media_Type, Date, Votes, Flags)"
	   			   + " 			   VALUES (null, \"?\", ?, ?, NULL, NULL, \"?\", 2, \"?\", 0, 0);";
		
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lat);
			stmt.setString(4, text);
			stmt.setString(5, date);
		} catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in postText");
			return null;
		}
		

		return stmt;

	}
	
	/*
	 * 8
	 */
	private static int setVote(Connection conn, int postID, int vote) {
		
		PreparedStatement stmt = null;
		int ret;
		
		String query = null;
		if (vote == 1) {
			query = "UPDATE Media_Attributes "
				  + "SET Votes = Votes + 1 "
				  + "WHERE ID = ?;";
		}
		else if (vote == -1) {
			query = "UPDATE Media_Attributes "
			 	  + "SET Votes = Votes - 1 "
				  + "WHERE ID = ?;";
		}
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in setVote");
			return 0;
		}

		return ret;
		
	}

	/*
	 * 	9
	 */
	private static int flag(Connection conn, int postID) {

		PreparedStatement stmt = null;
		int ret;
		
		String query = "UPDATE Media_Attributes "
					 + "SET Flags = Flags + 1 "
					 + "WHERE ID = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in flag");
			return 0;
		}

		return ret;

	}

	/*
	 * 	10
	 */
	private static int unFlag(Connection conn, int postID) {

		PreparedStatement stmt = null;
		int ret;
		
		String query = "UPDATE Media_Attributes "
					 + "SET Flags = Flags - 1 "
					 + "WHERE ID = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in unFlag");
			return 0;
		}

		return ret;

	}

	/*
	 * 	11
	 */
	private static int postComment(Connection conn, int postID, String comment, String user) {
		
		if (user == null || user == "" || comment == null || comment == "" || postID < 0) {
			System.err.println("Parameter error in postComment");
			return 0;
		}

		PreparedStatement stmt = null;
		boolean ret;

		String query = "INSERT INTO Comments (Post_ID, Comment, Comment_Date, Comment_Flags, Comment_ID, Comment_User) "
									+ "VALUES (?, ?, null, 0, null, ?);";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			stmt.setString(2, comment);
			stmt.setString(3, user);
			
			ret = stmt.execute();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in postComment");
			System.err.printf("PreparedStatement: %s\n", stmt.toString());
			return 0;
		}
		
		if (ret) {
			return 1;
		}
		return 0;

	}

	/*
	 * 	12
	 */
	@SuppressWarnings("unused")
	private static int removeMedia(Connection conn, int postID, String user) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		int ret;

		String query1 =   "DELETE FROM Media_Attributes "
						+ "WHERE ID = ? AND "
						+ "User = ?;";
		
		String query2 =   "DELETE FROM Comments "
						+ "WHERE Post_ID = ?;";
		
		String query3 =   "DELETE FROM Tags "
						+ "WHERE Post_ID = ?;";
		
		String query4 = "DELETE FROM Media "
					  + "WHERE ID = ?;";

		//Query1
		try {
			stmt = conn.prepareStatement(query1);
			
			stmt.setInt(1, postID);
			stmt.setString(2, user);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeMedia");
			return 0;
		}
		
		//Query2
		try {
			stmt2 = conn.prepareStatement(query2);
			
			stmt.setInt(1, postID);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeMedia");
			return 0;
		}
		
		//Query3
		try {
			stmt2 = conn.prepareStatement(query3);
			
			stmt.setInt(1, postID);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeMedia");
			return 0;
		}
		
		//Query4
		try {
			stmt2 = conn.prepareStatement(query4);
					
			stmt.setInt(1, postID);
					
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeMedia");
			return 0;
		}
		

		return ret;

	}

	/*
	 * 13
	 */
	@SuppressWarnings("unused")
	private static int removeComment(Connection conn, int commentID, String user) {
		
		PreparedStatement stmt = null;
		int ret;

		String query = "DELETE FROM Comments "
					 + "WHERE Comment_ID = ? AND User = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, commentID);
			stmt.setString(2, user);
			
			ret = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeComment");
			return 0;
		}
		

		return ret;

	}
	
	/*
	 * 14
	 */
	public static JSONObject sqlDelete(Connection conn, JSONObject jo) {
		
		PreparedStatement stmt  = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		@SuppressWarnings("unused")
		int ret;
		
		String query  = null;
		String query2 = null;
		String query3 = null;
		String query4 = null;
		if (JSONMessage.isBan(jo)) {
			banUser(jo, conn);
		}
		else if (JSONMessage.isRemove(jo)) {
			
			int postID  = JSONMessage.getID  (jo);
			
			query  = "DELETE FROM Media WHERE ID = ?;";
			query2 = "DELETE FROM Media_Attributes WHERE ID = ?;";
			query3 = "DELETE FROM Tags WHERE Post_ID = ?;";
			query4 = "DELETE FROM Comments WHERE Post_ID = ?;";

			try {
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, postID);
				
				ret = stmt.executeUpdate();
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement in setVote");
				return null;
			}
			
			Connection conn2 = null;
			try {
				Class.forName("com.mysql.jdbc.GoogleDriver");
				String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
				conn2 = DriverManager.getConnection(url);
			}
			catch (Exception e) {
				System.err.println("Error generating connection 2 in sqlDelete");
				return null;
			}
			
			try {
				stmt2 = conn2.prepareStatement(query2);
				stmt2.setInt(1, postID);
				
				ret = stmt2.executeUpdate();
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement 2 in sqlDelete");
				return null;
			}
			
			Connection conn3 = null;
			try {
				Class.forName("com.mysql.jdbc.GoogleDriver");
				String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
				conn3 = DriverManager.getConnection(url);
			}
			catch (Exception e) {
				System.err.println("Error generating connection 3 in sqlDelete");
				return null;
			}
			
			try {
				stmt3 = conn3.prepareStatement(query3);
				stmt3.setInt(1, postID);
				
				ret = stmt3.executeUpdate();
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement 3 in sqlDelete");
				return null;
			}
			
			Connection conn4 = null;
			try {
				Class.forName("com.mysql.jdbc.GoogleDriver");
				String url = "jdbc:google:mysql://august-clover-86805:frame/Frame?user=root";
				conn4 = DriverManager.getConnection(url);
			}
			catch (Exception e) {
				System.err.println("Error generating connection 2 in postPicture");
				return null;
			}
			
			try {
				stmt4 = conn4.prepareStatement(query4);
				stmt4.setInt(1, postID);
				
				ret = stmt4.executeUpdate();
			}
			catch (SQLException e) {
				System.err.println("Error creating PreparedStatement in setVote");
				return null;
			}
		}
		else {
			System.err.println("Incorrect call into sqlDelete, not a ban or remove");
			return null;
		}

		return JSONMessage.isSuccess();
		
	}
	
	private static JSONObject banUser(JSONObject jo, Connection conn) {
		
		PreparedStatement stmt = null;
		String query = "INSERT INTO Banned_Users (User_Name, Banned_Date) VALUES (?, null);";
		
		String user = JSONMessage.getUser(jo);
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in setVote");
			return null;
		}
		
		return JSONMessage.isSuccess();
	}

}