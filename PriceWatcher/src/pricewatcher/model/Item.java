package pricewatcher.model;

public class Item {

	public String name;
	public double originalPrice;
	public double currentPrice;
	public double priceChange;
	public String url;
	public String dateAdded;
	
	public Item(String name, double price, String url, String dateAdded) {
		this.name = name;
		this.currentPrice = price;
		this.originalPrice = price;
		this.url = url;
		this.priceChange = 0.0;
		this.dateAdded = dateAdded;
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
	

}
