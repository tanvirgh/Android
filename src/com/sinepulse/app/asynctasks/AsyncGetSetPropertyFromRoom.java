/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.activities.RoomManager;

/**
 * @author tanvir.ahmed
 * 
 */
public class AsyncGetSetPropertyFromRoom extends AsyncTask<Void, Void, Boolean> {

	// RoomManagerFragment parentActivity;
	RoomManager parentActivity;
	private int deviceId;
	// private int userId;
	private int propertyId;
	private int value;

	public AsyncGetSetPropertyFromRoom(RoomManager _parentActivity,
			int deviceId, int propertyId, int value) {
		this.parentActivity = _parentActivity;
		this.deviceId = deviceId;
		this.propertyId = propertyId;
		this.value = value;

	}

	@Override
	protected void onPreExecute() {
		parentActivity.startDevicePropertyProgress();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		parentActivity.setPropertyRequestFromRoom(deviceId, propertyId, value);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		parentActivity.shouldResend = false;
		parentActivity.circularSeekBar1.setEnabled(true);
		parentActivity.circularSeekBar1.setClickable(true);
		parentActivity.stopDevicePropertyProgress();
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
				parentActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						parentActivity.setDevicePropertyData();

					}
				});

			}
		}
	}
}
