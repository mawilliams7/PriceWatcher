package pricewatcher.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/** A special panel to display the detail of an item. */

@SuppressWarnings("serial")
public class ItemView extends JPanel implements ListCellRenderer<Item>{
	
	private Item item;
	
	/** Directory for image files: src/image in Eclipse. */
	private final static String IMAGE_DIR = "/image/";
        
    /** Create a new instance. */
    public ItemView() {
    	Dimension size = getSize();
    	setOpaque(true);
    	this.setPreferredSize(new Dimension(size.width, 160));
    }

    /** Overridden here to display the details of the item. */
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
        g.drawString("Added:      " + item.getDateAdded() + " ($" + item.getOriginalPrice() + ")", x, y);
    }
    
    @Override
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
    
    /** Return the image stored in the given file. */
    public Image getImage(String file) {
        try {
        	URL url = new URL(getClass().getResource(IMAGE_DIR), file);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Item getItem() {
    	return this.item;
    }
    
    public void setItem(Item item) {
    	this.item = item;
    }
}
