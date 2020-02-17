package com.ventaja.calorie.alarm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;


public class Tab_Alarms extends Fragment implements TimePickerDialog.OnTimeSetListener {
	boolean called = false;		//******Hack variable for working around google's api error with running onTimeSet twice when done is pressed and once when back is pressed
	Tab_Alarms window = this;
	final String ALARMS_FILE = "alarm_list";
	LinearLayout alarmsList;
	int nextAlarmId = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//// Create the view first so it can be used
		View view = inflater.inflate(R.layout.tab_alarms, container, false);
		
		alarmsList = (LinearLayout) view.findViewById(R.id.alarmListLayout);
		
		
		// Get the next alarm id from persistent storage
		SharedPreferences settings = getActivity().getSharedPreferences("CalorieAlarmPrefs", 0);
		nextAlarmId = settings.getInt("NextAlarmID", 1);
		
		
		//// Set up the button used to create a new alarm
		Button newAlarm = (Button) view.findViewById(R.id.newAlarmButton);
		newAlarm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				called = false;
				// Create the time picker dialog and show it
				new TimePickerDialog(getActivity(), window, 0, 0, false).show();
			}
		});
	     
		
		//// Add existing alarms
		// Get the alarms from storage
    	String[] alarmList = getAlarmList();
    	
    	// Display the alarms if there are any
    	if (alarmList != null) {
	    	for(int i = 0; i < alarmList.length; i++) {
	    		int hr = Integer.parseInt(alarmList[i].split(",")[0]);
	    		int min = Integer.parseInt(alarmList[i].split(",")[1]);
	    		int alarmId = Integer.parseInt(alarmList[i].split(",")[2]);
	    		showAlarm(hr, min, alarmId);
	    	}
    	}
		
		return view;
	}
	
	
	

	public void onTimeSet(TimePicker view, int hour, int minute) {
		
		if (called && !alarmExists(hour, minute)) {
			//// Save the new alarm to persistent storage
			// Create the string to append
			String basicTime = String.valueOf(hour) + "," + String.valueOf(minute) + "," + String.valueOf(nextAlarmId) + ";";
			// Save it to the alarm list
			FileOutputStream fos;
			try {
				fos = getActivity().openFileOutput(ALARMS_FILE, Context.MODE_APPEND);
				fos.write(basicTime.getBytes());
				fos.close();

				//// Display the new alarm
				showAlarm(hour, minute, nextAlarmId);
				startAlarm(hour, minute, nextAlarmId);
				nextAlarmId++;	// New alarm added, so make sure a unique ID is available for the next alarm

				
				SharedPreferences.Editor editor = getActivity().getSharedPreferences("CalorieAlarmPrefs", 0).edit();
				editor
					.putInt("NextAlarmID", nextAlarmId)
					.apply();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		called = true;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	private void showAlarm(final int hour, final int minute, final int alarmId) {		
		// Create the label with 12 hour clock format
		TextView newAlarm = new TextView(getActivity());			
		newAlarm.setTextSize(25);
		newAlarm.setText(createTimeString(hour, minute));
		
		// Create the button that gets rid of the alarm
		Button deleteAlarm = new Button(getActivity());	
		deleteAlarm.setText("X");

		// Create the layout that holds the label and delete button
		final RelativeLayout alarmBar = new RelativeLayout(getActivity());
		
		// Add the new alarm label to the layout
	    RelativeLayout.LayoutParams newAlarmParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    newAlarmParams.setMargins(2, 9, 0, 0);
		newAlarmParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    newAlarmParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    alarmBar.addView(newAlarm, newAlarmParams);
		
	    // Add the new alarm's delete button to the layout
	    RelativeLayout.LayoutParams removeAlarmParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    removeAlarmParams.setMargins(0, 0, 2, 0);
	    removeAlarmParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    removeAlarmParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    alarmBar.addView(deleteAlarm, removeAlarmParams);
	    
	    // Get the alarm list and add the new alarm to it
		alarmsList.addView(alarmBar);
		
		// Create a listener for the delete button
		deleteAlarm.setOnClickListener(new OnClickListener () {
			public void onClick(View view) {
				// Delete the alarm from the list of alarms
				alarmBar.setVisibility(RelativeLayout.GONE);
				
				// Remove the alarm from persistent storage
				String basicTime = "";
				String lookFor = String.valueOf(hour) + "," + String.valueOf(minute) + "," + alarmId;
				String[] alarmList = getAlarmList();
				if (alarmList != null) {
					for (int i = 0; i < alarmList.length; i++) {
						if (!alarmList[i].equals(lookFor)) {
							basicTime += alarmList[i] + ";";
						}
					}
				
					// Write the new list to memory
					FileOutputStream fos;
					try {
						fos = getActivity().openFileOutput(ALARMS_FILE, Context.MODE_PRIVATE);
						fos.write(basicTime.getBytes());
						fos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Remove the alarm from the alarm service
			        Intent intent = new Intent(getActivity(), BroadcastReceiver_Alarm.class);
			        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), alarmId, intent, 0);
					AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
					am.cancel(pendingIntent);
				}
			}
		});
	}
	
	
	private void startAlarm(int hour, int minute, int alarmId) {		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		
		//This is the intent that is launched when the alarm goes off.
        Intent intent = new Intent(getActivity(), BroadcastReceiver_Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), alarmId, intent, 0);
		

		// Make sure the alarm goes off in the future instead of immediately
		if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
			am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() + 86400000 , AlarmManager.INTERVAL_DAY, pendingIntent);
		} else {
			am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() , AlarmManager.INTERVAL_DAY, pendingIntent);
		}
	}
	
	 
	

	
	
	private String[] getAlarmList() {
	    String[] alarmList = null;
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(ALARMS_FILE)));
		    String inputString;
		    if ((inputString = inputReader.readLine()) != null) {
		    	alarmList = inputString.split(";");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return alarmList;
	}
	
	
	

	
	
	private boolean alarmExists(int hour, int minute) {
		String[] alarmList = getAlarmList();
		if (alarmList != null) {
			for (int i = 0; i < alarmList.length; i++) {
				if ((Integer.parseInt(alarmList[i].split(",")[0]) == hour) && (Integer.parseInt(alarmList[i].split(",")[1]) == minute)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	
	String createTimeString(int hour, int minute) {
		String alarmText = "";
		if (hour == 0) {
			alarmText += "12";
		} else if (hour > 12) {
			alarmText += String.valueOf(hour - 12);
		} else {
			alarmText += String.valueOf(hour);
		}
		alarmText += ":";
		
		if (minute < 10) {
			alarmText += "0";
		}
		alarmText += String.valueOf(minute);

		if (hour < 12) {
			alarmText += (" AM");
		} else {
			alarmText += (" PM");
		}
		return alarmText;
	}
}