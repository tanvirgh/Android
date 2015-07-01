/**
 * 
 */
package com.sinepulse.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sinepulse.app.R;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.Wamp;
import com.sinepulse.app.asynctasks.AsyncProcessRequestFromDashboard;
import com.sinepulse.app.asynctasks.AsyncSetDeviceStatus;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * @author tanvir.ahmed
 * 
 */
public class DeviceListByTypeAdapter extends ArrayAdapter<Device> {

	// protected static final String LOG_TAG =
	// DeviceListAdapter.class.getSimpleName();

	Device deviceByTypeEntity;
	ArrayList<Device> deviceByTypeList;
	int _position = -1;
	int width;
	View oldView = null;
	private int layoutResourceId;
	private Context context;
//	AsyncSetStatusFromDashboard asyncSetStatusFromDashboard = null;
	AsyncSetDeviceStatus asyncSetDeviceStatus=null;
	int modifiedIndex = 0;
	public static Device orderLine;
	public ArrayList<Device> requestQueue = new ArrayList<Device>();
	int onOffValue;
//	Wamp wamp=new Wamp();

	public DeviceListByTypeAdapter(Context Home, int layoutResourceId,
			ArrayList<Device> deviceList) {
		super(Home, layoutResourceId, new ArrayList<Device>());
		addAll(deviceList);
		this.layoutResourceId = layoutResourceId;
		this.context = Home;
		this.deviceByTypeList = deviceList;
	}

	public static class DeviceByTypeHolder {
		private ImageView onOffImage;
		private RelativeLayout rlDeviceDetails;
		private TextView tvdevice_name;
		private TextView tvroom_name;
		private ToggleButton btdevice_value;
		private int rowID;

		private DeviceByTypeHolder(ImageView onOffImage,
				RelativeLayout rlDeviceDetails, TextView tvroom_name,
				TextView tvdevice_name, ToggleButton btdevice_value, int rowID) {
			this.onOffImage = onOffImage;
			this.rlDeviceDetails = rlDeviceDetails;
			this.tvroom_name = tvroom_name;
			this.tvdevice_name = tvdevice_name;
			this.btdevice_value = btdevice_value;
			this.rowID = rowID;
		}

		public static DeviceByTypeHolder create(RelativeLayout bBasketRowItem,
				int rowId) {
			ImageView onOffImage = (ImageView) bBasketRowItem
					.findViewById(R.id.onOffImage);
			RelativeLayout rlDeviceDetails = (RelativeLayout) bBasketRowItem
					.findViewById(R.id.rlDeviceDetails);
			TextView tvroom_name = (TextView) bBasketRowItem
					.findViewById(R.id.tvroom_name);
			TextView tvdevice_name = (TextView) bBasketRowItem
					.findViewById(R.id.tvdevice_name);
			ToggleButton btdevice_value = (ToggleButton) bBasketRowItem
					.findViewById(R.id.btdevice_value);
			return new DeviceByTypeHolder(onOffImage, rlDeviceDetails,
					tvroom_name, tvdevice_name, btdevice_value, rowId);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final DeviceByTypeHolder dth;
		deviceByTypeEntity = deviceByTypeList.get(position);
		deviceByTypeEntity.RowId = position;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
			dth = DeviceByTypeHolder.create((RelativeLayout) convertView,
					deviceByTypeEntity.RowId);
			convertView.setPadding(4, 3, 4, 3);
			dth.rlDeviceDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);
			dth.tvdevice_name = (TextView) convertView
					.findViewById(R.id.tvdevice_name);
			dth.tvroom_name = (TextView) convertView
					.findViewById(R.id.tvroom_name);
			dth.btdevice_value = (ToggleButton) convertView
					.findViewById(R.id.btdevice_value);
			dth.btdevice_value.setTag(position);
			convertView.setTag(dth);

		} else {
			dth = (DeviceByTypeHolder) convertView.getTag();

		}
//		dth.btdevice_value.setTag(position);
		dth.rowID = position;

		dth.tvdevice_name.setText(deviceByTypeEntity.getName());
		dth.tvroom_name.setText(deviceByTypeEntity.getRoomName());
		dth.btdevice_value.setOnCheckedChangeListener(null);
		if (deviceByTypeEntity.IsActionPending) {
			dth.btdevice_value.setChecked(!deviceByTypeEntity.IsOn);
			dth.btdevice_value.setEnabled(false);
		} else {
			dth.btdevice_value.setChecked(deviceByTypeEntity.IsOn);
			dth.btdevice_value.setEnabled(true);
		}
		if (deviceByTypeEntity.IsOn) {
			dth.onOffImage.setImageResource(R.drawable.greenled_medium);
		} else {
			dth.onOffImage.setImageResource(R.drawable.redled_medium);
		}

		dth.btdevice_value
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						modifiedIndex = Integer.parseInt(buttonView.getTag()
								.toString());
                        onOffValue=(isChecked?1:0);
//                        if(CommonValues.getInstance().deviceList
//                              .get(modifiedIndex).IsActionPending || (CommonValues.getInstance().deviceList
//                              .get(modifiedIndex).IsOn=isChecked)){
//                           
//                           return;
//                          }
                        CommonValues.getInstance().deviceList
                        .get(modifiedIndex).IsActionPending=true;
                          buttonView.setEnabled(false);
						
						orderLine = getItem(modifiedIndex);
						requestQueue.add(orderLine);
						processRequestQueue();
						
//						wamp.connectWampClient();
						

					}

				});

		if (_position != position) {
			convertView.setBackgroundResource(R.drawable.bgmedium);

		}

		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	Device tobeProcessedLine = null;
	public boolean requestProcessing = false;
	

	public void processRequestQueue() {

		if (requestProcessing == true)
			return;
		if (requestQueue.size() > 0) {
			tobeProcessedLine = requestQueue.get(0);
			requestProcessing = true;
		}
		 else if (CommonValues.getInstance().deviceList.size() == 0) {
			 //Refresh call
		 return;
        }
		else {
			return;
		}
	/*	if (asyncSetStatusFromDashboard != null) {
			asyncSetStatusFromDashboard.cancel(true);
		}
		asyncSetStatusFromDashboard = new AsyncSetStatusFromDashboard(
				asyncProcessRequestListener);
//		asyncSetStatusFromDashboard.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		asyncSetStatusFromDashboard.execute();*/
		if(asyncSetDeviceStatus!=null){
			asyncSetDeviceStatus.cancel(true);
		}
		asyncSetDeviceStatus= new AsyncSetDeviceStatus(asyncProcessRequestListener);
		asyncSetDeviceStatus.execute();
		

	}

	AsyncProcessRequestFromDashboard asyncProcessRequestListener = new AsyncProcessRequestFromDashboard() {
          
		@Override
		public void onTaskPreExecute() {
          Home.startDeviceProgress();
		}

		@Override
		public void onTaskPostExecute(Object object) {
			Home.stopDeviceProgress();
			setStatusResponseData();
			if (requestQueue.size() > 0) {
				requestQueue.remove(0);
			}
			requestProcessing = false;
			processRequestQueue();
		}

		@Override
		public void startSendingTask() {
			if (tobeProcessedLine != null ) {
				sendSetStatusRequest(
						tobeProcessedLine.Id,
						onOffValue);
			}
		}

		@Override
		public void onDoInBackground() {
			startSendingTask();
		}

		
	};

	
	
	
	public int getSize() {
		return deviceByTypeList.size();
	}

	public Device getItemAtPosition(int position) {
		deviceByTypeEntity = deviceByTypeList.get(position);
		return deviceByTypeEntity;
	}

	@Override
	public int getCount() {
		return deviceByTypeList.size();
	}

	public void setSelection(int pos) {
		_position = pos;
	}

	boolean touchEnabled = true;

	public void setTouchEnabled(boolean touchEnabled) {
		this.touchEnabled = touchEnabled;
	}

	RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.drawable.bgmedium);
				}
				setSelection(((DeviceByTypeHolder) v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};

	public boolean sendSetStatusRequest(int deviceId, int onOffValue) {

//		String setStatusUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ userId + "/status?id=" + deviceId + "&status=" + onOffValue;
		String setStatusUrl = CommonURL.getInstance().RootUrl + "status" ;
		boolean status=onOffValue==1?true:false;
		
		if (JsonParser.postSetStatusRequest(setStatusUrl,deviceId,status) != null) {
			System.out.println(setStatusUrl);
			return true;
		}
		return false;

	}

	public void setStatusResponseData() {
		if (CommonValues.getInstance().deviceList != null) {
			try {
				int indextoChange = indexOfModifiedDevice(CommonValues
						.getInstance().modifiedDeviceStatus);
				// CommonValues.getInstance().deviceList.remove(modifiedIndex);
				CommonValues.getInstance().deviceList.set(indextoChange,
						CommonValues.getInstance().modifiedDeviceStatus);
				notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void refreshAdapter() {
		notifyDataSetChanged();
	}

	public int indexOfModifiedDevice(Device device) {
		int actualIndex = -1;
		for (int i = 0; i < CommonValues.getInstance().deviceList.size(); i++) {
			if (CommonValues.getInstance().deviceList.get(i).Id == device.Id) {
				actualIndex = i;
				break;
			}
		}

		return actualIndex;

	}

}