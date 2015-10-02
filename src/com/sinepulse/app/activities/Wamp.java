package com.sinepulse.app.activities;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.Reply;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.utils.CommonValues;

/**
 * This class will receive the wamp notification and update the application Ui
 * accordingly.
 * 
 * @author tanvir.ahmed
 * 
 */

public class Wamp extends MainActionbarBase {

	WampClient client;
	String LOG_WAMP = "WAMP";
	String WAMP_HOST = "";
	String WAMP_REALM = "devicewampmessaging";
	int MIN_RECONNECT_INTERVAL = 5; // sec
	String TOPIC_ID = "sinepulsemcmobile";
	public Button button1;
	Context currentContext = null;

	int POST_ID = 52213;// topic

	int USER_ID = 52188;

	public void connectWampClient(Context context) {
		currentContext = context;
		if (CommonValues.getInstance().nsdResolvedIp != null) {
			WAMP_HOST = "ws://" + CommonValues.getInstance().nsdResolvedIp
					+ ":9090/";
		}
		try {
			// Create a builder and configure the client
			WampClientBuilder builder = new WampClientBuilder();
			builder.withUri(WAMP_HOST)
					.withRealm(WAMP_REALM)
					.withInfiniteReconnects()
					.withReconnectInterval(MIN_RECONNECT_INTERVAL,
							TimeUnit.SECONDS);
			// Create a client through the builder. This will not immediately
			// start a connection attempt
			try {
				client = builder.build();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// subscribe for session status changes
			// managePubSubWampData();
			// request to open the connection with the server
			client.open();
		} catch (Exception e) {
			Log.i(LOG_WAMP, "Error " + e);
		}
	}

	/**
	 * 
	 */
	public void managePubSubWampData() {
		client.statusChanged().subscribe(new Action1<WampClient.Status>() {
			@Override
			public void call(WampClient.Status t1) {
				Log.i(LOG_WAMP, "Session status changed to " + t1);

				if (t1 == WampClient.Status.Connected) {
					// once it's connected, subscribe to Add events and
					// request the comments.
					setCommentsAddListener();
					// RPC
					// requestComments(1,false);
				}
			}
		});
	}

	/**
	 * Makes a subscription on the Wamp client to received the comments add
	 * event on the current Post
	 */
	JSONArray jArray = null;

	void setCommentsAddListener() {
		// comments.[post_id].add
		// final String procedure = "comments." + POST_ID + ".add";
		// Log.i(LOG_WAMP, "DIN SUBSCRIPTION");

		client.makeSubscription(TOPIC_ID).subscribe(new Action1<PubSubData>() {
			@Override
			public void call(PubSubData arg0) {
				if (arg0 != null) {

					ObjectNode objectNode = arg0.keywordArguments();
					String str = objectNode.toString();

					try {

						Log.i(LOG_WAMP, "PUBSUB DATA FOUND " + str);

						int targetDeviceId = convertWampResponse(str);
						if (currentContext instanceof Home) {
							if (Home_.vfDeviceType != null
									&& Home.vfDeviceType.getDisplayedChild() == 1) {
								setWampStatus(targetDeviceId, currentContext);
							} else if (Home_.vfDeviceType != null
									&& Home.vfDeviceType.getDisplayedChild() == 2) {
								setWampProperty(targetDeviceId, currentContext);
							}
						}
						if (currentContext instanceof RoomManager) {
							if (RoomManager_.vfRoom != null
									&& RoomManager.vfRoom.getDisplayedChild() == 1) {
								setWampStatus(targetDeviceId, currentContext);
							}
							if (RoomManager_.vfRoom != null
									&& RoomManager.vfRoom.getDisplayedChild() == 2) {
								setWampProperty(targetDeviceId, currentContext);
							}

						}

					} catch (JSONException e) {
					}
				}

			}

		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable arg0) {
				if (arg0 != null) {
					// Log.i(LOG_WAMP, TOPIC_ID + " call Throwable response: "
					// + arg0.toString());
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
				e.printStackTrace();
			}

		}
		try {
			device.IsOn = (targetDevice.getInt("Value") == 1) ? true : false;
			device.IsActionPending = targetDevice.getBoolean("IsActionPending");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		CommonValues.getInstance().deviceList.set(index, device);
	}

	public void getWampRpcDevice(int index, JSONObject rpcDeviceData) {
		Device device = CommonValues.getInstance().deviceList.get(index);
		try {
			device.IsOn = rpcDeviceData.getBoolean("IsOn");
			device.IsActionPending = rpcDeviceData
					.getBoolean("IsActionPending");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		CommonValues.getInstance().deviceList.set(index, device);

	}

	/**
	 * Requests the Wamp client to make a call to get the comments for the
	 * current Post.
	 * 
	 * @param deviceListByTypeAdapter
	 */

	public void requestforRpcStatusData(int deviceId, int isOn,
			Context runningContext) {
		currentContext = runningContext;
		try {
			String procedure = "status";
			JsonNodeFactory jnf = new JsonNodeFactory(true);
			ObjectNode node = new ObjectNode(jnf);
			node.put("DeviceId", deviceId);
			node.put("Status", isOn);
			// apikey
			node.put("ApiKey", CommonValues.getInstance().ApiKey);

			ArrayNode arrayNode = new ArrayNode(jnf);

			Observable<Reply> observable = client.call(procedure, arrayNode,
					node);

			observable.subscribe(new Action1<Reply>() {
				@Override
				public void call(Reply reply) {

					// CommonValues.getInstance().end =
					// System.currentTimeMillis();
					// Log.d("Time", "Found result in " +
					// (CommonValues.getInstance().end -
					// CommonValues.getInstance().start) + " ms");
					if (reply != null && reply.arguments().size() > 0) {
						String str = reply.arguments().get(0).toString();
						Log.i(LOG_WAMP, "RPC Status DATA FOUND " + str);
						setWampRpcStatus(str, currentContext);

					}
				}
			}, new Action1<Throwable>() {
				@Override
				public void call(Throwable arg0) {
					if (arg0 != null) {
						// Log.i(LOG_WAMP,
						// "comments.get call Throwable response: "
						// + arg0.toString());
					}
				}

			});
		} catch (Exception e) {
			// Log.i(LOG_WAMP, "requestComments Exception");
		}
	}

	public void requestforRpcPropertyData(int deviceId, int propertyId,
			int value, Context existingCon) {
		currentContext = existingCon;
		try {
			String procedure = "property";
			JsonNodeFactory jnf = new JsonNodeFactory(true);
			ObjectNode node = new ObjectNode(jnf);
			node.put("DeviceId", deviceId);
			node.put("PropertyId", propertyId);
			node.put("Value", value);
			// apikey
			node.put("ApiKey", CommonValues.getInstance().ApiKey);

			ArrayNode arrayNode = new ArrayNode(jnf);

			Observable<Reply> observable = client.call(procedure, arrayNode,
					node);

			observable.subscribe(new Action1<Reply>() {
				@Override
				public void call(Reply reply) {
					// CommonValues.getInstance().end =
					// System.currentTimeMillis();
					// Log.d("Time", "Found result in " +
					// (CommonValues.getInstance().end -
					// CommonValues.getInstance().start) + " ms");
					if (reply != null && reply.arguments().size() > 0) {
						// ArrayNode arguments = reply.arguments();
						String str = reply.arguments().get(0).toString();
						Log.i(LOG_WAMP, "RPC property DATA FOUND " + str);
						// int targetDeviceId = convertWampRpcResponse(str);

						try {
							setWampProperty(convertWampResponse(str),
									currentContext);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}, new Action1<Throwable>() {
				@Override
				public void call(Throwable arg0) {
					if (arg0 != null) {
						Log.i(LOG_WAMP,
								"comments.get call Throwable response: "
										+ arg0.toString());
					}
				}

			});
		} catch (Exception e) {
			Log.i(LOG_WAMP, "requestComments Exception");
		}
	}

	/**
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	private int convertWampResponse(String str) throws JSONException {
		JSONObject jsonComment = new JSONObject(str);

		jArray = jsonComment.getJSONArray("Data");

		int targetDeviceId = jArray.getJSONObject(0).getInt("DeviceId");
		return targetDeviceId;
	}

	private JSONObject convertWampRpcResponse(String str) throws JSONException {
		JSONObject jsonComment = new JSONObject(str);

		jsonComment = jsonComment.getJSONObject("Data");

		// int targetDeviceId = jsonComment.getInt("Id");
		return jsonComment;
	}

	/**
	 * @param targetDeviceId
	 */
	private void setWampStatus(int targetDeviceId, Context curcontxt) {

		int index = -1;
		for (int i = 0; i < CommonValues.getInstance().deviceList.size(); i++) {
			if (CommonValues.getInstance().deviceList.get(i).Id == targetDeviceId) {
				index = i;
				break;
			}
		}
		if (index != -1) {

			getWampDevice(index, jArray);
			if (curcontxt instanceof Home) {

				((Activity) currentContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (CommonValues.getInstance().connectionMode
								.equals("Local")) {
							((Home) currentContext).dtAdapter.refreshAdapter();
						}

					}
				});
			} else if (curcontxt instanceof RoomManager) {
				((Activity) currentContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((RoomManager) currentContext).dAdapter
								.refreshAdapter();

					}
				});
			}

		}
	}

	private void setWampRpcStatus(String str, Context presentContxt) {

		JSONObject rpcDeviceData = null;
		try {
			// JsonParser.processDeviceStatusResponse(str);
			rpcDeviceData = convertWampRpcResponse(str);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Home_.vfDeviceType != null
				&& Home.vfDeviceType.getDisplayedChild() == 1) {
			int index = -1;
			for (int i = 0; i < CommonValues.getInstance().deviceList.size(); i++) {
				try {
					if (CommonValues.getInstance().deviceList.get(i).Id == rpcDeviceData
							.getInt("Id")) {
						index = i;
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (index != -1) {

				getWampRpcDevice(index, rpcDeviceData);
				if (presentContxt instanceof Home) {
					((Activity) currentContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							((Home) currentContext).dtAdapter.refreshAdapter();

						}
					});
				} else if (presentContxt instanceof RoomManager) {
					((Activity) currentContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							((RoomManager) currentContext).dAdapter
									.refreshAdapter();

						}
					});

				}

			}
		}
	}

	/**
	 * @param targetDeviceId
	 */
	private void setWampProperty(int targetDeviceId, Context curCntxt) {
		ArrayList<DeviceProperty> devicePropertyValues = getWampDevicePropertyData();
		CommonValues.getInstance().devicePropertyList = devicePropertyValues;
		if (curCntxt instanceof Home) {
			if (targetDeviceId == CommonValues.getInstance().home.deviceIdFromHome) {
				((Activity) currentContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (CommonValues.getInstance().connectionMode
								.equals("Local")) {
							((Home) currentContext).setPropertyResponseData();
						}

					}
				});
			}
		} else if (curCntxt instanceof RoomManager) {
			if (targetDeviceId == CommonValues.getInstance().roomManager.deviceId) {
				((Activity) currentContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((RoomManager) currentContext).setDevicePropertyData();

					}
				});

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
				deviceProperty.setPropertyId(jsonDeviceProperty
						.getJSONObject(i).getInt("PropertyId"));
				deviceProperty.setValue(jsonDeviceProperty.getJSONObject(i)
						.getString("Value"));
				deviceProperty.setPendingValue(jsonDeviceProperty
						.getJSONObject(i).getString("PendingValue"));
				deviceProperty.setIsActionPending(jsonDeviceProperty
						.getJSONObject(i).getBoolean("IsActionPending"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			devicePropertyValues.add(deviceProperty);
		}

		return devicePropertyValues;
	}

}
