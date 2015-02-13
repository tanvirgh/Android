/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sinepulse.app.activities.HelpActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetAllTickets extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	HelpActivity parentActivity;
	public int userId;
	
	public AsyncGetAllTickets(HelpActivity parent, int userId) {
		this.parentActivity=parent;
		this.userId=userId;
		
	}
	


	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startProgress();
		
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
					}
				});
		
			}
		}
	}
	}
}

