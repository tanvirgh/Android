package com.sinepulse.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.sinepulse.app.R;
import com.sinepulse.app.utils.NetworkUtil;

/**
 * Application Launcher class 
 * This class activity perform a splash screen with a time interval
 * After showing this class redirect to home screen as application default 
 * @author Tac
 * 
 */
public class SplashScreen extends Activity {

	// how long until we go to the next activity
	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms
//	CheckMC checkMC = null;
//	protected static String connnectionState="NONE";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		/*if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();
		}*/
		final String status = NetworkUtil.getConnectivityStatusString(this);
		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
//					if(CommonTask.isNetworkAvailable(getApplicationContext())){
//						if (status.equals("Wifi enabled")) {
//							WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
//							WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//							//make api call to find rajberrypi and if yes set it as base url and save the base url in shared preference
//							//if api returns no...theck for subnet mask as 151.....if yes  set x.x.x.151 as baseurl
//							
//							int ip = wifiInfo.getIpAddress();
//							String ipAddress = Formatter.formatIpAddress(ip);
//							String[] tokens = ipAddress.split("\\."); 
//						    tokens[3]="111";
//						    ipAddress=tokens[0]+"."+tokens[1]+"."+tokens[2]+"."+tokens[3];
////							Log.d("WIFI Ip", ipAddress);
//							String urlForMcState="http://"+ipAddress+"/api/is-online";
//							
//							connectByIp(urlForMcState);
//							
//						} else if(status.equals("Mobiledata enabled")) {
//							CommonURL.getInstance().assignValues(CommonURL.getInstance().remoteBaseUrl);
//							Log.d("NInfo", "GSM");
//						}else{
//							//No Internet
//						}
//						}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					SplashScreen.this.finish();
					Intent intent = new Intent(
							"com.sinepulse.app.activities.UserLogin");
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//					CommonValues.getInstance().homeIntent=intent;
					startActivity(intent);
				}
			}
//			public void connectByIp(String urlForMcState) {
//				if (checkMC != null) {
//					checkMC.cancel(true);
//				}
//				connnectionState="IP";
//				checkMC = new CheckMC(urlForMcState);
//				checkMC.execute();
//			}
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
	
//	private class CheckMC extends AsyncTask<Void, Void, Boolean> {
//		private String mcUrl;
//
//		public CheckMC(String mcUrl) {
//			this.mcUrl=mcUrl;
//			
//		}
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			
//			sendMCStatusRequest(mcUrl);
//
//			return null;
//		}
//		@Override
//		protected void onPostExecute(Boolean result) {
//			
//			
//		};
//		
//	}
//	
//	
//	public boolean sendMCStatusRequest(String mcUrl) {
//
//
//		if (getMcStatus(mcUrl) != null) {
////			JsonParser.connnectionState="";
//			return true;
//		}
//		return false;
//	}
//public  String  getMcStatus(String url) {
//		
//		InputStream is = null;
//		String result = "";
//		DefaultHttpClient httpClient = new DefaultHttpClient();
//
//		try {
//			HttpGet httpGet = new HttpGet(url);
//			HttpResponse httpResponse = httpClient.execute(httpGet);
//			HttpEntity httpEntity = httpResponse.getEntity();
//			is = httpEntity.getContent();
//
//			if (is != null) {
//				result = JsonParser.convertInputStreamToString(is);
//			} else {
//				result = "Did not work!";
//				CommonValues.getInstance().IsServerConnectionError = true;
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (result != null && !result.equals("")) {
//			try {
//				JSONObject jObject = null;
//				jObject = new JSONObject(result);
////				String host=(jObject.getJSONObject("Data")).getString("hostname");
//				String Ip=(jObject.getJSONObject("Data")).getString("ip");
//				String baseUrlForMC="http://"+Ip+"/api/";
//				CommonURL.getInstance().assignValues(baseUrlForMC);
////				CommonURL.getInstance().baseUrl=baseUrlForMC;
//				
//				}
//
//			catch (JSONException e) {
//				Log.e("log_tag", "Error parsing data " + e.toString());
//			}
//		}else{
//			if(connnectionState.equals("IP")){
//				connectByRasPeri();
//			}else if(connnectionState.equals("RasPeri")){
//				CommonURL.getInstance().assignValues(CommonURL.getInstance().remoteBaseUrl);
//			}
////			CommonURL.getInstance().baseUrl=CommonURL.getInstance().remoteBaseUrl;
//		}
//
//		// 11. return result
//		return result;
//	}

/**
 * 
 */
//public void connectByRasPeri() {
//	  connnectionState="RasPeri";
//		String urlForRajpBeripi="http://sinepulsemcprod.local/api/is-online";
//		if (checkMC != null) {
//			checkMC.cancel(true);
//		}
//		
//		checkMC = new CheckMC(urlForRajpBeripi);
//		checkMC.execute();
//		
//}

}