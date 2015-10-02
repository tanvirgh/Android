package com.sinepulse.app.utils;

import java.util.ArrayList;

import android.content.Intent;
import android.view.Menu;

import com.sinepulse.app.activities.Home_;
import com.sinepulse.app.activities.RoomManager_;
import com.sinepulse.app.activities.Wamp;
import com.sinepulse.app.entities.Alert;
import com.sinepulse.app.entities.Device;
import com.sinepulse.app.entities.DeviceProperty;
import com.sinepulse.app.entities.DevicePropertyLog;
import com.sinepulse.app.entities.HomeLink;
import com.sinepulse.app.entities.LogInInfo;
import com.sinepulse.app.entities.Preset;
import com.sinepulse.app.entities.Room;
import com.sinepulse.app.entities.Summary;
import com.sinepulse.app.entities.Ticket;
import com.sinepulse.app.entities.TicketType;
import com.sinepulse.app.entities.UserProfile;

/**
 * Singleton Class
 * use for initializing some common values used in application
 * @author 
 * 
 */

public class CommonValues {
	public  String ApiKeyGsb = "";
	public long start;
	public long end;
	public Wamp wamp=new Wamp();
	public boolean IsServerConnectionError = false;
	public boolean shouldPerformAutoLogin=false;
	public  boolean isAutologin=false;
	public boolean isappinBackground = false;
	public UserProfile profile = new UserProfile();
	public Summary summary = new Summary();
	public int userId =0;
	public String appToken="";
	public String localIp="";
	public String ApiKey="";
	public ArrayList<Room> roomList=new ArrayList<Room>();
	public ArrayList<Device> deviceList=new ArrayList<Device>();
	public ArrayList<DeviceProperty> devicePropertyList=new ArrayList<DeviceProperty>();
	public ArrayList<Preset> presetList=new ArrayList<Preset>();
	public Device modifiedDeviceStatus=new Device();
	public HomeLink cameraInfo=new HomeLink();
	public ArrayList<TicketType> ticketTypeList=new ArrayList<TicketType>();
	public ArrayList<Ticket> allTicketList=new ArrayList<Ticket>();
	public Ticket singleTicket=new Ticket();
	public Alert alertObj;
//	public TicketType ticketType=new TicketType();
	public RoomManager_ roomManager=new RoomManager_();
	public Home_ home=new Home_();
	public LogInInfo logInInfo=new LogInInfo();
	
	public ArrayList<DevicePropertyLog> deviceLogDetailList=new ArrayList<DevicePropertyLog>();
	public ArrayList<DevicePropertyLog> userLogDetailList=new ArrayList<DevicePropertyLog>();
	public Menu menuList = null;
	public Intent homeIntent = null;
	public Intent saveServerInfo = null;
	public int currentCameraIndex=1;
	public String currentAction="";
	public String previousAction="";
	
	
	public static boolean beforeJB=false; //before Jelly Bean
	//public Typeface applicationFont;
	
	public int ErrorCode = CommonConstraints.NO_EXCEPTION;

	static CommonValues commonValuesInstance;
	public  String ApiKeyLocal="";
	public  boolean isPasswordChanged=false;
	public boolean logoutResponse=false;
	public boolean shouldSendLogReq=true;
	public String connectionMode="";
	public String hostNameSuffix="";
	public String nsdResolvedIp;
	public int deviceTypeId;
	public int deviceIdFromHome;
	public com.actionbarsherlock.view.Menu globalMenu=null;
	public int roomId;
	public int deviceTypeIdRoom;
	public int deviceIdRoom;

	/**
	 * Return Instance
	 * @return
	 */
	public static CommonValues getInstance() {		
		return commonValuesInstance;
	}

	/**
	 * Create instance 
	 */
	public static void initializeInstance() {
		if (commonValuesInstance == null) {
			commonValuesInstance = new CommonValues();
//			checkApiVersion();
		}
	}
	// Constructor hidden because of singleton
	private CommonValues(){
		
	}
	

}
