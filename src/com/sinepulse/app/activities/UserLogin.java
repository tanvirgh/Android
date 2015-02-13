package com.sinepulse.app.activities;

import java.io.IOException;
import java.io.InputStream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.ActionBar;
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
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * 
 * @author tavnir
 * 
 */

@EActivity(R.layout.user_login)
public class UserLogin extends MainActionbarBase implements OnClickListener {

	public ActionBar mSupportActionBar;

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
//	CheckMC checkMC = null;
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
	String urlForMc="";

	/**
	 * Called when the activity is started.
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		UserLogin.context = this;
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.hide();

		// specifyAppMode();
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
		if (checkPlayServices()) {
			// Log.d(TAG, "Play Service Found!");
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(this);
			if (regid.isEmpty()) {
				registerInBackground();
				// Log.d(TAG, regid);
			} else {
				// mDisplay.setText("Stored" + regid);
				// Log.d(TAG, regid);
			}
		} else {
			// Log.i(TAG, "No valid Google Play Services APK found.");
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
			 if(CommonTask.checkEmail(etUserName.getText().toString())==true){
				 proceedLoginProcess();
				}else{
					etUserName.setError("Invalid Email address.");
				}
			// if(specifyAppMode()){
			
			// }
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

		if (selectAppMode() == true) {

		} else {
			CommonTask.ShowMessage(this, "Failed to resolve service mode.");
		}

		// selectAppMode();
/*		if (CommonURL.getInstance().LoginCustomerURL
				.equals("http://dev.sinepulse.com/smarthome/service/prod/DataAPI.svc/data/login")) {
			Toast.makeText(context, "Internet Mode", Toast.LENGTH_SHORT).show();
		}
*/
		etUserPassword.clearFocus();
		super.onResume();
	}

	/**
	 * 
	 */
	public boolean selectAppMode() {
		if (CommonTask.isNetworkAvailable(this)) {
			resolveNetworkState();
			return true;
		} else {
			CommonTask.ShowMessage(this,
					"Please turn on WiFi or Data Connection");
			return false;
		}

	}

	/**
	 * 
	 */
	public void resolveNetworkState() {
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (status.equals("Wifi enabled")) {
			/*if (CommonTask.isEmpty(CommonTask.getBaseUrl(this))) {
				// If SharedPreference is empty try resolving with host
				// name...Step1
				connectByHostName();

			} else {
				// Already have an IP in Preference...Lets try to connect with
				// that...Step2
				urlForMc = "http://"
						+ CommonTask.getBaseUrl(this) + "/api/is-online";
				if (checkMC != null) {
					checkMC.cancel(true);
				}
				connnectionState = "PREF";
				checkMC = new CheckMC(urlForMc, (UserLogin_) this,false);
//				checkMC.execute();
				checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			}*/
			connectByHostName();
		} else if (status.equals("Mobiledata enabled")) {
			CommonURL.getInstance().assignValues(
					CommonURL.getInstance().remoteBaseUrl);
//			Toast.makeText(context, "Internet Mode", Toast.LENGTH_SHORT).show();
			CommonValues.getInstance().connectionMode="Internet";
			// Log.d("NInfo", "GSM");
		} else {
			CommonValues.getInstance().IsServerConnectionError = true;
			CommonTask.ShowMessage(this,
					"Device offline ! Pleasee check your network connection.");

		}
	}

	/**
	 * Failed to resolve with host name..Lets try with replacing WIFI IP last
	 * sub net mask and attempt to connect.
	 */

	public void connectByIp() {
		WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		int ip = wifiInfo.getIpAddress();
		String ipAddress = Formatter.formatIpAddress(ip);
		String[] tokens = ipAddress.split("\\.");
		tokens[3] = "151";
		ipAddress = tokens[0] + "." + tokens[1] + "." + tokens[2] + "."
				+ tokens[3];
		// Log.d("WIFI Ip", ipAddress);
		urlForMc = "http://" + ipAddress + "/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}
		connnectionState = "IP";
		checkMC = new CheckMC(urlForMc, (UserLogin_) this,isSolvedLocal);
//		checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		checkMC.execute();
	}

	/*private class CheckMC extends AsyncTask<Void, Void, Boolean> {
		private String mcUrl;
		private Context context;

		public CheckMC(String mcUrl, Context context) {
//			this.mcUrl = mcUrl;
//			this.context = context;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			sendMCStatusRequest(mcUrl);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// bUserLogin.setEnabled(true);
			if (isSolvedLocal == true) {
				// proceedLoginProcess();
				saveLocalIpInPreference();
				Toast.makeText(context, "Local Mode", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(context, "Internet Mode", Toast.LENGTH_SHORT)
						.show();
			}
		};

	}*/

	public boolean sendMCStatusRequest(String mcUrl) {

		if (getMcStatus(mcUrl) != null && getMcStatus(mcUrl) != "") {
			return true;
		}
		return false;
	}

	boolean isSolvedLocal = false;

	public String getMcStatus(String url) {

		InputStream is = null;
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters,
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
			e.printStackTrace();
		}
 		if (result != null && !result.equals("")) {

			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				// String
				// host=(jObject.getJSONObject("Data")).getString("hostname");
				String Ip = (jObject.getJSONObject("Data")).getString("ip");
				CommonValues.getInstance().localIp = Ip;

				String baseUrlForMC = "http://" + Ip + "/api/";
				CommonURL.getInstance().assignValues(baseUrlForMC);
				isSolvedLocal = true;
				CommonValues.getInstance().connectionMode="Local";

			}

			catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		} else {

			if (connnectionState.equals("RasPeri")) {
				// Failed to resolve with host name..Lets try with replacing
				// WIFI IP last sub net...Step3
				isSolvedLocal = false;
				urlForMc="";
				connectByIp();
			}
			/*else if (connnectionState.equals("PREF")) {
				// Failed to resolve with shared preference IP..Lets try with
				// host name
				isSolvedLocal = false;
				urlForMc="";
				connectByHostName();
			} */
			else if (connnectionState.equals("IP")) {
				// Both Host and IP resolve process failed...Lets assign the
				// Internet URL as base URL
				isSolvedLocal = false;
				CommonURL.getInstance().assignValues(
						CommonURL.getInstance().remoteBaseUrl);
				CommonValues.getInstance().connectionMode="Internet";

			}
		}

		// 11. return result
		return result;
	}

	/**
	 * try to resolve the connectivity by Appropriate host name
	 */

	public void connectByHostName() {
		connnectionState = "RasPeri";
		urlForMc = "http://sinepulsemcprod/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}

		checkMC = new CheckMC(urlForMc, (UserLogin_) this,isSolvedLocal);
		checkMC.execute();
//		checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

	public void saveLocalIpInPreference() {
		CommonTask.SavePreferences(this, CommonConstraints.PREF_URL_KEY,
				CommonConstraints.PREF_LOCALIP_KEY,
				CommonValues.getInstance().localIp);
	}

	/**
	 * Method that check whether the application is connected to server or not
	 */

	public boolean isConnectedToServer(String AppToken) {
		return CommonTask.isValidLogIn(etUserName.getText().toString(),
				etUserPassword.getText().toString(), AppToken);
	}

	private boolean validateLoginInfo() {
		if (etUserName.getText().toString().trim().equals("")) {
			etUserName.setError("Please Provide UserName");
			return false;
		} else if (etUserPassword.getText().toString().trim().equals("")) {
			etUserPassword.setError("Please Provide Password");
			return false;
		}
		else{
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
		//though this is not recommended to use this method but here it has been used for a custom requirement..Tanvir
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
		CommonValues.getInstance().appToken = registrationId;
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



}
