/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonValues;

/**
 * Send log out request to server asynchronously.
 * @author tanvir.ahmed
 *
 */
public class AsyncLogOutTask extends AsyncTask<Void, Void, Boolean> {
	
	Home parentActivity;
	private int userId;
	ProgressDialog progress ;
	
	public AsyncLogOutTask(Home _parentActivity, int userId) {
		this.parentActivity=_parentActivity;
		this.userId=userId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
//		parentActivity.startNavDrawerProgress();
		progress = new ProgressDialog(parentActivity);
		progress.setTitle("Please wait");
		progress.setMessage("Logging out...");
		progress.setCancelable(true);
		progress.show();

		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendLogOutRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		progress.dismiss();
//		super.onPostExecute(result);
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
