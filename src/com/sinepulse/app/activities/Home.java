package com.sinepulse.app.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.Post;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.sinepulse.app.R;
import com.sinepulse.app.adapters.DeviceListByTypeAdapter;
import com.sinepulse.app.adapters.DeviceLogAdapter;
import com.sinepulse.app.adapters.NavDrawerListAdapter;
import com.sinepulse.app.asynctasks.AsyncGetCurtainPresetValues;
import com.sinepulse.app.asynctasks.AsyncGetDeviceLogInfo;
import com.sinepulse.app.asynctasks.AsyncGetDeviceProperty;
import com.sinepulse.app.asynctasks.AsyncGetDevicesByType;
import com.sinepulse.app.asynctasks.AsyncGetSetPropertyFromDashBoard;
import com.sinepulse.app.asynctasks.AsyncLogOutTask;
import com.sinepulse.app.asynctasks.AsyncRefreshDashBoard;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.entities.DevicePropertyLog;
import com.sinepulse.app.utils.CommonConstraints;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NavDrawerItem;

/**
 * 
 * @author tac
 * 
 */
@SuppressWarnings("deprecation")
@TargetApi(13)
@EActivity(R.layout.main)
public class Home extends MainActionbarBase implements OnClickListener,
		OnTouchListener {

	Singleton m_Inst = Singleton.getInstance();

	// private static final int INITIAL_STATE = -1;
	public static final int INITIAL_STATE = -1, DEVICE_STATE = 0,
			PROPERTY_STATE = 1, VIEWLOG_STATE = 2;

	public enum DeviceTypeState {
		INITIAL_STATE, // backState -1
		DEVICE_STATE, PROPERTY_STATE, VIEWLOG_STATE
	};

	// backState use for manage back button
	public static DeviceTypeState backState = DeviceTypeState.INITIAL_STATE;
	InputMethodManager imm;
	public ProgressBar progressBar, menuProgressBar;
	public static Context context;
	// Nav Drawer menu items
	public String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public static NavDrawerListAdapter navDrawerAdapter;
	public static DrawerLayout mDrawerLayout;
	public static ExpandableListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;
	public static boolean isDrawerOpen = false;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
	// DashBoardItems
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	@ViewById(R.id.btn_total_power)
	protected Button btn_total_power;
	@ViewById(R.id.home_btn_room)
	protected Button home_btn_room;
	@ViewById(R.id.home_btn_fan)
	protected Button home_btn_fan;
	@ViewById(R.id.home_btn_bulb)
	protected Button home_btn_bulb;
	@ViewById(R.id.home_btn_ac)
	protected Button home_btn_ac;
	@ViewById(R.id.home_btn_curtain)
	protected Button home_btn_curtain;
	@ViewById(R.id.toggleButton2)
	protected ToggleButton toggleButton2;
	@ViewById(R.id.btShowLog)
	public Button btShowLog;
	@ViewById(R.id.list_image)
	public ImageView list_image;
	@ViewById(R.id.seekBar1)
	public SeekBar seekBar1;
	@ViewById(R.id.porda)
	public RelativeLayout porda;
	@ViewById(R.id.ivDevice)
	public ImageView ivDevice;
	@ViewById(R.id.tvDeviceName)
	public TextView tvDeviceName;
	@ViewById(R.id.tvProgressValue)
	public TextView tvProgressValue;
	@ViewById(R.id.tvDeviceLogHeadingText)
	public TextView tvDeviceLogHeadingText;
	@ViewById(R.id.tvToday)
	public TextView tvToday;
	@ViewById(R.id.tvYesterday)
	public TextView tvYesterday;
	@ViewById(R.id.bSearch)
	public Button bSearch;
	@ViewById(R.id.spinner1)
	public Spinner spinner1;

	// FirstRow
	@ViewById(R.id.firstRowLeft)
	protected TextView firstRowLeft;
	@ViewById(R.id.firstRowRightUp)
	protected TextView firstRowRightUp;
	@ViewById(R.id.firstRowRightDown)
	protected TextView firstRowRightDown;
	// Second Row
	@ViewById(R.id.secondRowLeft)
	protected TextView secondRowLeft;
	@ViewById(R.id.secondRowRightUp)
	protected TextView secondtRowRightUp;
	@ViewById(R.id.secondRowRightDown)
	protected TextView secondRowRightDown;
	// ThirdRow
	@ViewById(R.id.thirdRowLeft)
	protected TextView thirdRowLeft;
	@ViewById(R.id.thirdRowRightUp)
	protected TextView thirdRowRightUp;
	@ViewById(R.id.thirdRowRightDown)
	protected TextView thirdRowRightDown;
	// Forth Row
	@ViewById(R.id.fourthRowLeft)
	protected TextView fourthRowLeft;
	@ViewById(R.id.fourthRowRightUp)
	protected TextView fourthtRowRightUp;
	@ViewById(R.id.fourthRowRightDown)
	protected TextView fourthRowRightDown;
	@ViewById(R.id.curtainControl)
	public RelativeLayout curtainControl;
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

	@ViewById(R.id.lvDeviceList)
	public ListView deviceListView;
	@ViewById(R.id.btAddDevice)
	public ImageView btAddDevice;
	@ViewById(R.id.tvdeviceValue)
	public TextView tvdeviceValue;
	@ViewById(R.id.btdevice_value)
	public ToggleButton btdevice_value;
	@ViewById(R.id.onOffImage)
	public ImageView onOffImage;
	public DeviceListByTypeAdapter dtAdapter;
	@ViewById(R.id.vfDeviceType)
	public static ViewFlipper vfDeviceType;
	@ViewById(R.id.devicelistProgressBar)
	public ProgressBar devicelistProgressBar;
	@ViewById(R.id.deviceLogProgressBar)
	public ProgressBar deviceLogProgressBar;
	@ViewById(R.id.devicePropertyProgressBar)
	public ProgressBar devicePropertyProgressBar;
	@ViewById(R.id.dashBoardProgressBar)
	public ProgressBar dashBoardProgressBar;
	// @ViewById(R.id.nav_drawer_progress_bar)
	// public ProgressBar nav_drawer_progress_bar;
	@ViewById(R.id.lvLogList)
	public ListView deviceLogListView;
	AsyncGetDeviceLogInfo asyncGetDeviceLogInfo = null;
	AsyncLogOutTask asyncLogOutTask = null;
	AsyncRefreshDashBoard asyncRefreshDashBoard = null;
	AsyncGetCurtainPresetValues asyncGetCurtainPresetValues = null;
	AsyncGetDeviceProperty asyncGetDeviceProperty = null;
	static AsyncGetDevicesByType asyncGetDeviceByType = null;
	private DeviceLogAdapter dLogAdapter;
	@ViewById(R.id.etDateFrom)
	public static EditText etDateFrom;
	@ViewById(R.id.etDateTo)
	public EditText etDateTo;
	@ViewById(R.id.tvEmptyLog)
	public TextView tvEmptyLog;
	AsyncGetSetPropertyFromDashBoard asyncGetSetPropertyFromDashBoard = null;
	int modifiedIndex = 0;
	static String fromDate = "";
	String toDate = "";
	int seekBarProgressValue = 0;
	int curtainPropertyId = 0;
	int curtainMultiplier = 0;

	int presetItemPosition;
	int deviceTypeId;
	boolean shouldSetPreset = false;
	boolean knobState;
	RoundKnobButton rv;
	TextView tv2;
	RelativeLayout panel;

	int year;
	int month;
	int day;
	private DatePickerDialog fromDatePickerDialog = null;
	private DatePickerDialog toDatePickerDialog = null;
	private SimpleDateFormat dateFormatter;

	Calendar newCalendar;
	Date tobesetFromDate = new Date();
	Date tobesetToDate = new Date();
	private Runnable bWaitRunnable;
	private Handler bHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_Inst.InitGUIFrame(this);
		Home.context = this;
		mSupportActionBar.setDisplayHomeAsUpEnabled(false);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		backState = DeviceTypeState.INITIAL_STATE;
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

	}

	OnChildClickListener groupSelectionListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			return false;
		}
	};

	NavDrawerItem basketDrawerItem = null;
	GestureDetector gestureDetector;
	boolean tapped;

	@AfterViews
	void afterViewsLoaded() {
		bHandler = new Handler();
		gestureDetector = new GestureDetector(context, new GestureListener());
		vfDeviceType.setDisplayedChild(0);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		CommonTask.loadSettings(this);
		CommonTask.loadLoginUser(this);

		fromDate = dateFormatter.format(d).toString();
		toDate = dateFormatter.format(d).toString();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu1);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		addDrawerMenuItems();

		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter

		navDrawerAdapter = new NavDrawerListAdapter(this, navDrawerItems,
				Childtem, groupSelectionListener);
		mDrawerList.setAdapter(navDrawerAdapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name_drawer,
				R.string.app_name_drawer) {
			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
				isDrawerOpen = false;

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				Log.d("drawerselectedposition",
						"" + mDrawerList.getCheckedItemPosition());
				supportInvalidateOptionsMenu();
				isDrawerOpen = true;
				isSearchExpanded = false;

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setOnGroupClickListener(groupClickListener);

		mDrawerList.setGroupIndicator(null);
		// mDrawerList.setOnChildClickListener(childLst);

		mDrawerList.setOnTouchListener(new ListView.OnTouchListener() {
			// The below segment of Code is used for smooth scrolling of list
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}

		});
		if (CommonValues.getInstance().summary.deviceSummaryArray != null) {
			setDashBoardTopRowData();
			// FirstRowData(Bulb)
			setDashBoardFanData();
			// Second Row Data(Fan)
			setDashBoardBulbData();
			// Third Row Data(Curtain)
			setDashBoardAcData();
			// Forth Row Data(AC)
			setDashBoardCurtainData();
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}
	}

	public void setDashBoardCurtainData() {
		if (CommonValues.getInstance().summary != null
				&& CommonValues.getInstance().summary.deviceSummaryArray.size() > 0) {
			fourthRowLeft
					.setText(String.valueOf((CommonValues.getInstance().summary.deviceSummaryArray
							.get(3)).PowerUsage)
							+ " w");

			int curtainOn = CommonValues.getInstance().summary.deviceSummaryArray
					.get(3).RunningDeviceCount;
			fourthtRowRightUp.setText(" " + String.valueOf(curtainOn) + " ");

			int curtainOff = CommonValues.getInstance().summary.deviceSummaryArray
					.get(3).DeviceCount - curtainOn;
			fourthRowRightDown.setText(" " + String.valueOf(curtainOff) + " ");
			// int deviceTypeId =
			// CommonValues.getInstance().summary.deviceSummaryArray
			// .get(3).DeviceTypeId;
		}

	}

	/**
	 * 
	 */
	public void setDashBoardAcData() {
		if (CommonValues.getInstance().summary != null
				&& CommonValues.getInstance().summary.deviceSummaryArray.size() > 0) {
			thirdRowLeft
					.setText(String.valueOf((CommonValues.getInstance().summary.deviceSummaryArray
							.get(2)).PowerUsage)
							+ " w");

			int acOn = CommonValues.getInstance().summary.deviceSummaryArray
					.get(2).RunningDeviceCount;
			thirdRowRightUp.setText(" " + String.valueOf(acOn) + " ");

			int acOff = CommonValues.getInstance().summary.deviceSummaryArray
					.get(2).DeviceCount - acOn;
			thirdRowRightDown.setText(" " + String.valueOf(acOff) + " ");
			// int deviceTypeId =
			// CommonValues.getInstance().summary.deviceSummaryArray
			// .get(2).DeviceTypeId;
		}
	}

	/**
	 * 
	 */
	public void setDashBoardBulbData() {
		if (CommonValues.getInstance().summary != null
				&& CommonValues.getInstance().summary.deviceSummaryArray.size() > 0) {
			secondRowLeft
					.setText(String.valueOf((CommonValues.getInstance().summary.deviceSummaryArray
							.get(1)).PowerUsage)
							+ " w");

			int bulbOn = CommonValues.getInstance().summary.deviceSummaryArray
					.get(1).RunningDeviceCount;
			secondtRowRightUp.setText(" " + String.valueOf(bulbOn) + " ");

			int bulbOff = CommonValues.getInstance().summary.deviceSummaryArray
					.get(1).DeviceCount - bulbOn;
			secondRowRightDown.setText(" " + String.valueOf(bulbOff) + " ");
			// int deviceTypeId =
			// CommonValues.getInstance().summary.deviceSummaryArray
			// .get(1).DeviceTypeId;
		}
	}

	/**
	 * 
	 */
	public void setDashBoardFanData() {
		if (CommonValues.getInstance().summary != null
				&& CommonValues.getInstance().summary.deviceSummaryArray.size() > 0) {
			firstRowLeft
					.setText(String.valueOf((CommonValues.getInstance().summary.deviceSummaryArray
							.get(0)).PowerUsage)
							+ " w");

			int fanOn = CommonValues.getInstance().summary.deviceSummaryArray
					.get(0).RunningDeviceCount;
			firstRowRightUp.setText(" " + String.valueOf(fanOn) + " ");

			int fanOff = CommonValues.getInstance().summary.deviceSummaryArray
					.get(0).DeviceCount - fanOn;
			firstRowRightDown.setText(" " + String.valueOf(fanOff) + " ");
			// int deviceTypeId =
			// CommonValues.getInstance().summary.deviceSummaryArray
			// .get(0).DeviceTypeId;
		}
	}

	/**
	 * 
	 */
	public void setDashBoardTopRowData() {
		if (CommonValues.getInstance().summary != null
				&& CommonValues.getInstance().summary.deviceSummaryArray.size() > 0) {
			// Total power
			btn_total_power
					.setText(CommonValues.getInstance().summary.TotalPower
							+ "  Watts in use");
			// Total Rooms
			home_btn_room.setText("   "
					+ CommonValues.getInstance().summary.RoomCount + " Rooms");
			// Total Fans
			home_btn_fan.setText("   "
					+ (CommonValues.getInstance().summary.deviceSummaryArray
							.get(0)).DeviceCount + " Fans");
			// Total Bulbs
			home_btn_bulb.setText("   "
					+ (CommonValues.getInstance().summary.deviceSummaryArray
							.get(1)).DeviceCount + " Lights");
			// Total AC
			home_btn_ac.setText("   "
					+ (CommonValues.getInstance().summary.deviceSummaryArray
							.get(2)).DeviceCount + " ACs");
			// Total Curtains
			home_btn_curtain.setText("   "
					+ (CommonValues.getInstance().summary.deviceSummaryArray
							.get(3)).DeviceCount + " Curtains");
		}
	}

	public void middleViewClicked(View v) {
		vfDeviceType.setInAnimation(CommonTask.inFromRightAnimation());
		vfDeviceType.setOutAnimation(CommonTask.outToLeftAnimation());
		getSupportActionBar().setTitle("All Devices");
		backState = DeviceTypeState.DEVICE_STATE;
		vfDeviceType.setDisplayedChild(1);
		switch (v.getId()) {
		case R.id.include2:
			// new DisplayDeviceDetails(Home.this,
			// CommonValues.getInstance().summary.deviceSummaryArray
			// .get(0).DeviceTypeId);
			deviceTypeId = CommonValues.getInstance().summary.deviceSummaryArray
					.get(0).DeviceTypeId;
			LoadDeviceDetailsContent(deviceTypeId);
			break;
		case R.id.include3:
			deviceTypeId = CommonValues.getInstance().summary.deviceSummaryArray
					.get(1).DeviceTypeId;
			LoadDeviceDetailsContent(deviceTypeId);
			break;
		case R.id.include4:
			deviceTypeId = CommonValues.getInstance().summary.deviceSummaryArray
					.get(2).DeviceTypeId;
			LoadDeviceDetailsContent(deviceTypeId);
			break;
		case R.id.include5:
			deviceTypeId = CommonValues.getInstance().summary.deviceSummaryArray
					.get(3).DeviceTypeId;
			LoadDeviceDetailsContent(deviceTypeId);
			break;

		default:
			break;
		}
	}

	public static void LoadDeviceDetailsContent(int deviceType) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_All_Device_Dashboard;
		if (asyncGetDeviceByType != null) {
			asyncGetDeviceByType.cancel(true);
		}
		asyncGetDeviceByType = new AsyncGetDevicesByType(Home.context,
				deviceType);
		asyncGetDeviceByType.execute();
	}

	public boolean sendGetDeviceByTypeRequest(Integer deviceType) {

		String getDeviceUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ CommonValues.getInstance().userId + "/devices?roomid=0"
				+ "&typeId=" + deviceType;

		if (JsonParser.getDevicesRequest(getDeviceUrl) != null) {
			return true;
		}
		return false;

	}

	public static Device deviceManagerEntity = null;
	public static DevicePropertyLog deviceLogEntity = null;
	public static DeviceProperty devicePropertyEntity = null;
	int deviceId = 0;

	public void setupDeviceByTypeListViewAdapter(int deviceType) {
		switch (deviceType) {
		case 1:
			tvdeviceValue.setText("  Fan");
			// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
			// 0, R.drawable.fan_on, 0, 0);
			btAddDevice.setBackgroundResource(R.drawable.fan_on);
			break;
		case 2:
			tvdeviceValue.setText("  Light");
			// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
			// 0, R.drawable.bulbon, 0, 0);
			btAddDevice.setBackgroundResource(R.drawable.bulbon);
			break;
		case 3:
			tvdeviceValue.setText("  AC");
			// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
			// 0, R.drawable.ac_large, 0, 0);
			btAddDevice.setBackgroundResource(R.drawable.ac_large_top);
			break;
		case 4:
			tvdeviceValue.setText("  Curtain");
			// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
			// 0, R.drawable.curtain_large, 0, 0);
			btAddDevice.setBackgroundResource(R.drawable.curtain_large);
			break;

		default:
			break;
		}
		setDeviceByTypeAdapter();

		deviceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				vfDeviceType.setInAnimation(CommonTask.inFromRightAnimation());
				vfDeviceType.setOutAnimation(CommonTask.outToLeftAnimation());

				vfDeviceType.setDisplayedChild(2);
				backState = DeviceTypeState.PROPERTY_STATE;
				dtAdapter.setSelection(position);
				deviceListView.setSelection(position);
				deviceListView.setSelectionFromTop(position, view.getTop());
				deviceManagerEntity = dtAdapter.getItemAtPosition(position);
				spinner1.setEnabled(true);

				loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
						deviceManagerEntity.Id);
				if (deviceManagerEntity.DeviceTypeId == 4) {
					shouldSetPreset = true;
				}
				deviceId = deviceManagerEntity.Id;
			}
		});

	}

	private void setDeviceByTypeAdapter() {
		if (CommonValues.getInstance().deviceList != null) {
			dtAdapter = new DeviceListByTypeAdapter(this,
					R.layout.device_typewise_listitem_view,
					CommonValues.getInstance().deviceList);
			deviceListView.setAdapter(dtAdapter);
			dtAdapter.setTouchEnabled(false);
			deviceListView.setEnabled(true);
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}

	}

	public void loadDeviceProperty(int DeviceTypeId, int deviceId) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Properties_Dashboard;
		System.out.println("Recall Property");
		if (asyncGetDeviceProperty != null) {
			asyncGetDeviceProperty.cancel(true);
		}
		asyncGetDeviceProperty = new AsyncGetDeviceProperty(Home.this,
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

	public void setDevicePropertyValue(DeviceProperty property) {

		switch (property.PropertyId) {
		case 1:// On_Off
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
			/*
			 * seekBar1.setOnSeekBarChangeListener(null); if
			 * (property.IsActionPending) {
			 * seekBar1.setProgress(Integer.parseInt(property
			 * .getPendingValue()));
			 * tvProgressValue.setText("Dimming : "+property.getPendingValue() +
			 * " %"); seekBar1.setEnabled(false); } else {
			 * seekBar1.setProgress(Integer.parseInt(property.getValue()));
			 * seekBar1.setEnabled(true);
			 * tvProgressValue.setText("Dimming : "+property.getValue() + " %");
			 * }
			 */
			// rv.SetListener(null);
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
			/*
			 * seekBar1.setOnSeekBarChangeListener(null); if
			 * (property.IsActionPending) {
			 * 
			 * seekBar1.setProgress(Integer.parseInt(property
			 * .getPendingValue())); seekBar1.setEnabled(false);
			 * tvProgressValue.setText(property.getPendingValue() + " %"); }
			 * else {
			 * seekBar1.setProgress(Integer.parseInt(property.getValue()));
			 * seekBar1.setEnabled(true);
			 * tvProgressValue.setText(property.getValue() + " %"); }
			 */
			// rv.SetListener(null);
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

	public void sendSetProperty(int userId, int value, int DeviceId,
			int propertyId) {
		if (asyncGetSetPropertyFromDashBoard != null) {
			asyncGetSetPropertyFromDashBoard.cancel(true);
		}
		asyncGetSetPropertyFromDashBoard = new AsyncGetSetPropertyFromDashBoard(
				Home.this, userId, DeviceId, propertyId, value);
		asyncGetSetPropertyFromDashBoard.execute();

	}

	int lightKnobValue = 0;

	public void setDevicePropertyControlData(int deviceTypeId) {
		hideRotatingKnob();
		switch (deviceTypeId) {
		case 1:// Fan
			// hideRotatingKnob();
			getSupportActionBar().setTitle("Fan Control");
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			generateRotatingKnobView();

			rv.SetListener(new com.sinepulse.app.activities.RoundKnobButton.RoundKnobButtonListener() {
				public void onRotate(final int percentage) {
					tv2.post(new Runnable() {
						public void run() {
							tv2.setText(percentage + " %");
							int fanKnobValue = percentage;
							if (fanKnobValue == 99) {
								fanKnobValue = 100;
							}
							sendSetProperty(CommonValues.getInstance().userId,
									fanKnobValue, deviceId, 3);
							loadDeviceProperty(
									deviceManagerEntity.DeviceTypeId,
									deviceManagerEntity.Id);
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
			// hideRotatingKnob();
			getSupportActionBar().setTitle("Light Control");
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			generateRotatingKnobView();
			rv.SetListener(new com.sinepulse.app.activities.RoundKnobButton.RoundKnobButtonListener() {

				public void onRotate(final int percentage) {
					tv2.post(new Runnable() {
						public void run() {
							tv2.setText(percentage + " %");
							lightKnobValue = percentage;
							if (lightKnobValue == 99) {
								lightKnobValue = 100;
							}
							sendSetProperty(CommonValues.getInstance().userId,
									lightKnobValue, deviceId, 2);
							loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
									deviceManagerEntity.Id);
						}
					});
				}

			});

			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 3:// Ac
			// hideRotatingKnob();
			getSupportActionBar().setTitle("AC Control");
			ivDevice.setImageDrawable(getResources().getDrawable(
					R.drawable.ac_large));
			seekBar1.setVisibility(View.INVISIBLE);
			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			break;
		case 4:// Curtain
			// hideRotatingKnob();
			getSupportActionBar().setTitle("Curtain Control");
			spinner1.setVisibility(View.VISIBLE);
			loadCurtainPresetValues(CommonValues.getInstance().userId,
					deviceManagerEntity.Id);
			seekBar1.setVisibility(View.INVISIBLE);

			tvProgressValue.setVisibility(View.INVISIBLE);
			porda.setVisibility(View.VISIBLE);
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			break;

		default:
			break;
		}
		if (CommonValues.getInstance().devicePropertyList != null) {
			for (int i = 0; i < CommonValues.getInstance().devicePropertyList
					.size(); i++) {
				// System.out.println("Device "+CommonValues.getInstance().devicePropertyList
				// .get(i));
				setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
						.get(i));
			}
		} else {
			CommonTask.ShowMessage(this, "Network Problem.Please Retry.");
		}
	}

	/**
	 * 
	 */
	ViewGroup parent;

	public void generateRotatingKnobView() {
		View C = findViewById(R.id.propertyMiddleView);
		parent = (ViewGroup) C.getParent();
		// panel = (RelativeLayout)
		// findViewById(R.id.propertyMiddleView).getParent();
		rv = new RoundKnobButton(this, R.drawable.stator, R.drawable.rotoron,
				R.drawable.rotoroff, m_Inst.Scale(360), m_Inst.Scale(360));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		// lp.setMargins(0, 0, 0, 200);
		parent.addView(rv, lp);

		tv2 = new TextView(this);
		tv2.setText("");
		tv2.setTextColor(getResources().getColor(R.color.lime));
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.font_size));
		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		parent.addView(tv2, lp);
	}

	/**
	 * 
	 */
	public void hideRotatingKnob() {
		if (parent != null) {
			if (rv.getVisibility() == View.VISIBLE) {
				parent.removeView(rv);
				rv.setVisibility(View.GONE);
			}
			if (tv2.getVisibility() == View.VISIBLE) {
				parent.removeView(tv2);
				tv2.setVisibility(View.GONE);
			}
		}
		/*
		 * if(rv!=null){ panel.removeView(rv); rv.setVisibility(View.GONE);
		 * 
		 * } if(tv2!=null){ panel.removeView(tv2); tv2.setVisibility(View.GONE);
		 * }
		 */
	}

	private void loadCurtainPresetValues(int userId, int deviceId) {
		if (asyncGetCurtainPresetValues != null) {
			asyncGetCurtainPresetValues.cancel(true);
		}
		asyncGetCurtainPresetValues = new AsyncGetCurtainPresetValues(this,
				userId, deviceId);
		asyncGetCurtainPresetValues.execute();

	}

	private void LoadDeviceLogContent(int deviceId, int FilterType,
			String fromDate, String toDate) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Activities_Dashboard;
		if (asyncGetDeviceLogInfo != null) {
			asyncGetDeviceLogInfo.cancel(true);
		}
		asyncGetDeviceLogInfo = new AsyncGetDeviceLogInfo(this, deviceId,
				FilterType, fromDate, toDate);
		asyncGetDeviceLogInfo.execute();

	}

	public boolean sendGetDeviceLogRequest(Integer deviceId, int FilterType,
			String fromDate, String toDate) {

		String getDeviceLogUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(CommonValues.getInstance().userId)
				+ "/device/" + String.valueOf(deviceId) + "/activities";
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
				dLogAdapter = new DeviceLogAdapter(Home.this,
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

	ExpandableListView.OnGroupClickListener groupClickListener = new ExpandableListView.OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView eListView, View view,
				int groupPosition, long id) {
			// cancelAsyncOnVisibleFlipper();
			if (groupPosition != 7) {
				displayFragment(groupPosition);
			} else {
				CommonTask.ShowLogOutConfirmation(Home.context,
						"Do you really want to Log Out from the app?",
						showLogOutEvent());
			}

			return false;
		}
	};

	/**
	 * 
	 */
	public void redirectToLogInPage() {
		if (CommonValues.getInstance().logoutResponse == true) {
			removePreferenceLoginData();
			Intent intent = new Intent("com.sinepulse.app.activities.UserLogin");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		} else {
			CommonTask
					.ShowMessage(this,
							"Something went wrong while logout.Please exit the app and login again.");
		}
	}

	/**
	 * 
	 */
	public void removePreferenceLoginData() {
		SharedPreferences password = UserLogin.context.getSharedPreferences(
				CommonConstraints.PREF_PASSWORD_KEY,
				UserLogin.context.MODE_PRIVATE);
		password.edit().remove(CommonConstraints.PREF_PASSWORD_KEY).commit();
		SharedPreferences userName = UserLogin.context.getSharedPreferences(
				CommonConstraints.PREF_LOGINUSER_NAME,
				UserLogin.context.MODE_PRIVATE);
		userName.edit().remove(CommonConstraints.PREF_LOGINUSER_NAME).commit();
	}

	public void addDrawerMenuItems() {
		// All Device
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// User Log
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Settings
		basketDrawerItem = new NavDrawerItem(navMenuTitles[2],
				navMenuIcons.getResourceId(2, -1), true, "0");
		navDrawerItems.add(basketDrawerItem);
		// Change Pass
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));

		// Support
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// Help
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// About
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		// LogOut
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
		Log.d("selectedposition", "" + mDrawerList.getCheckedItemPosition());
		if (mDrawerList.getCheckedItemPosition() == 2) {
			hideRefreshMenu(menu);
		}
		if (mDrawerList.getCheckedItemPosition() == 3) {
			hideRefreshMenu(menu);
		}
		if (mDrawerList.getCheckedItemPosition() == 4) {
			hideRefreshMenu(menu);
		}
		if (mDrawerList.getCheckedItemPosition() == 5) {
			hideRefreshMenu(menu);
		}

		return prepared;
	}

	void cancelFromSettingsScreen() {
		backToHomeScreen();
	}

	/**
	 * Manage back-button pressed event in anywhere from home screen
	 * backState=-1 is use for application exit and 0 is use for back from
	 * setting screen to home
	 */
	@Override
	public void onBackPressed() {
		spinner1.setEnabled(true);
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
		// Log.d("backcount",""+getSupportFragmentManager().getBackStackEntryCount());
		if (currentFragment == ALLDEVICE_FRAGMENT) {
			if (backState == DeviceTypeState.INITIAL_STATE) {
				if(MainActionbarBase.stackIndex!=null){
				MainActionbarBase.stackIndex.removeAllElements();
				}
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			vfDeviceType.setInAnimation(CommonTask.inFromLeftAnimation());
			vfDeviceType.setOutAnimation(CommonTask.outToRightAnimation());
			if (backState == DeviceTypeState.DEVICE_STATE) {
				clearDeviceByTypeData();
				new AsyncRefreshDashBoard(Home.this).execute();
				getSupportActionBar().setTitle("Dashboard");
				vfDeviceType.setDisplayedChild(0);
				backState = DeviceTypeState.INITIAL_STATE;
			}
			if (backState == DeviceTypeState.PROPERTY_STATE) {
				// clearDeviceByTypeData();
				getSupportActionBar().setTitle("All Devices");
				vfDeviceType.setDisplayedChild(1);
				backState = DeviceTypeState.DEVICE_STATE;
				LoadDeviceDetailsContent(deviceTypeId);
			}
			if (backState == DeviceTypeState.VIEWLOG_STATE) {
				etDateFrom.setText("");
				etDateTo.setText("");
				getSupportActionBar().setTitle("Device Control");
				vfDeviceType.setDisplayedChild(2);
				backState = DeviceTypeState.PROPERTY_STATE;
				loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
						deviceManagerEntity.Id);
			}
		}

		if (mDrawerList.getCheckedItemPosition() != currentFragment
				&& mDrawerList.getCount() > currentFragment) {
			mDrawerList.setItemChecked(currentFragment, true);
			navDrawerAdapter.setSelectedPosition(currentFragment);
		}

		if (this.getCurrentFocus() != null)
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public DialogInterface.OnClickListener showLogOutEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					CommonValues.getInstance().summary.deviceSummaryArray
							.clear();
					CommonValues.getInstance().userId = 0;
					CommonValues.getInstance().currentAction = CommonIdentifier.Action_LogOut;
					if (asyncLogOutTask != null) {
						asyncLogOutTask.cancel(true);
					}
					asyncLogOutTask = new AsyncLogOutTask(Home.this,
							CommonValues.getInstance().userId);
					asyncLogOutTask.execute();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();

					break;

				default:
					break;
				}

			}

		};
		return dialogClickListener;

	}

	/**
	 * Clear Device By Type page Data
	 */
	public void clearDeviceByTypeData() {
		CommonValues.getInstance().deviceList.clear();
		deviceListView.setAdapter(null);
		tvdeviceValue.setText("");
		// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		btAddDevice.setBackgroundResource(0);
	}

	private void backToHomeScreen() {
		backState = DeviceTypeState.INITIAL_STATE;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static boolean showSettingsScreen = false;

	@Override
	protected void onResume() {
		super.onResume();
		CommonValues.getInstance().home = (Home_) this;
		currentFragment = ALLDEVICE_FRAGMENT;

		if (vfDeviceType.getDisplayedChild() == 0) {
			this.setTitle("Dashboard");
			CommonValues.getInstance().currentAction = CommonIdentifier.Action_Summary;
			if (asyncRefreshDashBoard != null) {
				asyncRefreshDashBoard.cancel(true);
			}
			asyncRefreshDashBoard = new AsyncRefreshDashBoard(Home.this);
			asyncRefreshDashBoard.execute();
		}
		if (vfDeviceType.getDisplayedChild() == 1) {
			this.setTitle("All Devices");
			LoadDeviceDetailsContent(deviceTypeId);

		}
		if (vfDeviceType.getDisplayedChild() == 2) {
			this.setTitle("Device Control");
			loadDeviceProperty(deviceManagerEntity.DeviceTypeId,
					deviceManagerEntity.Id);

		}

		bDashboard.setBackground(getResources().getDrawable(
				R.drawable.dashboard_selected1));
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (mDrawerList != null) {
			mDrawerList.setItemChecked(currentFragment, true);
			// mDrawerList.setSelection(currentFragment);
			navDrawerAdapter.setSelectedPosition(currentFragment);
			// Close the Drawer if already Opened
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	@Override
	public void displayFragment(int position) {
		// cancelAsyncOnVisibleFlipper();
		// if selected from navigation drawer
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			// update the main content by replacing fragments
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		super.displayFragment(position);
		mDrawerList.setItemChecked(position, true);
		// setTitle(navMenuTitles[position]);
		// mDrawerList.setSelection(position);
		navDrawerAdapter.setSelectedPosition(currentFragment);

	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		// Hide the Soft Keypad Globally..Tanvir
		if (this.getCurrentFocus() != null)
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
			return true;
		}
		if (item.getItemId() == R.id.menu_refresh) {
			// Toast.makeText(this, "Please Wait..Page is Refreshing",
			// Toast.LENGTH_SHORT).show();
			switch (vfDeviceType.getDisplayedChild()) {
			case 0:
				Toast.makeText(this, "Please Wait..Refreshing DashBoard",
						Toast.LENGTH_SHORT).show();
				// new AsyncRefreshDashBoard(this).execute();
				if (asyncRefreshDashBoard != null) {
					asyncRefreshDashBoard.cancel(true);
				}
				asyncRefreshDashBoard = new AsyncRefreshDashBoard(Home.this);
				asyncRefreshDashBoard.execute();

				break;
			case 1:
				Toast.makeText(this, "Please Wait..Refreshing Device List",
						Toast.LENGTH_SHORT).show();
				LoadDeviceDetailsContent(deviceTypeId);
				break;
			case 2:
				Toast.makeText(this, "Please Wait..Refreshing Properties",
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

		return super.onOptionsItemSelected(item);

	}

	private android.view.MenuItem getMenuItem(
			final com.actionbarsherlock.view.MenuItem item) {
		return new android.view.MenuItem() {
			@Override
			public int getItemId() {
				return item.getItemId();
			}

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean collapseActionView() {
				return false;
			}

			@Override
			public boolean expandActionView() {
				return false;
			}

			@Override
			public ActionProvider getActionProvider() {
				return null;
			}

			@Override
			public View getActionView() {
				return null;
			}

			@Override
			public char getAlphabeticShortcut() {
				return 0;
			}

			@Override
			public int getGroupId() {
				return 0;
			}

			@Override
			public Drawable getIcon() {
				return null;
			}

			@Override
			public Intent getIntent() {
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo() {
				return null;
			}

			@Override
			public char getNumericShortcut() {
				return 0;
			}

			@Override
			public int getOrder() {
				return 0;
			}

			@Override
			public SubMenu getSubMenu() {
				return null;
			}

			@Override
			public CharSequence getTitle() {
				return null;
			}

			@Override
			public CharSequence getTitleCondensed() {
				return null;
			}

			@Override
			public boolean hasSubMenu() {
				return false;
			}

			@Override
			public boolean isActionViewExpanded() {
				return false;
			}

			@Override
			public boolean isCheckable() {
				return false;
			}

			@Override
			public boolean isChecked() {
				return false;
			}

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider) {
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view) {
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId) {
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable) {
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked) {
				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled) {
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon) {
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes) {
				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent) {
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar) {
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener) {
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener) {
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar) {
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum) {

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title) {
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title) {
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title) {
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible) {
				return null;
			}
		};
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void searchClicked() {
		super.searchClicked();
		isSearchExpanded = true;
	}

	SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d = new Date();

	@Override
	@Click({ R.id.bCamera, R.id.bRoom, R.id.bDashboard, R.id.btShowLog,
			R.id.etDateFrom, R.id.etDateTo, R.id.tvToday, R.id.tvYesterday,
			R.id.bSearch })
	public void onClick(View v) {
		// vfDeviceType.setDisplayedChild(0);

		switch (v.getId()) {
		case R.id.bCamera:
			// cancelAsyncOnVisibleFlipper();
			if(MainActionbarBase.stackIndex!=null){
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
			// cancelAsyncOnVisibleFlipper();
			if(MainActionbarBase.stackIndex!=null){
			MainActionbarBase.stackIndex.removeAllElements();
			}
			currentFragment = ROOM_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
			Intent roomIntent = new Intent(this, RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);
			break;
		case R.id.bDashboard:
			// getSupportActionBar().setTitle("Dash Board");
			mDrawerList.setItemChecked(0, true);
			navDrawerAdapter.setSelectedPosition(ALLDEVICE_FRAGMENT);

			currentFragment = ALLDEVICE_FRAGMENT;
			if (vfDeviceType.getDisplayedChild() == 0) {
				return;
			} else {
				getSupportActionBar().setTitle("Dashboard");
				new AsyncRefreshDashBoard(Home.this).execute();
				CommonValues.getInstance().deviceList.clear();
				deviceListView.setAdapter(null);
				tvdeviceValue.setText("");
				// btAddDevice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
				// 0);
				btAddDevice.setBackgroundResource(0);
				vfDeviceType.setDisplayedChild(0);
			}
			break;
		case R.id.btShowLog:
			getSupportActionBar().setTitle("Device Activity");
			vfDeviceType.setInAnimation(CommonTask.inFromRightAnimation());
			vfDeviceType.setOutAnimation(CommonTask.outToLeftAnimation());
			backState = DeviceTypeState.VIEWLOG_STATE;
			vfDeviceType.setDisplayedChild(3);
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
			etDateFrom.setInputType(InputType.TYPE_NULL);
			etDateTo.setInputType(InputType.TYPE_NULL);
//			etDateFrom.setText(fromDate);
//			etDateTo.setText(toDate);
			etDateFrom.setHint("Start Date");
			etDateTo.setHint("End Date");
			etDateFrom.requestFocus();
			setDateTimeField();
			bSearch.setVisibility(View.INVISIBLE);
			LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter.format(d)
					.toString(), formatter.format(d).toString());
			break;
		case R.id.etDateFrom:
			if (fromDatePickerDialog != null)
				fromDatePickerDialog.show();
			fromDatePickerDialog.getDatePicker().getCalendarView().setBackgroundResource(R.drawable.abs__ab_solid_light_holo);

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

			/*String tDelims = "T";
			String[] tTokens = dateFormatter.format(d).toString()
					.split(tDelims);*/
			etDateFrom.setText("");
			etDateTo.setText("");

			// bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}
			LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter.format(d)
					.toString(), formatter.format(d).toString());
			break;
		case R.id.tvYesterday:
			if (tvEmptyLog.isShown()) {
				tvEmptyLog.setVisibility(View.GONE);
			}

		/*	String yDelims = "T";
			String[] yTokens = dateFormatter
					.format(d.getTime() - 24 * 60 * 60 * 1000).toString()
					.split(yDelims);*/
			etDateFrom.setText("");
			etDateTo.setText("");

			// bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#2C5197"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}
			LoadDeviceLogContent(deviceManagerEntity.DeviceTypeId, 2, formatter
					.format(d.getTime() - 24 * 60 * 60 * 1000).toString(),
					formatter.format(d.getTime() - 24 * 60 * 60 * 1000)
							.toString());
			break;
		case R.id.bSearch:
			if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
				deviceLogListView.setAdapter(null);
				dLogAdapter.clear();
			}

			LoadDeviceLogContent(deviceManagerEntity.Id, 3, stserverDate,
					lstserverDate);

			break;

		default:
			break;
		}

	}

	/**
	 * @return
	 */
	public StringBuilder getTodaysFormattedDate() {
		return new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" ");
	}

	/**
	 * 
	 */
	public void cancelAsyncOnVisibleFlipper() {
		if (vfDeviceType.getDisplayedChild() == 0) {
			asyncRefreshDashBoard.cancel(true);
		}
		if (vfDeviceType.getDisplayedChild() == 1) {
			asyncGetDeviceByType.cancel(true);
		}
		if (vfDeviceType.getDisplayedChild() == 2) {
			asyncGetDeviceProperty.cancel(true);
		}
		if (vfDeviceType.getDisplayedChild() == 3) {
			asyncGetDeviceLogInfo.cancel(true);
		}
	}

	private boolean validateLastDateInput() {

		try {
//			Date firstDate = dateFormatter.parse(stDate);
			Date lastDate = dateFormatter.parse(lstDate);
			Date CurrentDate = d;
			 if(etDateFrom.getText().toString().length()==0){
					CommonTask.ShowMessage(this,
							"Please select Start Date first.");
					etDateTo.setText("");
					return false;
				}
			 else if (lastDate.after(CurrentDate)) {
				CommonTask.ShowMessage(this,
						"End date can't be greater than Current Date");
				etDateTo.setText("");
				return false;
			} else if (lastDate.before(tobesetFromDate)) {
				CommonTask.ShowMessage(this,
						"End date can't be less than Start date");
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
		CommonTask.ShowMessage(this, "Please select Start Date First");
	}

	public static void showFirstDateError() {
		CommonTask.ShowMessage(Home.context,
				"Start Date can't be Greater than Current Date.");
	}

	public void startDeviceProgress() {
		devicelistProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDeviceProgress() {
		if (null != devicelistProgressBar && devicelistProgressBar.isShown()) {

			devicelistProgressBar.setVisibility(View.GONE);
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

	public void startLogProgress() {
		deviceLogProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopLogProgress() {
		if (null != deviceLogProgressBar && deviceLogProgressBar.isShown()) {

			deviceLogProgressBar.setVisibility(View.GONE);
		}
	}

	public void startDashBoardProgress() {
		dashBoardProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDashBoardProgress() {
		if (null != dashBoardProgressBar && dashBoardProgressBar.isShown()) {

			dashBoardProgressBar.setVisibility(View.GONE);
		}
	}

	public boolean setPropertyRequestFromDashboard(int userId, int deviceId,
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

	public boolean sendSetPropertyRequestForSeekbar(int userId, int deviceId,
			int propertyId, int value) {
		String setPropertyUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ userId + "/property?id=" + deviceId + "&propertyid="
				+ propertyId + "&value=" + value;
		if (JsonParser.setProptyerRequest(setPropertyUrl) != null) {
			return true;
		}
		return false;

	}

	public void setPropertyResponseData() {
		for (int i = 0; i < CommonValues.getInstance().devicePropertyList
				.size(); i++) {
			setDevicePropertyValue(CommonValues.getInstance().devicePropertyList
					.get(i));
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
		ArrayAdapter<String> presetAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, presetValues);
		spinner1.setAdapter(presetAdapter);
		presetAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

	public boolean sendLogOutRequest(int userId) {
		String logOutUrl = CommonURL.getInstance().LogOutURL;

		if (JsonParser.postLogOutRequest(logOutUrl, userId,
				CommonValues.getInstance().appToken, 1) != null) {
			return true;
		}
		return false;

	}

	public boolean sendGetSummaryRequest() {

		String getSummaryUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(CommonValues.getInstance().userId)
				+ "/summary";
		if (JsonParser.getSummaryRequest(getSummaryUrl) != null) {
			return true;
		}
		return false;

	}

	String stDate = "";
	String lstDate = "";
	String stserverDate = formatter.format(d).toString();
	String lstserverDate = formatter.format(d).toString();
	Calendar newDate = Calendar.getInstance();

	private void setDateTimeField() {
		newCalendar = Calendar.getInstance();

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
	

}