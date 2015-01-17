package trigger.clockTAU;

import trigger.triggerType;

public class dateTimeTriggerType extends triggerType
{
	public int day, month, year, hour, minute, dayOfTheWeek;
	
	@Override
	public String nameOfTrigger() 
	{
		return "dateTimeTriggerType";
	}
}
