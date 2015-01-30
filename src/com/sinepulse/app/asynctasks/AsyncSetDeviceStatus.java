/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncSetDeviceStatus extends AsyncTask<Void, Void, Boolean> {
	AsyncProcessRequestFromDashboard mListener;

	
	public AsyncSetDeviceStatus(AsyncProcessRequestFromDashboard listener) {
		mListener  = listener;
	}
	

	@Override
	protected void onPreExecute() {
		mListener.onTaskPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		
		mListener.startSendingTask();
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mListener.onTaskPostExecute(result);
	}
}
