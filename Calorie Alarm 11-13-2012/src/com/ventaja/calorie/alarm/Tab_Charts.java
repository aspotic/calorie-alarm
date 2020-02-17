package com.ventaja.calorie.alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.R.color;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Tab_Charts extends Fragment {
	final String LOG_FILE = "count_history";
	final int PLOT_LENGTH = 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.tab_charts, container, false);
    	final LineGraphView graphView = new LineGraphView(getActivity(), "");

    	GraphViewData[] graphData = new GraphViewData[PLOT_LENGTH];
    	String[] calorieLog = getCalorieLog();
    	if (calorieLog != null) {
    		
    		// If the number of log entries are greater than the number of points to show, then shift index to show latest entries
    		int startIndex = 0;
    		if (calorieLog.length > PLOT_LENGTH) {
    			startIndex = calorieLog.length - PLOT_LENGTH;
    		}
    		
    		// Add all the log entries
    		for (int i = startIndex; i < calorieLog.length; i++) {
    			graphData[i - startIndex] = new GraphViewData(i, Double.parseDouble(calorieLog[i].split(",")[3]));
    		}
        	
    		// Show the plot
    		//graphView.setViewPort(0, 7);
    		graphView.setHorizontalScrollBarEnabled(true);
    		graphView.setScrollable(true);
    		graphView.setBackgroundColor(color.holo_blue_dark);
    		graphView.setDrawBackground(true);
    		graphView.addSeries(new GraphViewSeries(graphData));
    		
    		LinearLayout layout = (LinearLayout) view.findViewById(R.id.chartsGraph);
        	layout.addView(graphView);
    	}
    	

		//// Set up the button used to select day view
		Button setDays = (Button) view.findViewById(R.id.daysButton);
		setDays.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});
    	

		//// Set up the button used to select week view
		Button setWeeks = (Button) view.findViewById(R.id.weeksButton);
		setWeeks.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
    	

		//// Set up the button used to select month view
		Button setMonths = (Button) view.findViewById(R.id.monthsButton);
		setMonths.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
    	
        return view;
    }
    
    

	private String[] getCalorieLog() {
	    String[] alarmList = null;
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(LOG_FILE)));
		    String inputString;
		    if ((inputString = inputReader.readLine()) != null) {
		    	alarmList = inputString.split(";");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return alarmList;
	}
}