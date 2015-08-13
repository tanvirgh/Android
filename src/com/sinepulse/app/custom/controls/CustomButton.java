package com.sinepulse.app.custom.controls;

import com.sinepulse.app.utils.ButtonTooltipHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButton extends SafeButton{
	public CustomButton(Context context) {
		super(context);
		initialize(context);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		if(!isInEditMode())
		{
//		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
//				"fonts/Roboto-Regular.ttf"));
		ButtonTooltipHelper.setup(this);
		}
	}
}