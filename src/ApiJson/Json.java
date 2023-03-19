package ApiJson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.Gson;

public class Json {
	public static void main(String[] args) {
		
		 String apiUrl = "http://universities.hipolabs.com/search?country=Oman";
		 try {
		 URL url = new URL(apiUrl);
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 conn.setRequestMethod("GET");
		 conn.setRequestProperty("Accept", "application/json");
		 if (conn.getResponseCode() != 200) {
		 throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
		 }
		 BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		 String output;
		 StringBuilder json = new StringBuilder();
		 while ((output = br.readLine()) != null) {
		 json.append(output);
		 }
		 conn.disconnect();
		 Gson gson = new Gson();
		 ArrayList<MyObhject> myObj = gson.fromJson(json.toString(),ArrayList.class);
		 // Use myObj for further processing
		 System.out.println("Done");
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
}