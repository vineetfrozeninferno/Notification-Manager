package trigger.LocationTAU;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import trigger.triggerAcquisitionUnit;
import trigger.triggerMinutes;
import trigger.triggerType;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.notificationmanager.MainScreen;
import com.example.notificationmanager.backendService;

public class LocationTAU extends triggerAcquisitionUnit implements LocationListener
{
	private LocationManager locationManager=null;
	//private static CreateNotificationActivity MainActivityInstance;

	public LocationTAU (){

	}

	//public LocationTAU(CreateNotificationActivity create){
	//	MainActivityInstance = create;
	//}

	@Override
	public triggerMinutes wakeMeUpEveryThMinuteOfTheHour()
	{
		//register the return type
		LatLongTriggerType lltt = new LatLongTriggerType();
		triggerType.registerTriggerType(lltt);

		return triggerMinutes.One;
	}

	@Override
	public List<triggerType> acquireTrigger()
	{
		LatLongTriggerType retTrig = new LatLongTriggerType();
		List<triggerType> retList = new ArrayList<triggerType>();

		if(backendService.backendServiceContext != null)
		{
			locationManager = (LocationManager) backendService.backendServiceContext.getSystemService(MainScreen.LOCATION_SERVICE);
			//set the trigger data to be returned
			Criteria criteria = new Criteria();
			Object provider = locationManager.getBestProvider(criteria, false);
			Location location = locationManager.getLastKnownLocation((String) provider);
			if (location != null)
			{
				System.out.println("Provider " + provider + " has been selected.");
				//onLocationChanged(location);
				DecimalFormat df = new DecimalFormat("###.##");
				retTrig.latitude = Double.valueOf(df.format(location.getLatitude()));
				retTrig.longitude = Double.valueOf(df.format(location.getLongitude()));
				//add the trigger to the list
				retList.add(retTrig);
			}
		}
		return retList;
	}

	@Override
	public String name()
	{
		return "LocationTAU";
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//retTrig.latitude = location.getLatitude();
		//retTrig.longitude = location.getLongitude();
		//DecimalFormat df = new DecimalFormat("###.##");
		//retTrig.latitude = Double.valueOf(df.format(location.getLatitude()));
		//retTrig.longitude = Double.valueOf(df.format(location.getLongitude()));
		//add the trigger to the list
		//retList.add(retTrig);

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}

