package com.LTH.aprofile.Preferences;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public abstract class Preference {
	protected String name;
	protected int iconResId;
	protected int prefValue;
	protected int colorCode;

	public static final int ALPHA_BTN = 80; // Opacity for toggled off button

	public static final int ALPHA_MAX = 255;// (0-255)

	protected int type;

	protected Activity callingActivity;

	// prefValue should be between 0 and 100
	public Preference(int prefValue, Activity callingActivity) {
		this.prefValue = prefValue;
		this.callingActivity = callingActivity;
		colorCode = Color.GRAY;
	}

	public abstract void load();

	public void editPrefValue(int prefValue) {
		this.prefValue = prefValue;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getPrefValue() {
		return prefValue;
	}

	public void setPrefValue(int prefValue) {
		this.prefValue = prefValue;
	}

	public int getIconResId() {
		return iconResId;
	}

	public int getColorCode() {
		return colorCode;
	}

	/*returns a string of preference ID and prefered value ex. ";1:50"
	 * Used when sending and receiving profiles
	 */
	@Override
	public String toString() {
		String ret = ";"+type + ":" + prefValue;
		return ret;
	}

	public ImageView getIconButton(Boolean toggle) {
		ImageView imageView = new ImageView(callingActivity);
		imageView.setImageResource(getIconResId());

		int alpha = ALPHA_BTN;
		Boolean pressed = false;
		if (toggle) {
			alpha = ALPHA_MAX;
			pressed = true;
		}
		imageView.setSelected(pressed);
		imageView.setAlpha(alpha);
		imageView.setId(getType());

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ImageView iv = (ImageView) view;
				view.setSelected(!view.isSelected());
				int alpha = (view.isSelected()) ? ALPHA_MAX : ALPHA_BTN;
				iv.setAlpha(alpha);

			}
		});

		// imageView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// int action = event.getAction();
		// int x = (int) event.getX();
		// int y = (int) event.getY();
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// Intent myIntent = new Intent(callingActivity,
		// SetBrightness.class);
		//
		// callingActivity.startActivityForResult(myIntent,
		// MainActivity.REQUEST_CODE_NEW_PROFILE);
		// break;
		//
		// }
		//
		// return true;
		// }
		// });

		return imageView;

	}

	// targetValue should be between 0 and 100
	public abstract void set(int targetValue, Activity activity);
}
