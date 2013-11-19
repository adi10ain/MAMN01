package com.LTH.aprofile;

import java.util.ArrayList;

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

	private TextView button1;
	private TextView button2;
	private TextView button3;
	private TextView button4;
	private TextView button5;
	ArrayList<TextView> list;
	
	private int prevY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profile);

		status1 = (TextView) findViewById(R.id.status1);
		status2 = (TextView) findViewById(R.id.status2);
		status3 = (TextView) findViewById(R.id.status3);
		status4 = (TextView) findViewById(R.id.status4);
		status5 = (TextView) findViewById(R.id.status5);

		button1 = (TextView) findViewById(R.id.button1);
		button2 = (TextView) findViewById(R.id.button2);
		button3 = (TextView) findViewById(R.id.button3);
		button4 = (TextView) findViewById(R.id.button4);
		button5 = (TextView) findViewById(R.id.button5);
		
		list = new ArrayList<TextView>();
		list.add(status1);
		list.add(status2);
		list.add(status3);
		list.add(status4);
		list.add(status5);
		
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
		for (TextView button : list) {
			
			//Log.d("add profile", "test "+button3.getLeft() +" " +button3.getTop()+" " +button3.getRight()+" " +button3.getBottom());
			Rect r = new Rect(button.getLeft(), button.getTop(), button.getRight(), button.getBottom());
			if(r.contains((int) me.getX(), 0)){
	        	Log.d("add profile", "träff"+button.getText());
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
