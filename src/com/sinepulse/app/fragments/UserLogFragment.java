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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.activities.Home_;
import com.sinepulse.app.activities.RoomManager_;
import com.sinepulse.app.activities.VideoActivity_;
import com.sinepulse.app.adapters.UserLogAdapter;
import com.sinepulse.app.asynctasks.AsyncGetUserLogInfo;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;

/**
 * 
 * @author tanvir
 * 
 */
@EFragment(R.layout.user_log)
public class UserLogFragment extends SherlockFragment implements
		OnClickListener {
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
	String fromDate=null;
	String toDate=null;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getActivity().setContentView(R.layout.catalogue2);
		getSherlockActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// context = activity.getApplicationContext();
	// }

	@AfterViews
	void afterViewLoaded() {
		
		// setUserInformation();
		loadTodaysLog();
		
		bSearch.setVisibility(View.INVISIBLE);
		etDateFrom.setOnClickListener(this);
		etDateTo.setOnClickListener(this);
		
		tvYesterday.setOnClickListener(this);
		tvToday.setOnClickListener(this);
			
	}

	/**
	 * 
	 */
	public void loadTodaysLog() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		 Date d=new Date();
		// etDateFrom.setText(formatter.format(d).toString());
		tvToday.setTextColor(Color.parseColor("#2C5197"));
		tvYesterday.setTextColor(Color.parseColor("#bdbdbd"));
		loadUserLogInfo(CommonValues.getInstance().userId,1,formatter.format(d).toString(),formatter.format(d).toString());
	}

	@Override
	@Click({ R.id.bDeliverydate, R.id.etDateFrom, R.id.etDateTo,R.id.tvToday,R.id.tvYesterday, R.id.bCamera,R.id.bDashboard,R.id.bRoom})
	public void onClick(View v) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		 Date d=new Date();
		switch (v.getId()) {
		// case R.id.bDeliverydate:
		// Search logic will go here
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
				uLogAdapter.clear();
		        }
			loadUserLogInfo(CommonValues.getInstance().userId,1,formatter.format(d).toString(),formatter.format(d).toString());
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
				uLogAdapter.clear();
		        }
				loadUserLogInfo(CommonValues.getInstance().userId,2,formatter.format(d).toString(),formatter.format(d).toString());
			break;
		case R.id.bDashboard:
//			hom
//			Home.fragmentBackStack.clear();
			((MainActionbarBase) getActivity()).displayFragment(0);
			break;
		case R.id.bCamera:
			Intent cameraIntent = new Intent(getActivity(), VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);
			
			break;
		case R.id.bRoom:
			Intent roomIntent = new Intent(getActivity(), RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);
			
			break;
		default:
			break;
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
				
			}
		});
		
		bSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CommonValues.getInstance().deviceLogDetailList.size() > 0){
					deviceLogListView.setAdapter(null);
					uLogAdapter.clear();
			        }
				loadUserLogInfo(CommonValues.getInstance().userId,3,fromDate,toDate);
				
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
		

	}

	private void loadUserLogInfo(int userId,int FilterType, String fromDate,String toDate) {
		if (asyncGetUserLogInfo != null) {
			asyncGetUserLogInfo.cancel(true);
		}
//		asyncGetUserLogInfo = new AsyncGetUserLogInfo(this,
//				CommonValues.getInstance().userId,FilterType,fromDate,toDate);
//		asyncGetUserLogInfo.execute();

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
		if (CommonValues.getInstance().deviceLogDetailList!=null &&CommonValues.getInstance().deviceLogDetailList.size() > 0) {
			tvEmptyLog.setVisibility(View.INVISIBLE);
			uLogAdapter = new UserLogAdapter(getActivity(),
					R.layout.log_list_item,
					CommonValues.getInstance().deviceLogDetailList);
			deviceLogListView.setAdapter(uLogAdapter);
			uLogAdapter.setTouchEnabled(false);
			deviceLogListView.setEnabled(true);
		} else {
			tvEmptyLog.setVisibility(View.VISIBLE);
			tvEmptyLog.setText("Sorry ! No Log Available ");
		}
		
		

	}

	public boolean onBackPressed() {
//		if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
//			CommonValues.getInstance().deviceLogDetailList.clear();
//			deviceLogListView.setAdapter(null);
//			uLogAdapter.clear();
//		}
		etDateFrom.setText("");
		etDateTo.setText("");
		backState = UserLogSate.INITIAL_STATE;
		return false;
		

	}

	@Override
	public void onResume() {
		getSherlockActivity().setTitle("User Activity");
		/*if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
			deviceLogListView.setAdapter(null);
			uLogAdapter.clear();
		}*/
		etDateFrom.setText("");
		etDateTo.setText("");
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
		deviceLogProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != deviceLogProgressBar && deviceLogProgressBar.isShown()) {

			deviceLogProgressBar.setVisibility(View.GONE);
		}
	}

}
