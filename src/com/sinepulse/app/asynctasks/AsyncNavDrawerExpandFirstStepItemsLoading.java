package com.sinepulse.app.asynctasks;

import android.os.AsyncTask;

import com.sinepulse.app.adapters.NavDrawerListAdapter;
import com.sinepulse.app.utils.CommonValues;

public class AsyncNavDrawerExpandFirstStepItemsLoading extends AsyncTask<Void, Void, Boolean> {
	NavDrawerListAdapter navDrawerListAdapter;
	

	public AsyncNavDrawerExpandFirstStepItemsLoading(NavDrawerListAdapter src) {
		navDrawerListAdapter = src;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
			CommonValues.getInstance().IsServerConnectionError = false;
			//load familyitems in familyInq object
//			navDrawerListAdapter.loadListView();
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
//				navDrawerListAdapter.viewListView(navDrawerListAdapter);
//		navDrawerListAdapter.loadListView();

		}
	
}

