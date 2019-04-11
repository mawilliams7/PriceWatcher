package pricewatcher.base;

import java.io.IOException;

public class PriceFinder {
	/** The constructor for the PriceFinder */
	public PriceFinder() {
	}
	
	/** Generates a new random price for an item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * */
	public double getNewPrice (Item item) throws IOException{
		/* Gets the current price of an item. For this version, price is randomly generated within a range.*/
		return 25.0 + Math.random() * (100.0 - 25.0);
	}
	
}