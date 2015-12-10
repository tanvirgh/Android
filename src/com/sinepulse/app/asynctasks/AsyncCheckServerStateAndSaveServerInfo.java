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
	boolean isloginworkDone;

	public AsyncCheckServerStateAndSaveServerInfo(UserLogin src,String AppToken,boolean isloginworkDone) {
		saveLogInData = src;
		Apptoken=AppToken;
		this.isloginworkDone=isloginworkDone;
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
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (saveLogInData != null) {
		if (!CommonValues.getInstance().IsServerConnectionError) {
			saveLogInData.setUserInfoAfterSave(isloginworkDone);
		} else {
			if(CommonValues.getInstance().loginError!=null && CommonValues.getInstance().loginError!=""){
			CommonTask.ShowMessage(saveLogInData,
					CommonValues.getInstance().loginError);
			}else{
				CommonTask.ShowMessage(saveLogInData,
						"No Data Returned From Server");
			}
		}
			}
		}

	}

	private boolean performLogin() {
		
		
		// save LogIn screen information in shared preference
		if (saveLogInData.isConnectedToServer(Apptoken)) {
			isloginworkDone=true;
			saveLogInData.saveUsernameAndPassword();
		}else{
			isloginworkDone=false;
			CommonValues.getInstance().IsServerConnectionError=true;
		}
			
		return true;
	}

}
