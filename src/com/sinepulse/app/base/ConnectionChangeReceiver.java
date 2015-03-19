/**
 * 
 */
package com.sinepulse.app.base;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.asynctasks.AsyncLogOutTask;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author tanvir.ahmed
 * 
 */
public class ConnectionChangeReceiver extends BroadcastReceiver

{

	AsyncLogOutTask asyncLogOutTask = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			CommonValues.getInstance().summary.deviceSummaryArray.clear();
			CommonValues.getInstance().userId = 0;
			CommonValues.getInstance().currentAction = CommonIdentifier.Action_LogOut;
			if (asyncLogOutTask != null) {
				asyncLogOutTask.cancel(true);
			}
			asyncLogOutTask = new AsyncLogOutTask((Home) Home.context,
					CommonValues.getInstance().userId);
			asyncLogOutTask.execute();
		}
		if (mobNetInfo != null) {
			CommonValues.getInstance().summary.deviceSummaryArray.clear();
			CommonValues.getInstance().userId = 0;
			CommonValues.getInstance().currentAction = CommonIdentifier.Action_LogOut;
			if (asyncLogOutTask != null) {
				asyncLogOutTask.cancel(true);
			}
			asyncLogOutTask = new AsyncLogOutTask((Home) Home.context,
					CommonValues.getInstance().userId);
			asyncLogOutTask.execute();
		}

	}

}
