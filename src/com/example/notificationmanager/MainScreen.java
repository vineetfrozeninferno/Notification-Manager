package com.example.notificationmanager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import event.eventFramework;
import event.GPSEvent.GPSEvent;
import frontendFunctions.AlarmPickFrontEnd;

public class MainScreen extends Activity
{
	public static TextView time, date;
	public static ListView listView;

	public static List<String> listOfAlarms = new ArrayList<String>();

	/*@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//start service - we can add a stopBackendService too as an enhancement later..
		startService(new Intent(this, backendService.class));
		backendService.frontEndContext = this;

		//should be listed by the event framework
		listOfAlarms.add("Time-Based Alarm");
		listOfAlarms.add("Location-Based Alarm");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, android.R.id.text1, listOfAlarms);

		listView = (ListView) findViewById(R.id.listOfAlarms);
		listView.setAdapter(adapter); 

		listView.setOnItemClickListener(new alarmSelectAction());

		//		alarmButton = (Button)findViewById(R.id.btnSelect);
		//		LocationButton = (Button)findViewById(R.id.btnSelectLocation);

		time = (TextView)findViewById(R.id.textTime);
		date = (TextView)findViewById(R.id.textDate);

		TabHost tabHost=(TabHost)findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Choose Alarms");
		spec1.setContent(R.id.tabChooseAlarm);
		spec1.setIndicator("Choose Alarms");

		TabSpec spec2=tabHost.newTabSpec("Chosen Alarms");
		spec2.setContent(R.id.tabChosenAlarms);
		spec2.setIndicator("Chosen Alarms");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
	}

	//Function called when the button is clicked
	public void createNotification(View view) {

	}
}

class alarmSelectAction implements OnItemClickListener
{
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		String  alarmType = (String) MainScreen.listView.getItemAtPosition(position);

		if(alarmType.equalsIgnoreCase("Time-Based Alarm"))
			timeBasedAlarm(view);

		if(alarmType.equalsIgnoreCase("Location-Based Alarm"))
			locationBasedAlarm(view);			
	}

	public void timeBasedAlarm(View view)
	{
		Intent pickerScreen;
		Activity act = (Activity) view.getContext();

		pickerScreen = new Intent(view.getContext(), AlarmPickFrontEnd.class);
		act.startActivity(pickerScreen);
	}

	public void locationBasedAlarm(View view)
	{
		GPSEvent gps = new GPSEvent();
		gps.latitude = 12.982195;
		gps.longitude = 77.638615;
		DecimalFormat df = new DecimalFormat("###.##");
		gps.latitude = Double.valueOf(df.format(gps.latitude));
		gps.longitude = Double.valueOf(df.format(gps.longitude));

		eventFramework.registerEvent(gps);
	}
}