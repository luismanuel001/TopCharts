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
	String chartID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);

        dbHelper = new StationsDBAdapter(this);
		dbHelper.open();
		fillTop10Table();
		//cursor.close();
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
        cursor = dbHelper.getChartRow(position+1, chartID);
	    songInfo.putString("title", cursor.getString(cursor.getColumnIndex("title")));
	    songInfo.putString("artist", cursor.getString(cursor.getColumnIndex("name")));
	    songInfo.putString("imageURL", cursor.getString(cursor.getColumnIndex("image")));
        myIntent.putExtras(songInfo);
        cursor.close();
        dbHelper.close();
        startActivity(myIntent);
        

	}
    
    private void fillTop10Table(){
		String url;

        
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
					dbHelper.insertChart(e.getString("_id"), e.getInt("rank"), e.getString("title"), e.getString("name"), e.getString("image"));
					i++;
				}
	        }catch(JSONException e){
	        	 Log.e("log_tag", "Error parsing data "+e.toString());
	        }
        }
        
        cursor = dbHelper.fetchChart(chartID);
    	cursor.moveToFirst();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.rowlayout, cursor,
        		new String[] {"rank", "title", "name"}, 
                new int[] { R.id.text_id, R.id.text_song, R.id.text_artist});
		setListAdapter(adapter);
	}
    
    @Override
    public void onBackPressed() {
    	finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}