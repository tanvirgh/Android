/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.VideoActivity;
import com.sinepulse.app.utils.CommonValues;

/**
 * fetch camera related information from server through asynchronous call
 * @author tanvir.ahmed
 *
 */
public class AsyncGetCameraInfo extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment  roomManagerFrg ;
	VideoActivity  parentActivity ;
	
//	private int logedInUserId;
	
	public AsyncGetCameraInfo(VideoActivity parentActivity) {
		this.parentActivity = parentActivity;
//		this.logedInUserId=logedInUserId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		parentActivity.startProgress();
		parentActivity.streamingButton.setEnabled(false);
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		
		parentActivity.sendGetCameraInfoRequest();
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.stopProgress();
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
		parentActivity.streamingButton.setEnabled(true);
	}
	}
	

}
