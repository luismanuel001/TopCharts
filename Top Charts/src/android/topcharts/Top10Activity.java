package android.topcharts;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Top10Activity extends Activity {
	/** Called when the activity is first created. */
	private StationsDBAdapter dbHelper;
	private Cursor cursor;
	JSONArray  songs;
	String chartID;
	static final String KEY_SONG = "song"; 
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_RANK = "rank";
	static final String KEY_THUMB_URL = "image";
	ListView list;
    LazyAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);
        
        dbHelper = new StationsDBAdapter(this);
		dbHelper.open();
		fillTop10Table();
		//cursor.close();
		//if (dbHelper != null) {
		//	dbHelper.close();
		//}
    }
/*
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent myIntent = new Intent(v.getContext(), SongInfoActivity.class);
        Bundle songInfo = new Bundle();
        
        dbHelper.open();
        cursor = dbHelper.getChartRow(position+1, chartID);
	    songInfo.putString("title", cursor.getString(cursor.getColumnIndex("title")));
	    songInfo.putString("artist", cursor.getString(cursor.getColumnIndex("artist")));
	    songInfo.putString("imageURL", cursor.getString(cursor.getColumnIndex("image")));
        myIntent.putExtras(songInfo);
        cursor.close();
        dbHelper.close();
        startActivity(myIntent);
        

	}
    
*/
    private void fillTop10Table(){
		String url;
        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();


        
        Bundle bundle = getIntent().getExtras();
        chartID = bundle.getString("chart");
        
        //chartID = "hot-100";
        
        // Capitalizes each word on chartID
        String s = chartID.replace("-", " ");
        
        final StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for(int i=0,l=words.length;i<l;++i) {
          if(i>0) result.append(" ");      
          result.append(Character.toUpperCase(words[i].charAt(0)))
                .append(words[i].substring(1));

        }

        TextView zip = (TextView) findViewById(R.id.top10);
        zip.setText(result+" Chart");
        
        if (dbHelper.checkChart(chartID) == false){
	        url = "http://lmanuel.x10.mx/topcharts/charts.php?chart="+chartID;
	        JSONObject json = JSONfunctions.getJSONfromURL(url, null);
	        
	        try{
	        	
	        	songs = json.getJSONArray("charts");
	        	int i = 0;
		        while(i<songs.length()){
		        	
		        	JSONObject e = songs.getJSONObject(i);
					dbHelper.insertChart(e.getString("_id"), e.getInt("rank"), e.getString("title"), e.getString("artist"), e.getString("image"));
					i++;
				}
	        }catch(JSONException e){
	        	 Log.e("log_tag", "Error parsing data "+e.toString());
	        }
        }
        
        cursor = dbHelper.fetchChart(chartID);
    	cursor.moveToFirst();
    	while(cursor.moveToNext()){
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(KEY_TITLE, cursor.getString(cursor.getColumnIndex("title")));
			map.put(KEY_ARTIST, cursor.getString(cursor.getColumnIndex("artist")));
			map.put(KEY_RANK, Integer.toString(cursor.getInt(cursor.getColumnIndex("rank"))));
			map.put(KEY_THUMB_URL, cursor.getString(cursor.getColumnIndex("image")));
	
			// adding HashList to ArrayList
			songsList.add(map);
    	}

		list=(ListView)findViewById(R.id.list);
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);        
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Get the item that was clicked
				Intent myIntent = new Intent(view.getContext(), SongInfoActivity.class);
		        Bundle songInfo = new Bundle();
		        
		        dbHelper.open();
		        cursor = dbHelper.getChartRow(position+1, chartID);
			    songInfo.putString("title", cursor.getString(cursor.getColumnIndex("title")));
			    songInfo.putString("artist", cursor.getString(cursor.getColumnIndex("artist")));
			    songInfo.putString("imageURL", cursor.getString(cursor.getColumnIndex("image")));
		        myIntent.putExtras(songInfo);
		        //cursor.close();
		        //dbHelper.close();
		        startActivity(myIntent);

			}
		});		
    	/*
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.rowlayout, cursor,
        		new String[] {"rank", "title", "artist"}, 
                new int[] { R.id.text_id, R.id.text_song, R.id.text_artist});
		setListAdapter(adapter);
		*/
    	
	}
    
    @Override
    public void onBackPressed() {
    	this.onDestroy();
    	//this.finish();
    	//adapter.imageLoader.clearCache();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.imageLoader.clearCache();
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        this.finish();
    }
}