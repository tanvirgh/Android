/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.SupportActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * Load ticket detials information from server asynchronously.
 * @author tanvir.ahmed
 *
 */
public class AsyncLoadTicketDetails extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	SupportActivity parentActivity;
	public int userId;
	public int ticketId;
	
	public AsyncLoadTicketDetails(SupportActivity parent, int userId,int ticketId) {
		this.parentActivity=parent;
		this.userId=userId;
		this.ticketId=ticketId;
		
	}
	


	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startSingleTktProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.loadTicketDetails(userId,ticketId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopSingleTktProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						parentActivity.setSingleTicketData();
					}
				});
		
			}
		}
	}
	}
}


