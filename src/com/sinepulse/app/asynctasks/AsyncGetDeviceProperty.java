/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.utils.CommonValues;

/**
 * Fetch device properties information from server  through asynchronous call
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDeviceProperty  extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	Context parentActivity;
	private int deviceId;
	private int deviceTypeId;
//	private int roomId;
	
	public AsyncGetDeviceProperty(Context _parentActivity, int deviceTypeId,int deviceId) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.deviceTypeId=deviceTypeId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		((Home) parentActivity).startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		((Home) parentActivity).sendGetDevicePropertyRequest(deviceId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		((Home) parentActivity).stopDevicePropertyProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		((Activity) parentActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Home) parentActivity).setDevicePropertyControlData(deviceTypeId);
			}
		});
		
			}
		}
	}
	}
}
	

