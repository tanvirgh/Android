package com.sinepulse.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sinepulse.app.R;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * Application Launcher class 
 * This class activity perform a splash screen with a time interval
 * After showing this class redirect to home screen as application default 
 * @author Tac
 * 
 */
public class SplashScreen extends SherlockFragmentActivity {

	// how long until we go to the next activity
	protected boolean _active = true;
	protected int _splashTime = 1000; // time to display the splash screen in ms

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.splash);
		/*if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();
		}*/
//		 The workaround I chose to implement to resolve this issue is to check for the Intent.
//		CATEGORY_LAUNCHER category and Intent.ACTION_MAIN action in the intent that starts the initial Activity. 
//		If those two flags are present and the Activity is not at the root of the task (meaning the app was already running), 
//		then I call finish() on the initial Activity. That exact solution may not work in all scenario, but something similar should.
//		https://code.google.com/p/android/issues/detail?id=2373
		if (!isTaskRoot()
	            && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
	            && getIntent().getAction() != null
	            && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

	        finish();
	        return;
	    }
		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
//						System.out.println("wait  :"+waited);
						if (_active) {
							waited += 100;
						}
					}
				

				} catch (InterruptedException e) {
					// do nothing
				} finally {
					
					Intent intent = new Intent(
							"com.sinepulse.app.activities.UserLogin");
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//					CommonValues.getInstance().homeIntent=intent;
					startActivity(intent);
//					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
					SplashScreen.this.finish();
					
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}

		return true;
	}
	@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        finish();
    }
	

}