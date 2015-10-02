/**
 * 
 */
package com.sinepulse.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author tanvir.ahmed
 *
 */
public class NetworkUtil {
    
   public static int TYPE_WIFI = 1;
   public static int TYPE_MOBILE = 2;
   public static int TYPE_NOT_CONNECTED = 0;
   public static int TYPE_BOTH_CONNECTED = 3;
    
    
   public static int getConnectivityStatus(Context context) {
       ConnectivityManager cm = (ConnectivityManager) context
               .getSystemService(Context.CONNECTIVITY_SERVICE);

       NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
       if (null != activeNetwork) {
           if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI 
        		   )
               return TYPE_WIFI;
            
           if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE  
        		 )
               return TYPE_MOBILE;
           if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE  
             && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI 
        		   )
               return TYPE_BOTH_CONNECTED;
           
       } 
       return TYPE_NOT_CONNECTED;
   }
   
   /*public static String getHostName(String defValue) {
	    try {
	        Method getString = Build.class.getDeclaredMethod("getString", String.class);
	        getString.setAccessible(true);
	        return getString.invoke(null, "net.hostname").toString();
	    } catch (Exception ex) {
	        return defValue;
	    }
	}*/
    
   public static String getConnectivityStatusString(Context context) {
       int conn = NetworkUtil.getConnectivityStatus(context);
       String status = null;
       if (conn == NetworkUtil.TYPE_WIFI) {
           status = "Wifi enabled";
       } else if (conn == NetworkUtil.TYPE_MOBILE) {
           status = "Mobiledata enabled";
       } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
           status = "Not connected to Internet";
       }else if (conn == NetworkUtil.TYPE_BOTH_CONNECTED) {
           status = "Both Enabled";
       }
       return status;
   }
}
