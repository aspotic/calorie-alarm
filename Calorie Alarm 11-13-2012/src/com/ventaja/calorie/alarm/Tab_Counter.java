package com.ventaja.calorie.alarm;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class Tab_Counter extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//// Create the view for this fragment. Needs to be done here so widgets can be accessed in this method
		View view = inflater.inflate(R.layout.tab_counter, container, false);


		//// Get stored calorie count from persistent storage and display it
		SharedPreferences settings = getActivity().getSharedPreferences("CalorieAlarmPrefs", 0);   	 
		TextView calorieCountLabel = (TextView) view.findViewById(R.id.calorieCountLabel);
		calorieCountLabel.setText(String.valueOf(settings.getInt("CalorieCount", 0)));

		
		//// Put the values into the calorie quantity spinner
		final Spinner spinner = (Spinner) view.findViewById(R.id.numCaloriesSpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.calorie_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);


		//// Set the action for the add calories button (make it increment the # of calories)
		Button addCaloriesButton = (Button) view.findViewById(R.id.incrementCaloriesButton);
		addCaloriesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Get the current calorie count from persistent storage
				SharedPreferences settings = getActivity().getSharedPreferences("CalorieAlarmPrefs", 0);
				int initialCalories = settings.getInt("CalorieCount", 0);
				// Set the new calorie count
				int newCalories = initialCalories + Integer.parseInt(spinner.getSelectedItem().toString());
				// Save the new calorie count to persistent storage
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("CalorieCount", newCalories);
				editor.apply();
				// Display the new calorie count
				TextView calorieCountLabel = (TextView) getActivity().findViewById(R.id.calorieCountLabel);
				calorieCountLabel.setText(String.valueOf(newCalories));
			}
		});
	     
	     

		//// Set the action for the add calories button (make it increment the # of calories)
		Button subtractCaloriesButton = (Button) view.findViewById(R.id.decrementCountButton);
		subtractCaloriesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Get the current calorie count from persistent storage
				SharedPreferences settings = getActivity().getSharedPreferences("CalorieAlarmPrefs", 0);
				int initialCalories = settings.getInt("CalorieCount", 0);
				// Set the new calorie count
				int newCalories = initialCalories - Integer.parseInt(spinner.getSelectedItem().toString());
				// Save the new calorie count to persistent storage
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("CalorieCount", newCalories);
				editor.apply();
				// Display the new calorie count
				TextView calorieCountLabel = (TextView) getActivity().findViewById(R.id.calorieCountLabel);
				calorieCountLabel.setText(String.valueOf(newCalories));
			}
		});
		
    	
        return view;
    }
}