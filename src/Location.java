import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Location extends Object implements Comparable<Location> {

	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private Location current;
	
	public Location(String name, String address,
			double latitude, double longitude) {
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setCurrent(null);
	}
	
	@Override
	public boolean equals(Object place) {
		if (place instanceof Location) {
			Location location = (Location) place;
			if (name.equals(location.name) && address.equals(location.address)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Location o) {
		// TODO Auto-generated method stub
		if (current!=null) {
			return Double.compare(getDistance(), o.getDistance());
		}
		else {
			return name.compareToIgnoreCase(o.getName());
		}
	}
	
	
	public double getDistance() {
		double distance = Geocoding.distance(latitude, longitude,
				current.getLatitude(), current.getLongitude());
		return distance;
	}
	
	public String getLocationURL() {
		String URL = null;
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String encodedCoordinates = "/@"+latitude+","+longitude+",17z/";
			URL = "https://www.google.com/maps/place/"
					+encodedAddress+encodedCoordinates;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return URL;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
	public Location getCurrent() {
		return current;
	}

	public void setCurrent(Location current) {
		this.current = current;
	}

}
