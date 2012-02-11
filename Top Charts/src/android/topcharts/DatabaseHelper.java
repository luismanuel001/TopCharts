package android.topcharts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "applicationdata";

	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		//StationsTable.onCreate(database);
		database.execSQL("create table stations "
				+ "(_id integer primary key, "
				+ " name text not null, " + " description text,"
				+ " genre text," + " market text);");
		database.execSQL("create table locations "
				+ "(_id integer primary key autoincrement, "
				+ " zipcode text not null, " + " market text);");
		database.execSQL("create table songs "
				+ "(_id integer primary key, " + " title text not null, "
				+ " artist text not null," + " artistID integer not null,"
				+ " genre text," + " cover text," 
				+ " yesURL text not null," + " lyricsURL text);");
		database.execSQL("create table top10 "
				+ "(_id integer primary key autoincrement, position integer not null,"
				+ " stationID integer not null, " + "songID integer not null);");
		database.execSQL("CREATE TABLE charts "
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, chartID VARCHAR(30) NOT NULL, rank INT NOT NULL, "
				+ "title TEXT NOT NULL, " + "name TEXT NOT NULL,  image TEXT, UNIQUE(_id, rank));");
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		//StationsTable.onUpgrade(database, oldVersion, newVersion);
		database.execSQL("DROP TABLE IF EXISTS stations");
		database.execSQL("DROP TABLE IF EXISTS locations");
		database.execSQL("DROP TABLE IF EXISTS songs");
		database.execSQL("DROP TABLE IF EXISTS top10");
		database.execSQL("DROP TABLE IF EXISTS charts");
		onCreate(database);
	}
}