package android.topcharts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchLocationActivity extends Activity{

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        
        final EditText zipCode = (EditText) findViewById(R.id.textbox_zipcode);
        zipCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	zipCode.setText("");
            }
    	});
        final Button next = (Button) findViewById(R.id.but_setloc);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	
            	String location = zipCode.getText().toString();
            	Intent myIntent = new Intent(view.getContext(), StationsActivity.class);
		        Bundle bundle = new Bundle();
		        bundle.putString("location", location);
		        myIntent.putExtras(bundle);
		        startActivity(myIntent);
            	
            }  
        });
        
    }
}
