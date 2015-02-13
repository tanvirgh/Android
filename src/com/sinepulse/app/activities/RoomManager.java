package com.sinepulse.app.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.sinepulse.app.adapters.DeviceListAdapter;
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
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

@EActivity(R.layout.room_status)
public class RoomManager extends MainActionbarBase implements OnClickListener,
		OnTouchListener, OnItemClickListener {
	
	Singleton m_Inst = Singleton.getInstance();
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

	public DeviceListAdapter dAdapter;
	boolean isRoomVisible = false;
	int deviceTypeId;
	int roomid;
	boolean shouldSetPreset = false;
	int presetItemPosition;

	SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d = new Date();
	RoundKnobButton rv ;
	TextView tv2;
	RelativeLayout panel ;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_Inst.InitGUIFrame(this);
		RoomManager.context = this;
		backState = RoomsState.INITIAL_STATE;
		createMenuBar();

	}

	@AfterViews
	void afterViewLoaded() {
		gestureDetector = new GestureDetector(this, new GestureListener());
		backState = RoomsState.INITIAL_STATE;
		vfRoom.setDisplayedChild(0);
		btAddRoom.setText("Room List");
		fromDate=formatter.format(d).toString();
		toDate=formatter.format(d).toString();
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			MainActionbarBase.stackIndex.removeAllElements();
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
				LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter
						.format(d).toString(), formatter.format(d).toString());
				break;
			default:
				break;
			}
		}
		return true;
	}

	public boolean sendGetRoomRequest(Integer userId) {

		String getRoomUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/rooms";

		if (JsonParser.getRoomRequest(getRoomUrl) != null) {
			return true;
		}
		return false;
	}

	public void setupRoomListViewAdapter() {
		if (CommonValues.getInstance().roomList != null) {
			rAdapter = new RoomListAdapter(this, R.layout.room_list_item,
					CommonValues.getInstance().roomList);
			roomListView.setAdapter(rAdapter);
			rAdapter.setTouchEnabled(false);
			roomListView.setEnabled(true);
			roomListView.setOnItemClickListener(this);
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
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
		if (CommonValues.getInstance().deviceList != null) {
			dAdapter = new DeviceListAdapter(this, R.layout.device_item_view,
					CommonValues.getInstance().deviceList);
			deviceListView.setAdapter(dAdapter);
			dAdapter.setTouchEnabled(false);
			deviceListView.setEnabled(true);
			deviceListView.setOnItemClickListener(this);
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
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
//			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
//					R.drawable.icon_room_medium, 0, 0, 0);
			// new DisplayRoomDetails(RoomManagerFragment.this,
			// roomManagerEntity.Id);
			roomid = roomManagerEntity.getId();
			LoadRoomDetailsContent(roomid);

			// displayFragment(7);
			break;
		case R.id.lvDeviceList:
			getSupportActionBar().setTitle("Device Control");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;
			dAdapter.setSelection(position);
			deviceListView.setSelection(position);
			deviceListView.setSelectionFromTop(position, view.getTop());
			deviceManagerEntity = dAdapter.getItemAtPosition(position);
			deviceTypeId = deviceManagerEntity.DeviceTypeId;

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

		String getDevicePropertyUrl = CommonURL.getInstance().GetCommonURL
				+ "/" + CommonValues.getInstance().userId + "/properties?id="
				+ DeviceId;
		if (JsonParser.getDevicePropertyRequest(getDevicePropertyUrl) != null) {
			return true;
		}
		return false;

	}

	public void setDevicePropertyControlData(int deviceTypeId) {
		// rAdapter.clear();
		switch (deviceTypeId) {
		case 1:// Fan
			getSupportActionBar().setTitle("Fan Control");
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			 generateRotatingKnobView();
			rv.SetListener(new com.sinepulse.app.activities.RoundKnobButton.RoundKnobButtonListener() {

				@Override
				public void onRotate(final int percentage) {
					tv2.post(new Runnable() {
						@Override
						public void run() {
							 tv2.setText(percentage + " %");
//							if(knobState){
							int value=percentage;
							sendSetProperty(CommonValues.getInstance().userId,
									value, deviceId, 3);
						}
					});
				}
			});
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 2:// Light
			getSupportActionBar().setTitle("Light Control");
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			 generateRotatingKnobView();
			 rv.SetListener(new com.sinepulse.app.activities.RoundKnobButton.RoundKnobButtonListener() {
					
					@Override
					public void onRotate(final int percentage) {
						tv2.post(new Runnable() {
							@Override
							public void run() {
								 tv2.setText(percentage + " %");
//								if(knobState){
								int value=percentage;
								if(value==99){
									value=100;
								}
								sendSetProperty(CommonValues.getInstance().userId,
										value, deviceId,2);
							}
						});
					}
				});
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 3:// AC
			getSupportActionBar().setTitle("AC Control");
			hideRotatingKnob();
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.ac_large));
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 4:// Curtain
			hideRotatingKnob();
			spinner1.setVisibility(View.VISIBLE);
			loadCurtainPresetValues(CommonValues.getInstance().userId,
					deviceManagerEntity.Id);
			getSupportActionBar().setTitle("Curtain Control");
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.VISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + " ");
			break;

		default:
			break;
		}
		if (CommonValues.getInstance().devicePropertyList != null) {
			for (int i = 0; i < CommonValues.getInstance().devicePropertyList
					.size(); i++) {

				setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
						.get(i));
			}
		} else {
			CommonTask.ShowMessage(this, "Network Problem.Please Retry.");
		}
	}
	
	ViewGroup parent;
	public void generateRotatingKnobView() {
		 View C = findViewById(R.id.propertyMiddleView);
		 parent = (ViewGroup) C.getParent();
//		panel = (RelativeLayout) findViewById(R.id.propertyMiddleView).getParent();
		 rv = new RoundKnobButton(this, R.drawable.stator,
					R.drawable.rotoron, R.drawable.rotoroff, m_Inst.Scale(360),
					m_Inst.Scale(360));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//			lp.setMargins(0, 0, 0, 200);
		parent.addView(rv,lp);
		
		tv2 = new TextView(this);
		tv2.setText("");
		tv2.setTextColor(getResources().getColor(R.color.lime));
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
		           getResources().getDimension(R.dimen.font_size));
		lp = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		parent.addView(tv2, lp);
	}
	
	public void hideRotatingKnob() {
		if(parent!=null){
			if(rv.getVisibility()==View.VISIBLE){
				parent.removeView(rv);
				rv.setVisibility(View.GONE);
			}
			if(tv2.getVisibility()==View.VISIBLE){
				parent.removeView(tv2);
				tv2.setVisibility(View.GONE);
			}
		}
		/*if(rv!=null){
		panel.removeView(rv);
		}
		if(tv2!=null){
		panel.removeView(tv2);
		}*/
	}

	private void loadCurtainPresetValues(int userId, int deviceId) {
		if (asyncGetCurtainPresetValuesFromRoom != null) {
			asyncGetCurtainPresetValuesFromRoom.cancel(true);
		}
		asyncGetCurtainPresetValuesFromRoom = new AsyncGetCurtainPresetValuesFromRoom(
				this, userId, deviceId);
		asyncGetCurtainPresetValuesFromRoom.execute();

	}

	public void setDevicePropertyValue(DeviceProperty property) {

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
		case 2:// Dimming Light
			/*seekBar1.setOnSeekBarChangeListener(null);
			if (property.IsActionPending) {
				seekBar1.setProgress(Integer.parseInt(property
						.getPendingValue()));
				tvProgressValue.setText("Dimming : "+property.getPendingValue() + " %");
				seekBar1.setEnabled(false);
			} else {
				seekBar1.setProgress(Integer.parseInt(property.getValue()));
				seekBar1.setEnabled(true);
				tvProgressValue.setText("Dimming : "+property.getValue() + " %");
			}*/
			if (property.IsActionPending) {
				 rv.setRotorPercentage(Integer.parseInt(property
							.getPendingValue()));
				 tv2.setText(property.getPendingValue() + " %");
				 rv.setEnabled(false);
				} else {
					rv.setRotorPercentage(Integer.parseInt(property.getValue()));
					rv.setEnabled(true);
					tv2.setText(property.getValue() + " %");
				}

			break;
		case 3:// Dimming Fan(Knob)
			/*seekBar1.setOnSeekBarChangeListener(null);
			if (property.IsActionPending) {
				seekBar1.setProgress(Integer.parseInt(property
						.getPendingValue()));
				seekBar1.setEnabled(false);
				tvProgressValue.setText("Dimming : "+property.getPendingValue() + " %");
			} else {
				seekBar1.setProgress(Integer.parseInt(property.getValue()));
				seekBar1.setEnabled(true);
				tvProgressValue.setText("Dimming : "+property.getValue() + " %");
			}*/
			if (property.IsActionPending) {
				 rv.setRotorPercentage(Integer.parseInt(property
							.getPendingValue()));
				 tv2.setText(property.getPendingValue() + " %");
				 rv.setEnabled(false);
				} else {
					rv.setRotorPercentage(Integer.parseInt(property.getValue()));
					rv.setEnabled(true);
					tv2.setText(property.getValue() + " %");
				}
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
				sendSetProperty(CommonValues.getInstance().userId,
						(isChecked ? 1 : 0), deviceId, 1);
				// toggleButton2.setEnabled(false);
			}
		});
		/*seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				seekBarProgressValue = seekBar.getProgress();
				seekBar1.setEnabled(false);
				if (deviceManagerEntity.DeviceTypeId == 1) {
					sendSetProperty(CommonValues.getInstance().userId,
							seekBarProgressValue, deviceId, 3);
				} else if (deviceManagerEntity.DeviceTypeId == 2) {
					sendSetProperty(CommonValues.getInstance().userId,
							seekBarProgressValue, deviceId, 2);
				}
				tvProgressValue.setText("Dimming : "+String.valueOf(seekBarProgressValue)
						+ " %");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// seekBarProgress=progress;
				// sendSetPropertyForSeekBar(CommonValues.getInstance().userId,progress,2,property.DeviceId);
			}
		});*/
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

	public void sendSetProperty(int userId, int value, int DeviceId,
			int propertyId) {
		if (asyncGetSetPropertyFromRoom != null) {
			asyncGetSetPropertyFromRoom.cancel(true);
		}
		asyncGetSetPropertyFromRoom = new AsyncGetSetPropertyFromRoom(this,
				userId, DeviceId, propertyId, value);
		asyncGetSetPropertyFromRoom.execute();

	}

	public boolean setPropertyRequestFromRoom(int userId, int deviceId,
			int propertyId, int value) {
		String setPropertyUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ userId + "/property?id=" + deviceId + "&propertyid="
				+ propertyId + "&value=" + value;
		System.out.println(setPropertyUrl);
		if (JsonParser.setProptyerRequest(setPropertyUrl) != null) {
			return true;
		}
		return false;

	}

	public void setPropertyResponseData() {
		if (CommonValues.getInstance().devicePropertyList != null) {
			for (int i = 0; i < CommonValues.getInstance().devicePropertyList
					.size(); i++) {
				setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
						.get(i));
			}
		} else {
			CommonTask.ShowMessage(this, "Network Error.Please try Later");
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
			String fromDate, String toDate) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Activities;
		if (asyncGetDeviceLogInfo != null) {
			asyncGetDeviceLogInfo.cancel(true);
		}
		asyncGetDeviceLogInfo = new AsyncGetLogFromRoomFragment(this, deviceId,
				FilterType, fromDate, toDate);
		asyncGetDeviceLogInfo.execute();

	}

	public boolean sendGetDeviceLogRequest(Integer deviceId, int FilterType,
			String fromDate, String toDate) {

		String getDeviceLogUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ CommonValues.getInstance().userId + "/device/"
				+ String.valueOf(deviceId) + "/activities";
		if (JsonParser.postDeviceLogRequest(getDeviceLogUrl, FilterType,
				fromDate, toDate) != null) {
			return true;
		}
		return false;

	}

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
			} else {
				tvEmptyLog.setVisibility(View.VISIBLE);
				tvEmptyLog.setText("Sorry! No Log Available");
			}
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}

	}

	Dialog dialog;

	private void showCalendar(final View v) {
		dialog = new Dialog(this);
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
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				if (etDateFrom != null && v.getId() == R.id.etDateFrom) {
					fromDate = formatter.format(
							new Date(year - 1900, month, dayOfMonth))
							.toString();
					String delims = "T";
					String[] tokens = fromDate.split(delims);
					try {
						Date firstDate = formatter.parse(fromDate);
						Date CurrentDate = d;
						if(firstDate.after(CurrentDate)){
							showFirstDateError();
						}else{
							etDateFrom.setText(tokens[0]);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (etDateTo != null && v.getId() == R.id.etDateTo) {
					if (etDateFrom.getText().toString().length() > 0) {
						toDate = formatter.format(
								new Date(year - 1900, month, dayOfMonth))
								.toString();
						String delims = "T";
						String[] tokens = toDate.split(delims);
						 if (validateLastDateInput()) {
								etDateTo.setText(tokens[0]);
								bSearch.setVisibility(View.VISIBLE);
								}
					} else {
						showError();
					}
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
				if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
					deviceLogListView.setAdapter(null);
					dLogAdapter.clear();
				}
				LoadDeviceLogContent(deviceManagerEntity.Id, 3, fromDate,
						toDate);

			}
		});

	}

	private boolean validateLastDateInput() {
		Date CurrentDate = d;
		try {
			Date firstDate = formatter.parse(fromDate);
			Date lastDate = formatter.parse(toDate);
			if (lastDate.after(CurrentDate)) {
				CommonTask.ShowMessage(this, "To date can't be greater than current date");
				etDateTo.setText("");
				return false;
			}else if( lastDate.before(firstDate)){
				CommonTask.ShowMessage(this, "To date can't be less than From date");
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
		CommonTask.ShowMessage(this, "Please select FromDate First");
	}
	public void showFirstDateError() {
		CommonTask.ShowMessage(this, "From Date should  be less than current date");
	}

	public boolean sendGetDeviceRequest(int userId, int roomId) {

		String getDeviceUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/devices?roomid=" + roomId
				+ "&typeId=0";

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;

	}

	@Override
	public void onPause() {
		// this.setTitle(R.string.Home);
		// spinner1.setEnabled(false);
		super.onPause();
		// updateBasket();

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
			MainActionbarBase.stackIndex.removeAllElements();
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
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				// deviceLogListView.setAdapter(null);
				// dLogAdapter.clear();
			}
//			etDateFrom.setText("");
//			etDateTo.setText("");
//			bSearch.setVisibility(View.INVISIBLE);
			getSupportActionBar().setTitle("Device Control");
			vfRoom.setDisplayedChild(2);
			backState = RoomsState.PROPERTY_STATE;
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);

		default:
			break;
		}

		// return backHandled;

	}

	@Override
	public void onResume() {
		CommonValues.getInstance().roomManager = (RoomManager_) this;
		getSupportActionBar().setTitle(R.string.create_room);
		if (vfRoom.getDisplayedChild() == 0) {
			if (asyncGetRoomInfo != null) {
				asyncGetRoomInfo.cancel(true);
			}
			asyncGetRoomInfo = new AsyncGetRoom(this,
					CommonValues.getInstance().userId);
			asyncGetRoomInfo.execute();

		}
		if (vfRoom.getDisplayedChild() == 1) {
			LoadRoomDetailsContent(roomid);

		}
		if (vfRoom.getDisplayedChild() == 2) {
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);

		}
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
	@Override
	@Click({ R.id.bCamera, R.id.bRoom, R.id.bDashboard, R.id.btShowLog,
			R.id.etDateFrom, R.id.etDateTo, R.id.tvToday, R.id.tvYesterday })
	public void onClick(View v) {
		// fragmentBackStack.clear();
		switch (v.getId()) {
		case R.id.bCamera:
			// cancelAsyncOnVisibleFlipper();
			MainActionbarBase.stackIndex.removeAllElements();
			currentFragment = CAMERA_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(6)))
				stackIndex.push(String.valueOf(6));
			Intent cameraIntent = new Intent(this, VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);
			break;
		case R.id.bRoom:
			break;
		case R.id.bDashboard:
			MainActionbarBase.stackIndex.removeAllElements();
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
			getSupportActionBar().setTitle("Device Activity");
			vfRoom.setInAnimation(CommonTask.inFromRightAnimation());
			vfRoom.setOutAnimation(CommonTask.outToLeftAnimation());
			backState = RoomsState.VIEWLOG_STATE;
			vfRoom.setDisplayedChild(3);
			tvDeviceLogHeadingText.setText("  " + deviceManagerEntity.Name);
			if (deviceManagerEntity.DeviceTypeId == 1) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.fan_on, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 2) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.bulbon, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 3) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.ac_medium, 0, 0, 0);
			} else if (deviceManagerEntity.DeviceTypeId == 4) {
				tvDeviceLogHeadingText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.curtainmedium, 0, 0, 0);
			}
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			String delims ="T" ;
		    String[] tokens = formatter.format(d).toString().split(delims);
		    etDateFrom.setText(tokens[0]);
		    etDateTo.setText(tokens[0]);
			LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter.format(d)
					.toString(), formatter.format(d).toString());
			break;
		case R.id.etDateFrom:
		case R.id.etDateTo:
			showCalendar(v);
			break;
		case R.id.tvToday:
			String tDelims ="T" ;
		    String[] tTokens = formatter.format(d).toString().split(tDelims);
			etDateFrom.setText(tTokens[0]);
			etDateTo.setText(tTokens[0]);
//			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId, 1, formatter
					.format(d).toString(), formatter.format(d).toString());
			break;
		case R.id.tvYesterday:
			if (tvEmptyLog.isShown()) {
				tvEmptyLog.setVisibility(View.GONE);
			}
			String yDelims ="T" ;
		    String[] yTokens = formatter.format(d.getTime() - 24 * 60 * 60 * 1000).toString().split(yDelims);
			etDateFrom.setText(yTokens[0]);
			etDateTo.setText(yTokens[0]);
//			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#2C5197"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId, 2, formatter
					.format(d.getTime()-24*60*60*1000).toString(), formatter.format(d.getTime()-24*60*60*1000).toString());
			break;

		default:
			break;
		}

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

	public boolean sendCurtainPresetRequest(int userId, int deviceId) {
		String curtainPresetUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ userId + "/devices/" + deviceId + "/presets";
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
					sendSetProperty(CommonValues.getInstance().userId,
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
						R.drawable.curtain_large));
			}

			break;

		default:
			break;
		}

	}

}