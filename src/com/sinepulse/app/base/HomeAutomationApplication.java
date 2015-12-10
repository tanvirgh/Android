package com.sinepulse.app.base;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


import com.sinepulse.app.utils.CommonURL;
import com.sinepulse.app.utils.CommonValues;

import android.app.Application;

/**
 * Automatically call at the application startup
 * Add this class reference as application class at AndroidManifest.xml file 
 * Basically this class use for initialize the global use of classes and variable    
 * @author tac
 *
 */
@ReportsCrashes(formKey = "", mailTo = "tanvir.ahmed@aplombtechbd.com", mode = ReportingInteractionMode.SILENT)
//												resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
//												resDialogText = R.string.crash_dialog_text,
//												resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
//												resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
//												resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
//												resDialogOkToast = R.string.crash_dialog_ok_toast )// optional. displays a Toast message when the user accepts to send a report.
public class HomeAutomationApplication extends Application{

	@Override
	public void onCreate() {		
		super.onCreate();
		// Create instances so that we can use whole over the application
		initializeCommonInstance();	
		// The following line triggers the initialization of ACRA
        ACRA.init(this);
	}

	// Initialize Instances
	private void initializeCommonInstance() {
		//if server address is already not defined we are putting the default values in there.
//		if(CommonTask.getBaseUrl(this)==null || CommonTask.getBaseUrl(this).equals("")){
//			CommonTask.SavePreferences(this, CommonConstraints.PREF_SETTINGS_NAME,
//					CommonConstraints.PREF_URL_KEY, this.getString(R.string.serveraddress));
//			CommonTask.SavePreferences(this, CommonConstraints.PREF_SETTINGS_NAME,
//					CommonConstraints.PREF_SHOPNUMBER_KEY, this.getString(R.string.servershopnumber));
//		}
		
		CommonValues.initializeInstance();
		CommonURL.initializeInstance();
		
		
	}
	

}
