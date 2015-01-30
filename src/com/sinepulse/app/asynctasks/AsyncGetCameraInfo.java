/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.activities.VideoActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 *
 */
public class AsyncGetCameraInfo extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment  roomManagerFrg ;
	VideoActivity  parentActivity ;
	
	private int logedInUserId;
	
	public AsyncGetCameraInfo(VideoActivity parentActivity, int logedInUserId) {
		this.parentActivity = parentActivity;
		this.logedInUserId=logedInUserId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
//		parentActivity.startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		
		parentActivity.sendGetCameraInfoRequest(logedInUserId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
//		parentActivity.stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null  ) {
				parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				parentActivity.setCameraInfo();
			}
		});
		
			}
		}
	}
	}
	

}
