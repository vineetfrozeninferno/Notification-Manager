package trigger;

/*
 * Enumeration to ensure that the number of intervals that the TAU can use are limited..
 * Add any new  intervals here, if you feel they are justified.
 * Reason being, creating 60 lists with all possible triggerMinute would lead to too many lists and may
 * make the system inefficient.
 */

public enum triggerMinutes
{
	One(1), Five(5), Ten(10), Fifteen(15), Twenty(20), Thirty(30), Forty(35), FortyFive(45), Fifty(50);
	
	private int value;
	
	private triggerMinutes(int val)
	{
		value = val;
	}
	
	public int getValue()
	{
		return value;
	}
}
