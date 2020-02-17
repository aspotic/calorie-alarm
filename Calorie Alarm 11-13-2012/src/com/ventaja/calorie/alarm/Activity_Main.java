package com.ventaja.calorie.alarm;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class Activity_Main extends FragmentActivity {
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //// Setup ActionBar
        // ActionBar gets initiated
        ActionBar actionbar = getActionBar();
        
        // Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // initiating both tabs and set text to it.
        ActionBar.Tab counterTab = actionbar.newTab().setText(R.string.nav_counter);
        ActionBar.Tab logTab = actionbar.newTab().setText(R.string.nav_charts);
        ActionBar.Tab historyTab = actionbar.newTab().setText(R.string.nav_history);
        ActionBar.Tab alarmsTab = actionbar.newTab().setText(R.string.nav_alarms);
 
        // create the two fragments we want to use for display content
        Fragment counterFragment = new Tab_Counter();
        Fragment logFragment = new Tab_Charts();
        Fragment historyFragment = new Tab_History();
        Fragment alarmsFragment = new Tab_Alarms();
 
        // set the Tab listener. Now we can listen for clicks.
        counterTab.setTabListener(new MyTabsListener(counterFragment));
        logTab.setTabListener(new MyTabsListener(logFragment));
        historyTab.setTabListener(new MyTabsListener(historyFragment));
        alarmsTab.setTabListener(new MyTabsListener(alarmsFragment));
 
        // add the tabs to the action bar
        actionbar.addTab(counterTab);
        actionbar.addTab(logTab);
        actionbar.addTab(historyTab);
        actionbar.addTab(alarmsTab);
        
        //// create an alarm to update log, and calorie count at the start of each day
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//This is the intent that is launched when the alarm goes off.
        Intent intent = new Intent(this, BroadcastReceiver_UpdateLog.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
		// Make sure the alarm goes off at the end of the day
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND,59);
		calendar.set(Calendar.MILLISECOND, 999);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
		
		
		
		
		
/*
		// Store calorie count in history file
		FileOutputStream fos;
		try {
			fos = openFileOutput("count_history", Context.MODE_APPEND);
			fos.write((
				"2012,10,1,1000;" +
				"2012,10,2,2000;" +
				"2012,10,3,1600;" +
				"2012,10,4,1700;" +
				"2012,10,5,1850;" +
				"2012,10,6,2400;" +
				"2012,10,7,3000;" +
				"2012,10,8,1400;" +
				"2012,10,9,2000;" +
				"2012,10,10,1800;" +
				"2012,10,11,1750;" +
				"2012,10,12,1600;" +
				"2012,10,13,1650;" +
				"2012,10,14,2450;" +
				"2012,10,15,1200;" +
				"2012,10,16,1700;" +
				"2012,10,17,1900;"
				).getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
    }




    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_counter, menu);
        return true;
    }
    
}