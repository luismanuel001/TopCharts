package android.topcharts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class TopChartsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Button next = (Button) findViewById(R.id.but_totop10);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
		        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		    	int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		
		    	String chart = "";
		
		    	switch (checkedRadioButton) {
		    	case R.id.rad_country : chart = "hot-100";
		    	break;
		    	case R.id.rad_electronica : chart = "radio-songs";
		    	break;
		    	case R.id.rad_hip_hop : chart = "r-b-hip-hop-songs";
		    	break;
		    	case R.id.rad_latin : chart = "latin-songs";
		    	break;
		    	case R.id.rad_metal : chart = "alternative-songs";
		    	break;
		    	case R.id.rad_pop : chart = "pop-songs";
		    	break;
		    	case R.id.rad_punk : chart = "dance-club-play-songs";
		    	break;
		    	case R.id.rad_rock : chart = "rock-songs";
		    	break;
		    	}
		            	
		        Intent myIntent = new Intent(view.getContext(), Top10Activity.class);
		        Bundle bundle = new Bundle();
		        bundle.putString("chart", chart);
		        myIntent.putExtras(bundle);
		        startActivityForResult(myIntent, 0);
		        }

        	});
    }        

}