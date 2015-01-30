package com.sinepulse.app.animations;

import android.view.View;
import android.view.animation.Animation;

public final class AlphaAnimationListener implements
		Animation.AnimationListener {

	View alphaView;
	boolean visibility;

	public AlphaAnimationListener(View v, boolean visible) {
		alphaView = v;
		visibility = visible;
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		alphaView.setVisibility(visibility ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}