  package pricewatcher.base;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/** An Item for the PriceWatcher
 *
 * 
 */
public class Item {
	
	/**
	 * Name of this item.
	 */
	private String name;
	/**
	 * Original price of the item.
	 */
	private double originalPrice;
	/**
	 *  Current price of the item.
	 */
	private double currentPrice;
	/**
	 * Price change of the item.
	 */
	private double priceChange;
	/**
	 * URL of the item.
	 */
	private String url;
	/**
	 * The date the item was added.
	 */
	private String dateAdded;
	
    /** Constructor for an Item object.
     * @param name The name of the item
     * @param price The original price of the item
     * @param url The url of the item
     * @param dateAdded The date the item was added
     * */
	public Item(String name, double price, String url, String dateAdded) {
		this.name = name;
		this.currentPrice = price;
		this.originalPrice = price;
		this.url = url;
		this.priceChange = 0.0;
		this.dateAdded = dateAdded;
	}
	
	/** Default constructor for an Item object*/
	public Item() {
	}
	
    /** Updates the price for an Item.
     * @param newPrice The new price of the item
     * */
	public void updatePrice(double newPrice) {
		this.priceChange = ((newPrice - this.originalPrice) / (this.originalPrice)) * 100;
		this.currentPrice = newPrice;
	}
	
    /** Gets the original price of an item.
     * @return The original price of the item
     * */
	public double getOriginalPrice() {
		return this.originalPrice;
	}
	
    /** Gets the current price of an item.
     * @return The current price of the item
     * */
	public double getCurrentPrice() {
		return this.currentPrice;
	}
	
    /** Gets the price change of an item.
     * @return The price change of the item
     * */
	public double getPriceChange() {
		return this.priceChange;
	}
	
    /** Gets the url of an item.
     * @return The url of the item
     * */
	public String getURL() {
		return this.url;
	}
	
    /** Gets the name of an item.
     * @return The name of the item
     * */
	public String getName() {
		return this.name;
	}
	
    /** Gets the date the item was added.
     * @return The date the item was added
     * */
	public String getDateAdded() {
		return this.dateAdded;
	}
	
	@Override
    /** GThe equals method for item
     * @param obj The object to be compared to
     * @return Checks if the Object passed is equal to item
     * */
	public boolean equals(Object obj) {
		Item comp = (Item) obj;
		if(!this.name.equals(comp.getName())) {
			return false;
		}
		
		if(!this.url.equals(comp.getURL())) {
			return false;
		}
		
		if(!this.dateAdded.equals(comp.getDateAdded())) {
			return false;
		}
		
		if(!(this.originalPrice == comp.getOriginalPrice())) {
			return false;
		}
		
		return true;
	}
	
    /** Sets the current price of the item
     * @param currentPrice The current price of the Item
     * */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
    /** Sets the url of the item
     * @param url The url of the item
     * */
	public void setURL(String url) {
		this.url = url;
	}
	
    /** Sets the name of the item
     * @param name The name of the Item
     * */
	public void setName(String name) {
		this.name = name;
	}
	
    /** Sets the original price of the item
     * @param price The original price of the item
     * */
	public void setOriginalPrice(Double price) {
		this.originalPrice = price;
	}
	
    /** Sets the date the item was added
     * @param dateAdded The date the item was added
     * */
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	
    /** Converts an Item to a JSONObject
     * @return The item as a JSON object
     * */
	public JSONObject toJSON() {
	    Map<String, Object> map = new HashMap<>();
	    map.put("name", name);
	    map.put("currentPrice", currentPrice);
	    map.put("originalPrice", originalPrice);
	    map.put("dateAdded", dateAdded);
	    map.put("url", url);
	    return new JSONObject(map);
	}

}
