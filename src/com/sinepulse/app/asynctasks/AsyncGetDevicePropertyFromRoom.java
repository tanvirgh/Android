/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDevicePropertyFromRoom extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment parentActivity;
	RoomManager  parentActivity ;
	private int deviceId;
	private int deviceTypeId;
//	private int roomId;
	
	public AsyncGetDevicePropertyFromRoom(RoomManager _parentActivity, int deviceTypeId,int deviceId) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.deviceTypeId=deviceTypeId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startDevicePropertyProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.sendGetDevicePropertyRequest(deviceId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopDevicePropertyProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				parentActivity.setDevicePropertyControlData(deviceTypeId);
			}
		});
		
			}
		}
	}
	}
}
	
