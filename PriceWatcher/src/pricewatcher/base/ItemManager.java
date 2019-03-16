package pricewatcher.base;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
	public List<Item> items;
	
	public ItemManager() {
		this.items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public void removeItem(Item item) {
		this.items.remove(item);
	}
	
	public List<Item> getAllItems(){
		return this.items;
	}
	
}
