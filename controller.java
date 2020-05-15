package mediSmart.Dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

import java.io.PrintWriter;
import mediSmart.Bean.RegisterBean;
import mediSmart.Util.AES;
import mediSmart.Util.ConstantValues;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class controller {
	
	public final static String AUTH_KEY_FCM = "AAAAA60ejZs:APA91bFGnD4vAoaH4jUx9-GpKKDt9EU1YRrkumOQw4aGdgBd7P3CxdB0jfJwOy5bsJc6Nji_MTodwI_uMKPcazNsmiUzgQnPgcrT8oB4wuXtn8CCibESS7uQm82ZPJtnyof4d39NsHcs";
 	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send"; 
	//public final static String API_SMS_KEY = "cS5me5xOp0W7428qLHmvIg";wqpulEGsx0mjIBJ6A80BZA
	public final static String API_SMS_KEY = "wqpulEGsx0mjIBJ6A80BZA";
 	abstractdao abd;
		   
	public int search(String email, String password) throws Exception 
	{
		abd=new abstractdao();
		
		RegisterBean u = (RegisterBean)abd.getById(ConstantValues.EMAIL, email,RegisterBean.class);
		if(u!=null){
			if(AES.decrypt(u.getPassword()).equals(password))
				 return u.getUserId();
			else
				return -1;
		}
		return -2;
	}
	
	 public RegisterBean getUserid(int userid) {
			abd = new abstractdao();
			RegisterBean u = (RegisterBean) abd.getById(ConstantValues.USERID, userid, RegisterBean.class);
			System.out.println("Get USer Result :uname ->"+u.getName());
			return u;
			}
	
	public void javaScriptFunction(PrintWriter out, String pageName,String message)
	{
		out.println("<script>");
		out.println("alert('"+message+"')");
		out.println("window.location='"+pageName+"'");
		out.println("</script>");	
	}
	
	public String OTP(int len)
    {
        System.out.println("Generating OTP:");
        // Using numeric values
        String numbers = "0123456789";
        String str="";
        // Using random method
        Random rndm_method = new Random();
 
        char[] otp = new char[len];
 
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
             numbers.charAt(rndm_method.nextInt(numbers.length()));
            str=str+otp[i];
        }
        return str;  
    }
	// userDeviceIdKey is the device id you will query from your database

	 	public static void pushFCMNotification(String userDeviceIdKey, String notification, String notifiHeading) throws Exception{

	 	   String authKey = AUTH_KEY_FCM; // You FCM AUTH key
	 	   String FMCurl = API_URL_FCM; 

	 	   URL url = new URL(FMCurl);
	 	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	 	   conn.setUseCaches(false);
	 	   conn.setDoInput(true);
	 	   conn.setDoOutput(true);

	 	   conn.setRequestMethod("POST");
	 	   conn.setRequestProperty("Authorization","key="+authKey);
	 	   conn.setRequestProperty("Content-Type","application/json");

	 	   JSONObject json = new JSONObject();
	 	   json.put("to",userDeviceIdKey.trim());
	 	   JSONObject info = new JSONObject();
	 	   info.put("title", notifiHeading); // Notification title
	 	   info.put("body", notification); // Notification body
	 	   json.put("notification", info);

	 	   OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	 	   wr.write(json.toString());
	 	   wr.flush();
	 	   conn.getInputStream();
	 	}
	 	public void sendSMS(String message, String mobile) {
			String stringURL = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=" + API_SMS_KEY
					+ "&senderid=SMSTST&channel=2&DCS=0&flashsms=0&number=91" + mobile + "&text=" + message.replace(" ", "%20")
					+ "&route=Normal%20Connect";

			try {
				HttpClient client = new DefaultHttpClient();

				URL url = new URL(stringURL);

				HttpGet req = new HttpGet(url.toString());
				HttpResponse resp = client.execute((HttpUriRequest) req);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 	
	 	public static void main(String[] args) {
			new controller().sendSMS("hi", "8793204873");
		}
	 
  }