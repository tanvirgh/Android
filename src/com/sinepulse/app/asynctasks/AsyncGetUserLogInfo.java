/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.UserLogActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * used to fetch user activities from server based on search parameter asynchronously.
 * @author tanvir.ahmed
 *
 */
public class AsyncGetUserLogInfo extends AsyncTask<Void, Void, Boolean> {
	
	UserLogActivity parentActivity;
	
	public String fromDate;
	public String toDate;
	public int FilterType;
	public int PageNumber;
	public int ChunkSize;
//	private boolean interrupttask=false;
	
	public AsyncGetUserLogInfo(UserLogActivity _parentActivity,int FilterType,String fromDate,String toDate,int PageNumber,int ChunkSize) {
		this.parentActivity=_parentActivity;
		this.fromDate=fromDate;
		this.toDate=toDate;
		this.FilterType=FilterType;
		this.PageNumber=PageNumber;
		this.ChunkSize=ChunkSize;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
	/*	while (interrupttask==true) {
		      if (isCancelled()) break;
		    }*/
		 parentActivity.sendGetUserLogRequest(FilterType,fromDate,toDate,PageNumber,ChunkSize);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				if(CommonValues.getInstance().deviceLogDetailList!=null){
	       parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
//				if(CommonValues.getInstance().shouldSendLogReq==false){
//					((UserLogActivity) parentActivity).refreshAdapter();
//				interrupttask=true;
//					return;
//				}
			parentActivity.setupUserLogAdapter();
			}
		});
		
	}
			}
		}
	}
	}
}
	

