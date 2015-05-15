package com.sinepulse.app.fragments;

import java.util.Stack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.activities.Home;

@EFragment(R.layout.main)
public class HomeFragment extends SherlockFragment {

	private static final int INITIAL_STATE = -1;

	/**
	 * backState use for manage back button
	 */
	public static int backState;

	InputMethodManager imm;

	public static Context context;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	@ViewById(R.id.btn_total_power)
	protected Button btn_total_power;
	@ViewById(R.id.home_btn_room)
	protected Button home_btn_room;
	@ViewById(R.id.home_btn_fan)
	protected Button home_btn_fan;
	@ViewById(R.id.home_btn_bulb)
	protected Button home_btn_bulb;
	@ViewById(R.id.home_btn_curtain)
	protected Button home_btn_curtain;
	// FirstRow
	@ViewById(R.id.firstRowLeft)
	protected TextView firstRowLeft;
	@ViewById(R.id.firstRowRightUp)
	protected TextView firstRowRightUp;
	@ViewById(R.id.firstRowRightDown)
	protected TextView firstRowRightDown;
	// Second Row
	@ViewById(R.id.secondRowLeft)
	protected TextView secondRowLeft;
	@ViewById(R.id.secondRowRightUp)
	protected TextView secondtRowRightUp;
	@ViewById(R.id.secondRowRightDown)
	protected TextView secondRowRightDown;
	//ThirdRow
	@ViewById(R.id.thirdRowLeft)
	protected TextView thirdRowLeft;
	@ViewById(R.id.thirdRowRightUp)
	protected TextView thirdtRowRightUp;
	@ViewById(R.id.thirdRowRightDown)
	protected TextView thirdRowRightDown;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HomeFragment.context = this.getActivity();
		backState = INITIAL_STATE;
		getSherlockActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@AfterViews
	void afterViewsLoaded(){
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	
	void cancelFromSettingsScreen(){
		backToHomeScreen();
	}

	/**
	 * Manage back-button pressed event in anywhere from home screen
	 * backState=-1 is use for application exit and 0 is use for back from
	 * setting screen to home
	 */
	/*public void onBackPressed() {
//		getSherlockActivity().setTitle(R.string.Home);
		if (backState == INITIAL_STATE)
			CommonTask.CloseApplication(getActivity());
		else if (backState == 0) {
			backToHomeScreen();
		} else {
			backState = INITIAL_STATE;
			CommonTask.CloseApplication(getActivity());
		}
	}*/

	private void backToHomeScreen() {
		backState = INITIAL_STATE;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public static boolean showSettingsScreen = false;

	@Override
	public void onResume() {
//		getSherlockActivity().setTitle("DashBoard");
		super.onResume();
		
		if(showSettingsScreen){
			showSettingsScreen = false;
		}

	}

	@Override
	public void onPause(){
		super.onPause();
		
	}
	protected int currentFragment = -1;
	public static final int ALLDEVICE_FRAGMENT = 0, POWERUSAGE_FRAGMENT = 1,
			ROOM_MANAGER_FRAGMENT = 2, SETTINGS_FRAGMENT = 3,
			HELP_FRAGMENT = 4, ABOUT_FRAGMENT = 5, USERLOG_FRAGMENT=6,CAMERA_FRAGMENT = 7;

//	public UserProfileFragment_ userProfileFragment = null;
//	public RoomManagerFragment_ roomManagerFragment = null;
//	public DeviceStatusFragment_ deviceStatusFragment = null;
////	public DeviceControlFragment_ deviceControlFragment = null;
//	public AboutFragment_ aboutFragment = null;
//	public UserLogFragment_ userlogFragment=null;
//	public CameraStreamFragment_ cameraStreamFragment = null;
////	public DeviceByRoomFragment_ deviceByRoomFragment=null;

	
	
	public static Stack<Integer> fragmentBackStack = new Stack<Integer>();

	void addFragmentToBackStack(int id) {
		if (fragmentBackStack.contains(id)) {
			fragmentBackStack.remove(fragmentBackStack.indexOf(id));
		}
		fragmentBackStack.add(id);
	}
	
	public void displayFragment(int position) {
		Fragment fragment = null;
		android.support.v4.app.FragmentTransaction fragtmentTransaction = getActivity().getSupportFragmentManager()
				.beginTransaction();
		if (this.getActivity().getCurrentFocus() != null)
			imm.hideSoftInputFromWindow(
					this.getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		
		/*if (position == currentFragment && currentFragment == ROOM_MANAGER_FRAGMENT) {
			if (roomManagerFragment != null) {
				roomManagerFragment.resetVideoView();
				return;
			}
		}*/
		else if (position == currentFragment) {
			// same itemselected from navigation drawer so lets do nothing
			return;
		}
		
		boolean replace = false;
		if (currentFragment == CAMERA_FRAGMENT) {
			replace = true;
		}


		switch (position) {

		case ALLDEVICE_FRAGMENT:
			currentFragment = ALLDEVICE_FRAGMENT;
//			Intent homeIntent = new Intent(getActivity(), Home.class);
//			startActivity(homeIntent);
			// when home was selected from navigation bar, we want to clear all
			// the current entries in back stack
			if (getFragmentManager().getBackStackEntryCount() > 0)
				getFragmentManager().popBackStack(
						getFragmentManager().getBackStackEntryAt(0)
								.getId(),
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			fragmentBackStack.clear();

			break;
//		case POWERUSAGE_FRAGMENT:
//			currentFragment = POWERUSAGE_FRAGMENT;
//			if (deviceControlFragment == null) {
//				deviceControlFragment = new DeviceControlFragment_();
//			}
//			// I am not sure if this is the best way to reuse an existing
//			// fragment, we need to remove previous fragment from backstack
//			// before adding the same item in screen
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(deviceControlFragment);
//			fragtmentTransaction.commit();
//			fragment = deviceControlFragment;
//
//			break;
		case ROOM_MANAGER_FRAGMENT:
			currentFragment = ROOM_MANAGER_FRAGMENT;
//			if (roomManagerFragment == null) {
//				roomManagerFragment = new RoomManagerFragment_();
//
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(roomManagerFragment);
//			fragtmentTransaction.commit();
//			fragment = roomManagerFragment;

			break;
		case SETTINGS_FRAGMENT:
			currentFragment = SETTINGS_FRAGMENT;

//			if (userProfileFragment == null) {
//				userProfileFragment = new UserProfileFragment_();
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(userProfileFragment);
//			fragtmentTransaction.commit();
//			fragment = userProfileFragment;
			break;
		case HELP_FRAGMENT:
			currentFragment = HELP_FRAGMENT;
//			if (deviceStatusFragment == null) {
//				deviceStatusFragment = new DeviceStatusFragment_();
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(deviceStatusFragment);
//			fragtmentTransaction.commit();
//			fragment = deviceStatusFragment;
			break;
		case ABOUT_FRAGMENT:
			currentFragment = ABOUT_FRAGMENT;
//			if (aboutFragment == null) {
//				aboutFragment = new AboutFragment_();
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(aboutFragment);
//			fragtmentTransaction.commit();
//			fragment = aboutFragment;
			break;
		case USERLOG_FRAGMENT:
			currentFragment = USERLOG_FRAGMENT;
//			if (userlogFragment == null) {
//				userlogFragment = new UserLogFragment_();
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(userlogFragment);
//			fragtmentTransaction.commit();
//			fragment = userlogFragment;
			break;
		case CAMERA_FRAGMENT:
			currentFragment = CAMERA_FRAGMENT;
//			if (cameraStreamFragment == null) {
//				cameraStreamFragment = new CameraStreamFragment_();
//
//			}
//			fragtmentTransaction = getFragmentManager()
//					.beginTransaction();
//			fragtmentTransaction.remove(cameraStreamFragment);
//			fragtmentTransaction.commit();
//			fragment = cameraStreamFragment;

			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			android.support.v4.app.FragmentTransaction ft = fragmentManager
					.beginTransaction();
			if (replace) {
				ft.replace(R.id.frame_container, fragment);
				// tag = fragment.getTag();
			} else {
				ft.add(R.id.frame_container, fragment);
				// tag = fragment.getTag();
			}
			if (currentFragment != ALLDEVICE_FRAGMENT ) {
				// if not home we want to add in back stack entry
				ft.addToBackStack("" + currentFragment);
				addFragmentToBackStack(currentFragment);
			}
			ft.commit();

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
			
	
	
}
