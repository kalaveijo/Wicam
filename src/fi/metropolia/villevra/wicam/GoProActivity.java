package fi.metropolia.villevra.wicam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GoProActivity extends Activity implements SurfaceHolder.Callback, OnPreparedListener, OnClickListener{
	private GetCont myTask; 
	private MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private SurfaceView playerSurfaceView;
	private String videoSrc = "http://10.5.5.9:8080/live/amba.m3u8";
	private ImageButton recordBtn;
	private parseUrl myParse;
	private String myHref;
	private String myUrl;
	private String videoUrl;
	private boolean recording = false;
	private ProgressBack myProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		setBipVolume();
		setCameraMode("video");
		
		recordBtn = (ImageButton)findViewById(R.id.recbtn);
		recordBtn.setOnClickListener(this);
		
		playerSurfaceView = (SurfaceView)findViewById(R.id.playersurface);

        surfaceHolder = playerSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        
        myUrl= "http://10.5.5.9:8080/videos/DCIM/100GOPRO/"; //this is where all the gopro videos are on the camera
		
		
 }

	

public void onClick(View v){
		
		if (v.getId()==R.id.recbtn){
			if(recording==false){
				recording=true;
				recordBtn.setImageResource(R.drawable.stop);
				Record();
			}else{
				recording=false;
				recordBtn.setImageResource(R.drawable.rec);
				Record();
				
				
				//parse the url and get the last mp4
				//start download on post execute
				myParse = new parseUrl();
				
				myParse.execute("");
				
				
			}
		}
}



private class parseUrl extends AsyncTask<String, Void, String>{
	
	String myHrefName;
	String File;
	//ArrayList<String> descriptionList;
	

    @Override
    protected void onPreExecute()
    {
  
        //descriptionList = new ArrayList<String>();
    }
    
    @Override
    protected String doInBackground(String... params) {
        try {
            Document doc = Jsoup.connect(myUrl).get();

            
            Elements links = doc.select(".link");
            //the following loop finally gets the latest MP4 filename so we can download it to the phone
            for( Element link: links )
            {	
				
                myHrefName = link.attr("href");
                String myHrefNameArray[] = myHrefName.split("\\.");
                
                File = myHrefNameArray[myHrefNameArray.length-2];
                myHref = File+".MP4"; //final filename
                
            
            }

        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        setVideoUrl(myHref);
        
        //start progress and download
        myProgress = new ProgressBack();
		
		myProgress.execute("");
    }

}

public void setVideoUrl(String filename){
	videoUrl = myUrl+filename;
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
 
 public void Record(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if(recording){
				try{
					//does the following when recording
					URL myUrl = new URL("http://10.5.5.9/bacpac/SH?t=gopro2013&p=%01");
					myTask = new GetCont();
					myTask.execute(myUrl);
				}
				catch (Exception e) {
					Log.e("URL","URL creation ", e);
				}
			}else{
				try{
					//does the following when stopping recording
					URL myUrl = new URL("http://10.5.5.9/bacpac/SH?t=gopro2013&p=%00");
					myTask = new GetCont();
					myTask.execute(myUrl);
				}
				catch (Exception e) {
					Log.e("URL","URL creation ", e);
				}
			}
		} else {
		// error message
	}
}

 
 
 public void setBipVolume(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
				try{
					URL myUrl = new URL("http://10.5.5.9/camera/BS?t=gopro2013&p=%00");//sets the bip volume to 0
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
 
//code in case you need to switch to photo mode from video or vice versa
public void setCameraMode(String mode){
	ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	if (networkInfo != null && networkInfo.isConnected()) {
		if (mode=="video"){
			try{
				URL myUrl = new URL("http://10.5.5.9/camera/CM?t=gopro2013&p=%00");
				myTask = new GetCont();
				myTask.execute(myUrl);
			}
			catch (Exception e) {
				Log.e("URL","URL creation ", e);
			}
		}else if(mode=="photo"){
			try{
				URL myUrl = new URL("http://10.5.5.9/camera/CM?t=gopro2013&p=%01");
				myTask = new GetCont();
				myTask.execute(myUrl);
			}
			catch (Exception e) {
				Log.e("URL","URL creation ", e);
			}
		}
	}
}

 @Override
 public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
  // TODO Auto-generated method stub
  
 }

 @Override
 public void surfaceCreated(SurfaceHolder arg0) {

        try {
         mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(videoSrc);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
 }

 @Override
 public void surfaceDestroyed(SurfaceHolder arg0) {
  // TODO Auto-generated method stub
  
 }

 @Override
 public void onPrepared(MediaPlayer mp) {
  mediaPlayer.start();
 }
 
 /*
  * Following is the code for downloads progress.
  */
 
 private class ProgressBack extends AsyncTask<String,String,String> {
     ProgressDialog PD;
    @Override
    protected void onPreExecute() {
        PD= ProgressDialog.show(GoProActivity.this,"Progress", "Please Wait ...", true);
        PD.setCancelable(true);
    }

    @Override
    protected String doInBackground(String... arg0) {
    	
    	DownloadFile(videoUrl,myHref);            
    return null;
    }
    protected void onPostExecute(String result) {
       
    	PD.dismiss();

    }

}
 
 public void DownloadFile(String fileURL, String fileName) {
     try {
    	 
         String RootDir = Environment.getExternalStorageDirectory()
                 + File.separator + "DCIM/WiCam";
         System.out.println(RootDir);
         File RootFile = new File(RootDir);
         RootFile.mkdir();
         // File root = Environment.getExternalStorageDirectory();
         
         URL u = new URL(fileURL);
         HttpURLConnection c = (HttpURLConnection) u.openConnection();
         c.setRequestMethod("GET");
         c.setDoOutput(true);
         c.connect();
         FileOutputStream f = new FileOutputStream(new File(RootFile,
                 fileName));
         InputStream in = c.getInputStream();
         byte[] buffer = new byte[1024];
         int len1 = 0;
         
         while ((len1 = in.read(buffer)) > 0) {                          
             f.write(buffer, 0, len1);               
         }
         
         f.close();
         

     } catch (Exception e) {

         Log.d("Error....", e.toString());
     }

 }

}
