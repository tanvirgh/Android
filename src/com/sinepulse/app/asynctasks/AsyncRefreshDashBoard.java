/**
 * 
 */
package com.sinepulse.app.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.utils.CommonValues;

/**
 * Load updated dash board data from server asynchronously
 * @author tanvir.ahmed
 *
 */
public class AsyncRefreshDashBoard extends AsyncTask<Void, Void, Boolean> {

	Context parentActivity;

	public AsyncRefreshDashBoard(Context src) {
		this.parentActivity = src;
		
	}

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		try {
			// progress bar starts with the method call
			((Home) parentActivity).startDashBoardProgress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		((Home) parentActivity).sendGetSummaryRequest();
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		((Home) parentActivity).stopDashBoardProgress();
		
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (parentActivity != null) {
		((Home) parentActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Home) parentActivity).setDashBoardData();
			}
		});
		
		}
		}

	}
	}


}