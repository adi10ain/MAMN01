package com.LTH.aprofile;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class AddProfile extends Activity {
	ListView availableAP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profile);
		
		availableAP = (ListView) findViewById(R.id.listAvailableAP);
		
		TextView tv=new TextView(getApplicationContext());
	    tv.setText("TEST");
		availableAP.addView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_profile, menu);
		return true;
	}

}
