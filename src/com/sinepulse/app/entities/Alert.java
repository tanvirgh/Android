/**
 * 
 */
package com.sinepulse.app.entities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * @author tanvir.ahmed
 *
 */
public class Alert {
	
	public String  title;
	public String type;
	public int code;
	public String details;
	
	
	public static Alert setAlartData(JSONObject jsonobj){
		Alert alert=new Alert();
		try {
			alert.title=jsonobj.getString("Title");
			alert.type=jsonobj.getString("Type");
			alert.code=jsonobj.getInt("Code");
			alert.details=jsonobj.getString("Description");
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alert;
		
	}
	
	public static Alert setCustomAlertData(int alertCode,String alertType){
		Map<Integer,String> customAlertMap=initCustomAlertMap();
		Alert alert=new Alert();
		alert.code=alertCode;
		alert.type=alertType;
		alert.title=getCustomAlertTitle(alertCode);
		alert.details=customAlertMap.get(alertCode);
		return alert;
	}
	 
	private static String getCustomAlertTitle(int alertCode){
		int hundredsDigit=(int)alertCode/100;
		String title = "";
		switch (hundredsDigit) {
		
		case 3:
			title="Server Response";
			break;
		case 4:
			title="Server Connection";
			break;
		case 5:
			title="Application";
			break;

		default:
			break;
		}
		
		
		
		return title;
		
	}
	
	
	
	private static Map<Integer,String> initCustomAlertMap(){
		Map<Integer,String> customAlertMap=new HashMap<Integer, String>();
		customAlertMap.put(310, "Server returned empty JSON");
		customAlertMap.put(311, "Server returned empty data");
		customAlertMap.put(330, "Server returned empty response");
		customAlertMap.put(320, "Parsing error");
		customAlertMap.put(400, "No Connection");
		customAlertMap.put(410, "Mc and GSB unreachable");
		customAlertMap.put(420, "Connection Timeout");
		customAlertMap.put(510, "Camera streaming failed");
		customAlertMap.put(511, "Ip/Port is not accessible for camera feed");
		customAlertMap.put(520, "Internal app data parsing failed");
		return customAlertMap;
		
	}
	
	
	
	

}
