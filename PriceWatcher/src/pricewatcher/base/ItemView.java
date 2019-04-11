package pricewatcher.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics; 
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/** A special panel to display the detail of an item. */

@SuppressWarnings("serial")
public class ItemView extends JPanel implements ListCellRenderer<Item>{
	
	/** The item currently being used by the ItemView. */
	private Item item;

    /** Create a new instance. */
    public ItemView() {
    	Dimension size = getSize();
    	setOpaque(true);
    	this.setPreferredSize(new Dimension(size.width, 160));
    }

    /** Overridden here to display the details of the item. 
     * @param g The graphics object used to paint items
     * */
    @Override
	public void paint(Graphics g) {
    	super.paint(g);
    	int x = 20;         
    	int y = 30; 
    	g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("Name:      " + item.getName(), x, y);
        y += 20;
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("URL:         " + item.getURL(), x, y);
        y += 20;
        g.drawString("Price:         ", x, y);
        g.setColor(Color.blue);
        g.drawString("$" + String.format("%.2f", item.getCurrentPrice()), x + 89, y);
        y += 20;
        g.setColor(Color.BLACK);
        g.drawString("Change:      ", x, y);
        if(item.getPriceChange() < 0) {
        	g.setColor(Color.green);
        }
          
        else if (item.getPriceChange()==0) {
        	g.setColor(Color.BLACK);
        }
        else {
        	g.setColor(Color.red);
        }
        g.drawString(String.format("%.2f", item.getPriceChange()) +"%", x + 99, y);
        y += 20;
        g.setColor(Color.black);
        g.drawString("Added:      " + item.getDateAdded() + " ($" + String.format("%.2f", item.getOriginalPrice()) + ")", x, y);
    }
    
    @Override
    /** Overridden here to display the details of the item. 
     * @param list The JList that contains items
     * @param value The item that needs to be painted
     * @param index The index of the item that is selected in the list
     * @param isSelected Boolean for if the item is selected
     * @param cellHasFocus Boolean for is the given cell has focus
     * @return The rendered component
     * */
	public Component getListCellRendererComponent(JList<? extends Item> list, Item value, int index, boolean isSelected, boolean cellHasFocus) {
        setItem(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } 
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
		return this;
	}
    
    /**Sets the item to be painted.*/
    public void setItem(Item item) {
    	this.item = item;
    }
}
