/*
 * USAGE: Depending on integer received from front end, call the respective method
 * 
 * Get information (return value is String):
 * 		Method			ID #		Parameters TODO
 *  -Get Picture     :   1		:	
 *  -Get Video       :   2		:	
 *  -Get Text        :   3		:	
 *  -Get Comments    :   4		:	
 *  
 * Post information (return value is Statement):
 * 		Method			ID #		Parameters TODO
 *  -Post Picture    :   5		:	
 *  -Post Video      :   6		:	
 *  -Post Text       :   7		:	
 *  -Upvote          :   8		:	
 *  -Downvote        :   9		:	
 *  -UnUpvote        :   10		:	
 *  -UnDownvote      :   11		:	
 *  -Flag            :   12		:	
 *  -UnFlag          :   13		:	
 *  -Commenting      :   14		:	
 *  -Remove Media    :   15		:	
 *  -Remove Comment  :   16		:	
 *  
 *  NOTE: Removal of pictures and videos use same method as we only need 
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
	 *       -Have SimpleServlet pass in a Connection, and this code returns a prepared statement (PROBABLY BEST)
	 *       -Still return a string, and have the SimpleServlet do the combining of the actual values with the sql string (NOT SO GOOD)
	 *       -
	 *       -
	 *       -
	 * 
	 * Logic:
	 *  if we have a tag post, that takes priority
	 *   TODO Views? Create a view with an INNER JOIN (see http://www.mysqltutorial.org/mysql-inner-join.aspx)
	 *   View SQL:
	 *    CREATE VIEW Media_and_Tags
	 *    AS
	 *    SELECT *
	 *    FROM TODO
	 *      
	 *   Tag SQL:
	 *    SELECT (Picture or Video, depending on method)
	 *    FROM Media_and_Tags
	 *    WHERE (parameter tag) = (database tag)
	 *   
	 *   
	 *  else we need to search database based on lat, long, last post, and date
	 *   if first time getting images (first fill of screen) lastPost should be zero
	 *   need posts with:
	 *    latitude within 1.5
	 *    longitude within 1.5
	 *    ID > lastPost
	 *    ID < lastPost + 10 + 1 (+1 so we don't get 9, as if only +10 we would get 9)
	 *    Date > timestamp
	 *    
	 *    
	 *    SELECT (Picture or Video, depending on method), Votes, Flags
	 *    FROM Media_and_Tags
	 *    WHERE Latitude  < ABS(lat + 1.5) AND
	 *    		Latitude  > ABS(lat - 1.5) AND
	 *    		Longitude < ABS(lon + 1.5) AND
	 *    		Longitude > ABS(lon - 1.5) AND
	 *    		ID >  lastPost 			   AND
	 *    		ID < (lastPost + 10 + 1)   AND
	 *    		Date > timestamp
	 *    
	 *    If called with a tag add:
	 *    								   AND
	 *    (tag passed in method) = (tag in database)
	 *    
	 *    
	 *    NOTE: pictures should be turned into files (gifs, see )
	 *    If want to sort by new, oldest, newest, is actually easy, just do an ORDER sql call
	 *    
	 *    
	 *    
	 *    
	 *  Things that need to change in SimpleServlet
	 *   -Statement -> PreparedStatement
	 *   -Check for null return, indicates an error in the parameters passed
	 *   -
	 *   -
	 *   -
	 *   -
	 */



	/*
	 * PARAMETERS FOR GET METHODS
	 *  -@param lastPost: Last post ID on user page (so can get next ten) (integer)
	 *  -@param lat: latitude of posts to get 								(double)
	 *  -@param lon: longitude of the posts to get 							(double)
	 *  -@param tag: get all posts with this tag 							(string)
	 *  -@param timestamp: String value of the date in YYYY-MM-DD HH:MM:SS 	(string, HH is 00-23)
	 *  -@param sort: 0 for descending, 1 for ascending 				   	(integer)
	 *  -@param postID: the ID of the post we are getting information from 	(integer)
	 */




	/*
	 * Master method for GET
	 */
	public String sqlGET(int methodID, int postID, int lastPost, double lat, double lon, String tag, String timestamp, int sort) {

		String query = null;

		switch(methodID) {

		case 1:
			query = getPicture(lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 2:
			query = getVideo(lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 3:
			query = getText(lastPost, lat, lon, tag, timestamp, sort);
			break;

		case 4:
			query = getComments(postID);
			break;
			
		default:
			System.err.println("Incorrect methodID in getMaster");
			break;

		}

		return query;
	}


	/*
	 * 	1 TODO
	 */
	private String getPicture(int lastPost, double lat, double lon, String tag, String timestamp, int sort) {

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain a Picture");
			return null;
		}

		String query = null;

		//If the tag is not null then that takes priority over all other parameters
		if (tag != null || tag == "") {
			query = "";

			return query;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "";


		return query;

	}

	/*
	 * 	2 TODO
	 */
	private String getVideo(int lastPost, double lat, double lon, String tag, String timestamp, int sort) {

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain a Video");
			return null;
		}

		String query = null;


		//If the tag is not null then that takes priority over all other parameters
		if (tag != null) {
			query = "";

			return query;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "";


		return query;
	}

	/*
	 * 	3 TODO
	 */
	private String getText(int lastPost, double lat, double lon, String tag, String timestamp, int sort) {

		//Check parameters for correctness
		if (lastPost < 0 || timestamp == null || timestamp == "" || sort < 0 || sort > 1) {
			System.err.println("Incorrect parameters to obtain Text");
			return null;
		}

		String query = null;


		//If the tag is not null then that takes priority over all other parameters
		if (tag != null) {
			query = "";

			return query;
		}

		//Only make it here if tag is NULL, need to search database based on other parameters
		query = "";


		return query;
	}

	/*
	 * 	4 TODO
	 */
	private String getComments(int postID) {
		
		if (postID < 0) {
			System.err.println("Incorrect postID to obtain Comments : postID needs to be greater than zero");
			return null;
		}
		
		String query = null;





		return query;
	}








	/*
	 * PARAMETERS FOR POST METHODS:
	 *  -@param :
	 *  -@param :
	 *  -@param text: text to be posted in the database
	 *  -@param comment: the comment to be stored in the database
	 *  -@param postID: the ID of the post being referenced
	 *  -
	 *
	 * NUMBERING FOR POST METHODS:
	 *  Post Picture    :   5
	 *  Post Video      :   6
	 *  Post Text       :   7
	 *  Upvote          :   8
	 *  Downvote        :   9
	 *  UnUpvote        :   10
	 *  UnDownvote      :   11
	 *  Flag            :   12
	 *  UnFlag          :   13
	 *  Commenting      :   14
	 *  Remove Post     :   15
	 *  Remove Comment  :   16
	 */


	
	public String sqlPOST(int methodID, int postID, String user, int lat, int lon, String text_or_comment, String date /* Something for Picture */ /* Something for Video */) {
		
		String query = null;
		
		switch(methodID) {
			case 5: //TODO
				
				break;
				
			case 6: //TODO
				
				break;
				
			case 7:
				query = postText(user, text_or_comment, lat, lon, date);
				break;
				
			case 8:
				query = upvote(postID);
				break;
				
			case 9:
				query = downvote(postID);
				break;
				
			case 10:
				query = unUpvote(postID);
				break;
				
			case 11:
				query = unDownvote(postID);
				break;
				
			case 12:
				query = flag(postID);
				break;
				
			case 13:
				query = unFlag(postID);
				break;
				
			case 14:
				query = postComment(user, postID, text_or_comment);
				break;
				
			case 15:
				query = removeMedia(postID, user);
				break;
				
			case 16:
				query = removeComment(postID, user);
				break;
				
			default:
				System.err.println("Incorrect methodID in postMaster");
				break;
		}
		
		return query;
		
	}

	/*
	 * 	5 TODO
	 */
	private String postPicture() {

		String query = null;

		return query;

	}

	/*
	 * 	6 TODO
	 */
	private String postVideo() {

		String query = null;

		return query;

	}

	/*
	 * 	7 TODO
	 */
	private String postText(String user, String text, int lat, int lon, String date) {

		if (user == null || user == "" || text == null || text == "" || date == null || date == "") {
			System.err.println("Parameter error in postText");
		}
		
		String query = null;
		

		return query;

	}

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

	/*
	 * 	12
	 */
	private String flag(int postID) {

		String query = "UPDATE Media "
				+ "SET Flags = Flags + 1 "
				+ "WHERE ID = " + postID + ";";

		return query;

	}

	/*
	 * 	13
	 */
	private String unFlag(int postID) {

		String query = "UPDATE Media "
				+ "SET Flags = Flags - 1 "
				+ "WHERE ID = " + postID + ";";

		return query;

	}

	/*
	 * 	14
	 */
	private String postComment(String user, int postID, String comment) {

		String query = "INSERT INTO Commments (Post_ID, Comment, Date, Flags, Comment_ID) "
				+ "VALUES (" + postID +", \"" + comment + "\", null, null, null)";

		return query;

	}

	/*
	 * 	15
	 */
	private String removeMedia(int postID, String user) {

		String queries = null;

		queries = "DELETE FROM Media "
				+ "WHERE ID = " + postID + " AND User = " + user + "; "
				+ "DELETE FROM Comments "
				+ "WHERE Comment_ID = " + postID + " AND User = " + user  + "; "
				+ "DELETE FROM Tags "
				+ "WHERE Post_ID = " + postID + ";";

		return queries;

	}

	/*
	 * 16
	 */
	private String removeComment(int commentID, String user) {

		String query = "DELETE FROM Comments "
				+ "WHERE Comment_ID = " + commentID + " AND User = " + user + ";";

		return query;

	}

}
