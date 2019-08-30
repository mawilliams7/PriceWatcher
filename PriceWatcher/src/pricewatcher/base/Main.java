/* Mark Williams
 * 3-4-2019
 * CS 3331
 */


package pricewatcher.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Main extends JFrame {

    /** Default dimension of the GUI. */
    private final static Dimension DEFAULT_SIZE = new Dimension(1250, 1000);
      
    /** Item Manager for the GUI. */
    protected FileItemManager itemManager;
    
    /** Item that the user is currently selecting. */
    private Item selectedItem;
    
    /** Price Finder for the items.*/
    protected WebPriceFinder priceFinder;
    
    /** Menu bar for the GUI. */
    protected JMenuBar menuBar;
    
    /** Progress bar for the GUI. */
    protected JProgressBar progressBar;
    
    protected boolean allNeedUpdate;
    
    /** The list model for the JList. */
    private DefaultListModel<Item> listModel;
    
    /** The JList for the GUI. */
    private JList<Item> jList;

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel(" ");

    /** Create a new dialog. */
    public Main() {
    	this(DEFAULT_SIZE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        showMessage("Welcome!");
    }

    /** Create a new dialog of the given screen dimension. 
     * @param dim The dimension of the screen the GUI is being displayed on
     * */
    public Main(Dimension dim) {
    	super("Price Watcher");
    	this.priceFinder = new WebPriceFinder();
    	// Creates the menu bar for the Price Watcher
    	try {
    		setMenuBar();  
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
        setSize(dim);
        setResizable(true);
        initializeItemManager();
        intializeListModel();
        configureUI();
    }
    /** Initializes the list model using the Item Manager. */
    protected void intializeListModel() {
        listModel = new DefaultListModel<Item>();
        for(Item iter: this.itemManager.getAllItems()) {
        	listModel.addElement(iter);
        }
    	
    }
    /** Uses an audio clip to alert the user if a price of an item has dropped. */
    protected void alertPriceDropped() {     
    	try {
    		// Open an audio input stream.           
    		AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/sound/sample.wav"));              
    		// Get a sound clip resource.
    		Clip clip = AudioSystem.getClip();
    		// Open audio clip and load samples from the audio input stream.
    		clip.open(audioIn);
    		clip.start();
    	} 
    	catch (UnsupportedAudioFileException e) {
    		e.printStackTrace();
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
    	} 
    	catch (LineUnavailableException e) {
    		e.printStackTrace();
    	}
    }
    
    /** Configures UI for the GUI. */
    protected void configureUI() {
        setLayout(new BorderLayout());
        // Creates the tool bar
        JToolBar toolBar = new JToolBar();
        try {
        	toolBar = makeToolBar();
        }
        catch(IOException e) {
        	
        }
        toolBar.setBorder(BorderFactory.createEmptyBorder(10,16,0,16)); 
        add(toolBar, BorderLayout.NORTH);
        // Creates the board that display item
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(10,16,0,16),
        		BorderFactory.createLineBorder(Color.GRAY)));
        board.setLayout(new GridLayout(1,1));
        jList = new JList<Item>(listModel);
        jList.setCellRenderer(new ItemView());
        // The Mouse Listener for the Item 
        MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                  Item item = (Item) o;
                  try {
                	  popUpRequested(item, mouseEvent.getPoint());
                  }
                  catch(IOException e) {
                	
                  }
                }
              }
              if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                  int index = theList.locationToIndex(mouseEvent.getPoint());
                  if (index >= 0) {
                    Object o = theList.getModel().getElementAt(index);
                    Item item = (Item) o;
                    setSelectedItem(item);
                  }
                }
            }
          };
        jList.addMouseListener(mouseListener);
        board.add(new JScrollPane(jList));
        this.setJMenuBar(menuBar);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
        super.repaint();
    }
    
    /** Populates the menu bar for the GUI. 
     * @throws IOException
     * */
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
    
    /** Handles pop-ups for the user. 
     * @param item The item that is being selected
     * @param poin The point where the user requested the pop-up
     * */
    protected void popUpRequested(Item item, Point point) throws IOException { 
        JPopupMenu pm = new JPopupMenu();
        JMenuItem m1 = new JMenuItem("Get Current Price");
        m1.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/update.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JMenuItem m2 = new JMenuItem("View Webpage");
        m2.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/view.jpg")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JMenuItem m3 = new JMenuItem("Edit Item");
        m3.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/edit.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JMenuItem m4 = new JMenuItem("Remove Item");
        m4.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/image/remove.PNG")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        pm.add(m1); 
        pm.add(m2); 
        pm.add(m3);  
        pm.add(m4);
        // Adds actions listeners to JMenuItems
        m1.addActionListener(e -> updatePrice(item)); 
        m2.addActionListener(e -> launchWebsite(item)); 
        m3.addActionListener(e -> editItem(item));
        m4.addActionListener(e -> removeItem(item)); 
        pm.show(this, point.x, point.y); 
    } 
      
    /** Create a control panel consisting of a refresh button. 
     * @throws IOException 
     * @return A completely functional JToolBar
     * */
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
    /** Updates price of selected item. 
     * */
    protected void updatePriceOfSelectedItem(){
    	if(selectedItem != null) {
    		updatePrice(selectedItem);
    	}
    	else {
    		showMessage("No item selected.");
    	}
    }
    
    /** Launches website of selected item.
     * */
    protected void launchWebsiteOfSelectedItem(){
    	if(selectedItem != null) {
    		launchWebsite(selectedItem);
    	}
    	else {
    		showMessage("No item selected.");
    	}
    }
    
    /** Allows user to edit selected item.
     * */
    protected void editSelectedItem(){
    	if(selectedItem != null) {
    		editItem(selectedItem);
    	}
    	else {
    		showMessage("No item selected.");
    	}
    }
    
    /** Allows user to remove selected item.
     * */
    protected void removeSelectedItem(){
    	if(selectedItem != null) {
    		removeItem(selectedItem);
    	}
    	else {
    		showMessage("No item selected.");
    	}
    }
    /** Updates the price of an item.
     * @param item The item to be updated
     * */
    protected void updatePrice(Item item) {
    	try {
	    	item.updatePrice(this.priceFinder.getNewPrice(item));
	    	itemManager.updateItem(item);
	    	if(item.getPriceChange() < 0) {
	    		alertPriceDropped();
	    	}
	    	super.repaint();
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		showMessage("Unable to update price.");
    	}
    }
    
    /** Adds item based on user input.
     * */
    protected void addItem() {

        JDialog dialog = new JDialog(this, "Adding item"); 
        
        JTextField itemNameField = new JTextField(10);
        JTextField itemURLField = new JTextField(10);
        JTextField itemPriceField = new JTextField(10);
        JTextField itemDateAddedField = new JTextField(getCurrentDate());
        
        // Sets the buttons for the dialog
        JButton add = new JButton("Add");
        JButton cancel = new JButton("Cancel");
        
        add.addActionListener(event -> {
        	Item newItem = new Item();
        	newItem.setName(itemNameField.getText());
        	newItem.setURL(itemURLField.getText());
        	newItem.setOriginalPrice(Double.parseDouble(itemPriceField.getText()));
        	newItem.setCurrentPrice(Double.parseDouble(itemPriceField.getText()));
        	newItem.setDateAdded(itemDateAddedField.getText());
        	this.itemManager.addItem(newItem);
        	listModel.addElement(newItem);
        	super.repaint();
        	dialog.dispose();
        	super.repaint();
        });
        cancel.addActionListener(event -> {
        	dialog.dispose();
        });
        
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("New item name:"));
        myPanel.add(itemNameField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("New item URL:"));
        myPanel.add(itemURLField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("New item price:"));
        myPanel.add(itemPriceField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("New date added:"));
        myPanel.add(itemDateAddedField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(Box.createHorizontalStrut(100));
        myPanel.add(add);
        myPanel.add(cancel);
        dialog.add(myPanel);
        dialog.setSize(200, 300); 
        dialog.setVisible(true);

    }
    
    /** Gets the current date
     * @return The current date in 'MM/dd/yyyy' format
     * */
    protected String getCurrentDate() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
    	LocalDateTime now = LocalDateTime.now();
    	return dtf.format(now);
    	
    }
    
    /** Updates the prices of all the items.
     * @throws InterruptedException 
     * */
    protected void updateAllPrices() {
    	int increment = 100 / this.itemManager.getAllItems().size();
    	for(Item iter: this.itemManager.getAllItems()) {
    		progressBar.setValue(progressBar.getValue() + increment);
    		updatePrice(iter);
    	}
    	progressBar.setValue(0);
    	super.repaint();
    }   
    
    /** Launches website of selected item.
     * @param item The item to be removed
     * */
    protected void removeItem(Item item) {
    	itemManager.removeItem(item);
        if (listModel.size() > 0) {
        	listModel.removeElement(item);
        }
        super.repaint();
    }
    /** Allows the user to edit an item.
     * @param item The item to be edited
     * */
    protected void editItem(Item item) {
    	JDialog dialog = new JDialog(this, "Editing item"); 
        JTextField itemNameField = new JTextField(item.getName());
        JTextField itemURLField = new JTextField(item.getURL());

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Item name:"));
        myPanel.add(itemNameField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(Box.createHorizontalStrut(30));
        myPanel.add(new JLabel("Item URL:"));
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(itemURLField);
        dialog.add(myPanel);
        
        JButton edit = new JButton("Edit");
        JButton cancel = new JButton("Cancel");
        
        edit.addActionListener(event -> {
        	itemManager.deleteItemFile(item);
            item.setName(itemNameField.getText());
            item.setURL(itemURLField.getText());
            this.itemManager.updateItem(item);
            dialog.dispose();
            super.repaint();
        });
        cancel.addActionListener(event -> {
        	dialog.setVisible(false);
        	super.repaint();
        	dialog.dispose();
        });
        
        myPanel.add(edit);
        myPanel.add(cancel);
        
        dialog.setSize(500, 200); 
        dialog.setVisible(true);
    }
    /** Launches website of an item.
     * @param item The item whose website the user wants to go to
     * */
	protected void launchWebsite(Item item) {
		/* Launches item web sites in user's default browser*/
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(item.getURL()));
				showMessage("Webpage opened in your default browser.");
			}
			catch (IOException | URISyntaxException e) {
				showMessage("Unable to access webpage for " + item.getName() + ".");
			}
		}
		else {
			showMessage("Unable to access webpage for " + item.getName() + ".");
		}
	}

    /** Show briefly the given string in the message bar. 
     * @param msg The message to be displayed
     * */
    protected void showMessage(String msg) {
        msgBar.setText(msg);
        new Thread(() -> {
        	try {
				Thread.sleep(3 * 1000); // 3 seconds
			} catch (InterruptedException e) {
			}
        	if (msg.equals(msgBar.getText())) {
        		SwingUtilities.invokeLater(() -> msgBar.setText(" "));
        	}
        }).start();
    }
    
    /** Sets the selected item
     * @item item The item the user has selected
     * */
    protected void setSelectedItem(Item item) {
    	this.selectedItem = item;
    }
    
    /** Initializes the item manager
     * */
    protected void initializeItemManager() {
    	this.itemManager = new FileItemManager();
    }
}
