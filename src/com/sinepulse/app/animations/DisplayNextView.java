package com.sinepulse.app.animations;


import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

public final class DisplayNextView implements Animation.AnimationListener {
	private boolean mCurrentView;
	LinearLayout first_layout;
	LinearLayout second_layout;
	InputMethodManager m_imm;
	int m_keyboardStatus;

	public DisplayNextView(boolean currentView, LinearLayout layout1,
			LinearLayout layout2, InputMethodManager imm, int keyboardStatus) {
		mCurrentView = currentView;
		this.first_layout = layout1;
		this.second_layout = layout2;
		this.m_imm = imm;
		this.m_keyboardStatus = keyboardStatus;
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		first_layout.post(new SwapViews(mCurrentView, first_layout,
				second_layout));
		if (m_imm != null) {
			m_imm.toggleSoftInput(m_keyboardStatus, m_keyboardStatus);

		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}