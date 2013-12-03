package com.LTH.aprofile.Classes.Sensors;

import java.util.ArrayList;

import com.LTH.aprofile.MainActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

public class GestureSelector {

	private int selectedX;
	private int selectedY;
	private GestureActivity gestureActivity;

	ArrayList<ArrayList<View>> buttons;

	Animation prevFadeOutAnim;

	private static int SELECT_BUTTON_TIMER = 3000;

	public GestureSelector(GestureActivity gestureActivity) {
		selectedX = 0;
		selectedY = 0;
		this.gestureActivity = gestureActivity;

		buttons = new ArrayList<ArrayList<View>>();
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

	// adds ArrayList<View> to a new row
	public void addRow(ArrayList<View> rowButtons) {
		buttons.add(rowButtons);

	}

	// ####################################### Animation & Gesture handler

	// Called whenever a gesture is detected
	public void onGesture(int gesture) {
		if (!(buttons.isEmpty() || buttons.get(0).isEmpty())) {

			int prevX = selectedX;
			int prevY = selectedY;

			switch (gesture) {
			case GestureActivity.GESTURE_UP:
				if (selectedY > 0) {
					selectedX = findClosestViewInXAxis(selectedY - 1);
					selectedY--;
				}

				break;
			case GestureActivity.GESTURE_RIGHT:
				if (selectedX < buttons.get(selectedY).size() - 1)
					selectedX++;
				break;
			case GestureActivity.GESTURE_DOWN:
				if (selectedY < buttons.size() - 1) {
					selectedX = findClosestViewInXAxis(selectedY + 1);
					selectedY++;
				}
				break;
			case GestureActivity.GESTURE_LEFT:
				if (selectedX > 0)
					selectedX--;
				break;
			case GestureActivity.GESTURE_SHAKE:
				// shake gesture detected -- do something
				break;
			}

			View selectedButton = buttons.get(selectedY).get(selectedX);

			// animate only if selection were changed
			if (prevX != selectedX || prevY != selectedY) {
				animate(selectedButton, 1f, 1.3f, 1f, 1.3f, 1000);
				View prevButton = buttons.get(prevY).get(prevX);
				animate(prevButton, 1.3f, 1f, 1.3f, 1f, 500);

			}
		}

	}

	// ScaleAnimation of target View
	private Animation animate(View target, float fromX, float toX, float fromY,
			float toY, int duration) {

		ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setInterpolator(gestureActivity,
				android.R.anim.bounce_interpolator);
		animation.setDuration(duration);
		animation.setFillAfter(true);

		Animation fadeOut = new AlphaAnimation(1, 0.05f);
		fadeOut.setInterpolator(new DecelerateInterpolator());
		fadeOut.setStartTime(duration);
		fadeOut.setDuration(SELECT_BUTTON_TIMER);
		fadeOut.setFillAfter(false);
		prevFadeOutAnim = fadeOut;

		// when fade out animation finished
		prevFadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				View selectedButton = buttons.get(selectedY).get(selectedX);
				selectedButton.performClick();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		AnimationSet s = new AnimationSet(false);// false mean dont share

		s.addAnimation(animation);
		s.addAnimation(fadeOut);
		s.setFillAfter(false);

		if (MainActivity.settings.getGestureToggle())
			target.startAnimation(s);

		if (fromX > toX) {
			prevFadeOutAnim.cancel();
		}
		// target.startAnimation(animation);
		return animation;

	}

	// Returns the closes view in X axis for the current view selected
	private int findClosestViewInXAxis(int targetRow) {
		View currentView = buttons.get(selectedY).get(selectedX);
		int currentViewXpos = currentView.getLeft();

		ArrayList<View> row = buttons.get(targetRow);

		View v = row.get(0);

		int smallestDiff = Math.abs(currentViewXpos - v.getLeft());
		int xPos = 0;

		for (int i = 0; i < row.size(); i++) {

			v = row.get(i);
			int diff = Math.abs(currentViewXpos - v.getLeft());

			if (diff < smallestDiff) {
				smallestDiff = diff;
				xPos = i;
			}
		}

		return xPos;

	}

}
