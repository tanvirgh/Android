/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;
import com.sinepulse.app.activities.SupportActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetAllTickets extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	SupportActivity parentActivity;
	public int userId;
	
	public AsyncGetAllTickets(SupportActivity parent, int userId) {
		this.parentActivity=parent;
		this.userId=userId;
		
	}
	


	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startProgress();
//		parentActivity.refresh.setEnabled(false);
		
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendGetAllTicketRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopProgress();
		
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						parentActivity.setAllTicketListViewAdapter();
//						parentActivity.refresh.setEnabled(true);
					}
				});
		
			}
		}
	}
	}
}

