/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.utils.CommonValues;

/**
 * Fetch device activities info from server based on search parameter through asynchronous call
 * @author tanvir.ahmed
 *
 */
public class AsyncGetLogFromRoomFragment extends AsyncTask<Void, Void, Boolean> {
//	RoomManagerFragment parentActivity;
	RoomManager parentActivity ;
	private int deviceId;
	public String fromsDate;
	public String tosDate;
	public int FilterType;
	public int PageNumber;
	public int ChunkSize;
	
	public AsyncGetLogFromRoomFragment(RoomManager roomManagerFragment, int deviceId,int FilterType,String fromsDate,String tosDate,int PageNumber,int ChunkSize) {
		this.parentActivity=roomManagerFragment;
		this.deviceId=deviceId;
		this.fromsDate=fromsDate;
		this.tosDate=tosDate;
		this.FilterType=FilterType;
		this.PageNumber=PageNumber;
		this.ChunkSize=ChunkSize;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startLogProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
//		deviceStatusFrg.sendGetDeviceRequest(userId);
		parentActivity.sendGetDeviceLogRequest(deviceId,FilterType,fromsDate,tosDate,PageNumber,ChunkSize);
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
//					if(CommonValues.getInstance().shouldSendLogReq==false){
//						parentActivity.refreshAdapter();
//					return;
//					}
					parentActivity.setupDeviceLogAdapter();
				}
			});
		
		}
	}
		}
	}
	}
}
	


