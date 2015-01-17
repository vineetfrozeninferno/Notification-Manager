package trigger.LocationTAU;

import trigger.triggerType;

public class LatLongTriggerType extends triggerType
{
	public double latitude,longitude;
	
	@Override
	public String nameOfTrigger() 
	{
		return "LatLongTriggerType";
	}
}
