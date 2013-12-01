package com.LTH.aprofile.GUI;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Preferences.Preference;

public class EditSettingsConnected extends EditSettings {

	public EditSettingsConnected(Activity activity, Profile profile) {
		super(activity, profile);
		statusPanel.setPadding((int) (30 * scale), 0, (int) (30 * scale), 0);
		touchPanel.setPadding((int) (30 * scale), 0, (int) (30 * scale), 0);

	}


	@Override
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
					preferenceChanged(col, p.getPrefValue());
				}

			}

		});
	}

}
