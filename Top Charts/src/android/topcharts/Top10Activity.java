package android.topcharts;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Top10Activity extends ListActivity {
	/** Called when the activity is first created. */
	JSONArray  songs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top10);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String genre, url;
        Bundle bundle = getIntent().getExtras();
        genre = bundle.getString("genre");
        url = "http://api.yes.com/1/chart?genre=" + genre;// + "&hot=vote";
        
        
        JSONObject json = JSONfunctions.getJSONfromURL(url);
                
        try{
        	
        	songs = json.getJSONArray("songs");
        	
	        for(int i=0;i<songs.length();i++){						
				HashMap<String, String> map = new HashMap<String, String>();	
				JSONObject e = songs.getJSONObject(i);
				
				map.put("id",  String.valueOf(i));
	        	map.put("title", e.getString("title"));
	        	map.put("by", "By: " + e.getString("by"));
	        	mylist.add(map);			
			}		
        }catch(JSONException e)        {
        	 Log.e("log_tag", "Error parsing data "+e.toString());
        }
        
        ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.rowlayout, 
                        new String[] {"title" , "by" }, 
                        new int[] { R.id.text_song, R.id.text_artist });
        
        setListAdapter(adapter);
        
        
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent myIntent = new Intent(v.getContext(), SongInfoActivity.class);
        Bundle songInfo = new Bundle();
        
        try {
        	JSONObject e = songs.getJSONObject(position);
			songInfo.putString("by", e.get("by").toString());
	        songInfo.putString("cover", e.get("cover").toString());
	        songInfo.putString("id", e.get("id").toString());
	        songInfo.putString("title", e.get("title").toString());
	        songInfo.putString("video", e.get("video").toString());
        } catch (JSONException e) {
        	Log.e("log_tag", "Error parsing data "+e.toString());
		}
        myIntent.putExtras(songInfo);
        startActivityForResult(myIntent, 0);

	}
}