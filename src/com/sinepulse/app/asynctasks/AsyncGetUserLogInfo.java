/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.UserLogActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetUserLogInfo extends AsyncTask<Void, Void, Boolean> {
	
	UserLogActivity parentActivity;
	private int userId;
	public String fromDate;
	public String toDate;
	public int FilterType;
	
	public AsyncGetUserLogInfo(UserLogActivity userLogFragment, int userId,int FilterType,String fromDate,String toDate) {
		this.parentActivity=userLogFragment;
		this.userId=userId;
		this.fromDate=fromDate;
		this.toDate=toDate;
		this.FilterType=FilterType;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendGetUserLogRequest(userId,FilterType,fromDate,toDate);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				parentActivity.setupUserLogAdapter();
			}
		});
		
	}
		}
	}
	}
}
	

