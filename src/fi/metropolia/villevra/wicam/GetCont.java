package fi.metropolia.villevra.wicam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class GetCont extends AsyncTask<URL, Void, String> {
	protected String doInBackground(URL... url) {
	String str="";
	try{
	URL myUrl = url[0];
	HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String inStr;
	while ((inStr = in.readLine()) != null) {
	str = str + inStr; }
	in.close();
	conn.disconnect();
	}
	catch (Exception e) {
	Log.e("Connection", "Reading error", e);
	}
	return str;
	}
	protected void onPostExecute(String result) {
	
	}
}