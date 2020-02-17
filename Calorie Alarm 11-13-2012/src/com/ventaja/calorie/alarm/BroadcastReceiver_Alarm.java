package com.ventaja.calorie.alarm;

import java.io.IOException;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class BroadcastReceiver_Alarm extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// Create a message to the user
		Toast.makeText(context, "Time to update calorie count", Toast.LENGTH_LONG).show();

		// Vibrate the mobile phone
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
		
		// Play alarm sound
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		MediaPlayer mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, soundUri);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mMediaPlayer.setLooping(false);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Bring up calorie alarm counter page
		Intent openCalorieAlarmActivity = new Intent(context, Activity_Main.class);
	    openCalorieAlarmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(openCalorieAlarmActivity);    
	}
} 