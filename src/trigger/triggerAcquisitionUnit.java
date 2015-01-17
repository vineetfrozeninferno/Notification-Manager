package trigger;

import java.util.List;

/*
 * This class acts as the parent class to be inherited by all TAUs created.
 * It defines the basic functions that the framework expects the TAU to have.
 * This allows the developer to create a TAU to interact with the hardware/OS in any way it wishes
 * and return a bunch of triggers.
 * The TriggerFramework doesn't care about how the interaction happens with the hardware or the OS, making it (OS/hardware)-agnostic
 */

public abstract class triggerAcquisitionUnit
{
	//function returning the interval after which the triggerAcquisition must be woken
	public abstract triggerMinutes wakeMeUpEveryThMinuteOfTheHour();
		
	//The code that is called periodically to acquire the triggers
	public abstract List<triggerType> acquireTrigger();
	
	//The name of the triggerAcquisitionUnit
	public abstract String name();
	
	//equals function for hashmap
	@Override
	public boolean equals(Object obj)
	{
		if( obj == null || obj.getClass() != this.getClass() )
			return false;
		else
			return ((triggerAcquisitionUnit)obj).name().equalsIgnoreCase(this.name());
	}
	
	@Override
	public int hashCode() {
		return this.name().hashCode();
	}
	
}
