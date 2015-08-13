package com.sinepulse.app.activities;

import org.MediaPlayer.PlayM4.Player;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_IPCHANINFO;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.RealPlayCallBack;
import com.sinepulse.app.R;
import com.sinepulse.app.asynctasks.AsyncGetCameraInfo;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Alert;
import com.sinepulse.app.fragments.DebugTools;
import com.sinepulse.app.fragments.LiveSurface;
import com.sinepulse.app.utils.CommonIdentifier;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.JsonParser;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * 
 * @author tanvir.ahmed
 *
 */

@EActivity(R.layout.mediaplayer)
public class VideoActivity extends MainActionbarBase implements
		android.widget.AdapterView.OnItemSelectedListener,
		SurfaceHolder.Callback, OnClickListener {

	ArrayAdapter<String> dataAdapter;
	int spinnerValue;
	String streamingUrl = "";
	boolean fragmentPaused = false;
	public static final int INITIAL_STATE = -1;

	public enum CameraFragmentState {
		INITIAL_STATE // backState -1
	};

	public static CameraFragmentState backState = CameraFragmentState.INITIAL_STATE;
	@ViewById(R.id.spinner_camera)
	public Spinner spCamera;
	@ViewById(R.id.MyStreamButton)
	public Button streamingButton;
	@ViewById(R.id.bCamera)
	public Button bCamera;
	@ViewById(R.id.surface)
	public LiveSurface surface;
	int mPlayId = -1;
	public static final int CHANNEL_TYPE_ANALOG = 1;
	public static final int CHANNEL_TYPE_DIGIT = 0;
	public static final int CHANNEL_TYPE_ZERO = 3;
	public static final byte CHANNEL_ENABLED = 1;
	public static final byte CHANNEL_DISABLED = 0;
	LoadingDevicesTask loadingDevicesTask = null;
	AsyncGetCameraInfo asyncGetCameraInfo = null;
	String cameraIp;
	Integer port;
	String userName;
	String password;
	@ViewById(R.id.pbCamera)
	public ProgressBar pbCamera;
	public Menu actionBarMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createMenuBar();

		// loadStream( CommonValues.getInstance().currentCameraIndex);

	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.actionBarMenu = menu;
		return true;
	};*/

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		this.actionBarMenu = menu;
		boolean prepared = super.onPrepareOptionsMenu(menu);
		hideRefreshMenu(menu);
		setConnectionNodeImage(actionBarMenu);
		
		return prepared;
	};

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
		}
		return true;
	}

	@AfterViews
	void afterViewLoaded() {
		spCamera.setOnItemSelectedListener(this);
		
	}

	@Override
	@Click({ R.id.MyStreamButton, R.id.bCamera, R.id.bRoom, R.id.bDashboard })
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.MyStreamButton://
			releaseVideoProperties();
			final String status = NetworkUtil.getConnectivityStatusString(this);
			if (status.equals("Wifi enabled")) {
				continueStreaming();

			} else if (status.equals("Mobiledata enabled")) {
				if (streamingButton.getText().toString() == "Start Streaming") {
					/*
					 * CommonTask .ShowConfirmation( this,
					 * "Video Stream will consume high Data Volume.Do you want to continue?"
					 * , ShowCameraEvent());
					 */
					CommonValues.getInstance().alertObj = Alert
							.setCustomAlertData(530, "Warning");
					CommonTask.ShowConfirmation(this,
							CommonValues.getInstance().alertObj,
							ShowCameraEvent());
				} else {
					// releaseVideoProperties();
					// continueStreaming();
					Player.getInstance().refreshPlay(playPort);

				}

			}

			break;
		case R.id.bCamera:
			break;
		case R.id.bRoom:
			// releaseVideoProperties();
			playPort = -1;
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
		case R.id.bDashboard:
			// loadingDevicesTask.cancel(true);
			// releaseVideoProperties();
			playPort = -1;
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
			// ((MainActionbarBase) getActivity()).displayFragment(0);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 */
	public void continueStreaming() {
		// Toast.makeText(VideoActivity.this,
		// "*** Loading Video.Please wait ***",
		// Toast.LENGTH_SHORT).show();
		loadStream(spinnerValue);
	}

	public DialogInterface.OnClickListener ShowCameraEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					continueStreaming();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
			}
		};
		return dialogClickListener;

	}

	private void loadStream(int spinnerValue) {
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_DvrInfo;
		if (loadingDevicesTask != null) {
			loadingDevicesTask.cancel(true);
		}
		loadingDevicesTask = new LoadingDevicesTask(spinnerValue, cameraIp,
				port, userName, password);
		loadingDevicesTask.execute();
		streamingButton.setText("Refresh");
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long id) {
		streamingButton.setText("Start Streaming");
		switch (position) {
		// Showing selected spinner item
		case 0:
			spinnerValue = 1;
			break;
		case 1:
			// Whatever you want to happen when the second item gets selected
			spinnerValue = 2;
			break;
		case 2:
			// Whatever you want to happen when the third item gets selected
			spinnerValue = 3;
			break;
		case 3:
			// Whatever you want to happen when the 4th item gets selected
			spinnerValue = 4;
			break;
		case 4:
			// Whatever you want to happen when the 5th item gets selected
			spinnerValue = 5;
			break;
		case 5:
			spinnerValue = 6;
			break;
		case 6:
			spinnerValue = 7;
			break;
		case 7:
			spinnerValue = 8;
			break;
		case 8:
			spinnerValue = 9;
			break;
		case 9:
			spinnerValue = 10;
			break;
		case 10:
			spinnerValue = 11;
			break;
		case 11:
			spinnerValue = 12;
			break;
		case 12:
			spinnerValue = 13;
			break;
		case 13:
			spinnerValue = 14;
			break;
		case 14:
			spinnerValue = 15;
			break;
		case 15:
			spinnerValue = 16;
			break;
		default:
			break;

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// playVideo(path);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	@Override
	public void onPause() {
		releaseVideoProperties();
		streamingButton.setText("Start Streaming");
		super.onPause();
		fragmentPaused = true;
		spinnerValue = spCamera.getSelectedItemPosition() + 1;
		CommonValues.getInstance().currentCameraIndex = spinnerValue;
		CommonValues.getInstance().cameraInfo=null;
	}

	@Override
	public void onResume() {
		bCamera.setBackground(getResources().getDrawable(
				R.drawable.camera_selected1));
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getSupportActionBar().setTitle("Live Video");
		streamingButton.setText("Start Streaming");
		CommonValues.getInstance().currentAction = CommonIdentifier.Action_CameraInfo;
		if (asyncGetCameraInfo != null) {
			asyncGetCameraInfo.cancel(true);
		}
		asyncGetCameraInfo = new AsyncGetCameraInfo(this);
		asyncGetCameraInfo.execute();
//		setConnectionIndicator(actionBarMenu);
		// playPort=-1;
		fragmentPaused = false;
		super.onResume();

	}

	/**
	 * 
	 */
	private void releaseVideoProperties() {

		if (surface != null && player != null) {
			if (surface.getHolder().getSurface().isValid() && -1 != playPort) {
				Player.getInstance().closeStream(playPort);
				Player.getInstance().freePort(playPort);
				Player.getInstance().setVideoWindow(playPort, 0, null);
				playPort = -1;
				Player.getInstance().stop(mPlayId);
			}
			// hcNetSdk.NET_DVR_StopPlayBack(playPort);
			hcNetSdk.NET_DVR_Logout_V30(mPlayId);
			hcNetSdk.NET_DVR_Cleanup();
			playPort = -1;
			mPlayId = -1;
		}
	}

	@Override
	public void onBackPressed() {
		// releaseVideoProperties();
		playPort = -1;
		mPlayId = -1;
		if (MainActionbarBase.stackIndex != null) {
			MainActionbarBase.stackIndex.removeAllElements();
		}
		currentFragment = ALLDEVICE_FRAGMENT;
		Intent homeIntent = new Intent(this, Home_.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(homeIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStop() {
		// releaseVideoProperties() ;
		super.onStop();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			this.getActionBar().hide();
			this.getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// View videoview=findViewById(R.id.videoScreen);
			// videoview.getLayoutParams().height=WindowManager.LayoutParams.FLAG_FULLSCREEN;
			// videoview.setBackgroundColor(R.color.black);
			// spCamera.setVisibility(View.INVISIBLE);
			// streamingButton.setVisibility(View.INVISIBLE);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			this.getActionBar().show();
			// spCamera.setVisibility(View.VISIBLE);
			// streamingButton.setVisibility(View.VISIBLE);
			// this.getWindow().clearFlags(
			// WindowManager.LayoutParams.FLAG_FULLSCREEN);
			this.getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	private static final int PLAYING_BUFFER_SIZE = 1024 * 1024 * 4;

	private Player player;
	private int playPort = -1;

	private HCNetSDK hcNetSdk;

	private ExceptionCallBack exceptionCallback = new ExceptionCallBack() {

		@Override
		public void fExceptionCallBack(int code, int userId, int handle) {
		}
	};

	private RealPlayCallBack realplayCallback = new RealPlayCallBack() {

		@Override
		public void fRealDataCallBack(int handle, int dataType, byte[] buffer,
				int bufferSize) {
			/*
			 * System.out .println(String .format(
			 * "fRealDataCallBack{ handle : %s, dataType : %s, bufferSize : %s }"
			 * , handle, dataType, bufferSize));
			 */

			int i = 0;

			switch (dataType) {
			case HCNetSDK.NET_DVR_SYSHEAD:

				if (-1 == (playPort = Player.getInstance().getPort())) {
					// System.out.println("Can't get play port!");
					return;
				}

				if (0 < bufferSize) {
					if (openPlayer(buffer, bufferSize)) {
						System.out.println("Open player successfully.");
					} else {
						// System.out.println("Open player failed.");
						// CommonTask
						// .ShowMessage((VideoActivity) realplayCallback,
						// "Some error occured in HIK Vision SDK.Please try watching Stream later");
					}
				}

				break;

			case HCNetSDK.NET_DVR_STREAMDATA:
			case HCNetSDK.NET_DVR_STD_VIDEODATA:
			case HCNetSDK.NET_DVR_STD_AUDIODATA:

				if (0 < bufferSize && -1 != playPort) {
					try {
						for (i = 0; i < 400; i++) {
							if (Player.getInstance().inputData(playPort,
									buffer, bufferSize)) {
								// System.out.println("Played successfully.");
								break;
							}

							// System.out.println("Playing failed.");

							Thread.sleep(10);
						}
					} catch (Exception e) {

					}

					if (i == 400) {
						// System.out.println("inputData failed");
					}

				}

			}

		}
	};

	private boolean openPlayer(byte[] buffer, int bufferSize) {

		if (!Player.getInstance().setStreamOpenMode(playPort,
				Player.STREAM_FILE)) {
			// System.out.println("The player set stream mode failed!");
			return false;
		}

		if (!Player.getInstance().openStream(playPort, buffer, bufferSize,
				PLAYING_BUFFER_SIZE)) {
			Player.getInstance().freePort(playPort);
			playPort = -1;

			return false;
		}

		Player.getInstance().setStreamOpenMode(playPort, Player.STREAM_FILE);
		// System.out.println("We are using " + surface.getHolder()
		// +" as a Displayer.");

		if (!Player.getInstance().play(playPort,
				surface.getHolder().getSurface())) {
			Player.getInstance().closeStream(playPort);
			Player.getInstance().freePort(playPort);

			playPort = -1;

			return false;
		}

		return true;
	}

	public void catchErrorIfNecessary() {
		int code = hcNetSdk.NET_DVR_GetLastError();
		if (0 != code && code == 1 || code == 153) {
			// System.out.println("Error: " + code);
			authenticationError = true;
		} else if (0 != code && code == 4) {
			videoDecodingError = true;
		} else if (0 != code && code == 32) {
			invalidPort = true;
		}
	}

	public boolean sendGetCameraInfoRequest() {

		// String getCameraInfoUrl = CommonURL.getInstance().GetCommonURL + "/"
		// + logedInUserId + "/home" + "/camera";
		String getCameraInfoUrl = CommonURL.getInstance().RootUrl + "camera";

		if (JsonParser.getcameraInfoRequest(getCameraInfoUrl) != null
				&& JsonParser.getcameraInfoRequest(getCameraInfoUrl) != "") {
			return true;
		} else {
			/*
			 * VideoActivity.this.runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() {
			 * CommonTask.ShowMessage(VideoActivity.this,
			 * "No Data Returned From Server."); } });
			 */
			return false;
		}
	}

	public void setCameraInfo() {
//		setConnectionNodeImage(actionBarMenu);
		if (CommonValues.getInstance().cameraInfo != null) {
			cameraIp = CommonValues.getInstance().cameraInfo.IpAddress;
			port = CommonValues.getInstance().cameraInfo.port;
//			port=9000;
			userName = CommonValues.getInstance().cameraInfo.userName;
			password = CommonValues.getInstance().cameraInfo.password;
//			 userName = "smarthomedev11";
			// password = "123456";
			Integer channelCount = CommonValues.getInstance().cameraInfo.ChannelCount;
			// String[] state=new String[channelCount];
			if (userName == null || password == null) {
				CommonTask.ShowMessage(this, "Server Returned empty Camera Data");
			}
			if (channelCount == 0) {
				streamingButton.setEnabled(false);
				CommonTask.ShowMessage(this, "Surveillance system not available");
			} else {
				streamingButton.setEnabled(true);
			}
			String[] cameraStates = new String[channelCount];
			for (int i = 0; i < channelCount; i++) {

				cameraStates[i] = "Camera " + (i + 1);
			}
			// Initialize and set Adapter
			dataAdapter = new ArrayAdapter<String>(this,
					R.layout.spinneritem_camera, cameraStates);
			spCamera.setAdapter(dataAdapter);
			spCamera.setSelection(CommonValues.getInstance().currentCameraIndex - 1);

		} else {
			// CommonTask.ShowMessage(this, "Error Fetching Data from Server");
			/*CommonTask
					.ShowNetworkChangeConfirmation(
							this,
							"Network State/Configuration Settings has been changed.Please log in again to continue.",
							showNetworkChangeEvent());*/
		}
	}

	boolean dvrOff = false;
	boolean authenticationError = false;
	boolean videoDecodingError = false;
	boolean invalidPort = false;

	private class LoadingDevicesTask extends AsyncTask<Void, Void, Boolean> {

		private int spinnerValue;
		private String iP;
		private int port;
		private String userName;
		private String passWord;

		public LoadingDevicesTask(int spinnerValue, String iP, int port,
				String userName, String passWord) {
			this.spinnerValue = spinnerValue;
			this.iP = iP;
			this.port = port;
			this.userName = userName;
			this.passWord = passWord;

		}

		@Override
		protected void onPreExecute() {
			CommonValues.getInstance().previousAction = CommonValues
					.getInstance().currentAction;
			startProgress();
			streamingButton.setEnabled(false);

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			player = Player.getInstance();
			hcNetSdk = new HCNetSDK();
			hcNetSdk.NET_DVR_Init();
			hcNetSdk.NET_DVR_SetConnectTime(Integer.MAX_VALUE);
			hcNetSdk.NET_DVR_SetExceptionCallBack(exceptionCallback);

			// get play port
			playPort = player.getPort();
			catchErrorIfNecessary();

			// ----------------------------------------------------------------
			NET_DVR_DEVICEINFO_V30 dvr_deviceinfo;
			try {
				dvr_deviceinfo = new NET_DVR_DEVICEINFO_V30();
				mPlayId = hcNetSdk.NET_DVR_Login_V30(iP, port, userName,
						passWord, dvr_deviceinfo);
				DebugTools.dump(dvr_deviceinfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catchErrorIfNecessary();
			
			if (mPlayId < 0) {
				dvrOff = true;
				// return dvrOff;
			}

			
			NET_DVR_IPPARACFG_V40 ipParaCfg = new NET_DVR_IPPARACFG_V40();
			// UserId, Command, ChannelNo., Out
			try {
				hcNetSdk.NET_DVR_GetDVRConfig(mPlayId,
						HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, ipParaCfg);
				for (NET_DVR_IPCHANINFO entry : ipParaCfg.struIPChanInfo) {
					if (CHANNEL_ENABLED == entry.byEnable) {
						DebugTools.dump(entry);
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			DebugTools.dump(ipParaCfg);
			catchErrorIfNecessary();

			NET_DVR_CLIENTINFO clientInfo = new NET_DVR_CLIENTINFO();

			clientInfo.lChannel = spinnerValue;

			clientInfo.lLinkMode = 0;

			clientInfo.sMultiCastIP = null;

			// UserId, ClientInfo, RealplayCallback, Blocked
			/*
			 * final int returned = hcNetSdk.NET_DVR_RealPlay_V30(userId,
			 * clientInfo, realplayCallback, true);
			 */
			try {
				mPlayId = hcNetSdk.NET_DVR_RealPlay_V30(mPlayId, clientInfo,
						realplayCallback, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catchErrorIfNecessary();

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			stopProgress();
			streamingButton.setEnabled(true);
			// super.onPostExecute(result);
			if (authenticationError == true) {
				// Toast.makeText(VideoActivity.this, "Authentication Error.",
				// Toast.LENGTH_SHORT).show();
				CommonValues.getInstance().alertObj = Alert.setCustomAlertData(
						512, "Error");
				CommonTask.ShowAlertMessage(VideoActivity.this,
						CommonValues.getInstance().alertObj);
				return;
			}
			if (dvrOff == true) {
				// Toast.makeText(VideoActivity.this, "DVR is switched off.",
				// Toast.LENGTH_SHORT).show();
				CommonValues.getInstance().alertObj = Alert.setCustomAlertData(
						510, "Error");
				CommonTask.ShowAlertMessage(VideoActivity.this,
						CommonValues.getInstance().alertObj);
				return;
			}
			if (videoDecodingError == true) {
				Toast.makeText(VideoActivity.this, "Video Decoding Error.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			/*if (invalidPort == true) {
				// Toast.makeText(VideoActivity.this, "Invalid Port.",
				// Toast.LENGTH_SHORT).show();
				CommonValues.getInstance().alertObj = Alert.setCustomAlertData(
						511, "Error");
				CommonTask.ShowAlertMessage(VideoActivity.this,
						CommonValues.getInstance().alertObj);
				return;
			}*/
			android.os.AsyncTask.Status status = getStatus();
			if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
				if (CommonValues.getInstance().currentAction
						.equals(CommonValues.getInstance().previousAction)) {

					if (Player.getInstance().getPort() == -1) {
						showVideoLoadingError();
					}
				}
			}
		}

	}

	private void showVideoLoadingError() {
		CommonTask.ShowMessage(this, "Error Loading Video Stream");

	}

	public void startProgress() {
		pbCamera.setVisibility(View.VISIBLE);

	}

	public void stopProgress() {
		pbCamera.setVisibility(View.INVISIBLE);

	}
	
/*	public void setConnectionIndicator(com.actionbarsherlock.view.Menu menu){
		MenuItem connImage=menu.findItem(R.id.menu_conn_indicatior);
		if(CommonValues.getInstance().connectionMode=="Local"){
			connImage.setIcon(getResources().getDrawable(R.drawable.local));
		}else if(CommonValues.getInstance().connectionMode=="Internet"){
			connImage.setIcon(getResources().getDrawable(R.drawable.internet));
		}
		
	}*/

}
