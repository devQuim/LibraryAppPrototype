package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class Prefs extends Activity {

	public CheckBox lmodechk,lsmschk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lmode);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

		}else{
			showGPSDisabledAlertToUser();
		}
		lmodechk=(CheckBox)findViewById(R.id.lmodechk);
		lsmschk=(CheckBox)findViewById(R.id.lsmschk);
		if(lafCache.lob){
			lmodechk.setChecked(true);
		}
		if(lafCache.smsfeed){
			lsmschk.setChecked(true);
		}
		lmodechk.setOnClickListener(new View.OnClickListener() {
			@Override
				public void onClick(View v) {

				if (((CheckBox) v).isChecked()) {
lafCache.lob=true;
				} else {
lafCache.lob=false;
				}

			}

		});
		lsmschk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					lafCache.smsfeed=true;
				} else {
					lafCache.smsfeed=false;
				}
			}
		});

	}


	private void showGPSDisabledAlertToUser(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
								finish();

							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						dialog.cancel();
						finish();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
