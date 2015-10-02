/**
 * 
 */
package com.sinepulse.app.asynctasks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sinepulse.app.activities.Home;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * Get all device list type wise from server for home tab through asynchronous call.
 * @author tanvir.ahmed
 *
 */
public class AsyncGetDevicesByType extends AsyncTask<Void, Void, ArrayList<Device>> {
	
//	DisplayDeviceDetails  deviceDetails ;
    Context parentActivity;
	private int deviceType;
	private int deviceTypeId;
//	private int roomId;
	
	public AsyncGetDevicesByType(Context _parentActivity, int deviceType,int deviceTypeId) {
//		deviceDetails = deviceDetailsFragment;
		this.parentActivity=_parentActivity;
		this.deviceType=deviceType;
		this.deviceTypeId=deviceTypeId;
//		this.roomId=roomId;
		
	}
	

	@Override
	protected void onPreExecute() {
		CommonValues.getInstance().previousAction=CommonValues.getInstance().currentAction;
		Home.startDeviceProgress();
		
	}
	

	@Override
	protected ArrayList<Device> doInBackground(Void... params) {
		((Home) parentActivity).sendGetDeviceByTypeRequest(deviceType);
		return CommonValues.getInstance().deviceList;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Device> result) {
		Home.stopDeviceProgress();
		if(result.size()!=0){
		if(CommonValues.getInstance().currentAction.equals(CommonValues.getInstance().previousAction)){
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
		if (parentActivity != null) {
		((Activity) parentActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Home) parentActivity).setupDeviceByTypeListViewAdapter(deviceType);
			}
		});
		
			}
		}
	}
		}else{
//			CommonTask.ShowAlertMessage(parentActivity, CommonValues.getInstance().alertObj );
			
		}
	}



	
}
	
