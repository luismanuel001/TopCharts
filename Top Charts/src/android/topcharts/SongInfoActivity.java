package android.topcharts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SongInfoActivity extends Activity{
	boolean VIDEO_EXISTS = false;
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

        
        
//        cover.setText();
        TextView video = (TextView) findViewById(R.id.text_video);
        String strVideo = bundle.getString("video");
        if (strVideo == null) strVideo = "No video link in database";
        else VIDEO_EXISTS = true;
        video.setText(strVideo);
        
        Button next = (Button) findViewById(R.id.but_video);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	if (VIDEO_EXISTS){
            		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bundle.getString("video"))));
            		
            	}
            	
            	
            }

        });
    }

}