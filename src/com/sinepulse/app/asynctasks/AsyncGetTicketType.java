/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.SupportActivity;

/**
 * Fetch the ticket types asynchronously from server
 * @author tanvir.ahmed
 *
 */
public class AsyncGetTicketType extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	SupportActivity parentActivity;
	public int userId;
	
	public AsyncGetTicketType(SupportActivity parent, int userId) {
		this.parentActivity=parent;
		this.userId=userId;
		
	}
	


	@Override
	protected void onPreExecute() {
//		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendGetTicketTypeRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
//		parentActivity.stopDevicePropertyProgress();
				parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						parentActivity.setTIcketTypeResponseData();
					}
				});
		
			}
		}
	}
}

