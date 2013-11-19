package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class GestureSelector {
	private GestureSensor gestureSensor;
	private int selectedX;
	private int selectedY;
	private Activity activity;

	ArrayList<ArrayList<View>> buttons;

	public GestureSelector(Activity activity) {
		selectedX = 0;
		selectedY = 0;
		this.activity = activity;

		buttons = new ArrayList<ArrayList<View>>();
	}

	public void initGestureSensor() {
		gestureSensor = new GestureSensor(activity, this);

	}
	
	public void clear() {
		buttons.clear();
	}

	public void removeRow(int row) {
		if (row < buttons.size() - 1)
			buttons.remove(row);
	}

	public void addRow(ArrayList<View> list) {
		buttons.add(list);
	}

	public void addViewToRow(int row, View view) {
		if (buttons.get(row) != null)
			buttons.get(row).add(view);

	}

	public void addViewToRow(View view) {
		ArrayList<View> list = new ArrayList<View>();
		list.add(view);
		buttons.add(list);

	}

	public void addChildrenToRow(int row, ViewGroup view) {
		if (buttons.get(row) != null) {

			for (int i = 0; i < view.getChildCount(); i++) {
				buttons.get(row).add(view.getChildAt(i));
			}
		}
	}

	public int countRows() {
		return buttons.size();
	}

	public void addChildrenBeforeRow(int row, ViewGroup view) {
		if (buttons.get(row) != null) {
			ArrayList<View> list = new ArrayList<View>();
			for (int i = 0; i < view.getChildCount(); i++) {
				list.add(view.getChildAt(i));
			}

			buttons.add(row, list);
		}
	}

	public void addChildrenToRow(ViewGroup view) {
		ArrayList<View> list = new ArrayList<View>();

		for (int i = 0; i < view.getChildCount(); i++)
			list.add(view.getChildAt(i));
		buttons.add(list);

	}

	public void addChildrenToRows(ViewGroup view) {

		for (int i = 0; i < view.getChildCount(); i++) {
			addViewToRow(view.getChildAt(i));
		}

	}

	public void onGesture(int gesture) {
		if (!(buttons.isEmpty() || buttons.get(0).isEmpty())) {
			View prevButton = buttons.get(selectedY).get(selectedX);
			animate(prevButton, 1.3f, 1f, 1.3f, 1f, 300);

			switch (gesture) {
			case GestureSensor.GESTURE_UP:
				if (selectedY > 0) {
					selectedX = 0;
					selectedY--;
				}
				break;
			case GestureSensor.GESTURE_RIGHT:
				if (selectedX < buttons.get(selectedY).size() - 1)
					selectedX++;
				break;
			case GestureSensor.GESTURE_DOWN:
				if (selectedY < buttons.size() - 1) {
					selectedX = 0;
					selectedY++;
				}
				break;
			case GestureSensor.GESTURE_LEFT:
				if (selectedX > 0)
					selectedX--;
				break;
			}

			View selectedButton = buttons.get(selectedY).get(selectedX);
			animate(selectedButton, 1f, 1.3f, 1f, 1.3f, 600);
		}

	}

	public void animate(View target, float fromX, float toX, float fromY,
			float toY, int duration) {
		ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, (float) 0.5,
				Animation.RELATIVE_TO_SELF, (float) 0.5);
		animation.setInterpolator(activity, android.R.anim.bounce_interpolator);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		target.startAnimation(animation);
	}

}
