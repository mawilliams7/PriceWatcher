package pricewatcher.base;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
	/** The list of items in the item manager. */
	private List<Item> items;
	
	/** Creates a new ItemManager. */
	public ItemManager() {
		this.items = new ArrayList<Item>();
	}
	
	/** Adds an item to the item manager. 
	 * @param item The item to be added
	 * */
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	/** Removes an item to the item manager. 
	 * @param item The item to be removed
	 * */
	public void removeItem(Item item) {
		this.items.remove(item);
	}
	
	/** Gets the complete list of items from the ItemManager. 
	 * @return The contents of the ItemManager list
	 * */
	public List<Item> getAllItems(){
		return this.items;
	}
}
