package com.sinepulse.app.asynctasks;

/**
 *  Interface for managing concurrent request queue.
 * @author tanvir.ahmed
 *
 */

public interface AsyncTaskInterface {
	void onTaskPreExecute();
	void onDoInBackground();
	void onTaskPostExecute(Object result);	
}
