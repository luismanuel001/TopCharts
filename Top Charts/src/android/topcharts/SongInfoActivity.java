package android.topcharts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SongInfoActivity extends Activity{
	boolean VIDEO_EXISTS = false;
	private String video_url;
	//private StationsDBAdapter dbHelper;
	//private Cursor cursor;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songinfo);
        
        final Bundle bundle = getIntent().getExtras();
        TextView by = (TextView) findViewById(R.id.text_by);
        by.setText(bundle.getString("artist"));
        TextView title = (TextView) findViewById(R.id.text_title);
        title.setText(bundle.getString("title"));
        
        String cover = bundle.getString("imageURL");
        try {
        	  ImageView i = (ImageView)findViewById(R.id.img_cover);
        	  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(cover).getContent());
        	  i.setImageBitmap(bitmap); 
        	} catch (MalformedURLException e) {
        	  e.printStackTrace();
        	} catch (IOException e) {
        	  e.printStackTrace();
        	}

        
    	video_url = getYouTubeURL(bundle.getString("title"), bundle.getString("artist"));
        
        TextView video = (TextView) findViewById(R.id.text_video);
        video.setText(video_url);
        
        Button next = (Button) findViewById(R.id.but_video);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video_url)));           	
            	
            }

        });
    }
    
    private String getYouTubeURL(String title, String artist){
    	
    	String q = title + " " + artist;
        
        
        q = q.replace(" ", "%20");
        
        try {
	        URL jsonURL = new URL("http://gdata.youtube.com/feeds/api/videos?v=2&alt=jsonc&q="+q+"&category=Music&max-results=10");
	        URLConnection jc = jsonURL.openConnection();
	        InputStream is = jc.getInputStream();
	        String jsonTxt = IOUtils.toString( is );
	        
	        JSONObject json = new JSONObject(jsonTxt);
			JSONObject video_feeds = json.getJSONObject("data");
			JSONArray items = video_feeds.getJSONArray("items");
			JSONObject e = items.getJSONObject(0);
			JSONObject player = e.getJSONObject("player");
			
			
			video_url = player.getString("default");
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data "+e.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    	return video_url;
    }
    
    @Override
    public void onBackPressed() {
    	this.finish();
    }
}