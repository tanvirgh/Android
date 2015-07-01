/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinepulse.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sinepulse.app.R;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.utils.CommonValues;

/**
 * IntentService responsible for handling GCM messages.
 * @author tac
 */
public class GCMIntentService extends IntentService {

	String TAG = "Tanvir GCM Messsage";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	JSONObject jsonObj = null;

	public GCMIntentService() {
		super("GCMIntentService");
	}
	@Override
	  public void onStart(Intent intent, int startId) {
	    if (intent != null) {
	      super.onStart(intent, startId);
	    }
	  }

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Bundle extras = intent.getExtras();
		 String from=extras.getString("from");
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		// Log.d(TAG, "onHandledIntent");
		// Log.d("ExtraBundle",extras.toString());
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty() && extras!=null) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
//				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
			/*	sendNotification("Deleted messages on server: "
						+ extras.toString());*/
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i = 0; i < 5; i++) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
				// Post notification of received message.
				 if(from.equals("google.com/iid"))
                 {
                     //related to google ... DO NOT PERFORM ANY ACTION
                 }
                 else { 
				String action="";
//				JSONObject jsonObj=null;
				try {
					jsonObj = new JSONObject(
							extras.getString("header"));
					
					if(jsonObj!=null){
					 action = jsonObj.getString("Action");
					}
					if (action.equals("DeviceStatus")) {

						if (getCurrentVisibleView("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.RoomManager_}")) {
							if (RoomManager_.vfRoom!=null &&RoomManager.vfRoom.getDisplayedChild() == 1) {
								Device device = parseGCMDevice(extras);
								int index = -1;
								for (int i = 0; i < CommonValues.getInstance().deviceList
										.size(); i++) {
									if (CommonValues.getInstance().deviceList
											.get(i).Id == device.Id) {
										index = i;
										break;
									}
								}
								if (index != -1) {
									CommonValues.getInstance().deviceList
											.set(index,
													CommonValues.getInstance().modifiedDeviceStatus);
									CommonValues.getInstance().roomManager
											.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if(CommonValues.getInstance().connectionMode.equals("Internet")){
													CommonValues.getInstance().roomManager.dAdapter
															.refreshAdapter();
													}
												}
											});
								}
							}
						}
						if (getCurrentVisibleView("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.Home_}")) {
							if (Home_.vfDeviceType!=null &&Home.vfDeviceType.getDisplayedChild() == 1) {
								Device device = parseGCMDevice(extras);
								int index = -1;
								for (int i = 0; i < CommonValues.getInstance().deviceList
										.size(); i++) {
									if (CommonValues.getInstance().deviceList
											.get(i).Id == device.Id) {
										index = i;
										break;
									}
								}
								if (index != -1) {
									CommonValues.getInstance().deviceList
											.set(index,
													CommonValues.getInstance().modifiedDeviceStatus);

									CommonValues.getInstance().home
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													if(CommonValues.getInstance().connectionMode.equals("Internet")){
													CommonValues.getInstance().home.dtAdapter
															.refreshAdapter();
													}

												}
											});

								}
							}
						}
					}
					if (action.equals("DeviceProperties")) {
						if (getCurrentVisibleView("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.RoomManager_}")) {
							if (RoomManager_.vfRoom!=null &&RoomManager.vfRoom.getDisplayedChild() == 2) {
								Integer pushDeviceId = jsonObj.getInt("Id");
								ArrayList<DeviceProperty> devicePropertyValues = parseGCMDeviceProperty(extras);
								if (pushDeviceId == CommonValues.getInstance().roomManager.deviceId) {
									CommonValues.getInstance().devicePropertyList = devicePropertyValues;
									CommonValues.getInstance().roomManager
											.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if(CommonValues.getInstance().connectionMode.equals("Internet")){
													CommonValues.getInstance().roomManager
															.setDevicePropertyData();
													}
												}
											});
								}
							}
						}
						if (getCurrentVisibleView("ComponentInfo{com.sinepulse.app/com.sinepulse.app.activities.Home_}")) {
							if (Home_.vfDeviceType!=null &&Home.vfDeviceType.getDisplayedChild() == 2) {
								Integer pushDeviceId = jsonObj.getInt("Id");
								ArrayList<DeviceProperty> devicePropertyValues = parseGCMDeviceProperty(extras);
								if (pushDeviceId == CommonValues.getInstance().home.deviceId) {
									CommonValues.getInstance().devicePropertyList = devicePropertyValues;
									CommonValues.getInstance().home
											.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if(CommonValues.getInstance().connectionMode.equals("Internet")){
													CommonValues.getInstance().home
															.setPropertyResponseData();
													}
												}
											});
								}
							}
						}

					}
//					sendNotification("Received: " + extras.getString("header"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * @param extras
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<DeviceProperty> parseGCMDeviceProperty(Bundle extras)
			throws JSONException {
		JSONObject jsonMessage = new JSONObject(extras.getString("message"));
		String deviceData = jsonMessage.getString("Data");
		JSONArray jsonDeviceProperty = new JSONArray(deviceData);
		ArrayList<DeviceProperty> devicePropertyValues = new ArrayList<DeviceProperty>();
		for (int i = 0; i < jsonDeviceProperty.length(); i++) {
			DeviceProperty deviceProperty = new DeviceProperty();

			deviceProperty.setDeviceId(jsonDeviceProperty.getJSONObject(i)
					.getInt("DeviceId"));
			deviceProperty.setPropertyId(jsonDeviceProperty.getJSONObject(i)
					.getInt("PropertyId"));
			deviceProperty.setValue(jsonDeviceProperty.getJSONObject(i)
					.getString("Value"));
			deviceProperty.setPendingValue(jsonDeviceProperty.getJSONObject(i)
					.getString("PendingValue"));
			deviceProperty.setIsActionPending(jsonDeviceProperty.getJSONObject(
					i).getBoolean("IsActionPending"));
			devicePropertyValues.add(deviceProperty);
		}
		return devicePropertyValues;
	}

	/**
	 * @param extras
	 * @return
	 * @throws JSONException
	 */
//	JSONObject jsonMessage=null;
//	JSONObject jsonDevice=null;
	public Device parseGCMDevice(Bundle extras) throws JSONException {
		JSONObject jsonMessage = new JSONObject(extras.getString("message"));
		String deviceData = jsonMessage.getString("Data");
		JSONObject jsonDevice = new JSONObject(deviceData);
		Device device = new Device();
		device.setName(jsonDevice.getString("Name"));
		device.setId(jsonDevice.getInt("Id"));
		device.setRoomName(jsonDevice.getString("RoomName"));
		device.setDeviceTypeId(jsonDevice.getInt("DeviceTypeId"));
		device.setIsOn(jsonDevice.getBoolean("IsOn"));
		device.setIsActionPending(jsonDevice.getBoolean("IsActionPending"));
		CommonValues.getInstance().modifiedDeviceStatus = device;
		return device;
	}

	/**
	 * @return
	 */
	public boolean getCurrentVisibleView(String componentName) {
		boolean shouldProceed = false;
		ArrayList<String> runningactivities = new ArrayList<String>();

		ActivityManager activityManager = (ActivityManager) getBaseContext()
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> services = activityManager
				.getRunningTasks(Integer.MAX_VALUE);

		for (int i1 = 0; i1 < services.size(); i1++) {
			runningactivities.add(0, services.get(i1).topActivity.toString());
		}
		if (runningactivities.contains(componentName) == true) {
			// return true;
			shouldProceed = true;
		}
		return shouldProceed;
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, UserLogin.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.sp_logo)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_VIBRATE);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}