package event.alarmEvent;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import trigger.triggerType;
import trigger.clockTAU.dateTimeTriggerType;
import alert.alert;
import event.eventBaseType;


public class alarmEvent extends eventBaseType
{
	public int hour, min, year, month, day;
	public String name, alarmAlertString;
	
	public alarmEvent()
	{
		//default values
		year = month = day = hour = min = -1;
	}
	
	@Override
	public List<triggerType> triggersInterestedIn()
	{
		List<triggerType> tmp = new ArrayList<triggerType>();
		trigger.clockTAU.dateTimeTriggerType t = new dateTimeTriggerType();
		tmp.add(t);
		return tmp;
	}

	@Override
	public String IDOfEvent()
	{
		return name;
	}

	@Override
	public Boolean doesItInterestMe(triggerType t)
	{
		//ensure that the trigger is of type trigger.clockTAU.dateTimeTriggerType
		if(!t.getClass().toString().equalsIgnoreCase(trigger.clockTAU.dateTimeTriggerType.class.toString()))
		{
			return false;
		}
		
		trigger.clockTAU.dateTimeTriggerType tmpTrig = (trigger.clockTAU.dateTimeTriggerType)t;
		
		
		if(tmpTrig.hour == hour && tmpTrig.minute == min)
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
			
			trigger.clockTAU.dateTimeTriggerType tmp = (dateTimeTriggerType) t;
			
			a.title = "Alarm";
			if(this.alarmAlertString.isEmpty() == false)
				a.alertingText = this.alarmAlertString;
			else
				a.alertingText = "ThE AlArM HaS GoNe OfF!! " + tmp.hour +":" + tmp.minute + "\n";
				
			retList.add(a);
		}
		
		return retList;
	}

	@Override
	public Node serializeToXML()
	{
		return eventBaseType.basicSerializer(this);
		/*
		Document doc;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e)
		{
			return null;
		}
		
		Element retNode = doc.createElement("event");
		retNode.setAttribute("type", this.getClass().getName());
		
		//event id data
		Element name = doc.createElement("property");
		name.setAttribute("name", "name");
		name.appendChild(doc.createTextNode(this.IDOfEvent()));
		retNode.appendChild(name);
		
		//year data
		Element yr = doc.createElement("property");
		yr.setAttribute("name", "year");
		yr.appendChild(doc.createTextNode(Integer.toString(this.year)));
		retNode.appendChild(yr);
		
		//month data
		Element mth = doc.createElement("property");
		mth.setAttribute("name", "month");
		mth.appendChild(doc.createTextNode(Integer.toString(this.month)));
		retNode.appendChild(mth);
		
		//date data
		Element d = doc.createElement("property");
		d.setAttribute("name", "day");
		d.appendChild(doc.createTextNode(Integer.toString(this.day)));
		retNode.appendChild(d);
		
		//hour data
		Element h = doc.createElement("property");
		h.setAttribute("name", "hour");
		h.appendChild(doc.createTextNode(Integer.toString(this.hour)));
		retNode.appendChild(h);
		
		//minute data
		Element m = doc.createElement("property");
		m.setAttribute("name", "min");
		m.appendChild(doc.createTextNode(Integer.toString(this.min)));
		retNode.appendChild(m);
		
		return retNode;
		*/
	}
	
	@Override
	public void buildFromXML(Node XMLData)
	{
		eventBaseType.basicDeserializer(XMLData, this);
		
		/*
		//get the children nodes.. they are the properties..
		NodeList propertyList = XMLData.getChildNodes();

		for(int i = 0; i < propertyList.getLength(); i++)
		{
			String propertyName = propertyList.item(i).getAttributes().getNamedItem("name").getNodeValue();
			String tmp = propertyList.item(i).getChildNodes().item(0).getNodeValue();

			if(propertyName.equals("name"))
				this.name = tmp;
			else
			{
				int propertyValue = Integer.parseInt(tmp);
				
				if(propertyName.equals("year")) this.year = propertyValue;
				else if(propertyName.equals("month")) this.month = propertyValue;
				else if(propertyName.equals("day")) this.day = propertyValue;
				else if(propertyName.equals("hour")) this.hour = propertyValue;
				else if(propertyName.equals("min")) this.min = propertyValue;
			}
		}*/
	}
}
