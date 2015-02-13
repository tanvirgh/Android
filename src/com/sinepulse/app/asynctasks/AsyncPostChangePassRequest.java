/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sinepulse.app.activities.ChangePasswordActivity;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncPostChangePassRequest extends AsyncTask<Void, Void, Boolean> {
	
	ChangePasswordActivity parentActivity;
	private String currentPass;
	private String newPass;
	
	public AsyncPostChangePassRequest(ChangePasswordActivity _parentActivity, String currentPass,String newPass) {
		this.parentActivity=_parentActivity;
		this.currentPass=currentPass;
		this.newPass=newPass;
		
	}
	

	@Override
	protected void onPreExecute() {
		parentActivity.startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendChamgePassReq(currentPass,newPass);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		        parentActivity.stopProgress();
				parentActivity.resetChangePassWindow();
				Toast.makeText(parentActivity, "Password Change Successful", Toast.LENGTH_SHORT).show();
	}
		}
	}
}
