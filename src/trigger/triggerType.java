package trigger;

import java.util.ArrayList;
import java.util.List;

/*
 *This class must be overriden by custom triggerTypes.
 *It holds the data that the custom TAU will want to send to the event it is listening to.
 *Careful to ensure that you do not create repeated TAUs and triggerTypes instead of listening to existing ones. 
 */

public abstract class triggerType
{
	public static List<triggerType> listOfTriggerTypes = new ArrayList<triggerType>();
	
	/*add the details/data and functions
	 * that represents the trigger here
	 */

	
	/* Use this function to register a new trigger type with the framework.
	 * The eventFramework will create a list for all events listening to this trigger.
	 */
	public static Boolean registerTriggerType(triggerType trigger)
	{
		//No two triggers must have the same name
		for(triggerType t : listOfTriggerTypes)
		{
			if(t.nameOfTrigger().equalsIgnoreCase(trigger.nameOfTrigger()))
			{
				return false;
			}
		}
			
		return listOfTriggerTypes.add(trigger);
	}
	
	//override to return name of the trigger
	public abstract String nameOfTrigger();
	
	@Override
	public boolean equals(Object obj)
	{	
		triggerType other = (triggerType) obj;
		
		if(other == null)
			return false;
		
		if(other.nameOfTrigger().equalsIgnoreCase(this.nameOfTrigger()))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return this.nameOfTrigger().hashCode();
	}
}
