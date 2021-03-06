/**
 * 
 */
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
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.adapters.UserLogAdapter;
import com.sinepulse.app.asynctasks.AsyncGetUserLogInfo;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * This class will be used to view users activity on system.there are different search parameter 
 * for searching users activity.this page is a view only page.
 * @author tanvir.ahmed
 * 
 */
@EActivity(R.layout.user_log)
public class UserLogActivity extends MainActionbarBase implements
		OnClickListener {

	public static final int INITIAL_STATE = -1;

	public enum UserLogSate {
		INITIAL_STATE // backState -1

	};

	boolean fragmentPaused = false;

	public static UserLogSate backState = UserLogSate.INITIAL_STATE;
	static AsyncGetUserLogInfo asyncGetUserLogInfo = null;
	private UserLogAdapter uLogAdapter;
	@ViewById(R.id.lvUserLogList)
	public ListView userLogListView;
	@ViewById(R.id.userLogProgressBar)
	public static ProgressBar userLogProgressBar;
	@ViewById(R.id.bDeliverydate)
	public Button bDeliverydate;
	@ViewById(R.id.bSearch)
	public Button bSearch;
	@ViewById(R.id.etDateFrom)
	public EditText etDateFrom;
	@ViewById(R.id.etDateTo)
	public EditText etDateTo;
	@ViewById(R.id.tvEmptyLog)
	public TextView tvEmptyLog;
	@ViewById(R.id.tvToday)
	public TextView tvToday;
	@ViewById(R.id.tvYesterday)
	public TextView tvYesterday;
	String fromDate = "";
	String toDate = "";
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d = new Date();
	int year;
	int month;
	int day;
	private DatePickerDialog fromDatePickerDialog = null;
	private DatePickerDialog toDatePickerDialog = null;
	private SimpleDateFormat dateFormatter;
	Date tobesetFromDate = new Date();
	Date tobesetToDate = new Date();

	int FilterType = 0;
	String fromsDate = "";
	String tosDate = "";
	int PageNumber = 0;
	int ChunkSize = 30;
	public static Context context;
//	Menu connMenu=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getActivity().setContentView(R.layout.catalogue2);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		UserLogActivity.context=this;
        mainActionBarContext=UserLogActivity.context;
//        this.connMenu=actionBarMenu;
		createMenuBar();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		setDateTimeField();
	}

	@AfterViews
	void afterViewLoaded() {
		bSearch.setVisibility(View.INVISIBLE);
		
		loadTodaysLog();
		/*
		 * fromDate=formatter.format(d).toString();
		 * toDate=formatter.format(d).toString();
		 * fromDate = dateFormatter.format(d).toString(); toDate =
		 * dateFormatter.format(d).toString(); etDateFrom.setText(fromDate);
		 * etDateTo.setText(toDate);
		 */
		etDateFrom.setHint("Start Date");
		etDateTo.setHint("End Date");

		// bSearch.setVisibility(View.INVISIBLE);
		etDateFrom.setOnClickListener(this);
		etDateTo.setOnClickListener(this);

		tvYesterday.setOnClickListener(this);
		tvToday.setOnClickListener(this);

	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setTitle("Activities");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);

	}
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
//		connMenu=menu;
		setConnectionNodeImage(CommonValues.getInstance().globalMenu,this);
		return prepared;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		
		if (item.getItemId() == R.id.menu_conn_indicatior) {
			if (status.equals("Mobiledata enabled") && CommonValues.getInstance().connectionMode.equals("Internet") ) {
			CommonTask.ShowMessage(this, "Local mode is not accessible in GSM network.Please try with WiFi.");
		}else{
			CommonTask
			.ShowNetworkChangeConfirmation(
					UserLogActivity.this,
					"Do you Really want to change mode?.",
					showNetworkChangeEvent());
		}
		}
		
		if (item.getItemId() == R.id.menu_refresh) {
			loadTodaysLog();
		}
		return true;
	}

	/**
	 * 
	 */
	public void loadTodaysLog() {
		

		tvToday.setTextColor(Color.parseColor("#6699ff"));
		tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
		/*
		 * esl.FilterType=1; esl.fromDate=formatter.format(d).toString();
		 * esl.toDate=formatter.format(d).toString();
		 */
		FilterType = 1;
		fromsDate = formatter.format(d).toString();
		tosDate = formatter.format(d).toString();
		PageNumber = 1;
		// ChunkSize=40;

		loadUserLogInfo(FilterType, fromsDate, tosDate, PageNumber, ChunkSize);

	}

	@Override
	@Click({ R.id.bDeliverydate, R.id.etDateFrom, R.id.etDateTo, R.id.tvToday,
			R.id.tvYesterday, R.id.bCamera, R.id.bDashboard, R.id.bRoom,
			R.id.bSearch })
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.bDeliverydate:
		// Search logic will go here
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
			if (toDatePickerDialog != null)
				toDatePickerDialog.show();
			toDatePickerDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {

					toDatePickerDialog.getDatePicker().getCalendarView()
							.setDate(tobesetToDate.getTime());
				}
			});
			// bSearch.setVisibility(View.VISIBLE);
			// showCalendar(v);
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
			if (CommonValues.getInstance().userLogDetailList.size() > 0) {
				clearPreviousData();
			}
			// loadUserLogInfo(CommonValues.getInstance().userId, 1, formatter
			// .format(d).toString(), formatter.format(d).toString());
			loadTodaysLog();
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
			if (CommonValues.getInstance().userLogDetailList.size() > 0) {
				clearPreviousData();
			}
			/*
			 * esl.FilterType=2; esl.fromDate=formatter .format(d.getTime() - 24
			 * * 60 * 60 * 1000).toString(); esl.toDate=formatter
			 * .format(d.getTime() - 24 * 60 * 60 * 1000).toString();
			 */
			loadYesterdayLog();
			break;
		case R.id.bDashboard:
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			Home.mDrawerList.setItemChecked(ALLDEVICE_FRAGMENT, true);
			Home.navDrawerAdapter.setSelectedPosition(ALLDEVICE_FRAGMENT);
			currentFragment = ALLDEVICE_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(0)))
				stackIndex.push(String.valueOf(0));
			Intent homeIntent = new Intent(this, Home_.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
			break;
		case R.id.bCamera:
			// Toast.makeText(UserLogActivity.this,
			// "No Servilance System Available", Toast.LENGTH_SHORT).show();
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
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			currentFragment = ROOM_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
			Intent roomIntent = new Intent(this, RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);

			break;
		case R.id.bSearch:
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if (CommonValues.getInstance().userLogDetailList.size() > 0) {
				clearPreviousData();
			}
			if (validateEmptyDateInput()) {
				/*
				 * esl.FilterType=3; esl.fromDate=stserverDate;
				 * esl.toDate=lstserverDate;
				 */
				FilterType = 3;
				fromsDate = stserverDate;
				tosDate = lstserverDate;
				PageNumber = 1;
				// ChunkSize=40;

				loadUserLogInfo(FilterType, fromsDate, tosDate, PageNumber,
						ChunkSize);
			}
			break;
		default:
			break;
		}


	}

	/**
	 * 
	 */
	private void loadYesterdayLog() {
		FilterType = 2;
		fromsDate = formatter.format(d.getTime() - 24 * 60 * 60 * 1000)
				.toString();
		tosDate = formatter.format(d.getTime() - 24 * 60 * 60 * 1000)
				.toString();
		PageNumber = 1;
		// ChunkSize=40;

		loadUserLogInfo(FilterType, fromsDate, tosDate, PageNumber,
				ChunkSize);
	}

	/**
	 * 
	 */
	private void clearPreviousData() {
		userLogListView.setAdapter(null);
		uLogAdapter.clear();
		CommonValues.getInstance().userLogDetailList.clear();
	}

	/**
	 * 
	 */
	public boolean validateEmptyDateInput() {
		if (etDateTo.getText().toString().length() == 0) {
			CommonTask.ShowMessage(this, "End Date Can't be blank");
			return false;
		}
		return true;
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
		CommonTask.ShowMessage(this, "Please select StartDate First.");
	}

	public void showFirstDateError() {
		CommonTask.ShowMessage(this,
				"Start Date can't be Greater than Current Date.");
	}

	public void showLastDateError() {
		CommonTask
				.ShowMessage(this, "End Date can't  be less than Start date.");
	}

	public static void loadUserLogInfo(int FilterType, String fromsDate,
			String tosDate, int PageNumber, int ChunkSize) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_User_Activities;
		if (asyncGetUserLogInfo != null) {
			asyncGetUserLogInfo.cancel(true);
		}
		asyncGetUserLogInfo = new AsyncGetUserLogInfo((UserLogActivity) UserLogActivity.context, FilterType,
				fromsDate, tosDate, PageNumber, ChunkSize);
		asyncGetUserLogInfo.execute();

	}

	public boolean sendGetUserLogRequest(int FilterType, String fromsDate,
			String tosDate, int PageNumber, int ChunkSize) {

		// String postUserLogUrl = CommonURL.getInstance().GetCommonURL + "/"
		// + String.valueOf(userId) + "/activities";
		String postUserLogUrl = CommonURL.getInstance().RootUrl
				+ "myactivities";

		if (JsonParser.postUserLogRequest(postUserLogUrl, FilterType,
				fromsDate, tosDate, PageNumber, ChunkSize, shouldAppendList) != null
				&& JsonParser.postUserLogRequest(postUserLogUrl, FilterType,
						fromsDate, tosDate, PageNumber, ChunkSize,
						shouldAppendList) != "") {
			return true;
		} else {
			/*UserLogActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowAlertMessage(UserLogActivity.this, CommonValues.getInstance().alertObj );
					
				}
			});*/
			
			return false;
		}
	}

	/**
	 * 
	 */
	boolean shouldAppendList = false;

	public void setupUserLogAdapter() {
//		setConnectionNodeImage(actionBarMenu);
		if (CommonValues.getInstance().userLogDetailList != null) {
			if (CommonValues.getInstance().userLogDetailList.size() > 0) {
				tvEmptyLog.setVisibility(View.INVISIBLE);
				uLogAdapter = new UserLogAdapter(this, R.layout.log_list_item,
						CommonValues.getInstance().userLogDetailList);
				userLogListView.setAdapter(uLogAdapter);
				uLogAdapter.setTouchEnabled(false);
				userLogListView.setEnabled(true);

				userLogListView
						.setOnScrollListener(new AbsListView.OnScrollListener() {

							@Override
							public void onScrollStateChanged(AbsListView view,
									int scrollState) {
								int threshold = 1;
								int count = userLogListView.getCount();

								if (scrollState == SCROLL_STATE_IDLE) {

									if (userLogListView
											.getLastVisiblePosition() >= count
											- threshold) {
//										if (CommonValues.getInstance().shouldSendLogReq == true) {
//											CommonValues.getInstance().shouldSendLogReq = false;
//										}
										if (CommonValues.getInstance().userLogDetailList
												.size() % ChunkSize == 0
												&& CommonValues.getInstance().userLogDetailList
														.size() >= ChunkSize) {
										shouldAppendList = true;
										// Execute LoadMoreData AsyncTask
//										CommonValues.getInstance().currentAction = CommonIdentifier.Action_User_Activities;
										PageNumber = PageNumber += 1;

//										if (asyncGetUserLogInfo != null) {
//											asyncGetUserLogInfo.cancel(true);
//										}
//										asyncGetUserLogInfo = new AsyncGetUserLogInfo(
//												UserLogActivity.this,
//												FilterType, fromsDate, tosDate,
//												PageNumber, ChunkSize);
//										asyncGetUserLogInfo.execute();
										
										loadUserLogInfo(FilterType, fromsDate, tosDate, PageNumber,
												ChunkSize);
										}else{
											shouldAppendList = false;
										}
									}
								}
							}

							@Override
							public void onScroll(AbsListView view,
									int firstVisibleItem, int visibleItemCount,
									int totalItemCount) {
								// TODO Auto-generated method stub

							}
						});

			} else {
				tvEmptyLog.setVisibility(View.VISIBLE);
				tvEmptyLog.setText("Sorry ! No Log Available ");
			}
		} else {
//			CommonTask.ShowAlertMessage(this, CommonValues.getInstance().alertObj );
			 /*CommonTask
			 .ShowNetworkChangeConfirmation(
			 this,
			 "Network State has been changed.Please log in again to continue.",
			 showNetworkChangeEvent());*/
		}

	}

//	public void refreshAdapter() {
//		CommonValues.getInstance().shouldSendLogReq = true;
//		uLogAdapter.notifyDataSetChanged();
//	}

	@Override
	public void onBackPressed() {

		backState = UserLogSate.INITIAL_STATE;
		if (MainActionbarBase.stackIndex != null) {
			MainActionbarBase.stackIndex.removeAllElements();
		}
		currentFragment = ALLDEVICE_FRAGMENT;
		Intent homeIntent = new Intent(this, Home_.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(homeIntent);

	}

	@Override
	public void onResume() {
		this.setTitle("Activities");
//		setConnectionNodeImage(actionBarMenu,mainActionBarContext);
		// loadTodaysLog();
		fragmentPaused = false;
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		fragmentPaused = true;

	}

	public void startProgress() {
		userLogProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != userLogProgressBar && userLogProgressBar.isShown()) {

			userLogProgressBar.setVisibility(View.GONE);
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

}
