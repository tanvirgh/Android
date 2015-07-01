package com.sinepulse.app.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.sinepulse.app.activities.UserLogin;
import com.sinepulse.app.activities.UserProfileActivity_;
import com.sinepulse.app.asynctasks.AsyncLogOutTask;
import com.sinepulse.app.asynctasks.CheckMC;
import com.sinepulse.app.utils.CommonConstraints;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

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

	protected static String connnectionState = "NONE";
	CheckMC checkMC = null;
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
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mConnReceiver, filter);
		// registerReceiver(conChangeReceiver, filter);

		// Home Button position reset..moving right
		ImageView homeButton = (ImageView) findViewById(android.R.id.home);

		FrameLayout.LayoutParams distanceFromUpIcon = (FrameLayout.LayoutParams) homeButton
				.getLayoutParams();
		distanceFromUpIcon.leftMargin = 10;

	}

	public SearchView mSearchView = null;
	public MenuItem searchItem;

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.actionbarmenu, menu);

		// CommonValues.getInstance().menuList = (android.view.Menu) menu;

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
		Editor editor = getSharedPreferences("clear_cache",
				Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
		try {

			trimCache(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean isappinBackground = false;

	@Override
	protected void onPause() {
		super.onPause();
		isappinBackground = true;
		if (mConnReceiver != null) {
			unregisterReceiver(mConnReceiver);
		}
		// unregisterReceiver(conChangeReceiver);
	}

	AppMsg msg = null;

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (activityVisible) {
				if (CommonTask.isNetworkStateChanged(MainActionbarBase.this) == false) {
					msg = AppMsg.makeText(MainActionbarBase.this,
							getString(R.string.networkError),
							AppMsg.STYLE_ALERT_ALWAYS_VISIBLE);
					msg.show();
					ArrayList<String> runningactivities = checkActivityVisibility();
					if (runningactivities
							.contains("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}") == true) {
						// return true;
						return;
					} else {

						CommonTask
								.ShowNetworkChangeConfirmation(
										MainActionbarBase.this,
										"Network State has been changed.Please log in again to continue.",
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

	AsyncLogOutTask asyncLogOutTask = null;

	public DialogInterface.OnClickListener showNetworkChangeEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:

//					clearAppData();
					ArrayList<String> runningactivities = checkActivityVisibility();
					  if (runningactivities .contains(
					  "ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}"
					  ) == true) {
						  return; 
						  } 
					  else {
						  Intent loginintent = new Intent(
									"com.sinepulse.app.activities.UserLogin");
							loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(loginintent);
					  }
					

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

	boolean isWifiChanged = false;

	@Override
	protected void onResume() {
		super.onResume();
		// ConnectivityManager connectivityManager = (ConnectivityManager) this
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (isappinBackground == true) {
			
			  /*ArrayList<String> runningactivities = checkActivityVisibility();
			  if (runningactivities .contains(
			  "ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.UserLogin_}"
			  ) == true) {
				  return; 
				  } 
			  else {
//			  connectByHostNameLocal();
			  connectByIp();
			  }*/
			 
			connectByIp();
//			  connectByHostNameLocal();

		}

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);

		registerReceiver(mConnReceiver, filter);
		// registerReceiver(conChangeReceiver, filter);
		/*
		 * if(connectivityManager.getBackgroundDataSetting()==true){
		 * CommonTask.ShowNetworkChangeConfirmation(MainActionbarBase.this,
		 * "Changed", showNetworkChangeEvent());
		 * }else{
		 * return ; }
		 */

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

	/**
	 * @return
	 */
	public ArrayList<String> checkActivityVisibility() {
		ArrayList<String> runningactivities = new ArrayList<String>();

		ActivityManager activityManager = (ActivityManager) getBaseContext()
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> services = activityManager
				.getRunningTasks(Integer.MAX_VALUE);

		for (int i1 = 0; i1 < services.size(); i1++) {
			runningactivities.add(0, services.get(i1).topActivity.toString());
		}
		return runningactivities;
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
			Intent userProfileIntent = new Intent(this,
					UserProfileActivity_.class);
			startActivity(userProfileIntent);

			break;
		case CHANGEPASSWORD_FRAGMENT:
			currentFragment = CHANGEPASSWORD_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(3)))
				stackIndex.push(String.valueOf(3));
			Intent changePassIntent = new Intent(this,
					ChangePasswordActivity_.class);
			startActivity(changePassIntent);
			break;

		case ABOUT_FRAGMENT:
			currentFragment = ABOUT_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
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
								"Support is not accesible in Local Mode.Please turn on Data connection and switch to Internet mode to access this Menu.");
				if (stackIndex != null) {
					stackIndex.removeAllElements();
				}
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
	public void setConnectionNodeImage(com.actionbarsherlock.view.Menu menu){
		MenuItem connImage=menu.findItem(R.id.menu_conn_indicatior);
		if(CommonValues.getInstance().connectionMode=="Local"){
			connImage.setIcon(getResources().getDrawable(R.drawable.local));
		}else{
			connImage.setIcon(getResources().getDrawable(R.drawable.internet));
		}
		
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	String urlForMc = "";
	boolean isSolvedLocal = false;

	/**
	 * try to resolve the connectivity by Appropriate host name.local
	 */

	public void connectByHostNameLocal() {
		// CommonValues.getInstance().hostNameSuffix
		connnectionState = "Local";
		urlForMc = "http://sinepulsemcdev" + ".sinepulse.bd.local"
				+ "/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}

		checkMC = new CheckMC(urlForMc, MainActionbarBase.this, isSolvedLocal);
		checkMC.execute();

	}

	/**
	 * try to resolve the connectivity by Appropriate host name
	 */
	public void connectByHostName() {

		connnectionState = "RasPeri";
		urlForMc = "http://sinepulsemcdev/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}

		checkMC = new CheckMC(urlForMc, MainActionbarBase.this, isSolvedLocal);
		checkMC.execute();

	}

	/**
	 * Failed to resolve with host name..Lets try with replacing WIFI IP last
	 * sub net mask and attempt to connect.
	 */

	public void connectByIp() {
		WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		int ip = wifiInfo.getIpAddress();
		@SuppressWarnings("deprecation")
		String ipAddress = Formatter.formatIpAddress(ip);
		String[] tokens = ipAddress.split("\\.");
		tokens[3] = "111";
		ipAddress = tokens[0] + "." + tokens[1] + "." + tokens[2] + "."
				+ tokens[3];
		// Log.d("WIFI Ip", ipAddress);
		urlForMc = "http://" + ipAddress + "/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}
		connnectionState = "IP";
		checkMC = new CheckMC(urlForMc, MainActionbarBase.this, isSolvedLocal);
		// checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		checkMC.execute();
	}

	public boolean sendMCStatusRequest(String mcUrl) {

		if (getMcStatus(mcUrl) != null && getMcStatus(mcUrl) != "") {
			return true;
		} else {
			// fireNetworkChangeEvent();
			return false;
		}

	}

	boolean ismcOff = false;

	public String getMcStatus(String url) {

		InputStream is = null;
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			// HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			if (is != null) {
				result = JsonParser.convertInputStreamToString(is);
			} else {
				result = "Did not work!";
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			ismcOff = true;
			e.printStackTrace();
		}

		if (result != null && !result.equals("")) {

			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				// String
				// host=(jObject.getJSONObject("Data")).getString("hostname");
				String Ip = (jObject.getJSONObject("Data")).getString("ip");
				if (Ip != null && Ip != "") {
					CommonValues.getInstance().localIp = Ip;
				}
				String baseUrlForMC = "http://" + Ip + "/api/";
				CommonURL.getInstance().assignValues(baseUrlForMC);
				isSolvedLocal = true;
				isWifiChanged = true;
				if (CommonValues.getInstance().connectionMode == "Internet") {
					fireNetworkChangeEvent();
				}
				CommonValues.getInstance().connectionMode = "Local";
				if (CommonValues.getInstance().localIp != null
						&& CommonValues.getInstance().localIp != "") {
					saveLocalIpInPreference();
				}

			}

			catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		} else {

			if (connnectionState.equals("Local")) {
				// ..Lets try with host name
/*				if (checkMC != null) {
					checkMC.cancel(true);
				}
*/				
				isSolvedLocal = false;
				urlForMc = "";
				connectByHostName();

			} else if (connnectionState.equals("RasPeri")) {
				// Failed to resolve with host name..Lets try with replacing
				// WIFI IP last sub net...Step3

				isSolvedLocal = false;
				urlForMc = "";
				connectByIp();

			} else if (connnectionState.equals("IP")) {
				if (isWifiChanged == false
						&& CommonValues.getInstance().connectionMode == "Local") {
					fireNetworkChangeEvent();

				} else {
//					setInternetUrl();
				}
			}
			/*if (ismcOff == true) {
				setInternetUrl();
			}*/
			setInternetUrl();
		}

		// 11. return result
		return result;
	}

	/**
	 * 
	 */
	public void setInternetUrl() {
		isSolvedLocal = false;
		CommonURL.getInstance().assignValues(
				CommonURL.getInstance().remoteBaseUrl);
		CommonValues.getInstance().connectionMode = "Internet";
	}

	/**
		 * 
		 */
	public void fireNetworkChangeEvent() {
		MainActionbarBase.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				CommonTask
						.ShowNetworkChangeConfirmation(
								MainActionbarBase.this,
								"Network State has been changed.Please log in again to continue.",
								showNetworkChangeEvent());

			}
		});
	}

	public void saveLocalIpInPreference() {
		CommonTask.SavePreferences(this, CommonConstraints.PREF_URL_KEY,
				CommonConstraints.PREF_LOCALIP_KEY,
				CommonValues.getInstance().localIp);
	}

	public void clearAppData() {
		CommonValues.getInstance().userId = 0;
		CommonValues.getInstance().ApiKey = "";
		Editor editor = getSharedPreferences("clear_cache",
				Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
		trimCache(MainActionbarBase.this);
	}

	/**
		 * 
		 */
	public void removePreferenceLoginData() {
		SharedPreferences password = UserLogin.context.getSharedPreferences(
				CommonConstraints.PREF_PASSWORD_KEY,
				UserLogin.context.MODE_PRIVATE);
		password.edit().remove(CommonConstraints.PREF_PASSWORD_KEY).commit();
		SharedPreferences userName = UserLogin.context.getSharedPreferences(
				CommonConstraints.PREF_LOGINUSER_NAME,
				UserLogin.context.MODE_PRIVATE);
		userName.edit().remove(CommonConstraints.PREF_LOGINUSER_NAME).commit();
	}

}
