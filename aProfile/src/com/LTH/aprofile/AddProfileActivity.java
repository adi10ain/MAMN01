package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.HashMap;

import com.LTH.aprofile.Classes.ResizeAnimation;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddProfileActivity extends Activity {
	private TextView status1;
	private TextView status2;
	private TextView status3;
	private TextView status4;
	private TextView status5;

	private View bar1;
	private View bar2;
	private View bar3;
	private View bar4;
	private View bar5;
	HashMap<View,TextView> list;
	

	private int prevY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profile);
		
		bar1 = findViewById(R.id.bar1);
		bar2 = findViewById(R.id.bar2);
		bar3 = findViewById(R.id.bar3);
		bar4 = findViewById(R.id.bar4);
		bar5 = findViewById(R.id.bar5);

		status1 = (TextView) findViewById(R.id.status1);
		status2 = (TextView) findViewById(R.id.status2);
		status3 = (TextView) findViewById(R.id.status3);
		status4 = (TextView) findViewById(R.id.status4);
		status5 = (TextView) findViewById(R.id.status5);

	
		
		list = new HashMap<View, TextView>();
		list.put(bar1, status1);
		list.put(bar2, status2);
		list.put(bar3, status3);
		list.put(bar4, status4);
		list.put(bar5, status5);
		

		
		LinearLayout touchPanel = (LinearLayout) findViewById(R.id.touchPanel);
		
		prevY = 0;
		
		touchPanel.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent me) {
				Log.d("add profile", "button6");
				
				
					test(me);
				

				
				return true;
			}
		});
		
	
		
	}
	
	private void test(MotionEvent me) {
		for (View bar : list.keySet()) {
			
			//Log.d("add profile", "test "+button3.getLeft() +" " +button3.getTop()+" " +button3.getRight()+" " +button3.getBottom());
			Rect r = new Rect(bar.getLeft(), bar.getTop(), bar.getRight(), bar.getBottom());
			Log.d("add profile", "left "+bar.getLeft() +" right "+bar.getRight() +" x "+me.getX());
			if(r.contains((int) me.getX(), 0)){
				TextView button = list.get(bar);
	        	Log.d("add profile", "left "+button.getLeft() +" right "+button.getRight() +" x "+me.getX());
	        	Display display = getWindowManager().getDefaultDisplay();
	        	int add =  (int) (display.getWidth()*0.1);

	        	
	        	if (r.contains(r)) {
	        		int height = (int) ((me.getY()/100)*display.getWidth());
					if (height <= 0)
						height = 10;
					button.setHeight(height);
	        	} else if (Math.abs(prevY - me.getY()) > 5) {
	        		Log.d("add profile", "steg 2 "+button.getText());
					int currentHeight =button.getHeight();
					add = (prevY > me.getY()) ? add : -add;
					int height = (int) currentHeight + add;
					if (height <= 0)
						height = 10;
					button.setHeight(height);
					
					prevY = (int) me.getY();
	        	}
	        	break;
	        }
			
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_profile, menu);
		return true;
	}

}
