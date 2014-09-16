package fi.metropolia.villevra.wicam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.VideoView;

public class WatchActivity extends Activity implements OnClickListener{
	
	private VideoView videoView;
	private static final int PICK_FROM_GALLERY=1;
	private ImageButton shareButton;
	private ImageButton pauseButton;
	private Boolean playing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch);
		
		
		
		
		videoView=(VideoView)findViewById(R.id.videoView);
		
		shareButton = (ImageButton)findViewById(R.id.shareButton);
		shareButton.setOnClickListener(this);
		
		pauseButton = (ImageButton)findViewById(R.id.pauseButton);
		pauseButton.setOnClickListener(this);
		
		Intent intent = new Intent();

        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_GALLERY);
		
	}
	
public void onClick(View v){
		
		if (v.getId()==R.id.shareButton){
			Intent i = new Intent(getApplicationContext(), ShareActivity.class);
			startActivity(i);
			// selected video should be sent to the activity to be shared	
			//code here
		
		}else if (v.getId()==R.id.pauseButton){
			if (playing==true){
				// change button icon
				pauseButton.setImageResource(R.drawable.play);
				// pause video
				videoView.pause();
				playing=false;
			}else{
				// change button icon
				pauseButton.setImageResource(R.drawable.pause);
				// resume video
				videoView.start();
				playing=true;
			}
		}
}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if (resultCode != RESULT_OK) return;

	    if (requestCode == PICK_FROM_GALLERY) {
	        Uri mVideoURI = data.getData();  
	        videoView.setVideoURI(mVideoURI);
	        videoView.start(); 
	        playing=true;
	    }

	}
		
	
}
