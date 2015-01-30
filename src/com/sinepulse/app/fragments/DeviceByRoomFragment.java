/**
 * 
 */
package com.sinepulse.app.fragments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.adapters.DeviceListAdapter;
import com.sinepulse.app.asynctasks.AsyncGetDevices;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * @author tanvir.ahmed
 *
 */
@EFragment(R.layout.device_list)
public class DeviceByRoomFragment extends SherlockFragment {
	public static final int INITIAL_STATE = -1;
	public enum UserProfileSate {
		INITIAL_STATE // backState -1		

	};
	boolean fragmentPaused = false;

	AsyncGetDevices asyncGetDeviceInfo = null;
	private DeviceListAdapter dAdapter;
	@ViewById(R.id.lvDeviceList)
	public ListView deviceListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getActivity().setContentView(R.layout.catalogue2);
		getSherlockActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	@AfterViews
	void afterViewLoaded(){
//		setUserInformation();
		LoadRoomDetailsContent();
	}
	private void LoadRoomDetailsContent() {
		if (asyncGetDeviceInfo != null) {
			asyncGetDeviceInfo.cancel(true);
	      }
//		asyncGetDeviceInfo = new AsyncGetDevices(this,CommonValues.getInstance().userId);
//		asyncGetDeviceInfo.execute();
		
		}
	
	public boolean sendGetDeviceRequest(Integer userId) {
	     Integer roomIndex = null;
	    String getRoomUrl=CommonURL.getInstance().GetCommonURL+"/"+String.valueOf(userId)+"/rooms";
	    JsonParser.getRoomRequest(getRoomUrl);

		String getDeviceUrl=CommonURL.getInstance().GetCommonURL+"/"+
		String.valueOf(userId)+"/devices?roomid="+1+"&typeId=0";

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;
	
	}

public void setupDeviceListViewAdapter() {
//	rAdapter.clear();
	dAdapter = new DeviceListAdapter(getActivity(),R.layout.device_item_view, CommonValues.getInstance().deviceList);
	deviceListView.setAdapter(dAdapter);
	deviceListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity(), "Click Device", Toast.LENGTH_LONG).show();
			
		}
	});
	
}

	
	public boolean onBackPressed() {
		
			// CommonTask.CloseApplication(this);
//			backState = UserProfileSate.INITIAL_STATE;
			return false;
		

	}
	
	@Override
	public void onResume() {
		getSherlockActivity().setTitle("User Profile");
		fragmentPaused = false;
		super.onResume();
		
		

	}
	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
		super.onPause();
		fragmentPaused = true;
		
	}
	
	public void startProgress() {
//		userProfileProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress() {

//		userProfileProgressBar.setVisibility(View.INVISIBLE);
	}

	

}
