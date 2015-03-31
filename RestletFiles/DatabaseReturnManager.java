import org.json.JSONException;
import org.json.JSONObject;
import java.sql.ResultSet;



/*
 *  USAGE: For each retrieval type (picture, video, text, comments) call one of these methods with the resultset
 *  	   recived from the database to get the corresponding JSON packet
 * 		Action			ID #		Method		Parameters (See below for parameter descriptions):
 *    -Picture		:	 1		:	TODO	:	ResultSet rs
 *    -Video		:	 2		:	TODO	:	ResultSet rs
 *    -Text			:	 3		:	TODO	:	ResultSet rs
 *    -Comment		:	 4		:	TODO	:	ResultSet rs
 *    
 *    -@param ResultSet rs: the result set returned by the database after the SQL query
 */



public class DatabaseReturnManager {

	/*
	 * @param ResultSet rs: The ResultSet containing all the info for a Picture TODO
	 */
	public JSONObject pictureToJSON(ResultSet rs) {
		
	}
	
	/*
	 * @param ResultSet rs: The ResultSet containing all the info for a Video TODO
	 */
	public JSONObject videoToJSON(ResultSet rs) {
		
	}
	
	/*
	 * @param ResultSet rs: The ResultSet containing all the info for text TODO
	 */
	public JSONObject textToJSON(ResultSet rs) {
		
	}
	
	/*
	 * @param ResultSet rs: The ResultSet containing all the info for comments TODO
	 */
	public JSONObject commentsToJSON(ResultSet rs) {
		
	}
	
}
