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
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
        
        //TextView video = (TextView) findViewById(R.id.text_video);
        //video.setText(video_url);
        
        
        Button spotify = (Button) findViewById(R.id.but_spotify);
        spotify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	if(isAppInstalled("com.spotify.mobile.android.ui")){
            		Intent intent = new Intent(Intent.ACTION_MAIN);
            		intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            		intent.setComponent(new ComponentName("com.spotify.mobile.android.ui", "com.spotify.mobile.android.ui.Launcher"));
            		intent.putExtra(SearchManager.QUERY, bundle.getString("artist")+" "+bundle.getString("title"));
            		startActivity(intent);
            	}
            	else{
            		appNotInstalledDialog(SongInfoActivity.this, "Spotify", "com.spotify.mobile.android.ui");
            		
            	}
            	
            	
            	
            }

        });

        Button amazon = (Button) findViewById(R.id.but_amazon);
        amazon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	if(isAppInstalled("com.amazon.mp3")){
                	
	            	Intent intent = new Intent();
	            	String query = bundle.getString("artist")+" "+bundle.getString("title");
	            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	intent.setClassName("com.amazon.mp3", "com.amazon.mp3.activity.IntentProxyActivity");
	            	intent.setAction("com.amazon.mp3.action.EXTERNAL_EVENT");
	            	intent.putExtra("com.amazon.mp3.extra.EXTERNAL_EVENT_TYPE","com.amazon.mp3.type.SEARCH");
	            	intent.putExtra("com.amazon.mp3.extra.SEARCH_TYPE", 0); // 0 = Song, 1 = album
	            	intent.putExtra("com.amazon.mp3.extra.SEARCH_STRING", query);
	            	startActivity(intent);
            
            	}
            	else{
            		appNotInstalledDialog(SongInfoActivity.this, "Amazon MP3", "com.amazon.mp3");
            		
            	}
            }
        });

        
        Button youtube = (Button) findViewById(R.id.but_video);
        youtube.setOnClickListener(new View.OnClickListener() {
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

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
           pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
           installed = true;
        } catch (PackageManager.NameNotFoundException e) {
           installed = false;
        }
        return installed;
    }
    
public void appNotInstalledDialog(Context context, String appName, final String appURI){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		//builder.setIcon(R.drawable.dialog_question);
		builder.setTitle("App not installed");
		builder.setMessage(appName + " is not installed on your phone. Do you want to install it from the market?");
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("Go to Market", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int which) {
			
			  Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:"+ appURI));
			  startActivity(marketIntent);
		  }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
			
		  }
		});
		AlertDialog alert = builder.create();
		alert.show();
		
	}

}