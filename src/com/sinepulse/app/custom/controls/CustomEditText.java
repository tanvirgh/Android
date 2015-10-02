package com.sinepulse.app.custom.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CustomEditText extends EditText {
	private OnKeyListener keyPreListener;
	public CustomEditText(Context context) {
		super(context);
		initialize(context);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		if(!isInEditMode())
		{
//		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
//				"fonts/Roboto-Regular.ttf"));
		}

	}
	
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if(keyPreListener!=null){
			keyPreListener.onKey(null, keyCode, event);
		}
		return super.onKeyPreIme(keyCode, event);
	}
	
	public void setOnKeyPreListener(OnKeyListener listener){
		keyPreListener = listener;
	}
}