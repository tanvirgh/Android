package com.sinepulse.app.fragments;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.sinepulse.app.R;

/**
 * @author tac
 */

@EFragment(R.layout.about)
public class AboutFragment extends SherlockFragment implements OnItemClickListener{

	public static final int INITIAL_STATE = -1;
	public enum AboutState {
		INITIAL_STATE, // backState -1		

	};

	public static AboutState backState = AboutState.INITIAL_STATE;

	InputMethodManager imm;

	
	/**
	 * Automatically call once when class created initialize all control
	 * variable and load family using LoadFamilyContent Asynchronously use
	 * thread for performing search
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@AfterViews
	void afterViewsLoaded(){
		
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
		getSherlockActivity().setTitle("About");
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







//	@Click({R.id.bSearchItemAddBusket,R.id.bSearchItemAdd,R.id.bSearchItemSubtract,R.id.bSearchBack, R.id.bsearchcancel})
//	public void onClick(View v) {
//	}


	private void cancelBack() {


//		imm.hideSoftInputFromWindow(saerchitem.getWindowToken(), 0);

		backState = AboutState.INITIAL_STATE;
	}


	
	
}
