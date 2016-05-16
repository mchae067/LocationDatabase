import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;

public class MyFavoritePlaces {

	public static void main(String[] args) {
		//List object of places
		Database myPlaceDatabase = new Database();
		boolean currentSet = false;
		//Indefinite loop, until 'q' is entered
		while(true) {
			//Main menu
			Scanner in = new Scanner(System.in);
			System.out.print(
					"My Favorite Places 2016"+
					"\n--------------------------");
			if (myPlaceDatabase.numberOfPlaces()==0) {
				System.out.print(
						"\nNo places loaded."+
								"\n--------------------------"+
						"\nA)dd R)ead Q)uit : ");
			}
			else if (currentSet) {
				System.out.print("\ndistance from "+myPlaceDatabase.
						retrieveLocationByIndex(0).getName());
				for (int i=0;i<myPlaceDatabase.numberOfPlaces();i++) {
			        DecimalFormat df = new DecimalFormat("#.##");
					System.out.print(
							"\n"+(i+1)+") "+myPlaceDatabase.
							retrieveLocationByIndex(i).getName()
							+" ("+df.format(myPlaceDatabase.
							retrieveLocationByIndex(i).getDistance())
							+" miles)");
					}
					System.out.print("\n--------------------------"+
					   "\nA)dd S)how E)dit D)elete C)urrent R)ead W)rite Q)uit : ");
			}
			else {
				for (int i=0;i<myPlaceDatabase.numberOfPlaces();i++) {
					System.out.print(
						"\n"+(i+1)+") "+myPlaceDatabase.
						retrieveLocationByIndex(i).getName());
				}
				System.out.print("\n--------------------------"+
				   "\nA)dd S)how E)dit D)elete C)urrent R)ead W)rite Q)uit : ");
			}

			String input = in.next();

			//Quit
			if (input.equals("q")) {
			System.out.println("Thank you for using My Favorite Places 2016!");
				in.close();
				System.exit(0);
			}
			//Add
			else if(input.equals("a")) {
				System.out.print("Enter the name: ");
				in.nextLine();
				String name = in.nextLine();
				System.out.print("Enter the address: ");
				String address = in.nextLine();
				String jsonResponse = null;
				try {
					jsonResponse = Geocoding.find(address);
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				try {
					GResponse gResponse = GeocodeResponse.parse(jsonResponse);
					Location location = new Location(name,
							gResponse.getFormattedAddress(),
							gResponse.getLatitude(),
							gResponse.getLongitude());
					if (myPlaceDatabase.alreadyExists(location)) {
						System.out.println("Location already in list.");
					}
					else {
						myPlaceDatabase.add(location);
					}
				}
				catch (IndexOutOfBoundsException e) {
					System.out.println(
							"Place not found using address: "+address);
					System.out.println("Press Enter to continue.");
					in.nextLine();
				}
			}
			//Show
			else if(input.equals("s")) {
				if (myPlaceDatabase.numberOfPlaces()==0) {
					System.out.println("No locations to Show");
				}
				else {
					System.out.print("Enter number of place to Show: ");
					int index = in.nextInt()-1;
					try {
						System.out.println(
								myPlaceDatabase.retrieveLocationByIndex(index)
										.getName()
								+"\n"+
								myPlaceDatabase.retrieveLocationByIndex(index)
										.getAddress()
								+"\n"+
								myPlaceDatabase.retrieveLocationByIndex(index)
										.getLatitude()
								+","+
								myPlaceDatabase.retrieveLocationByIndex(index)
										.getLongitude()
								+"\n"+
								myPlaceDatabase.retrieveLocationByIndex(index)
										.getLocationURL()
								);
						Geocoding.openBrowser(myPlaceDatabase
							.retrieveLocationByIndex(index).getLocationURL());
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println("Invalid selection.");
					} catch (IOException e) {
						System.out.println("ERROR: Could not open URL.");
					} catch (URISyntaxException e) {
						System.out.println("ERROR: Could not open URL.");
					}
					System.out.print("Press Enter to continue.");
					in.nextLine();
				}
			}
			//Edit
			else if(input.equals("e")) {
				if (myPlaceDatabase.numberOfPlaces()==0) {
					System.out.println("No locations to Edit");
				}
				else {
					System.out.print("Enter number of place to Edit: ");
					int index = in.nextInt()-1;
					if (index<0 || index > myPlaceDatabase.numberOfPlaces()-1) {
						System.out.println("Invalid selection\n");
						continue;
					}
					System.out.println("Current name: "+myPlaceDatabase.
							retrieveLocationByIndex(index).getName());
					System.out.print("Enter the new name: ");
					in.nextLine();
					String name = in.nextLine();
					System.out.println("Current address: "+myPlaceDatabase.
							retrieveLocationByIndex(index).getAddress());
					System.out.print("Enter the new address: ");
					String address = in.nextLine();
					String jsonResponse = null;
					try {
						jsonResponse = Geocoding.find(address);
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
					GResponse gResponse = GeocodeResponse.parse(jsonResponse);
					try {
						myPlaceDatabase.retrieveLocationByIndex(index)
						.setAddress(gResponse.getFormattedAddress());
						myPlaceDatabase.retrieveLocationByIndex(index)
						.setName(name);
						myPlaceDatabase.retrieveLocationByIndex(index)
						.setLatitude(gResponse.getLatitude());
						myPlaceDatabase.retrieveLocationByIndex(index)
						.setLongitude(gResponse.getLongitude());
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println(
								"Place not found using address: "+address);
						System.out.println("Press Enter to continue.");
						in.nextLine();
					}
				}
			}
			//Delete
			else if(input.equals("d")) {
				if (myPlaceDatabase.numberOfPlaces()==0) {
					System.out.println("No locations to Delete");
				}
				else {
					System.out.print("Enter number of place to Delete: ");
					int index = in.nextInt()-1;
					try {
						myPlaceDatabase.remove(index);
						System.out.println
							(myPlaceDatabase.retrieveLocationByIndex(index)
									.getName()+" deleted.");
						System.out.println("Press Enter to continue.");
						in.nextLine();
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println("Invalid selection.");
						System.out.println("Press Enter to continue.");
						in.nextLine();
					}	
				}
			}
			//Read
			else if (input.equals("r")) {
				ArrayList<File> available = findMFPFFiles();
				System.out.println("Available files:");
				for (int i=0;i<available.size();i++) {
					System.out.println("	"+available.get(i).getName());
				}
				System.out.print("Enter name of File to Read: ");
				String filename = in.next();
				File file = new File(filename);
				myPlaceDatabase.readFromFile(file);
			}
			//Write
			else if (input.equals("w")) {
				if (myPlaceDatabase.numberOfPlaces()==0) {
					System.out.println("No locations to Write");
				}
				else {
					ArrayList<File> available = findMFPFFiles();
					System.out.println("Available files: ");
					for (int i=0;i<available.size();i++) {
						System.out.println("	"+available.get(i).getName());
					}
					System.out.print("Enter name of File to Write to: ");
					String filename = in.next();
					try {
						myPlaceDatabase.writeToFile(filename);
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
				}
			}
			//set Current
			else if (input.equals("c")) {
				if (myPlaceDatabase.numberOfPlaces()==0) {
					System.out.println("No locations to set to Current");
				}
				else {
					System.out.print
						("Enter number of place to be Current place: ");
					int index = in.nextInt()-1;
					try {
						Location current =
								myPlaceDatabase.retrieveLocationByIndex(index);
						for (int i=0;i<myPlaceDatabase.numberOfPlaces();i++) {
							myPlaceDatabase.retrieveLocationByIndex(i)
							.setCurrent(current);
						}
						System.out.println
							(current.getName()+" set as Current place.");
						currentSet = true;
						System.out.println("Press Enter to continue.");
						in.nextLine();
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println("Invalid selection");
						System.out.println("Press Enter to continue.");
						in.nextLine();
					}
				}
			}
			else {
				System.out.println("Invalid input.");
				System.out.println("Press Enter to continue.");
				in.nextLine();
				in.nextLine();
			}
			myPlaceDatabase.sort();
			System.out.print("\n");
		}
	} 

	//Finds available .mpf's for read and write
	public static ArrayList<File> findMFPFFiles() {
		ArrayList<File> returnList = new ArrayList<File>();
		String filepath = "./";
		File folder = new File(filepath);
		for ( File file : folder.listFiles()) {
			if ( file.getName().endsWith(".mfp")) {
				returnList.add(file);
			} 
		}
		return returnList;
	}
}
