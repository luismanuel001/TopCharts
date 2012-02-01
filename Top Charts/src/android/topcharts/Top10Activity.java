package android.topcharts;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Top10Activity extends ListActivity {
	/** Called when the activity is first created. */
	private StationsDBAdapter dbHelper;
	private Cursor cursor;
	JSONArray  songs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);

        dbHelper = new StationsDBAdapter(this);
		dbHelper.open();
		fillTop10Table();
		if (dbHelper != null) {
			dbHelper.close();
		}
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent myIntent = new Intent(v.getContext(), SongInfoActivity.class);
        Bundle songInfo = new Bundle();
        dbHelper.open();
        
	    songInfo.putInt("songID", dbHelper.getSongIDfromPos(position+1));
        myIntent.putExtras(songInfo);
        cursor.close();
        dbHelper.close();
        startActivity(myIntent);
        finish();

	}
    
    private void fillTop10Table(){
		String url;
		int id;
		boolean insert;
        
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        
        TextView zip = (TextView) findViewById(R.id.top10);
        zip.setText("Top 10 songs played on " + dbHelper.getName(id));
        
        if (dbHelper.checkStation(id) == false){
	        url = "http://api.yes.com/1/chart?name=" + dbHelper.getName(id);
	        JSONObject json = JSONfunctions.getJSONfromURL(url);
	        
	        try{
	        	
	        	songs = json.getJSONArray("songs");
	        	int i = 0;
		        while(i<10){
		        	
					//JSONObject e = songs.getJSONObject(songs.length()-i-1);
		        	JSONObject e = songs.getJSONObject(i);
					insert = dbHelper.insertTop10(i+1, id, e.getInt("id"));
					if (insert){
						i++;
					}
				}
		        
	        }catch(JSONException e){
	        	 Log.e("log_tag", "Error parsing data "+e.toString());
	        }
        }
        cursor = dbHelper.fetchTop10FromStation(id);
    	cursor.moveToFirst();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.rowlayout, cursor,
        		new String[] {"title", "artist"}, 
                new int[] { R.id.text_song, R.id.text_artist});
        
		setListAdapter(adapter);
	}
    
    @Override
    public void onBackPressed() {
    	finish();
    }
}