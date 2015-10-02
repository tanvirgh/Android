/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonValues;

/**
 * Used to check local server availability through asynchronous call.
 * @author tanvir.ahmed
 *
 */
public class CheckMC extends AsyncTask<Void, Void, String> {
	private String mcUrl;
	Context parentActivity;
	boolean isSolvedLocal=false;

	public CheckMC(String mcUrl, Context  parentActivity,boolean isSolvedLocal) {
		this.mcUrl = mcUrl;
		this.parentActivity = parentActivity;
        this.isSolvedLocal=isSolvedLocal;
	}

	@Override
	protected String doInBackground(Void... params) {
//		parentActivity.startmenuProgress();
		((MainActionbarBase) parentActivity).sendMCStatusRequest(mcUrl);
		return CommonValues.getInstance().localIp;
	}

	@Override
	protected void onPostExecute(String result) {
//		parentActivity.stopmenuProgress();
		// bUserLogin.setEnabled(true);
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				if (isSolvedLocal == true) {
					// proceedLoginProcess();
					((MainActionbarBase) parentActivity).saveLocalIpInPreference();
					CommonValues.getInstance().connectionMode="Local";
//					Toast.makeText(parentActivity, "Local Mode", Toast.LENGTH_SHORT)
//							.show();
				} 
//				else {
//					Toast.makeText(parentActivity, "Internet Mode", Toast.LENGTH_SHORT)
//							.show();
//					CommonValues.getInstance().connectionMode="Internet";
//				if(result!=""){
//					if(CommonValues.getInstance().isAutologin){
//						if(CommonValues.getInstance().ApiKeyLocal==null || CommonValues.getInstance().ApiKeyLocal.equals("")){
//							CommonValues.getInstance().isAutologin=false;
//							((Activity) parentActivity).runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									Home.sendApiKeyReqAsync();
//								}
//							});
//							}
//						}
//				}
			}
				
			}
		}
	}
		
	

