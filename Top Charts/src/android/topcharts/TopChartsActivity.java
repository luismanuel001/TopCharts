package android.topcharts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopChartsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts);
        
        
        Button hot100 = (Button) findViewById(R.id.button_hot_100);
        hot100.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("hot-100", view);
		        
            	}
        	});
        
        Button radioSongs = (Button) findViewById(R.id.button_radio);
        radioSongs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("radio-songs", view);
		        
            	}
        	});
        
        Button hipHop = (Button) findViewById(R.id.button_hip_hop);
        hipHop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("r-b-hip-hop-songs", view);
		        
            	}
        	});
        
        Button latinSongs = (Button) findViewById(R.id.button_latin);
        latinSongs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("latin-songs", view);
		        
            	}
        	});
        
        Button alternative = (Button) findViewById(R.id.button_alternative);
        alternative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("alternative-songs", view);
		        
            	}
        	});
        
        Button popSongs = (Button) findViewById(R.id.button_pop);
        popSongs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("pop-songs", view);
		        
            	}
        	});
        
        Button danceClub = (Button) findViewById(R.id.button_dance_club);
        danceClub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("dance-songs", view);
		        
            	}
        	});
        
        Button rockSongs = (Button) findViewById(R.id.button_rock);
        rockSongs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("rock-songs", view);
		        
            	}
        	});
        
        Button ringstones = (Button) findViewById(R.id.button_ringtones);
        ringstones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("ringtones", view);
		        
            	}
        	});
        
        Button youTube = (Button) findViewById(R.id.button_youtube);
        youTube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            		goToChart("youtube", view);
		        
            	}
        	});
        
        	/*
	    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	    	int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
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
	        */	
    }
    
    public void goToChart(String chart, View v){
    	
    	Intent myIntent = new Intent(v.getContext(), Top10Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chart", chart);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, 0);
    }
    
    public void onBackPressed() {
    	this.onDestroy();
    }
    
    public void onDestroy() {
    	super.onDestroy();
    	this.finish();
    }
}