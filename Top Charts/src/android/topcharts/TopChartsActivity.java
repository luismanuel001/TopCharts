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

    	String genre = "";

    	switch (checkedRadioButton) {
    	case R.id.rad_country : genre = "Country";
    	break;
    	case R.id.rad_electronica : genre = "Electronica";
    	break;
    	case R.id.rad_hip_hop : genre = "Hip-Hop";
    	break;
    	case R.id.rad_latin : genre = "Latin";
    	break;
    	case R.id.rad_metal : genre = "Metal";
    	break;
    	case R.id.rad_pop : genre = "Pop";
    	break;
    	case R.id.rad_punk : genre = "Punk";
    	break;
    	case R.id.rad_rock : genre = "Rock";
    	break;
    	}
            	
                Intent myIntent = new Intent(view.getContext(), Top10Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("genre", genre);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, 0);
            }

        });
    }        

}