package com.LTH.aprofile.Classes;

import java.util.ArrayList;
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

	// Initiates the GestureSensor
	public void initGestureSensor() {
		gestureSensor = new GestureSensor(activity, this);

	}

	// Resets the GestureSelector
	public void clear() {
		buttons.clear();
		selectedX = 0;
		selectedY = 0;
	}

	// returns the amount of rows
	public int countRows() {
		return buttons.size();
	}

	// ####################################### Adding/deleting buttons

	// Removes a specified row
	public void removeRow(int row) {
		if (row < buttons.size() - 1)
			buttons.remove(row);
	}

	// adds a view to the end of a specified row
	public void addViewToRow(int row, View view) {
		if (buttons.get(row) != null)
			buttons.get(row).add(view);

	}

	// adds a view to the end of the last row
	public void addViewToRow(View view) {
		ArrayList<View> list = new ArrayList<View>();
		list.add(view);
		buttons.add(list);

	}

	// Adds children of a view to a specified row
	public void addChildrenToRow(int row, ViewGroup view) {
		if (buttons.get(row) != null) {

			for (int i = 0; i < view.getChildCount(); i++) {
				buttons.get(row).add(view.getChildAt(i));
			}
		}
	}

	// Adds children of a view in a row above a specified one
	public void addChildrenBeforeRow(int row, ViewGroup view) {
		if (buttons.get(row) != null) {
			ArrayList<View> list = new ArrayList<View>();
			for (int i = 0; i < view.getChildCount(); i++) {
				list.add(view.getChildAt(i));
			}

			buttons.add(row, list);
		}
	}

	// Adds all children of a view to a row
	public void addChildrenToRow(ViewGroup view) {
		ArrayList<View> list = new ArrayList<View>();

		for (int i = 0; i < view.getChildCount(); i++)
			list.add(view.getChildAt(i));
		buttons.add(list);

	}

	// Adds each child of a view as a separate row
	public void addChildrenToRows(ViewGroup view) {

		for (int i = 0; i < view.getChildCount(); i++)
			addViewToRow(view.getChildAt(i));

	}

	// ####################################### Animation & Gesture handler

	// Called whenever a gesture is detected
	public void onGesture(int gesture) {
		if (!(buttons.isEmpty() || buttons.get(0).isEmpty())) {

			int prevX = selectedX;
			int prevY = selectedY;

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

			// animate only if selection were changed
			if (prevX != selectedX || prevY != selectedY) {
				selectedButton.bringToFront();
				animate(selectedButton, 1f, 1.3f, 1f, 1.3f, 300);
				View prevButton = buttons.get(prevY).get(prevX);
				animate(prevButton, 1.3f, 1f, 1.3f, 1f, 1700);

			}
		}

	}

	// ScaleAnimation of target View
	private Animation animate(View target, float fromX, float toX, float fromY,
			float toY, int duration) {

		ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY);
		animation.setInterpolator(activity, android.R.anim.bounce_interpolator);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		target.startAnimation(animation);
		return animation;

	}

}
