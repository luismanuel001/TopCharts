package android.topcharts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONfunctions {

	public static JSONObject getJSONfromURL(String url, String arg0){
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;		
		HttpEntity entity = null;
		//http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(url);
	            httppost.addHeader("Accept-Encoding", "gzip");
	            List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("arg0", arg0));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            entity = response.getEntity();
	            
	            if (entity.getContentEncoding() != null) 
	            {
		            String contentEncoding = entity.getContentEncoding().toString();
		            if (contentEncoding.contains("gzip")) 
		            { 
		                is = new GZIPInputStream(entity.getContent());
		            }
	            } 
	            else 
	            { 
	                is = entity.getContent();
	            }

	    }catch(Exception e){
	            Log.e("log_tag", "Error in http connection "+e.toString()+url);
	    }
	    
	  //convert response to string
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	        		//line = line.replace("&apos;", "'");
	        		line = line.replace("'", "&apos;");
	                sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    
	    try{
	    	
            jArray = new JSONObject(result);            
	    }catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    }
    
	    return jArray;
	}
}
