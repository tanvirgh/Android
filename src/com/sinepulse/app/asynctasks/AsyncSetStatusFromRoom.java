/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager_;
import com.sinepulse.app.adapters.DeviceListAdapterByRoom;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncSetStatusFromRoom extends AsyncTask<Void, Void, Boolean> {
	
//	DisplayDeviceDetails  deviceDetails ;
	DeviceListAdapterByRoom deviceDetails;
	Context parentActivity;
	private int deviceId;
	private int userId;
	private boolean isChecked;
	
	public AsyncSetStatusFromRoom(DeviceListAdapterByRoom deviceDetails,Context _parentActivity ,int deviceId,int userId,boolean isChecked) {
		this.deviceDetails=deviceDetails;
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.userId=userId;
		this.isChecked=isChecked;
		
	}
	

	@Override
	protected void onPreExecute() {
//		((RoomManager_) parentActivity).startDeviceProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceDetails.sendSetStatusRequest(userId,deviceId,isChecked);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (deviceDetails != null) {
				((RoomManager_) parentActivity).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						((RoomManager_) parentActivity).stopDeviceProgress();
						deviceDetails.setStatusResponseData();
					}
				});
		
			}
		}
	}
}
