package com.sinepulse.app.asynctasks;

import android.app.Activity;
import android.content.Context;
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
	Context  roomManagerFrg ;
	
	private int userId;
	
	public AsyncGetRoom(Context roomManagerFragment, int userId) {
		roomManagerFrg = roomManagerFragment;
		this.userId=userId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		((RoomManager) roomManagerFrg).startProgress();
		
	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		
		((RoomManager) roomManagerFrg).sendGetRoomRequest(userId);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		((RoomManager) roomManagerFrg).stopProgress();
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (roomManagerFrg != null  ) {
		((Activity) roomManagerFrg).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				((RoomManager) roomManagerFrg).setupRoomListViewAdapter();
			}
		});
		
			}
		}
	}
	}
	

}
