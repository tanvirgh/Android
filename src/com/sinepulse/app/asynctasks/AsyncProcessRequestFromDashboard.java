package com.sinepulse.app.asynctasks;

/**
 * Interface for managing concurrent request queue.
 * @author tanvir.ahmed
 *
 */

public interface AsyncProcessRequestFromDashboard extends
		AsyncTaskInterface {
	   void startSendingTask();
//	void finishAddTask();
}
