package com.sinepulse.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Tanvir Ahmed Chowdhury 
 * use for connecting to application server
 */

public class JSONfunctions {

	public static JSONObject getJSONfromURL(String url) {
		return getJSONfromURL(url, CommonConstraints.GET);
	}

	public static JSONObject getJSONfromURL(String url, String httpType) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;
		CommonValues.getInstance().IsServerConnectionError = false;
		Log.d("Server URL",  url);

		try {
			// code block for for server connection timeout..here it is set to
			// 10 second..Tanvir
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, CommonConstraints.TIMEOUT_MILLISEC);
			// TimeOut Code Ends
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpResponse response;
			response = httpclient
					.execute(httpType.equals(CommonConstraints.GET) ? new HttpGet(url)
							: new HttpPost(url));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} else if(response.getStatusLine().getStatusCode() == 404){
				CommonValues.getInstance().IsServerConnectionError = true;
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
			CommonValues.getInstance().IsServerConnectionError = true;
		}
		if (is != null) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is,"UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = fixEncoding(sb.toString());
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
			if (result != null && !result.equals("")) {
				try {

					jArray = new JSONObject(result);
				} catch (JSONException e) {
					Log.e("log_tag", "Error parsing data " + e.toString());
					CommonValues.getInstance().IsServerConnectionError = true;
				}
			}
		}
		return jArray;
	}

	/**
	 * Retrieve json data string from a url request
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object retrieveDataFromStream(String url, Class<?> dataClass,String userName,String Password) {
		//MRB: lets check if we have a working data and then continue otherwise return null immediately.
//		if(Home.context!=null && !CommonTask.isNetworkAvailable(Home.context)){
//			CommonValues.getInstance().ErrorCode = CommonConstraints.NO_INTERNET_AVAILABLE;
//			return null;
//		}
		Log.d("ServerURL",  url);
		
		CommonValues.getInstance().ErrorCode = CommonConstraints.NO_EXCEPTION;
		int TIMEOUT_MILLISEC = 10000; // = 10 seconds
		InputStream inputStream = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpPost httpPost = new HttpPost(url);
			// Set accept header
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			
	            String json = "";
	            // 3. build jsonObject
	            JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("UserName", userName);
	            jsonObject.accumulate("Password", Password);
			
			
	       HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse response ;
			
			response = httpclient.execute(httpPost);
			Log.e("response",  response.toString());

			// check response ok(200) code
			//MRB: below line is commented out deliberately as this url
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					inputStream = entity.getContent();
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream,"UTF-8"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						GsonBuilder builder = new GsonBuilder();
						builder.registerTypeAdapter(double.class, new DoubleDeserializer());
						Gson gson = builder.create();		
						return gson.fromJson(fixEncoding(sb.toString()), dataClass);
//						return gson.fromJson(sb.toString(), dataClass);
					} 
					catch (Exception e) {
						Log.e("log_tag", "Error converting result " + e.toString());
						CommonValues.getInstance().IsServerConnectionError = true;
					}
				}
			}

		} catch (ClientProtocolException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ErrorCode = CommonConstraints.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IllegalStateException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ErrorCode = CommonConstraints.ILLEGAL_STATE_EXCEPTION;
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ErrorCode = CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (IOException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ErrorCode = CommonConstraints.IO_EXCEPTION;
		} catch (Exception e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ErrorCode = CommonConstraints.GENERAL_EXCEPTION;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("log_tag", "Error in " + e.toString());
				CommonValues.getInstance().ErrorCode = CommonConstraints.IO_EXCEPTION;
			}
		}
		return null;
	}
	
	public static String fixEncoding(String latin1) {
		try {
			byte[] bytes = latin1.getBytes("ISO-8859-1");
			if (!validUTF8(bytes))
				return latin1;
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Impossible, throw unchecked
			throw new IllegalStateException("No Latin1 or UTF-8: "
					+ e.getMessage());
		}

	}

	public static boolean validUTF8(byte[] input) {
		int i = 0;
		// Check for BOM
		if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
				&& (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
			i = 3;
		}

		int end;
		for (int j = input.length; i < j; ++i) {
			int octet = input[i];
			if ((octet & 0x80) == 0) {
				continue; // ASCII
			}

			// Check for UTF-8 leading byte
			if ((octet & 0xE0) == 0xC0) {
				end = i + 1;
			} else if ((octet & 0xF0) == 0xE0) {
				end = i + 2;
			} else if ((octet & 0xF8) == 0xF0) {
				end = i + 3;
			} else {
				// Java only supports BMP so 3 is max
				return false;
			}

			while (i < end) {
				i++;
				octet = input[i];
				if ((octet & 0xC0) != 0x80) {
					// Not a valid trailing byte
					return false;
				}
			}
		}
		return true;
	}
	
	
	
}
