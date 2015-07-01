package com.sinepulse.app.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.VideoActivity;
import com.sinepulse.app.adapters.DeviceListAdapterByRoom;
import com.sinepulse.app.adapters.DeviceLogAdapter;
import com.sinepulse.app.adapters.RoomListAdapter;
import com.sinepulse.app.asynctasks.AsyncGetCurtainPresetValuesFromRoom;
import com.sinepulse.app.asynctasks.AsyncGetDevicePropertyFromRoom;
import com.sinepulse.app.asynctasks.AsyncGetDevices;
import com.sinepulse.app.asynctasks.AsyncGetLogFromRoomFragment;
import com.sinepulse.app.asynctasks.AsyncGetRoom;
import com.sinepulse.app.asynctasks.AsyncGetSetPropertyFromRoom;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.entities.Room;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * @author tac
 */

@EFragment(R.layout.room_status)
public class RoomManagerFragment extends SherlockFragment implements
		OnItemClickListener ,OnTouchListener{

	@ViewById(R.id.lvRoomList)
	public ListView roomListView;
	@ViewById(R.id.lvDeviceList)
	public ListView deviceListView;
	@ViewById(R.id.lvLogList)
	public ListView deviceLogListView;
	@ViewById(R.id.btAddRoom)
	public Button btAddRoom;
	@ViewById(R.id.btAddDevice)
	public Button btAddDevice;
	@ViewById(R.id.roomlistProgressBar)
	public ProgressBar roomlistProgressBar;
	@ViewById(R.id.devicelistProgressBar)
	public ProgressBar devicelistProgressBar;
	@ViewById(R.id.deviceLogProgressBar)
	public ProgressBar deviceLogProgressBar;
	@ViewById(R.id.devicePropertyProgressBar)
	public ProgressBar devicePropertyProgressBar;
	@ViewById(R.id.vfRoom)
	public static ViewFlipper vfRoom;
	@ViewById(R.id.ivDevice)
	public ImageView ivDevice;
	@ViewById(R.id.tvDeviceName)
	public TextView tvDeviceName;
	@ViewById(R.id.toggleButton)
	public ToggleButton toggleButton;
	@ViewById(R.id.btShowLog)
	public Button btShowLog;
	@ViewById(R.id.list_image)
	public ImageView list_image;
	@ViewById(R.id.seekBar1)
	public SeekBar seekBar1;
	@ViewById(R.id.porda)
	public RelativeLayout porda;
	@ViewById(R.id.toggleButton2)
	protected ToggleButton toggleButton2;
	@ViewById(R.id.tvEmptyLog)
	public TextView tvEmptyLog;
	@ViewById(R.id.tvToday)
	public TextView tvToday;
	@ViewById(R.id.tvYesterday)
	public TextView tvYesterday;
	@ViewById(R.id.etDateFrom)
	public EditText etDateFrom;
	@ViewById(R.id.etDateTo)
	public EditText etDateTo;
	@ViewById(R.id.bSearch)
	public Button bSearch;
	@ViewById(R.id.spinner1)
	public Spinner spinner1;
	@ViewById(R.id.up)
	public ImageView up;
	@ViewById(R.id.down)
	public ImageView down;
	@ViewById(R.id.right)
	public ImageView right;
	@ViewById(R.id.left)
	public ImageView left;
	@ViewById(R.id.pause)
	public ImageView pause;
	@ViewById(R.id.reset)
	public ImageView reset;
	@ViewById(R.id.calibration)
	public ImageView calibration;
	@ViewById(R.id.spinner_camera)
	public Spinner spCamera;
   @ViewById(R.id.MyStreamButton)
   public Button streamingButton;
   @ViewById(R.id.bRoom)
   public Button bRoom;
	String fromDate=null;
	String toDate=null;
	int seekBarProgressValue=0;
	int deviceId = 0;
	int curtainPropertyId = 0;
	int curtainMultiplier = 0;
	GestureDetector gestureDetector;
	boolean tapped;

	public static final int INITIAL_STATE = -1, DEVICE_STATE = 0,
			PROPERTY_STATE = 1, VIEWLOG_STATE = 2;
	public static Room roomManagerEntity = null;
	public static Device deviceManagerEntity = null;

	public enum RoomsState {
		INITIAL_STATE, // backState -1
		DEVICE_STATE, PROPERTY_STATE, VIEWLOG_STATE
	};

	public static RoomsState backState = RoomsState.INITIAL_STATE;
	private RoomListAdapter rAdapter;
	AsyncGetRoom asyncGetRoomInfo = null;
	AsyncGetDevices asyncGetDeviceInfo = null;
	AsyncGetLogFromRoomFragment asyncGetDeviceLogInfo = null;
	AsyncGetSetPropertyFromRoom asyncGetSetPropertyFromRoom=null;
	AsyncGetCurtainPresetValuesFromRoom asyncGetCurtainPresetValuesFromRoom=null;
	private DeviceLogAdapter dLogAdapter;
	@ViewById(R.id.tvDeviceLogHeadingText)
	public TextView tvDeviceLogHeadingText;

	private DeviceListAdapterByRoom dAdapter;

	/**
	 * Automatically call from menu select once initialize all controls
	 * initialize all controls pin all button for the particular item position
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    }

	@AfterViews
	void afterViewLoaded() {
		// setupListViewAdapter();
		backState = RoomsState.INITIAL_STATE;
		btAddRoom.setText("Room List");
		if (asyncGetRoomInfo != null) {
			asyncGetRoomInfo.cancel(true);
		}
//		asyncGetRoomInfo = new AsyncGetRoom(this,
//				CommonValues.getInstance().userId);
//		asyncGetRoomInfo.execute();

	}

	/*@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity.getApplicationContext();
	}*/

	public boolean sendGetRoomRequest(Integer userId) {

		String getRoomUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/rooms";

		if (JsonParser.getRoomRequest(getRoomUrl) != null) {
			return true;
		}
		return false;
	}

	public void setupRoomListViewAdapter() {
      if(CommonValues.getInstance().roomList!=null){
		rAdapter = new RoomListAdapter(getActivity(), R.layout.room_list_item,
				CommonValues.getInstance().roomList);
		roomListView.setAdapter(rAdapter);
		rAdapter.setTouchEnabled(false);
		roomListView.setEnabled(true);
		roomListView.setOnItemClickListener(this);
      }
		

	}

	public void resetRoomView() {
		/*backState = RoomsState.INITIAL_STATE;
		vfRoom.setDisplayedChild(0);
		// roomListView.setBackgroundColor(Color.RED);
		roomListView.setItemChecked(-1, true);*/
	}

	public void setupDeviceListViewAdapter() {
		// rAdapter.clear();
		if(CommonValues.getInstance().deviceList!=null){
		dAdapter = new DeviceListAdapterByRoom(getActivity(),
				R.layout.device_item_view,
				CommonValues.getInstance().deviceList);
		deviceListView.setAdapter(dAdapter);
		dAdapter.setTouchEnabled(false);
		deviceListView.setEnabled(true);
		deviceListView.setOnItemClickListener(this);
		}
		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {
		switch (parent.getId()) {
		case R.id.lvRoomList:
			getSherlockActivity().setTitle("Devices");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			vfRoom.setDisplayedChild(1);
			backState = RoomsState.DEVICE_STATE;
			rAdapter.setSelection(position);
			roomListView.setSelection(position);
			roomListView.setSelectionFromTop(position, view.getTop());
			roomManagerEntity = rAdapter.getItemAtPosition(position);
			btAddDevice.setText("  " + roomManagerEntity.Name);
			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.icon_room_medium, 0, 0, 0);
			// new DisplayRoomDetails(RoomManagerFragment.this,
			// roomManagerEntity.Id);
			LoadRoomDetailsContent();
			// displayFragment(7);
			break;
		case R.id.lvDeviceList:
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;
			dAdapter.setSelection(position);
			deviceListView.setSelection(position);
			deviceListView.setSelectionFromTop(position, view.getTop());
			deviceManagerEntity = dAdapter.getItemAtPosition(position);
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);
			deviceId = deviceManagerEntity.Id;
			break;

		default:
			break;
		}

	}

	AsyncGetDevicePropertyFromRoom asyncGetDeviceProperty = null;

	public void loadDeviceProperty(int DeviceTypeId, int deviceId) {
		if (asyncGetDeviceProperty != null) {
			asyncGetDeviceProperty.cancel(true);
		}
//		asyncGetDeviceProperty = new AsyncGetDevicePropertyFromRoom(this,
//				deviceManagerEntity.DeviceTypeId, deviceManagerEntity.Id);
//		asyncGetDeviceProperty.execute();
	}

	public boolean sendGetDevicePropertyRequest(int DeviceId) {

		String getDevicePropertyUrl = CommonURL.getInstance().GetCommonURL
				+ "/" + CommonValues.getInstance().userId + "/properties?id="
				+ DeviceId;
		if (JsonParser.getDevicePropertyRequest(getDevicePropertyUrl) != null) {
			return true;
		}
		return false;

	}

	public void setDevicePropertyControlData(int deviceTypeId) {
		rAdapter.clear();
		switch (deviceTypeId) {
		case 1:
			getSherlockActivity().setTitle("Fan Control");
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.fanlarge));
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			seekBar1.setVisibility(View.VISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 2:
			getSherlockActivity().setTitle("Light Control");
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.bulbon));
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			seekBar1.setVisibility(View.VISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 3:
			getSherlockActivity().setTitle("AC Control");
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.ac_large));
			seekBar1.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 4:
			spinner1.setVisibility(View.VISIBLE);
			loadCurtainPresetValues(CommonValues.getInstance().userId,deviceManagerEntity.Id);
			getSherlockActivity().setTitle("Curtain Control");
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.curtain_large));
			seekBar1.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.VISIBLE);
			tvDeviceName.setText(" ");
			break;

		default:
			break;
		}
		for (int i = 0; i < CommonValues.getInstance().devicePropertyList
				.size(); i++) {
			setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
					.get(i));
		}
	}
	private void loadCurtainPresetValues(int userId,int deviceId) {
		if (asyncGetCurtainPresetValuesFromRoom != null) {
			asyncGetCurtainPresetValuesFromRoom.cancel(true);
		}
//		asyncGetCurtainPresetValuesFromRoom = new AsyncGetCurtainPresetValuesFromRoom(this,userId,deviceId);
//		asyncGetCurtainPresetValuesFromRoom.execute();
		
	}

	public void setDevicePropertyValue(final DeviceProperty property) {
		switch (property.PropertyId) {
		case 1://OnOff
			if (property.IsActionPending) {
				if(property.PendingValue.equals("1")){
					toggleButton2.setChecked(true);
					}else{
						toggleButton2.setChecked(false);
					}
				toggleButton2.setEnabled(false);
			}else{
				if(property.Value.equals("1")){
					toggleButton2.setChecked(true);
					}else{
						toggleButton2.setChecked(false);
					}
					toggleButton2.setEnabled(true);
			}
			if(property.Value.equals("1")){
				list_image.setImageResource(R.drawable.greenled_large);
			}else{
				list_image.setImageResource(R.drawable.redled_large);
			}
			break;
		case 2://Dimming
			if (property.IsActionPending) {
				seekBar1.setProgress(Integer.parseInt(property.getPendingValue()));
				seekBar1.setEnabled(false);
			} else {
				seekBar1.setProgress(Integer.parseInt(property
						.getValue()));
				seekBar1.setEnabled(true);
			}

			break;
		case 3://Dimming
			if (property.IsActionPending) {
				seekBar1.setProgress(Integer.parseInt(property.getPendingValue()));
				seekBar1.setEnabled(false);
			} else {
				seekBar1.setProgress(Integer.parseInt(property
						.getValue()));
				seekBar1.setEnabled(true);
			}
			break;
		default:
			break;
		}
     toggleButton2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sendSetProperty(CommonValues.getInstance().userId,(isChecked?1:0),1,property.DeviceId);
			}
		});
     seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				seekBarProgressValue=seekBar.getProgress();
				if(deviceManagerEntity.DeviceTypeId==1){
				sendSetProperty(CommonValues.getInstance().userId,seekBarProgressValue,3,property.DeviceId);
				}else if(deviceManagerEntity.DeviceTypeId==2){
					sendSetProperty(CommonValues.getInstance().userId,seekBarProgressValue,2,property.DeviceId);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
//				seekBarProgress=progress;
//				sendSetPropertyForSeekBar(CommonValues.getInstance().userId,progress,2,property.DeviceId);
			}
		});
        up.setOnTouchListener(this);
		down.setOnTouchListener(this);
		right.setOnTouchListener(this);
		left.setOnTouchListener(this);
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty(CommonValues.getInstance().userId, 1, deviceId,
						7);
			}
		});
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty(CommonValues.getInstance().userId, 1, deviceId,
						8);
				reset.setEnabled(false);
			}
		});
		calibration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty(CommonValues.getInstance().userId, 1, deviceId,
						9);
				calibration.setEnabled(false);
			}
		});
	}
	public void sendSetProperty(int userId,int value,
			int propertyId,int DeviceId) {
		if (asyncGetSetPropertyFromRoom != null) {
			asyncGetSetPropertyFromRoom.cancel(true);
		}
//		asyncGetSetPropertyFromRoom = new AsyncGetSetPropertyFromRoom(this,userId,DeviceId,propertyId,value);
//		asyncGetSetPropertyFromRoom.execute();
		
	}
	public boolean setPropertyRequestFromRoom(int userId, int deviceId,int propertyId, int value) {
		String setPropertyUrl = CommonURL.getInstance().GetCommonURL
				+ "/" + userId + "/property?id="+ deviceId+"&propertyid="+propertyId+"&value="+value;
//		if (JsonParser.setProptyerRequest(setPropertyUrl) != null) {
//			return true;
//		}
		return false;
		
	}

	public void setPropertyResponseData() {
		if(CommonValues.getInstance().devicePropertyList!=null){
		for (int i = 0; i < CommonValues.getInstance().devicePropertyList
				.size(); i++) {
			setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
					.get(i));
		}
		}
		
	}
	

	private void LoadRoomDetailsContent() {
		if (asyncGetDeviceInfo != null) {
			asyncGetDeviceInfo.cancel(true);
		}
//		asyncGetDeviceInfo = new AsyncGetDevices(this,
//				CommonValues.getInstance().userId);
//		asyncGetDeviceInfo.execute();

	}

	private void LoadDeviceLogContent(int deviceId,int FilterType, String fromDate,String toDate) {
		if (asyncGetDeviceLogInfo != null) {
			asyncGetDeviceLogInfo.cancel(true);
		}
//		asyncGetDeviceLogInfo = new AsyncGetLogFromRoomFragment(this,
//				deviceId,FilterType,fromDate,toDate);
//		asyncGetDeviceLogInfo.execute();

	}

	public boolean sendGetDeviceLogRequest(Integer deviceId,int FilterType, String fromDate,String toDate) {

		String getDeviceLogUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ CommonValues.getInstance().userId + "/device/"
				+ String.valueOf(deviceId) + "/activities";
		/*if (JsonParser.postDeviceLogRequest(getDeviceLogUrl,FilterType, fromDate, toDate) != null) {
			return true;
		}*/
		return false;

	}

	public void setupDeviceLogAdapter() {
		if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
			tvEmptyLog.setVisibility(View.INVISIBLE);
			dLogAdapter = new DeviceLogAdapter(getActivity(),
					R.layout.log_list_item,
					CommonValues.getInstance().deviceLogDetailList);
			deviceLogListView.setAdapter(dLogAdapter);
			dLogAdapter.setTouchEnabled(false);
			deviceLogListView.setEnabled(true);
		} else {
			tvEmptyLog.setVisibility(View.VISIBLE);
			tvEmptyLog.setText("Sorry! No Log Available");
		}

	}
	Dialog dialog;
	private void showCalendar(final View v) {
		dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.delivery_date);
		dialog.setCancelable(true);

		final CalendarView calendarView1 = (CalendarView) dialog
				.findViewById(R.id.calendarView1);

		calendarView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calendarView1.getDate();

			}
		});
		calendarView1.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			@SuppressWarnings("deprecation")
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
				tvToday.setTextColor(Color.parseColor("#bdbdbd"));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				if (etDateFrom != null && v.getId() == R.id.etDateFrom) {
					fromDate=formatter.format(new Date(year - 1900, month, dayOfMonth)).toString();
					String delims ="T" ;
				    String[] tokens = fromDate.split(delims);
					etDateFrom.setText(tokens[0]);
				}
				if (etDateTo != null && v.getId() == R.id.etDateTo) {
					 toDate=formatter.format(new Date(year - 1900, month, dayOfMonth)).toString();
						String delims ="T" ;
					    String[] tokens = toDate.split(delims);
						etDateTo.setText(tokens[0]);
						bSearch.setVisibility(View.VISIBLE);
				}
				// dialog.cancel();
			}
		});

		// Date ok button
		Button button = (Button) dialog.findViewById(R.id.Button01);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();

			}
		});
		// Date cancel button
		Button button2 = (Button) dialog.findViewById(R.id.Button02);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();

			}
		});

		// now that the dialog is set up, it's time to show it
		dialog.show();
		
		  bSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
						deviceLogListView.setAdapter(null);
						dLogAdapter.clear();
				        }
					LoadDeviceLogContent(deviceManagerEntity.Id,3,fromDate,toDate);
					
				}
			});

	}

	public boolean sendGetDeviceRequest(Integer userId) {
		Integer roomIndex = roomManagerEntity.Id;
		String getRoomUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/rooms";
		JsonParser.getRoomRequest(getRoomUrl);

		String getDeviceUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/devices?roomid="
				+ CommonValues.getInstance().roomList.get(roomIndex).getId()
				+ "&typeId=0";

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;

	}

	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
//		spinner1.setEnabled(false);
		super.onPause();
		// updateBasket();

	}

	public boolean onBackPressed() {
		/*
		 * if (currentFragment == CAMERA_FRAGMENT) { if (cameraStreamFragment !=
		 * null) { if (cameraStreamFragment.onBackPressed() == false) {
		 * fragmentBackStack.clear(); goBackToDashboard(); } } }
		 */
		vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
		vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
		boolean backHandled = true;
		switch (backState) {
		case INITIAL_STATE:
			MainActionbarBase.currentMenuIndex = MainActionbarBase
					.getLastIndex();
			backHandled = false;
			break;
		case DEVICE_STATE:
			getSherlockActivity().setTitle("Rooms");
			vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
			vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
			backState = RoomsState.INITIAL_STATE;
			vfRoom.setDisplayedChild(0);
			break;
		case PROPERTY_STATE:
			getSherlockActivity().setTitle("Devices");
			vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
			vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
			backState = RoomsState.DEVICE_STATE;
			vfRoom.setDisplayedChild(1);
			break;
		case VIEWLOG_STATE:
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}
			etDateFrom.setText("");
			etDateTo.setText("");
			bSearch.setVisibility(View.INVISIBLE);
			getSherlockActivity().setTitle("Device Control");
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;

		default:
			break;
		}

		return backHandled;

	}

	@Override
	public void onResume() {
		getSherlockActivity().setTitle(R.string.create_room);
		backState = RoomsState.INITIAL_STATE;
		bRoom.setBackground(getResources().getDrawable(R.drawable.roomselected));
		super.onResume();

	}

	public void startProgress() {
		roomlistProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != roomlistProgressBar && roomlistProgressBar.isShown()) {

			roomlistProgressBar.setVisibility(View.GONE);
		}
	}

	public void startDeviceProgress() {
		devicelistProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDeviceProgress() {
		if (null != devicelistProgressBar && devicelistProgressBar.isShown()) {

			devicelistProgressBar.setVisibility(View.GONE);
		}

	}

	@Click({ R.id.bCamera, R.id.bRoom, R.id.bDashboard, R.id.btShowLog,R.id.etDateFrom, R.id.etDateTo,R.id.tvToday,R.id.tvYesterday })
	public void onClick(View v) {
//		fragmentBackStack.clear();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		 Date d=new Date();
		switch (v.getId()) {
		case R.id.bCamera:
			asyncGetRoomInfo.cancel(true);
//			Home.mDrawerList.setItemChecked(7, true);
//			Home.navDrawerAdapter.setSelectedPosition(7);
//			currentFragment = CAMERA_FRAGMENT;
//			displayFragment(7);
//			((MainActionbarBase) getActivity()).displayFragment(7);
			Intent cameraIntent = new Intent(this.getActivity(), VideoActivity.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);

			break;
		case R.id.bRoom:
//			Home.mDrawerList.setItemChecked(2, true);
//			Home.navDrawerAdapter.setSelectedPosition(2);
//			currentFragment = ROOM_MANAGER_FRAGMENT;
//			displayFragment(2);
			break;
		case R.id.bDashboard:
			asyncGetRoomInfo.cancel(true);
			Home.mDrawerList.setItemChecked(0, true);
			Home.navDrawerAdapter.setSelectedPosition(0);
			goBackToDashboard();
//			((MainActionbarBase) getActivity()).displayFragment(0);
			break;
		case R.id.btShowLog:
			getSherlockActivity().setTitle("Device Log");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			backState = RoomsState.VIEWLOG_STATE;
			vfRoom.setDisplayedChild(3);
			tvDeviceLogHeadingText.setText("  " + deviceManagerEntity.Name);
			if (deviceManagerEntity.DeviceTypeId == 1) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.fanmedium, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 2) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.bulbmedium, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 3) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.ac_medium, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 4) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.curtainmedium, 0, 0, 0);
			}
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			LoadDeviceLogContent(deviceManagerEntity.Id,1,formatter.format(d).toString(),formatter.format(d).toString());
			break;
		case R.id.etDateFrom:
		case R.id.etDateTo:
			showCalendar(v);
			break;
		case R.id.tvToday:
			etDateFrom.setText("");
			etDateTo.setText("");
			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
		        }
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId,1,formatter.format(d).toString(),formatter.format(d).toString());
			break;
		case R.id.tvYesterday:
			if(tvEmptyLog.isShown()){
				tvEmptyLog.setVisibility(View.GONE);
			}
			etDateFrom.setText("");
			etDateTo.setText("");
			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#2C5197"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
		        }
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId,2,formatter.format(d).toString(),formatter.format(d).toString());
			break;


		default:
			break;
		}

	}

	/**
	 * 
	 */
	public void goBackToDashboard() {
//		currentFragment = ALLDEVICE_FRAGMENT;
		Intent homeIntent = new Intent(this.getActivity(), Home.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(homeIntent);
	}

	public void startLogProgress() {
		deviceLogProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopLogProgress() {
		if (null != deviceLogProgressBar && deviceLogProgressBar.isShown()) {

			deviceLogProgressBar.setVisibility(View.GONE);
		}
	}

	public void startDevicePropertyProgress() {
		devicePropertyProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDevicePropertyProgress() {
		if (null != devicePropertyProgressBar
				&& devicePropertyProgressBar.isShown()) {

			devicePropertyProgressBar.setVisibility(View.GONE);
		}
	}
	
	public boolean sendCurtainPresetRequest(int userId, int deviceId) {
		String curtainPresetUrl=CommonURL.getInstance().GetCommonURL
		+ "/" + userId +"/devices/"+deviceId+"/presets";
		if (JsonParser.setPresetRequest(curtainPresetUrl) != null) {
			return true;
		}
		return false;
		
		
	}

	public void setCurtainPresetResponseData() {
		String[] presetValues = getCurtainPresetValues();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),R.layout.spinner_item, presetValues);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		}

	/**
	 * @return
	 */
	private String[] getCurtainPresetValues() {
		String[] presetArray=new String[CommonValues.getInstance().presetList.size()];
		for(int i=0;i<CommonValues.getInstance().presetList.size();i++){
			presetArray[i]=CommonValues.getInstance().presetList.get(i).getName();
		}
		return presetArray;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {
		case R.id.up:
			curtainMultiplier = 1;
			curtainPropertyId = 4;
			break;
		case R.id.down:
			curtainMultiplier = -1;
			curtainPropertyId = 4;
			break;
		case R.id.right:
			curtainMultiplier = 1;
			curtainPropertyId = 5;
			break;
		case R.id.left:
			curtainMultiplier = -1;
			curtainPropertyId = 5;
			break;
		default:
			break;

		}
		return gestureDetector.onTouchEvent(event);
		// false;
	}

	public class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {

			return true;
		}

		// event when double tap occurs
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.d("Tap", "Double");
			sendSetProperty(CommonValues.getInstance().userId,
					10 * curtainMultiplier, deviceId, curtainPropertyId);
			return true;
		}

		// event when single tap occurs
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.d("Tap", "Single");
			sendSetProperty(CommonValues.getInstance().userId,
					5 * curtainMultiplier, deviceId, curtainPropertyId);
			return super.onSingleTapUp(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Log.d("Tap", "Long");
			sendSetProperty(CommonValues.getInstance().userId,
					15 * curtainMultiplier, deviceId, curtainPropertyId);
			super.onLongPress(e);
		}
	}
}
