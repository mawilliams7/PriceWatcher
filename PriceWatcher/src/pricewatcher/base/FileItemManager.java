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
			String filename = "/storage/" + item.getName().replaceAll(" ", "-") + ".json";
			System.out.println(filename);
			File file = new File(filename);
			file.createNewFile();
			System.out.println("serendipity");
			writer = new FileWriter(filename);
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
