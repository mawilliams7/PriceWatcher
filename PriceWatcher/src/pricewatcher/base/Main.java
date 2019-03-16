/* Mark Williams
 * 3-4-2019
 * CS 3331
 */


package pricewatcher.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Main extends JFrame {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_SIZE = new Dimension(1250, 1000);
      
    /** Special panel to display the watched item. */
    private ItemManager itemManager;
    
    private Item selectedItem;
    
    private PriceFinder priceFinder;

    private JMenuBar menuBar;
    
    private JToolBar toolBar;
    
    private DefaultListModel<Item> listModel;
    
    private JList<Item> jList;
      
    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel(" ");

    /** Create a new dialog. */
    public Main() {
    	this(DEFAULT_SIZE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        showMessage("Welcome!");
    	configureUI();
    }

    /** Create a new dialog of the given screen dimension. */
    public Main(Dimension dim) {
    	super("Price Watcher");
    	this.priceFinder = new PriceFinder();
    	setMenuBar();
    	setToolBar();
        setSize(dim);
        initializeItemManager();
        intializeListModel();
        configureUI();
    }
    
    private void intializeListModel() {
        listModel = new DefaultListModel<Item>();
        for(Item iter: this.itemManager.getAllItems()) {
        	listModel.addElement(iter);
        }
    	
    }
    private void alertPriceDropped() {     
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
    
    /** Configure UI. */
    private void configureUI() {
        setLayout(new BorderLayout());
        JPanel control = makeControlPanel();
        control.setBorder(BorderFactory.createEmptyBorder(10,16,0,16)); 
        add(control, BorderLayout.NORTH);
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(10,16,0,16),
        		BorderFactory.createLineBorder(Color.GRAY)));
        board.setLayout(new GridLayout(1,1));
        jList = new JList<Item>(listModel);
        jList.setCellRenderer(new ItemView());
        MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                  Item item = (Item) o;
                  popUpRequested(item, mouseEvent.getPoint(), index);
                }
              }
            }
          };
        jList.addMouseListener(mouseListener);
        board.add(new JScrollPane(jList));
        this.setJMenuBar(menuBar);
        control.add(toolBar);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
        super.repaint();
    }
    
    private void addButtons(JToolBar toolBar) {
        JButton button = new JButton();
        button.setActionCommand("Make");
        button.setToolTipText("Test");
        toolBar.add(button);
    }
    
    private void setMenuBar() {
    	menuBar = new JMenuBar();

    	JMenu menu = new JMenu("Item");
    	menu.setMnemonic(KeyEvent.VK_A);
    	menu.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");
    	menuBar.add(menu);

    	//a group of JMenuItems
    	JMenuItem checkPrices = new JMenuItem("Check prices",
    	                         KeyEvent.VK_T);
    	checkPrices.setAccelerator(KeyStroke.getKeyStroke(
    	        KeyEvent.VK_1, ActionEvent.ALT_MASK));
    	checkPrices.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
            	updateAllPrices();
            } 
        });
    	menu.add(checkPrices);
    	JMenuItem addItem = new JMenuItem("Add Item",
		                KeyEvent.VK_T);
		addItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		addItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) 
			{ 
					addItem();
			} 
		});
    	menu.add(addItem);
    }
    
    private void setToolBar() {
    	toolBar = new JToolBar("Test");
    	addButtons(toolBar);
    }

    public void popUpRequested(Item item, Point point, int index) { 
        JPopupMenu pm = new JPopupMenu();
        JMenuItem m1 = new JMenuItem("Get Current Price"); 
        JMenuItem m2 = new JMenuItem("View Webpage"); 
        JMenuItem m3 = new JMenuItem("Edit Item"); 
        JMenuItem m4 = new JMenuItem("Remove Item");
        pm.add(m1); 
        pm.add(m2); 
        pm.add(m3); 
        pm.add(m4);
        
        m1.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
            	updatePrice(item);
            } 
        }); 
  
        m2.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
            	launchWebsite(item);
            } 
        }); 
  
        m3.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
               editItem(item);
            } 
        });
        
        m4.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
                removeItem(item, index);
            } 
        }); 
        pm.show(this, point.x, point.y); 
    } 
      
    /** Create a control panel consisting of a refresh button. */
    private JPanel makeControlPanel() {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        return panel;
    }
    
    private void updatePrice(Item item) {
    	item.updatePrice(this.priceFinder.getNewPrice(item.getURL()));
    	if(item.getPriceChange() < 0) {
    		alertPriceDropped();
    	}
    }
    
    private void addItem() {
        JTextField itemNameField = new JTextField(10);
        JTextField itemURLField = new JTextField(10);
        JTextField itemPriceField = new JTextField(10);
        JTextField itemDateAddedField = new JTextField(getCurrentDate());

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("New item name:"));
        myPanel.add(itemNameField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("new item URL:"));
        myPanel.add(itemURLField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("New item price:"));
        myPanel.add(itemPriceField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("New date added:"));
        myPanel.add(itemDateAddedField);
        add(myPanel);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Enter edits", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        	Item newItem = new Item();
        	newItem.setName(itemNameField.getText());
        	newItem.setURL(itemURLField.getText());
        	newItem.setOriginalPrice(Double.parseDouble(itemPriceField.getText()));
        	newItem.setCurrentPrice(Double.parseDouble(itemPriceField.getText()));
        	newItem.setDateAdded(itemDateAddedField.getText());
        	this.itemManager.addItem(newItem);
        	for(Item iter: this.itemManager.getAllItems()) {
        		System.out.println(iter.getName());
        	}
        	listModel.addElement(newItem);
        	super.repaint();
        }
    }
    
    private String getCurrentDate() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
    	LocalDateTime now = LocalDateTime.now();
    	return dtf.format(now);
    	
    }
    
    private void updateAllPrices() {
    	for(Item iter: this.itemManager.getAllItems()) {
    		iter.updatePrice(this.priceFinder.getNewPrice(iter.getURL()));
        	if(iter.getPriceChange() < 0) {
        		alertPriceDropped();
        	}
    	}
    	super.repaint();
    }
    
    private void removeItem(Item item, int index) {
    	itemManager.removeItem(item);
        if (listModel.size() > 0) {
        	listModel.remove(index);
        }
        super.repaint();
    }
    
    private void editItem(Item item) {
        JTextField itemNameField = new JTextField(item.getName());
        JTextField itemURLField = new JTextField(item.getURL());

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Item name:"));
        myPanel.add(itemNameField);
        myPanel.add(Box.createVerticalStrut(30));
        myPanel.add(new JLabel("Item URL:"));
        myPanel.add(itemURLField);
        add(myPanel);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Enter edits", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        	item.setName(itemNameField.getText());
        	item.setURL(itemURLField.getText());
        }
        super.repaint();
    }
    
	private void launchWebsite(Item item) {
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

    /** Show briefly the given string in the message bar. */
    private void showMessage(String msg) {
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
    
    private void initializeItemManager() {
    	this.itemManager = new ItemManager();
    	itemManager.addItem(new Item("LED monitor", 61.13, "https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022", "8/25/18"));
    	itemManager.addItem(new Item("Fire TV Edition", 129.99, "https://www.bestbuy.com/site/toshiba-32-class-led-720p-smart-hdtv-fire-tv-edition/6211003.p?skuId=6211003", "9/11/12"));
    	itemManager.addItem(new Item("Test", 129.99, "https://www.bestbuy.com/site/toshiba-32-class-led-720p-smart-hdtv-fire-tv-edition/6211003.p?skuId=6211003", "9/11/12"));
    }
        
    public static void main(String[] args) {
    	new Main();
    }

}
