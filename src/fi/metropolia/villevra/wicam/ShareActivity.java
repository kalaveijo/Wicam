package fi.metropolia.villevra.wicam;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ShareActivity extends Activity implements OnClickListener {
	private Button uploadButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		
		uploadButton = (Button)findViewById(R.id.uploadButton);
		uploadButton.setOnClickListener(this);
		
		
		//sharing groups, right now hard coded, but feel free to use databases and sqlite
		List<String> list = new ArrayList<String>();
		 
		list.add("Everyone");
		list.add("Group A");
		list.add("Group B");
        
		Spinner groupSelect = (Spinner) findViewById(R.id.groupSelect);
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, list);
        	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        	groupSelect.setAdapter(dataAdapter);
	}
	
public void onClick(View v){
		
		if (v.getId()==R.id.uploadButton){
			//notify user for upload
			Context context = getApplicationContext();
			CharSequence text = "Video uploaded";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			//go back to home screen
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			// selected video should be sent to the activity to be shared?
			
		
		}
		
}
}
