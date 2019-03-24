package pricewatcher.base;

import java.io.File;
import java.io.FileWriter;

public class FileItemManager extends ItemManager{
	private FileWriter writer;

	public FileItemManager() {
		loadItems();
	}
	
	@Override
	public void addItem(Item item) {
		super.addItem(item);
		try{
			String filename = "/PriceWatcher/src/storage/" + item.getURL().replaceAll("/", "") + ".json";
			System.out.println(filename);
			File file = new File(filename);
			System.out.println("Serendipity");
			file.createNewFile();
			writer = new FileWriter(file);
			writer.write((item.toJSON()).toString());
			writer.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	    
	}
	@Override
	public void removeItem(Item item) {
		super.removeItem(item);
	}
	
	public void loadItems() {
		
	}
}
