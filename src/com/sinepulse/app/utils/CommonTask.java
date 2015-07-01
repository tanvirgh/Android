package com.sinepulse.app.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sinepulse.app.R;
import com.sinepulse.app.animations.AlphaAnimationListener;
import com.sinepulse.app.animations.DisplayNextView;
import com.sinepulse.app.animations.Flip3dAnimation;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Alert;
import com.sinepulse.app.entities.LogInInfo;

/**
 * @author Some common methods are defined in this class that is used all
 *         through the application such as email validation,robotto font
 *         method,cofirmation message,alert message,loading user info etc.
 */

@SuppressLint("DefaultLocale")
public class CommonTask {
	public static String getString(String value) {

		if (value == null)
			return "";
		try {
			return new String(value.getBytes(CommonConstraints.EncodingCode));
		} catch (UnsupportedEncodingException e) {
			return e.toString();
		}
	}

	public static String getString(double value) {

		String dVal = NumberFormat.getInstance(Locale.FRANCE).format(
				CommonTask.round(value, 2, BigDecimal.ROUND_HALF_UP));
		String[] vals = dVal.split(",");
		if (vals.length == 1) {
			return vals[0] + "," + "00";
		} else if (vals.length > 1) {
			return vals[1].length() > 1 ? dVal : vals[0] + "," + vals[1] + "0";
		}
		return dVal;

	}

	public static String getContentString(double value) {

		String dVal = NumberFormat.getInstance(Locale.FRANCE).format(
				CommonTask.round(value, 2, BigDecimal.ROUND_HALF_UP));
		String[] vals = dVal.split(",");
		if (vals.length > 1) {
			return vals[1].length() > 1 ? dVal : (vals[1] != "0" ? (vals[0]
					+ "," + vals[1] + "0") : "");
		}
		return dVal;

	}

	public static int convertToDimensionValue(Context context, int inputValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				inputValue, context.getResources().getDisplayMetrics());
	}

	private static BitmapDrawable getDrawableImage(String url) {
		try {
			URL urlAdd = new URL(url);
			URLConnection connection = urlAdd.openConnection();
			connection.setConnectTimeout(CommonConstraints.TIMEOUT_MILLISEC);
			connection.setReadTimeout(CommonConstraints.TIMEOUT_MILLISEC);
			connection.connect();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);

			Bitmap bmp = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return new BitmapDrawable(null, bmp);

		} catch (Exception e) {
		}
		return null;
	}

	public static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}

	public static String getstring(int i) {
		String aString = Integer.toString(i);
		return aString;

	}

	@SuppressLint("DefaultLocale")
	public static String toCamelCase(String s) {
		return s.substring(0, 1).toUpperCase(Locale.getDefault())
				+ s.substring(1).toLowerCase(Locale.getDefault());
	}

	public static String toCamelCase(String s, String separator) {
		String[] parts = s.split(separator);
		String camelCaseString = "";
		for (String part : parts) {
			if (!part.equals(null) && !part.isEmpty() && !part.equals("")) {
				camelCaseString = camelCaseString + toCamelCase(part);
				camelCaseString = camelCaseString + " ";
			} else {
				camelCaseString = part;
			}
		}
		return camelCaseString;
	}

	public static void ShowMessage(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name)
				.setIcon(R.drawable.warning)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void ShowAlertMessage(Context context, Alert alert) {
		String connectionNode="";
		if(CommonValues.getInstance().connectionMode.equals("Local")){
			connectionNode="[MC]: ";
		}else{
			connectionNode="[GSB]: ";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(alert.title +" "+alert.type+" ["+alert.code+"]")
				.setIcon(R.drawable.warning)
				.setMessage(connectionNode+alert.details)
				.setCancelable(false)
				.setNegativeButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDg = builder.create();
		alertDg.show();
	}

	public static void ShowConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setIcon(R.drawable.warning)
				.setMessage(message)
				.setPositiveButton(R.string.button_yes, event)
				.setNegativeButton(R.string.button_no, event);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void ShowNetworkChangeConfirmation(Context context,
			String message, DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setIcon(R.drawable.warning)
				.setMessage(message)
				.setPositiveButton(R.string.button_yes, event);
		AlertDialog alert = builder.create();
		alert.show();
	}

	// method for exit from the app
	public static void CloseApplication(Context context) {

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void ApplyRotation(float start, float end,
			boolean is1stLayout, LinearLayout layout1, LinearLayout layout2,
			int rotateDuration, InputMethodManager imm, int keyboardStatus) {
		final float centerX = layout1.getWidth() / 2.0f;
		final float centerY = layout1.getHeight() / 2.0f;
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(rotateDuration);
		rotation.setAnimationListener(new DisplayNextView(is1stLayout, layout1,
				layout2, imm, keyboardStatus));
		if (is1stLayout) {
			layout1.startAnimation(rotation);

		} else {
			layout2.startAnimation(rotation);
		}
	}

	public static void setAlphaVisible(View view, boolean isVisible) {
		AlphaAnimation animation = isVisible ? new AlphaAnimation(0.0f, 1.0f)
				: new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(1000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AlphaAnimationListener(view,
				isVisible));
	}

	/*
	 * public static void setAsyncImageBackground(View view, String id) {
	 * view.setTag(id); new AsyncImageLoader().execute(view); }
	 */

	// for the previous movement
	public static Animation inFromRightAnimation() {
		return inFromRightAnimation(350);
	}

	public static Animation outToLeftAnimation() {
		return outToLeftAnimation(350);
	}

	public static Animation inFromLeftAnimation() {
		return inFromLeftAnimation(350);
	}

	public static Animation outToRightAnimation() {
		return outToRightAnimation(350);
	}

	public static Animation inFromRightAnimation(int speed) {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(speed);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public static Animation outToLeftAnimation(int speed) {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(speed);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	// for the next movement
	public static Animation inFromLeftAnimation(int speed) {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(speed);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	public static Animation outToRightAnimation(int speed) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(speed);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public static int SWIPE_MIN_DISTANCE() {
		return 25;
	}

	public static int SWIPE_MAX_OFF_PATH() {
		return 25;
	}

	public static int SWIPE_THRESHOLD_VELOCITY() {
		return 30;
	}

	// method for saving data to shared preference
	public static void SavePreferences(Context context, String sharedId,
			String key, String value) {

		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// sharedId, Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void loadSettings(Context context) {
		// String baseUrl = getBaseUrl(context);
		// String baseUrl =
		// "http://dev.sinepulse.com/smarthome/service/dev/DataAPI.svc/data/";
		// if (!baseUrl.equals("")) {
		// CommonURL.getInstance().assignValues(baseUrl,
		// getPassword(context));
		// }
	}

	public static String getBaseUrl(Context context) {
		String baseUrlIp = "";
		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// "settings", Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		baseUrlIp = sharedPreferences.getString(
				CommonConstraints.PREF_LOCALIP_KEY, "");
		return baseUrlIp;
	}

	public static String getPassword(Context context) {
		String password = "";
		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// "settings", Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		password = sharedPreferences.getString(
				CommonConstraints.PREF_PASSWORD_KEY, "");
		return password;
	}

	public static String getUserName(Context context) {
		String userName = "";
		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// "settings", Context.MODE_PRIVATE);
		/*
		 * SharedPreferences sharedPreferences = PreferenceManager
		 * .getDefaultSharedPreferences(context); userName =
		 * sharedPreferences.getString( CommonConstraints.PREF_URL_KEY, "");
		 */
		return userName;
	}

	// This Method Checks the successful log in status
	public static boolean isValidLogIn(Context context,String userName, String password,
			String AppToken) {

		LogInInfo logInInfo = new LogInInfo();
		logInInfo.setUserName(userName);
		logInInfo.setUserpassword(password);
		logInInfo.setAppToken(AppToken);
		logInInfo.setAppType("1");

		// String asd=CommonURL.getInstance().baseUrl;
		if (JsonParser.postLogInRequest(context,
				CommonURL.getInstance().LoginCustomerURL, logInInfo) != null) {
			return true;
		}
		return false;
	}

	public static boolean getCheckoutMode(Context context) {
		boolean onlineCheckout = false;
		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// "settings", Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		onlineCheckout = sharedPreferences.getBoolean(
				CommonConstraints.PREF_CHECKOUT_KEY, false);
		return onlineCheckout;
	}

	public static void loadLoginUser(Context context) {
		// SharedPreferences sharedPreferences = context.getSharedPreferences(
		// CommonConstraints.PREF_LOGINUSER_NAME, Context.MODE_PRIVATE);
		//
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

	}

	public static void resetPreferenceValue(Context context) {
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CUSTOMERID_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_FIRSTNAME_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_LASTNAME_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ZIPCODE_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_EMAIL_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_TELEPHONE_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS1_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS2_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CITY_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_PASSWORD_KEY, "");
		loadLoginUser(context);
	}

	/*
	 * public static UserInformation getLoginUser() { if (isUserLoggedIn()) {
	 * return CommonValues.getInstance().loginuser; } else { return null; } }
	 */

	public static boolean isUserLoggedIn() {
		if (CommonValues.getInstance().profile == null)
			return false;
		// return !CommonValues.getInstance().loginuser.IsLoggedIn;
		return true;
	}

	public static void setApplicationUrls(Context context,
			ArrayList<String> values) {
		setStringArrayPref(context, CommonConstraints.PREF_BASEURLS_KEY, values);
	}

	public static ArrayList<String> getApplicationUrls(Context context) {
		return getStringArrayPref(context, CommonConstraints.PREF_BASEURLS_KEY);
	}

	public static void setStringArrayPref(Context context, String key,
			ArrayList<String> values) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		JSONArray a = new JSONArray();
		for (int i = 0; i < values.size(); i++) {
			a.put(values.get(i));
		}
		if (!values.isEmpty()) {
			editor.putString(key, a.toString());
		} else {
			editor.putString(key, null);
		}
		editor.commit();
	}

	public static ArrayList<String> getStringArrayPref(Context context,
			String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String json = prefs.getString(key, null);
		ArrayList<String> urls = null;
		if (json != null) {
			try {
				urls = new ArrayList<String>();
				JSONArray a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					String url = a.optString(i);
					urls.add(url);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}

	// Method for Checking Email Validation..Tanvir
	public static boolean checkEmail(String email) {
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	// Method for displaying Checkout Confirmationmessage..Tanivr
	public static void showCheckoutConfirmation(Context context,
			String message, DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setPositiveButton(R.string.button_ok, event);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Indicates whether network connectivity exists or is in the process of
	 * being established. For the latter, call isConnected() instead, which
	 * guarantees that the network is fully usable.
	 * 
	 * @param context
	 * @return Returns true if network connectivity exists or is in the process
	 *         of being established, false otherwise.
	 */
	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		State networkState = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.
					getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null) {
				networkState = networkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED
						|| networkState == NetworkInfo.State.CONNECTING) {
					return true;
				}
			}
			networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {

				networkState = networkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED
						|| networkState == NetworkInfo.State.CONNECTING) {
					return true;
				}
			}

		}
		return false;
		
	/*	if(connectivityManager.getBackgroundDataSetting()==true){
			return true;
		}else{
			return false;
		}*/
	}
	

	public static String lastConType = "";

	public static boolean isNetworkStateChanged(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobnetworkInfo = null;
		NetworkInfo wifinetworkInfo = null;
		State networkState = null;
		if (lastConType.equals("Mobile")) {
			wifinetworkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifinetworkInfo != null) {

				networkState = wifinetworkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED) {
					lastConType = "";
					return false;
				}
			}
		}

		if (connectivityManager != null) {
			// Check Wifi
			wifinetworkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifinetworkInfo != null) {

				networkState = wifinetworkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED) {
					lastConType = "Wifi";
					return true;
				}
			}
			// Check mobile
			mobnetworkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobnetworkInfo != null) {
				networkState = mobnetworkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED) {
					lastConType = "Mobile";
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * User for getting exception message
	 * 
	 * @param context
	 * @param exceptionCode
	 * @return
	 */
	public static String getCustomExceptionMessage(Context context,
			int exceptionCode) {

		switch (exceptionCode) {
		// case CommonConstraints.GENERAL_EXCEPTION:
		// return context.getString(R.string.GeneralExceptionMessage);
		// case CommonConstraints.CLIENT_PROTOCOL_EXCEPTION:
		// return context.getString(R.string.ClientProtocolExceptionMessage);
		// case CommonConstraints.ILLEGAL_STATE_EXCEPTION:
		// return context.getString(R.string.IllegalStateExceptionMessage);
		// case CommonConstraints.IO_EXCEPTION:
		// return context.getString(R.string.IOExceptionMessage);
		// case CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION:
		// return context
		// .getString(R.string.UnsupportedEncodingExceptionMessage);
		// case CommonConstraints.NO_INTERNET_AVAILABLE:
		// return context
		// .getString(R.string.networkError);
		default:
			return "";
		}
	}

	public static void showSoftKeybord(final EditText editText) {
		(new Handler()).postDelayed(new Runnable() {

			@Override
			public void run() {
				editText.dispatchTouchEvent(MotionEvent.obtain(
						SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_DOWN, 0, 0, 0));
				editText.dispatchTouchEvent(MotionEvent.obtain(
						SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_UP, 0, 0, 0));
				editText.selectAll();

			}
		}, 200);
	}

	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0 || str == "")
			return true;
		else
			return false;
	}

	public static void ShowLogOutConfirmation(Context context, String message,
			android.content.DialogInterface.OnClickListener showLogOutEvent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setIcon(R.drawable.warning)
				.setMessage(message)
				.setPositiveButton(R.string.button_yes, showLogOutEvent)
				.setNegativeButton(R.string.button_no, showLogOutEvent);
		AlertDialog alert = builder.create();
		alert.show();

	}

}
