package com.sinepulse.app.utils;

import java.util.Locale;

public class CommonConstraints {

	public static final int NO_EXCEPTION = -1;
	public static final int GENERAL_EXCEPTION = 0;
	public static final int CLIENT_PROTOCOL_EXCEPTION=1;
	public static final int ILLEGAL_STATE_EXCEPTION=2;
	public static final int UNSUPPORTED_ENCODING_EXCEPTION=3;
	public static final int IO_EXCEPTION=4;
	public static final int NO_INTERNET_AVAILABLE = 5;
	
	public static final String EncodingCode = "ISO-8859-1";
	public static final String ClientId = "0";
	// Initialize local date & time Zone
	public static final Locale Locale = java.util.Locale.US;
	// Initialize date & time format
	public static final int DateFormat = java.text.DateFormat.SHORT;
	public static final String GET="GET";
	public static final String POST="POST";
	public static final String PREF_SETTINGS_NAME ="settings"; 
	public static final String PREF_LOGINUSER_NAME="loginuser";
	public static final String PREF_URL_KEY="url";
	public static final String PREF_CUSTOMERID_KEY="customerid";
	public static final String PREF_LOCALIP_KEY="localip";
	public static final String PREF_FIRSTNAME_KEY="firstname";
	public static final String PREF_LASTNAME_KEY="lastname";
	public static final String PREF_EMAIL_KEY="email";
	public static final String PREF_TELEPHONE_KEY="telephone";
	public static final String PREF_ZIPCODE_KEY="zipcode";
	public static final String PREF_ADDRESS1_KEY="address1";
	public static final String PREF_ADDRESS2_KEY="address2";
	public static final String PREF_CITY_KEY="city";
	public static final String PREF_PASSWORD_KEY="password";
	public static final String PREF_BASEURLS_KEY="baseurls";
	public static final String PREF_SETTING ="Setting";
	public static final String PREF_CHECKOUT ="Checkout";
	public static final String PREF_CHECKOUT_KEY ="checkout_preference";
	public static final int TIMEOUT_MILLISEC = 3000; // = 3 seconds
	public static final int MAX_QUANTITY_FOR_SINGLE_ITEM = 99;
	
	
	

}
