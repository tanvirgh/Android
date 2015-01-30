/**
 * 
 */
package com.sinepulse.app.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author tanvir.ahmed
 *
 */
public final class CommonGcmValues {
	
	 /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "815025480747";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
   public  static void displayMessage(Context context, String message) {
    	Log.e("GCM",message);

        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
