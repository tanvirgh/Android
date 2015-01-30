package com.sinepulse.app.asynctasks;

public interface AsyncTaskInterface {
	void onTaskPreExecute();
	void onDoInBackground();
	void onTaskPostExecute(Object result);	
}
