/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.sinepulse.app.activities.DisplayDeviceDetails;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.adapters.DeviceListByTypeAdapter;

/**
 * @author tanvir.ahmed
 * 
 */
public class AsyncSetStatusFromDashboard extends AsyncTask<Void, Void, Boolean> {

	DeviceListByTypeAdapter displayDetails;
	Context parentActivity;
	private int deviceId;
	private int userId;
	private boolean isChecked;
	private int value;
//	AsyncProcessRequestFromDashboard mListener;

	public AsyncSetStatusFromDashboard(DeviceListByTypeAdapter displayDetails,
			Context _parentActivity, int deviceId, int userId, int value) {
		this.parentActivity = _parentActivity;
		this.displayDetails = displayDetails;
		this.deviceId = deviceId;
		this.userId = userId;
		this.value = value;

	}
	/*public AsyncSetStatusFromDashboard(AsyncProcessRequestFromDashboard listener) {
		mListener  = listener;
	}*/
	

	@Override
	protected void onPreExecute() {
//		((Home) parentActivity).startDeviceProgress();
//		mListener.onTaskPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		
		displayDetails.sendSetStatusRequest(userId, deviceId, value);
//		SystemClock.sleep(2000);
//		mListener.startSendingTask();
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (displayDetails != null) {
				((Home) parentActivity).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						((Home) parentActivity).stopDeviceProgress();
						displayDetails.setStatusResponseData();
					}
				});
				
			}
		}
//		mListener.onTaskPostExecute(result);
	}
}
