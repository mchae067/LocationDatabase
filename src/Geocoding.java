import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides methods useful for the My Favorite Places project.
 * Methods include:
 * - find: to obtain latitude and longitude for an address utilizing Google's 
 *         Geocoding API.
 * - distance: to determine the distance between 2 points on the Earth's surface.
 * - openBrowser: to open a browser at a specified URL.
 * 
 * @See https://developers.google.com/maps/documentation/geocoding/intro#Geocoding
 * 
 * Adapted from Stuart Reges' GeoLocator project (University of Washington).
 * @author Jim Williams
*/
public class Geocoding {
	// URL for the Google Maps Geocoding API
	private static final String GEOCODING_URL 
			= "https://maps.googleapis.com/maps/api/geocode/json?address=";

	// charset used for encoding the address
	private static final String CHARSET = "UTF-8";

	/**
	 * Utilizes Google's Geocoding API to get location information given an
	 * address. The GeocodeResponse class contains methods to parse the 
	 * JSON response into objects.
	 * 
	 * @param address A street address.
	 * @return a JSON formatted response with various location information
	 * such as latitude and longitude.
	 * 
	 * @throws IOException
	 */
	public static String find(String address) throws IOException {
		// encode address in a form to pass as part of URL as get request
		String encodedAddress = URLEncoder.encode(address, CHARSET);

		// prepare get request string.
		String httpGetRequest = GEOCODING_URL + encodedAddress;

		// perform get request
		URLConnection connection = new URL(httpGetRequest).openConnection();
		connection.setRequestProperty("Accept-Charset", CHARSET);

		// gather and return response as a String
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputReader);

		StringBuffer response = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			response.append(line);
			response.append("\n");
		}

		return response.toString();
	}
	
	//Earth's radius in miles
	//https://en.wikipedia.org/wiki/Earth_radius
    private static final double EARTHS_RADIUS_IN_MILES = 3959.0;
    
    /**
     * Calculate the distance between two points on the Earth's surface
     * using the Spherical Law of Cosines.
     * 
     * @param latitude1    Latitude of point 1
     * @param longitude1   Longitude of point 1
     * @param latitude2    Latitude of point 2
     * @param longitude2   Longitude of point 2
     * @return the distance in miles between the two points.
     */
	public static double distance( double latitude1, double longitude1,
			double latitude2, double longitude2) {
		
		//Latitude and longitude are points that describe a location on the
		//Earth's surface but are also angles measured in degrees.
		//http://www-istp.gsfc.nasa.gov/stargaze/Slatlong.htm
		//Convert latitude and longitude for the two points into radians
		double lat1Radians = Math.toRadians( latitude1);
        double long1Radians = Math.toRadians( longitude1);
        double lat2Radians = Math.toRadians( latitude2);
        double long2Radians = Math.toRadians( longitude2);
        
        //Utilize the Spherical Law of Cosines. The triangle is the 2 points
        //plus the North pole.
        //https://en.wikipedia.org/wiki/Spherical_law_of_cosines
        //http://helpdesk.objects.com.au/java/distance-between-two-latitude-longitude-points
        double arc = Math.acos( Math.sin(lat1Radians) * Math.sin(lat2Radians) 
        		+ Math.cos(lat1Radians) * Math.cos(lat2Radians) 
        		* Math.cos(long1Radians - long2Radians));
        return arc * EARTHS_RADIUS_IN_MILES;
    }
	
	/**
	 * Starts a browser page with the specified url, if on a supported Desktop.
	 * @param url  A url such as https://www.google.com/maps/
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void openBrowser(String url) throws IOException, URISyntaxException {
		if( Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(new URI( url));
		}		
	}	
	
	/**
	 * This main method had code demonstrating how to use the various methods
	 * in Geocoding.java and GeocodeResponse.java files.
	 * @param args unused
	 */
	public static void main(String[] args) {
		
		// example of using Geocoding.find and GeocodeResponse.parse methods.
		String jsonResponse;
		try {
			jsonResponse = Geocoding.find("1600 Amphitheatre Parkway, Mountain View, CA");
			System.out.println("JSON formatted Geocoding results:\n" + jsonResponse);

			GResponse gResponse = GeocodeResponse.parse(jsonResponse);
			System.out.println("Gson parsing response instance:\n" + gResponse);

			System.out.println("Selected output:");
			System.out.println(gResponse.getFormattedAddress());
			System.out.println(gResponse.getLatitude());
			System.out.println(gResponse.getLongitude());

			// how far apart are Good and Evil?
			String goodJson = Geocoding.find("Good");
			String evilJson = Geocoding.find("Evil");

			GResponse good = GeocodeResponse.parse(goodJson);
			System.out.println("Good:" + good);

			GResponse evil = GeocodeResponse.parse(evilJson);
			System.out.println("Evil:" + evil);

			double distanceMiles = Geocoding.distance(good.getLatitude(), good.getLongitude(), evil.getLatitude(),
					evil.getLongitude());
			System.out.println("Distance between Good and Evil is: " + distanceMiles + " miles.");

			openBrowser("https://www.google.com/maps/place/2+E+Main+St%2C+Madison%2C+WI+53703%2C+USA/@43.074691,-89.3841678,17z/");
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}