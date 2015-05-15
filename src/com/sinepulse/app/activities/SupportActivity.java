/**
 * 
 */
package com.sinepulse.app.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnMenuVisibilityListener;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.adapters.AllTicketListAdapter;
import com.sinepulse.app.asynctasks.AsyncCreateTicket;
import com.sinepulse.app.asynctasks.AsyncGetAllTickets;
import com.sinepulse.app.asynctasks.AsyncGetTicketType;
import com.sinepulse.app.asynctasks.AsyncLoadTicketDetails;
import com.sinepulse.app.asynctasks.AsyncRefreshDashBoard;
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
@EActivity(R.layout.help_page)
public class SupportActivity extends MainActionbarBase implements
		OnClickListener, OnItemClickListener{

	public static ActionBar mSupportActionBar;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	@ViewById(R.id.bCreateTIcket)
	public Button bCreateTIcket;
	@ViewById(R.id.vfTicket)
	public static ViewFlipper vfTicket;
	@ViewById(R.id.spType)
	public Spinner spType;
	@ViewById(R.id.btSubmitTicket)
	public Button btSubmitTicket;
	@ViewById(R.id.etSubject)
	public EditText etSubject;
	@ViewById(R.id.etMessage)
	public EditText etMessage;
	@ViewById(R.id.pbTicket)
	public ProgressBar pbTicket;
	@ViewById(R.id.pbCreateTicket)
	public ProgressBar pbCreateTicket;
	@ViewById(R.id.pbSingleTicket)
	public ProgressBar pbSingleTicket;
	@ViewById(R.id.lvTicketList)
	public ListView ticketListView;
	@ViewById(R.id.etDate)
	public EditText etDate;
	@ViewById(R.id.etTktSubject)
	public EditText etTktSubject;
	@ViewById(R.id.etStatus)
	public EditText etStatus;
	@ViewById(R.id.etTktMessage)
	public EditText etTktMessage;
	@ViewById(R.id.etTicketNo)
	public EditText etTicketNo;
//	@ViewById(R.id.menu_refresh)
//	public Button refresh;
	InputMethodManager imm;

	private AllTicketListAdapter tAdapter;
	public static final int INITIAL_STATE = -1, VIEWSINGLETICKET_STATE = 0,
			CREATETICKET_STATE = 1;

	public enum TicketsState {
		INITIAL_STATE, // backState -1
		VIEWSINGLETICKET_STATE, CREATETICKET_STATE
	};

	public static TicketsState backState = TicketsState.INITIAL_STATE;
	AsyncGetTicketType asyncGetTicketType = null;
	AsyncCreateTicket asyncCreateTicket = null;
	AsyncGetAllTickets asyncGetAllTickets = null;
	AsyncLoadTicketDetails asyncLoadTicketDetails = null;

	// public static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.settinglistbackground);
		super.onCreate(savedInstanceState);
		// HelpActivity.context = this;
		createMenuBar();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}
	private boolean isReached = false;
	@AfterViews
	void afterViewLoaded() {
		backState = TicketsState.INITIAL_STATE;
		// btSubmitTicket.setEnabled(false);
		vfTicket.setDisplayedChild(0);
//		View refreshbtn=findViewById(R.id.menu_refresh);
		loadAllTicketList();
		etMessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
//				getTenCharPerLineString(etMessage.getText().toString().trim());
//				etMessage.setText(getTenCharPerLineString(etMessage.getText().toString().trim()));
				/* if(etMessage.getText().length()==35 && !isReached) {
					 etMessage.append("\n");
			            isReached = true;
				}
			        // if edittext has less than 10chars & boolean has changed, reset
				 else{
			        	isReached = false;
			        }*/
				}
				
		});

	}

	/**
	 * 
	 */
	public void loadAllTicketList() {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_All_Tickets;
		if (asyncGetAllTickets != null) {
			asyncGetAllTickets.cancel(true);
		}
		asyncGetAllTickets = new AsyncGetAllTickets(this,
				CommonValues.getInstance().userId);
		asyncGetAllTickets.execute();
//		View refreshbtn=findViewById(R.id.menu_refresh);
		
	}
	public String getTenCharPerLineString(String text){

	    String tenCharPerLineString = "";
	    while (text.length() > 10) {

	        String buffer = text.substring(0, 10);
	        tenCharPerLineString = tenCharPerLineString + buffer + "/n";
	        text = text.substring(10);
	    }

	    tenCharPerLineString = tenCharPerLineString + text.substring(0);
	    return tenCharPerLineString;
	}
	

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setTitle("Support");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		if (item.getItemId() == R.id.menu_refresh && vfTicket.getDisplayedChild()==0) {
			loadAllTicketList();
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
		setActionBarMenuVisibility(true);
		return prepared;
	}

	@Override
	@Click({ R.id.bCamera, R.id.bDashboard, R.id.bRoom, R.id.bCreateTIcket,
			R.id.btSubmitTicket })
	public void onClick(View v) {
		
		switch (v.getId()) {
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
			// spType.setEnabled(false);
			spType.setAdapter(null);
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
		case R.id.btSubmitTicket:
			if (validateSubmitTicketWindow()) {
				String Subject = etSubject.getText().toString().trim();
				
				String Details = etMessage.getText().toString().trim();
				
				
				int TicketTypeId = CommonValues.getInstance().ticketTypeList
						.get(spType.getSelectedItemPosition()).Id;
				if (asyncCreateTicket != null) {
					asyncCreateTicket.cancel(true);
				}
				asyncCreateTicket = new AsyncCreateTicket(this,
						CommonValues.getInstance().userId, Subject, Details,
						TicketTypeId);
				asyncCreateTicket.execute();
			}

			break;
		case R.id.bCreateTIcket:
			setActionBarMenuVisibility(false);
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			etSubject.requestFocus();
			// CommonTask.showSoftKeybord(etSubject);
			getSupportActionBar().setTitle("Create Ticket");
			vfTicket.setInAnimation(CommonTask.inFromRightAnimation());
			vfTicket.setOutAnimation(CommonTask.outToLeftAnimation());
			vfTicket.setDisplayedChild(1);
			backState = TicketsState.CREATETICKET_STATE;
			if (asyncGetTicketType != null) {
				asyncGetTicketType.cancel(true);
			}
			asyncGetTicketType = new AsyncGetTicketType(this,
					CommonValues.getInstance().userId);
			asyncGetTicketType.execute();

			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		spType.setEnabled(true);
		mSupportActionBar.setTitle("Support");
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onBackPressed() {
		vfTicket.setInAnimation(CommonTask.inFromLeftAnimation());
		vfTicket.setOutAnimation(CommonTask.outToRightAnimation());
		setActionBarMenuVisibility(true);
		switch (backState) {
		case INITIAL_STATE:
			if (MainActionbarBase.stackIndex != null) {
				MainActionbarBase.stackIndex.removeAllElements();
			}
			Intent homeIntent = new Intent(this, Home_.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
			break;
		case VIEWSINGLETICKET_STATE:
			mSupportActionBar.setTitle("Support");
			vfTicket.setDisplayedChild(0);
			backState = TicketsState.INITIAL_STATE;
			loadAllTicketList();
			break;
		case CREATETICKET_STATE:
			mSupportActionBar.setTitle("Support");
			vfTicket.setDisplayedChild(0);
			backState = TicketsState.INITIAL_STATE;
			loadAllTicketList();
			break;

		default:
			break;
		}

	}

	public boolean sendGetTicketTypeRequest(int userId) {
		String getTicketTypeUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/tickettypes";

		if (JsonParser.getTicketTypeRequest(getTicketTypeUrl) != null) {
			return true;
		}
		return false;

	}

	public void setTIcketTypeResponseData() {
		String[] ticketTypeValues = getTicketTypeValues();
		ArrayAdapter<String> ticketTypeAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, ticketTypeValues);
		ticketTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(ticketTypeAdapter);
		// spType.setSelection(presetItemPosition + 1);

	}

	private String[] getTicketTypeValues() {
		// TODO Auto-generated method stub
		if (CommonValues.getInstance().ticketTypeList != null
				&& CommonValues.getInstance().ticketTypeList.size() > 0) {
			String[] ticketTypeArray = new String[CommonValues.getInstance().ticketTypeList
					.size()];
			for (int i = 0; i < CommonValues.getInstance().ticketTypeList
					.size(); i++) {
				ticketTypeArray[i] = CommonValues.getInstance().ticketTypeList
						.get(i).getTypeName();
			}
			// shouldSetPreset = false;
			return ticketTypeArray;
		} else {
			// shouldSetPreset = true;
			CommonTask.ShowMessage(this, "Network Problem.Please Retry.");
			return null;
		}
	}

	public boolean sendCreateTicketRequest(int userId, String Subject,
			String Details, Integer TicketTypeId) {
		String createTicketUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/newTicket";

		if (JsonParser.postCreateTicketRequest(createTicketUrl, Subject,
				Details, TicketTypeId) != null) {
			return true;
		}
		return false;

	}

	public void startProgress() {
		pbTicket.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		if (null != pbTicket && pbTicket.isShown()) {

			pbTicket.setVisibility(View.GONE);
		}
	}

	public void startCreateTktProgress() {
		pbCreateTicket.setVisibility(View.VISIBLE);
	}

	public void stopCreateTktProgress() {
		if (null != pbCreateTicket && pbCreateTicket.isShown()) {

			pbCreateTicket.setVisibility(View.GONE);
		}
	}

	public void startSingleTktProgress() {
		pbSingleTicket.setVisibility(View.VISIBLE);
	}

	public void stopSingleTktProgress() {
		if (null != pbSingleTicket && pbSingleTicket.isShown()) {

			pbSingleTicket.setVisibility(View.GONE);
		}
	}

	public boolean sendGetAllTicketRequest(int userId) {
		String getAllTicketUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ String.valueOf(userId) + "/tickets";

		if (JsonParser.getAllTicketRequest(getAllTicketUrl) != null) {
			return true;
		}
		return false;

	}

	public void setAllTicketListViewAdapter() {
		if (CommonValues.getInstance().allTicketList != null
				&& CommonValues.getInstance().allTicketList.size() > 0) {
			tAdapter = new AllTicketListAdapter(this,
					R.layout.ticket_list_item,
					CommonValues.getInstance().allTicketList);
			ticketListView.setAdapter(tAdapter);
			tAdapter.setTouchEnabled(false);
			ticketListView.setEnabled(true);
			ticketListView.setOnItemClickListener(this);
		} else {
			CommonTask.ShowMessage(this, "No tickets created yet.");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setActionBarMenuVisibility(false);
		vfTicket.setInAnimation(CommonTask.inFromRightAnimation());
		vfTicket.setOutAnimation(CommonTask.outToLeftAnimation());
		getSupportActionBar().setTitle("Ticket Detials");
		backState = TicketsState.VIEWSINGLETICKET_STATE;
		vfTicket.setDisplayedChild(2);
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_SingleTicket;
		if (asyncLoadTicketDetails != null) {
			asyncLoadTicketDetails.cancel(true);
		}
		asyncLoadTicketDetails = new AsyncLoadTicketDetails(this,
				CommonValues.getInstance().userId,
				CommonValues.getInstance().allTicketList.get(position).getId());
		asyncLoadTicketDetails.execute();

	}

	public void resetCreateTicketWindow() {
		etSubject.setText("");
		etMessage.setText("");

	}

	public boolean loadTicketDetails(int userId, int ticketId) {
		String getViewTicketUrl = CommonURL.getInstance().GetCommonURL + "/"
				+ userId + "/tickets" + "/" + ticketId;

		if (JsonParser.getViewTicketRequest(getViewTicketUrl) != null) {
			return true;
		}
		return false;
	}

	public void setSingleTicketData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Long timeInMillis = Long
				.valueOf(CommonValues.getInstance().singleTicket
						.getSubmissionDate().getTime());
		Date LoggedAt = new Date(timeInMillis);
		etDate.setText(formatter.format(LoggedAt));
		etTktSubject.setText(CommonValues.getInstance().singleTicket
				.getSubject());
		etStatus.setText(CommonValues.getInstance().singleTicket.getStatus());
		etTktMessage.setText(CommonValues.getInstance().singleTicket
				.getDetails());
		etTicketNo
				.setText(String.valueOf(CommonValues.getInstance().singleTicket
						.getId()));

	}

	private boolean validateSubmitTicketWindow() {

		if (etSubject.getText().toString().trim().equals("")) {
			etSubject.setError("Please provide Ticket Subject.");
			return false;
		} else if (etMessage.getText().toString().trim().equals("")) {
			etMessage.setError("Please provide Ticket Details.");
			return false;
		}

		else {
			etSubject.setError(null);
			etMessage.setError(null);
			return true;
		}
		// TODO Auto-generated method stub

	}

	public void setActionBarMenuVisibility(boolean visibility) {
		if (MainActionbarBase.actionBarMenu != null) {
			int size = MainActionbarBase.actionBarMenu.size();
			for (int i = 0; i < size; i++) {
				MainActionbarBase.actionBarMenu.getItem(i).setVisible(
						visibility);
			}
		}
	}

}
