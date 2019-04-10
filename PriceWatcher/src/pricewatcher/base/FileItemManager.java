package pricewatcher.base;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class FileItemManager extends ItemManager{
	private FileWriter writer;

	public FileItemManager() {
		loadItems();
	}
	
	@Override
	public void addItem(Item item) {
		super.addItem(item);
		try{
			writeItem(item);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	    
	}
	
	public void updateItem(Item item) {
		try{
			writeItem(item);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void removeItem(Item item) {
		super.removeItem(item);
		deleteItemFile(item);
	}
	
	public void deleteItemFile(Item item){
		File file = new File("src/storage/" + item.getName().replaceAll(" ", "-") + ".json");
		file.delete();
	}
	
	public void loadItems() {
		File folder = new File("src/storage/");
		for(File file : folder.listFiles()) {
			if(readItemFromFile(file) != null) {
				super.addItem(readItemFromFile(file));
			}
		}
	}
	
	public Item readItemFromFile(File file) {
		 try {
			 JSONTokener parser = new JSONTokener(new FileReader(file));
			 JSONObject obj = (JSONObject) parser.nextValue();
			 JSONObject itemJSON = (JSONObject) obj;
			 Item item = fromJSON(itemJSON);
			 return item;
		 }
		 catch (Exception e) {
			 System.out.println(e);
			 return null;
		 }
	}
	
	public void writeItem(Item item) throws IOException {
		String filename = "src/storage/" + item.getName().replaceAll(" ", "-") + ".json";
		System.out.println(filename);
		File file = new File(filename);
		file.createNewFile();
		writer = new FileWriter(filename);
		writer.write((item.toJSON()).toString());
		writer.close();
	}
	
	public Item fromJSON(JSONObject obj) {
		try {
			String name = obj.getString("name");
			float originalPrice = (float) obj.getDouble("originalPrice");
			float currentPrice = (float) obj.getDouble("currentPrice");
		    String dateAdded = obj.getString("dateAdded");
		    String url = obj.getString("url");
		    Item item = new Item(name, originalPrice, url, dateAdded);
		    item.updatePrice(currentPrice);
		    return item;
		}
		catch (Exception e) {
			return null;
		}
	}
}
