/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncLogOutTask extends AsyncTask<Void, Void, Boolean> {
	
	Context parentActivity;
	private int userId;
	ProgressDialog progress ;
	
	public AsyncLogOutTask(Context _parentActivity, int userId) {
		this.parentActivity=_parentActivity;
		this.userId=userId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
//		parentActivity.startNavDrawerProgress();
		progress = new ProgressDialog(parentActivity);
		progress.setTitle("Please wait !");
		progress.setMessage("Logging out...");
		progress.setCancelable(true);
		progress.show();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		((Home) parentActivity).sendLogOutRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		progress.dismiss();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		
//		parentActivity.stopNavDrawerProgress();
	   ((Home) parentActivity).redirectToLogInPage();
	}
		}
	}
	}
}
