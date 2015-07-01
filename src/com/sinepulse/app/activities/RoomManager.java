package com.sinepulse.app.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.activities.CircularSeekBar.OnCircularSeekBarChangeListener;
import com.sinepulse.app.adapters.DeviceListAdapterByRoom;
import com.sinepulse.app.adapters.DeviceLogAdapter;
import com.sinepulse.app.adapters.RoomListAdapter;
import com.sinepulse.app.asynctasks.AsyncGetCurtainPresetValuesFromRoom;
import com.sinepulse.app.asynctasks.AsyncGetDeviceLogInfo;
import com.sinepulse.app.asynctasks.AsyncGetDevicePropertyFromRoom;
import com.sinepulse.app.asynctasks.AsyncGetDevices;
import com.sinepulse.app.asynctasks.AsyncGetLogFromRoomFragment;
import com.sinepulse.app.asynctasks.AsyncGetRoom;
import com.sinepulse.app.asynctasks.AsyncGetSetPropertyFromRoom;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.entities.Room;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

@EActivity(R.layout.room_status)
public class RoomManager extends MainActionbarBase implements OnClickListener,
		OnTouchListener, OnItemClickListener {

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
	@ViewById(R.id.bCamera)
	public Button bCamera;
	@ViewById(R.id.roomlistProgressBar)
	public ProgressBar roomlistProgressBar;
	@ViewById(R.id.devicelistProgressBar)
	public static ProgressBar devicelistProgressBar;
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
	@ViewById(R.id.btShowLog)
	public Button btShowLog;
	@ViewById(R.id.list_image)
	public ImageView list_image;
	@ViewById(R.id.seekBar1)
	public SeekBar seekBar1;
	@ViewById(R.id.porda)
	public RelativeLayout porda;
	@ViewById(R.id.knob)
	public RelativeLayout knob;
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
	@ViewById(R.id.tvProgressValue)
	public TextView tvProgressValue;
	String fromDate = "";
	String toDate = "";
	int seekBarProgressValue = 0;
	int deviceId = 0;
	int curtainPropertyId = 0;
	int curtainMultiplier = 0;
	GestureDetector gestureDetector;
	boolean tapped;
	public static final int INITIAL_STATE = -1, DEVICE_STATE = 0,
			PROPERTY_STATE = 1, VIEWLOG_STATE = 2;
	public static Room roomManagerEntity = null;
	public static Device deviceManagerEntity = null;
	public static Context context;

	public enum RoomsState {
		INITIAL_STATE, // backState -1
		DEVICE_STATE, PROPERTY_STATE, VIEWLOG_STATE
	};

	public static RoomsState backState = RoomsState.INITIAL_STATE;
	private RoomListAdapter rAdapter;
	AsyncGetRoom asyncGetRoomInfo = null;
	static AsyncGetDevices asyncGetDeviceInfo = null;
	AsyncGetLogFromRoomFragment asyncGetDeviceLogInfo = null;
	AsyncGetSetPropertyFromRoom asyncGetSetPropertyFromRoom = null;
	AsyncGetCurtainPresetValuesFromRoom asyncGetCurtainPresetValuesFromRoom = null;
	private DeviceLogAdapter dLogAdapter;
	@ViewById(R.id.tvDeviceLogHeadingText)
	public TextView tvDeviceLogHeadingText;
	@ViewById(R.id.circularSeekBar1)
	public CircularSeekBar circularSeekBar1;
	@ViewById(R.id.tvCircleProgressValue)
	public TextView tvCircleProgressValue;
	

	public DeviceListAdapterByRoom dAdapter;
	boolean isRoomVisible = false;
	int deviceTypeId;
	int roomid;
	boolean shouldSetPreset = false;
	int presetItemPosition;

	SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d = new Date();
	int year;
	int month;
	int day;
	private DatePickerDialog fromDatePickerDialog = null;
	private DatePickerDialog toDatePickerDialog = null;
	private SimpleDateFormat dateFormatter;
	RoundKnobButton rv;
	TextView tv2;
	RelativeLayout panel;
	Date tobesetFromDate = new Date();
	Date tobesetToDate = new Date();
	int FilterType=0;
	String fromsDate="";
	String tosDate="";
	int PageNumber=0;
	int ChunkSize=30;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RoomManager.context = this;
		backState = RoomsState.INITIAL_STATE;
		createMenuBar();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

	}

	@AfterViews
	void afterViewLoaded() {
		gestureDetector = new GestureDetector(this, new GestureListener());
		backState = RoomsState.INITIAL_STATE;
		vfRoom.setDisplayedChild(0);
//		btAddRoom.setText("Room List");
		fromDate = dateFormatter.format(d).toString();
		toDate = dateFormatter.format(d).toString();
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_All_Room;
		if (asyncGetRoomInfo != null) {
			asyncGetRoomInfo.cancel(true);
		}
		asyncGetRoomInfo = new AsyncGetRoom(this,
				CommonValues.getInstance().userId);
		asyncGetRoomInfo.execute();
	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		mSupportActionBar.setTitle(R.string.create_room);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
		setConnectionNodeImage(menu);
		return prepared;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
		}
		if (item.getItemId() == R.id.menu_refresh) {
			// Toast.makeText(this, "Please Wait..Page is Refreshing",
			// Toast.LENGTH_SHORT).show();
			switch (vfRoom.getDisplayedChild()) {
			case 0:
				Toast.makeText(this, "Please Wait..Refreshing Room List",
						Toast.LENGTH_SHORT).show();
				if (asyncGetRoomInfo != null) {
					asyncGetRoomInfo.cancel(true);
				}
				asyncGetRoomInfo = new AsyncGetRoom(this,
						CommonValues.getInstance().userId);
				asyncGetRoomInfo.execute();
				break;
			case 1:
				Toast.makeText(this, "Please Wait..Refreshing Device List",
						Toast.LENGTH_SHORT).show();
				LoadRoomDetailsContent(roomid);
				break;
			case 2:
				Toast.makeText(this, "Please Wait..Refreshing Property",
						Toast.LENGTH_SHORT).show();
				loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
						deviceManagerEntity.Id);
				break;
			case 3:
				Toast.makeText(this, "Please Wait..Refreshing Device Log",
						Toast.LENGTH_SHORT).show();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				Date d = new Date();
				FilterType=1;
				fromsDate=formatter.format(d).toString();
				tosDate=formatter.format(d).toString();
				PageNumber=1;
				LoadDeviceLogContent(deviceManagerEntity.Id, FilterType, fromsDate, tosDate,PageNumber,ChunkSize);
				break;
			default:
				break;
			}
		}
		return true;
	}

	public boolean sendGetRoomRequest(Integer userId) {

//		String getRoomUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ String.valueOf(userId) + "/rooms";
		String getRoomUrl = CommonURL.getInstance().RootUrl + "rooms";

		if (JsonParser.getRoomRequest(getRoomUrl) != null && JsonParser.getRoomRequest(getRoomUrl) !="") {
			return true;
		}else{
			RoomManager.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowNetworkChangeConfirmation(RoomManager.this, "Network State has changed.Please log in again to continue.", showNetworkChangeEvent());
					asyncGetRoomInfo.cancel(true);
				}
			});
			
		return false;
		}
	}

	public void setupRoomListViewAdapter() {
		if (CommonValues.getInstance().roomList != null && CommonValues.getInstance().roomList.size()>0 )
		{
			rAdapter = new RoomListAdapter(this, R.layout.room_list_item,
					CommonValues.getInstance().roomList);
			roomListView.setAdapter(rAdapter);
			rAdapter.setTouchEnabled(false);
			roomListView.setEnabled(true);
			roomListView.setOnItemClickListener(this);
		} else {
			CommonTask.ShowMessage(this, "No Data returned from server.");
		}

	}

	public void resetRoomView() {
		/*
		 * backState = RoomsState.INITIAL_STATE; vfRoom.setDisplayedChild(0); //
		 * roomListView.setBackgroundColor(Color.RED);
		 * roomListView.setItemChecked(-1, true);
		 */
	}

	public void setupDeviceListViewAdapter() {
		// rAdapter.clear();
		if (CommonValues.getInstance().deviceList != null && CommonValues.getInstance().deviceList.size()>0) {
			dAdapter = new DeviceListAdapterByRoom(this, R.layout.device_item_view,
					CommonValues.getInstance().deviceList);
			deviceListView.setAdapter(dAdapter);
			dAdapter.setTouchEnabled(false);
			deviceListView.setEnabled(true);
			deviceListView.setOnItemClickListener(this);
		} else {
			CommonTask.ShowMessage(this, "No Data returned from Server");
//			CommonTask
//			.ShowNetworkChangeConfirmation(
//					this,
//					"Network State has been changed.Please log in again to continue.",
//					showNetworkChangeEvent());
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {
		switch (parent.getId()) {
		case R.id.lvRoomList:
			getSupportActionBar().setTitle("Devices");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			vfRoom.setDisplayedChild(1);
			backState = RoomsState.DEVICE_STATE;
			rAdapter.setSelection(position);
			roomListView.setSelection(position);
			roomListView.setSelectionFromTop(position, view.getTop());
			roomManagerEntity = rAdapter.getItemAtPosition(position);
			btAddDevice.setText("  " + roomManagerEntity.Name);
			roomid = roomManagerEntity.getId();
			if(CommonValues.getInstance().deviceList!=null){
				CommonValues.getInstance().deviceList.clear();
				deviceListView.setAdapter(null);
				}
			LoadRoomDetailsContent(roomid);

			// displayFragment(7);
			break;
		case R.id.lvDeviceList:
//			getSupportActionBar().setTitle("Device Control");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;
			dAdapter.setSelection(position);
			deviceListView.setSelection(position);
			deviceListView.setSelectionFromTop(position, view.getTop());
			deviceManagerEntity = dAdapter.getItemAtPosition(position);
			deviceTypeId = deviceManagerEntity.DeviceTypeId;
			if(deviceTypeId==1){
				getSupportActionBar().setTitle("Fan Control");
				}else if(deviceTypeId==2){
					getSupportActionBar().setTitle("Light Control");
				}else if(deviceTypeId==3){
					getSupportActionBar().setTitle("Ac Control");
				}else if(deviceTypeId==4){
					getSupportActionBar().setTitle("Curtain Control");
				}

			loadDeviceProperty(deviceTypeId, deviceManagerEntity.Id);
			if (deviceManagerEntity.DeviceTypeId == 4) {
				shouldSetPreset = true;
			}
			deviceId = deviceManagerEntity.Id;
			break;

		default:
			break;
		}

	}

	AsyncGetDevicePropertyFromRoom asyncGetDeviceProperty = null;

	public void loadDeviceProperty(int DeviceTypeId, int deviceId) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Properties;
		if (asyncGetDeviceProperty != null) {
			asyncGetDeviceProperty.cancel(true);
		}
		asyncGetDeviceProperty = new AsyncGetDevicePropertyFromRoom(this,
				deviceManagerEntity.DeviceTypeId, deviceManagerEntity.Id);
		asyncGetDeviceProperty.execute();
	}

	public boolean sendGetDevicePropertyRequest(int DeviceId) {

//		String getDevicePropertyUrl = CommonURL.getInstance().GetCommonURL
//				+ "/" + CommonValues.getInstance().userId + "/properties?id="
//				+ DeviceId;
		String getDevicePropertyUrl = CommonURL.getInstance().RootUrl + "properties?deviceId="+ DeviceId;
		
		if (JsonParser.getDevicePropertyRequest(getDevicePropertyUrl) != null && JsonParser.getDevicePropertyRequest(getDevicePropertyUrl) !="") {
			return true;
		}else{
			RoomManager.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowNetworkChangeConfirmation(RoomManager.this, "Network State has changed.Please log in again to continue.", showNetworkChangeEvent());
					asyncGetDeviceProperty.cancel(true);
				}
			});
			
		return false;
		}

	}

	public void setDevicePropertyControlData(int deviceTypeId) {
		// rAdapter.clear();
//		hideRotatingKnob();
		switch (deviceTypeId) {
		case 1:// Fan
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			knob.setVisibility(View.VISIBLE);
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 2:// Light
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			knob.setVisibility(View.VISIBLE);
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 3:// AC
			// hideRotatingKnob();
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.ac_large));
			knob.setVisibility(View.INVISIBLE);
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 4:// Curtain
			// hideRotatingKnob();
			spinner1.setVisibility(View.VISIBLE);
			loadCurtainPresetValues(deviceManagerEntity.Id);
			knob.setVisibility(View.INVISIBLE);
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.VISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			break;

		default:
			break;
		}
		setDevicePropertyData();
	}

	

	private void loadCurtainPresetValues(int deviceId) {
		if (asyncGetCurtainPresetValuesFromRoom != null) {
			asyncGetCurtainPresetValuesFromRoom.cancel(true);
		}
		asyncGetCurtainPresetValuesFromRoom = new AsyncGetCurtainPresetValuesFromRoom(
				this,deviceId);
		asyncGetCurtainPresetValuesFromRoom.execute();

	}

	public void setDevicePropertyValues(DeviceProperty property) {

		switch (property.PropertyId) {
		case 1:// OnOff
			setHeaderImage(property.Value);
			toggleButton2.setOnCheckedChangeListener(null);
			if (property.IsActionPending) {
				if (property.PendingValue.equals("1")) {
					toggleButton2.setChecked(true);
				} else {
					toggleButton2.setChecked(false);
				}
				toggleButton2.setEnabled(false);
			} else {
				if (property.Value.equals("1")) {
					toggleButton2.setChecked(true);
				} else {
					toggleButton2.setChecked(false);
				}
				toggleButton2.setEnabled(true);
			}
			if (property.Value.equals("1")) {
				list_image.setImageResource(R.drawable.greenled_large);
			} else {
				list_image.setImageResource(R.drawable.redled_large);
			}
			break;
		case 2:// Dimming Light & Fan
			circularSeekBar1.setOnSeekBarChangeListener(null);
			if (property.IsActionPending) {
				/*if(shouldResend==true){
				CommonTask.ShowMessage(RoomManager.this, "Previous Dimming request has not completed yet");
				}*/
				circularSeekBar1.setProgress(Integer.parseInt(property
						  .getPendingValue()));
				tvCircleProgressValue.setText(property.getPendingValue() + " %");
				setSeekbarInactiveColor();
				
			}else{
				circularSeekBar1.setProgress(Integer.parseInt(property.getValue()));
				tvCircleProgressValue.setText(property.getValue() + " %");
				setSeekbarActiveColour();
			}

			break;
		case 3:// Dimming Fan(Knob)
			/*
			  seekBar1.setOnSeekBarChangeListener(null); if
			  (property.IsActionPending) {
			  seekBar1.setProgress(Integer.parseInt(property
			  .getPendingValue())); seekBar1.setEnabled(false);
			  tvProgressValue.setText("Dimming : "+property.getPendingValue() +
			  " %"); } else {
			 seekBar1.setProgress(Integer.parseInt(property.getValue()));
			  seekBar1.setEnabled(true);
			  tvProgressValue.setText("Dimming : "+property.getValue() + " %");
			  }
			 */
			break;
		case 6:// Preset
			if (property.IsActionPending) {
				for (int i = 0; i < CommonValues.getInstance().presetList
						.size(); i++) {
					if (CommonValues.getInstance().presetList.get(i).Id == Integer
							.parseInt(property.PendingValue)) {
						presetItemPosition = i;
						break;
					}
				}
			} else {
				for (int i = 0; i < CommonValues.getInstance().presetList
						.size(); i++) {
					if (CommonValues.getInstance().presetList.get(i).Id == Integer
							.parseInt(property.Value)) {
						presetItemPosition = i;
						break;
					}
				}

			}
			break;
		default:
			break;
		}
		toggleButton2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				sendSetProperty((isChecked ? 1 : 0), deviceId, 1);
				buttonView.setEnabled(false);
			}
		});
		/*
		 * seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		 * 
		 * @Override public void onStopTrackingTouch(SeekBar seekBar) {
		 * seekBarProgressValue = seekBar.getProgress();
		 * seekBar1.setEnabled(false); if (deviceManagerEntity.DeviceTypeId ==
		 * 1) { sendSetProperty(CommonValues.getInstance().userId,
		 * seekBarProgressValue, deviceId, 3); } else if
		 * (deviceManagerEntity.DeviceTypeId == 2) {
		 * sendSetProperty(CommonValues.getInstance().userId,
		 * seekBarProgressValue, deviceId, 2); }
		 * tvProgressValue.setText("Dimming : "
		 * +String.valueOf(seekBarProgressValue) + " %"); }
		 * 
		 * @Override public void onStartTrackingTouch(SeekBar seekBar) { }
		 * 
		 * @Override public void onProgressChanged(SeekBar seekBar, int
		 * progress, boolean fromUser) { // seekBarProgress=progress; //
		 * sendSetPropertyForSeekBar
		 * (CommonValues.getInstance().userId,progress,2,property.DeviceId); }
		 * });
		 */
		circularSeekBar1.setOnSeekBarChangeListener(new OnCircularSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(CircularSeekBar seekBar) {
				seekBarProgressValue = circularSeekBar1.getProgress();
				if(shouldResend==true){
					CommonTask.ShowMessage(RoomManager.this, "Previous Dimming request has not completed yet");
					setSeekbarInactiveColor();
					return;
				}else{
					setSeekbarActiveColour();
				if (deviceManagerEntity.DeviceTypeId == 1) {
					sendSetProperty(
							seekBarProgressValue, deviceId, 2);
					
				} else if (deviceManagerEntity.DeviceTypeId == 2) {
					sendSetProperty(seekBarProgressValue, deviceId, 2);
					
				}
				}
				tvCircleProgressValue.setText(String.valueOf(seekBarProgressValue) + " %");
				
				
				
			}

			
			
			@Override
			public void onStartTrackingTouch(CircularSeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(CircularSeekBar circularSeekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		up.setOnTouchListener(this);
		down.setOnTouchListener(this);
		right.setOnTouchListener(this);
		left.setOnTouchListener(this);
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty(1, deviceId,
						7);
			}
		});
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty( 1, deviceId,
						8);
				reset.setEnabled(false);
			}
		});
		calibration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSetProperty(1, deviceId,
						9);
				calibration.setEnabled(false);
			}
		});
	}
    public boolean shouldResend=false;
	public void sendSetProperty( int value, int DeviceId,
			int propertyId) {
		shouldResend=true;
		if (asyncGetSetPropertyFromRoom != null) {
			asyncGetSetPropertyFromRoom.cancel(true);
		}
		asyncGetSetPropertyFromRoom = new AsyncGetSetPropertyFromRoom(this,
				DeviceId, propertyId, value);
		asyncGetSetPropertyFromRoom.execute();

	}

	public boolean setPropertyRequestFromRoom(int deviceId,
			int propertyId, int value) {
//		String setPropertyUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ userId + "/property?id=" + deviceId + "&propertyid="
//				+ propertyId + "&value=" + value;
//		System.out.println(setPropertyUrl);
		String setPropertyUrl=CommonURL.getInstance().RootUrl+"property";
		if (JsonParser.postSetPropertyRequest(setPropertyUrl,deviceId,propertyId,value) != null && JsonParser.postSetPropertyRequest(setPropertyUrl,deviceId,propertyId,value) !="") {
			return true;
		}else{
			RoomManager.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowNetworkChangeConfirmation(RoomManager.this, "Network State has changed.Please log in again to continue.", showNetworkChangeEvent());
					asyncGetSetPropertyFromRoom.cancel(true);
				}
			});
			
		return false;
		}

	}

	/*public void setPropertyResponseData() {
		assignDevicePropertyFromRoom();
	}*/

	/**
	 * 
	 */
	public void setDevicePropertyData() {
		if (CommonValues.getInstance().devicePropertyList != null && CommonValues.getInstance().devicePropertyList.size()>0) {
			for (int i = 0; i < CommonValues.getInstance().devicePropertyList
					.size(); i++) {
				setDevicePropertyValues(CommonValues.getInstance().devicePropertyList
						.get(i));
			}
		} else {
			CommonTask.ShowMessage(this, "No Data Returned From Server.");
		}
	}

	public static void LoadRoomDetailsContent(int roomid) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_All_Device;
		if (asyncGetDeviceInfo != null) {
			asyncGetDeviceInfo.cancel(true);
		}
		asyncGetDeviceInfo = new AsyncGetDevices(RoomManager.context,
				CommonValues.getInstance().userId, roomid);
		asyncGetDeviceInfo.execute();

	}

	private void LoadDeviceLogContent(int deviceId, int FilterType,
			String fromsDate, String tosDate,int PageNumber,int ChunkSize) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Activities;
		if (asyncGetDeviceLogInfo != null) {
			asyncGetDeviceLogInfo.cancel(true);
		}
		asyncGetDeviceLogInfo = new AsyncGetLogFromRoomFragment(this, deviceId,
				FilterType, fromsDate, tosDate,PageNumber,ChunkSize);
		asyncGetDeviceLogInfo.execute();

	}

	public boolean sendGetDeviceLogRequest(int deviceId, int FilterType,
			String fromsDate, String tosDate,int PageNumber,int ChunkSize) {

//		String getDeviceLogUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ CommonValues.getInstance().userId + "/device/"
//				+ String.valueOf(deviceId) + "/activities";
		String getDeviceLogUrl = CommonURL.getInstance().RootUrl  +"deviceactivities";
		if (JsonParser.postDeviceLogRequest(getDeviceLogUrl,deviceId, FilterType,
				fromsDate, tosDate,PageNumber,ChunkSize,shouldAppendList) != null && JsonParser.postDeviceLogRequest(getDeviceLogUrl,deviceId, FilterType,
						fromDate, toDate,PageNumber,ChunkSize,shouldAppendList) !="") {
			return true;
		}else{
			RoomManager.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
//					CommonTask.ShowMessage(RoomManager.this, "No Data Returned From Server.");
					CommonTask.ShowMessage(RoomManager.this, " End of List.No more data available for your search parameter.");
//					CommonTask.ShowNetworkChangeConfirmation(RoomManager.this, "Network State has changed.Please log in again to continue.", showNetworkChangeEvent());
				}
			});
			
		return false;
		}

	}
	boolean shouldAppendList=false;
	public void setupDeviceLogAdapter() {
		if (CommonValues.getInstance().deviceLogDetailList != null) {
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				tvEmptyLog.setVisibility(View.INVISIBLE);
				dLogAdapter = new DeviceLogAdapter(this,
						R.layout.log_list_item,
						CommonValues.getInstance().deviceLogDetailList);
				deviceLogListView.setAdapter(dLogAdapter);
				dLogAdapter.setTouchEnabled(false);
				deviceLogListView.setEnabled(true);
				
            deviceLogListView.setOnScrollListener(new AbsListView.OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						int threshold = 1; 
	                    int count = deviceLogListView.getCount(); 

	                    if (scrollState == SCROLL_STATE_IDLE) { 
	                        if (deviceLogListView.getLastVisiblePosition() >= count 
	                                - threshold) { 
	                        	if(CommonValues.getInstance().shouldSendLogReq==true){
		                    		CommonValues.getInstance().shouldSendLogReq=false;
		                    	}
	                        	shouldAppendList=true;
	                            // Execute LoadMoreData AsyncTask 
	                        	CommonValues.getInstance().currentAction = CommonIdentifier.Action_Activities;
	                        	PageNumber=PageNumber+=1;
	                    		if (asyncGetDeviceLogInfo != null) {
	                    			asyncGetDeviceLogInfo.cancel(true);
	                    		}
	                    		asyncGetDeviceLogInfo = new AsyncGetLogFromRoomFragment(RoomManager.this, deviceId,FilterType, fromsDate, tosDate,PageNumber,ChunkSize);
	                    		asyncGetDeviceLogInfo.execute();
	                        } 
	                    } 
						
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						
					}
				});
				
			} else {
				tvEmptyLog.setVisibility(View.VISIBLE);
				tvEmptyLog.setText("Sorry! No Log Available");
			}
		} else {
			CommonTask.ShowMessage(this, "No Data Returned from Server");
			
		}


	}
	public void refreshAdapter(){
		CommonValues.getInstance().shouldSendLogReq=true;
		dLogAdapter.notifyDataSetChanged();
		
	}

	private boolean validateLastDateInput() {
		try {
			// Date firstDate = dateFormatter.parse(stDate);
			Date lastDate = dateFormatter.parse(lstDate);
			Date CurrentDate = d;
			if (etDateFrom.getText().toString().length() == 0) {
				CommonTask.ShowMessage(this, "Please select Start Date first.");
				etDateTo.setText("");
				return false;
			} else if (lastDate.after(CurrentDate)) {
				CommonTask.ShowMessage(this,
						"End Date can't be greater than Current Date");
				etDateTo.setText("");
				return false;
			} else if (lastDate.before(tobesetFromDate)) {
				CommonTask.ShowMessage(this,
						"End Date can't be less than Start date");
				etDateTo.setText("");
				return false;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	public void showError() {
		CommonTask.ShowMessage(this, "Please select Start Date First.");
	}

	public void showFirstDateError() {
		CommonTask.ShowMessage(this,
				"Start Date can't be greater than Current Date.");
	}

	public boolean sendGetDeviceRequest(int userId, int roomId) {

//		String getDeviceUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ String.valueOf(userId) + "/devices?roomid=" + roomId
//				+ "&typeId=0";
		String getDeviceUrl = CommonURL.getInstance().RootUrl+ 
				 "devices?roomid=" + roomId
				+ "&typeId=0";

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null && JsonParser.getDevicesRequest(getDeviceUrl) !="") {
			return true;
		}else{
			RoomManager.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowNetworkChangeConfirmation(RoomManager.this, "Network State has changed.Please log in again to continue.", showNetworkChangeEvent());
					asyncGetDeviceInfo.cancel(true);
				}
			});
			
		return false;
		}

	}

	@Override
	public void onPause() {
		// this.setTitle(R.string.Home);
		// spinner1.setEnabled(false);
		super.onPause();
	/*	if(CommonValues.getInstance().connectionMode=="Local"){
			if(wamp!=null)
		wamp.stopWampClient();
		}*/

	}

	@Override
	public void onBackPressed() {
		vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
		vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
		// boolean backHandled = true;
		switch (backState) {
		case INITIAL_STATE:
			// MainActionbarBase.currentMenuIndex = MainActionbarBase
			// .getLastIndex();
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			// fragmentBackStack.clear();
			Intent homeIntent = new Intent(this, Home_.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
			// backHandled = false;
			break;
		case DEVICE_STATE:
			getSupportActionBar().setTitle("Rooms");
			vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
			vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
			backState = RoomsState.INITIAL_STATE;
			vfRoom.setDisplayedChild(0);
			CommonValues.getInstance().deviceList.clear();
			// deviceListView.setAdapter(null);
			// dAdapter.clear();
			break;
		case PROPERTY_STATE:
			getSupportActionBar().setTitle("Devices");
			vfRoom.setInAnimation(CommonTask.inFromLeftAnimation());
			vfRoom.setOutAnimation(CommonTask.outToRightAnimation());
			backState = RoomsState.DEVICE_STATE;
			vfRoom.setDisplayedChild(1);
			LoadRoomDetailsContent(roomid);
			break;
		case VIEWLOG_STATE:
//			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
//				// deviceLogListView.setAdapter(null);
//				// dLogAdapter.clear();
//			}
			etDateFrom.setText("");
			etDateTo.setText("");
			// bSearch.setVisibility(View.INVISIBLE);
			if(deviceTypeId==1){
				getSupportActionBar().setTitle("Fan Control");
				}else if(deviceTypeId==2){
					getSupportActionBar().setTitle("Light Control");
				}else if(deviceTypeId==3){
					getSupportActionBar().setTitle("Ac Control");
				}else if(deviceTypeId==4){
					getSupportActionBar().setTitle("Curtain Control");
				}
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);

		default:
			break;
		}

		// return backHandled;

	}
	Wamp wamp=new Wamp();
	@Override
	public void onResume() {
		super.onResume();
		if(CommonValues.getInstance().connectionMode=="Local"){
		wamp.connectWampClient(this);
		}
		CommonValues.getInstance().roomManager = (RoomManager_) this;
		if (vfRoom.getDisplayedChild() == 0) {
//			getSupportActionBar().setTitle("Rooms");
			if (asyncGetRoomInfo != null) {
				asyncGetRoomInfo.cancel(true);
			}
			asyncGetRoomInfo = new AsyncGetRoom(this,
					CommonValues.getInstance().userId);
			asyncGetRoomInfo.execute();

		}
		if (vfRoom.getDisplayedChild() == 1) {
//			getSupportActionBar().setTitle("Devices");
			LoadRoomDetailsContent(roomid);

		}
		if (vfRoom.getDisplayedChild() == 2) {
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);

		}
		if (vfRoom.getDisplayedChild() == 3) {
//			getSupportActionBar().setTitle("Activities");
//			bSearch.setVisibility(View.INVISIBLE);
//			LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter.format(d)
//					.toString(), formatter.format(d).toString());

		}
		bRoom.setBackground(getResources().getDrawable(R.drawable.roomselected));
		

	}

	public void startProgress() {
		roomlistProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != roomlistProgressBar && roomlistProgressBar.isShown()) {

			roomlistProgressBar.setVisibility(View.GONE);
		}
	}

	public static void startDeviceProgress() {
		devicelistProgressBar.setVisibility(View.VISIBLE);
	}

	public static void stopDeviceProgress() {
		if (null != devicelistProgressBar && devicelistProgressBar.isShown()) {

			devicelistProgressBar.setVisibility(View.GONE);
		}

	}

	@Override
	@Click({ R.id.bCamera, R.id.bRoom, R.id.bDashboard, R.id.btShowLog,
			R.id.etDateFrom, R.id.etDateTo, R.id.tvToday, R.id.tvYesterday,
			R.id.bSearch })
	public void onClick(View v) {
		// fragmentBackStack.clear();
		switch (v.getId()) {
		case R.id.bCamera:
			// cancelAsyncOnVisibleFlipper();
//			Toast.makeText(RoomManager.this, "No Servilance System Available", Toast.LENGTH_SHORT).show();
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			currentFragment = CAMERA_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(6)))
				stackIndex.push(String.valueOf(6));
			Intent cameraIntent = new Intent(this, VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);
			break;
		case R.id.bRoom:
			if (vfRoom.getDisplayedChild() == 0) {
				return;
			} else {
				getSupportActionBar().setTitle("Rooms");
				new AsyncGetRoom(RoomManager.this,CommonValues.getInstance().userId).execute();
				vfRoom.setDisplayedChild(0);
			}
			break;
		case R.id.bDashboard:
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			// cancelAsyncOnVisibleFlipper();
			Home.mDrawerList.setItemChecked(ALLDEVICE_FRAGMENT, true);
			Home.navDrawerAdapter.setSelectedPosition(ALLDEVICE_FRAGMENT);
			currentFragment = ALLDEVICE_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(0)))
				stackIndex.push(String.valueOf(0));
			goBackToDashboard();
			// ((MainActionbarBase) getActivity()).displayFragment(0);
			break;
		case R.id.btShowLog:
			getSupportActionBar().setTitle("Activities");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			backState = RoomsState.VIEWLOG_STATE;
			vfRoom.setDisplayedChild(3);
			tvDeviceLogHeadingText.setText("  " + deviceManagerEntity.Name);
			if (deviceManagerEntity.DeviceTypeId == 1) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.logfan, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 2) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.logbulb, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 3) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.logac, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 4) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.logcurtain, 0, 0, 0);
			}
			tvToday.setTextColor(Color.parseColor("#6699ff"));
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			etDateFrom.setInputType(InputType.TYPE_NULL);
			etDateTo.setInputType(InputType.TYPE_NULL);
			// etDateFrom.setText(fromDate);
			// etDateTo.setText(toDate,#2C5197);
			etDateFrom.setHint("Start Date");
			etDateTo.setHint("End Date");
			etDateFrom.requestFocus();
			setDateTimeField();
			bSearch.setVisibility(View.INVISIBLE);
			FilterType=1;
			fromsDate=formatter.format(d).toString();
			tosDate=formatter.format(d).toString();
			PageNumber=1;
			LoadDeviceLogContent(deviceManagerEntity.Id, FilterType, fromsDate, tosDate,PageNumber,ChunkSize);
			break;
		case R.id.etDateFrom:
			if (fromDatePickerDialog != null)
				fromDatePickerDialog.show();
			fromDatePickerDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					fromDatePickerDialog.getDatePicker().getCalendarView()
							.setDate(tobesetFromDate.getTime());
				}
			});
			bSearch.setVisibility(View.VISIBLE);
			break;
		case R.id.etDateTo:
			// showCalendar(v);
			if (toDatePickerDialog != null)
				toDatePickerDialog.show();
			toDatePickerDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					toDatePickerDialog.getDatePicker().getCalendarView()
							.setDate(tobesetToDate.getTime());
				}
			});
			break;
		case R.id.tvToday:
			/*
			 * String tDelims = "T"; String[] tTokens
			 * =dateFormatter.format(d).toString().split(tDelims);
			 */
			etDateFrom.setText("");
			etDateTo.setText("");
			 bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#6699ff"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				clearPreviousLogData();
				
			}
			
			FilterType=1;
			fromsDate=formatter.format(d).toString();
			tosDate=formatter.format(d).toString();
			PageNumber=1;
			LoadDeviceLogContent(deviceManagerEntity.Id, FilterType, fromsDate, tosDate,PageNumber,ChunkSize);
			break;
		case R.id.tvYesterday:
			if (tvEmptyLog.isShown()) {
				tvEmptyLog.setVisibility(View.GONE);
			}
			/*
			 * String yDelims = "T"; String[] yTokens = dateFormatter
			 * .format(d.getTime() - 24 * 60 * 60 * 1000).toString()
			 * .split(yDelims);
			 */
			etDateFrom.setText("");
			etDateTo.setText("");
			 bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#6699ff"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				clearPreviousLogData();
			}
			FilterType=2;
			fromsDate=formatter
					.format(d.getTime() - 24 * 60 * 60 * 1000).toString();
			tosDate=formatter
					.format(d.getTime() - 24 * 60 * 60 * 1000).toString();
			PageNumber=1;
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId, FilterType,fromsDate ,
					tosDate,PageNumber,ChunkSize);
			break;
		case R.id.bSearch:
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				clearPreviousLogData();
			}
			if(validateEmptyDateInput()){
				FilterType=3;
				fromsDate=stserverDate;
				tosDate=lstserverDate;
				PageNumber=1;
			LoadDeviceLogContent(deviceManagerEntity.Id, FilterType, fromsDate,
					tosDate,PageNumber,ChunkSize);
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 
	 */
	private void clearPreviousLogData() {
		deviceLogListView.setAdapter(null);
		dLogAdapter.clear();
		CommonValues.getInstance().deviceLogDetailList.clear();
	}
	
	public boolean validateEmptyDateInput() {
		 if(etDateTo.getText().toString().length() == 0){
			CommonTask.ShowMessage(this, "End Date Can't be blank");
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	private void cancelAsyncOnVisibleFlipper() {
		if (vfRoom.getDisplayedChild() == 0) {
			asyncGetRoomInfo.cancel(true);
			CommonValues.getInstance().roomList.clear();
			// rAdapter.clear();
		}
		if (vfRoom.getDisplayedChild() == 1) {
			asyncGetDeviceInfo.cancel(true);
			CommonValues.getInstance().deviceList.clear();
			// dAdapter.clear();
		}
		if (vfRoom.getDisplayedChild() == 2) {
			asyncGetDeviceProperty.cancel(true);
		}
		if (vfRoom.getDisplayedChild() == 3) {
			asyncGetDeviceLogInfo.cancel(true);
			CommonValues.getInstance().deviceLogDetailList.clear();
			// dLogAdapter.clear();
		}
	}

	/**
	 * 
	 */
	public void goBackToDashboard() {
		Intent homeIntent = new Intent(this, Home_.class);
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

	public boolean sendCurtainPresetRequest( int deviceId) {
//		String curtainPresetUrl = CommonURL.getInstance().GetCommonURL + "/"
//				+ userId + "/devices/" + deviceId + "/presets";
		String curtainPresetUrl = CommonURL.getInstance().RootUrl + "presets?"+ "deviceId=" + deviceId ;
		if (JsonParser.setPresetRequest(curtainPresetUrl) != null) {
			return true;
		}
		return false;

	}

	public void setCurtainPresetResponseData() {
		String[] presetValues = getCurtainPresetValues();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, presetValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		spinner1.setSelection(presetItemPosition + 1);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (shouldSetPreset) {
					sendSetProperty(
							position, deviceId, 6);
				}
				shouldSetPreset = true;
				spinner1.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	/**
	 * @return
	 */
	private String[] getCurtainPresetValues() {
		if (CommonValues.getInstance().presetList != null) {
			String[] presetArray = new String[CommonValues.getInstance().presetList
					.size()];
			for (int i = 0; i < CommonValues.getInstance().presetList.size(); i++) {
				presetArray[i] = CommonValues.getInstance().presetList.get(i)
						.getDisplayName();
			}
			shouldSetPreset = false;
			return presetArray;
		} else {
			// shouldSetPreset = true;
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
			return null;
		}
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
			sendSetProperty(
					10 * curtainMultiplier, deviceId, curtainPropertyId);
			return true;
		}

		// event when single tap occurs
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.d("Tap", "Single");
			sendSetProperty(
					5 * curtainMultiplier, deviceId, curtainPropertyId);
			return super.onSingleTapUp(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Log.d("Tap", "Long");
			sendSetProperty(
					15 * curtainMultiplier, deviceId, curtainPropertyId);
			super.onLongPress(e);
		}
	}

	private void setHeaderImage(String value) {
		switch (deviceTypeId) {

		case 1:// Fan
			if (value.equals("1")) {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.fan_on));
			} else {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.fan_off));
			}

			break;
		case 2:// Light
			if (value.equals("1")) {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.bulbon));
			} else {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.bulb_off));
			}

			break;
		case 4:// Curtain
			if (value.equals("1")) {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.curtain_large));
			} else {
				ivDevice.setImageDrawable(getResources().getDrawable(
						R.drawable.curtain_large_off));
			}

			break;

		default:
			break;
		}

	}

	String stDate = "";
	String lstDate = "";
	String stserverDate = formatter.format(d).toString();
	String lstserverDate = formatter.format(d).toString();
	Calendar newDate = Calendar.getInstance();

	private void setDateTimeField() {
		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						newDate.set(year, monthOfYear, dayOfMonth);

						try {
							Date fromDate = dateFormatter.parse(dateFormatter
									.format(newDate.getTime()));
							stDate = dateFormatter.format(newDate.getTime());
							stserverDate = formatter.format(newDate.getTime());
							Date CurrentDate = new Date();
							if (fromDate.after(CurrentDate)) {
								showFirstDateError();
							} else {
								etDateFrom.setText(dateFormatter.format(newDate
										.getTime()));
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						try {
							tobesetFromDate = dateFormatter.parse(etDateFrom
									.getText().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						lstDate = dateFormatter.format(newDate.getTime());
						lstserverDate = formatter.format(newDate.getTime());
						if (validateLastDateInput()) {
							etDateTo.setText(dateFormatter.format(newDate
									.getTime()));

						}
						try {
							tobesetToDate = dateFormatter.parse(etDateTo
									.getText().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

	}
	/**
	 * 
	 */
	private void setSeekbarActiveColour() {
		circularSeekBar1.setCircleColor(Color.rgb(190, 190, 190));
		circularSeekBar1.setPointerColor(Color.argb(235, 74, 138, 255));
		circularSeekBar1.setCircleProgressColor(Color.argb(235, 74, 138, 255));
	}

	/**
	 * 
	 */
	private void setSeekbarInactiveColor() {
		circularSeekBar1.setCircleColor(getResources().getColor(R.color.dark_gray));
		circularSeekBar1.setPointerColor(getResources().getColor(R.color.dark_gray));
		circularSeekBar1.setCircleProgressColor(R.color.dark_gray);
	}
	

}