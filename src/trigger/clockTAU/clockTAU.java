package trigger.clockTAU;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import trigger.triggerAcquisitionUnit;
import trigger.triggerMinutes;
import trigger.triggerType;

/*
 * sample TAU for reference.
 * This code can be hardware specific or OS specific. It will not affect the working of the trigger framework
 * This sample code creates a TAU that listens to the system clock every minute and generates the trigger to send out to the event framework
 * It also registers the kind of trigger it generates - dateTimeTriggerType 
 */

public class clockTAU extends triggerAcquisitionUnit 
{
	@Override
	public triggerMinutes wakeMeUpEveryThMinuteOfTheHour()
	{
		//register the return type
		dateTimeTriggerType dttt = new dateTimeTriggerType();
		triggerType.registerTriggerType(dttt);
		
		return triggerMinutes.One;
	}

	@Override
	public List<triggerType> acquireTrigger()
	{
		List<triggerType> retList = new ArrayList<triggerType>();
		
		//get system time
		Calendar c = Calendar.getInstance(TimeZone.getDefault());
		
		//set the trigger data to be returned
		dateTimeTriggerType retTrig = new dateTimeTriggerType();
		retTrig.year = c.get(c.YEAR);
		retTrig.month = c.get(c.MONTH) + 1;	//java uses a non-intuitive 0-based month numbering sys.. Jan would return 0. This changes it to Jan = 1;
		retTrig.day = c.get(c.DAY_OF_MONTH);
		retTrig.dayOfTheWeek = c.get(c.DAY_OF_WEEK);
		retTrig.hour = c.get(c.HOUR_OF_DAY);
		retTrig.minute = c.get(c.MINUTE);
		
		//add the trigger to the list
		retList.add(retTrig);
		
		return retList;
	}

	@Override
	public String name()
	{
		return "ClockTAU";
	}
	
}
