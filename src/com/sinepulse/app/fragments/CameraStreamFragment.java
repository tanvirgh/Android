package com.sinepulse.app.fragments;

import org.MediaPlayer.PlayM4.Player;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_IPCHANINFO;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.RealPlayCallBack;
import com.sinepulse.app.fragments.DebugTools;

import com.sinepulse.app.R;
import com.sinepulse.app.activities.Home;
import com.sinepulse.app.base.MainActionbarBase;
/**
 * 
 * @author tanvir
 *
 */
@EFragment(R.layout.mediaplayer)
public class CameraStreamFragment extends SherlockFragment implements 
					android.widget.AdapterView.OnItemSelectedListener,  SurfaceHolder.Callback, OnClickListener  {
	
	private String[] cameraStates;
	ArrayAdapter<String> dataAdapter;
	int spinnerValue =0;
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
	int userId = 0;
	public static final int CHANNEL_TYPE_ANALOG = 1;
	public static final int CHANNEL_TYPE_DIGIT = 0;
	public static final int CHANNEL_TYPE_ZERO = 3;
	public static final byte CHANNEL_ENABLED = 1;
	public static final byte CHANNEL_DISABLED = 0;
	LoadingDevicesTask loadingDevicesTask = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		
	}
	
	@AfterViews
	void afterViewLoaded(){
		// Initialize and set Adapter
		cameraStates = getResources().getStringArray(R.array.camera_arrays);
		dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, cameraStates);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCamera.setPrompt("Choose A Camera!");
		spCamera.setAdapter(dataAdapter);

		spCamera.setOnItemSelectedListener(this);
		
	}
	
	@Override
	@Click({R.id.MyStreamButton,R.id.bCamera, R.id.bRoom, R.id.bDashboard})
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.MyStreamButton:// 
			Toast.makeText(getActivity(), "**** Loading Video.Please wait ****", Toast.LENGTH_SHORT).show();
			if (surface.getHolder().getSurface().isValid() &&  -1 != playPort) {
				Player.getInstance().closeStream(playPort);
				Player.getInstance().freePort(playPort);
				Player.getInstance().setVideoWindow(playPort, 0, null);
				hcNetSdk.NET_DVR_Cleanup();
				}
		switch (spinnerValue) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			loadStream(spinnerValue);
		
		}
			break;
		case R.id.bCamera:
			Home.mDrawerList.setItemChecked(7, true);
			Home.navDrawerAdapter.setSelectedPosition(7);
//			currentFragment = CAMERA_FRAGMENT;
			break;
		case R.id.bRoom:
			Home.mDrawerList.setItemChecked(2, true);
			Home.navDrawerAdapter.setSelectedPosition(2);
//			displayFragment(2);
			((MainActionbarBase) getActivity()).displayFragment(2);
			/*Intent roomIntent = new Intent(this.getActivity(), RoomManager_.class);
			roomIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(roomIntent);*/
			break;
		case R.id.bDashboard:
//			currentFragment = ALLDEVICE_FRAGMENT;
			Intent homeIntent = new Intent(this.getActivity(), Home.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
//			((MainActionbarBase) getActivity()).displayFragment(0);
			break;
		default:
			break;
		}

	}
	private void loadStream(int spinnerValue) {
	
		if (loadingDevicesTask != null) {
			loadingDevicesTask.cancel(true);
		}
		loadingDevicesTask = new LoadingDevicesTask(spinnerValue);
		loadingDevicesTask.execute();
		streamingButton.setText("Refresh");
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View v,   int position,
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
			// Whatever you want to happen when the thrid item gets selected
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
			// Whatever you want to happen when the 6th item gets selected
			spinnerValue = 6;
			break;
		case 6:
			// Whatever you want to happen when the thrid item gets selected
			spinnerValue = 7;
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
//		 playVideo(path);
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

	@Override
	public void onPause() {
		getSherlockActivity().setTitle(R.string.Home);
		super.onPause();
		fragmentPaused = true;
		
	}
	@Override
	public void onResume() {
		bCamera.setBackground(getResources().getDrawable(R.drawable.camera_selected1));
		getSherlockActivity().setTitle("Video Stream");
		if (surface.getHolder().getSurface().isValid() &&  -1 != playPort) {
			Player.getInstance().closeStream(playPort);
			Player.getInstance().freePort(playPort);
			Player.getInstance().setVideoWindow(playPort, 0, null);
			hcNetSdk.NET_DVR_Cleanup();
			}
		int spinnerValue=spCamera.getSelectedItemPosition()+1;
		loadStream(spinnerValue);
		fragmentPaused = false;
		super.onResume();
		
	}
	
	public boolean onBackPressed() {
		backState = CameraFragmentState.INITIAL_STATE;
		return false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onStop(){
		super.onStop();
	}
	@Override
	 public void onConfigurationChanged(Configuration newConfig) {
       super.onConfigurationChanged(newConfig);
       if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           getActivity().getActionBar().hide();
           spCamera.setVisibility(View.INVISIBLE);
           streamingButton.setVisibility(View.INVISIBLE);
           getActivity(). getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
    	   getActivity().getActionBar().show();
    	   spCamera.setVisibility(View.VISIBLE);
    	   streamingButton.setVisibility(View.VISIBLE);
    	   getActivity(). getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       }
	}
	private class LoadingDevicesTask extends AsyncTask<Void, Void, Boolean> {

		private int spinnerValue;

		public LoadingDevicesTask(int spinnerValue) {
			this.spinnerValue = spinnerValue;

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
			NET_DVR_DEVICEINFO_V30 dvr_deviceinfo = new NET_DVR_DEVICEINFO_V30();
			userId = hcNetSdk.NET_DVR_Login_V30("203.202.243.26", 8000,
					"admin", "12345", dvr_deviceinfo);
			DebugTools.dump(dvr_deviceinfo);
			catchErrorIfNecessary();
			NET_DVR_IPPARACFG_V40 ipParaCfg = new NET_DVR_IPPARACFG_V40();
			// UserId, Command, ChannelNo., Out
			hcNetSdk.NET_DVR_GetDVRConfig(userId,
					HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, ipParaCfg);
			for (NET_DVR_IPCHANINFO entry : ipParaCfg.struIPChanInfo) {
				if (CHANNEL_ENABLED == entry.byEnable) {
					DebugTools.dump(entry);
				}
			}

			DebugTools.dump(ipParaCfg);
			catchErrorIfNecessary();

			NET_DVR_CLIENTINFO clientInfo = new NET_DVR_CLIENTINFO();

			clientInfo.lChannel = spinnerValue;

			clientInfo.lLinkMode = 0;

			clientInfo.sMultiCastIP = null;

			// UserId, ClientInfo, RealplayCallback, Blocked
			final int returned = hcNetSdk.NET_DVR_RealPlay_V30(userId,
					clientInfo, realplayCallback, true);
			catchErrorIfNecessary();

			return null;

		}
	}

	private static final int PLAYING_BUFFER_SIZE = 1024 * 1024 * 4;

	private Player  player;
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
			/*System.out
					.println(String
							.format("fRealDataCallBack{ handle : %s, dataType : %s, bufferSize : %s }",
									handle, dataType, bufferSize));*/

			int i = 0;

			switch (dataType) {
			case HCNetSDK.NET_DVR_SYSHEAD:

				if (-1 == (playPort = Player.getInstance().getPort())) {
//					System.out.println("Can't get play port!");
					return;
				}

				if (0 < bufferSize) {
					if (openPlayer(buffer, bufferSize)) {
//						System.out.println("Open player successfully.");
					} else {
//						System.out.println("Open player failed.");
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
//								System.out.println("Played successfully.");
								break;
							}

//							System.out.println("Playing failed.");

							Thread.sleep(10);
						}
					} catch (Exception e) {

					}

					if (i == 400) {
//						System.out.println("inputData failed");
					}

				}

			}

		}
	};

	private boolean openPlayer(byte[] buffer, int bufferSize) {

		if (!Player.getInstance().setStreamOpenMode(playPort,
				Player.STREAM_FILE)) {
//			System.out.println("The player set stream mode failed!");
			return false;
		}

		if (!Player.getInstance().openStream(playPort, buffer, bufferSize,
				PLAYING_BUFFER_SIZE)) {
			Player.getInstance().freePort(playPort);
			playPort = -1;

			return false;
		}

		Player.getInstance().setStreamOpenMode(playPort, Player.STREAM_FILE);

		/*System.out.println("We are using " + surface.getHolder()
				+ " as a Displayer.");*/

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
		if (0 != code){
			System.out.println("Error: " + code);
		}
	}
	


}
