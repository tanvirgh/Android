/**
 * 
 */
package com.sinepulse.app.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncPostChangePassRequest;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * This class is used to change users password through a asynchronous call to server.
 * @author tanvir.ahmed
 *
 */
@EActivity(R.layout.change_password)
public class ChangePasswordActivity extends MainActionbarBase implements OnClickListener{
//	Menu connMenu=null;
	public static Context context;
	public static final int INITIAL_STATE = -1;
	public enum AboutState {
		INITIAL_STATE, // backState -1

	};

	public static AboutState backState = AboutState.INITIAL_STATE;

	InputMethodManager imm;
	@ViewById(R.id.etOldPass)
	public  EditText etOldPass;
	@ViewById(R.id.etNewPassword)
	public  EditText etNewPassword;
	@ViewById(R.id.etConfirmPassword)
	public  EditText etConfirmPassword;
	@ViewById(R.id.bSavePassword)
	public Button bSavePassword;
	@ViewById(R.id.pbChangePass)
	public ProgressBar pbChangePass;
	AsyncPostChangePassRequest asyncPostChangePassRequest = null;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;

	/**
	 * Automatically call once when class created initialize all control
	 * variable and load family using LoadFamilyContent Asynchronously use
	 * thread for performing search
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChangePasswordActivity.context=this;
        mainActionBarContext=ChangePasswordActivity.context;
//        connMenu=actionBarMenu;
		this.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		createMenuBar() ;
	}

	@AfterViews
	void afterViewsLoaded() {
//		bSavePassword.setEnabled(false);
		/*imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		etOldPass.requestFocus();
		CommonTask.showSoftKeybord(etOldPass);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);*/
		etOldPass.requestFocus();
//		CommonTask.showSoftKeybord(etOldPass);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}
	
	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));

		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setTitle("Change Password");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
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
					ChangePasswordActivity.this,
					"Do you Really want to change mode?.",
					showNetworkChangeEvent());
			}
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
//		connMenu=menu;
		setConnectionNodeImage(CommonValues.getInstance().globalMenu,this);
		hideRefreshMenu(menu);
		
		return prepared;
	}


	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen
	 */
	@Override
	public void onBackPressed() {

		backState = AboutState.INITIAL_STATE;
		if(MainActionbarBase.stackIndex!=null){
		MainActionbarBase.stackIndex.removeAllElements();
		}
		currentFragment = ALLDEVICE_FRAGMENT;
		Intent homeIntent = new Intent(this, Home_.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(homeIntent);
	}

	@Override
	public void onResume() {
		this.setTitle("Change Password");
		fragmentPaused = false;
//		setConnectionNodeImage(actionBarMenu,mainActionBarContext);
		super.onResume();
		backState = AboutState.INITIAL_STATE;
	}

	boolean fragmentPaused = false;

	@Override
	public void onPause() {
		super.onPause();
		fragmentPaused = true;
	}


	@Override
	@Click({R.id.bSavePassword,R.id.bCamera,R.id.bDashboard,R.id.bRoom})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bSavePassword:
		if (validateChangePassWindow()) {
			String currentPass = etOldPass.getText().toString();
			String newPass = etNewPassword.getText().toString();
			if (etNewPassword.getText().toString()
					.equals(etConfirmPassword.getText().toString())) {
				etConfirmPassword.setError(null);
//				Toast.makeText(this,
//						"New Password and Confirm password matched",
//						Toast.LENGTH_SHORT).show();
				if (asyncPostChangePassRequest != null) {
					asyncPostChangePassRequest.cancel(true);
				}
				asyncPostChangePassRequest = new AsyncPostChangePassRequest(
						this, currentPass, newPass);
				asyncPostChangePassRequest.execute();
			} else {
//				Toast.makeText(this,
//						"New Password and Confirm password Does not match",
//						Toast.LENGTH_SHORT).show();
				etConfirmPassword.setError("New Password and Confirm password Does not match");
			}
		}
		break;
		case R.id.bDashboard:
			if(MainActionbarBase.stackIndex!=null){
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
//			Toast.makeText(ChangePasswordActivity.this, "No Servilance System Available", Toast.LENGTH_SHORT).show();
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
		
	}

	private boolean validateChangePassWindow() {
		
		if (etOldPass.getText().toString().trim().equals("")){
			etOldPass.setError("Please provide current password");
			return false;
		}
		else if( etNewPassword.getText().toString().trim().equals("")){
			etNewPassword.setError("Please provide new password");
			return false;
		}
		else if (etConfirmPassword.getText().toString().trim().equals("")) {
			etConfirmPassword.setError("Please provide confirm password");
			return false;
		} else if (!etOldPass.getText().toString()
				.equals(CommonTask.getPassword(this))) {
			etOldPass.setError("Current Password does not match");
//			bSavePassword.setEnabled(false);
			return false;
			
		}else if(etOldPass.getText().toString().equals(etNewPassword.getText().toString()) && etOldPass.getText().toString().equals(etConfirmPassword.getText().toString())){
			etNewPassword.setError("Current password and New Password are same");
			return false;
		}
		
		else {
//			bSavePassword.setEnabled(true);
			etOldPass.setError(null);
			etNewPassword.setError(null);
			etConfirmPassword.setError(null);
			return true;
		}
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
//	public boolean ispassChanged=false;
	public boolean sendChangePassReq(String currentPass, String newPass) {
		
//		String changePassUrl = CommonURL.getInstance().GetCommonURL+ "/" + CommonValues.getInstance().userId + "/passChange";
		String changePassUrl = CommonURL.getInstance().RootUrl+  "passchange";
		if (JsonParser.postChangePassRequest(changePassUrl, currentPass,
				newPass) != null && JsonParser.postChangePassRequest(changePassUrl, currentPass,
						newPass) !="") {
			
			return true;
		}else{
		return false;
		}
	}

	public void resetChangePassWindow() {

		etOldPass.setText("");
		etNewPassword.setText("");
		etConfirmPassword.setText("");
		removePreferenceLoginData();
		clearAppData();
		Intent intent = new Intent("com.sinepulse.app.activities.UserLogin");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		Toast.makeText(this, getResources().getString(R.string.passchangetext) ,Toast.LENGTH_LONG).show();
//		this.finish();

	}

	public void startProgress() {
		pbChangePass.setVisibility(View.VISIBLE);

	}

	public void stopProgress() {
		pbChangePass.setVisibility(View.GONE);
	}
}
