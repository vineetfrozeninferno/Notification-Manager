package event;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import trigger.triggerType;
import alert.alertFramework;

public class eventFramework implements Runnable
{
	public static HashMap<triggerType, List<String>> triggerEventMap = new HashMap<triggerType, List<String>>();
	public static HashMap<String, eventBaseType> MapOfEventIDsAndEvents = new HashMap<String, eventBaseType>();
	
	public static List<Class> ListOfAllPossibleEventTypes = new ArrayList<Class>();
	
	//the queue of incoming requests
	public static Queue<triggerType> triggerQueue = new LinkedList<triggerType>();
	
	//status of the event thread
	public static boolean active = false;
	
	
	public static void init(List<eventBaseType> allPossibleEventTypes)
	{
		for(eventBaseType e : allPossibleEventTypes)
		{
			//check if the list of eventTypes has this entry
			Boolean alreadyExists = false;
			for(Class cls : ListOfAllPossibleEventTypes)
			{
				if(e.getClass().getName().equals(cls.getName()))
				{
					alreadyExists = true;
					break;
				}
			}
			
			if(alreadyExists == false)
				ListOfAllPossibleEventTypes.add(e.getClass());
		}
	}
	
	public static void registerEvent(eventBaseType e)
	{
		MapOfEventIDsAndEvents.put(e.IDOfEvent(), e);
		
		for(triggerType t : e.triggersInterestedIn())
		{
			if(triggerEventMap.containsKey(t) && !triggerEventMap.get(t).isEmpty())
			{
				List<String> tmp = triggerEventMap.get(t);
				tmp.add(e.IDOfEvent());
				triggerEventMap.put(t, tmp);
			}
			else
			{
				List<String> tmp = new ArrayList<String>();
				tmp.add(e.IDOfEvent());
				triggerEventMap.put(t, tmp);
			}
		}
	}
	
	public static void deregisterEvent(String NameOfEvent)
	{
		if(MapOfEventIDsAndEvents.containsKey(NameOfEvent) == false)
		{
			return;
		}
		
		//the event exists - remove it from the list
		List<triggerType> triggersTheEventWasInterestedIn = MapOfEventIDsAndEvents.get(NameOfEvent).triggersInterestedIn();
		MapOfEventIDsAndEvents.remove(NameOfEvent);
		
		//remove the event from the triggerEventMap
		for(triggerType t : triggersTheEventWasInterestedIn)
		{
			List<String> tmp = triggerEventMap.get(t);
			tmp.remove(NameOfEvent);
			triggerEventMap.put(t, tmp);
		}
			
	}
	
	//add trigger to queue and start the event framework thread if it hasn't started.
	public static void addTriggerToQueue(triggerType t)
	{
		synchronized (triggerQueue)
		{
			triggerQueue.add(t);
		}
		
		//if the thread isn't running - get it running
		if(active == false)
		{
			Thread eventFrameworkThread = new Thread(new eventFramework());
			eventFrameworkThread.start();
		}
	}
	
	public static void addTriggersToQueue(List<triggerType> t)
	{
		synchronized (triggerQueue)
		{
			triggerQueue.addAll(t);
		}
		
		//if the thread isn't running - get it running
		if(active == false)
		{
			Thread eventFrameworkThread = new Thread(new eventFramework());
			eventFrameworkThread.start();
		}
	}
	
	public static void startThread()
	{
		active = true;
		
		while(!triggerQueue.isEmpty())
		{
			synchronized (triggerQueue)
			{
				//get the first trigger in the queue
				triggerType t = triggerQueue.remove();
				if(triggerEventMap.containsKey(t) && !triggerEventMap.get(t).isEmpty())
				{
					for(String eventName : triggerEventMap.get(t))
					{
						eventBaseType e = MapOfEventIDsAndEvents.get(eventName);
						//is the event interested in the trigger
						if(e.doesItInterestMe(t))
						{
							//let the event do its stuff
							alertFramework.addAlertsToQueue(e.generateAlerts(t));
						}
					}
				}
			}
			
			//check if there are events registered for that trigger
			
		}
		
		//the triggerQueue is empty
		active = false;
	}

	@Override
	public void run()
	{
		startThread();
	}
	
	public static String createXMLDocOfAllEvents()
	{
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			return "";
		}
		
		Node root = doc.createElement("listOfEvents");

		for(eventBaseType event : MapOfEventIDsAndEvents.values())
		{
			Node tmpNode = doc.importNode(event.serializeToXML(), true);
			root.appendChild(tmpNode);
		}
		
		doc.appendChild(root);

		StringWriter sw = new StringWriter();
		try
		{
			Transformer t = TransformerFactory.newInstance().newTransformer();
			Source src = new DOMSource(doc);
			Result res = new StreamResult(sw);
			t.transform(src, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sw.toString(); 
	}
	
	public static void populateEventsFromXMLString(String XMLString)
	{
		try
		{
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(XMLString)));

			Node root = doc.getDocumentElement();
			NodeList listOfEvents = root.getChildNodes();

			for( int i = 0; i < listOfEvents.getLength(); i++ )
			{
				Node event = listOfEvents.item(i);
				String eventClass = event.getAttributes().getNamedItem("type").getNodeValue();
				
				for(Class cls: ListOfAllPossibleEventTypes)
				{
					if(eventClass.equals(cls.getName()))
					{
						eventBaseType tmp = (eventBaseType) cls.newInstance();
						tmp.buildFromXML(event);
						eventFramework.registerEvent(tmp);
						break;
					}
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
