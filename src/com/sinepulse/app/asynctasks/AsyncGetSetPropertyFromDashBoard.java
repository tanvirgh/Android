/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetSetPropertyFromDashBoard extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
//	DeviceListByTypeAdapter parentActivity;
	Home parentActivity;
	private int deviceId;
	private int userId;
	private int propertyId;
	private int value;
	
	public AsyncGetSetPropertyFromDashBoard(Home _parentActivity,int userId,int deviceId,int propertyId,int value) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.userId=userId;
		this.propertyId=propertyId;
		this.value=value;
		
	}
	

	@Override
	protected void onPreExecute() {
		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.setPropertyRequestFromDashboard(userId,deviceId,propertyId,value);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				parentActivity.stopDevicePropertyProgress();
				parentActivity.setPropertyResponseData();
			}
		});
		
			}
		}
	}
}

