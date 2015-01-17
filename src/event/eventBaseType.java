package event;

import java.lang.reflect.Field;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import trigger.triggerType;
import alert.alert;

public abstract class eventBaseType
{	
	//the triggerTypes that the event must register itself to listen to
	public abstract List<triggerType> triggersInterestedIn();
	
	//the unique name/ID identifying the event
	public abstract String IDOfEvent();
	
	//function called to check if the event is interested in further analysis of the trigger or alerting the user
	public abstract Boolean doesItInterestMe(triggerType t);
	
	//the function that further checks the trigger and generates a list of alerts as required
	public abstract List<alert> generateAlerts(triggerType t);
	
	public abstract Node serializeToXML();
	
	//function to serialize the event details that are of primitive type or strings into an xml
	public static Node basicSerializer(eventBaseType event)
	{
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
		retNode.setAttribute("type", event.getClass().getName());
		
		for(Field f : event.getClass().getDeclaredFields())
		{
			if(f.getType().isPrimitive() || f.getType().equals(String.class))
			{
				try
				{
				Element tmp = doc.createElement("property");
				tmp.setAttribute("name", f.getName());
				tmp.appendChild(doc.createTextNode(f.get(event).toString()));
				retNode.appendChild(tmp);
				}
				catch(Exception e)
				{}
			}
		}
		return retNode;
	}
	
	//function to build event from the xml
	public abstract void buildFromXML(Node xmlData);
	
	public static eventBaseType basicDeserializer(Node xmlData, eventBaseType ebt)
	{
		eventBaseType retEvent = null;
		if(ebt == null)
			ebt = retEvent;
		
		String type = xmlData.getAttributes().getNamedItem("type").getNodeValue();
		try
		{
			retEvent = (eventBaseType) Class.forName(type).getConstructor().newInstance();


			NodeList propertyList = xmlData.getChildNodes();

			for(int i = 0; i < propertyList.getLength(); i++)
			{
				String propertyName = propertyList.item(i).getAttributes().getNamedItem("name").getNodeValue();
				String tmp = propertyList.item(i).getChildNodes().item(0).getNodeValue();

				Field f = Class.forName(type).getDeclaredField(propertyName);
				
				if(f.getType().equals(int.class)) 			{ f.set(retEvent, Integer.parseInt(tmp));		f.set(ebt, Integer.parseInt(tmp));		}
				else if(f.getType().equals(String.class))	{ f.set(retEvent, tmp);							f.set(ebt, tmp); 						}
				else if(f.getType().equals(boolean.class))	{ f.set(retEvent, Boolean.parseBoolean(tmp)); 	f.set(ebt, Boolean.parseBoolean(tmp));	}
				else if(f.getType().equals(byte.class))		{ f.set(retEvent, Byte.parseByte(tmp)); 		f.set(ebt, Byte.parseByte(tmp)); 		}
				else if(f.getType().equals(char.class))		{ f.set(retEvent, tmp.charAt(0)); 				f.set(ebt, tmp.charAt(0)); 				}
				else if(f.getType().equals(short.class))	{ f.set(retEvent, Short.parseShort(tmp)); 		f.set(ebt, Short.parseShort(tmp)); 		}
				else if(f.getType().equals(long.class))		{ f.set(retEvent, Long.parseLong(tmp)); 		f.set(ebt, Long.parseLong(tmp)); 		}
				else if(f.getType().equals(float.class))	{ f.set(retEvent, Float.parseFloat(tmp)); 		f.set(ebt, Float.parseFloat(tmp)); 		}
				else if(f.getType().equals(double.class))	{ f.set(retEvent, Double.parseDouble(tmp)); 	f.set(ebt, Double.parseDouble(tmp)); 	}
			}
		}
		catch(Exception e) {}
		
		return retEvent;
	}
	
}