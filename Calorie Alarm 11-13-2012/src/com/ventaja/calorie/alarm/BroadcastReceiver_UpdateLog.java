package com.ventaja.calorie.alarm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.Toast;

public class BroadcastReceiver_UpdateLog extends BroadcastReceiver {
	final String LOG_FILE = "count_history";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Create a message to the user
		Toast.makeText(context, "NEW DAY", Toast.LENGTH_LONG).show();

		// Vibrate the mobile phone
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
		
		
		
		
		
		// Get calorie count
		SharedPreferences settings = context.getSharedPreferences("CalorieAlarmPrefs", 0);
		int calorieCount = settings.getInt("CalorieCount", 0);
		
		// Store calorie count in history file
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(LOG_FILE, Context.MODE_APPEND);
			fos.write(
				(
					String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) 			+ "," + 
					String.valueOf(Calendar.getInstance().get(Calendar.MONTH)) 			+ "," +
					String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) 	+ "," + 
					String.valueOf(calorieCount)										+ ";"
				).getBytes()
			);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Reset calorie count
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("CalorieCount", 0);
		editor.apply();
	}
} 