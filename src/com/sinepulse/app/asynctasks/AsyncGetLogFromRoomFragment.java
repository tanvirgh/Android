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
public class AsyncGetLogFromRoomFragment extends AsyncTask<Void, Void, Boolean> {
//	RoomManagerFragment parentActivity;
	RoomManager parentActivity ;
	private int deviceId;
	public String fromDate;
	public String toDate;
	public int FilterType;
	
	public AsyncGetLogFromRoomFragment(RoomManager roomManagerFragment, int deviceId,int FilterType,String fromDate,String toDate) {
		this.parentActivity=roomManagerFragment;
		this.deviceId=deviceId;
		this.fromDate=fromDate;
		this.toDate=toDate;
		this.FilterType=FilterType;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startLogProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendGetDeviceLogRequest(deviceId,FilterType,fromDate,toDate);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopLogProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		if(CommonValues.getInstance().deviceLogDetailList!=null){
			parentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					parentActivity.setupDeviceLogAdapter();
				}
			});
		
		}
	}
		}
	}
	}
}
	


