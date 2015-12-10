package com.sinepulse.app.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

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
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
	// CheckMC checkMC = null;
	CheckMC checkMC = null;
	CheckMC checkMC1 = null;
	protected static String connnectionState = "NONE";

	public static Context context;
	private android.os.Handler handler;

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
		UserLogin.context = this;
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.hide();
		// getHostnameSuffix();
		// specifyAppMode();
	}

	@AfterViews
	void afterViewsLoaded() {
		// bUserLogin.setEnabled(false);
		handler = new android.os.Handler();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		etUserName.requestFocus();
		CommonTask.showSoftKeybord(etUserName);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		etUserPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!etUserName.hasFocus()) {
					if (etUserName.getText().toString().trim().equals("")) {
						etUserName.setError("Please Provide User Name.");
						etUserName.requestFocus();
						etUserPassword.clearFocus();
					} else if (CommonTask.checkEmail(etUserName.getText()
							.toString()) == false) {
						etUserName.setError("Invalid Emal Address.");
						etUserName.requestFocus();
						etUserPassword.clearFocus();
					}

					else {
						etUserName.setError(null);
					}

				}

			}
		});
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
			} else {

			}
		} else {
			// Log.i(TAG, "No valid Google Play Services APK found.");
			regid = "";
		}

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

	boolean isloginworkDone = false;

	/**
	 * use for manage all button click events centrally
	 */

	@Click(R.id.bUserLogin)
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bUserLogin:
			if (validateLoginPassInfo()) {
				bindHostname();
			}
			handler.postDelayed(new Runnable() {
				public void run() {
					// Looper.prepare();
					proceedLoginProcess();
					// handler.removeCallbacks(this);
					// Looper.myLooper().quit();
				}
			}, 7000);
			
			

			break;
		default:
			break;
		}

	}

	/**
	 * 
	 */
	public void proceedLoginProcess() {

		etUserName.clearFocus();
		etUserPassword.clearFocus();
		backState = INITIAL_STATE;
		imm.hideSoftInputFromWindow(bUserLogin.getWindowToken(), 0);
		/*
		 * if (CommonTask.isEmpty(regid)) { registerInBackground(); } else {
		 * String GcmRegId = getRegistrationId(getApplicationContext());
		 * saveUserInfo(GcmRegId, isloginworkDone); //
		 * System.out.println(GcmRegId); }
		 */
		String GcmRegId = getRegistrationId(getApplicationContext());
		if (GcmRegId != "") {
			saveUserInfo(GcmRegId, isloginworkDone);
		} else {
			GcmRegId = "";
			saveUserInfo(GcmRegId, isloginworkDone);
		}
	}

	// }

	/**
	 * Save UserName and Password through async call
	 */
	public void saveUserInfo(String GcmRegistrationId, boolean isloginworkDone) {
		if (asyncSaveLogInInfo != null) {
			asyncSaveLogInInfo.cancel(true);
		}
		asyncSaveLogInInfo = new AsyncCheckServerStateAndSaveServerInfo(this,
				GcmRegistrationId, isloginworkDone);
		asyncSaveLogInInfo.execute();
	}

	

	public void startmenuProgress() {
		pbFirstPage.setVisibility(View.VISIBLE);

	}

	public void stopmenuProgress() {
		pbFirstPage.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
       loadPreferences();
		etUserPassword.clearFocus();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		saveUsernameAndPassword();
		super.onPause();
	}

	/**
	 * 
	 */
	private void bindHostname() {
		if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("pwd@aplombtechbd.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemcpwd", "30");
			}
		} else if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("sinepulsemctesta@dayrep.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemctest", "127");
			}
		} else if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("mict5@aplombtechbd.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemcict1", "24");
			}
		} else if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("mict6@aplombtechbd.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemcict2", "23");
			}
		} else if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("hpmd@aplombtechbd.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemchp", "");
			}
		} else if (etUserName.getText().toString().trim()
				.equalsIgnoreCase("nik@aplombtechbd.com")) {
			if (CommonTask.isNetworkAvailable(this)) {
				resolveNetworkState("sinepulsemc", "");
			}
		}

		else {
			if (CommonTask.isNetworkAvailable(this)) {
				// resolveNetworkState("sinepulsemcprod", "151");
				isSolvedLocal = false;
				CommonURL.getInstance().assignValues(
						CommonURL.getInstance().remoteBaseUrl);
				CommonValues.getInstance().connectionMode = "Internet";
				Toast.makeText(
						context,
						"Local server unreachable." + "\n"
								+ "Connecting to Internet Server.",
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	/**
	 * 
	 */
	/*
	 * public boolean selectAppMode() { if (CommonTask.isNetworkAvailable(this))
	 * { resolveNetworkState(); return true; } else { //
	 * CommonTask.ShowMessage(this, //
	 * "Please turn on WiFi or Data Connection"); return false; }
	 * 
	 * }
	 */

	/**
	 * 
	 */
	public void resolveNetworkState(String hostName, String IP) {
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (status.equals("Wifi enabled")) {
			/*
			 * if (CommonTask.isEmpty(CommonTask.getBaseUrl(this))) { // If
			 * SharedPreference is empty try resolving with host // name...Step1
			 * connectByHostNameLocal(hostName, IP);
			 * 
			 * } else { // Already have an IP in Preference...Lets try to
			 * connect with // that...Step2 urlForMc = "http://" +
			 * CommonTask.getBaseUrl(this) + "/api/is-online";
			 * UserLogin.this.runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() { Toast.makeText(context,
			 * "Connecting By Ip", Toast.LENGTH_SHORT).show();
			 * 
			 * } }); if (checkMC != null) { checkMC.cancel(true); }
			 * connnectionState = "IP"; checkMC = new CheckMC(urlForMc,
			 * (UserLogin_) this, false, "", IP); checkMC.execute(); //
			 * checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			 * 
			 * }
			 */
			connectByHcIp(hostName, IP);
		} else if (status.equals("Mobiledata enabled")) {
			CommonURL.getInstance().assignValues(
					CommonURL.getInstance().remoteBaseUrl);
			Toast.makeText(context, "Connecting to Internet server",
					Toast.LENGTH_SHORT).show();
			CommonValues.getInstance().connectionMode = "Internet";
			// Log.d("NInfo", "GSM");
		} else {
			CommonValues.getInstance().IsServerConnectionError = true;
			// CommonTask.ShowMessage(this,
			// "Pleasee check your network connection.");

		}
	}

	/**
	 * Failed to resolve with host name..Lets try with replacing WIFI IP last
	 * sub net mask and attempt to connect.
	 */
	WifiManager wifiMgr;
	String targetIp = "";

	public void connectByIp(String hostName, String IP) {

		// Log.d("WIFI Ip", ipAddress);
		targetIp = CommonValues.getInstance().nsdResolvedIp;

		if (targetIp == null ) {
			UserLogin.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context,
							"NSD failed to resolve IP .Ip result :" + targetIp,
							Toast.LENGTH_SHORT).show();

				}
			});
			// connectByHostName(hostName, IP);
			if(IP.equals("")){
				connectByHostName(hostName, IP);
			}else{
				connectByHcIp(hostName, IP);
			}
			
		} else {
			urlForMc = "http://" + targetIp + "/api/is-online";
			UserLogin.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "Connecting By" + targetIp,
							Toast.LENGTH_SHORT).show();

				}
			});
			if (checkMC != null) {
				checkMC.cancel(true);
			}
			connnectionState = "IP";
			checkMC = new CheckMC(urlForMc, (UserLogin_) this, isSolvedLocal,
					hostName, IP);
			// checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			checkMC.execute();
		}
	}

	public void connectByHcIp(String hostName, String IP) {
		WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		int ip = wifiInfo.getIpAddress();
		@SuppressWarnings("deprecation")
		String ipAddress = Formatter.formatIpAddress(ip);
		String[] tokens = ipAddress.split("\\.");
		tokens[3] = IP;
		ipAddress = tokens[0] + "." + tokens[1] + "." + tokens[2] + "."
				+ tokens[3];
		// Log.d("WIFI Ip", ipAddress);
		urlForMc = "http://" + ipAddress + "/api/is-online";
		if (checkMC != null) {
			checkMC.cancel(true);
		}
		connnectionState = "HCIP";
		checkMC = new CheckMC(urlForMc, (UserLogin_) this, isSolvedLocal,
				hostName, IP);
		// checkMC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		checkMC.execute();
	}

	public boolean sendMCStatusRequest(String mcUrl, String hostName, String IP) {

		if (getMcStatus(mcUrl, hostName, IP) != null
				&& getMcStatus(mcUrl, hostName, IP) != "") {
			return true;
		} else {
			// CommonTask
			// .ShowMessage(UserLogin.this, "Failed to Resolve " + mcUrl);
			return false;
		}
	}

	boolean isSolvedLocal = false;

	public String getMcStatus(String url, final String hostName, final String IP) {

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
				// saveLocalIpInPreference();

				String baseUrlForMC = "http://" + Ip + "/api/";
				CommonURL.getInstance().assignValues(baseUrlForMC);
				isSolvedLocal = true;
				CommonValues.getInstance().connectionMode = "Local";
				UserLogin.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "Local Mode",
								Toast.LENGTH_SHORT).show();

					}
				});

			}

			catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		} else {
			if (connnectionState.equals("IP")) {
				// Failed to resolve with host name.local..try with hostname
				isSolvedLocal = false;
				urlForMc = "";
				UserLogin.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						connectByHcIp(hostName, IP);
					}
				});
			}
			else if (connnectionState.equals("HCIP")) {

				isSolvedLocal = false;
				urlForMc = "";
				UserLogin.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						connectByHostName(hostName, IP);
					}
				});
			} else if (connnectionState.equals("RasPeri")) {
				// Both Host and IP resolve process failed...Lets assign the
				// Internet URL as base URL
				isSolvedLocal = false;
				CommonURL.getInstance().assignValues(
						CommonURL.getInstance().remoteBaseUrl);
				CommonValues.getInstance().connectionMode = "Internet";
				UserLogin.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								context,
								"Local server unreachable." + "\n"
										+ "Connecting to Internet Server.",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}

		// 11. return result
		return result;
	}

	/**
	 * try to resolve the connectivity by Appropriate host name
	 */
	public void connectByHostName(final String hostName, String IP) {
		UserLogin.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(UserLogin.this, "Connecting by " + hostName,
						Toast.LENGTH_SHORT).show();

			}
		});
		connnectionState = "RasPeri";
		urlForMc = "http://" + hostName + "/api/is-online";

		if (checkMC1 != null) {
			checkMC1.cancel(true);
		}
		// checkMC=null;
		checkMC1 = new CheckMC(urlForMc, (UserLogin_) this, isSolvedLocal,
				hostName, IP);
		// checkMC1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		checkMC1.execute();
	}

	/**
	 * try to resolve the connectivity by Appropriate host name.local
	 */

	public void connectByHostNameLocal(String hostName, String IP) {
		UserLogin.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "Connecting by hostname.local",
						Toast.LENGTH_SHORT).show();

			}
		});
		connnectionState = "Local";
		urlForMc = "http://" + hostName + ".local" + "/api/is-online";

		if (checkMC != null) {
			checkMC.cancel(true);
		}

		checkMC = new CheckMC(urlForMc, (UserLogin_) this, isSolvedLocal,
				hostName, IP);
		checkMC.execute();

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
	 * Use for loading the username,password from SharedPreferences which is
	 * already saved before from login screen input
	 */
	public void loadPreferences() {
		etUserName.setText(CommonTask.getUserName(UserLogin.context));
		etUserPassword.setText(CommonTask.getPassword(UserLogin.context));
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
				etUserPassword.getText().toString(), AppToken, this);
	}

	private boolean validateLoginPassInfo() {

		if (etUserName.getText().toString().trim().equals("")) {
			etUserName.setError("Please Provide User Name.");
			etUserName.requestFocus();
			etUserPassword.clearFocus();
			return false;
		} else if (CommonTask.checkEmail(etUserName.getText().toString()) == false) {
			etUserName.setError("Invalid Email Address.");
			etUserName.requestFocus();
			etUserPassword.clearFocus();
			return false;
		} else if (etUserPassword.getText().toString().trim().equals("")) {
			etUserPassword.setError("Please Provide Password");
			etUserName.clearFocus();
			etUserPassword.requestFocus();
			return false;
		}

		else {
			etUserName.setError(null);
			etUserPassword.setError(null);
			return true;
		}
	}

	/**
	 * After save user information application will redirect to home activity
	 * 
	 */
	public void setUserInfoAfterSave(boolean isloginworkDone) {
		
//		 CommonTask.loadSettings(this);
		// CommonValues.getInstance().loginuser = new UserInformation();
		if (isloginworkDone) {
			Intent homeIntent = new Intent(this, Home_.class);
			startActivity(homeIntent);
			// though this is not recommended to use this method but here it has
			// been used for a custom requirement..Tanvir
			finish();
		}

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
					regid = "";
					storeRegistrationId(context, regid);
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

	private InetAddress getLocalIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		InetAddress address = null;
		try {
			address = InetAddress.getByName(String.format(Locale.ENGLISH,
					"%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
					(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address;
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();
		// lock.release();
	}

	/*
	 * String requiredHostNameSuffix = "";
	 * 
	 * private void getHostnameSuffix() {
	 * 
	 * new Thread(new Runnable() {
	 * 
	 * @Override public void run() { android.net.wifi.WifiManager wifi =
	 * (android.net.wifi.WifiManager)
	 * getSystemService(android.content.Context.WIFI_SERVICE);
	 * 
	 * lock = wifi.createMulticastLock(getClass().getSimpleName());
	 * 
	 * lock.setReferenceCounted(false);
	 * 
	 * try { InetAddress addr = getLocalIpAddress(); for(InetAddress addr1 :
	 * InetAddress.getAllByName("192.168.1.228")){
	 * System.out.println("tanvir :"+addr1.getHostAddress()); } InetAddress
	 * addr1 = InetAddress.getByName(addr .getHostName()); String hostname =
	 * addr1.getHostName(); String Delims = "\\."; String[] actualHostName =
	 * hostname.split(Delims);
	 * 
	 * for (int i = 1; i < actualHostName.length; i++) { requiredHostNameSuffix
	 * = requiredHostNameSuffix + "." + actualHostName[i];
	 * 
	 * } lock.acquire(); } catch (IOException e) { e.printStackTrace(); return;
	 * }
	 * 
	 * } }).start();
	 * 
	 * }
	 */

}
