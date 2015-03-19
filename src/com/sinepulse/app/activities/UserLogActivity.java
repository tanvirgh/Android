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
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

/**
 * @author tanvir.ahmed
 *
 */
@EActivity(R.layout.user_log)
public class UserLogActivity extends MainActionbarBase implements OnClickListener{
	
	public static final int INITIAL_STATE = -1;

	public enum UserLogSate {
		INITIAL_STATE // backState -1

	};

	boolean fragmentPaused = false;

	public static UserLogSate backState = UserLogSate.INITIAL_STATE;
	AsyncGetUserLogInfo asyncGetUserLogInfo = null;
	private UserLogAdapter uLogAdapter;
	@ViewById(R.id.lvLogList)
	public ListView deviceLogListView;
	@ViewById(R.id.deviceLogProgressBar)
	public ProgressBar deviceLogProgressBar;
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
	String fromDate="";
	String toDate="";
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	Date d=new Date();
	int year;
	int month;
	int day;
	private DatePickerDialog fromDatePickerDialog = null;
	private DatePickerDialog toDatePickerDialog = null;
	private SimpleDateFormat dateFormatter;
	Date tobesetFromDate = new Date();
	Date tobesetToDate=new Date();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getActivity().setContentView(R.layout.catalogue2);
		this.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		createMenuBar();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		setDateTimeField();
	}
	
	

	@AfterViews
	void afterViewLoaded() {
		bSearch.setVisibility(View.INVISIBLE);
		loadTodaysLog();
	  /*  fromDate=formatter.format(d).toString();
	    toDate=formatter.format(d).toString();*/
	    fromDate = dateFormatter.format(d).toString();
		toDate = dateFormatter.format(d).toString();
		etDateFrom.setText(fromDate);
		 etDateTo.setText(toDate);
		
//		bSearch.setVisibility(View.INVISIBLE);
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
		mSupportActionBar.setTitle("User Activity");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			 onBackPressed();
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
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//		 Date d=new Date();
		// etDateFrom.setText(formatter.format(d).toString());
		tvToday.setTextColor(Color.parseColor("#2C5197"));
		tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
		loadUserLogInfo(CommonValues.getInstance().userId,1,formatter.format(d).toString(),formatter.format(d).toString());
	}
	
	
	@Override
	@Click({ R.id.bDeliverydate, R.id.etDateFrom, R.id.etDateTo,R.id.tvToday,R.id.tvYesterday, R.id.bCamera,R.id.bDashboard,R.id.bRoom})
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.bDeliverydate:
		// Search logic will go here
		case R.id.etDateFrom:
			if(fromDatePickerDialog!=null)
				fromDatePickerDialog.show();
fromDatePickerDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					
					fromDatePickerDialog.getDatePicker().getCalendarView().setDate(tobesetFromDate.getTime());
				}
			});
			bSearch.setVisibility(View.VISIBLE);
			break;
		case R.id.etDateTo:
			if(toDatePickerDialog!=null)
				toDatePickerDialog.show();
toDatePickerDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					
					toDatePickerDialog.getDatePicker().getCalendarView().setDate(tobesetToDate.getTime());
				}
			});
//			bSearch.setVisibility(View.VISIBLE);
//			showCalendar(v);
			break;
		case R.id.tvToday:
			  String tDelims = "T"; 
			  String[] tTokens =dateFormatter.format(d).toString().split(tDelims);
			  etDateFrom.setText(tTokens[0]); 
			  etDateTo.setText(tTokens[0]);
//			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
			tvToday.setTextColor(Color.parseColor("#2C5197"));
			if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
				deviceLogListView.setAdapter(null);
				uLogAdapter.clear();
		        }
			loadUserLogInfo(CommonValues.getInstance().userId,1,formatter.format(d).toString(),formatter.format(d).toString());
			break;
		case R.id.tvYesterday:
			if(tvEmptyLog.isShown()){
				tvEmptyLog.setVisibility(View.GONE);
			}
			 String yDelims = "T"; 
			  String[] yTokens = dateFormatter .format(d.getTime() - 24 * 60 * 60 * 1000).toString()
			  .split(yDelims); 
			  etDateFrom.setText(yTokens[0]);
			  etDateTo.setText(yTokens[0]);
//			bSearch.setVisibility(View.INVISIBLE);
			tvYesterday.setTextColor(Color.parseColor("#2C5197"));
			tvToday.setTextColor(Color.parseColor("#bdbdbd"));
			if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
				deviceLogListView.setAdapter(null);
				uLogAdapter.clear();
		        }
				loadUserLogInfo(CommonValues.getInstance().userId,2,formatter.format(d.getTime()-24*60*60*1000).toString(),formatter.format(d.getTime()-24*60*60*1000).toString());
			break;
		case R.id.bDashboard:
			MainActionbarBase.stackIndex.removeAllElements();
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
			MainActionbarBase.stackIndex.removeAllElements();
			currentFragment=CAMERA_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(6)))
				stackIndex.push(String.valueOf(6));
			Intent cameraIntent = new Intent(this, VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);
			
			break;
		case R.id.bRoom:
			MainActionbarBase.stackIndex.removeAllElements();
			currentFragment=ROOM_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
			Intent roomIntent = new Intent(this, RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);
			
			break;
		default:
			break;
		}
	


	
	bSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
					deviceLogListView.setAdapter(null);
					uLogAdapter.clear();
			        }
				loadUserLogInfo(CommonValues.getInstance().userId,3,stserverDate, lstserverDate);
				
			}
		});
		

	}
	
	private boolean validateLastDateInput() {
		try {
			Date firstDate = dateFormatter.parse(stDate);
			Date lastDate = dateFormatter.parse(lstDate);
			Date CurrentDate = d;
			if (lastDate.after(CurrentDate)) {
				CommonTask.ShowMessage(this,
						"To date can't be greater than current date");
				etDateTo.setText("");
				return false;
			} else if (lastDate.before(firstDate)) {
				CommonTask.ShowMessage(this,
						"To date can't be less than From date");
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
		CommonTask.ShowMessage(this, "Please select FromDate First.");
	}
	public void showFirstDateError() {
		CommonTask.ShowMessage(this, "From Date should not be Greater than current date.");
	}
	public void showLastDateError() {
		CommonTask.ShowMessage(this, "To Date should not  be less than From date.");
	}

	private void loadUserLogInfo(int userId,int FilterType, String fromDate,String toDate) {
		CommonValues.getInstance().currentAction=CommonIdentifier.Action_User_Activities;
		if (asyncGetUserLogInfo != null) {
			asyncGetUserLogInfo.cancel(true);
		}
		asyncGetUserLogInfo = new AsyncGetUserLogInfo(this,
				CommonValues.getInstance().userId,FilterType,fromDate,toDate);
		asyncGetUserLogInfo.execute();

	}

	public boolean sendGetUserLogRequest(Integer userId,int FilterType,String fromDate,String toDate) {

		String postUserLogUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/activities";

		if (JsonParser.postUserLogRequest(postUserLogUrl, FilterType, fromDate, toDate) != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void setupUserLogAdapter() {
		if(CommonValues.getInstance().deviceLogDetailList!=null){
		if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
			tvEmptyLog.setVisibility(View.INVISIBLE);
			uLogAdapter = new UserLogAdapter(this,
					R.layout.log_list_item,
					CommonValues.getInstance().deviceLogDetailList);
			deviceLogListView.setAdapter(uLogAdapter);
			uLogAdapter.setTouchEnabled(false);
			deviceLogListView.setEnabled(true);
		} else {
			tvEmptyLog.setVisibility(View.VISIBLE);
			tvEmptyLog.setText("Sorry ! No Log Available ");
		}
		}else{
			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
		}
		

	}

	@Override
	public void onBackPressed() {
//		if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
//			CommonValues.getInstance().deviceLogDetailList.clear();
//			deviceLogListView.setAdapter(null);
//			uLogAdapter.clear();
//		}
//		etDateFrom.setText("");
//		etDateTo.setText("");
		backState = UserLogSate.INITIAL_STATE;
		MainActionbarBase.stackIndex.removeAllElements();
		currentFragment = ALLDEVICE_FRAGMENT;
		Intent homeIntent = new Intent(this, Home_.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(homeIntent);
		

	}

	@Override
	public void onResume() {
		this.setTitle("User Activity");
		/*if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
			deviceLogListView.setAdapter(null);
			uLogAdapter.clear();
		}*/
//		etDateFrom.setText("");
//		etDateTo.setText("");
		fragmentPaused = false;
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		fragmentPaused = true;

	}

	public void startProgress() {
		deviceLogProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != deviceLogProgressBar && deviceLogProgressBar.isShown()) {

			deviceLogProgressBar.setVisibility(View.GONE);
		}
	}
	
	String stDate="";
	String lstDate="";
	String stserverDate=formatter.format(d).toString();
	String lstserverDate=formatter.format(d).toString();	
	Calendar newDate = Calendar.getInstance();
	
	
	
	private void setDateTimeField() {
			Calendar newCalendar = Calendar.getInstance();
			fromDatePickerDialog = new DatePickerDialog(this,
					new OnDateSetListener() {

						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							newDate.set(year, monthOfYear, dayOfMonth);
							
							try {
						   Date fromDate= dateFormatter.parse(dateFormatter.format(newDate.getTime()));
						   stDate=dateFormatter.format(newDate.getTime());
						   stserverDate=formatter.format(newDate.getTime());
							Date CurrentDate = new Date();
							if(fromDate.after(CurrentDate)){
								showFirstDateError();
							}else{
								etDateFrom.setText(dateFormatter.format(newDate.getTime()));
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
							try {
								tobesetFromDate = dateFormatter.parse(etDateFrom.getText().toString());
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
							 lstDate=dateFormatter.format(newDate.getTime());
							 lstserverDate=formatter.format(newDate.getTime());
							 if (validateLastDateInput()) {
							etDateTo.setText(dateFormatter.format(newDate
									.getTime()));
							
							 }
							 try {
								 tobesetToDate = dateFormatter.parse(etDateTo.getText().toString());
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
