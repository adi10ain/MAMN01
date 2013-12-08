package com.LTH.aprofile.GUI;

import com.LTH.aprofile.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public final class PiechartView extends View {
	protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected int size;
	protected int amountSlices;
	protected int selectionOffset;

	// Make this view size independent of screen resolution
	final float scale;

	// size (DP) in diameter
	public PiechartView(Context context, int amountSlices, int sizeDP) {
		super(context);

		scale = getResources().getDisplayMetrics().density;
		size = (int) (sizeDP * scale);

		this.amountSlices = amountSlices;
		selectionOffset = 270;

		this.setLayoutParams(new LayoutParams(size, size));
		size = size / 2;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setColor(Color.GRAY);

		canvas.drawCircle(size, size, size, paint);

		paint.setColor(Color.WHITE);
		// draw white lines, slice separators
		for (int i = 0; i < amountSlices; i++) {
			float angle = selectionOffset + i * (360 / amountSlices);
			angle = (float) ((Math.PI / 180) * angle);
			canvas.drawLine(size, size, size * (float) Math.cos(angle) + size,
					size * (float) Math.sin(angle) + size, paint);
		}

	}

}
