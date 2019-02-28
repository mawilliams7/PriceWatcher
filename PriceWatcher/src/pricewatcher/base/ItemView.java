package pricewatcher.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pricewatcher.model.Item;

/** A special panel to display the detail of an item. */

@SuppressWarnings("serial")
public class ItemView extends JPanel {
	
	private List<Item> itemList;
    
	/** Interface to notify a click on the view page icon. */
	public interface ClickListener {
		
		/** Callback to be invoked when the view page icon is clicked. */
		void clicked();
	}
	
	/** Directory for image files: src/image in Eclipse. */
	private final static String IMAGE_DIR = "/image/";
        
	/** View-page clicking listener. */
    private ClickListener listener;
    
    /** Create a new instance. */
    public ItemView(List<Item> itemList) {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if (isViewPageClicked(e.getX(), e.getY()) && listener != null) {
            		listener.clicked();
            	}
            }
        });
        this.itemList = itemList;
    }
        
    /** Set the view-page click listener. */
    public void setClickListener(ClickListener listener) {
    	this.listener = listener;
    }
    
    /** Overridden here to display the details of the item. */
    @Override
	public void paint(Graphics g) {
        super.paint(g); 
        int x = 20, y = 20;
        for (Item item : this.itemList) {
        	g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        	g.drawImage(getImage("view.jpg"), x, y, null);
        	y += 50;
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
	        y += 220;
        }
    }
    
    /** Return true if the given screen coordinate is inside the viewPage icon. */
    private boolean isViewPageClicked(int x, int y) {
    	return new Rectangle(20, 20, 20, 20).contains(x,  y);
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
}
