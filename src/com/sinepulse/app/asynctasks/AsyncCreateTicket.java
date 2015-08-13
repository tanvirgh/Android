/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sinepulse.app.activities.SupportActivity;

/**
 * Send the ticket details (created by user) to server asynchronously
 * @author tanvir.ahmed
 *
 */
public class AsyncCreateTicket extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	SupportActivity parentActivity;
	public int userId;
	public String Subject;
	public String Details;
	public int TicketTypeId;
	
	public AsyncCreateTicket(SupportActivity parent, int userId,String Subject,String Details,Integer TicketTypeId) {
		this.parentActivity=parent;
		this.userId=userId;
		this.Subject=Subject;
		this.Details=Details;
		this.TicketTypeId=TicketTypeId;
		
	}
	


	@Override
	protected void onPreExecute() {
		parentActivity.startCreateTktProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendCreateTicketRequest(userId,Subject,Details,TicketTypeId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.stopCreateTktProgress();
				parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						parentActivity.resetCreateTicketWindow();
						Toast.makeText(parentActivity, "Ticket Created Successfully", Toast.LENGTH_SHORT).show();
					}
				});
		
			}
		}
	}
}

