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
public class AsyncGetDeviceLogInfo extends AsyncTask<Void, Void, Boolean> {
	
	Home parentActivity;
	private int deviceId;
	public String fromDate;
	public String toDate;
	public int FilterType;
	
	public AsyncGetDeviceLogInfo(Home _parentActivity, int deviceId,int FilterType,String fromDate,String toDate) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.fromDate=fromDate;
		this.toDate=toDate;
		this.FilterType=FilterType;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startLogProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendGetDeviceLogRequest(deviceId,FilterType,fromDate,toDate);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopLogProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				parentActivity.setupDeviceLogAdapter();
			}
		});
		
	}
		}
	}
	}
}
	

