/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.fragments.RoomManagerFragment;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetCurtainPresetValuesFromRoom extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment parentActivity;
	RoomManager parentActivity ;
	public int userId;
	private int deviceId;
	
	public AsyncGetCurtainPresetValuesFromRoom(RoomManager _parentActivity, int userId,int deviceId) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.userId=userId;
		
	}
	

	@Override
	protected void onPreExecute() {
//		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendCurtainPresetRequest(userId,deviceId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
parentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						parentActivity.setCurtainPresetResponseData();
					}
				});
			}
		}
	}
}
