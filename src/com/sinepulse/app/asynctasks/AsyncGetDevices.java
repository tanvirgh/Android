/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.fragments.RoomManagerFragment;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author Tanvir
 *
 */
public class AsyncGetDevices extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManager parentActivity;
	Context parentActivity;
	private int userId;
	private int roomId;
	
	public AsyncGetDevices(Context _parentActivity, int userId,int roomId) {
		this.parentActivity=_parentActivity;
		this.userId=userId;
		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		((RoomManager) parentActivity).startDeviceProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		((RoomManager) parentActivity).sendGetDeviceRequest(userId,roomId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		((RoomManager) parentActivity).stopDeviceProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		((Activity) parentActivity).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				((RoomManager) parentActivity).setupDeviceListViewAdapter();
			}
		});
		
	}
		}
	}
	}
}
	
