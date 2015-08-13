package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.utils.CommonValues;

/**
 * Get all Room list from server  through asynchronous call.
 * @author tanvir.ahmed
 *
 */

public class AsyncGetRoom extends AsyncTask<Void, Void, Boolean> {
	
//	RoomManagerFragment  roomManagerFrg ;
	RoomManager  roomManagerFrg ;
	
	private int userId;
	
	public AsyncGetRoom(RoomManager roomManagerFragment, int userId) {
		roomManagerFrg = roomManagerFragment;
		this.userId=userId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		roomManagerFrg.startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		
		roomManagerFrg.sendGetRoomRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		roomManagerFrg.stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (roomManagerFrg != null  ) {
		roomManagerFrg.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				roomManagerFrg.setupRoomListViewAdapter();
			}
		});
		
			}
		}
	}
	}
	

}
