package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Settings;
import com.LTH.aprofile.Classes.Preferences.BrightnessPreference;
import com.LTH.aprofile.Classes.Preferences.SoundLevelPreference;
import com.LTH.aprofile.Classes.Preferences.VibrationPreference;
import com.LTH.aprofile.Classes.Sensors.GestureActivity;
import com.LTH.aprofile.Classes.Sensors.GestureSelector;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SettingsActivity extends GestureActivity {

	LinearLayout list_Profiles;
	private static Settings settings;
	private GestureSelector gestSelect;

	private static Activity activity;

	private CheckBox toggleGesture;

	// used to add support for editing multiple profiles
	private int amtFinishedAnims;
	private LinkedList<Profile> profilesToEdit;

	public static final int REQUEST_CODE_CHANGE_PROFILE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setGestureUpdateInterval(500);
		addListenForGesture(GESTURE_UP);
		addListenForGesture(GESTURE_DOWN);
		addListenForGesture(GESTURE_RIGHT);
		addListenForGesture(GESTURE_LEFT);
		gestureSensor.initiate();

		setContentView(R.layout.settings);

		settings = MainActivity.settings;

		gestSelect = new GestureSelector(this);

		toggleGesture = (CheckBox) findViewById(R.id.GestureToggle);
		toggleGesture.setChecked(settings.getGestureToggle());

		list_Profiles = (LinearLayout) findViewById(R.id.listProfiles);

		amtFinishedAnims = 0;
		profilesToEdit = new LinkedList<Profile>();

		activity = this;

		listAllProfiles();
	}

	private void listAllProfiles() {
		gestSelect.clear();
		// Clear old wifi-profiles list
		list_Profiles.removeAllViews();

		// add spacing between each item (profile)
		TableRow.LayoutParams params = new TableRow.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 5);

		// get background color for each item (profile)
		int color = getResources().getColor(R.color.METRO_DARK_BROWN);

		// make transparent
		color = Color.argb(80, Color.red(color), Color.green(color),
				Color.blue(color));

		// get text font for each item (profile)

		// Displays all profiles in the wifi-profiles list
		ArrayList<Profile> profiles = settings.getProfiles();
		for (final Profile profile : profiles) {

			TextView tv = new TextView(this);
			tv.setText("" + profile);
			tv.setTextSize(16.0f);
			
			// Make this view size independent of screen resolution
			final int scale = (int) getResources().getDisplayMetrics().density;
			tv.setPadding(10* scale, 25* scale, 0* scale, 10* scale);
			
			tv.setLayoutParams(params);
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundColor(color);
			tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {

					profilesToEdit.addFirst(profile);

					view.setClickable(false);

					TranslateAnimation animation = new TranslateAnimation(0.0f,
							view.getWidth() + 100, 0.0f, 0.0f);
					animation.setDuration(700);
					animation.setInterpolator(new AccelerateInterpolator());
					animation.setFillAfter(true);

					animation
							.setAnimationListener(new Animation.AnimationListener() {

								@Override
								public void onAnimationStart(Animation arg0) {
								}

								@Override
								public void onAnimationRepeat(Animation arg0) {
								}

								@Override
								public void onAnimationEnd(Animation arg0) {
									amtFinishedAnims++;

									// check if all edit animations finished
									if (profilesToEdit.size() == amtFinishedAnims) {

										for (Profile p : profilesToEdit) {

											Intent myIntent = new Intent(
													activity,
													EditProfileActivity.class);
											int profIndex = settings
													.getProfiles().indexOf(p);
											myIntent.putExtra(
													"PROFILE_TO_EDIT",
													profIndex);
											
											activity.startActivityForResult(
													myIntent,
													REQUEST_CODE_CHANGE_PROFILE);

										}

									}

								}

							});
					view.startAnimation(animation);

				}
			});

			list_Profiles.addView(tv);

		}

		// TextView tv_addProfile = (TextView)findViewById(R.id.addNewProfile);

		// list_Profiles.addView(tv_addProfile);
		gestSelect.addViewToRow(toggleGesture);
		gestSelect.addChildrenToRows(list_Profiles);

	}
	

	public void btn_addNewProfile(View view) {

		// Add new profile item
		final Profile newProfile = new Profile();
		SoundLevelPreference pref1 = new SoundLevelPreference(50, this);
		BrightnessPreference pref2 = new BrightnessPreference(50, this);
		VibrationPreference pref3 = new VibrationPreference(50, this);
		newProfile.addPref(pref1);
		newProfile.addPref(pref2);
		newProfile.addPref(pref3);

		MainActivity.settings.addProfile(newProfile);
		Intent myIntent = new Intent(activity, EditProfileActivity.class);
		int profIndex = settings.getProfiles().indexOf(newProfile);
		myIntent.putExtra("PROFILE_TO_EDIT", profIndex);
		activity.startActivityForResult(myIntent, REQUEST_CODE_CHANGE_PROFILE);
	}

	public void btn_gesture_toggle(View view) {
		CheckBox cb = (CheckBox) view;

		settings.setGestureToggle(cb.isChecked());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		amtFinishedAnims = 0;
		profilesToEdit = new LinkedList<Profile>();
		listAllProfiles();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public void onGesture(int gesture) {
		gestSelect.onGesture(gesture);

	};
}
