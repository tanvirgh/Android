/**
 * 
 */
package com.sinepulse.app.fragments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncPostChangePassRequest;
import com.sinepulse.app.utils.CommonTask;

/**
 * @author tanvir.ahmed
 * 
 */
@EFragment(R.layout.change_password)
public class ChangePasswordFragment extends SherlockFragment implements
		OnItemClickListener {

	public static final int INITIAL_STATE = -1;

	public enum AboutState {
		INITIAL_STATE, // backState -1

	};

	public static AboutState backState = AboutState.INITIAL_STATE;

	InputMethodManager imm;
	@ViewById(R.id.etOldPass)
	public EditText etOldPass;
	@ViewById(R.id.etNewPassword)
	public EditText etNewPassword;
	@ViewById(R.id.etConfirmPassword)
	public EditText etConfirmPassword;
	@ViewById(R.id.bSavePassword)
	public Button bSavePassword;
	@ViewById(R.id.pbChangePass)
	public ProgressBar pbChangePass;
	AsyncPostChangePassRequest asyncPostChangePassRequest = null;

	/**
	 * Automatically call once when class created initialize all control
	 * variable and load family using LoadFamilyContent Asynchronously use
	 * thread for performing search
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@AfterViews
	void afterViewsLoaded() {

	}

	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen
	 */
	public boolean onBackPressed() {

		backState = AboutState.INITIAL_STATE;
		return false;
	}

	@Override
	public void onResume() {
		getSherlockActivity().setTitle("Change Password");
		fragmentPaused = false;
		super.onResume();
		backState = AboutState.INITIAL_STATE;
	}

	boolean fragmentPaused = false;

	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
		super.onPause();
		fragmentPaused = true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {

		switch (parent.getId()) {

		default:
			break;
		}

	}

	@Click(R.id.bSavePassword)
	public void onClick(View v) {
		if (validateChangePassWindow()) {
			String currentPass = etOldPass.getText().toString();
			String newPass = etNewPassword.getText().toString();
			if (etNewPassword.getText().toString()
					.equals(etConfirmPassword.getText().toString())) {
				if (asyncPostChangePassRequest != null) {
					asyncPostChangePassRequest.cancel(true);
				}
//				asyncPostChangePassRequest = new AsyncPostChangePassRequest(
//						this, currentPass, newPass);
//				asyncPostChangePassRequest.execute();
			} else {
				Toast.makeText(getActivity(),
						"New Password and Confirm password Does not match",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean validateChangePassWindow() {
		if (etOldPass.getText().toString().equals("")
				|| etNewPassword.getText().toString().equals("")
				|| etConfirmPassword.getText().toString().equals("")) {
			Toast.makeText(getActivity(),
					"Please provide required information", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!etOldPass.getText().toString()
				.equals(CommonTask.getPassword(getActivity()))) {
			Toast.makeText(getActivity(), "Current Password does not match",
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public boolean sendChamgePassReq(String currentPass, String newPass) {
//		String changePassUrl = CommonURL.getInstance().baseUrl + "/user/"
//				+ CommonValues.getInstance().userId + "/passChange";
//		if (JsonParser.postChangePassRequest(changePassUrl, currentPass,
//				newPass) != null) {
//			return true;
//		}
		return false;
	}

	public void resetChangePassWindow() {

		etOldPass.setText("");
		etNewPassword.setText("");
		etConfirmPassword.setText("");

	}

	public void startProgress() {
		pbChangePass.setVisibility(View.VISIBLE);

	}

	public void stopProgress() {
		pbChangePass.setVisibility(View.GONE);
	}

}
