package com.sinepulse.app.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.sinepulse.app.asynctasks.AsyncLogOutTask;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.devspark.appmsg.AppMsg;
import com.sinepulse.app.R;
import com.sinepulse.app.activities.About_;
import com.sinepulse.app.activities.ChangePasswordActivity_;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.Home_;
import com.sinepulse.app.activities.SupportActivity_;
import com.sinepulse.app.activities.UserLogActivity_;
import com.sinepulse.app.activities.UserProfileActivity_;
import com.sinepulse.app.asynctasks.AsyncLogOutTask;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury used for loading sherlock action bar,set
 *         Application Theme and also describing menu functions,starting
 *         activities,validating server configuration if no valid IP and shop
 *         number is set in settigs screen related error message will dispalyed
 *         and prompt user for providing valid server address.Also starts
 *         various activities when user click on realted menu items.Also go home
 *         method of this class is working for return back to homescreen from
 *         any stage of application when necessary.
 */

public class MainActionbarBase extends SherlockFragmentActivity {
	
	AsyncLogOutTask asyncLogOutTask = null;
	// public Intent settingIntent;
	public static final int INITAIL_STATE = -1;
	public static final int SETTINGS_ACTIVITY = 0, ABOUT_ACTIVITY = 1,
			ROOM_MANAGER_ACTIVITY = 2, USER_PROFILE_ACTIVITY = 3;

	public static final int THEME = R.style.AppTheme;
	public com.actionbarsherlock.app.ActionBar mSupportActionBar;
	public static TextView basketCount;

	public static int currentMenuIndex = INITAIL_STATE;
	public static int previousMenuIndex;

	public static Stack<String> stackIndex;

	private InputMethodManager imm;

	public static Menu actionBarMenu;
	public static Context appContext;
	public ProgressBar abs__search_progress_bar;

	AutoCompleteTextView searchAutoCompleteTextView;

	public Intent settingIntent, roomManagerIntent, aboutIntent,
			userProfileIntent, cameraIntent;

	public static boolean isSearchExpanded = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(THEME);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		if (stackIndex == null)
			stackIndex = new Stack<String>();
		mSupportActionBar = getSupportActionBar();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mSupportActionBar.setTitle(R.string.app_name);
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mConnReceiver, filter);

		// Home Button position reset..moving right
		ImageView homeButton = (ImageView) findViewById(android.R.id.home);

		FrameLayout.LayoutParams distanceFromUpIcon = (FrameLayout.LayoutParams) homeButton
				.getLayoutParams();
		distanceFromUpIcon.leftMargin = 10;
		MainActionbarBase.appContext = getBaseContext();

	}

	public SearchView mSearchView = null;
	public MenuItem searchItem;

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.actionbarmenu, menu);

		// CommonValues.getInstance().menuList = menu;

		this.actionBarMenu = menu;
		return true;
	}

	public static int getLastIndex() {
		if (stackIndex.size() > 1) {
			stackIndex.pop();
			return Integer.parseInt(stackIndex.peek());
		} else if (stackIndex.size() > 0) {
			stackIndex.pop();
		}
		return INITAIL_STATE;
	}

	/***
	 * The user has selected an Menu Item. Go to that Activity
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// HIde the Soft Keypad Globally..TAC
		if (this.getCurrentFocus() != null)
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		return true;
	}

	// defining the state and start of various activities
	/**
	 * Selecting an item from the menu
	 */
	// public void manageActivity() {
	//
	// switch (currentMenuIndex) {
	//
	// case SETTINGS_ACTIVITY:
	//
	// if (settingIntent == null) {
	// settingIntent = new Intent(MainActionbarBase.this,
	// Settings.class);
	// settingIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	// }
	//
	// if (!stackIndex.contains(String.valueOf(0)))
	// stackIndex.push(String.valueOf(0));
	// startActivity(settingIntent);
	// break;
	//
	// default:
	// goHome(MainActionbarBase.this);
	// break;
	// }
	// }

	// method used for return back to homescreen from any activity
	public void goHome(Context activity) {
		if (!(activity instanceof Home)) {
			try {
				displayFragment(ALLDEVICE_FRAGMENT);
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// boolean isinBackground=false;
	@Override
	protected void onPause() {
		// isinBackground=true;
		// CommonValues.getInstance().isStateChanged=false;
		super.onPause();
		// CLose the search view and clear the input fields when
		// leaving...tanvir
		if (mSearchView != null) {
			mSearchView.onActionViewCollapsed();
			mSearchView.setQuery("", false);
			mSearchView.clearFocus();
		}
		unregisterReceiver(mConnReceiver);
		// unregisterReceiver(wifiListener);
	}

	AppMsg msg = null;

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager activityManager = (ActivityManager) getBaseContext()
					.getSystemService(Context.ACTIVITY_SERVICE);

			List<RunningTaskInfo> services = activityManager
					.getRunningTasks(Integer.MAX_VALUE);
			ArrayList<String> runningactivities = new ArrayList<String>();
			for (int i1 = 0; i1 < services.size(); i1++) {
				runningactivities.add(0,
						services.get(i1).topActivity.toString());
			}
			if (activityVisible) {
				if (CommonTask.isNetworkStateChanged(MainActionbarBase.this) == false) {
					msg = AppMsg.makeText(MainActionbarBase.this,
							getString(R.string.networkError),
							AppMsg.STYLE_ALERT_ALWAYS_VISIBLE);
					msg.show();
					// check if it is from login screen or not...if so do
					// nothing.else back to login screen

					if (runningactivities
							.contains("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}") == true) {
						// return true;
						return;
					} else {

						CommonTask
								.ShowNetworkChangeConfirmation(
										MainActionbarBase.this,
										"Network State has been changed.Please log in to continue.",
										showNetworkChangeEvent());

					}

				} else {
					if (msg != null) {
						msg.cancel();
						AppMsg.cancelAll();
					}
				}
			}

		}

	};
	boolean activityVisible = false;

	@Override
	protected void onStart() {
		super.onStart();
		activityVisible = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		activityVisible = false;
	}

	public DialogInterface.OnClickListener showNetworkChangeEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					 CommonValues.getInstance().connectionMode="";
					
					CommonValues.getInstance().summary.deviceSummaryArray.clear();
					CommonValues.getInstance().userId = 0;
					Intent loginintent = new Intent(
							"com.sinepulse.app.activities.UserLogin");
					loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(loginintent);
//					cleanupAndExit(MainActionbarBase.this);

					break;
				// case DialogInterface.BUTTON_NEGATIVE:
				// dialog.cancel();
				//
				// break;

				default:
					break;
				}

			}

		};
		return dialogClickListener;

	}
	
	
	

	
	
	
	
	@Override
	protected void onResume() {
		// start();
		super.onResume();
		/*ConnectivityManager connectivityManager = (ConnectivityManager) MainActionbarBase.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager.getBackgroundDataSetting()==true){
			CommonTask.ShowNetworkChangeConfirmation(MainActionbarBase.this, "New Changed", showNetworkChangeEvent());
			
		}else{
			
			return ;
		}*/

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);

		registerReceiver(mConnReceiver, filter);

		// on resume, if network is not available we again show the alert
		// message
		if (CommonTask.isNetworkAvailable(MainActionbarBase.this) == false) {
			if (msg != null) {
				msg.cancel();
				AppMsg.cancelAll();
			}

			AppMsg.cancelAll();

			msg = AppMsg.makeText(MainActionbarBase.this,
					getString(R.string.networkError),
					AppMsg.STYLE_ALERT_ALWAYS_VISIBLE);
			msg.show();
		} else {
			if (msg != null && msg.isShowing()) {
				msg.cancel();
				AppMsg.cancelAll();
			}
		}
	}

	// override this method in child classes in order to be notified about
	// search button click
	public void searchClicked() {

	}

	protected int currentFragment = -1;
	public static final int ALLDEVICE_FRAGMENT = 0, USERLOG_FRAGMENT = 1,
			SETTINGS_FRAGMENT = 2, CHANGEPASSWORD_FRAGMENT = 3,
			SUPPORT_FRAGMENT = 4, HELP_FRAGMENT = 5, ABOUT_FRAGMENT = 6,
			ROOM_FRAGMENT = 7, CAMERA_FRAGMENT = 8;

	/**
	 * Dislaying fragment view for selected nav drawer list item
	 * */
	public void displayFragment(int position) {

		if (this.getCurrentFocus() != null)
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

		else if (position == currentFragment) {
			// same itemselected from navigation drawer so lets do nothing
			return;
		}
		switch (position) {

		case ALLDEVICE_FRAGMENT:
			currentFragment = ALLDEVICE_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(0)))
				stackIndex.push(String.valueOf(0));
			Intent homeIntent = new Intent(this, Home_.class);
			startActivity(homeIntent);
			// when home was selected from navigation bar, we want to clear all
			// the current entries in back stack
			if (getSupportFragmentManager().getBackStackEntryCount() > 0)
				getSupportFragmentManager().popBackStack(
						getSupportFragmentManager().getBackStackEntryAt(0)
								.getId(),
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			// fragmentBackStack.clear();

			break;
		case USERLOG_FRAGMENT:
			currentFragment = USERLOG_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(1)))
				stackIndex.push(String.valueOf(1));
			// if (userlogFragment == null) {
			// userlogFragment = new UserLogFragment_();
			// }
			// fragtmentTransaction = getSupportFragmentManager()
			// .beginTransaction();
			// fragtmentTransaction.remove(userlogFragment);
			// fragtmentTransaction.commit();
			// fragment = userlogFragment;
			Intent userLogIntent = new Intent(this, UserLogActivity_.class);
			userLogIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(userLogIntent);
			break;
		case SETTINGS_FRAGMENT:
			currentFragment = SETTINGS_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(2)))
				stackIndex.push(String.valueOf(2));

			/*
			 * if (userProfileFragment == null) { userProfileFragment = new
			 * UserProfileFragment_(); } fragtmentTransaction =
			 * getSupportFragmentManager() .beginTransaction();
			 * fragtmentTransaction.remove(userProfileFragment);
			 * fragtmentTransaction.commit(); fragment = userProfileFragment;
			 */
			Intent userProfileIntent = new Intent(this,
					UserProfileActivity_.class);
			startActivity(userProfileIntent);

			break;
		case CHANGEPASSWORD_FRAGMENT:
			currentFragment = CHANGEPASSWORD_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(3)))
				stackIndex.push(String.valueOf(3));
			// if (changePasswordFragment == null) {
			// changePasswordFragment = new ChangePasswordFragment_();
			// }
			// fragtmentTransaction = getSupportFragmentManager()
			// .beginTransaction();
			// fragtmentTransaction.remove(changePasswordFragment);
			// fragtmentTransaction.commit();
			// fragment = changePasswordFragment;
			// break;
			Intent changePassIntent = new Intent(this,
					ChangePasswordActivity_.class);
			startActivity(changePassIntent);
			break;

		case ABOUT_FRAGMENT:
			currentFragment = ABOUT_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
			// if (aboutFragment == null) {
			// aboutFragment = new AboutFragment_();
			//
			// }
			// fragtmentTransaction = getSupportFragmentManager()
			// .beginTransaction();
			// fragtmentTransaction.remove(aboutFragment);
			// fragtmentTransaction.commit();
			// fragment = aboutFragment;
			// break;
			Intent aboutIntent = new Intent(this, About_.class);
			startActivity(aboutIntent);
			break;
		case SUPPORT_FRAGMENT:
			currentFragment = SUPPORT_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(4)))
				stackIndex.push(String.valueOf(4));
			if (CommonValues.getInstance().connectionMode.equals("Internet")) {
				Intent supportIntent = new Intent(this, SupportActivity_.class);
				startActivity(supportIntent);
			} else {
				CommonTask
						.ShowMessage(
								MainActionbarBase.this,
								"You are in Local Mode.Support menu is not accesible in this mode.Please switch to Internet mode to access this Menu.");
				stackIndex.removeAllElements();
				if (!stackIndex.contains(String.valueOf(0)))
					stackIndex.push(String.valueOf(0));
				Intent dashboardIntent = new Intent(this, Home_.class);
				startActivity(dashboardIntent);
			}
			break;

		case HELP_FRAGMENT:
			currentFragment = HELP_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(7)))
				stackIndex.push(String.valueOf(7));
			Intent viewIntent = new Intent(
					"android.intent.action.VIEW",
					Uri.parse("http://dev.sinepulse.com/SmartHome/Web/Dev/en/Page/Help"));
			startActivity(viewIntent);
			break;

		default:
			break;
		}

	}

	public void hideRefreshMenu(com.actionbarsherlock.view.Menu menu) {
		MenuItem refresh = menu.findItem(R.id.menu_refresh);
		refresh.setVisible(false);
		invalidateOptionsMenu();
	}

	public void start() {
		// Register for the intents that tell us about WiFi state changes.
		// Thanks to Kent for the tip.
		IntentFilter wifiFilter = new IntentFilter();
		wifiFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		wifiFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		wifiFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		this.registerReceiver(wifiListener, wifiFilter);

		// Get the initial status, or any status that may have changed
		// while we were paused.
		// getWifiState();
		// getWifiConnection();
	}

	// boolean stateChanged=false;

	private BroadcastReceiver wifiListener = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			ActivityManager activityManager = (ActivityManager) getBaseContext()
					.getSystemService(Context.ACTIVITY_SERVICE);

			List<RunningTaskInfo> services = activityManager
					.getRunningTasks(Integer.MAX_VALUE);
			ArrayList<String> runningactivitis = new ArrayList<String>();

			for (int i1 = 0; i1 < services.size(); i1++) {
				runningactivitis
						.add(0, services.get(i1).topActivity.toString());
			}
			if (runningactivitis
					.contains("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}") == true) {
				// return true;
				return;
			} else {
				// if (!activityVisible)
				if (action.equals(WifiManager.NETWORK_IDS_CHANGED_ACTION)) {

				} else if (action
						.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
					// getWifiConnection();
					CommonValues.getInstance().isStateChanged = true;
				} else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
					// getWifiConnection();
				} else if (action
						.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
					// getScanResults();
				} else if (action
						.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
					// getWifiConnection();

				} else if (action
						.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
					// getWifiConnection();

				} else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
					// getWifiState();

				}
			}

		};
	};

	@Override
	protected void onRestart() {

		// stateChanged==true &&
		// CommonValues.getInstance().isStateChanged=false;
		// if (CommonValues.getInstance().isStateChanged == true) {
		//
		// ActivityManager activityManager = (ActivityManager) getBaseContext()
		// .getSystemService(Context.ACTIVITY_SERVICE);
		//
		// List<RunningTaskInfo> services = activityManager
		// .getRunningTasks(Integer.MAX_VALUE);
		// ArrayList<String> runningactivities = new ArrayList<String>();
		// for (int i1 = 0; i1 < services.size(); i1++) {
		// runningactivities.add(0,
		// services.get(i1).topActivity.toString());
		// }
		//
		// if (runningactivities
		// .contains("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}")
		// == true) {
		// // return true;
		// return;
		// } else {
		//
		// CommonTask
		// .ShowNetworkChangeConfirmation(
		// MainActionbarBase.this,
		// "Network State has been changed.Please log in to continue.",
		// showNetworkChangeEvent());
		// }
		//
		// }
		super.onRestart();
	}
}
