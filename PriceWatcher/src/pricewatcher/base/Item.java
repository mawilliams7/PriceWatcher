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
	
	public Item(String name, double price, String url, String dateAdded) {
		this.name = name;
		this.currentPrice = price;
		this.originalPrice = price;
		this.url = url;
		this.priceChange = 0.0;
		this.dateAdded = dateAdded;
	}
	
	public Item() {
	}
	
	public void updatePrice(double newPrice) {
		this.priceChange = ((newPrice - this.originalPrice) / (this.originalPrice)) * 100;
		this.currentPrice = newPrice;
	}
	
	public double getOriginalPrice() {
		return this.originalPrice;
	}
	
	public double getCurrentPrice() {
		return this.currentPrice;
	}
	
	public double getPriceChange() {
		return this.priceChange;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDateAdded() {
		return this.dateAdded;
	}
	
	@Override
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
	
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public void setOriginalPrice(double newPrice) {
		this.originalPrice = newPrice;
	}
	
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setURL(String url) {
		this.url = url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
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
