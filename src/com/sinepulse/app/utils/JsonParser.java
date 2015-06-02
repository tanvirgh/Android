package com.sinepulse.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sinepulse.app.activities.UserLogin;
import com.sinepulse.app.base.MainActionbarBase;
import com.sinepulse.app.entities.Address;
import com.sinepulse.app.entities.City;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.entities.DevicePropertyLog;
import com.sinepulse.app.entities.DeviceSummary;
import com.sinepulse.app.entities.HomeLink;
import com.sinepulse.app.entities.LogInInfo;
import com.sinepulse.app.entities.Preset;
import com.sinepulse.app.entities.Room;
import com.sinepulse.app.entities.Ticket;
import com.sinepulse.app.entities.TicketType;
import com.sinepulse.app.entities.UserProfile;

/**
 * 
 * @author Tanvir.Chowdhury
 * 
 */

public class JsonParser  extends MainActionbarBase{
	static UserLogin saveLogInData;
	static JSONObject jData = null;
	static JSONArray jArray = null;
	static String cookieID="";
//	static DefaultHttpClient httpClient = new DefaultHttpClient();
	
	

	public static String postLogInRequest(String url, LogInInfo logInInfo) {
		InputStream inputStream = null;
		String result = "";
		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), CommonConstraints.TIMEOUT_MILLISEC);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			String json = "";
			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("UserName", logInInfo.getUserName());
			jsonObject.accumulate("Password", logInInfo.getUserpassword());
			jsonObject.accumulate("AppToken", logInInfo.getAppToken());
			jsonObject.accumulate("AppType", logInInfo.getAppType());
			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			Header[] cookieHeader = httpResponse.getHeaders("Set-Cookie");
	        if (cookieHeader.length > 0) {
	            cookieID = cookieHeader[0].getValue();
//	            Log.d("Coke", cookieID);
	        }
			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				if(jObject.getString("Success").equalsIgnoreCase("True")){
				//API key
				try {
					if(CommonValues.getInstance().connectionMode=="Local"){
					CommonValues.getInstance().ApiKey=jObject.getString("ApiKey");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jData = jObject.getJSONObject("Data");
				jArray = jData.getJSONArray("DeviceSummary");
				int lengthofArray= jArray.length();
				
				for (int i = 0; i < lengthofArray; i++) {
					DeviceSummary dSummary = new DeviceSummary();
					dSummary.setDeviceCount(jArray.getJSONObject(i).getInt(
							"DeviceCount"));
					dSummary.setDeviceTypeId(jArray.getJSONObject(i).getInt(
							"DeviceTypeId"));
					dSummary.setPowerUsage(jArray.getJSONObject(i).getDouble(
							"PowerUsage"));
					dSummary.setRunningDeviceCount(jArray.getJSONObject(i)
							.getInt("RunningDeviceCount"));

					CommonValues.getInstance().summary.deviceSummaryArray
							.add(dSummary);
				}
				CommonValues.getInstance().summary.setTotalPower(jData
						.getDouble("TotalPower"));
				CommonValues.getInstance().summary.setRoomCount(jData
						.getInt("RoomCount"));
				CommonValues.getInstance().userId = jData.getInt("UserId");

			}
				else{
					CommonValues.getInstance().IsServerConnectionError = true;
					CommonValues.getInstance().loginError=jObject.getString("Message");
					
					}
				}
			
			
			catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		// 11. return result
		return result;
	}
	
	public static String  getSummaryRequest(String url) {
		
		InputStream is = null;
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);
		if (result != null && !result.equals("")) {
//			CommonValues.getInstance().summary=null;
			CommonValues.getInstance().summary.deviceSummaryArray.clear();
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				jData = jObject.getJSONObject("Data");
				jArray = jData.getJSONArray("DeviceSummary");
				int lengthofArray= jArray.length();
				for (int i = 0; i < lengthofArray; i++) {
					DeviceSummary dSummary = new DeviceSummary();
					dSummary.setDeviceCount(jArray.getJSONObject(i).getInt(
							"DeviceCount"));
					dSummary.setDeviceTypeId(jArray.getJSONObject(i).getInt(
							"DeviceTypeId"));
					dSummary.setPowerUsage(jArray.getJSONObject(i).getDouble(
							"PowerUsage"));
					dSummary.setRunningDeviceCount(jArray.getJSONObject(i)
							.getInt("RunningDeviceCount"));
					
					CommonValues.getInstance().summary.deviceSummaryArray
							.add(dSummary);
				}
				CommonValues.getInstance().summary.setTotalPower(jData
						.getDouble("TotalPower"));
				CommonValues.getInstance().summary.setRoomCount(jData
						.getInt("RoomCount"));
				CommonValues.getInstance().userId = jData.getInt("UserId");

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}

		// 11. return result
		return result;
	}

	
	public static String  postLogOutRequest(String url,int userId,String appToken,int appType) {
		InputStream inputStream = null;
		String result = "";
		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), CommonConstraints.TIMEOUT_MILLISEC);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Cookie",cookieID );
			if(CommonValues.getInstance().connectionMode=="Local"){
			httpPost.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			}
			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("Id",userId);
			jsonObject.accumulate("AppToken", appToken);
			jsonObject.accumulate("AppType", appType);
			
			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}
		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
//				jObject.getBoolean("Success");
				
				CommonValues.getInstance().logoutResponse=jObject.getBoolean("Success");
				}

			catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
			
	}
		// 11. return result
		return result;
	}
	
	

	public static String getRoomRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray roomArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				roomArray = jObject.getJSONArray("Data");
				int lengthofArray=roomArray.length();
				ArrayList<Room> roomCount = new ArrayList<Room>();

				for (int i = 0; i <lengthofArray ; i++) {
					Room room = new Room();
					room.setName((roomArray.getJSONObject(i)).getString("Name"));
					room.setId(roomArray.getJSONObject(i).getInt("Id"));
					roomCount.add(room);
				}

				if (CommonValues.getInstance().roomList.size() > 0) {
					CommonValues.getInstance().roomList.clear();
				}
				CommonValues.getInstance().roomList.addAll(roomCount);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static String getDevicesRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray deviceArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				deviceArray = jObject.getJSONArray("Data");
				int lengthofArray=deviceArray.length();
				ArrayList<Device> deviceCount = new ArrayList<Device>();

				for (int i = 0; i < lengthofArray; i++) {
					Device device = new Device();
					device.setName((deviceArray.getJSONObject(i))
							.getString("Name"));
					device.setId(deviceArray.getJSONObject(i).getInt("Id"));
					device.setRoomName(deviceArray.getJSONObject(i).getString(
							"RoomName"));
					device.setDeviceTypeId(deviceArray.getJSONObject(i).getInt(
							"DeviceTypeId"));
					device.setIsOn(deviceArray.getJSONObject(i).getBoolean(
							"IsOn"));
					device.setIsActionPending(deviceArray.getJSONObject(i).getBoolean("IsActionPending"));
					deviceCount.add(device);
				}

				if (CommonValues.getInstance().deviceList.size() > 0) {
					CommonValues.getInstance().deviceList.clear();
				}
				CommonValues.getInstance().deviceList.addAll(deviceCount);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static String getDevicePropertyRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray devicePropertyArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				devicePropertyArray = jObject.getJSONArray("Data");
				int lengthofArray=devicePropertyArray.length();
				ArrayList<DeviceProperty> devicePropertyCount = new ArrayList<DeviceProperty>();

				for (int i = 0; i < lengthofArray; i++) {
					DeviceProperty deviceProperty = new DeviceProperty();
				
					deviceProperty.setDeviceId(devicePropertyArray
							.getJSONObject(i).getInt("DeviceId"));
					deviceProperty.setPropertyId(devicePropertyArray
							.getJSONObject(i).getInt("PropertyId"));
					deviceProperty.setValue(devicePropertyArray
							.getJSONObject(i).getString("Value"));
					deviceProperty.setPendingValue(devicePropertyArray
							.getJSONObject(i).getString("PendingValue"));
					deviceProperty.setIsActionPending(devicePropertyArray
							.getJSONObject(i).getBoolean("IsActionPending"));
					devicePropertyCount.add(deviceProperty);
				}

				 if (CommonValues.getInstance().devicePropertyList.size() > 0) {
				 CommonValues.getInstance().devicePropertyList.clear();
				 }
				 CommonValues.getInstance().devicePropertyList.addAll(devicePropertyCount);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}
	public static String postUserLogRequest(String url,int filterType,String fromDate,String toDate) {
		InputStream is = null;
		String result = "";
		JSONArray userLogArray = null;
//		DefaultHttpClient httpClient = new DefaultHttpClient();

		try{
			HttpClient httpclient = new DefaultHttpClient();
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					35000);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), 35000);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Cookie",cookieID );
			if(CommonValues.getInstance().connectionMode=="Local"){
			httpPost.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			}
			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("FilterType", filterType);
			jsonObject.accumulate("StartDateTime", "/Date("+new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(fromDate).getTime()+")/");
			jsonObject.accumulate("EndDateTime", "/Date("+new SimpleDateFormat ( "yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(toDate).getTime()+")/");
			
			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// 9. receive response as inputStream
			is = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (is != null) {
				result = convertInputStreamToString(is);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				userLogArray = jObject.getJSONArray("Data");
				int lengthofArray=userLogArray.length();
				ArrayList<DevicePropertyLog> userLogCount = new ArrayList<DevicePropertyLog>();

				for (int i = 0; i < lengthofArray; i++) {
					DevicePropertyLog devicePropertyLog = new DevicePropertyLog();
					devicePropertyLog.setUserId(userLogArray.getJSONObject(i)
							.getInt("UserId"));
					devicePropertyLog.setDevicePropertyId(userLogArray
							.getJSONObject(i).getInt("PropertyId"));

					String results = userLogArray.getJSONObject(i)
							.getString("LoggedAt").replaceAll("^/Date\\(", "");

					results = results.substring(0, results.indexOf('+'));
					Long timeInMillis = Long.valueOf(results);
					Date LoggedAt = new Date(timeInMillis);

					devicePropertyLog.setLoggedAt(LoggedAt);

					devicePropertyLog.setUserName(userLogArray.getJSONObject(
							i).getString("UserName"));
					devicePropertyLog.setDeviceName(userLogArray
							.getJSONObject(i).getString("DeviceName"));
					devicePropertyLog.setPropertyId(userLogArray
							.getJSONObject(i).getInt("PropertyId"));
					devicePropertyLog.setPropertyName(userLogArray
							.getJSONObject(i).getString("PropertyName"));
					devicePropertyLog.setValue(userLogArray
							.getJSONObject(i).getString("Value"));
					userLogCount.add(devicePropertyLog);
				}

				if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
					CommonValues.getInstance().deviceLogDetailList.clear();
				}
				CommonValues.getInstance().deviceLogDetailList
						.addAll(userLogCount);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static String postDeviceLogRequest(String url,int filterType,String fromDate,String toDate) {
		InputStream is = null;
		String result = "";
		JSONArray deviceLogArray = null;
//		DefaultHttpClient httpClient = new DefaultHttpClient();

		try{
			HttpClient httpclient = new DefaultHttpClient();
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					35000);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), 35000);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Cookie",cookieID );
			if(CommonValues.getInstance().connectionMode=="Local"){
			httpPost.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			}
			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("FilterType", filterType);
			jsonObject.accumulate("StartDateTime", "/Date("+new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(fromDate).getTime()+")/");
			jsonObject.accumulate("EndDateTime", "/Date("+new SimpleDateFormat ( "yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(toDate).getTime()+")/");
			
			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// 9. receive response as inputStream
			is = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (is != null) {
				result = convertInputStreamToString(is);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				deviceLogArray = jObject.getJSONArray("Data");
				int lengthofArray=deviceLogArray.length();
				ArrayList<DevicePropertyLog> deviceLogCount = new ArrayList<DevicePropertyLog>();

				for (int i = 0; i < lengthofArray; i++) {
					DevicePropertyLog devicePropertyLog = new DevicePropertyLog();
					devicePropertyLog.setUserId(deviceLogArray.getJSONObject(i)
							.getInt("UserId"));
					devicePropertyLog.setDevicePropertyId(deviceLogArray
							.getJSONObject(i).getInt("PropertyId"));

					String results = deviceLogArray.getJSONObject(i)
							.getString("LoggedAt").replaceAll("^/Date\\(", "");

					results = results.substring(0, results.indexOf('+'));
					Long timeInMillis = Long.valueOf(results);
					Date LoggedAt = new Date(timeInMillis);

					devicePropertyLog.setLoggedAt(LoggedAt);

					devicePropertyLog.setUserName(deviceLogArray.getJSONObject(
							i).getString("UserName"));
					devicePropertyLog.setDeviceName(deviceLogArray
							.getJSONObject(i).getString("DeviceName"));
					devicePropertyLog.setPropertyId(deviceLogArray
							.getJSONObject(i).getInt("PropertyId"));
					devicePropertyLog.setPropertyName(deviceLogArray
							.getJSONObject(i).getString("PropertyName"));
					devicePropertyLog.setValue(deviceLogArray
							.getJSONObject(i).getString("Value"));
					deviceLogCount.add(devicePropertyLog);
				}

				if (CommonValues.getInstance().deviceLogDetailList.size() > 0) {
					CommonValues.getInstance().deviceLogDetailList.clear();
				}
				CommonValues.getInstance().deviceLogDetailList
						.addAll(deviceLogCount);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static Object getUserProfileRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jUserProfile = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {

				JSONObject jObject = null;
				jObject = new JSONObject(result);
				jUserProfile = jObject.getJSONObject("Data");
				// jUserProfile = new JSONObject(result);

				UserProfile userProfile = new UserProfile();
				userProfile.setFirstName(jUserProfile.getString("FirstName"));
				userProfile.setLastName(jUserProfile.getString("LastName"));
				userProfile.setMiddleName(jUserProfile.getString("MiddleName"));
				
				userProfile.setUserName(jUserProfile.getString("UserName"));
				userProfile.setEmail(jUserProfile.getString("Email"));
				userProfile.setSex(jUserProfile.getString("Sex"));
				String results = jUserProfile
						.getString("DateOfBirth").replaceAll("^/Date\\(", "");

				results = results.substring(0, results.indexOf('+'));
				Long timeInMillis = Long.valueOf(results);
				Date dateOfBirth = new Date(timeInMillis);
				userProfile.setDateOfBirth(dateOfBirth);
				
				userProfile.setCellPhone(jUserProfile.getString("CellPhone"));
				
				userProfile.setSocialSecurityNumber(jUserProfile
						.getString("SocialSecurityNumber"));

				Address address = new Address();
				JSONObject addressJson = new JSONObject();
				if(!jUserProfile.isNull("Address")){
				addressJson = jUserProfile.getJSONObject("Address");
				address.setAddress1(addressJson.getString("Address1"));
				address.setAddress2(addressJson.getString("Address2"));
				userProfile.setAddress(address);
				}

//				City city = new City();
//				JSONObject cityJson = new JSONObject();
//				cityJson = addressJson.getJSONObject("City");
//				city.setCountry(cityJson.getString("Country"));
//				city.setName(cityJson.getString("Name"));
//				city.setState(cityJson.getString("State"));
//				userProfile.getAddress().setCity(city);

				CommonValues.getInstance().profile = userProfile;

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}
		return result;
	}

	public static String  setStatusRequest(String url) {
		InputStream is = null;
		String result = "";
//		JSONArray devicePropertyArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				jData = jObject.getJSONObject("Data");
				Device device = new Device();
				
				device.setName(jData.getString("Name"));
				device.setId(jData.getInt("Id"));
				device.setRoomName(jData.getString("RoomName"));
				device.setDeviceTypeId(jData.getInt("DeviceTypeId"));
				device.setIsOn(jData.getBoolean("IsOn"));
				device.setIsActionPending(jData.getBoolean("IsActionPending"));

				
				 CommonValues.getInstance().modifiedDeviceStatus=device;

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}
	
	public static String  setProptyerRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray devicePropertyArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				devicePropertyArray = jObject.getJSONArray("Data");
//				DeviceProperty deviceProperty = new DeviceProperty();
				ArrayList<DeviceProperty> devicePropertyValues = new ArrayList<DeviceProperty>();
				int arrayLength=devicePropertyArray.length();

				for (int i = 0; i < arrayLength; i++) {
					DeviceProperty deviceProperty = new DeviceProperty();
				
					deviceProperty.setDeviceId(devicePropertyArray
							.getJSONObject(i).getInt("DeviceId"));
					deviceProperty.setPropertyId(devicePropertyArray
							.getJSONObject(i).getInt("PropertyId"));
					deviceProperty.setValue(devicePropertyArray
							.getJSONObject(i).getString("Value"));
					deviceProperty.setPendingValue(devicePropertyArray
							.getJSONObject(i).getString("PendingValue"));
					deviceProperty.setIsActionPending(devicePropertyArray
							.getJSONObject(i).getBoolean("IsActionPending"));
					devicePropertyValues.add(deviceProperty);
				}

				 if (CommonValues.getInstance().devicePropertyList.size() > 0) {
				 CommonValues.getInstance().devicePropertyList.clear();
				 }
				 CommonValues.getInstance().devicePropertyList.addAll(devicePropertyValues);
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}
	
	public static String  setPresetRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray curtainPresetArray = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				curtainPresetArray = jObject.getJSONArray("Data");
				int arrayLength=curtainPresetArray.length();
//				DeviceProperty deviceProperty = new DeviceProperty();
				ArrayList<Preset> curtainPresetValues = new ArrayList<Preset>();

				for (int i = 0; i < arrayLength; i++) {
					Preset presetProperty = new Preset();
				
					presetProperty.setDescription(curtainPresetArray
							.getJSONObject(i).getString("Description"));
					presetProperty.setId(curtainPresetArray
							.getJSONObject(i).getInt("Id"));
					presetProperty.setValue(curtainPresetArray
							.getJSONObject(i).getInt("Value"));
					presetProperty.setDisplayName(curtainPresetArray
							.getJSONObject(i).getString("DisplayName"));
					presetProperty.setName(curtainPresetArray
							.getJSONObject(i).getString("Name"));
					curtainPresetValues.add(presetProperty);
				}

				 if (CommonValues.getInstance().presetList.size() > 0) {
				 CommonValues.getInstance().presetList.clear();
				 }
				 CommonValues.getInstance().presetList.addAll(curtainPresetValues);
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static String postChangePassRequest(String url,String currentPass,String newPass) {
		InputStream inputStream = null;
		String result = "";
		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), CommonConstraints.TIMEOUT_MILLISEC);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Cookie",cookieID );
			if(CommonValues.getInstance().connectionMode=="Local"){
			httpPost.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			}
			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("Password",currentPass);
			jsonObject.accumulate("NewPassword", newPass);

			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}

		if (result != null && !result.equals("")) {
			JSONObject jObject = null;
			try {
				jObject = new JSONObject(result);
				if(jObject.getString("Data").equals("true")){
					SharedPreferences password = UserLogin.context.getSharedPreferences(CommonConstraints.PREF_PASSWORD_KEY, Context.MODE_PRIVATE);
					password.edit().remove(CommonConstraints.PREF_PASSWORD_KEY).commit();
					CommonTask.SavePreferences(UserLogin.context, CommonConstraints.PREF_LOGINUSER_NAME,
							CommonConstraints.PREF_PASSWORD_KEY, newPass);
				}else{
					CommonValues.getInstance().IsServerConnectionError = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonValues.getInstance().IsServerConnectionError = true;
			}
			
	}
		// 11. return result
		return result;
	}
	
	public static String sendHttpGetRequest(String url, InputStream is,
			String result, DefaultHttpClient httpClient) {
		try {
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), CommonConstraints.TIMEOUT_MILLISEC);
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Cookie",cookieID );
			if(CommonValues.getInstance().connectionMode=="Local"){
			httpGet.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			}
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			if (is != null) {
				result = convertInputStreamToString(is);
			} else {
				result = "Did not work!";
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, "UTF-8"), 8);
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public static String getcameraInfoRequest(String url) {
		InputStream is = null;
		String result = "";
//		JSONObject jCameraInfo = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);

		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				jData = jObject.getJSONObject("Data");
				HomeLink homeLink=new HomeLink();
				homeLink.setIpAddress(jData.getString("IPAddress"));
				homeLink.setPort(Integer.parseInt(jData.getString("Port")));
				homeLink.setUserName(jData.getString("UserName"));
				homeLink.setPassword(jData.getString("Password"));
				homeLink.setChannelCount(Integer.parseInt(jData.getString("ChannelCount")));
			
				CommonValues.getInstance().cameraInfo=homeLink;	
				}
				
				 
		 catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}

		return result;

	}

	public static String getTicketTypeRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray ticketTypeArray = null;
//		JSONObject jCameraInfo = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);
		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				ticketTypeArray = jObject.getJSONArray("Data");
				int arrayLength=ticketTypeArray.length();
				ArrayList<TicketType> tTypeArrayValues = new ArrayList<TicketType>();
				for (int i = 0; i < arrayLength; i++) {
					TicketType tType = new TicketType();
				
					tType.setId(ticketTypeArray.getJSONObject(i).getInt("Id"));
					tType.setTypeName(ticketTypeArray.getJSONObject(i).getString("TypeName"));
					tTypeArrayValues.add(tType);
				}
				 if (CommonValues.getInstance().ticketTypeList.size() > 0) {
					 CommonValues.getInstance().ticketTypeList.clear();
					 }
					 CommonValues.getInstance().ticketTypeList.addAll(tTypeArrayValues);
				
			} 
		 catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}
		return result;
}
	
	public static String postCreateTicketRequest(String url,String Subject,String Details,Integer TicketTypeId) {
		InputStream inputStream = null;
		String result = "";
		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
//			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					CommonConstraints.TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), CommonConstraints.TIMEOUT_MILLISEC);
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
//			httpPost.addHeader("Cookie",cookieID );
//			httpPost.addHeader("ApiKey", CommonValues.getInstance().ApiKey);
			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("Subject",Subject);
			jsonObject.accumulate("Details", Details);
			jsonObject.accumulate("TicketTypeId", TicketTypeId);

			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set headers to inform server about the type of the content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}
			if (result.equals("")) {
				CommonValues.getInstance().IsServerConnectionError = true;
			} else {
				CommonValues.getInstance().IsServerConnectionError = false;
			}

		} catch (Exception e) {
			e.getMessage();
			CommonValues.getInstance().IsServerConnectionError = true;
		}

		if (result != null && !result.equals("")) {
			JSONObject jObject = null;
			try {
				jObject = new JSONObject(result);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonValues.getInstance().IsServerConnectionError = true;
			}
			
	}
		// 11. return result
		return result;
	}
	
	public static String getAllTicketRequest(String url) {
		InputStream is = null;
		String result = "";
		JSONArray allTicketArray = null;
//		JSONObject jCameraInfo = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);
		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				allTicketArray = jObject.getJSONArray("Data");
				int arrayLength=allTicketArray.length();
				ArrayList<Ticket> allTicketArrayValues = new ArrayList<Ticket>();
				
				for (int i = 0; i < arrayLength; i++) {
					Ticket allTicket = new Ticket();
				
					allTicket.setId(allTicketArray.getJSONObject(i).getInt("Id"));
					allTicket.setSubject(allTicketArray.getJSONObject(i).getString("Subject"));
					
					String results = allTicketArray.getJSONObject(i)
							.getString("SubmissionDate").replaceAll("^/Date\\(", "");
					results = results.substring(0, results.indexOf('+'));
					Long timeInMillis = Long.valueOf(results);
					Date LoggedAt = new Date(timeInMillis);
					allTicket.setSubmissionDate(LoggedAt);
					allTicket.setStatus(allTicketArray.getJSONObject(i).getString("Status"));
					allTicketArrayValues.add(allTicket);
				}
				 if (CommonValues.getInstance().allTicketList.size() > 0) {
					 CommonValues.getInstance().allTicketList.clear();
					 }
					 CommonValues.getInstance().allTicketList.addAll(allTicketArrayValues);
				
			} 
		 catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}
		return result;
}
	
	public static String getViewTicketRequest(String url) {
		InputStream is = null;
		String result = "";
//		Ticket singleTicket = null;
//		JSONObject jCameraInfo = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		result = sendHttpGetRequest(url, is, result, httpClient);
		if (result != null && !result.equals("")) {
			try {
				JSONObject jObject = null;
				jObject = new JSONObject(result);
				JSONObject singleTicketObj = jObject.getJSONObject("Data");
//				ArrayList<Ticket> singleTicketArrayValues = new ArrayList<Ticket>();
				
				
					Ticket ticket = new Ticket();
				
					ticket.setId(singleTicketObj.getInt("Id"));
					ticket.setSubject(singleTicketObj.getString("Subject"));
					
					String results = singleTicketObj
							.getString("SubmissionDate").replaceAll("^/Date\\(", "");
					results = results.substring(0, results.indexOf('+'));
					Long timeInMillis = Long.valueOf(results);
					Date LoggedAt = new Date(timeInMillis);
					ticket.setSubmissionDate(LoggedAt);
					ticket.setStatus(singleTicketObj.getString("Status"));
					ticket.setDetails(singleTicketObj.getString("Details"));
					CommonValues.getInstance().singleTicket=ticket;
				 
				
			} 
		 catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
			}
		}
		return result;
}
	
	
}
