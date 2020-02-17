package com.ventaja.calorie.alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Tab_History extends Fragment {
	final String LOG_FILE = "count_history";
	final int PLOT_LENGTH = 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	//// Create the view first so it can be used
    	View view = inflater.inflate(R.layout.tab_history, container, false);    	
   		LinearLayout historyList = (LinearLayout) view.findViewById(R.id.historyListLayout);
   		
   		
   		
   		//// Add history list
   		// Get the history from storage
       	String[] historyArray = getHistoryList();
       	
       	// Display the history if there is one
       	if (historyArray != null) {
   	    	for(int i = 0; i < historyArray.length; i++) {
   	    		historyList.addView(null);
   	    	}
       	}
   		
   		return view;
    }
	
	 
	

	
	
	private String[] getHistoryList() {
	    String[] historyList = null;
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(LOG_FILE)));
		    String inputString;
		    if ((inputString = inputReader.readLine()) != null) {
		    	historyList = inputString.split(";");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return historyList;
	}
}