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

public class StationsActivity extends ListActivity {
	/** Called when the activity is first created. */
	private StationsDBAdapter dbHelper;
	private Cursor cursor;
	private JSONArray  stations;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);
        
        dbHelper = new StationsDBAdapter(this);
		dbHelper.open();
		fillStationsTable();
		if (dbHelper != null) {
			dbHelper.close();
		}
          
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent myIntent = new Intent(v.getContext(), Top10Activity.class);
        Bundle station = new Bundle();
        
    	cursor.moveToPosition(position);
        station.putInt("id", cursor.getInt(cursor.getColumnIndex("_id")));
        cursor.close();
        myIntent.putExtras(station);
        startActivity(myIntent);
        finish();
	}
    
	private void fillStationsTable(){
		String location, url;
        
        Bundle bundle = getIntent().getExtras();
        location = bundle.getString("location");
        
        TextView zip = (TextView) findViewById(R.id.top10);
        zip.setText("Stations near " + location);
        
        if (dbHelper.checkZipCode(location) == false){
	        url = "http://api.yes.com/1/stations?loc=" + location + "&max=100";
	        JSONObject json = JSONfunctions.getJSONfromURL(url);
	        
	        try{
	        	
	        	stations = json.getJSONArray("stations");
	        	
		        for(int i=0;i<stations.length();i++){						
					JSONObject e = stations.getJSONObject(i);
					dbHelper.insertStation(e.getInt("id"), e.getString("name"), e.getString("desc"), e.getString("genre"), e.getString("market"), location);			
				}

		        
		        //cursor.moveToFirst();
	        }catch(JSONException e){
	        	 Log.e("log_tag", "Error parsing data "+e.toString());
	        }
        }
        cursor = dbHelper.getLocationInfo(location);
        cursor.moveToFirst();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.rowlayout, cursor,
        		new String[] {"name", "desc"}, 
                new int[] { R.id.text_song, R.id.text_artist});
		setListAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	@Override
    public void onBackPressed() {
    	finish();
    }
}

