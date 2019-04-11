package pricewatcher.base;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class FileItemManager extends ItemManager{
	/** The file writer for the FileItemManager */
	private FileWriter writer;
	/** The constructor for the FileItemManager. */
	public FileItemManager() {
		loadItems();
	}
	
	@Override
	/** Adds an item to the item manager. 
	 * @param item The item to be added
	 * */
	public void addItem(Item item) {
		super.addItem(item);
		try{
			writeItem(item);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	    
	}
	
	/** Updates an item in the item manager. 
	 * @param item The item to be updated
	 * */
	public void updateItem(Item item) {
		try{
			writeItem(item);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	/** Removes an item from the item manager. 
	 * @param item The item to be removed
	 * */
	public void removeItem(Item item) {
		super.removeItem(item);
		deleteItemFile(item);
	}
	
	/** Removes an item file from storage. 
	 * @param item The item to be removed from storage
	 * */
	public void deleteItemFile(Item item){
		File file = new File("src/storage/" + item.getName().replaceAll(" ", "-") + ".json");
		file.delete();
	}
	
	/** Loads in items to manager from item files. 
	 * */
	public void loadItems() {
		File folder = new File("src/storage/");
		for(File file : folder.listFiles()) {
			if(readItemFromFile(file) != null) {
				super.addItem(readItemFromFile(file));
			}
		}
	}
	
	/** Reads an item from an item file. 
	 * @param file The file to be read
	 * @return The item read form the file or null if there was an error
	 * */
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
	
	/** Writes an item to an item file.
	 * @param item The item that will be written to a file
	 * @throws IOExcpetion
	 * */
	public void writeItem(Item item) throws IOException {
		String filename = "src/storage/" + item.getName().replaceAll(" ", "-") + ".json";
		File file = new File(filename);
		file.createNewFile();
		writer = new FileWriter(filename);
		writer.write((item.toJSON()).toString());
		writer.close();
	}
	
	/** Parses an item from a JSONObject 
	 * @param obj The JSONObject to be parsed
	 * */
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
