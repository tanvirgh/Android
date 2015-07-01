/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.UserLogActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDeviceLogInfo extends AsyncTask<Void, Void, Boolean> {
	
	Home parentActivity;
	private int deviceId;
	public String fromDate;
	public String toDate;
	public int FilterType;
	public int PageNumber;
	public int ChunkSize;
	
	public AsyncGetDeviceLogInfo(Home _parentActivity, int deviceId,int FilterType,String fromDate,String toDate,int PageNumber,int ChunkSize) {
		this.parentActivity=_parentActivity;
		this.deviceId=deviceId;
		this.fromDate=fromDate;
		this.toDate=toDate;
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
		parentActivity.sendGetDeviceLogRequest(deviceId,FilterType,fromDate,toDate,PageNumber,ChunkSize);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopLogProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(CommonValues.getInstance().shouldSendLogReq==false){
					parentActivity.refreshAdapter();
					return;
				}
				parentActivity.setupDeviceLogAdapter();
			}
		});
		
	}
		}
	}
	}
}
	

