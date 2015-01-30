package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.R;
import com.sinepulse.app.activities.UserLogin;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury 
 * use for saving users information from home tab
 *  Asynchronously
 */

public class AsyncCheckServerStateAndSaveServerInfo extends AsyncTask<Void, Void, Boolean> {

	UserLogin saveLogInData;
	String Apptoken;

	public AsyncCheckServerStateAndSaveServerInfo(UserLogin src,String AppToken) {
		saveLogInData = src;
		Apptoken=AppToken;
	}

	@Override
	protected void onPreExecute() {
		try {
			// progress bar starts with the method call
			saveLogInData.startmenuProgress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		return performLogin();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		saveLogInData.stopmenuProgress();
		if (!CommonValues.getInstance().IsServerConnectionError) {
			saveLogInData.setUserInfoAfterSave();
		} else {
			CommonTask.ShowMessage(saveLogInData,
					saveLogInData.getString(R.string.wronguserpass));
		}

	}

	private boolean performLogin() {
		
		
		// save LogIn screen information in shared preference
		if (saveLogInData.isConnectedToServer(Apptoken)) {
			saveLogInData.saveUsernameAndPassword();
		}else{
			CommonValues.getInstance().IsServerConnectionError=true;
		}
			
		return true;
	}

}
