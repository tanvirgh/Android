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
public class AsyncLogOutTask extends AsyncTask<Void, Void, Boolean> {
	
	Home parentActivity;
	private int userId;
	
	public AsyncLogOutTask(Home _parentActivity, int userId) {
		this.parentActivity=_parentActivity;
		this.userId=userId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
//		parentActivity.startNavDrawerProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendLogOutRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
//		parentActivity.stopNavDrawerProgress();
	   parentActivity.redirectToLogInPage();
	}
		}
	}
	}
}
