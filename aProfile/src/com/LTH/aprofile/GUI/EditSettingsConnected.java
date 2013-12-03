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
import com.LTH.aprofile.Classes.Preferences.Preference;

public class EditSettingsConnected extends EditSettings {

	public EditSettingsConnected(Activity activity, Profile profile) {
		super(activity, profile);
		statusPanel.setPadding((int) (30 * scale), 0, (int) (30 * scale), 0);
		touchPanel.setPadding((int) (30 * scale), 0, (int) (30 * scale), 0);

	}

}
