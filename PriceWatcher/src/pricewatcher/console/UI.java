package pricewatcher.console;

import java.util.List;
import java.util.Scanner;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import pricewatcher.model.Item;
import pricewatcher.model.PriceFinder;

public class UI {
	public List<Item> items;
	
	public UI () {
		//Creates the hard-coded item used for the terminal version of price watcher
		String testItemUrl = "https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022";
		String testItemName = "LED monitor";
		String testDateAdded = "8/25/18";
		double testItemInitialPrice = 61.13;
		
		Item testItem = new Item(testItemName, testItemInitialPrice, testItemUrl, testDateAdded);
		List<Item> testItemList = new ArrayList<Item>();
		testItemList.add(testItem);
		this.items = testItemList;
	}
	
	public static void run() {
		 /* Runs price watcher on the terminal.*/
		UI consoleInterface  = new UI();
		System.out.println("Welcome to Price Watcher!");

		displayItems(consoleInterface);
		
		Scanner reader = new Scanner(System.in);
		int userChoice = getUserChoice(reader);
		
		List<Item> itemList = consoleInterface.items;

		while (userChoice != -1) {
			if (userChoice == 1) {
				for (Item item: itemList) {
					PriceFinder finder = new PriceFinder(item);
					item.updatePrice(finder.getNewPrice());
				}
				displayItems(consoleInterface);
				userChoice = getUserChoice(reader);
			}
			else if (userChoice == 2) {
				for (Item item: itemList) {
					launchWebsite(item);
				}
				userChoice = getUserChoice(reader);
			}
			else {
				System.out.println("Not a valid input.");
				userChoice = getUserChoice(reader);
			}
		}
		reader.close();
		System.out.println("Thank you for using this program!");
	}
	
	public static void displayItems(UI ui) {
		/* Displays the item information of each item on the terminal.*/
		List<Item> itemList = ui.items;
		for (Item item: itemList) {
			System.out.println("Name: " + item.getName());
			System.out.println("URL: " + item.getURL());
			System.out.println("Price: " + String.format("%.2f", item.getCurrentPrice()));
			System.out.println("Change: " + String.format("%.2f", item.getPriceChange()) + "%");
			System.out.println("Added: " + item.getDateAdded() + "($" + String.format("%.2f", item.getOriginalPrice()) + ")");
		}
	}
	
	public static int getUserChoice(Scanner reader) {
		/* Reads user input to determine next price watcher action.*/
		int userChoice = 0;
		System.out.println("Enter 1 (to check price), 2 (to view page), or -1 to quit.");
		try {
			userChoice = reader.nextInt();
		}
		catch (InputMismatchException e) {
			// Reads the erroneous input, clearing the scanner for new input
			reader.nextLine();
		}
		return userChoice;
	}
	
	public static void launchWebsite(Item item) {
		/* Launches item web sites in user's default browser*/
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(item.getURL()));
				System.out.println("Webpage opened in your default browser.");
			}
			catch (IOException | URISyntaxException e) {
				System.out.println("Unable to access webpage for " + item.getName() + ".");
			}
		}
		else {
			System.out.println("Unable to access webpage for " + item.getName() + ".");
		}
	}

}
