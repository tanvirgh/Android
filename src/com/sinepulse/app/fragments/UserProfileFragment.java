package com.sinepulse.app.fragments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncGetUserProfile;
import com.sinepulse.app.entities.UserProfile;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
/**
 * 
 * @author tanvir
 *
 */
@EFragment(R.layout.user_profile)
public class UserProfileFragment extends SherlockFragment {
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
	@ViewById(R.id.etAddress2)
	protected EditText etAddress2;
	@ViewById(R.id.etCity)
	protected EditText etCity;
	@ViewById(R.id.etTelephone)
	protected EditText etTelephone;
	@ViewById(R.id.etSex)
	protected EditText etSex;
	@ViewById(R.id.etSocialSecurity)
	protected EditText etSocialSecurity;
	@ViewById(R.id.userProfileProgressBar)
	public ProgressBar userProfileProgressBar;
	
	AsyncGetUserProfile asyncGetUserInfo = null;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	
	@AfterViews
	void afterViewLoaded(){
		loadUserInformation();
		
		
	}
	
	
	private void loadUserInformation() {
		if (asyncGetUserInfo != null) {
			asyncGetUserInfo.cancel(true);
	}
//		asyncGetUserInfo = new AsyncGetUserProfile(this,CommonValues.getInstance().userId);
//		asyncGetUserInfo.execute();
		
	}
	
public boolean sendGetUserProfileRequest(Integer userId) {
		
		String getUserProfileUrl=CommonURL.getInstance().GetCommonURL+"/"+String.valueOf(userId)+"/profile";

		if (JsonParser.getUserProfileRequest(getUserProfileUrl) != null) {
			return true;
		}
		return false;
	}


	/**
	 * 
	 */
	public void setUserInformation() {
		UserProfile userProfile = CommonValues.getInstance().profile;
		etFirstName.setText(userProfile.getFirstName() + " "
				+ userProfile.getMiddleName() + " "
				+ userProfile.getLastName());
		etEmail.setText(userProfile.getEmail());
		etAddress.setText(userProfile.getAddress().getAddress1());
		etAddress2.setText(userProfile.getAddress().getAddress2());
		etCity.setText(userProfile.getAddress().getCity().getCountry()
				+ "- " + userProfile.getAddress().getCity().getName());
//		etCity.setText("");
		etTelephone.setText(userProfile.getCellPhone());
		etSex.setText(userProfile.getSex());
		etSocialSecurity.setText(userProfile.getSocialSecurityNumber());
	}
	
	public boolean onBackPressed() {
			// CommonTask.CloseApplication(this);
			backState = UserProfileSate.INITIAL_STATE;
			return false;
		

	}
	
	@Override
	public void onResume() {
		this.setRetainInstance(true);
		getSherlockActivity().setTitle("User Profile");
//		fragmentPaused = false;
		super.onResume();
		
		

	}
	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
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
