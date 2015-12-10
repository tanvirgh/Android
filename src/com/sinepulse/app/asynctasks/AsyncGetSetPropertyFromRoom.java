/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.fragments.RoomManagerFragment;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetSetPropertyFromRoom extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment parentActivity;
	RoomManager parentActivity ;
	private int deviceId;
	private int userId;
	private int propertyId;
	private int value;
	
	public AsyncGetSetPropertyFromRoom(RoomManager _parentActivity,int userId,int deviceId,int propertyId,int value) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.userId=userId;
		this.propertyId=propertyId;
		this.value=value;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().IsServerConnectionError=false;
		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.setPropertyRequestFromRoom(userId,deviceId,propertyId,value);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(CommonValues.getInstance().IsServerConnectionError==true){
			CommonTask.ShowMessage(parentActivity, "Server Unreachable");
			return;
		}
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				parentActivity.stopDevicePropertyProgress();
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				parentActivity.setPropertyResponseData();
			}
		});
		
			}
		}
	}
}

