import java.util.List;
import com.google.gson.Gson;

/**
 * This class utilizes Google's Gson library to parse a JSON response returned 
 * from the Geocoding.find( address) method.  The class provides methods that
 * return selected fields, such as latitude, longitude and formatted address.
 * @See https://github.com/google/gson
 * @See https://github.com/google/gson#google-gson
 * 
 * @author Jim Williams
 */
public class GeocodeResponse {

	/**
	 * Takes the JSON response string returned by the Geocoder.find method,
	 * parses it and returns a GResponse object hierarchy with the information.
	 * 
	 * @param jsonGeocodeResponse Geocoding find response in the JSON format.
	 * @return an instance of GResponse containing the information from the
	 * JSON response.
	 */
	public static GResponse parse(String jsonGeocodeResponse) {
		GResponse responseAsObjects = new Gson().fromJson(jsonGeocodeResponse, 
				GResponse.class);
		return responseAsObjects;
	}
}

/**
 * These classes and their fields match the structure of the Geocoding JSON 
 * formatted response such that Gson can convert from JSON to instances of 
 * these classes. 
 *
 */
class GResponse {
	private String status;
	private List<GResult> results;
	private String error_message;
	
	public String getFormattedAddress() {
		return this.results.get(0).formatted_address;
	}
	
	public double getLongitude() {
		return Double.parseDouble(this.results.get(0).geometry.location.lng);
	}
	
	public double getLatitude() {
		return Double.parseDouble(this.results.get(0).geometry.location.lat);
	}
	
	public boolean hasAddress() {
		return this.status.equals("OK") && this.results.size() > 0;
	}
	
	public String toString() {
		if ( this.status.equals("OK")) {
			return this.status + "\n"
				+ this.getFormattedAddress() + "\n" 
				+ this.getLatitude() + "\n"
				+ this.getLongitude() + "\n";
		} else {
			return this.status + ": " + this.error_message;
		}
	}	
}

class GResult {
	List<GAddressComponent> address_components;
	String formatted_address;
	GGeometry geometry;
	Boolean partial_match;
	String place_id;
	List<String> types;
}
class GGeometry {
	GViewport bounds;
	GCoordinates location;
	String location_type;
	GViewport viewport;
}
class GViewport {
	GCoordinates northeast;
	GCoordinates southwest;
}
class GAddressComponent {
	String long_name;
	String short_name;
	List<String> types;
}
class GCoordinates {
	String lat;
	String lng;
}
