package com.sinepulse.app.activities;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sinepulse.app.R;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.utils.CommonTask;
import com.sinepulse.app.utils.CommonValues;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * Application Launcher class This activity class perform a splash screen with a
 * time interval .After showing splash this class will redirect to home
 * screen(dash board) as application default
 * 
 * @author tavnir
 * 
 */
public class SplashScreen extends SherlockFragmentActivity {
	private NsdManager mNsdManager;
	private NsdManager.DiscoveryListener mDiscoveryListener;
	private NsdManager.ResolveListener mResolveListener;
	private NsdServiceInfo mServiceInfo;
	public String mRPiAddress;

	// The NSD service type that the RPi exposes.
	private static final String SERVICE_TYPE = "_workstation._tcp.";

	// how long until we go to the next activity
	protected boolean _active = true;
	protected int _splashTime = 1000; // time to display the splash screen in ms

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		/*
		 * if (getIntent().getBooleanExtra("EXIT", false)) { finish(); }
		 */

		// The workaround I chose to implement to resolve this issue is to check
		// for the Intent.
		// CATEGORY_LAUNCHER category and Intent.ACTION_MAIN action in the
		// intent that starts the initial Activity.
		// If those two flags are present and the Activity is not at the root of
		// the task (meaning the app was already running),
		// then I call finish() on the initial Activity. That exact solution may
		// not work in all scenario, but something similar should.
		// https://code.google.com/p/android/issues/detail?id=2373
		if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
				&& getIntent().getAction() != null
				&& getIntent().getAction().equals(Intent.ACTION_MAIN)) {

			finish();
			return;
		}
		// NSD ****Hostname resolve realted
		mRPiAddress = "";
		mNsdManager = (NsdManager) (getApplicationContext()
				.getSystemService(this.NSD_SERVICE));
		// initRegistrationListener();
		initializeDiscoveryListener();
		initializeResolveListener();

		mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,
				mDiscoveryListener);
		
		// thread for displaying the SplashScreen
		
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						// System.out.println("wait  :"+waited);
						if (_active) {
							waited += 100;
						}
					}

				} catch (InterruptedException e) {
					// do nothing
				} finally {

					Intent intent = new Intent(
							"com.sinepulse.app.activities.UserLogin");
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// CommonValues.getInstance().homeIntent=intent;
					startActivity(intent);
//					SplashScreen.this.finish();

				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}

		return true;
	}

	String requiredServiceName = "";

	private void initializeDiscoveryListener() {

		// Instantiate a new DiscoveryListener
		mDiscoveryListener = new NsdManager.DiscoveryListener() {

			// Called as soon as service discovery begins.
			@Override
			public void onDiscoveryStarted(String regType) {
			}

			@Override
			public void onServiceFound(NsdServiceInfo service) {
				// A service was found! Do something with it.
				String name = service.getServiceName();
				String type = service.getServiceType();

				//the service name returns name with mac address.So split this to get only the service name
				String Delims = "\\[";
				String[] requiredServiceArray = name.split(Delims);
				requiredServiceName = requiredServiceArray[0];

				if (type.equals(SERVICE_TYPE)
						&& (requiredServiceName.trim().equals("sinepulsemctest"))) {
					try {
						mNsdManager.resolveService(service, mResolveListener);
						
						mNsdManager.stopServiceDiscovery(this);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}

			@Override
			public void onServiceLost(NsdServiceInfo service) {
				// When the network service is no longer available.
				// Internal bookkeeping code goes here.
			}

			@Override
			public void onDiscoveryStopped(String serviceType) {
			}

			@Override
			public void onStartDiscoveryFailed(String serviceType, int errorCode) {
				mNsdManager.stopServiceDiscovery(this);
			}

			@Override
			public void onStopDiscoveryFailed(String serviceType, int errorCode) {
				mNsdManager.stopServiceDiscovery(this);
			}
		};
	}

	private void initializeResolveListener() {
		mResolveListener = new NsdManager.ResolveListener() {

			@Override
			public void onResolveFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				// Called when the resolve fails. Use the error code to debug.
//				Log.e("NSD", "Resolve failed" + errorCode);
			}

			@Override
			public void onServiceResolved(NsdServiceInfo serviceInfo) {
				mServiceInfo = serviceInfo;

				// Port is being returned as 9. Not needed.
				// int port = mServiceInfo.getPort();
				InetAddress host = mServiceInfo.getHost();
				String address = host.getHostAddress();
				Log.d("NSD", "Resolved address = " + address);
				mRPiAddress = address;
				CommonValues.getInstance().nsdResolvedIp = mRPiAddress;
				if (address != null && !address.equals("")) {
					Toast.makeText(
							SplashScreen.this,
							"NSD output : "
									+ CommonValues.getInstance().nsdResolvedIp,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SplashScreen.this,
							"NSD output : " + "Failed to Resolve IP",
							Toast.LENGTH_SHORT).show();
				}

			}
		};
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		 finish();
	}

}