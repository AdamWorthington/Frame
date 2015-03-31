import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	 * TODO: Make all strings passed in safe (need to find SQL or other methods to do so)
	 *      -Use of Prepared Statements (Parameterized Queries)
	 *      -Use of Stored Procedures
	 *      -Escaping all User Supplied Input
	 *      -Enforce Least Privilege
	 *      -Perform White List Input Validation
	 *      -See https://www.owasp.org/index.php/SQL_Injection_Prevention_Cheat_Sheet
	 *      
	 *      Possibilities:
	 *       -Have SimpleServlet pass in a Connection, and this code returns a prepared statement (BEST) TODO
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
	 *  -@param lastPost: Last post ID on user page (so can get next ten) 	(int)
	 *  -@param lat: latitude of posts to get 								(double)
	 *  -@param lon: longitude of the posts to get 							(double)
	 *  -@param tag: get all posts with this tag 							(String)
	 *  -@param timestamp: String value of the date in YYYY-MM-DD HH:MM:SS 	(String, HH is 00-23)
	 *  -@param sort: 0 for descending, 1 for ascending 				   	(int)
	 *  -@param postID: the ID of the post we are getting information from 	(int)
	 */




	/*
	 * Master method for GET
	 */
	public PreparedStatement sqlGET(Connection conn, int methodID, int postID, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {

		PreparedStatement query = null;

		switch(methodID) {

		case 1:
			query = getPicture(conn, lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 2:
			query = getVideo(conn, lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 3:
			query = getText(conn, lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 4:
			query = getComments(conn, postID);
			break;
			
		default:
			System.err.printf("Incorrect methodID in sqlGET (Should be 1-4, was %d)\n", methodID);
			break;

		}

		return query;
	}


	/*
	 * 	1
	 */
	private PreparedStatement getPicture(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
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
				  + "ID < (? + 10 + 1) AND "			//6
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
				  + "ID < (? + 10 + 1) AND "			//6
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
	private PreparedStatement getVideo(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
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
	private PreparedStatement getText(Connection conn, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {
		
		PreparedStatement stmt = null;

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain Text");
			return null;
		}

		String query = null;


		//If the tag is not null then that takes priority over all other parameters
		if (tag != null) {
			query = "SELECT Text_String, ID, User, Votes, Flags "
					+ "FROM Text_and_Tags "
					+ "WHERE Latitude  < ? + 1.5 AND "		//1
					+ "Latitude  > ? - 1.5 AND "			//2
					+ "Longitude < ? + 1.5 AND "			//3
					+ "Longitude > ? - 1.5 AND "			//4
					+ "ID >  ? AND "						//5
					+ "ID < (? + 10 + 1) AND "				//6
					+ "Date > \"?\" AND "					//7
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
		query = "SELECT Text_String, ID, User, Votes, Flags "
				+ "FROM Text "
				+ "WHERE Latitude  < ? + 1.5 AND "	//1
				+ "Latitude  > ? - 1.5 AND "		//2
				+ "Longitude < ? + 1.5 AND "		//3
				+ "Longitude > ? - 1.5 AND "		//4
				+ "ID >  ? AND "					//5
				+ "ID < (? + 10 + 1) AND "			//6
				+ "Date > \"?\" AND "				//7
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
	private PreparedStatement getComments(Connection conn, int postID) {
		
		PreparedStatement stmt = null;
		
		if (postID < 0) {
			System.err.println("Incorrect postID to obtain Comments : postID needs to be greater than zero");
			return null;
		}
		
		String query = "SELECT Comment, Comment_Flags, Comment_User, Comment_ID "
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

		return stmt;
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


	
	public PreparedStatement sqlPOST(Connection conn, int methodID, int postID, int vote, String user, double lat, double lon, 
						  			 String text_or_comment, String date, File picture, File video) {
		
		PreparedStatement query = null;
		
		switch(methodID) {
			case 5:
				query = postPicture(conn, picture, user, lat, lon, date);
				break;
				
			case 6:
				query = postVideo(conn, video, user, lat, lon, date);
				break;
				
			case 7:
				query = postText(conn, user, text_or_comment, lat, lon, date);
				break;
				
			case 8:
				query = setVote(conn, postID, vote);
				break;
				
			case 9:
				query = flag(conn, postID);
				break;
				
			case 10:
				query = unFlag(conn, postID);
				break;
				
			case 11:
				query = postComment(conn, postID, text_or_comment, user);
				break;
				
			case 12:
				query = removeMedia(conn, postID, user);
				break;
				
			case 13:
				query = removeComment(conn, postID, user);
				break;
				
			default:
				System.err.printf("Incorrect methodID in sqlPOST (Should be 5-13, was %d)\n", methodID);
				break;
		}
		
		return query;
		
	}

	/*
	 * 	5
	 */
	private PreparedStatement postPicture(Connection conn, File picture, String user, double lat, double lon, String date) {
		
		PreparedStatement stmt = null;

		String query = "INSERT INTO MEDIA (ID, User, Latitude, Longitude, Picture, Video, Media_Type, Date, Votes, Flags)"
							   + " VALUES (null, \"?\", ?, ?, ?, NULL, 0, \"?\", 0, 0);";
		
		FileInputStream pictureStream;
		
		try {
			pictureStream = new FileInputStream(picture);
		}
		catch (FileNotFoundException e) {
			System.err.println("Error creating FileInputStream in postPicture");
			return null;
		}
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lat);
			stmt.setBlob(4, pictureStream);
			stmt.setString(5, date);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in postPicture");
			return null;
		}

		return stmt;

	}

	/*
	 * 	6
	 */
	private PreparedStatement postVideo(Connection conn, File video, String user, double lat, double lon, String date) {
		
		PreparedStatement stmt = null;

		String query = "INSERT INTO MEDIA (ID, User, Latitude, Longitude, Picture, Video, Media_Type, Date, Votes, Flags)"
							   + " VALUES (null, \"?\", ?, ?, NULL, ?, 1, \"?\", 0, 0);";
		
		FileInputStream videoStream;
		
		try {
			videoStream = new FileInputStream(video);
		}
		catch (FileNotFoundException e) {
			System.err.println("Error creating FileInputStream in postVideo");
			return null;
		}
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lat);
			stmt.setBlob(4, videoStream);
			stmt.setString(5, date);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in postVideo");
			return null;
		}

		return stmt;

	}

	/*
	 * 	7
	 */
	private PreparedStatement postText(Connection conn, String text, String user, double lat, double lon, String date) {
		
		PreparedStatement stmt = null;

		if (user == null || user == "" || text == null || text == "" || date == null || date == "") {
			System.err.println("Parameter error in postText");
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
	private PreparedStatement setVote(Connection conn, int postID, int vote) {
		
		PreparedStatement stmt = null;
		
		String query = "UPDATE Media "
					 + "SET Votes = ? "
					 + "WHERE ID = ?;";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, vote);
			stmt.setInt(2, postID);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in setVote");
			return null;
		}

		return stmt;
		
	}

	/*
	 * 	9
	 */
	private PreparedStatement flag(Connection conn, int postID) {

		PreparedStatement stmt = null;
		
		String query = "UPDATE Media "
					 + "SET Flags = Flags + 1 "
					 + "WHERE ID = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in flag");
			return null;
		}

		return stmt;

	}

	/*
	 * 	10
	 */
	private PreparedStatement unFlag(Connection conn, int postID) {

		PreparedStatement stmt = null;
		
		String query = "UPDATE Media "
					 + "SET Flags = Flags - 1 "
					 + "WHERE ID = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in unFlag");
			return null;
		}

		return stmt;

	}

	/*
	 * 	11
	 */
	private PreparedStatement postComment(Connection conn, int postID, String comment, String user) {
		
		if (user == null || user == "" || comment == null || comment == "") {
			System.err.println("Parameter error in postComment");
		}

		PreparedStatement stmt = null;

		String query = "INSERT INTO Commments (Post_ID, Comment, Comment_Date, Comment_Flags, Comment_ID, Comment_User) "
									+ "VALUES (?, \"?\", null, 0, null, \"?\")";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			stmt.setString(2, comment);
			stmt.setString(3, user);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in postComment");
			return null;
		}
		

		return stmt;

	}

	/*
	 * 	12
	 */
	private PreparedStatement removeMedia(Connection conn, int postID, String user) {
		
		PreparedStatement stmt = null;

		String queries = "DELETE FROM Media "
						+ "WHERE ID = ? AND "
						+ "User = \"?\"; "
					
						+ "DELETE FROM Comments "
						+ "WHERE Comment_ID = ? AND "
						+ "User = \"?\"; "
							
						+ "DELETE FROM Tags "
						+ "WHERE Post_ID = ?;";

		try {
			stmt = conn.prepareStatement(queries);
			
			stmt.setInt(1, postID);
			stmt.setString(2, user);
			
			stmt.setInt(3, postID);
			stmt.setString(4, user);
			
			stmt.setInt(5, postID);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeMedia");
			return null;
		}
		

		return stmt;

	}

	/*
	 * 13
	 */
	private PreparedStatement removeComment(Connection conn, int commentID, String user) {
		
		PreparedStatement stmt = null;

		String query = "DELETE FROM Comments "
				+ "WHERE Comment_ID = ? AND User = \"?\";";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, commentID);
			stmt.setString(2, user);
		}
		catch (SQLException e) {
			System.err.println("Error creating PreparedStatement in removeComment");
			return null;
		}
		

		return stmt;

	}
	
	
	
	
	
	
	/*
	 * OBSOLETE
	 */
	
	/*
	 * 	8
	 */
	private String upvote(int postID) {

		String query = "UPDATE Media "
				+ "SET Votes = Votes + 1 "
				+ "WHERE ID = " + postID + ";";

		return query;
	}

	/*
	 * 	9
	 */
	private String downvote(int postID) {

		String query = "UPDATE Media "
				+ "SET Votes = Votes - 1 "
				+ "WHERE ID = " + postID + ";";

		return query;

	}

	/*
	 * 	10
	 */
	private String unUpvote(int postID) {

		return downvote(postID);

	}

	/*
	 * 	11
	 */
	private String unDownvote(int postID) {

		return upvote(postID);

	}

}
