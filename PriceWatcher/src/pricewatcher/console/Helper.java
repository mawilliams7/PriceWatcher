package pricewatcher.console;

import java.io.IOException;

import pricewatcher.base.Item;
import pricewatcher.base.ItemManager;
import pricewatcher.base.PriceFinder;
import pricewatcher.base.WebPriceFinder;

public class Helper {
	public static void main(String[] args) {
		ItemManager itemManager = new ItemManager();
    	itemManager.addItem(new Item("Nerf Gun", 61.13, "https://www.walmart.com/ip/Nerf-Fortnite-AR-L-Nerf-Elite-Dart-Blaster-with-6-Nerf-Elite-Darts/354326531?athcpid=354326531&athpgid=athenaHomepage&athcgid=null&athznid=CC_Trending&athieid=&athstid=CS031&athguid=466001f5-46cfa622-9f64329c9a18a716&athena=true", "8/25/18"));
    	itemManager.addItem(new Item("Electric Winch", 129.99, "https://www.ebay.com/itm/X-BULL-12000LBS-Electric-Winch-12V-Towing-Truck-Trailer-Steel-Cable-Off-Road/332129189141?epid=676396295&hash=item4d54713d15:g:HmQAAOSwakxcRrdB", "9/11/12"));
    	itemManager.addItem(new Item("Acoustic Guitar", 129.99, "https://www.amazon.com/gp/product/B01K18HFOY?pf_rd_p=1cac67ce-697a-47be-b2f5-9ae91aab54f2&pf_rd_r=8RYM75MMBQ3ENKHF28MQ", "9/11/12"));
    	PriceFinder priceFinder = new WebPriceFinder();
    	for(Item iter: itemManager.getAllItems()) {
    		try {
    			priceFinder.getNewPrice(iter);
    		}
    		catch (IOException e) {
    			
    		}
    	}
    	
	}
}
