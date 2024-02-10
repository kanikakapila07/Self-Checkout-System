package com.autovend.software;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.autovend.devices.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

/** Iternation #3 
Muhammad Asjad Zubair – 30147898,  
Rohit Nair – 30142471, 
Desmond O’Brien - 30064340 
Grace Kim - 30062591 
Ryan Chrumka – 30144174   
Paige So’Brien - 30046397  
Pratham Pandey – 30133275 
Rylan Laplante – 30700936  
Mohammad Ibrahim Khan – 30103764 
Dylan Tuttle – 30038835 
Ben Foster – 30094638 
Robert Engel – 30119708 
Diane Doan – 30052326 
Justin Chu – 30162809 
Theodore Lun – 10184905 
Jeremy Thomas – 30149098 
Lucy OuYang – 30140886 
Kanika Kapila – 30153349  
Gaurav Gulati – 30121866 
Jinsu An – 30086178 
Karanjot Bassi – 30094007 
Akib Hasan Aryan- 30141456 
Sean Robertson – 10065949 
Smitkumar Saraiya – 30151834 
Muhtadi Alam- 30150910 
*/ 

public class AttendantGUI {

	public JFrame frame;
	private AttendantStationLogic attendant;
	public JTextPane blockedPane;
	private JTextPane errorMSG;
	
	public JTabbedPane tabbedPane;
	
	private JPanel menuPanel;
	
	public JLabel blockedLabel;
	public JLabel addItemLabel;
	
	public JButton button;
	public JButton logoutButton;
	public JButton preventUse;
	public JButton removeButton;
	public JButton fillPaper;
	public JButton fillInk;
	public JButton printReceipt;
	public JButton shutdowonStatupButton;
	public JButton approveButton;

	//public string

	/**
	 * Create the application.
	 */
	public AttendantGUI(AttendantStationLogic attend) {
		attendant = attend;
		attendant.registerGUI(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
				
		frame = new JFrame();
		//frame.setBounds(100, 100, 973, 571);
		frame.setSize(1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel headerPannel = new JPanel();
		headerPannel.setBackground(new Color(0, 0, 0));
		headerPannel.setForeground(new Color(47, 255, 37));
		headerPannel.setBounds(0, 0, 973, 29);
		frame.getContentPane().add(headerPannel);
		
		menuPanel = new JPanel();
		menuPanel.setBackground(new Color(44, 255, 48));
		menuPanel.setBounds(0, 24, 217, 519);
		frame.getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(53, 463, 117, 29);
		logoutButton.addMouseListener(new MouseAdapter() {
     	   @Override
			public void mousePressed(MouseEvent e) {
				frame.dispose();
				KeyboardLogin window = new KeyboardLogin(attendant);
				window.frame.setVisible(true);
			}
        });
		menuPanel.add(logoutButton);
		blockedPane = new JTextPane();
		blockedPane.setEditable(false);
		blockedPane.setBounds(39, 45, 117, 247);
		menuPanel.add(blockedPane);
		
		blockedLabel = new JLabel("Blocked/Disabled:");
		blockedLabel.setBounds(39, 23, 131, 16);
		menuPanel.add(blockedLabel);
		updateStationBranch();
	}
	public void updateBlockedList(Map<Integer, SelfCheckoutSystemLogic> blockedStations) {
		
		int iter = 0;
		String setText = "";
		for (Entry<Integer, SelfCheckoutSystemLogic> entry : blockedStations.entrySet()) {
			int blocked = entry.getKey();
			String a = "Station #"+blocked;
			setText = setText + a +"\n";
		}
		blockedPane.setText(setText);
		updateStationBranch();
	}
	
	public void updateStationBranch(){
		
		
		if (tabbedPane != null) {
		frame.getContentPane().remove(tabbedPane);
		}
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(218, 28, 1200, 800);
		frame.getContentPane().add(tabbedPane);
		for (int i =0; i < attendant.stations.size(); i++) {
			JTextField textField = new JTextField();
			int num = i+1;
			SelfCheckoutSystemLogic station = attendant.stations.get(i);
			JPanel stationPannel = new JPanel();
			tabbedPane.addTab("Station #"+num, null, stationPannel, null);
			stationPannel.setLayout(null);
			stationPannel.setSize(1200, 800);
			JLabel idLabel = new JLabel("Station #"+num);
			idLabel.setBounds(6, 6, 86, 16);
			stationPannel.add(idLabel);
			
			JLabel addItemLabel = new JLabel("Add item to bill:");
			addItemLabel.setBounds(482, 250, 125, 16);
			stationPannel.add(addItemLabel);
			
			JPanel keyBoard = new JPanel();
			keyBoard.setBounds(390, 306, 400, 200);
			keyBoard.setSize(400, 200);
			stationPannel.add(keyBoard);
			keyBoard.setLayout(new GridLayout(4, 10, 0, 0));
			
			String[] buttonLabels = {
		            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
		            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
		            "A", "S", "D", "F", "G", "H", "J", "K", "L", "Backspace",
		            "Z", "X", "C", "V", "B", "N", "M", ".", "@", "Enter"
		        };
			for (String label : buttonLabels) {
				button = new JButton(label);
		           button.addMouseListener(new MouseAdapter() {
		        	@Override
		        	public void mousePressed(MouseEvent e) {
		        		if(label.equals("Enter")) {
		        			String itemToAdd = textField.getText();
		        			attendant.addItem(itemToAdd, station);
		        			textField.setText("");
		        		}else if(label.equals("Backspace")) {
		        			String text = textField.getText();
		    	            if (text.length() > 0) {
		    	                text = text.substring(0, text.length() - 1);
		    	                textField.setText(text);
		    	            }
		        		}else {
		        			String text = textField.getText() + label;
		    	            textField.setText(text);
		        		}
		        	}
		        });
		           keyBoard.add(button);
			}
			textField.setBounds(479, 268, 157, 26);
			stationPannel.add(textField);
			textField.setColumns(10);
			
			errorMSG = new JTextPane();
			errorMSG.setEditable(false);
			errorMSG.setBounds(34, 331, 288, 56);
			errorMSG.setText(station.getErrorMessage());
			stationPannel.add(errorMSG);
			
			JLabel lblNewLabel = new JLabel("Representative:");
			lblNewLabel.setForeground(new Color(234, 0, 4));
			lblNewLabel.setBounds(22, 200, 109, 16);
			stationPannel.add(lblNewLabel);
			
			fillPaper = new JButton("Fill Paper");
			fillPaper.setForeground(new Color(255, 7, 0));
			fillPaper.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					SelfCheckoutStation st = station.getStation();
					try {
						st.printer.addPaper(1000);
					} catch (OverloadException e1) {}
				}
			});
			fillPaper.setBounds(22, 220, 117, 29);
			stationPannel.add(fillPaper);
			
			fillInk = new JButton("Fill Ink");
			fillInk.setForeground(new Color(255, 7, 0));
			fillInk.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					SelfCheckoutStation st = station.getStation();
					try {
						st.printer.addInk(1000);
					} catch (OverloadException e1) {}
				}
			});
			fillInk.setBounds(22, 245, 117, 29);
			stationPannel.setSize(1200, 800);
			stationPannel.add(fillInk);

			
			printReceipt = new JButton("Print Receipt");
			 printReceipt.addMouseListener(new MouseAdapter() {
	     	   @Override
				public void mousePressed(MouseEvent e) {
					try {
						station.startPrinting(station.getBillList());
					} catch (EmptyException | OverloadException e1) {
					}
					
				}
	        });
			 printReceipt.setBounds(22, 107, 157, 41);
			stationPannel.add(printReceipt);
			
			shutdowonStatupButton = new JButton("Startup/Shutdown");
			shutdowonStatupButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(station.isBlocked()== true) {
						attendant.overrideBlock(num);
						attendant.shutDown(station.getStation());
						station.currentScene.launch();
						
					}else {
						station.block("Station has been shut down, press startup to use");
						attendant.startUp(station.getStation());
						station.currentScene.launch();
					}
					
				}
			});
			shutdowonStatupButton.setBounds(22, 47, 157, 41);
			stationPannel.add(shutdowonStatupButton);
			
			JButton approveButton = new JButton("Approve ");
			if (station.waitingForAttendantNoBag) {
				approveButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						station.attendantRespondNoBag = true;
						station.waitingForAttendantNoBag = false;
						attendant.overrideBlock(num);
					}
				});
			}
			else {
				approveButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						attendant.overrideBlock(num);
					}

			});
				
			}
			approveButton.setBounds(99, 399, 152, 41);
			stationPannel.add(approveButton);

			
			
			// columns for table contents
			String[] columns = {"Description", "Price"};
			
			JScrollPane view = new JScrollPane();
			// Object array to build table contents, based on system's bill list
			Object[][] dataCurrentItems = new Object[station.getBillList().size()][2];
						
			System.out.println(station.getBillList().toString());
			
			// iterate through each item and add description and price to table
			int iter = 0;
				for (Product product : station.getBillList()) {
					Object[] row = new Object[2];
					if (product instanceof BarcodedProduct) {
						row[0] = ((BarcodedProduct) product).getDescription();
					}
					else {
						row[0] = ((PLUCodedProduct) product).getDescription();
					}
					
					row[1] = product.getPrice().toString();
					dataCurrentItems[iter] = row;
					++iter;
				}
						
			// setup a table model with this data

			DefaultTableModel model = new DefaultTableModel(dataCurrentItems, columns);
			JTable itemsTable = new JTable(model);
			view.setViewportView(itemsTable);
			view.setBounds(400, 27, 309, 215);
			stationPannel.add(view);

		
			
			preventUse = new JButton("Prevent/Permit Use");
			preventUse.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(station.isBlocked()== true) {
						attendant.overrideBlock(num);
					}else {
						station.block("");
					}
					station.currentScene.launch();
				}
			});
			preventUse.setBounds(191, 47, 157, 41);
			stationPannel.add(preventUse);
			
			removeButton = new JButton("Remove Item");
			removeButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int activeRow = itemsTable.getSelectedRow();
					if (activeRow != -1) {
						station.removeBillList(activeRow);
					}
				}
			});
			removeButton.setBounds(191, 107, 157, 41);
			stationPannel.add(removeButton);
		}
		
	}
	public JTextPane getErrorMSG() {
		return errorMSG;
	}
}
