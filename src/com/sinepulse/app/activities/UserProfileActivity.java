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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncGetUserProfile;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.UserProfile;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * this class will show the user profile related information through
 * asynchronous call to server.this is a view only page.
 * @author tanvir.ahmed
 *
 */
@EActivity(R.layout.user_profile)
public class UserProfileActivity extends MainActionbarBase implements OnClickListener{
	
	public static final int INITIAL_STATE = -1;
	public enum UserProfileSate {
		INITIAL_STATE // backState -1		

	};
//	boolean fragmentPaused = false;

	public static UserProfileSate backState = UserProfileSate.INITIAL_STATE;
	
	@ViewById(R.id.etFirstName)
	protected EditText etFirstName;
	@ViewById(R.id.etEmail)
	protected EditText etEmail;
	@ViewById(R.id.etAddress)
	protected EditText etAddress;
	@ViewById(R.id.etUserName)
	protected EditText etUserName;
	@ViewById(R.id.etCity)
	protected EditText etCity;
	@ViewById(R.id.etTelephone)
	protected EditText etTelephone;
	@ViewById(R.id.etSex)
	protected EditText etSex;
	@ViewById(R.id.etdob)
	protected EditText etdob;
	@ViewById(R.id.userProfileProgressBar)
	public ProgressBar userProfileProgressBar;
	
	static AsyncGetUserProfile asyncGetUserInfo = null;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	private Menu actionBarMenu;
	public static Context context;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createMenuBar();
		UserProfileActivity_.context=this;
        mainActionBarContext=UserProfileActivity_.context;
		
	}
	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setTitle(" Profile");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		this.actionBarMenu=menu;
		boolean prepared = super.onPrepareOptionsMenu(menu);
		setConnectionNodeImage(actionBarMenu,this);
		return prepared;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (item.getItemId() == android.R.id.home) {
			 onBackPressed();
		}
		if (item.getItemId() == R.id.menu_refresh) {
			loadUserInformation();
		}
		if (item.getItemId() == R.id.menu_conn_indicatior) {
			if (status.equals("Mobiledata enabled") && CommonValues.getInstance().connectionMode.equals("Internet") ) {
				CommonTask.ShowMessage(this, "Local mode is not accessible in GSM network.Please try with WiFi.");
			}else{
			CommonTask
			.ShowNetworkChangeConfirmation(
					UserProfileActivity.this,
					"Do you Really want to change mode?.",
					showNetworkChangeEvent());
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@AfterViews
	void afterViewLoaded(){
		loadUserInformation();
	}
	
	
	public static void loadUserInformation() {
		CommonValues.getInstance().currentAction=CommonIdentifier.Action_Profile;
		if (asyncGetUserInfo != null) {
			asyncGetUserInfo.cancel(true);
	}
		asyncGetUserInfo = new AsyncGetUserProfile((UserProfileActivity) UserProfileActivity.context,CommonValues.getInstance().userId);
		asyncGetUserInfo.execute();
		
	}
	
public boolean sendGetUserProfileRequest(Integer userId) {
		
//		String getUserProfileUrl=CommonURL.getInstance().GetCommonURL+"/"+String.valueOf(userId)+"/profile";
	   String getUserProfileUrl=CommonURL.getInstance().RootUrl+"profile";

		if (JsonParser.getUserProfileRequest(getUserProfileUrl) != null && JsonParser.getUserProfileRequest(getUserProfileUrl) != "") {
			return true;
		}else{
			/* UserProfileActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						CommonTask.ShowAlertMessage(UserProfileActivity.this, CommonValues.getInstance().alertObj );
						
					}
				});*/
		return false;
		}
	}


	/**
	 * 
	 */
	public void setUserInformation() {
//		setConnectionNodeImage(actionBarMenu);
		if(CommonValues.getInstance().profile!=null){
		UserProfile userProfile = CommonValues.getInstance().profile;
		String name="";  
		if(userProfile.getFirstName()!=null ){
			name= userProfile.getFirstName();
			
		} if(userProfile.getMiddleName()!=null){
			name=name+" "+userProfile.getMiddleName();
			
		} if(userProfile.getLastName()!=null){
			name=name+" "+userProfile.getLastName();
			
		}
		if(name.length()>0){
		etFirstName.setText(name);
		}else{
			etFirstName.setText("");
		}
		
		
		etUserName.setText(userProfile.getUserName());
		etEmail.setText(userProfile.getEmail());
		etSex.setText(userProfile.getSex());
		if(userProfile.getDateOfBirth()!=null){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Long timeInMillis = Long.valueOf(userProfile.getDateOfBirth().getTime());
		Date dob = new Date(timeInMillis);
		etdob.setText(formatter.format(dob));
		}else{
			etdob.setText("");
		}
		
		if(userProfile.getAddress()!=null &&userProfile.getAddress().getAddress1()!=null && userProfile.getAddress().getAddress2()!=null ){
		etAddress.setText(userProfile.getAddress().getAddress1());
//		etAddress2.setText(userProfile.getAddress().getAddress2());
		}else{
			etAddress.setText("Not applicable");
//			etAddress2.setText("");
		}
		
		
//		etCity.setText(userProfile.getAddress().getCity().getCountry()
//				+ "- " + userProfile.getAddress().getCity().getName());
		etTelephone.setText(userProfile.getCellPhone());
		
		}else{
//			CommonTask.ShowMessage(this, "Error Fetching Data from Server");
//			CommonTask
//			.ShowNetworkChangeConfirmation(
//					this,
//					"Network State/Configuration Settings has been changed.Please log in again to continue.",
//					showNetworkChangeEvent());
      /*  UserProfileActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CommonTask.ShowAlertMessage(UserProfileActivity.this, CommonValues.getInstance().alertObj );
					
				}
			});*/
		}
	}
	
	@Override
	public void onBackPressed() {
			backState = UserProfileSate.INITIAL_STATE;
			if(MainActionbarBase.stackIndex!=null){
			MainActionbarBase.stackIndex.removeAllElements();
			}
			currentFragment = ALLDEVICE_FRAGMENT;
			Intent homeIntent = new Intent(this, Home_.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
		

	}
	
	@Override
	@Click({ R.id.bDeliverydate, R.id.etDateFrom, R.id.etDateTo,R.id.tvToday,R.id.tvYesterday, R.id.bCamera,R.id.bDashboard,R.id.bRoom})
	public void onClick(View v) {
		if(MainActionbarBase.stackIndex!=null){
		MainActionbarBase.stackIndex.removeAllElements();
		}
		switch (v.getId()) {
		case R.id.bDashboard:
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
//			Toast.makeText(UserProfileActivity.this, "No Servilance System Available", Toast.LENGTH_SHORT).show();
			currentFragment=CAMERA_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(6)))
				stackIndex.push(String.valueOf(6));
			Intent cameraIntent = new Intent(this, VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);
			
			break;
		case R.id.bRoom:
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
	}

	
	@Override
	public void onResume() {
		this.setTitle("Profile");
//		fragmentPaused = false;
		super.onResume();
		
		

	}
	@Override
	public void onPause() {
		super.onPause();
//		fragmentPaused = true;
		
	}
	
	public void startProgress() {
		userProfileProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress() {

		userProfileProgressBar.setVisibility(View.INVISIBLE);
	}
	


}
