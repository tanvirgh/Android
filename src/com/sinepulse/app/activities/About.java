package com.sinepulse.app.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncPostChangePassRequest;
import com.sinepulse.app.base.MainActionbarBase;

/**
 * Display about section of the apps with version no and necessary details of the app.
 * @author tac
 *
 */
@EActivity(R.layout.about)
public class About extends MainActionbarBase implements OnClickListener {

	public static ActionBar mSupportActionBar;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.settinglistbackground);
		setContentView(R.layout.about);
		super.onCreate(savedInstanceState);

		createMenuBar();
		
	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));
		
		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setTitle("About");
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			
			 onBackPressed();

		}
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
		hideRefreshMenu(menu);
		return prepared;
	}
	
	@Click({R.id.bCamera,R.id.bDashboard,R.id.bRoom})
	public void onClick(View v) {
		MainActionbarBase.stackIndex.removeAllElements();
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
		mSupportActionBar.setTitle(R.string.about);
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
