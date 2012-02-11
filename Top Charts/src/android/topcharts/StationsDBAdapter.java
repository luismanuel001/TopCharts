package android.topcharts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StationsDBAdapter {

	// Database fields
	private Context context;
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public StationsDBAdapter(Context context) {
		this.context = context;
	}

	public StationsDBAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
            dbHelper.close();
        }
	}

	
/** * Create a new station If the station is successfully created return the new * rowId for that note, otherwise return a -1 to indicate failure. */

	public void insertStation(int id, String name, String description, String genre, String market, String zipcode) {
		if (db.rawQuery("SELECT zipcode, market FROM locations WHERE zipcode = '"
						+ zipcode +"' AND market = '"+ market +"';", null).getCount() == 0){
			db.execSQL( "INSERT INTO locations VALUES(null, '" + zipcode + "', '"
					+ market + "')");}
		if (db.rawQuery("SELECT _id FROM stations WHERE _id = '"
				+ id +"';", null).getCount() == 0){
			db.execSQL( "INSERT INTO stations VALUES(" + id + ", '"+ name + 
						"', '" + description + "', '" + genre + "', '" + market + "')");
		}
					
	}
	
	public boolean insertTop10(int position, int stationID, int songID) {
		boolean result = false;
		Cursor cursor = db.rawQuery("SELECT _id FROM songs WHERE _id = '"
						+ songID +"'", null);
		if (cursor.getCount() == 0){
			
			String url = "http://api.yes.com/1/media?mid=" + songID;
	        JSONObject json = JSONfunctions.getJSONfromURL(url, null);
	        try{
	        	
	        	JSONArray song = json.getJSONArray("songs");				
				JSONObject e = song.getJSONObject(0);
				
				insertSong(songID, e.getString("title"), e.getString("by"), e.getInt("artist"), e.getString("genre"), e.getString("cover"), e.getString("link"), e.getJSONObject("lyrics").getString("url"));
				
	        }catch(JSONException e){
	        	 Log.e("log_tag", "Error parsing data "+e.toString());
	        }
	        
		}
		cursor.moveToFirst();
		if (cursor.getCount() == 1){
			result = true;
			db.execSQL( "INSERT INTO top10 VALUES( null," + position 
						+ ", " + stationID + ", " + songID + ")");
		}
		return result;
	}
	
	public void insertSong(int id, String title, String artist, int artistID, String genre, String cover, String yesURL, String lyricsURL) throws SQLException{
		//if (title != "null" && artist != "null"){
			db.execSQL( "INSERT INTO songs VALUES( " + id + ", '" + title + "', '"
						+ artist + "', " + artistID + ", '" + genre + "', '" + cover + "', '"
						+ yesURL + "', '" + lyricsURL + "')");			
		//}
	}
	
	public void insertChart(String id, int rank, String title, String artist, String image) {
		try{	
		db.execSQL( "INSERT INTO charts VALUES( NULL, '" + id + "', " + rank + ", '"
						+ title + "', '" + artist + "', '" + image + "')");	
			
		}catch(SQLException e){
	   	 Log.e("log_tag", "Error Inserting to Charts data "+e.toString());
   }
	}
	
/** Update the station */

	public void updateStation(int id, String column, String value) {
		db.execSQL(	"UPDATE stations SET " + column + "='" + value 
					+ "' WHERE _id = " + id);
	}

	
/** Deletes station */

	public void deleteStation(int id) {
		db.execSQL(	"DELETE FROM stations WHERE _id = " + id);
	}

	public Cursor fetchTop10FromStation(int stationID) {
		return db.rawQuery( "SELECT * FROM songs, top10 WHERE top10.stationID = "
							+ stationID + " AND top10.songID = songs._id ORDER BY top10.position", null);
	}
	
	public Cursor fetchChart(String id) {
		Cursor result = null;
		try{
			result = db.rawQuery( "SELECT * FROM charts WHERE chartID = '" + id + "'", null);
		}catch(SQLException e){
		   	 Log.e("log_tag", "Error quering to Charts data "+e.toString());
		}
		return result;
	}

	public Cursor fetchStations(int id) throws SQLException {
		Cursor mCursor = db.rawQuery("SELECT * FROM stations WHERE _id = " + id, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public String getName(int id) throws SQLException {
		
		Cursor mCursor = db.rawQuery(	"SELECT name FROM stations WHERE _id = " + id, null);
		mCursor.moveToFirst();
		return mCursor.getString(mCursor.getColumnIndex("name"));
	}
	
	public boolean checkZipCode(String zipcode) throws SQLException{
		boolean result = false;
		if (db.rawQuery("SELECT zipcode, market FROM locations WHERE zipcode = "
						+ zipcode, null).getCount() != 0) result = true;
		return result;
	}
	
	public boolean checkStation(int id) throws SQLException{
		boolean result = false;
		if (db.rawQuery("SELECT * FROM top10 WHERE stationID = "
						+ id, null).getCount() != 0) result = true;
		return result;
	}
	
	public boolean checkChart(String id) throws SQLException{
		boolean result = false;
		if (db.rawQuery("SELECT * FROM charts WHERE chartID = '" + id + "'", null).getCount() != 0) result = true;
		return result;
	}

	public Cursor getLocationInfo(String zipcode) throws SQLException{
		return db.rawQuery(   "SELECT stations._id, name, description, genre,"
							+ " stations.market FROM stations, locations"
							+ " WHERE locations.market = stations.market AND zipcode = '"
							+ zipcode +"'", null);
	}

	public int getSongIDfromPos(int position) throws SQLException {
		
		Cursor mCursor = db.rawQuery(	"SELECT songID FROM top10 WHERE position = " + position, null);
		mCursor.moveToFirst();
		return mCursor.getInt(mCursor.getColumnIndex("songID"));
	}
	
	public Cursor getChartRow(int position, String id) throws SQLException {
		Cursor mCursor = db.rawQuery("SELECT * FROM charts WHERE chartID = '"+ id +"' AND rank = " + position, null);
		mCursor.moveToFirst();
		return mCursor;
	}
	public Cursor getSong(int id) throws SQLException {
		
		Cursor mCursor = db.rawQuery(	"SELECT * FROM songs WHERE _id = " + id, null);
		mCursor.moveToFirst();
		return mCursor;
	}
	
}