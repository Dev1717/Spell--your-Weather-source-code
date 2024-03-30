package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class myServlet
 */
@WebServlet("/MyServlet")
public class myServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public myServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//  stub request.getParameter("city");
		//System.out.println(cityName);
		//api key
		String apiKey = "477e651a2f4251dc12174b0d11cd3a8f";
		//get city
		String city = request.getParameter("city");
		
		//String country_code = request.getParameter("country_code");
		//link from open weather api
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		
		//linking of weather api-
		try {
			URL url = new URL(apiUrl);
			 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			 connection.setRequestMethod("GET");
			 
			 InputStream inputStream = connection.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             
			 Scanner sc = new Scanner(reader);
			 StringBuilder responseContent  = new StringBuilder();
			 
			 while(sc.hasNext()) {
				 responseContent.append(sc.nextLine()); 
			 }
			 
			 sc.close();
			 
			 //linking ends 
			 
			 //parse the json to extract humidity , wind etc.
			  Gson gson = new Gson();
			  JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
			  
		      //Date & Time
              long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
              String date = new Date(dateTimestamp).toString();
              
              //Temperature
              double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
              int temperatureCelsius = (int) (temperatureKelvin - 273.15);
             
              //Humidity
              int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
              
              //Wind Speed
              double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
              
              //Weather Condition
              String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
              
			 
			 //request all the data to set attribute to sen to jsp page 
              
              request.setAttribute("date", date);
              request.setAttribute("city", city);
              request.setAttribute("temperature", temperatureCelsius);
              request.setAttribute("weatherCondition", weatherCondition); 
              request.setAttribute("humidity", humidity);    
              request.setAttribute("windSpeed", windSpeed);
              request.setAttribute("weatherData", responseContent.toString());
			 
              connection.disconnect();


		}catch(Exception e) {
			e.getStackTrace();
		}
		
		//forward the data to jsp page for rendering
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}