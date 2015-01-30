/**
 * 
 */
package com.sinepulse.app.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncGetDevices;
import com.sinepulse.app.fragments.RoomManagerFragment;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * @author tanvir.ahmed
 *
 */
public class DisplayRoomDetails implements OnClickListener{
	
	RoomManagerFragment parentActivity;
	Integer roomIndex;
	
	AsyncGetDevices asyncGetDeviceInfo = null;
	public ProgressBar devicelistProgressBar;
	public ToggleButton btdevice_value;
	public ImageView onOffImage;
	
	
	public DisplayRoomDetails(RoomManagerFragment _parentActivity,Integer _roomIndex) {
		this.parentActivity = _parentActivity;
		this.roomIndex = _roomIndex;
		InitializeControls();
		LoadRoomDetailsContent();
		
		
	}

	private void LoadRoomDetailsContent() {
//		if (asyncGetDeviceInfo != null) {
//			asyncGetDeviceInfo.cancel(true);
//	      }
//		asyncGetDeviceInfo = new AsyncGetDevices(this,parentActivity,CommonValues.getInstance().userId);
//		asyncGetDeviceInfo.execute();
		
		}
		
		
	public boolean sendGetDeviceRequest(Integer userId) {
		
	    String getRoomUrl=CommonURL.getInstance().GetCommonURL+"/"+String.valueOf(userId)+"/rooms";
	    JsonParser.getRoomRequest(getRoomUrl);

		String getDeviceUrl=CommonURL.getInstance().GetCommonURL+"/"+
		String.valueOf(userId)+"/devices?roomid="+CommonValues.getInstance().roomList.get(roomIndex).getId()+"&typeId=0";

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;
	
	}



	private void InitializeControls() {
		RelativeLayout searchItemDetails;
		LayoutInflater inflater = (LayoutInflater) parentActivity.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		searchItemDetails = (RelativeLayout) inflater.inflate(
				R.layout.device_list, null);
		devicelistProgressBar=(ProgressBar) searchItemDetails.findViewById(R.id.devicelistProgressBar);
		btdevice_value=(ToggleButton) searchItemDetails.findViewById(R.id.btdevice_value);
		onOffImage=(ImageView) searchItemDetails.findViewById(R.id.onOffImage);
		
		
		
	}
	
	

	@Override
	public void onClick(View arg0) {
		
		
	}

}
