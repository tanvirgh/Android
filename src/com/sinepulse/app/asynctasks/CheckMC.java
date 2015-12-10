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
	UserLogin_ parentActivity;
	boolean isSolvedLocal = false;
	private String hostName;
	private String IP;

	public CheckMC(String mcUrl, UserLogin_ parentActivity,
			boolean isSolvedLocal, String hostName,String IP) {
		this.mcUrl = mcUrl;
		this.parentActivity = parentActivity;
		this.isSolvedLocal = isSolvedLocal;
		this.hostName = hostName;
		this.IP=IP;
	}

	@Override
	protected void onPreExecute() {
//		parentActivity.startmenuProgress();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if(mcUrl!=null && mcUrl!=""){
		parentActivity.sendMCStatusRequest(mcUrl, hostName,IP);
		
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
//		parentActivity.stopmenuProgress();
		// bUserLogin.setEnabled(true);
		if (isSolvedLocal == true) {
			// proceedLoginProcess();
//			parentActivity.saveLocalIpInPreference();
			CommonValues.getInstance().connectionMode = "Local";
			Toast.makeText(parentActivity, "Local Mode", Toast.LENGTH_SHORT)
					.show();
		} else {
//			 Toast.makeText(parentActivity, "Local Server Unreachable",
//			 Toast.LENGTH_SHORT)
//			 .show();
//			 CommonValues.getInstance().connectionMode="Internet";
		}

	}

}
