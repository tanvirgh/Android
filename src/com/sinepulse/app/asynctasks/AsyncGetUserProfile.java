/**
 * 
 */
package com.sinepulse.app.asynctasks;

import com.sinepulse.app.activities.UserProfileActivity;
import com.sinepulse.app.utils.CommonValues;

import android.os.AsyncTask;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetUserProfile extends AsyncTask<Void, Void, Boolean> {
	
	UserProfileActivity  userProfileFrg ;
	private int userId;
	
	public AsyncGetUserProfile(UserProfileActivity userProfileFragment, int userId) {
		userProfileFrg = userProfileFragment;
		this.userId=userId;
		
	}
	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		userProfileFrg.startProgress();
		
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		userProfileFrg.sendGetUserProfileRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		userProfileFrg.stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
			android.os.AsyncTask.Status status = getStatus();
			if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
		if (userProfileFrg!=null) {
			userProfileFrg.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					userProfileFrg.setUserInformation();
				}
			});
			
			
		}
			}
		
	}
	}

}
