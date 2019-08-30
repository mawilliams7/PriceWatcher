package pricewatcher.base;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import pricewatcher.base.Main.MyThread;

@SuppressWarnings("serial")
public class NewMain extends Main{
    public static void main(String[] args) {
    	new NewMain();
    }
    /** Populates the menu bar for the GUI. 
     * @throws IOException
     * */
    @Override
    protected void setMenuBar() throws IOException {
    	menuBar = new JMenuBar();

    	JMenu menu = new JMenu("Item");
    	menu.setMnemonic(KeyEvent.VK_A);
    	menu.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");
    	menuBar.add(menu);

    	//A group of JMenuItems
    	JMenuItem checkPrices = new JMenuItem("Check prices",
    	                         KeyEvent.VK_T);
    	checkPrices.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_1, ActionEvent.ALT_MASK));
    	checkPrices.addActionListener(e -> {
        	Thread helper = new Thread(new MyThread());
        	helper.start();
    	});
    	checkPrices.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/update.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    	menu.add(checkPrices);
    	
    	JMenuItem addItem = new JMenuItem("Add Item",
		                KeyEvent.VK_T);
		addItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		addItem.addActionListener(e ->addItem());
		addItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/add.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    	menu.add(addItem);	
    }
    /** Updates the prices of all the items.
     * @throws InterruptedException 
     * */
    @Override
    protected void updateAllPrices() {
    	int increment = 100 / this.itemManager.getAllItems().size();
    	for(Item iter: this.itemManager.getAllItems()) {
    		progressBar.setValue(progressBar.getValue() + increment);
    		updatePrice(iter);
    	}
    	progressBar.setValue(0);
    	super.repaint();
    }   
    /**Nested class for the thread used to updated all prices.*/
    class MyThread implements Runnable {
    	/**Updates item prices and the progress bar*/
    	public void run() {
	    	try {
				Thread.sleep(250);
			} 
	    	catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	synchronized(this) {
			   	progressBar.setVisible(true);
			    updateAllPrices();
			   	allNeedUpdate = false;
			   	progressBar.setVisible(false);
	    	}
    	}
    }
    
    /** Create a control panel consisting of a refresh button. 
     * @throws IOException 
     * @return A completely functional JToolBar
     * */
    @Override
    protected JToolBar makeToolBar() throws IOException {
    	JToolBar panel = new JToolBar();
    	// Creates the buttons for the tool bar
        JButton allPrices = new JButton();
        allPrices.setToolTipText("Update all prices");
        allPrices.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/update.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        allPrices.addActionListener(e -> {
        	Thread helper = new Thread(new MyThread());
        	helper.start();
        });
        allPrices.setFocusPainted(false);
        panel.add(allPrices);
        
        JButton addItem = new JButton();
        addItem.setToolTipText("Add an item");
        addItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/add.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        addItem.addActionListener(e -> addItem());
        addItem.setFocusPainted(false);
        panel.add(addItem);
        panel.add(Box.createHorizontalStrut(100));
        
        JButton updatePrice = new JButton();
        updatePrice.setToolTipText("Update Price of Selected Item");
        updatePrice.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/update.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        updatePrice.addActionListener(e -> updatePriceOfSelectedItem());
        updatePrice.setFocusPainted(false);
        panel.add(updatePrice);
        
        JButton accessWebpage = new JButton();
        accessWebpage.setToolTipText("Access webpage of Selected Item");
        accessWebpage.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/view.jpg")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        accessWebpage.addActionListener(e -> launchWebsiteOfSelectedItem());
        accessWebpage.setFocusPainted(false);
        panel.add(accessWebpage);
        
        JButton editItem = new JButton();
        editItem.setToolTipText("Edit Selected Item");
        editItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/edit.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        editItem.addActionListener(e -> editSelectedItem());
        editItem.setFocusPainted(false);
        panel.add(editItem);
        
        JButton removeItem = new JButton();
        removeItem.setToolTipText("Remove Selected Item");
        removeItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/remove.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        removeItem.addActionListener(e -> removeSelectedItem());
        removeItem.setFocusPainted(false);
        panel.add(removeItem);
        
        panel.add(Box.createHorizontalStrut(100));
        progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        progressBar.setValue(0);
        panel.add(progressBar);
        
        return panel;   
    }
}
