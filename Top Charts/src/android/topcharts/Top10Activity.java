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
	JSONObject e;
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
        	
        	JSONArray  songs = json.getJSONArray("songs");
        	
	        for(int i=0;i<songs.length();i++){						
				HashMap<String, String> map = new HashMap<String, String>();	
				e = songs.getJSONObject(i);
				
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
        
        
//        Button next = (Button) findViewById(R.id.button2);
//        next.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), TopChartsActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//
//        });
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		l.setTextFilterEnabled(true);
		@SuppressWarnings("unchecked")
		HashMap<String, String> o = (HashMap<String, String>) l.getItemAtPosition(position);
		
		Intent myIntent = new Intent(v.getContext(), SongInfoActivity.class);
        Bundle songinfo = new Bundle();
        songinfo.putString("id", o.get("id"));
        songinfo.putString("title", o.get("title"));
        songinfo.putString("by", o.get("by"));
        songinfo.putString("cover", o.get("cover"));
        songinfo.putString("video", o.get("video"));
        myIntent.putExtras(songinfo);
        startActivityForResult(myIntent, 0);
  		
        
        //Toast.makeText(Top10Activity.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show();
		
		
//		final ListView lv = getListView();
//        lv.setTextFilterEnabled(true);
//        lv.setOnItemClickListener(new OnItemClickListener() {
//        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
//        		@SuppressWarnings("unchecked")
//				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
//        		Toast.makeText(Main.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show(); 
//
//			}
//		});

	}
}