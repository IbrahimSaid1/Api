package ApiJson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;




public class Json {
	public static void main(String[] args) {
		
		
		Scanner sca= new Scanner(System.in);
		String country = null;
		
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

		 MyObject[] myObj = gson.fromJson(json.toString(), MyObject[].class);

		 
		 String url1 = "jdbc:sqlserver://localhost:1433;" + "databaseName=Universities;" + "encrypt=true;"
					+ "trustServerCertificate=true";

			String user = "sa";
			String pass = "root";
			Connection con = null;

			Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(url1, user, pass);
			Statement st = con.createStatement();
		 
			
			
		boolean condition= true;
		while(condition) {
		 System.out.println("1. Creat tables");
		 System.out.println("2. Insert data into table");
		 System.out.println("3. fetch data");
		 System.out.println("4. Taking backup");
		 System.out.println("5. Drop table");
		 System.out.println("6. Exit");

		 int a = sca.nextInt();
		 switch (a) {
		 case 1:
				String sql = "CREATE TABLE mytable (Name VARCHAR(255), Country VARCHAR(255),  "
						+ "AlphaTwoCode VARCHAR(2), Domains VARCHAR(255),"
						+ "WebPages VARCHAR(255));" 
						+"CREATE TABLE countries (Name VARCHAR(255), AlphaTwoCode VARCHAR(2))";
				st.executeUpdate(sql);
				System.out.println("Done");
				break;
			
				
				
				
				
		 case 2:
			
		 System.out.print("Enter the country: ");
		 country= sca.next();
			
	     System.out.println("Universities in " + country + ":");
	     System.out.println(myObj);
	     

	    System.out.println(myObj.getClass());
         
         PreparedStatement pstmt = con.prepareStatement("INSERT INTO mytable VALUES (?, ?, ?, ?, ?)");
			for (MyObject university : myObj  ) {
				pstmt.setString(1, university.getName());
				pstmt.setString(2, university.getCountry());
				pstmt.setString(3, university.getAlpha_two_code());
				pstmt.setString(4, String.join(",", university.getDomains()));
				pstmt.setString(5, String.join(",", university.getWeb_pages()));
				pstmt.executeUpdate();
			}
	     
	     break;     
	    
		 case 3:
			
	     System.out.print("Enter the country: ");
		 country= sca.next();
		 
		 

			String sql1 = "SELECT * FROM mytable WHERE country= '" + country + "'";
			ResultSet resultSet = st.executeQuery(sql1);
			System.out.println("-------------------------------------------------------------------");
			System.out.println("                Universities in " + country + ":");
			System.out.println("-------------------------------------------------------------------");
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				String webPage = resultSet.getString("webpages");
				String domain = resultSet.getString("domains");
				System.out.println("----------------------------------------------------------");
				 System.out.println(name +  " | "+ "\t" + domain +  " | "+ "\t"  + webPage);
			}

			resultSet.close();
		 break;
		 
		 
		 case 4:
				 resultSet = st.executeQuery("SELECT * FROM mytable");
				 PrintWriter writer = new PrintWriter("backup.sql");
				 writer.println("-------------------------------------------------------------------");
				 writer.println("                Universities:");
				 writer.println("-------------------------------------------------------------------");
					while (resultSet.next()) {
						String name = resultSet.getString("name");
						String webPage = resultSet.getString("webpages");
						String domain = resultSet.getString("domains");
						writer.println("----------------------------------------------------------");
						writer.println(name +  " | "+ "\t" + domain +  " | "+ "\t"  + webPage);
					}
					writer.close();
				System.out.println("Data dumped to file successfully.");
				break;
		      
		 case 5:
			 String sql2 = "DROP TABLE mytable";
	         st.executeUpdate(sql2);
	         System.out.println("Table removed successfully.");
	         break;
	         
		 case 6:
	     condition= false;
		 }
		}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}

	private static List<MyObject> getAllUniversities() {
		// TODO Auto-generated method stub
		return null;
	}
}