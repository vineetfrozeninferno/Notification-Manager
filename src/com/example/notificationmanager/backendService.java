package com.example.notificationmanager;



import java.util.ArrayList;
import java.util.List;

import trigger.triggerFramework;
import alert.alert;
import alert.alertFramework;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import event.eventBaseType;
import event.eventFramework;
import event.GPSEvent.GPSEvent;
import event.alarmEvent.alarmEvent;

public class backendService extends Service {
	
	public static Context backendServiceContext = null;
	public static Context frontEndContext = null;
	
	public backendService()
	{
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		//no interface defined as yet - not sure if it will be.. :P
		return null;
	}

	@Override
    public void onCreate()
	{
        initAppBackendCode();
        backendServiceContext = this.getApplicationContext();
    }
	
	//needs to be called once to initialize the code
	public void initAppBackendCode()
	{
		//register all the TAUs into the trigger framework - should be done as the TAUs are loaded
		triggerFramework.registerTriggerAcquisitionUnit(new trigger.clockTAU.clockTAU());
		triggerFramework.registerTriggerAcquisitionUnit(new trigger.LocationTAU.LocationTAU());

		List<eventBaseType> initList = new ArrayList<eventBaseType>();
		initList.add(new alarmEvent());
		initList.add(new GPSEvent());

		//initialize the event framework
		eventFramework.init(initList);
	}

    @Override
    public void onStart(Intent intent, int startId)
    {
    	//start the framework
		triggerFramework.startFramework();
    }

    @Override
    public void onDestroy()
    {
    	alert deathbedStatment = new alert();
    	deathbedStatment.alertingText = "Either you or Android has seen it fit to terminate iForget.. I will forget to alert you till you re-activate me.. GoodBye..";
    	alertFramework.displayNotifications(deathbedStatment);
    	triggerFramework.killThread = true;
    }
}

