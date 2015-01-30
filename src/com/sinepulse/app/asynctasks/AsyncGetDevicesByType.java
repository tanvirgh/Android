/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.DisplayDeviceDetails;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDevicesByType extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
    Context parentActivity;
	private int deviceType;
//	private int roomId;
	
	public AsyncGetDevicesByType(Context _parentActivity, int deviceType) {
//		deviceDetails = deviceDetailsFragment;
		this.parentActivity=_parentActivity;
		this.deviceType=deviceType;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		((Home) parentActivity).startDeviceProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		((Home) parentActivity).sendGetDeviceByTypeRequest(deviceType);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		((Home) parentActivity).stopDeviceProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
		if (parentActivity != null) {
		((Activity) parentActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Home) parentActivity).setupDeviceByTypeListViewAdapter(deviceType);
			}
		});
		
			}
		}
	}
	}
}
	
