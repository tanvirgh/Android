package com.sinepulse.app.animations;

import android.view.View;
import android.widget.LinearLayout;

public final class SwapViews implements Runnable {
	private boolean mIsFirstView;
	LinearLayout layout1;
	LinearLayout layout2;

	public SwapViews(boolean isFirstView, LinearLayout layout1,
			LinearLayout layout2) {
		mIsFirstView = isFirstView;
		this.layout1 = layout1;
		this.layout2 = layout2;
	}

	@Override
	public void run() {
		// final float centerX = layout1.getWidth() / 2.0f;
		// final float centerY = layout1.getHeight() / 2.0f;
		// Flip3dAnimation rotation;

		if (mIsFirstView) {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			layout2.requestFocus();

		} else {
			layout2.setVisibility(View.GONE);
			layout1.setVisibility(View.VISIBLE);
			layout1.requestFocus();

		}

	}
}