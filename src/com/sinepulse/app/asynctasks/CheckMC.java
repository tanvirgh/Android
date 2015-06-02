/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class CheckMC extends AsyncTask<Void, Void, Boolean> {
	private String mcUrl;
	MainActionbarBase parentActivity;
	boolean isSolvedLocal=false;

	public CheckMC(String mcUrl, MainActionbarBase  parentActivity,boolean isSolvedLocal) {
		this.mcUrl = mcUrl;
		this.parentActivity = parentActivity;
        this.isSolvedLocal=isSolvedLocal;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
//		parentActivity.startmenuProgress();
		parentActivity.sendMCStatusRequest(mcUrl);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
//		parentActivity.stopmenuProgress();
		// bUserLogin.setEnabled(true);
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				if (isSolvedLocal == true) {
					// proceedLoginProcess();
					parentActivity.saveLocalIpInPreference();
					CommonValues.getInstance().connectionMode="Local";
//					Toast.makeText(parentActivity, "Local Mode", Toast.LENGTH_SHORT)
//							.show();
				} else {
//					Toast.makeText(parentActivity, "Internet Mode", Toast.LENGTH_SHORT)
//							.show();
//					CommonValues.getInstance().connectionMode="Internet";
				}
				
			}
		}
	}
		
	}

