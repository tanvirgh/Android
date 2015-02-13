/**
 * 
 */
package com.sinepulse.app.asynctasks;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.UserLogin_;
import com.sinepulse.app.activities.VideoActivity;
import com.sinepulse.app.utils.CommonValues;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author tanvir.ahmed
 *
 */
public class CheckMC extends AsyncTask<Void, Void, Boolean> {
	private String mcUrl;
	UserLogin_  parentActivity;
	boolean isSolvedLocal=false;

	public CheckMC(String mcUrl, UserLogin_  parentActivity,boolean isSolvedLocal) {
		this.mcUrl = mcUrl;
		this.parentActivity = parentActivity;
        this.isSolvedLocal=isSolvedLocal;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendMCStatusRequest(mcUrl);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// bUserLogin.setEnabled(true);
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

