package android.topcharts;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StationsTable {
	// Database creation SQL statement
	private static final String CREATE_STATIONS = "create table stations "
			+ "(_id integer primary key, "
			+ " name text not null, " + " desc text,"
			+ " genre text," + " market text);";
	private static final String CREATE_LOCATIONS = "create table locations "
			+ "(_id integer primary key autoincrement, "
			+ " zipcode text not null, " + " market text);";
	private static final String CREATE_CHARTS = "CREATE TABLE charts "
			+ "(_id VARCHAR(30) PRIMARY KEY, rank INT NOT NULL, "
			+ "title TEXT NOT NULL, " + "name TEXT NOT NULL,  image TEXT);";
//	private static final String FOREIGN_KEY_TRIGGER = "CREATE TRIGGER fk_station_location "
//			+ " BEFORE INSERT ON stations"
//			+ " FOR EACH ROW BEGIN"
//			+ " SELECT CASE WHEN ((SELECT zipcode FROM locations"
//			+ " WHERE "+colDeptID+"=new."+colDept+" ) IS NULL)"
//			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;"
//			+ "  END;";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_STATIONS);
		database.execSQL(CREATE_LOCATIONS);
		database.execSQL(CREATE_CHARTS);
		//database.execSQL(FOREIGN_KEY_TRIGGER);
		
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(StationsTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS stations");
		onCreate(database);
	}
}
