/**
 * 
 */
package com.sinepulse.app.asynctasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.Home_;
import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.activities.RoomManager_;
import com.sinepulse.app.activities.UserLogActivity;
import com.sinepulse.app.activities.UserProfileActivity;
import com.sinepulse.app.activities.VideoActivity;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;

/**
 * @author tanvir.ahmed
 * 
 */
public class AsyncSendApiKeyRequest extends AsyncTask<Void, Void, String> {
	Context callerActivity;
	int deviceTypeId;
	SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d = new Date();
	int FilterType = 1;
	String fromsDate = formatter.format(d).toString();
	String tosDate = formatter.format(d).toString();
	int PageNumber = 1;

	public AsyncSendApiKeyRequest(Context parentActivity) {
		this.callerActivity = parentActivity;

	}

	@Override
	protected void onPreExecute() {
	/*	if (callerActivity instanceof Home) {
			Home.startDeviceProgress();
		}
		if (callerActivity instanceof RoomManager) {
			RoomManager.startProgress();
		}*/
	};

	@Override
	protected String doInBackground(Void... params) {
		CommonTask.sendApiKeyRequest();
		return CommonValues.getInstance().ApiKey;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != "") {
			MainActionbarBase.setConnectionNodeImage(
					CommonValues.getInstance().globalMenu, callerActivity);
			// ArrayList<String> runningactivities = checkActivityVisibility();
			// if (runningactivities
			// .contains("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.Home_}")
			// == true) {
			Activity activity = (Activity) callerActivity;
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (callerActivity instanceof Home) {
//						Home.stopDeviceProgress();
						if (Home_.vfDeviceType != null
								&& Home.vfDeviceType.getDisplayedChild() == 0) {
							Home.refreshDashboarddata();
						}
						if (Home_.vfDeviceType != null
								&& Home.vfDeviceType.getDisplayedChild() == 1) {
							Home.LoadDeviceDetailsContent(CommonValues.getInstance().deviceTypeId);
						}
						if (Home_.vfDeviceType != null
								&& Home.vfDeviceType.getDisplayedChild() == 2) {
							Home.loadDeviceProperty(
									CommonValues.getInstance().deviceTypeId,
									CommonValues.getInstance().deviceIdFromHome);
						}
						if (Home_.vfDeviceType != null
								&& Home.vfDeviceType.getDisplayedChild() == 3) {

							Home.LoadDeviceLogContent(
									CommonValues.getInstance().deviceIdFromHome,
									FilterType, fromsDate, tosDate, PageNumber, 30);
						}

					}
					if (callerActivity instanceof RoomManager) {
//						RoomManager.stopProgress();
						if (RoomManager_.vfRoom != null
								&& RoomManager.vfRoom.getDisplayedChild() == 0) {
							RoomManager_.loadRoomInfoAsync();
						}
						if (RoomManager_.vfRoom != null
								&& RoomManager.vfRoom.getDisplayedChild() == 1) {
							RoomManager_.LoadRoomDetailsContent(CommonValues
									.getInstance().roomId);
						}
						if (RoomManager_.vfRoom != null
								&& RoomManager.vfRoom.getDisplayedChild() == 2) {
							RoomManager_.loadDeviceProperty(
									CommonValues.getInstance().deviceTypeIdRoom,
									CommonValues.getInstance().deviceIdRoom);
						}
						if (RoomManager_.vfRoom != null
								&& RoomManager.vfRoom.getDisplayedChild() == 3) {
							RoomManager_.LoadDeviceLogContent(
									CommonValues.getInstance().deviceIdRoom,
									FilterType, fromsDate, tosDate, PageNumber, 30);
						}
					}
					if (callerActivity instanceof UserProfileActivity) {
						UserProfileActivity.loadUserInformation();
					}
					if (callerActivity instanceof VideoActivity) {
						VideoActivity.loadCameraInfoAsync();
					}
					if (callerActivity instanceof UserLogActivity) {
						UserLogActivity.loadUserLogInfo(FilterType, fromsDate, tosDate, PageNumber, 30);
					}
					
				}
			});
			
		} else {
			if (CommonValues.getInstance().IsServerConnectionError) {
				CommonTask.ShowAlertMessage(callerActivity,
						CommonValues.getInstance().alertObj);
			}
		}

	}

	/*private ArrayList<String> checkActivityVisibility() {
		ArrayList<String> runningactivities = new ArrayList<String>();

		ActivityManager activityManager = (ActivityManager) ((ContextWrapper) callerActivity)
				.getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> services = activityManager
				.getRunningTasks(Integer.MAX_VALUE);

		for (int i1 = 0; i1 < services.size(); i1++) {
			runningactivities.add(0, services.get(i1).topActivity.toString());
		}
		return runningactivities;
	}*/

}
