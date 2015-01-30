package com.sinepulse.app.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
	public Button btAddDevice;
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
	public EditText etDateFrom;
	@ViewById(R.id.etDateTo)
	public EditText etDateTo;
	@ViewById(R.id.tvEmptyLog)
	public TextView tvEmptyLog;
	AsyncGetSetPropertyFromDashBoard asyncGetSetPropertyFromDashBoard = null;
	int modifiedIndex = 0;
	String fromDate = "";
	String toDate = "";
	int seekBarProgressValue = 0;
	int curtainPropertyId = 0;
	int curtainMultiplier = 0;

	int presetItemPosition;
	int deviceTypeId;
	boolean shouldSetPreset = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Home.context = this;
		mSupportActionBar.setDisplayHomeAsUpEnabled(false);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		backState = DeviceTypeState.INITIAL_STATE;

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
		gestureDetector = new GestureDetector(context, new GestureListener());
		vfDeviceType.setDisplayedChild(0);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		CommonTask.loadSettings(this);
		CommonTask.loadLoginUser(this);
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
							+ "  watts in use");
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
							.get(1)).DeviceCount + " Bulbs");
			// Total AC
			home_btn_ac.setText("   "
					+ (CommonValues.getInstance().summary.deviceSummaryArray
							.get(2)).DeviceCount + " AC s");
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
			btAddDevice.setText("  Fan");
			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.fan_on, 0, 0, 0);
			break;
		case 2:
			btAddDevice.setText("  Light");
			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.bulbon, 0, 0, 0);
			break;
		case 3:
			btAddDevice.setText("  AC");
			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ac_large, 0, 0, 0);
			break;
		case 4:
			btAddDevice.setText("  Curtain");
			btAddDevice.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.curtain_large, 0, 0, 0);
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

	/**
	 * 
	 */
	private void setDeviceByTypeAdapter() {
		if (CommonValues.getInstance().deviceList != null) {
			dtAdapter = new DeviceListByTypeAdapter(this,
					R.layout.device_typewise_listitem_view,
					CommonValues.getInstance().deviceList);
			// for (int i = 0; i < CommonValues.getInstance().deviceList.size();
			// i++) {
			// System.out.println("First Is On "
			// + CommonValues.getInstance().deviceList.get(i).IsOn);
			// }
			deviceListView.setAdapter(dtAdapter);
			dtAdapter.setTouchEnabled(false);
			deviceListView.setEnabled(true);
		} else {
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}

	}

	/**
	 * 
	 */
	public void loadDeviceProperty(int DeviceTypeId, int deviceId) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_Properties_Dashboard;
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

	public void setDevicePropertyValue(final DeviceProperty property) {

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
		case 2:// Dimming
			seekBar1.setOnSeekBarChangeListener(null);
			if (property.IsActionPending) {
				seekBar1.setProgress(Integer.parseInt(property
						.getPendingValue()));
				tvProgressValue.setText(property.getPendingValue() + " %");
				seekBar1.setEnabled(false);
			} else {
				seekBar1.setProgress(Integer.parseInt(property.getValue()));
				seekBar1.setEnabled(true);
				tvProgressValue.setText(property.getValue() + " %");
			}
			break;
		case 3:// Dimming
			seekBar1.setOnSeekBarChangeListener(null);
			if (property.IsActionPending) {

				seekBar1.setProgress(Integer.parseInt(property
						.getPendingValue()));
				seekBar1.setEnabled(false);
				tvProgressValue.setText(property.getPendingValue() + " %");
			} else {
				seekBar1.setProgress(Integer.parseInt(property.getValue()));
				seekBar1.setEnabled(true);
				tvProgressValue.setText(property.getValue() + " %");
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
						(isChecked ? 1 : 0), property.DeviceId, 1);
			}
		});
		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				seekBarProgressValue = seekBar.getProgress();
				if (deviceManagerEntity.DeviceTypeId == 1) {
					sendSetProperty(CommonValues.getInstance().userId,
							seekBarProgressValue, property.DeviceId, 3);
				} else if (deviceManagerEntity.DeviceTypeId == 2) {
					sendSetProperty(CommonValues.getInstance().userId,
							seekBarProgressValue, property.DeviceId, 2);
				}
				tvProgressValue.setText(String.valueOf(seekBarProgressValue)
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

	public void setDevicePropertyControlData(int deviceTypeId) {
		switch (deviceTypeId) {
		case 1:// Fan
			getSupportActionBar().setTitle("Fan Control");
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			seekBar1.setVisibility(View.VISIBLE);
			tvProgressValue.setVisibility(View.VISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 2:// Light
			getSupportActionBar().setTitle("Light Control");
			tvDeviceName.setText(deviceManagerEntity.Name + "  ");
			seekBar1.setVisibility(View.VISIBLE);
			tvProgressValue.setVisibility(View.VISIBLE);
			porda.setVisibility(View.INVISIBLE);
			spinner1.setVisibility(View.INVISIBLE);
			break;
		case 3:// Ac
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
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}
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
			if (groupPosition != 6) {
				displayFragment(groupPosition);
			} else {
				if (asyncLogOutTask != null) {
					asyncLogOutTask.cancel(true);
				}
				asyncLogOutTask = new AsyncLogOutTask(Home.this,
						CommonValues.getInstance().userId);
				asyncLogOutTask.execute();
			}

			return false;
		}
	};

	/**
	 * 
	 */
	public void redirectToLogInPage() {
		if (CommonValues.getInstance().logoutResponse == true) {
			Intent intent = new Intent("com.sinepulse.app.activities.UserLogin");
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else {
			CommonTask.ShowMessage(this, "Server Error ! LogOut Failed.");
		}
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
		// About
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// Help
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		// LogOut
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

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

	/**
	 * @param menu
	 */
	// private void hideRefreshMenu(com.actionbarsherlock.view.Menu menu) {
	// MenuItem refresh = menu.findItem(R.id.menu_refresh);
	// refresh.setVisible(false);
	// invalidateOptionsMenu();
	// }

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
				MainActionbarBase.stackIndex.removeAllElements();
				// systems back to finish
				CommonValues.getInstance().summary.deviceSummaryArray.clear();
				CommonValues.getInstance().userId = 0;
				Intent loginintent = new Intent(
						"com.sinepulse.app.activities.UserLogin");
				loginintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(loginintent);
				super.onBackPressed();

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
				// LoadDeviceDetailsContent(CommonValues.getInstance().summary.deviceSummaryArray
				// .get(0).DeviceTypeId);
			}
			if (backState == DeviceTypeState.VIEWLOG_STATE) {
				// clearDeviceByTypeData();
				if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
					// deviceLogListView.setAdapter(null);
					// dLogAdapter.clear();
				}
//				etDateFrom.setText("");
//				etDateTo.setText("");
//				bSearch.setVisibility(View.INVISIBLE);
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

	/**
	 * Clear Device By Type page Data
	 */
	public void clearDeviceByTypeData() {
		CommonValues.getInstance().deviceList.clear();
		deviceListView.setAdapter(null);
		btAddDevice.setText("");
		btAddDevice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
			this.setTitle("DashBoard");
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
		// goBackToHomeFragment();

		if (showSettingsScreen) {
			showSettingsScreen = false;
		}

		// on resume we have to set proper selection in navigation drawer, if on
		// resumed we check our back stack for which was was the last entry
		// and set it, if no item in back stack then we set it to home
		/*
		 * if (fragmentBackStack.size() > 0 && currentFragment !=
		 * ALLDEVICE_FRAGMENT) currentFragment = fragmentBackStack.peek(); else
		 * { currentFragment = ALLDEVICE_FRAGMENT; }
		 */
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
		// if (position == 1) {
		// spinner1.setEnabled(false);
		// } else if (position == 0) {
		// spinner1.setEnabled(true);
		// }

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
			R.id.etDateFrom, R.id.etDateTo, R.id.tvToday, R.id.tvYesterday })
	public void onClick(View v) {
		// vfDeviceType.setDisplayedChild(0);

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
			// cancelAsyncOnVisibleFlipper();
			MainActionbarBase.stackIndex.removeAllElements();
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
				new AsyncRefreshDashBoard(Home.this).execute();
				CommonValues.getInstance().deviceList.clear();
				deviceListView.setAdapter(null);
				btAddDevice.setText("");
				btAddDevice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				vfDeviceType.setDisplayedChild(0);
			}
			break;
		case R.id.btShowLog:
			getSupportActionBar().setTitle("Device Activity");
			String delims ="T" ;
		    String[] tokens = formatter.format(d).toString().split(delims);
		    etDateFrom.setText(tokens[0]);
		    etDateTo.setText(tokens[0]);
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
						R.drawable.curtainsmall, 0, 0, 0);
			}
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			LoadDeviceLogContent(deviceManagerEntity.Id, 1, formatter.format(d)
					.toString(), formatter.format(d).toString());
			break;
		case R.id.etDateFrom:
		case R.id.etDateTo:
			showCalendar(v);

			break;
		case R.id.tvToday:
//			etDateFrom.setText("");
//			etDateTo.setText("");
//			bSearch.setVisibility(View.INVISIBLE);
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
			etDateFrom.setText("");
			etDateTo.setText("");
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
				CommonTask.ShowMessage(this, "To date cant be greater than current date");
				etDateTo.setText("");
				return false;
			}else if( lastDate.before(firstDate)){
				CommonTask.ShowMessage(this, "To date cant be less than From date");
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

	/*
	 * public void startNavDrawerProgress() {
	 * nav_drawer_progress_bar.setVisibility(View.VISIBLE); }
	 * 
	 * public void stopNavDrawerProgress() { if (null != nav_drawer_progress_bar
	 * && nav_drawer_progress_bar.isShown()) {
	 * 
	 * nav_drawer_progress_bar.setVisibility(View.GONE); } }
	 */

	public boolean setPropertyRequestFromDashboard(int userId, int deviceId,
			int propertyId, int value) {
		String setPropertyUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ userId + "/property?id=" + deviceId + "&propertyid="
				+ propertyId + "&value=" + value;
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
						.getName();
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
		String logOutUrl = CommonURL.getInstance().LogOutURL + "/logout";

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

}