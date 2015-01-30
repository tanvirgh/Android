package com.sinepulse.app.fragments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonTask;


/**
 * @author tac
 */

@EFragment(R.layout.device_status)
public class DeviceStatusFragment extends SherlockFragment implements
		OnItemClickListener {
	@ViewById(R.id.step1imagetext)
	protected TextView ivStep1;
	@ViewById(R.id.step2imagetext)
	protected TextView ivStep2;
	@ViewById(R.id.vfBasket)
	public static ViewFlipper flipper;


	public static final int INITIAL_STATE = -1;

	

	public static int backState = INITIAL_STATE;

	private static String emptyBasketText;

	@ViewById(R.id.lvBasket)
	public static ListView basketListView;


	@ViewById(R.id.tvBasketHeadingText)
	protected static TextView basketHeading;
	private TextView tvStep2;

	

	public Button cancelBasket;
	@ViewById(R.id.bCheckoutBasket)
	protected Button checkoutBasketButton;


	public CheckBox cbSmsOnDelivery, cbToAnotherAddress, cbToPay,
			cbTermsAndCondition;


	@ViewById(R.id.basketview)
	public LinearLayout basketLayout;

	public LinearLayout lldiscount;




	InputMethodManager imm;

	/**
	 * 
	 * Tac
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// mSupportActionBar.setDisplayHomeAsUpEnabled(true);

	}

	@AfterViews
	void afterViewsLoaded() {
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);

		RelativeLayout emptyHolder = (RelativeLayout) getActivity()
				.findViewById(R.id.emptyHolder);

		basketListView.setEmptyView(emptyHolder);
		tvStep2 = (TextView) getActivity().findViewById(R.id.step2imagetext);
		emptyBasketText = getString(R.string.empty_basket);

		initializeButtonControl();

		backState = INITIAL_STATE;

	}


	// initialize all button controls
	private void initializeButtonControl() {

	}

	void markStepActive(int stepNumber) {
		switch (stepNumber) {
		case 1:// Running Device

			ivStep1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.stepcompleted));
			ivStep2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.stepincomplete));
			ivStep1.setEnabled(false);
			ivStep2.setEnabled(true);
			setActionBarMenuVisibility(true);
			break;
		case 2:// Inactive Device
			ivStep1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.stepincomplete));
			ivStep2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.stepcompleted));
			ivStep1.setEnabled(true);
			ivStep2.setEnabled(false);
			setActionBarMenuVisibility(false);
			break;

		default:
			break;
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().setTitle("Help");


	}


	

	void loadCheckoutModeFromSettings() {
		if (CommonTask.getCheckoutMode(getActivity()) == true) {
			// online chekcout, so we have to enable 4 steps in top bar
			tvStep2.setText(getResources().getString(R.string.pay));

		} else {
			// inhouse checkout, so we have to enable 2 steps in top bar
			tvStep2.setText(getResources().getString(R.string.pay));
		}
	}

	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
		super.onPause();

		// Set Initial state as active state when this activity is resumed
		backState = INITIAL_STATE;


	}


	

	@Override
	public void onStop() {
		super.onStop();
	}

	
	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen
	 */
	public boolean onBackPressed() {
		// enableDisableCheckoutButton();
		// hideUndoPopupWindow();
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		boolean backHandled = true;
		switch (backState) {
		
		case INITIAL_STATE:
			MainActionbarBase.currentMenuIndex = MainActionbarBase
					.getLastIndex();
			// manageActivity();
			backHandled = false;
			break;
		

		default:
			markStepActive(1);
			backState = INITIAL_STATE;
			break;
		}
		return backHandled;

	}


	@Click({ R.id.step1imagetext, R.id.step2imagetext, R.id.bContinueShopping })
	public void onClick(View v) {
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		switch (v.getId()) {

		case R.id.step1imagetext:// Active Device List
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			backState = INITIAL_STATE;
			markStepActive(1);
			break;

		case R.id.step2imagetext:// InActive Device List
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			// show basketmainscreen
			backState = INITIAL_STATE;
			markStepActive(2);
			break;

		case R.id.bContinueShopping: // Continue application button
			// Redirect to Home Activity
			((MainActionbarBase) getActivity()).displayFragment(0);
			
			break;

		}

	}
public void resetDeviceStatusView(){
		
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask
				.outToRightAnimation());
		// Set Initial state as active state when this activity is resumed
		backState = INITIAL_STATE;
		setActionBarMenuVisibility(true);
		markStepActive(1);
	}



	AnimationListener animListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			getActivity().getWindow().getDecorView().postInvalidate();
		}
	};



	public boolean onDown(MotionEvent arg0) {

		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
		// then dismiss the swipe.
		if (Math.abs(e1.getY() - e2.getY()) > CommonTask.SWIPE_MAX_OFF_PATH())
			return false;
		// Swipe from right to left.
		// The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and
		// a certain velocity (SWIPE_THRESHOLD_VELOCITY).
		if (e1.getX() - e2.getX() > CommonTask.SWIPE_MIN_DISTANCE()
				&& Math.abs(velocityX) > CommonTask.SWIPE_THRESHOLD_VELOCITY()) {
			// do stuff
			Log.d("swipedetected", "right to left");
			switch (backState) {
			case INITIAL_STATE:
//				nextPressedInBasketScreen();
				break;

			default:
				break;
			}
			return true;
		}
		try {
			if (flipper.getDisplayedChild() > 0) {
				boolean isDetectable = true;
				if (lldiscount != null) {
					int[] pos = new int[2];
					lldiscount.getLocationInWindow(pos);
					int ypos = pos != null ? pos[1] - lldiscount.getHeight()
							/ 2 : 0;
					isDetectable = e1.getY() > 40 ? lldiscount.isShown() ? e1
							.getY() > ypos
							&& e1.getY() < ypos + lldiscount.getHeight() ? false
							: true
							: true
							: false;
				}
				// Swipe from left to right.
				// The swipe needs to exceed a certain distance
				// (SWIPE_MIN_DISTANCE) and a certain velocity
				// (SWIPE_THRESHOLD_VELOCITY).
				if (isDetectable
						&& e2.getX() - e1.getX() > CommonTask
								.SWIPE_MIN_DISTANCE()
						&& Math.abs(velocityX) > CommonTask
								.SWIPE_THRESHOLD_VELOCITY()) {
					flipper.setInAnimation(CommonTask.inFromLeftAnimation());
					flipper.setOutAnimation(CommonTask.outToRightAnimation());
					switch (backState) {
					default:
						break;
					}
					flipper.setInAnimation(CommonTask.inFromRightAnimation());
					flipper.setOutAnimation(CommonTask.outToLeftAnimation());
					return true;
				}
			}
		} catch (Exception oEx) {

		}

		return false;
	}


	/**
	 * Hide or show action bar menu items depending on visible parameter
	 * 
	 * @param visibility
	 */
	public void setActionBarMenuVisibility(boolean visibility) {
		if (MainActionbarBase.actionBarMenu != null) {
			int size = MainActionbarBase.actionBarMenu.size();
			for (int i = 0; i < size; i++) {
				MainActionbarBase.actionBarMenu.getItem(i).setVisible(
						visibility);
			}
		}
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
