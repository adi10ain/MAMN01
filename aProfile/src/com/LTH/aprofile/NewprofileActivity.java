package com.LTH.aprofile;

import java.util.LinkedList;

import com.LTH.aprofile.Preferences.Preference;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class NewprofileActivity extends Activity {
	private Profile targetProfile;

	private TextView TV_profileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_newprofile);

		targetProfile = MainActivity.targetProfile;

		TV_profileName = (TextView) findViewById(R.id.ProfileName);
		TV_profileName.setText("" + targetProfile);
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.desiredPreferences);
		linearLayout.addView(targetProfile.genPrefButtons(this));
		
		

	}

	// When pressed accept button
	public void btnApprove(View view) {
		
		LinkedList<Integer> pref = new LinkedList<Integer>();
		
		//Find out what preferences we want to affect
		for (Preference p : targetProfile.getPref().values()) {
			ImageView btn = (ImageView) findViewById(p.getType());
			if (btn.isSelected())
				pref.add(p.getType());
		}

		// return to calling activity and send chosen preferences
		Intent result = new Intent(this, MainActivity.class);
		result.putExtra("PREFERENCES", pref);
		setResult(MainActivity.APPROVE_NEW_PROFILE, result);

		finish();
	}

	// when pressed decline button
	public void btnDecline(View view) {
		setResult(MainActivity.DECLINE_NEW_PROFILE);
		finish();
	}

}
