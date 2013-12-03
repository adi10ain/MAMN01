package com.LTH.aprofile.GUI;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Preferences.Preference;

public class EditSettings {
	protected Profile profile;
	HashMap<View, TextView> barStatusMap;

	protected LinearLayout statusPanel;
	protected LinearLayout touchPanel;

	protected Activity activity;

	protected final float scale;

	public EditSettings(Activity activity, Profile profile) {

		this.profile = profile;
		this.activity = activity;
		statusPanel = new LinearLayout(activity);
		touchPanel = new LinearLayout(activity);

		scale = activity.getResources().getDisplayMetrics().density;

		barStatusMap = new HashMap<View, TextView>();

		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);

		statusPanel.setLayoutParams(layoutParams);
		statusPanel.setOrientation(LinearLayout.HORIZONTAL);

		layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (200 * scale), 1f);
		touchPanel.setLayoutParams(layoutParams);
		touchPanel.setOrientation(LinearLayout.HORIZONTAL);

		generateBars();
		setTouchListener(touchPanel, activity);

	}

	public LinearLayout getSettingsPanel() {
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout settingsPanel = new LinearLayout(activity);
		settingsPanel.setLayoutParams(layoutParams);
		settingsPanel.setOrientation(LinearLayout.VERTICAL);

		settingsPanel.addView(statusPanel);
		settingsPanel.addView(touchPanel);

		return settingsPanel;

	}

	public LinearLayout getTouchPanel() {
		return touchPanel;
	}

	public LinearLayout getStatusPanel() {
		return statusPanel;
	}

	protected void generateBars() {
		View lastView = new View(activity);
		for (Preference p : profile.getPref().values()) {
			// add status bar
			LinearLayout col = new LinearLayout(activity);
			col.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f);
			col.setLayoutParams(layoutParams);
			col.setId(p.getType());
			int padding = (int) (20 * scale);
			col.setPadding(padding, padding, padding, padding);

			layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			TextView tv_status = new TextView(activity);
			tv_status.setLayoutParams(layoutParams);

			layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			TextView tv_colorBar = new TextView(activity);
			tv_colorBar.setLayoutParams(layoutParams);
			int color = activity.getResources().getColor(
					R.color.METRO_DARK_BROWN);

			tv_colorBar.setBackgroundColor(color);
			tv_colorBar.getBackground().setAlpha(45);

			col.addView(tv_status);
			col.addView(tv_colorBar);

			statusPanel.addView(col);

			// add icon
			ImageView icon = new ImageView(activity);
			p.setDynamicIcon(icon);
			icon.setImageResource(p.getIconResId());
			layoutParams = new LinearLayout.LayoutParams((int) (20 * scale),
					(int) (20 * scale), 1f);
			icon.setLayoutParams(layoutParams);

			icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			
			icon.setAlpha(150);
			padding = (int) (20 * scale);
			
			// icon.setPadding(padding, padding, padding, padding);

			touchPanel.addView(icon);

			barStatusMap.put(col, tv_status);
			lastView = col;

		}

		// show user preferences when the UI have loaded
		lastView.post(new Runnable() {

			@Override
			public void run() {

				for (View col : barStatusMap.keySet()) {
					Preference p = profile.getPref().get(col.getId());
					TextView statusChanger = barStatusMap.get(col);
					float targetValue = p.getPrefValue();
					int height = ((int) (((100 - targetValue) / 100) * col.getHeight()));
					statusChanger.setHeight(col.getHeight());
					ResizeAnimation anim = new ResizeAnimation(statusChanger, col.getHeight(), height);

					statusChanger.startAnimation(anim);
				
					//preferenceChanged(col, p.getPrefValue());
				}

			}

		});

	}

	// used separate method to make main method cleaner, only supposed to be
	// used with touchPanel
	private void setTouchListener(final LinearLayout linearLayout,
			final Activity activity) {
		linearLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent me) {
				for (View col : barStatusMap.keySet()) {

					Rect r = new Rect(col.getLeft(), col.getTop(), col
							.getRight(), col.getBottom());

					// only consider x-axis to determine which button was
					// touched
					if (r.contains((int) me.getX(), 0)) {

						int touchPanelHeight = linearLayout.getHeight();

						float targetValue = (int) me.getY();

						targetValue = 100 - (targetValue / touchPanelHeight) * 100;

						preferenceChanged(col, targetValue);

						break;
					}

				}

				return true;
			}
		});
	}

	// targetValue should be between 0 and 100
	protected void preferenceChanged(View col, float targetValue) {

		TextView statusChanger = barStatusMap.get(col);

		int height = ((int) (((100 - targetValue) / 100) * col.getHeight()));

		statusChanger.setHeight(height);

		int prefId = col.getId();
		Preference pref = profile.getPref().get(prefId);
		pref.set((int) targetValue, activity);
		pref.setPrefValue((int) targetValue);

	}

}
