package fi.metropolia.villevra.wicam;

/*
  WiCam - a prototype for an android app utilizing GoPro Hero3 to shoot videos and then using the vidoes for learning purposes

    Copyright (C) 2013  Ville Raitio



    This program is free software; you can redistribute it and/or modify

    it under the terms of the GNU General Public License as published by

    the Free Software Foundation; either version 2 of the License, or

    (at your option) any later version.



    This program is distributed in the hope that it will be useful,

    but WITHOUT ANY WARRANTY; without even the implied warranty of

    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

    GNU General Public License for more details.



    You should have received a copy of the GNU General Public License

    along with this program; if not, write to the Free Software

    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener{
	
	private GetCont myTask;
	MenuItem license;

	
	/*
	 * This class contains lots of errors on bootup. Prolly related to GetCont class implementation and reason it is called twice in a row
	 * (turnOnCamera() and turnOnPreview())
	 * 
	 * Also all errors are silently ignored for no reason, needs logger.
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_landscape);
		
		turnOnCamera();
		//define buttons:
		
		ImageButton cameraBtn = (ImageButton)findViewById(R.id.camerabtn);
		cameraBtn.setOnClickListener(this);
		
		/*
		 * Second button is commented out because implementation could not be started. Plan was to use this button for OptionActivity (totally
		 * unimplemented and lacks design).
		 * 
		 */
		
		//ImageButton watchBtn = (ImageButton)findViewById(R.id.watchbtn); //this button is used to add a loggable game to the database
		//watchBtn.setOnClickListener(this);
		
	}
	
	public void turnOnCamera(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
		try{
		URL myUrl = new URL("http://10.5.5.9/bacpac/PW?t=gopro2013&p=%01");
		myTask = new GetCont();
		myTask.execute(myUrl);
		}
		catch (Exception e) {
		Log.e("URL","URL creation ", e);
		}
		} else {
		// error message
		}
		
	}
	
	public void turnOnPreview(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
		try{
		URL myUrl = new URL("http://10.5.5.9/camera/PV?t=gopro2013&p=%02");
		myTask = new GetCont();
		myTask.execute(myUrl);
		}
		catch (Exception e) {
		Log.e("URL","URL creation ", e);
		}
		} else {
		// error message
		}
		
	}
	
	
	
	public void onClick(View v){
		
		if (v.getId()==R.id.camerabtn){
			//turn on camera if it is off
			turnOnCamera();
			turnOnPreview();
			//go to CameraActivity
			Intent i = new Intent(getApplicationContext(), GoProActivity.class);
			startActivity(i);
		}else if(v.getId()==R.id.watchbtn){
			//go to WatchActivity
			Intent i = new Intent(getApplicationContext(), WatchActivity.class);
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public boolean onMenuItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.license:
	    	Intent i = new Intent(getApplicationContext(), LicenseActivity.class);
			startActivity(i);
	    }
	    return true;
	}
}
