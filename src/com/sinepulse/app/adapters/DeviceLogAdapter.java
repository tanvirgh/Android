/**
 * 
 */
package com.sinepulse.app.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinepulse.app.R;
import com.sinepulse.app.entities.DevicePropertyDetail;
import com.sinepulse.app.entities.DevicePropertyLog;
import com.sinepulse.app.utils.CommonTask;

/**
 * @author tanvir.ahmed
 * 
 */
public class DeviceLogAdapter extends ArrayAdapter<DevicePropertyLog> {

	// protected static final String LOG_TAG =
	// DeviceListAdapter.class.getSimpleName();

	DevicePropertyLog deviceLogEntity;
	ArrayList<DevicePropertyLog> deviceLogDetailList;
	int _position = -1;
	int width;
	View oldView = null;
	private int layoutResourceId;
	private Context context;

	public DeviceLogAdapter(Context context, int layoutResourceId,
			ArrayList<DevicePropertyLog> deviceLogDetailList) {
		super(context, layoutResourceId, new ArrayList<DevicePropertyLog>());
		addAll(deviceLogDetailList);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.deviceLogDetailList = deviceLogDetailList;
	}

	public static class DeviceLogHolder {
		private LinearLayout rlDeviceLogDetails;
		private TextView tv_date;
		private TextView tv_log;
//		private TextView tv_others;
		private int rowID;

		private DeviceLogHolder(LinearLayout rlDeviceLogDetails,
				TextView tv_date, TextView tv_log,  int rowID) {
			this.rlDeviceLogDetails = rlDeviceLogDetails;
			this.tv_date = tv_date;
			this.tv_log = tv_log;
//			this.tv_others = tv_others;
			this.rowID = rowID;
		}

		public static DeviceLogHolder create(LinearLayout devicelogRowItem,
				int rowId) {
			LinearLayout rlDeviceLogDetails = (LinearLayout) devicelogRowItem
					.findViewById(R.id.rlDeviceLogDetails);
			TextView tv_date = (TextView) devicelogRowItem
					.findViewById(R.id.tv_date);
			TextView tv_log = (TextView) devicelogRowItem
					.findViewById(R.id.tv_log);
//			TextView tv_others = (TextView) devicelogRowItem
//					.findViewById(R.id.tv_others);

			return new DeviceLogHolder(rlDeviceLogDetails, tv_date, tv_log,
					rowId);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DeviceLogHolder dlh;
		deviceLogEntity = deviceLogDetailList.get(position);
		deviceLogEntity.Id = position;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
			dlh = DeviceLogHolder.create((LinearLayout) convertView,
					deviceLogEntity.Id);
			convertView.setTag(dlh);
			convertView.setPadding(4, 3, 4, 3);
			dlh.rlDeviceLogDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);

			dlh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			dlh.tv_log = (TextView) convertView.findViewById(R.id.tv_log);
			// dlh.tv_others = (TextView)
			// convertView.findViewById(R.id.tv_others);

		} else {
			dlh = (DeviceLogHolder) convertView.getTag();
		}
		dlh.rowID = position;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
		Long timeInMillis = Long.valueOf(deviceLogEntity.getLoggedAt().getTime());
		Date LoggedAt = new Date(timeInMillis);
		 dlh.tv_date.setText(formatter.format(LoggedAt));
		 String result="";
			if(!deviceLogEntity.getPropertyName().equalsIgnoreCase("Dimming") 
					&&! deviceLogEntity.getPropertyName().equalsIgnoreCase("Regulator")
					&&! deviceLogEntity.getPropertyName().equalsIgnoreCase("Shade")
					&&! deviceLogEntity.getPropertyName().equalsIgnoreCase("Rotate")
					&&! deviceLogEntity.getPropertyName().equalsIgnoreCase("Preset")){
			if(deviceLogEntity.getPropertyName().equalsIgnoreCase("Switch") && deviceLogEntity.getValue().equals("1")){
				result="ed  On";
			}else if(deviceLogEntity.getPropertyName().equalsIgnoreCase("Switch") && deviceLogEntity.getValue().equals("0")){
				result="ed  Off";
			}
			}else {
				result="  set to "+deviceLogEntity.getValue()+"%";
			}
		 dlh.tv_log.setText(deviceLogEntity.getPropertyName()+result+" By "+deviceLogEntity.getUserName());

		
		// dlh.tv_others.setText(deviceLogEntity.getUserName()+"Turned Off "+deviceLogEntity.getDeviceName()+
		// " At :"+formatter.format(LoggedAt));

		if (_position != position) {
//			convertView.setBackgroundResource(R.drawable.bg_cell_light);

		}

//		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	public int getSize() {
		return deviceLogDetailList.size();
	}

	public DevicePropertyDetail getItemAtPosition(int position) {
		deviceLogEntity = deviceLogDetailList.get(position);
		return deviceLogEntity;
	}

	@Override
	public int getCount() {
		return  deviceLogDetailList.size();
	}
	@Override
	  public boolean isEnabled (int position) {
	    return false;
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
//					oldView.setBackgroundResource(R.drawable.bg_cell_light);
				}
				setSelection(((DeviceLogHolder) v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};

}