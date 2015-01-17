package frontendFunctions;

import com.example.notificationmanager.MainScreen;
import com.example.notificationmanager.R;

import event.eventFramework;
import event.alarmEvent.alarmEvent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmPickFrontEnd extends Activity{
	Button set;
	public static TimePicker timep;
	public static DatePicker datep;
	public static TextView alarmAlertString;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker_frag);
		datep = (DatePicker)findViewById(R.id.datePicker);
		timep = (TimePicker)findViewById(R.id.timePicker1);
		alarmAlertString = (EditText)findViewById(R.id.editText1);
		set = (Button)findViewById(R.id.btnSet);

		set.setOnClickListener(new datePickerFrontend());
	}

}

class datePickerFrontend implements OnClickListener
{
	Integer hour,minute,month,day,year;
	String alertString;

	@Override
	public void onClick(View view)
	{
		if(AlarmPickFrontEnd.datep != null && AlarmPickFrontEnd.timep != null)
		{
			month = AlarmPickFrontEnd.datep.getMonth();
			day = AlarmPickFrontEnd.datep.getDayOfMonth();
			year = AlarmPickFrontEnd.datep.getYear();
			hour = AlarmPickFrontEnd.timep.getCurrentHour();
			minute = AlarmPickFrontEnd.timep.getCurrentMinute();
			alertString = AlarmPickFrontEnd.alarmAlertString.getText().toString();
			MainScreen.time.setText("Time is "+hour+":" +minute);

			MainScreen.date.setText("The date is "+day+"/"+(month+1)+"/"+year);
			dateTimePicker(hour,minute,month,day,year,alertString);
			Activity act = (Activity)view.getContext();
			act.finish();
		}
	}

	public void dateTimePicker(Integer hour,Integer minute,Integer month,Integer day,Integer year, String alertString){
		//Passing an instance of the Main Activity
		//alertFramework alertframeworkObject= new alertFramework(this);
		alarmEvent ae = new alarmEvent();
		ae.name = "ae1"+hour+minute;
		ae.hour = hour;
		ae.min = minute;
		ae.month = month;
		ae.day = day;
		ae.year = year;
		ae.alarmAlertString = alertString;

		//register the events with the event framework - done as the events are created or are loaded from existing state
		eventFramework.registerEvent(ae);
	}
}