package pricewatcher.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


public class WebPriceFinder extends PriceFinder{
	/** The constructor for the WebPriceFinder.*/
	public WebPriceFinder() {
	}
	
	@Override
	/**Gets the new price of an item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double getNewPrice(Item item) throws IOException {
		if(item.getURL().contains("walmart.com")) {
			return walmartCase(item);
		}
		if(item.getURL().contains("amazon.com")) {
			return amazonCase(item);
		}
		if(item.getURL().contains("ebay.com")) {
			return ebayCase(item);
		}
		return item.getCurrentPrice();
	}
	/**Gets the new price of a Walmart item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double walmartCase(Item item) throws IOException {
		String pattern = "(\\=\"\\d+\\.\\d\\d\\\")";
		double newPrice = getPrice(pattern, item.getURL());
		// If there was an error in getting the price this code will execute
		if (newPrice == -1) {  
			item.getCurrentPrice(); 
		}
		return newPrice;
	}
	
	/**Gets the new price of an Amazon item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double amazonCase(Item item) throws IOException {
		String pattern = "(\\=\"\\d+\\.\\d\\d\\\")";
		double newPrice = getPrice(pattern, item.getURL());
		// If there was an error in getting the price this code will execute
		if (newPrice == -1) {  
			item.getCurrentPrice(); 
		}
		return newPrice;
	}
	
	/**Gets the new price of an eBay item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double ebayCase(Item item) throws IOException {
		String pattern = "(\\:\"\\d+\\.\\d\\d\\\")";
		double newPrice = getPrice(pattern, item.getURL());
		// If there was an error in getting the price this code will execute
		if (newPrice == -1) {  
			item.getCurrentPrice(); 
		}
		return newPrice;
	}
	
	public double getPrice(String pattern, String address) throws IOException{
		HttpURLConnection con = null;
		URL url = new URL(address);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36");
		String encoding = con.getContentEncoding();
		if (encoding == null) { encoding = "utf-8"; }
		InputStreamReader reader = null;
		if ("gzip".equals(encoding)) {
		  	reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
		} 
		else {
		  	reader = new InputStreamReader(con.getInputStream(), encoding);
		}
		BufferedReader in = new BufferedReader(reader);
		String line;
		while ((line = in.readLine()) != null) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(line);
			if(m.find()) {
				String newPrice = m.group(1);
				if(newPrice.contains(":")) {
					newPrice = newPrice.replace(":", "").replaceAll("\"", "");
				}
				if(newPrice.contains("=")) {
					newPrice = newPrice.replace("=", "").replaceAll("\"", "");;
				}
				return Double.parseDouble(newPrice);
			}
		}
		if (con != null) {  
			con.disconnect(); 
		}
		return -1;
	}
}
