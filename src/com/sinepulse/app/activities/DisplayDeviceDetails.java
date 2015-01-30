/**
 * 
 */
package com.sinepulse.app.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncGetDevicesByType;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * @author tanvir.ahmed
 *
 */
public class DisplayDeviceDetails  implements OnClickListener{
	
	Home parentActivity;
	Integer deviceType;
	
	AsyncGetDevicesByType asyncGetDeviceByType = null;
	public ProgressBar devicelistProgressBar;
	
	
	
	public DisplayDeviceDetails(Home _parentActivity,Integer _deviceType) {
		this.parentActivity = _parentActivity;
		this.deviceType = _deviceType;
		InitializeControls();
		LoadDeviceDetailsContent();
		
	}

	private void LoadDeviceDetailsContent() {
//		if (asyncGetDeviceByType != null) {
//			asyncGetDeviceByType.cancel(true);
//	      }
//		asyncGetDeviceByType = new AsyncGetDevicesByType(this,parentActivity,deviceType);
//		asyncGetDeviceByType.execute();
		}
	

		
		
	public boolean sendGetDeviceByTypeRequest(Integer deviceType) {
		
	    String getRoomUrl=CommonURL.getInstance().GetCommonURL+"/"+CommonValues.getInstance().userId+"/rooms";
	    JsonParser.getRoomRequest(getRoomUrl);

		String getDeviceUrl=CommonURL.getInstance().GetCommonURL+"/"+
		CommonValues.getInstance().userId+"/devices?roomid=0"+"&typeId="+deviceType;

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;
	
	}



	private void InitializeControls() {
		RelativeLayout searchItemDetails;
		LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		searchItemDetails = (RelativeLayout) inflater.inflate(
				R.layout.device_by_type_list, null);
		devicelistProgressBar=(ProgressBar) searchItemDetails.findViewById(R.id.devicelistProgressBar);
		
		
		
	}
	
	

	@Override
	public void onClick(View arg0) {
		
		
	}

}
