package com.sinepulse.app.utils;

/**
 * Singleton Class
 * 
 * @author use for initializing common URL values
 */

public class CommonURL {	
//	http://192.168.11.151/api/
	//http://dev.sinepulse.com/smarthome/service/dev/DataAPI.svc/data/
	public String remoteBaseUrl="http://dev.sinepulse.com/smarthome/service/dev/DataAPI.svc/";
//	public String baseUrl="http://dev.sinepulse.com/smarthome/service/dev/DataAPI.svc/data/";
//	public String LoginCustomerURL=baseUrl	+ "login";
//	public String GetCommonURL=baseUrl	+ "user";
	public String LoginCustomerURL="";
	public String GetCommonURL="";
	public String LogOutURL="";
	public String RootUrl="";

	static CommonURL commonURLInstance;

	/**
	 * Return Instance
	 * 
	 * @return
	 */
	public static CommonURL getInstance() {		
		return commonURLInstance;
	}

	/**
	 * Create instance
	 */
	public static void initializeInstance() {
		if (commonURLInstance == null)
			commonURLInstance = new CommonURL();
	}

	// Constructor hidden because of singleton
	private CommonURL() {

	}

	public void assignValues(String baseUrl) {
		  RootUrl=baseUrl;
		 LoginCustomerURL = baseUrl	+ "login";
		 GetCommonURL = baseUrl	+ "user";
		 LogOutURL=baseUrl + "logout";
		
	}
}
