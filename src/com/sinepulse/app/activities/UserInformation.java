package com.sinepulse.app.activities;

import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.entities.UserProfile;
import com.sinepulse.app.utils.CommonValues;

public class UserInformation extends SherlockActivity {

	public static ActionBar mSupportActionBar;
	public EditText etFirstName;
	public EditText etEmail;
	public EditText etAddress;
	public EditText etAddress2;
	public EditText etCity;
	public EditText etTelephone;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.settinglistbackground);
		setContentView(R.layout.user_profile);
		super.onCreate(savedInstanceState);
		initializeViewControls();
		createMenuBar();
		
		setUserInformation();
		
	}

	/**
	 * 
	 */
	private void setUserInformation() {
		UserProfile userProfile = CommonValues.getInstance().profile;
		etFirstName.setText(userProfile.getFirstName() + " "
				+ userProfile.getMiddleName() + " "
				+ userProfile.getLastName());
		etEmail.setText(userProfile.getEmail());
		etAddress.setText(userProfile.getAddress().getAddress1());
		etAddress2.setText(userProfile.getAddress().getAddress2());
		etCity.setText(userProfile.getAddress().getCity().getCountry()
				+ "- " + userProfile.getAddress().getCity().getName());
		etTelephone.setText(userProfile.getCellPhone());
	}

	private void initializeViewControls() {
		etFirstName=(EditText) findViewById(R.id.etFirstName);
		etEmail=(EditText) findViewById(R.id.etEmail);
		etAddress=(EditText) findViewById(R.id.etAddress);
		etAddress2=(EditText) findViewById(R.id.etAddress2);
		etCity=(EditText) findViewById(R.id.etCity);
		etTelephone=(EditText) findViewById(R.id.etTelephone);
		
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
//			MainActionbarBase.stackIndex.removeAllElements();
//			Intent i = CommonValues.getInstance().homeIntent;
//			startActivity(i);

		}
		return true;
	}

	@Override
	public void onResume() {
		mSupportActionBar.setTitle("User Profile");
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}
}
