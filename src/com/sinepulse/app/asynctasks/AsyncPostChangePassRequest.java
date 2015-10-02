/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sinepulse.app.activities.ChangePasswordActivity;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

/**
 * Send password change request with required parameters to server asynchronously.
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
		parentActivity.sendChangePassReq(currentPass,newPass);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopProgress();
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			parentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (parentActivity != null) {
						if(CommonValues.getInstance().isPasswordChanged==true){
							parentActivity.resetChangePassWindow();
						}else{
							CommonTask.ShowMessage(parentActivity, "Password Change Failed.");
							if(CommonValues.getInstance().IsServerConnectionError){
								CommonTask.ShowAlertMessage(parentActivity,
										CommonValues.getInstance().alertObj);
						}
						
						}
						
			}
					
				}
			});
			
		}
	}
}
