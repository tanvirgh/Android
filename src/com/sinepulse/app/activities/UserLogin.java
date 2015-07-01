package com.sinepulse.app.activities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncCheckServerStateAndSaveServerInfo;
import com.sinepulse.app.asynctasks.CheckMC;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonConstraints;
import com.sinepulse.app.utils.CommonGcmValues;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * 
 * @author tavnir
 * 
 */

@EActivity(R.layout.user_login)
public class UserLogin extends MainActionbarBase implements OnClickListener {

	public ActionBar mSupportActionBar;
	android.net.wifi.WifiManager.MulticastLock lock;

	@ViewById(R.id.bUserLogin)
	Button bUserLogin;
	InputMethodManager imm;
	@ViewById(R.id.etUserName)
	public EditText etUserName;
	@ViewById(R.id.etUserPassword)
	public EditText etUserPassword;
	@ViewById(R.id.pbFirstPage)
	public ProgressBar pbFirstPage;
	GoogleCloudMessaging gcm;
	String regid;
	CheckMC checkMC = null;
	protected static String connnectionState = "NONE";

	public static Context context;

	private static final int INITIAL_STATE = -1;

	/**
	 * backState use for manage back button
	 */
	public static int backState;

	AsyncCheckServerStateAndSaveServerInfo asyncSaveLogInInfo = null;

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "Tanvir";
	String urlForMc = "";

	/**
	 * Called when the activity is started.
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		UserLogin.context = this;
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.hide();
//		getHostnameSuffix();
	}

	@AfterViews
	void afterViewsLoaded() {
		// bUserLogin.setEnabled(false);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		etUserName.requestFocus();
		CommonTask.showSoftKeybord(etUserName);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		try {
			GCMRegistrar.checkDevice(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			GCMRegistrar.checkManifest(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (checkPlayServices()) {
			// Log.d(TAG, "Play Service Found!");
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(this);

			if (regid.isEmpty()) {
				registerInBackground();
				// Log.d(TAG, regid);
			} else {
//				CommonTask.ShowMessage(this, "GCM Registration ID."+regid);
				// Log.d(TAG, regid);
			}

		} else {
			CommonTask.ShowMessage(this, "No valid Google Play Services APK found.");
		}
		// if (isMyServiceRunning()) {
		// Log.d(TAG, "Service running");
		// } else {
		// Log.d(TAG, "Service Not running");
		// }

	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		// return true;
		return super.onOptionsItemSelected(item);
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

	/**
	 * use for manage all button click events centrally
	 */

	@Override
	@Click(R.id.bUserLogin)
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bUserLogin:
			proceedLoginProcess();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 */
	public void proceedLoginProcess() {
		if (validateLoginInfo()) {

			etUserName.clearFocus();
			etUserPassword.clearFocus();
			backState = INITIAL_STATE;
			imm.hideSoftInputFromWindow(bUserLogin.getWindowToken(), 0);
			if (CommonTask.isEmpty(regid)) {
				registerInBackground();
			} else {
				String GcmRegId = getRegistrationId(getApplicationContext());
				saveUserInfo(GcmRegId);
				// System.out.println(GcmRegId);
			}
		}
	}

	/**
	 * Save UserName and Password through async call
	 */
	public void saveUserInfo(String GcmRegistrationId) {
		if (asyncSaveLogInInfo != null) {
			asyncSaveLogInInfo.cancel(true);
		}
		asyncSaveLogInInfo = new AsyncCheckServerStateAndSaveServerInfo(this,
				GcmRegistrationId);
		asyncSaveLogInInfo.execute();
	}

	/**
	 * Use for loading the username,password from SharedPreferences which is
	 * already saved before from login screen input
	 */
	public void loadPreferences() {
		etUserName.setText(CommonTask.getBaseUrl(UserLogin.context));
		etUserPassword.setText(CommonTask.getPassword(UserLogin.context));
	}

	public void startmenuProgress() {
		pbFirstPage.setVisibility(View.VISIBLE);

	}

	public void stopmenuProgress() {
		pbFirstPage.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		// if (selectAppMode() == true) {
		//
		// } else {
		// CommonTask.ShowMessage(this, "Failed to resolve service mode.");
		// }
		selectAppMode();
		etUserPassword.clearFocus();
		super.onResume();
	}

	/**
	 * 
	 */
	public void selectAppMode() {
		if (CommonTask.isNetworkAvailable(this)) {
			resolveNetworkState();
		/*	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			// return true;
		} else {
			// CommonTask.ShowMessage(this,
			// "Please turn on WiFi or Data Connection");
			// return false;
		}

	}

	/**
	 * 
	 */
	public void resolveNetworkState() {
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (status.equals("Wifi enabled")) {
			if (CommonTask.isEmpty(CommonTask.getBaseUrl(this))) {
				// If SharedPreference is empty try resolving with host
				// name.local...Step1
				connectByHostNameLocal();

			} else {
				// Already have an IP in Preference...Lets try to connect with
				// that...Step2
				urlForMc = "http://" + CommonTask.getBaseUrl(this)
						+ "/api/is-online";
				connnectionState = "IP";
				if (checkMC != null) {
					checkMC.cancel(true);
				}
				
				checkMC = new CheckMC(urlForMc, (UserLogin_) this, false);
				 checkMC.execute();
//				checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			}
//			 connectByHostNameLocal();
			/* try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} else if (status.equals("Mobiledata enabled")) {
			CommonURL.getInstance().assignValues(
					CommonURL.getInstance().remoteBaseUrl);
			CommonValues.getInstance().connectionMode = "Internet";
			// Log.d("NInfo", "GSM");
		} else {
			CommonValues.getInstance().IsServerConnectionError = true;

		}
	}

	

	@Override
	public void onBackPressed() {
		CommonTask.CloseApplication(this);
	}

	/**
	 * save Login screen info ie->User name and Password in shared preferences
	 */

	public void saveUsernameAndPassword() {
		CommonTask.SavePreferences(this, CommonConstraints.PREF_SETTINGS_NAME,
				CommonConstraints.PREF_LOGINUSER_NAME, etUserName.getText()
						.toString());

		CommonTask.SavePreferences(this, CommonConstraints.PREF_SETTINGS_NAME,
				CommonConstraints.PREF_PASSWORD_KEY, etUserPassword.getText()
						.toString());
	}


	/**
	 * Method that check whether the application is connected to server or not
	 */

	public boolean isConnectedToServer(String AppToken,Context context) {
		return CommonTask.isValidLogIn(context,etUserName.getText().toString(),
				etUserPassword.getText().toString(), AppToken);
	}

	private boolean validateLoginInfo() {
		if (etUserName.getText().toString().trim().equals("")) {
			etUserName.setError("Please Provide User Name");
			return false;
		} else if (CommonTask.checkEmail(etUserName.getText().toString()) == false) {
			etUserName.setError("Invalid Email address.");
			return false;
		} else if (etUserPassword.getText().toString().trim().equals("")) {
			etUserPassword.setError("Please Provide Password");
			return false;
		} else {
			etUserName.setError(null);
			etUserPassword.setError(null);
			return true;
		}
	}

	/**
	 * After save user information application will redirect to home activity
	 * 
	 */
	public void setUserInfoAfterSave() {
		// CommonTask.loadSettings(this);
		// CommonValues.getInstance().loginuser = new UserInformation();
		
		Intent homeIntent = new Intent(this, Home_.class);
		startActivity(homeIntent);
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		// though this is not recommended to use this method but here it has
		// been used for a custom requirement..Tanvir
		finish();

	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.sinepulse.app.activities.GCMIntentService"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public String getRegistrationId(Context context) {

		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(
				CommonGcmValues.PROPERTY_REG_ID, "");

		try {
			if (registrationId != null && registrationId != "")
				CommonValues.getInstance().appToken = registrationId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(
				CommonGcmValues.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(UserLogin.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(CommonGcmValues.SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

					// If there is an error, don't just keep trying to register.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(Object result) {
				// mDisplay.append(String.valueOf(result)+"\n");
				Log.i(TAG, String.valueOf(result));
			}

		}.execute(null, null, null);
	}

	public void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(CommonGcmValues.PROPERTY_REG_ID, regId);
		editor.putInt(CommonGcmValues.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						CommonGcmValues.PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				// Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        
    }
	
	/* @Override
	 protected void onStop() {
//		 super.onStop();
		
	 }
	 @Override
	 protected void onPause() {
//		 super.onPause();
//		 lock.release();
	 }*/
	
	 String requiredHostNameSuffix="";
	  private void getHostnameSuffix() {
			 
	        new Thread(new Runnable() {
	 
	            @Override
	            public void run() {
	                android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
	                /*Allows an application to receive 
	                Wifi Multicast packets. Normally the Wifi stack 
	                filters out packets not explicitly addressed to 
	                this device. Acquiring a MulticastLock will cause 
	                the stack to receive packets addressed to multicast
	                addresses. Processing these extra packets can 
	                cause a noticable battery drain and should be 
	                disabled when not needed. */
	                lock = wifi.createMulticastLock(getClass().getSimpleName());
	                /*Controls whether this is a reference-counted or 
	                non-reference- counted MulticastLock. 
	                Reference-counted MulticastLocks keep track of the 
	                number of calls to acquire() and release(), and 
	                only stop the reception of multicast packets when 
	                every call to acquire() has been balanced with a 
	                call to release(). Non-reference- counted 
	                MulticastLocks allow the reception of multicast 
	                packets whenever acquire() is called and stop 
	                accepting multicast packets whenever release() is 
	                called.*/
	                lock.setReferenceCounted(false);
	                 
	                try {
	                    InetAddress addr = getLocalIpAddress();
	                    InetAddress addr1 = InetAddress.getByName(addr.getHostName());
	                    String hostname = addr1.getHostName(); 
	                    String Delims="\\.";
	                    String[] actualHostName=hostname.split(Delims);
	                   
	                    for(int i=1;i<actualHostName.length;i++){
	                    	requiredHostNameSuffix=requiredHostNameSuffix+"."+actualHostName[i];
	                    	
	                    }
	                    CommonValues.getInstance().hostNameSuffix=requiredHostNameSuffix.trim();
	                    lock.acquire();
	                }
	                   catch (IOException e) {
	                    e.printStackTrace();
	                    return;
	                }
	         
	        }}).start();
	
}
	  
	  private InetAddress getLocalIpAddress() {
	    	WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
	        InetAddress address = null;
	        try {
	        	address = InetAddress.getByName(String.format(Locale.ENGLISH,
	                "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        return address;
	    }

}
