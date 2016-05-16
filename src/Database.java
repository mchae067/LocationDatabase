import java.util.*;
import java.io.*;

public class Database {

	private ArrayList<Location> locations;

	public Database(){
		locations = new ArrayList<Location>(); 
	}
	
	public void add(Location location){
		locations.add(location); 
	}
	
	public void remove(int index){
		locations.remove(index);
	}
	
	public Location retrieveLocationByIndex(int index) {
		Location ret = locations.get(index);
		return ret;
	}
	
	public Location retrieveLocationByName(String name) {
		Location ret = null;
		for (int i = 0; i<locations.size(); i++){
			if (locations.get(i).getName().equals(name)){
				ret = locations.get(i);
			}
		}
		return ret;
	}
	
	public boolean alreadyExists(Location location) {
		for (int i = 0; i<locations.size(); i++) {
			if (locations.get(i).equals(location)) {
				return true;
			}
		}
		return false;
	}
	
	public int numberOfPlaces() {
		int count = 0; 
		for (int i = 0; i<locations.size(); i++){
				count ++;
		}
		return count;
	}

	public void readFromFile(File file) {
		try {
			Scanner in = new Scanner(file);
			int count = 0;
			while (in.hasNext()) {
				String[] locationInfo = in.nextLine().split(";");
				if (locationInfo.length!=4) {
					count++;
				}
				else {
					Location location = new Location(locationInfo[0]
							,locationInfo[1],
						Double.parseDouble(locationInfo[2]),
						Double.parseDouble(locationInfo[3]));
					if (!alreadyExists(location)) {
						locations.add(location);
					}
				}
			}
			if (count>0) {
				System.out.println(count+" invalid entries");
			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to read from file: "+file.getName());		
		}		
	}
	
	public void writeToFile(String filename) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(filename, "UTF-8");
				for (int i=0;i<locations.size();i++) {
					writer.println(locations.get(i).getName()+";"+
									locations.get(i).getAddress()+";"+
									locations.get(i).getLatitude()+";"+
									locations.get(i).getLongitude());
				}
				writer.close();	
			} catch (FileNotFoundException e) {
				System.out.println("Unable to write to file: "+filename);		
			} catch (UnsupportedEncodingException e) {
				System.out.println("Unsupported encoding.");		
			}
			
		
	}
	
	public void sort() {
		Collections.sort(locations);
	}
}
