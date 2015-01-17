package event.GPSEvent;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import trigger.triggerType;
import trigger.LocationTAU.LatLongTriggerType;
import alert.alert;
import event.eventBaseType;

public class GPSEvent extends eventBaseType
{
	public double latitude, longitude;
	public String name; 
	
		
	
	
	public GPSEvent()
	{
		//default values
		latitude = longitude = 0;
		
	}
	
	@Override
	public List<triggerType> triggersInterestedIn()
	{
		List<triggerType> temp = new ArrayList<triggerType>();
		LatLongTriggerType latlong = new LatLongTriggerType();
		temp.add(latlong);
		return temp;
	}

	@Override
	public String IDOfEvent()
	{
		return name;
	}

	@Override
	public Boolean doesItInterestMe(triggerType t)
	{
		//ensure that the trigger is of right type
		if(!t.getClass().toString().equalsIgnoreCase(trigger.LocationTAU.LatLongTriggerType.class.toString()))
		{
			return false;
		}
		
		LatLongTriggerType tmpTrig = (LatLongTriggerType)t;
		
		double diff = (tmpTrig.latitude - latitude)*(tmpTrig.latitude - latitude) - (tmpTrig.longitude - longitude)*(tmpTrig.longitude - longitude);
		diff = Math.sqrt(diff);
		double threshold = 0;
		
		if(diff < threshold)
		{
			return true;
		}
		else
		{
			return false;
		}
			
	}

	@Override
	public List<alert> generateAlerts(triggerType t)
	{
		List<alert> retList = new ArrayList<alert>();
		
		//just a cross check
		if(this.doesItInterestMe(t) == true)
		{
			alert a = new alert();
			
			LatLongTriggerType tmp = (LatLongTriggerType) t;
			
			a.alertingText = "You are at the location. \n Latitude: " + tmp.latitude +"\n Longitude:" + tmp.longitude + "\n";
			retList.add(a);
		}
		
		return retList;
	}

	@Override
	public Node serializeToXML()
	{
		return eventBaseType.basicSerializer(this);
		
	}
	
	@Override
	public void buildFromXML(Node XMLData)
	{
		eventBaseType.basicDeserializer(XMLData, this);
		
	}
}

