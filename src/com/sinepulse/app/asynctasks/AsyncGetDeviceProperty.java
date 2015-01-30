/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDeviceProperty  extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	Home parentActivity;
	private int deviceId;
	private int deviceTypeId;
//	private int roomId;
	
	public AsyncGetDeviceProperty(Home _parentActivity, int deviceTypeId,int deviceId) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.deviceTypeId=deviceTypeId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendGetDevicePropertyRequest(deviceId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopDevicePropertyProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				parentActivity.setDevicePropertyControlData(deviceTypeId);
			}
		});
		
			}
		}
	}
	}
}
	

