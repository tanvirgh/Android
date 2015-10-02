/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;

/**
 * Fetch Curtain preset values from server for Home tab through asynchronous call
 * @author tanvir.ahmed
 *
 */
public class AsyncGetCurtainPresetValues extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	Home parentActivity;
	private int deviceId;
	
	public AsyncGetCurtainPresetValues(Home _parentActivity, int deviceId) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		
	}
	

	@Override
	protected void onPreExecute() {
		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendCurtainPresetRequest(deviceId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.stopDevicePropertyProgress();
				parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						parentActivity.setCurtainPresetResponseData();
					}
				});
		
			}
		}
	}
}
