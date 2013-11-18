package com.LTH.aprofile;

import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetBrightness extends Activity {

	// GridView gridView;
	LinearLayout linearLayout;

	private int displayHeight;
	private int displayWidth;

	private int prevY;
	private int currentHeight;
	
	ResizeAnimation scale;

	Context con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

		con = this;

		prevY = 0;
		setContentView(R.layout.activity_set_brightness);

		linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);

		Display display = getWindowManager().getDefaultDisplay();
		displayHeight = display.getHeight();
		displayWidth = display.getWidth();
		currentHeight = 0;
		((TextView) (linearLayout.getChildAt(0))).setHeight(50);
		scale = new ResizeAnimation((linearLayout.getChildAt(0)),0f,currentHeight,0f, 0);
		scale.setInterpolator(new DecelerateInterpolator (10f));
		(linearLayout.getChildAt(0)).startAnimation(scale); 
		

		linearLayout.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent me) {

				int action = me.getActionMasked();
				float currentYPosition = me.getY();
				float currentXPosition = me.getX();
				

				try {

					if (action == MotionEvent.ACTION_MOVE) {

						int add = (int) ((currentXPosition/displayWidth*1.0)*displayHeight*0.5);
						if (Math.abs(prevY - currentYPosition) > 5) {
							
							currentHeight =(linearLayout.getChildAt(0)).getHeight();
							add = (prevY > currentYPosition) ? -add : add;
							int height = (int) currentHeight + add;
							
							
							//((TextView) (linearLayout.getChildAt(0)))
									//.setHeight(height);
							
							if(scale.hasEnded()) {
							scale = new ResizeAnimation((linearLayout.getChildAt(0)),0f,currentHeight,0f, height);
							scale.setInterpolator(con, android.R.anim.linear_interpolator);
							scale.setFillAfter(true);
							scale.setDuration((int)((Math.abs(height-currentHeight))*1.3  ));
							prevY = (int) currentYPosition;
							
							(linearLayout.getChildAt(0)).clearAnimation();
							(linearLayout.getChildAt(0)).startAnimation(scale); 
							
							
							
							}
							currentHeight = height;
						}

						WindowManager.LayoutParams lp = getWindow()
								.getAttributes();

						if (currentHeight > displayHeight)
							currentHeight = displayHeight;
						else if (currentHeight < 0)
							currentHeight = 0;
						
						lp.screenBrightness = (float) ((displayHeight - currentHeight*0.99) / displayHeight);

						getWindow().setAttributes(lp);

						// return to calling activity on touch up
					} else if (me.getAction() == android.view.MotionEvent.ACTION_UP) {
						finish();
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}

				} catch (Exception e) {
				}

				return true;
			}
		});

	}
}
