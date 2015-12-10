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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sinepulse.app.R;
import com.sinepulse.app.activities.RoomManager;
import com.sinepulse.app.asynctasks.AsyncProcessRequestFromDashboard;
import com.sinepulse.app.asynctasks.AsyncSetDeviceStatus;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

public class DeviceListAdapter extends ArrayAdapter<Device> {

	// protected static final String LOG_TAG =
	// DeviceListAdapter.class.getSimpleName();

	Device deviceByRoomEntity;
	ArrayList<Device> deviceByRoomList;
	int _position = -1;
	int width;
	View oldView = null;
	private int layoutResourceId;
	private Context context;
	int modifiedIndex=0;
//	AsyncSetStatusFromRoom asyncSetStatusFromRoom=null;
	AsyncSetDeviceStatus asyncSetDeviceStatus=null;
	public static Device orderLine;
	public ArrayList<Device> requestQueue = new ArrayList<Device>();
	int onOffValue;

	public DeviceListAdapter(Context context, int layoutResourceId,
			ArrayList<Device> deviceList) {
		super(context, layoutResourceId, new ArrayList<Device>());
		 addAll(deviceList);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.deviceByRoomList = deviceList;
	}

	public static class DeviceByRoomHolder {
		private ImageView ivDeviceListItemImage;
		private ImageView onOffImage;
		private RelativeLayout rlDeviceDetails;
		private TextView tvdevice_name;
		private ToggleButton btdevice_value;
		private int rowID;

		private DeviceByRoomHolder(ImageView ivDeviceListItemImage,
				ImageView onOffImage, RelativeLayout rlDeviceDetails,
				TextView tvdevice_name, ToggleButton btdevice_value, int rowID) {
			this.ivDeviceListItemImage = ivDeviceListItemImage;
			this.onOffImage = onOffImage;
			this.rlDeviceDetails = rlDeviceDetails;
			this.tvdevice_name = tvdevice_name;
			this.btdevice_value = btdevice_value;
			this.rowID = rowID;
		}

		public static DeviceByRoomHolder create(LinearLayout devicelistRowItem,
				int rowId) {
			ImageView ivDeviceListItemImage = (ImageView) devicelistRowItem
					.findViewById(R.id.ivDeviceListItemImage);
			ImageView onOffImage = (ImageView) devicelistRowItem
					.findViewById(R.id.onOffImage);
			RelativeLayout rlDeviceDetails = (RelativeLayout) devicelistRowItem
					.findViewById(R.id.rlDeviceDetails);
			TextView tvdevice_name = (TextView) devicelistRowItem
					.findViewById(R.id.tvdevice_name);
			ToggleButton btdevice_value = (ToggleButton) devicelistRowItem
					.findViewById(R.id.btdevice_value);
			return new DeviceByRoomHolder(ivDeviceListItemImage, onOffImage,
					rlDeviceDetails, tvdevice_name, btdevice_value, rowId);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final DeviceByRoomHolder drh;
		deviceByRoomEntity = deviceByRoomList.get(position);
		deviceByRoomEntity.RowId = position;
		// deviceStatusEntity = deviceInquieryList.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
			drh = DeviceByRoomHolder.create((LinearLayout) convertView,
					deviceByRoomEntity.RowId);
			convertView.setPadding(4, 3, 4, 3);
			drh.rlDeviceDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);
			drh.tvdevice_name = (TextView) convertView
					.findViewById(R.id.tvdevice_name);
			drh.btdevice_value = (ToggleButton) convertView
					.findViewById(R.id.btdevice_value);
			drh.onOffImage = (ImageView) convertView
					.findViewById(R.id.onOffImage);
			drh.btdevice_value.setTag(position);
			convertView.setTag(drh);

		} else {
			drh = (DeviceByRoomHolder) convertView.getTag();
		}
//		drh.btdevice_value.setTag(position);
		drh.rowID = position;
		drh.tvdevice_name.setText(deviceByRoomEntity.getName());
		if (deviceByRoomEntity.getDeviceTypeId() == 1) {
			if (deviceByRoomEntity.IsOn){
				drh.ivDeviceListItemImage
				.setBackgroundResource(R.drawable.fan_on);
			}else{
			drh.ivDeviceListItemImage
					.setBackgroundResource(R.drawable.fan_off);
			}
		}
		if (deviceByRoomEntity.getDeviceTypeId() == 2) {
			if (deviceByRoomEntity.IsOn){
				drh.ivDeviceListItemImage
				.setBackgroundResource(R.drawable.bulbon);
			}else{
			drh.ivDeviceListItemImage
					.setBackgroundResource(R.drawable.bulb_off);
			}
		}
		if (deviceByRoomEntity.getDeviceTypeId() == 3) {
			if (deviceByRoomEntity.IsOn){
				drh.ivDeviceListItemImage
				.setBackgroundResource(R.drawable.ac_medium);
			}else{
			drh.ivDeviceListItemImage
					.setBackgroundResource(R.drawable.ac_medium);
			}
		}
		if (deviceByRoomEntity.getDeviceTypeId() == 4) {
			if (deviceByRoomEntity.IsOn){
				drh.ivDeviceListItemImage
				.setBackgroundResource(R.drawable.curtainmedium);
			}else{
			drh.ivDeviceListItemImage
					.setBackgroundResource(R.drawable.curtainmedium_off);
			}
		}
		drh.btdevice_value.setOnCheckedChangeListener(null);
		if(deviceByRoomEntity.IsActionPending){
			drh.btdevice_value.setChecked(!deviceByRoomEntity.IsOn);
			drh.btdevice_value.setEnabled(false);
			
		}else{
			drh.btdevice_value.setChecked(deviceByRoomEntity.IsOn);
			drh.btdevice_value.setEnabled(true);
		}
		if (deviceByRoomEntity.IsOn) {
			drh.onOffImage.setImageResource(R.drawable.greenled);
		}else{
			drh.onOffImage.setImageResource(R.drawable.redled);
		}
		
		drh.btdevice_value.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					modifiedIndex = Integer.parseInt(buttonView.getTag()
							.toString());
					onOffValue=(isChecked?1:0);
					 CommonValues.getInstance().deviceList
                     .get(modifiedIndex).IsActionPending=true;
					buttonView.setEnabled(false);
					orderLine = getItem(modifiedIndex);
					requestQueue.add(orderLine);
					processRequestQueue();
			} 
			
/*private void sendSetStatus(boolean isChecked,int deviceId,int userId) {
				if (asyncSetStatusFromRoom != null) {
					asyncSetStatusFromRoom.cancel(true);
				}
				asyncSetStatusFromRoom = new AsyncSetStatusFromRoom(DeviceListAdapter.this,context, deviceId,userId,isChecked);
				asyncSetStatusFromRoom.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				RoomManager.LoadRoomDetailsContent();
				
			}*/
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
		if(asyncSetDeviceStatus!=null){
			asyncSetDeviceStatus.cancel(true);
		}
		asyncSetDeviceStatus= new AsyncSetDeviceStatus(asyncProcessRequestListener);
		asyncSetDeviceStatus.execute();

	}
	
	AsyncProcessRequestFromDashboard asyncProcessRequestListener = new AsyncProcessRequestFromDashboard() {

		@Override
		public void onTaskPreExecute() {
			CommonValues.getInstance().IsServerConnectionError=false;
			RoomManager.startDeviceProgress();
		}

		@Override
		public void onTaskPostExecute(Object object) {
			RoomManager.stopDeviceProgress();
			  if(CommonValues.getInstance().IsServerConnectionError==true){
	            	CommonTask.ShowMessage(context, "Server is not reachable at this moment.");
//	            	asyncSetDeviceStatus.cancel(true);
	            	return;
	            }
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
				sendSetStatusRequest(CommonValues.getInstance().userId,
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
		return deviceByRoomList.size();
	}

	public Device getItemAtPosition(int position) {
		deviceByRoomEntity = deviceByRoomList.get(position);
		return deviceByRoomEntity;
	}

	@Override
	public int getCount() {
		return deviceByRoomList.size();
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
				setSelection(((DeviceByRoomHolder) v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};
	

	public boolean sendSetStatusRequest(int userId, int deviceId, int onOffValue) {
		
		String setStatusUrl = CommonURL.getInstance().GetCommonURL
				+ "/" + userId + "/status?id="+ deviceId+"&status="+onOffValue;
		if (JsonParser.setStatusRequest(setStatusUrl,context) != null &&  JsonParser.setStatusRequest(setStatusUrl,context)!="") {
//			System.out.println(setStatusUrl);
			return true;
		}else{
			CommonValues.getInstance().IsServerConnectionError=true;
		return false;
		}
		
	}

	public void setStatusResponseData() {
//		Log.d("Room:NamebeforeRemove", CommonValues.getInstance().deviceList.get(modifiedIndex).Name+"");
		if(CommonValues.getInstance().deviceList!=null){
		 try {
			int indextoChange= indexOfModifiedDevice(CommonValues.getInstance().modifiedDeviceStatus);
//			CommonValues.getInstance().deviceList.remove(indextoChange);
			 CommonValues.getInstance().deviceList.set(indextoChange,
			 CommonValues.getInstance().modifiedDeviceStatus);
//		 Log.d("Room:NameAfterAdd", CommonValues.getInstance().deviceList.get(modifiedIndex).Name+"");
			 notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	public void refreshAdapter(){
		notifyDataSetChanged();
	}
	
	public int indexOfModifiedDevice(Device device){
		int actualIndex=-1;
		for(int i=0;i<CommonValues.getInstance().deviceList.size();i++){
			if(CommonValues.getInstance().deviceList.get(i).Id== device.Id){
				actualIndex=i;
				break;
			}
		}
		
		
		return actualIndex;
		
	}

}