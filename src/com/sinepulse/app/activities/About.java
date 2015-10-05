package com.sinepulse.app.activities;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * Display about section of the application with build version no and necessary
 * details .
 * 
 * @author tac
 * 
 */
@EActivity(R.layout.about)
public class About extends MainActionbarBase implements OnClickListener {

	// public static ActionBar mSupportActionBar;
	@ViewById(R.id.bCamera)
	protected Button bCamera;
	@ViewById(R.id.bRoom)
	protected Button bRoom;
	@ViewById(R.id.bDashboard)
	protected Button bDashboard;
	@ViewById(R.id.tvAboutHeadingText1)
	protected TextView tvAboutHeadingText1;
	Menu connMenu = null;
	public static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTheme(R.style.settinglistbackground);
//		setContentView(R.layout.about);
		About.context = this;
		mainActionBarContext = About.context;
//		this.connMenu = actionBarMenu;
		this.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
		final String status = NetworkUtil.getConnectivityStatusString(this);
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		if (item.getItemId() == R.id.menu_conn_indicatior) {
			// setConnectionNodeImage(CommonValues.getInstance().globalMenu,
			// this);
			if (status.equals("Mobiledata enabled")
					&& CommonValues.getInstance().connectionMode
							.equals("Internet")) {
				CommonTask
						.ShowMessage(this,
								"Local mode is not accessible in GSM network.Please try with WiFi.");
			} else {
				CommonTask.ShowNetworkChangeConfirmation(About.this,
						"Do you Really want to change mode?.",
						showNetworkChangeEvent());
			}
			/*
			 * try { Thread.sleep(1000); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 * 
			 * setConnectionNodeImage(CommonValues.getInstance().globalMenu,
			 * this);
			 */

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean prepared = super.onPrepareOptionsMenu(menu);
		// connMenu=menu;
		setConnectionNodeImage(CommonValues.getInstance().globalMenu, this);
		hideRefreshMenu(menu);
		return prepared;
	}

	@Override
	@Click({ R.id.bCamera, R.id.bDashboard, R.id.bRoom,
			R.id.tvAboutHeadingText1 })
	public void onClick(View v) {
		if (MainActionbarBase.stackIndex != null) {
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
			// Toast.makeText(About.this, "No Servilance System Available",
			// Toast.LENGTH_SHORT).show();
			currentFragment = CAMERA_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(6)))
				stackIndex.push(String.valueOf(6));
			Intent cameraIntent = new Intent(this, VideoActivity_.class);
			cameraIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(cameraIntent);

			break;
		case R.id.bRoom:
			currentFragment = ROOM_FRAGMENT;
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
			Intent roomIntent = new Intent(this, RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);

			break;
		case R.id.tvAboutHeadingText1:
			Intent viewIntent = new Intent(
					"android.intent.action.VIEW",
					Uri.parse("https://play.google.com/store/apps/details?id=com.sinepulse.app"));
			startActivity(viewIntent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		// setConnectionNodeImage(actionBarMenu,mainActionBarContext);
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
