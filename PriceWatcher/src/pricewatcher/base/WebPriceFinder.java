package pricewatcher.base;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import org.jsoup.Jsoup;

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
		Document doc = (Document) Jsoup.connect(item.getURL()).timeout(0).get();
		Element link = doc.select("span.price-group").first();
		String priceAsString = link.text();
		priceAsString = priceAsString.replace("$", "");
		double newPrice = Double.parseDouble(priceAsString);
		return newPrice;
	}
	
	/**Gets the new price of an Amazon item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double amazonCase(Item item) throws IOException {
		Document doc = (Document) Jsoup.connect(item.getURL()). userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
	            .header("cookie", "incap_ses_436_255598=zI1vN7X6+BY84PhGvPsMBjKChVcAAAAAVhJ+1//uCecPhV2QjUMw6w==")
	            .timeout(0)
	            .get();;
		Element link = doc.select("#priceInsideBuyBox_feature_div").last();
		String priceAsString = link.text();
		priceAsString = priceAsString.replace("$", "");
		double newPrice = Double.parseDouble(priceAsString);
		return newPrice;
	}
	
	/**Gets the new price of an eBay item. 
	 * @param item The item whose price will be updated
	 * @return The new price of the item
	 * @throws IOException
	 * */
	public double ebayCase(Item item) throws IOException {
		Document doc = (Document) Jsoup.connect(item.getURL()).timeout(0).get();
		Element link = doc.select("#mm-saleDscPrc").last();
		String priceAsString = link.text();
		priceAsString = priceAsString.substring(priceAsString.indexOf("$") + 1);
		double newPrice = Double.parseDouble(priceAsString);
		return newPrice;
	}
}
