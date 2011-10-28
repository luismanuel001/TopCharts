package android.topcharts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SongInfoActivity extends Activity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songinfo);
        
        Bundle bundle = getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.text_title);
        title.setText(bundle.getString("title"));
        TextView by = (TextView) findViewById(R.id.text_by);
        by.setText(bundle.getString("by"));
//        ImageView cover = (ImageView) findViewById(R.id.img_cover);
//        cover.setText(bundle.getString("cover"));
        TextView video = (TextView) findViewById(R.id.text_video);
        title.setText(bundle.getString("video"));
    }

//private Bitmap loadImageFromNetwork(String url)
//        throws MalformedURLException, IOException {
//    HttpURLConnection conn = (HttpURLConnection) (new URL(url))
//            .openConnection();
//    conn.connect();
//    return BitmapFactory.decodeStream(new FlushedInputStream(conn
//            .getInputStream()));
//}
}