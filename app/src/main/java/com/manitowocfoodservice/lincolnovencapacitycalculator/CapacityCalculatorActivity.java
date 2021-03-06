package com.manitowocfoodservice.lincolnovencapacitycalculator;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class CapacityCalculatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_capacity_calculator);

		Spinner spinner = (Spinner) findViewById(R.id.pan_type);
		ArrayAdapter<CharSequence> adapter;
		adapter = ArrayAdapter.createFromResource(this, R.array.pan_types,
				R.layout.layout_spinner);
		adapter.setDropDownViewResource(R.layout.layout_spinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		Resources r = getResources();
		Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
		ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, logo, r.getColor(R.color.primary));
		this.setTaskDescription(description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_capacity_calculator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_use_model:
				getChooseModelDialog().show();
				break;
			case R.id.action_clear:
				((EditText) findViewById(R.id.belt_width)).setText("");
				((EditText) findViewById(R.id.oven_capacity)).setText("");
				((EditText) findViewById(R.id.bake_time)).setText("");
				((EditText) findViewById(R.id.pan_diameter)).setText("");
				((EditText) findViewById(R.id.pan_length)).setText("");
				((EditText) findViewById(R.id.pan_width)).setText("");
				((Spinner) findViewById(R.id.pan_type)).setSelection(0);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		TextView diameterLabel = (TextView) findViewById(R.id.pan_diameter_label);
		EditText diameter = (EditText) findViewById(R.id.pan_diameter);

		TextView widthLabel = (TextView) findViewById(R.id.pan_width_label);
		EditText width = (EditText) findViewById(R.id.pan_width);

		TextView lengthLabel = (TextView) findViewById(R.id.pan_length_label);
		EditText length = (EditText) findViewById(R.id.pan_length);

		TextView diamUnit = (TextView) findViewById(R.id.unit_label_pan_diameter);
		TextView lenUnit = (TextView) findViewById(R.id.unit_label_pan_length);
		TextView widUnit = (TextView) findViewById(R.id.unit_label_pan_width);

		if (position == 1) { // Round pan
			diameter.setText("");
			diameter.setVisibility(View.VISIBLE);
			diameterLabel.setVisibility(View.VISIBLE);
			diamUnit.setVisibility(View.VISIBLE);

			widUnit.setVisibility(View.INVISIBLE);
			widthLabel.setVisibility(View.INVISIBLE);
			width.setVisibility(View.INVISIBLE);

			lenUnit.setVisibility(View.INVISIBLE);
			length.setVisibility(View.INVISIBLE);
			lengthLabel.setVisibility(View.INVISIBLE);
		} else if (position == 2) { // Rectangular pan
			diameter.setVisibility(View.INVISIBLE);
			diameterLabel.setVisibility(View.INVISIBLE);
			diamUnit.setVisibility(View.INVISIBLE);

			widUnit.setVisibility(View.VISIBLE);
			width.setText("");
			widthLabel.setVisibility(View.VISIBLE);
			width.setVisibility(View.VISIBLE);

			lenUnit.setVisibility(View.VISIBLE);
			length.setText("");
			length.setVisibility(View.VISIBLE);
			lengthLabel.setVisibility(View.VISIBLE);
		} else { // "Select Type"
			diameter.setVisibility(View.INVISIBLE);
			diameterLabel.setVisibility(View.INVISIBLE);
			diamUnit.setVisibility(View.INVISIBLE);

			widUnit.setVisibility(View.INVISIBLE);
			widthLabel.setVisibility(View.INVISIBLE);
			width.setVisibility(View.INVISIBLE);

			lenUnit.setVisibility(View.INVISIBLE);
			length.setVisibility(View.INVISIBLE);
			lengthLabel.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing
	}

	private boolean anyInputsBlank() {
		EditText beltWidth = (EditText) findViewById(R.id.belt_width);
		boolean bw = beltWidth.getText().toString().equals("");

		EditText ovenCapacity = (EditText) findViewById(R.id.oven_capacity);
		boolean oc = ovenCapacity.getText().toString().equals("");

		EditText bakeTime = (EditText) findViewById(R.id.bake_time);
		boolean bt = bakeTime.getText().toString().equals("");

		EditText diameter = (EditText) findViewById(R.id.pan_diameter);
		boolean pd = diameter.getText().toString().equals("");

		EditText length = (EditText) findViewById(R.id.pan_length);
		boolean pl = length.getText().toString().equals("");

		EditText width = (EditText) findViewById(R.id.pan_width);
		boolean pw = width.getText().toString().equals("");

		return bw || oc || bt || pd || pl || pw;
	}

	private void throwError() {
		new AlertDialog.Builder(this)
				.setTitle("Missing Inputs")
				.setMessage("Please fill in all of the inputs, then try calculating the capacity again")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	private Dialog getChooseModelDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose a Model");
		builder.setSingleChoiceItems(R.array.models, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				position = which;
			}
		});
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String beltWidth = getResources().getStringArray(R.array.model_sizes)[position * 2];
				String ovenCapacity = getResources().getStringArray(R.array.model_sizes)[position * 2 + 1];

				((EditText) findViewById(R.id.belt_width)).setText(beltWidth);
				((EditText) findViewById(R.id.oven_capacity)).setText(ovenCapacity);

				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		return builder.create();
	}

	public void calculate(View view) {
		Spinner panTypeSpinner = (Spinner) findViewById(R.id.pan_type);
		if (panTypeSpinner.getSelectedItemPosition() == 1) {
			((EditText) findViewById(R.id.pan_length)).setText("0");
			((EditText) findViewById(R.id.pan_width)).setText("0");
		} else if (panTypeSpinner.getSelectedItemPosition() == 2) {
			((EditText) findViewById(R.id.pan_diameter)).setText("0");
		}

		if (anyInputsBlank()) {
			throwError();
			return;
		}
		double beltWidth = Double.parseDouble(((EditText) findViewById(R.id.belt_width)).getText().toString());
		double ovenCapacity = Double.parseDouble(((EditText) findViewById(R.id.oven_capacity)).getText().toString());
		double bakeTime = Double.parseDouble(((EditText) findViewById(R.id.bake_time)).getText().toString());

		double capacity;

		if (panTypeSpinner.getSelectedItemPosition() == 1) { // Round pan
			double diameter = Double.parseDouble(((EditText) findViewById(R.id.pan_diameter)).getText().toString());

			capacity = new CapacityCalculator(beltWidth, ovenCapacity, bakeTime, diameter).calculateCapacity();
		} else if (panTypeSpinner.getSelectedItemPosition() == 2) {// Rectangular pan
			double length = Double.parseDouble(((EditText) findViewById(R.id.pan_length)).getText().toString());
			double width = Double.parseDouble(((EditText) findViewById(R.id.pan_width)).getText().toString());

			capacity = new CapacityCalculator(beltWidth, ovenCapacity, bakeTime, length, width).calculateCapacity();
		} else {
			capacity = 0.0;
		}

		new AlertDialog.Builder(this)
				.setMessage("The capacity is " + capacity + " pans/hour")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}
