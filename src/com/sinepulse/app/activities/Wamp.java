package com.sinepulse.app.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.utils.CommonValues;

public class Wamp extends MainActionbarBase  {
	
	WampClient client;
	String LOG_WAMP = "WAMP";
	// config
	String WAMP_HOST = "ws://192.168.11.111:9090/";
	String WAMP_REALM = "devicewampmessaging";
	int MIN_RECONNECT_INTERVAL = 5; // sec
	String TOPIC_ID = "sinepulsemcmobile";
	public Button button1;
	Context currentContext=null;
	
	/* @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        connectWampClient();
	       
	    }*/


	public void connectWampClient(Context context) {
		currentContext=context;
		try {
			// Create a builder and configure the client
			WampClientBuilder builder = new WampClientBuilder();
			builder.withUri(WAMP_HOST)
					.withRealm(WAMP_REALM)
					.withInfiniteReconnects()
					.withReconnectInterval(MIN_RECONNECT_INTERVAL,
							TimeUnit.SECONDS);
			// Create a client through the builder. This will not immediately
			// start
			// a connection attempt
			try {
				client = builder.build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// subscribe for session status changes
			client.statusChanged().subscribe(new Action1<WampClient.Status>() {
				@Override
				public void call(WampClient.Status t1) {
					Log.i(LOG_WAMP, "Session status changed to " + t1);

					if (t1 == WampClient.Status.Connected) {
						// once it's connected, subscribe to Add events and
						// request the comments.
						setCommentsAddListener();
					}
				}
			});
			// request to open the connection with the server
			client.open();
		} catch (Exception e) {
			Log.i(LOG_WAMP, "Error " + e);
		}
	}

	/**
	 * Makes a subscription on the Wamp client to received the comments add
	 * event on the current Post
	 */
	JSONArray jArray=null;
	void setCommentsAddListener() {
		// comments.[post_id].add
		// final String procedure = "comments." + POST_ID + ".add";
		Log.i(LOG_WAMP, "DIN SUBSCRIPTION");
		client.makeSubscription(TOPIC_ID).subscribe(new Action1<PubSubData>() {
			@Override
			public void call(PubSubData arg0) {
				if (arg0 != null) {

					Log.i(LOG_WAMP,
							TOPIC_ID + " call Json response: "
									+ arg0.toString());

					ObjectNode objectNode = arg0.keywordArguments();
					String str = objectNode.toString();

					try {

						Log.i(LOG_WAMP, "DATA FOUND " + str);

						JSONObject jsonComment = new JSONObject(str);

					 jArray = jsonComment.getJSONArray("Data");

						int targetDeviceId = jArray.getJSONObject(0).getInt(
								"DeviceId");
						if(currentContext instanceof Home){
							if (Home_.vfDeviceType != null
									&& Home.vfDeviceType.getDisplayedChild() == 1) {
									int index = -1;
									for (int i = 0; i < CommonValues
											.getInstance().deviceList.size(); i++) {
										if (CommonValues.getInstance().deviceList
												.get(i).Id == targetDeviceId) {
											index = i;
											break;
										}
									}
									if (index != -1) {
										
										getWampDevice(index, jArray);
										((Activity) currentContext).runOnUiThread(new Runnable() {

													@Override
													public void run() {
														if (CommonValues
																.getInstance().connectionMode
																.equals("Local")) {
															((Home) currentContext).dtAdapter
																	.refreshAdapter();
														}

													}
												});

									}
							}
							if (Home_.vfDeviceType != null
									&& Home.vfDeviceType.getDisplayedChild() == 2) {
								if(targetDeviceId==CommonValues.getInstance().home.deviceId){
									ArrayList<DeviceProperty> devicePropertyValues = getWampDevicePropertyData();
									CommonValues.getInstance().devicePropertyList = devicePropertyValues;
									
									((Activity) currentContext).runOnUiThread(new Runnable() {
										

										@Override
										public void run() {
											if (CommonValues
													.getInstance().connectionMode
													.equals("Local")) {
												((Home) currentContext).setPropertyResponseData();
											}

										}
									});
									
								}
								
								
							}
						}
						if(currentContext instanceof RoomManager){
							if (RoomManager_.vfRoom!=null &&RoomManager.vfRoom.getDisplayedChild() == 1) {
								int index = -1;
								for (int i = 0; i < CommonValues
										.getInstance().deviceList.size(); i++) {
									if (CommonValues.getInstance().deviceList
											.get(i).Id == targetDeviceId) {
										index = i;
										break;
									}
								}
								if (index != -1) {
									
									getWampDevice(index, jArray);
									((Activity) currentContext).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if (CommonValues
													.getInstance().connectionMode
													.equals("Local")) {
												((RoomManager) currentContext).dAdapter
														.refreshAdapter();
											}

										}
									});
								}
								
							}
                       if (RoomManager_.vfRoom!=null &&RoomManager.vfRoom.getDisplayedChild() == 2) {
								
                    	   if(targetDeviceId==CommonValues.getInstance().roomManager.deviceId){
								ArrayList<DeviceProperty> devicePropertyValues = getWampDevicePropertyData();
								CommonValues.getInstance().devicePropertyList = devicePropertyValues;
								
								((Activity) currentContext).runOnUiThread(new Runnable() {
									

									@Override
									public void run() {
										if (CommonValues
												.getInstance().connectionMode
												.equals("Local")) {
											((RoomManager) currentContext).setDevicePropertyData();
										}

									}
								});
								
							}
							}
							
						}
						
					} catch (JSONException e) {
					}
				}

			}

			private ArrayList<DeviceProperty> getWampDevicePropertyData() {
				JSONArray jsonDeviceProperty = jArray;
				ArrayList<DeviceProperty> devicePropertyValues = new ArrayList<DeviceProperty>();
				for (int i = 0; i < jsonDeviceProperty.length(); i++) {
					DeviceProperty deviceProperty = new DeviceProperty();

					try {
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					devicePropertyValues.add(deviceProperty);
				}
				
				return devicePropertyValues;
			}

		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable arg0) {
				if (arg0 != null) {
					Log.i(LOG_WAMP, TOPIC_ID + " call Throwable response: "
							+ arg0.toString());
				}
			}

		});
	}

	public void stopWampClient() {
		client.close();
	}
	
	public void getWampDevice(int index, JSONArray deviceArray) {
		Device device = CommonValues.getInstance().deviceList.get(index);
		JSONObject targetDevice = null;

		for (int i = 0; i < deviceArray.length(); i++) {
			try {
				if (deviceArray.getJSONObject(i).getInt("PropertyId") == 1) {

					targetDevice = deviceArray.getJSONObject(i);
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			device.IsOn = (targetDevice.getInt("Value") == 1) ? true : false;
			device.IsActionPending = targetDevice.getBoolean("IsActionPending");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommonValues.getInstance().deviceList.set(index, device);
	}

}
