package alert;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.notificationmanager.MainScreen;
import com.example.notificationmanager.NotificationReceiverActivity;
import com.example.notificationmanager.R;
import com.example.notificationmanager.backendService;


public class alertFramework implements Runnable
{
	//the queue of incoming requests
	public static Queue<alert> alertQueue = new LinkedList<alert>();
	public static int counter=0;

	//status of the event thread
	public static boolean active = false;

	public alertFramework(){

	}

	public static void addAlertToQueue(alert t)
	{
		synchronized (alertQueue)
		{
			alertQueue.add(t);
		}

		//if the thread isn't running - get it running
		if(active == false)
		{
			Thread alertFrameworkThread = new Thread(new alertFramework());
			alertFrameworkThread.start();
		}
	}

	public static void addAlertsToQueue(List<alert> t)
	{
		synchronized (alertQueue)
		{
			alertQueue.addAll(t);
		}
		//if the thread isn't running - get it running
		if(active == false)
		{
			Thread alertFrameworkThread = new Thread(new alertFramework());
			alertFrameworkThread.start();
		}
	}

	public static void startThread()
	{
		active = true;

		while(!alertQueue.isEmpty())
		{
			synchronized (alertQueue)
			{
				//get the first trigger in the queue
				alert a = alertQueue.remove();
				displayNotifications(a);
			}

			//display logic - just pushing it to the console for now
			//System.out.println(a.alertingText);

		}

		//the triggerQueue is empty
		active = false;
	}

	public static void displayNotifications(alert a)
	{
		final alert notification = a;
		//Call to the UI thread to display the notification
		((MainScreen) backendService.frontEndContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				outputText(notification);
			}
		});
	}

	@Override
	public void run()
	{
		startThread();
	}

	//Handle the creation of the notifications, right now handles only one notification it will update the old notification
	public static void outputText(alert a) {
		Intent intent = new Intent(backendService.frontEndContext, NotificationReceiverActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(backendService.frontEndContext, 0, intent, 0);

		Builder notificationBuilder = new Notification.Builder(backendService.frontEndContext);
		notificationBuilder.setContentTitle(a.title);
		notificationBuilder.setContentText(a.alertingText).setSmallIcon(R.drawable.ic_launcher);

		Notification noti = notificationBuilder.setContentIntent(pIntent).build();
		NotificationManager notificationManager = (NotificationManager) backendService.frontEndContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		//hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(++counter, noti);

	}
}