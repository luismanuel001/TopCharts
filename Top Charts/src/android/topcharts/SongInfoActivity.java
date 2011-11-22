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
	String video_url;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songinfo);
        
        final Bundle bundle = getIntent().getExtras();
        TextView by = (TextView) findViewById(R.id.text_by);
        by.setText(bundle.getString("by"));
        TextView title = (TextView) findViewById(R.id.text_title);
        title.setText(bundle.getString("title"));
        
        String cover = bundle.getString("cover");
        try {
        	  ImageView i = (ImageView)findViewById(R.id.img_cover);
        	  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(cover).getContent());
        	  i.setImageBitmap(bitmap); 
        	} catch (MalformedURLException e) {
        	  e.printStackTrace();
        	} catch (IOException e) {
        	  e.printStackTrace();
        	}

        
        
        
        String q = bundle.getString("title") + " " + bundle.getString("by");
        q = q.replace(" ", "%20");
        
        try {
	        URL jsonURL = new URL("http://gdata.youtube.com/feeds/api/videos?v=2&alt=jsonc&q="+q+"&category=Music&format=1&max-results=10");
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
        
        
        TextView video = (TextView) findViewById(R.id.text_video);
        String strVideo = video_url;
        if (strVideo == null) strVideo = "No video link in database";
        else VIDEO_EXISTS = true;
        video.setText(strVideo);
        
        Button next = (Button) findViewById(R.id.but_video);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	//if (VIDEO_EXISTS){
            		//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bundle.getString("video"))));
            	
            		
            		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video_url)));
            	//}
            	
            	
            }

        });
    }

}