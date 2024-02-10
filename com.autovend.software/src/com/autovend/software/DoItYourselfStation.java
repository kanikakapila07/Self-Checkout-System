package com.autovend.software;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.MembershipCard;
import com.autovend.Coin;
import com.autovend.CreditCard;
import com.autovend.DebitCard;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;


public class DoItYourselfStation {
	private SelfCheckoutSystemLogic system;
	
	
	private JFrame window;
	
	private static int WINDOW_WIDTH = 800;
	private static int WINDOW_HEIGHT = 600;
	
	private static Color GREEN = new Color(135, 240, 132, 255);
	private static Color GREY = new Color(57, 61, 63, 255);
	private static Color BEIGE = new Color(213, 187, 177, 255);
	private static Color PEACH = new Color(201, 140, 167, 255);
	private static Color PINK = new Color(231, 109, 131, 255);
	
	private static Font HUGE = new Font("Sans-serif", Font.BOLD, 48);
	private static Font HEADER = new Font("Sans-serif", Font.BOLD, 36);

	private static Font MEDIUM = new Font("Sans-serif", Font.BOLD, 24);
	private static Font NORMAL = new Font("Sans-serif", Font.BOLD, 16);

	private boolean transactionPaidFor = false;
	public static GUIScene rootScene;
	public StationDisabledScene disabledScene = new StationDisabledScene(null);

	
	public DoItYourselfStation(SelfCheckoutSystemLogic system, int station_id) {
		this.system = system;
		// Create a new window
		// this.window = this.system.getStation().screen.getFrame();
		this.window = new JFrame();
		window.setTitle("TheLocalMarketplace Customer Station " + station_id);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1200, 800);
		window.setLocationRelativeTo(null);
		window.setResizable(true);
		
		// StationDisabledScene disabledScene = new StationDisabledScene(null);
		
		// Launch the welcome screen scene
		WelcomeScene welcomeScene = new WelcomeScene(null);
		welcomeScene.launch();
	}
	
	public void renderDisabled() {
			disabledScene.launch();
	}
	
	
	public String getTotalPrice() {
		return String.format("$%.2f", system.getAmountDue());
	}
	
	
	
	public void checkTransactionDone() {
		this.transactionPaidFor = system.getAmountDue() <= 0 && system.getBillList().size() >= 1; 
	}
	
	public class WelcomeScene extends GUIScene {
		
		public JButton startButton, languageButton, membershipButton;
		
		public ChooseAddItemScene addItem; 
		public TransactionSummaryScene transaction;
		public ChooseLanguageScene languageScene;
		public ChooseMembershipScene chooseMembershipScene;
		
		public WelcomeScene(GUIScene previousScene) {
			super(previousScene);

			
			startButton = new JButton();
			languageButton = new JButton();
			membershipButton = new JButton();

			DoItYourselfStation.rootScene = this;

		}

		// Displays the welcome screen
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			// Add the welcome message at the top
			// ----------------------------------------------------------------------
			// Create a new panel
			JPanel welcomePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			welcomePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(welcomePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel welcomeMessage = new JLabel("Welcome to TheLocalMarketplace!");
			// Set the font
			welcomeMessage.setFont(HEADER);
			// Set the color of the text
			welcomeMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			welcomePanel.add(welcomeMessage);
			// ----------------------------------------------------------------------
			
			// Add start button
			// ----------------------------------------------------------------------
			// Create a new panel to put the start button on
			JPanel startPanel = new JPanel(new GridBagLayout());
			startPanel.setBackground(Color.WHITE);
			window.add(startPanel, BorderLayout.CENTER);
			
			startButton.setText("START");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			startButton.setFocusable(false);
			// Set colors
			startButton.setBackground(GREEN);
			startButton.setForeground(Color.white);
			// Make the button change scenes
			startButton.setFont(MEDIUM);
			// Set the preferred size of the button
			startButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			transaction = new TransactionSummaryScene(this);
			startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					transaction.launch();
				}
			});
			startPanel.add(startButton);
			startPanel.add(Box.createHorizontalStrut(10));
			// ----------------------------------------------------------------------
			
			// Add language select and membership buttons
			// ----------------------------------------------------------------------
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			// Create a new button
			languageButton.setText("<html><center>Select<br>Language</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			languageButton.setFocusable(false);
			// Set colors
			languageButton.setBackground(Color.WHITE);
			languageButton.setForeground(GREEN);
			// Set font
			languageButton.setFont(NORMAL);
			// Make the button change scenes
			languageScene = new ChooseLanguageScene(this);
			languageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					languageScene.launch();
				}
			});
			// Add the button to the panel
			bottomPanel.add(languageButton, BorderLayout.WEST);
			
			// Create a new button
			membershipButton.setText("<html><center>Enter<br>Membership</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			membershipButton.setFocusable(false);
			// Set colors
			membershipButton.setBackground(Color.WHITE);
			membershipButton.setForeground(GREEN);
			// Set font
			membershipButton.setFont(NORMAL);
			// Make the button change scenes
			chooseMembershipScene = new ChooseMembershipScene(this);
			membershipButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					chooseMembershipScene.launch();
				}
			});
			// Add the button to the panel
			bottomPanel.add(membershipButton, BorderLayout.EAST);
			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	
	public class ChooseLanguageScene extends GUIScene {

		public JButton englishButton;
		public JButton alsoEnglishButton;
		public JButton backButton;
			
		public ChooseLanguageScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			// Add the welcome message at the top
			// ----------------------------------------------------------------------
			// Create a new panel
			JPanel welcomePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			welcomePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(welcomePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel welcomeMessage = new JLabel("Welcome to TheLocalMarketplace!");
			// Set the font
			welcomeMessage.setFont(HEADER);
			// Set the color of the text
			welcomeMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			welcomePanel.add(welcomeMessage);
			// ----------------------------------------------------------------------
			
			// Add start button
			// ----------------------------------------------------------------------
			// Create a new panel to put the start button on
			JPanel startPanel = new JPanel(new GridBagLayout());
			startPanel.setBackground(Color.WHITE);
			window.add(startPanel, BorderLayout.CENTER);
			
			
			englishButton = new JButton();
			englishButton.setText("English");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			englishButton.setFocusable(false);
			// Set colors
			englishButton.setBackground(GREEN);
			englishButton.setForeground(Color.white);
			// Make the button change scenes
			englishButton.setFont(MEDIUM);
			// Set the preferred size of the button
			englishButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			englishButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
				}
			});
			startPanel.add(englishButton);
			
			
			startPanel.add(Box.createHorizontalStrut(20));
			
			
			alsoEnglishButton = new JButton();
			alsoEnglishButton.setText("Also English");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			alsoEnglishButton.setFocusable(false);
			// Set colors
			alsoEnglishButton.setBackground(GREEN);
			alsoEnglishButton.setForeground(Color.white);
			// Make the button change scenes
			alsoEnglishButton.setFont(MEDIUM);
			// Set the preferred size of the button
			alsoEnglishButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.300), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			alsoEnglishButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
				}
			});
			startPanel.add(alsoEnglishButton);
			startPanel.add(Box.createHorizontalStrut(10));
			// ----------------------------------------------------------------------
			
			// Add language select and membership buttons
			// ----------------------------------------------------------------------
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton();
			// Create a new button
			backButton.setText("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
				}
			});
			// Add the button to the panel
			bottomPanel.add(backButton, BorderLayout.WEST);
			
		
			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
	}
	
	
	public class ChooseMembershipScene extends GUIScene {
		
		public JButton backButton;
		public JButton scanningButton;
		public JButton numberButton;
		public JButton swipeButton;
		
		public ScanMembershipScene scanMembershipScene;
		public TypeMembershipScene typeMembershipScene;
		public SwipeMembershipScene swipeMembershipScene;

		public ChooseMembershipScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Select Method of Adding Membership");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);
			
			scanningButton = new JButton("<html><center>Scan<br>Card</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			scanningButton.setFocusable(false);
			// Set colors
			scanningButton.setBackground(GREEN);
			scanningButton.setForeground(Color.white);
			// Make the button change scenes
			scanningButton.setFont(MEDIUM);
			// Set the preferred size of the button
			scanningButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			scanMembershipScene = new ScanMembershipScene(this);
			scanningButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					scanMembershipScene.launch();
				}
			});
			selectPanel.add(scanningButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			numberButton = new JButton("<html><center>Enter<br>Number</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			numberButton.setFocusable(false);
			// Set colors
			numberButton.setBackground(GREEN);
			numberButton.setForeground(Color.white);
			numberButton.setFont(MEDIUM);
			// Set the preferred size of the button
			numberButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			typeMembershipScene = new TypeMembershipScene(this);
			numberButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					typeMembershipScene.launch();
				}
			});
			selectPanel.add(numberButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			swipeButton = new JButton("<html><center>Swipe<br>Card</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			swipeButton.setFocusable(false);
			// Set colors
			swipeButton.setBackground(GREEN);
			swipeButton.setForeground(Color.white);
			swipeButton.setFont(MEDIUM);
			// Set the preferred size of the button
			swipeButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			swipeMembershipScene = new SwipeMembershipScene(this);
			swipeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					swipeMembershipScene.launch();
				}
			});
			selectPanel.add(swipeButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	public class ScanMembershipScene extends GUIScene {
		
		public JButton backButton;

		public ScanMembershipScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Scan Membership Card");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel messagePanel = new JPanel(new GridBagLayout());
			messagePanel.setBackground(Color.WHITE);
			window.add(messagePanel, BorderLayout.CENTER);
			
			// Set up the controller to wait for a card to be swiped
			new EnterMembershipByScanning(system, system.getStation());
			
			// Enable the main and handheld scanners
			system.getStation().mainScanner.enable();
			system.getStation().handheldScanner.enable();
			
			JPanel holdPanel = new JPanel(new BorderLayout());
			holdPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			holdPanel.setBackground(Color.WHITE);
			messagePanel.add(holdPanel);
			
			JLabel scanMembershipMessage = new JLabel("Please scan your membership card now.");
			scanMembershipMessage.setForeground(GREY);
			scanMembershipMessage.setFont(MEDIUM);
			holdPanel.add(scanMembershipMessage, BorderLayout.NORTH);
			
			JPanel hPanel = new JPanel(new BorderLayout());
			hPanel.setBackground(Color.WHITE);
			holdPanel.add(hPanel, BorderLayout.SOUTH);
			
			JButton scanButton = new JButton("Scan membership card 0123456789 with main scanner");
			scanButton.setFocusable(false);
			// Set colors
			scanButton.setBackground(Color.WHITE);
			scanButton.setForeground(GREEN);
			// Set font
			scanButton.setFont(NORMAL);
			// Make the button change scenes
			scanButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Create a dummy membership card
					MembershipCard dummyMembershipCard = new MembershipCard("membership", "0123456789", "User", false);
					// Scan it with the main scanner
					system.getStation().mainScanner.scan(dummyMembershipCard);
				}
			});
			hPanel.add(scanButton, BorderLayout.NORTH);
			
			JButton scanHandheldButton = new JButton("Scan membership card 0123456789 with handheld scanner");
			scanHandheldButton.setFocusable(false);
			// Set colors
			scanHandheldButton.setBackground(Color.WHITE);
			scanHandheldButton.setForeground(GREEN);
			// Set font
			scanHandheldButton.setFont(NORMAL);
			// Make the button change scenes
			scanHandheldButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Create a dummy membership card
					MembershipCard dummyMembershipCard = new MembershipCard("membership", "0123456789", "User", false);
					// Scan it with the handheld scanner
					system.getStation().handheldScanner.scan(dummyMembershipCard);
				}
			});
			hPanel.add(scanHandheldButton, BorderLayout.SOUTH);
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	public class TypeMembershipScene extends GUIScene {
		public boolean showWrongNumberMessage = false;
		public String membershipNumber = "";
		
		public JButton backButton;
		public JButton button0 = new JButton();
		public JButton button1 = new JButton();
		public JButton button2 = new JButton();
		public JButton button3 = new JButton();
		public JButton button4 = new JButton();
		public JButton button5 = new JButton();
		public JButton button6 = new JButton();
		public JButton button7 = new JButton();
		public JButton button8 = new JButton();
		public JButton button9 = new JButton();
		public JButton buttonBack = new JButton();
		public JButton buttonEnter = new JButton();
		
		public JTextField numField;
		
		public WelcomeScene welcomeScene;
		public TypeMembershipScene typeMembershipScene;
		
		public TypeMembershipScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Enter Membership Number");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel typePanel = new JPanel(new GridBagLayout());
			typePanel.setBackground(Color.WHITE);
			window.add(typePanel, BorderLayout.CENTER);
			
			JPanel holdPanel = new JPanel(new BorderLayout((int) (WINDOW_WIDTH * 0.01), (int) (WINDOW_WIDTH * 0.01)));
			holdPanel.setBackground(Color.WHITE);
			typePanel.add(holdPanel);
			typePanel.add(Box.createHorizontalStrut(10));
			
			// Add the text field
			// ----------------------------------------------------------------------
			numField = new JTextField();
			numField.setEditable(false);
			numField.setFont(NORMAL);
			holdPanel.add(numField, BorderLayout.NORTH);
			
			// Add the number pad
			// ----------------------------------------------------------------------
			JPanel numPanel = new JPanel(new GridLayout(4, 3));
			numPanel.setBackground(Color.WHITE);
			holdPanel.add(numPanel, BorderLayout.CENTER);
			
			JButton[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonBack, buttonEnter};
			for (int i = 0; i <= 11; i++) {
				final int buttonVal = i;
				JButton numButton = buttons[i];
				
				if (buttonVal == 10) {
					numButton.setText("<");
				} else if (buttonVal == 11) {
					numButton.setText("E");
				} else {
					numButton.setText("" + buttonVal);
				}
				numButton.setFocusable(false);
				numButton.setBackground(GREY);
				numButton.setForeground(Color.white);
				numButton.setFont(MEDIUM);
				numButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1), (int) (WINDOW_WIDTH * 0.1)));
				TypeMembershipScene thisScene = this;
				numButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// If the user pressed the backspace button
						if (buttonVal == 10) {
							if (thisScene.membershipNumber == null || thisScene.membershipNumber == "") {
								thisScene.membershipNumber = "";
							} else {
								// Remove the last character from the membership number
								thisScene.membershipNumber = thisScene.membershipNumber.substring(0, thisScene.membershipNumber.length() - 1);
							}
						// If the user pressed the enter button
						} else if (buttonVal == 11) {
							if (thisScene.membershipNumber == null) {
								thisScene.membershipNumber = "";
							}
							
							if (system.takeMembership(thisScene.membershipNumber)) {
								// If there is a membership card matching the given number
								System.out.println("Typed Membership Number: " + thisScene.membershipNumber);
								
								// Return to the welcome screen
								// Disable the "Enter Membership" button, since we've already done that
								welcomeScene = new WelcomeScene(null);
								welcomeScene.membershipButton.setEnabled(false);
								welcomeScene.launch();
							} else {
								// There is no membership card matching the given number
								// Re-launch the current scene,
								// but display a message indicating that there is no membership card matching the number the user entered
								typeMembershipScene = new TypeMembershipScene(previousScene);
								typeMembershipScene.showWrongNumberMessage = true;
								typeMembershipScene.launch();
							}
						// If the user pressed a regular number button
						} else {
							if (thisScene.membershipNumber == null) {
								thisScene.membershipNumber = "" + buttonVal;
							} else {
								// Remove the last character from the membership number
								thisScene.membershipNumber += buttonVal;
							}
						}
						
						numField.setText(thisScene.membershipNumber);
					}
				});
				numPanel.add(numButton);
			}
			// ----------------------------------------------------------------------
			
			// Add a text box to report to the user that no membership card was found with the entered number,
			// which only shows up if it was set while calling
			// ----------------------------------------------------------------------
			JLabel wrongNumberMessage = new JLabel("No matching membership card found. Please try again!");
			if (showWrongNumberMessage) {
				wrongNumberMessage.setForeground(PINK);
			} else {
				wrongNumberMessage.setForeground(holdPanel.getBackground());
			}
			wrongNumberMessage.setFont(NORMAL);
			holdPanel.add(wrongNumberMessage, BorderLayout.SOUTH);
			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	public class SwipeMembershipScene extends GUIScene {
		
		public JButton backButton;

		public SwipeMembershipScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Swipe Membership Card");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel messagePanel = new JPanel(new GridBagLayout());
			messagePanel.setBackground(Color.WHITE);
			window.add(messagePanel, BorderLayout.CENTER);
			
			// Set up the controller to wait for a card to be swiped
			new SwipeMembershipCardController(system, system.getStation());
			
			JPanel holdPanel = new JPanel(new BorderLayout());
			holdPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			holdPanel.setBackground(Color.WHITE);
			messagePanel.add(holdPanel);
			
			JLabel scanMembershipMessage = new JLabel("Please swipe your membership card now.");
			scanMembershipMessage.setForeground(GREY);
			scanMembershipMessage.setFont(MEDIUM);
			holdPanel.add(scanMembershipMessage, BorderLayout.NORTH);
			
			JButton swipeButton = new JButton("Swipe membership card 0123456789");
			swipeButton.setFocusable(false);
			// Set colors
			swipeButton.setBackground(Color.WHITE);
			swipeButton.setForeground(GREEN);
			// Set font
			swipeButton.setFont(NORMAL);
			// Make the button change scenes
			swipeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Create a dummy membership card
					MembershipCard dummyMembershipCard = new MembershipCard("membership", "0123456789", "User", false);
					try {
						system.getStation().cardReader.swipe(dummyMembershipCard, null);
					} catch (IOException e1) {}
				}
			});
			holdPanel.add(swipeButton, BorderLayout.SOUTH);
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	/**
	 * Scene for transaction summary
	 * 
	 * 
	 * @author desmo
	 *
	 */
	public class TransactionSummaryScene extends GUIScene {
		// will eventually include an option to go to pay scene
		//public PayScene payScene = new PayScene(this);
		
		public JButton addItemsButton;
		public JButton payButton;
		public JButton printReceiptButton;
		public JButton purchaseBagsButton;
		public JButton backButton;
		public JButton addOwnBagsButton;
		
		// include an option to go to add item selection
		public ChooseAddItemScene addItem;
		
		// TODO: Change this to choose payment method scene

		public ChoosePaymentMethodScene choosePayment = new ChoosePaymentMethodScene(this);
		public PrintReceiptScene printReceipt = new PrintReceiptScene(this);
		public PurchaseBagsScene purchaseBags = new PurchaseBagsScene(this);
		public AddOwnBagsScene addBags = new AddOwnBagsScene(this);
		
		
		public TransactionSummaryScene(GUIScene previous) {
			super(previous);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.isBlocked()) {
				renderDisabled();
				return;
			}
			// columns for table contents
			String[] columns = {"Description", "Price"};
			
			// current items
			
			// Object array to build table contents, based on system's bill list
			Object[][] dataCurrentItems = new Object[system.getBillList().size()][2];
			
			// iterate through each item and add description and price to table
			int iter = 0;
			for (Product product : system.getBillList()) {
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
			
			window.getContentPane().removeAll();
			window.repaint();
			
			/*
			 * This is all pretty much copy-pasted from Dylan
			 * 
			 * 
			 */
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Transaction Summary");
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
	

			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			
			// create a table, disable it so cant edit
			JTable table = new JTable(model);
			table.setEnabled(false);

			// slap table in a scroll pane and add to main panel
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);

			// make a button for moving to add item selection
			addItemsButton = new JButton("Add Items");
			addItemsButton.setBackground(GREEN);
			addItemsButton.setForeground(Color.WHITE);
			addItemsButton.setFont(MEDIUM);
			// Set the preferred size of the button
			addItemsButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addItem = new ChooseAddItemScene(this);
			addItemsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addItem.launch();
				}
			});
			
			// make a button for moving to add item selection
			payButton = new JButton("Pay");
			payButton.setBackground(GREEN);
			payButton.setForeground(Color.WHITE);
			payButton.setFont(MEDIUM);
			// Set the preferred size of the button
			payButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			payButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					choosePayment.launch();
				}
			});
			
			
			purchaseBagsButton = new JButton("Purchase Bags");
			purchaseBagsButton.setBackground(GREEN);
			purchaseBagsButton.setForeground(Color.WHITE);
			purchaseBagsButton.setFont(MEDIUM);
			// Set the preferred size of the button
			purchaseBagsButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			purchaseBagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					purchaseBags.launch();
				}
			});
			selectPanel.add(purchaseBagsButton);
			
			addOwnBagsButton = new JButton("Add Own Bags");
			addOwnBagsButton.setBackground(GREEN);
			addOwnBagsButton.setForeground(Color.WHITE);
			addOwnBagsButton.setFont(MEDIUM);
			// Set the preferred size of the button
			addOwnBagsButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addOwnBagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addBags.launch();
				}
			});
			selectPanel.add(addOwnBagsButton);
			
			
			if (transactionPaidFor) {
				
			
				printReceiptButton = new JButton("Print Receipt");
				printReceiptButton.setBackground(GREEN);
				printReceiptButton.setForeground(Color.WHITE);
				printReceiptButton.setFont(new Font("Sans-serif", Font.BOLD, 24));
				// Set the preferred size of the button
				printReceiptButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
				// Make the button change scenes
				printReceipt = new PrintReceiptScene(this);
				printReceiptButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						printReceipt.launch();
					}
				});
				selectPanel.add(printReceiptButton);
				
			}
			
			selectPanel.add(addItemsButton, BorderLayout.SOUTH);
			selectPanel.add(payButton, BorderLayout.SOUTH);
			window.add(selectPanel, BorderLayout.CENTER);
			
			// add a lable with total due, formatting is scuffed AF as evident on the line below, will fix when i figure out layouts better
			JLabel totalDue = new JLabel(String.format("                                                 Total amount due: %s", getTotalPrice()));
			totalDue.setFont(new Font("Sans-serif", Font.BOLD, 24));
			selectPanel.add(totalDue);

			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	
	public class AddOwnBagsScene extends GUIScene {
		
		TransactionSummaryScene transaction;
		JButton placeBagButton;
		JButton doneButton;
		JButton backButton;

		public AddOwnBagsScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.isBlocked()) {
				renderDisabled();
				return;
			}
			
			
			transaction = new TransactionSummaryScene(new WelcomeScene(null));
			
			// setup a table model with this data
			transaction = new TransactionSummaryScene(null);
			
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			
			/*
			 * This is all pretty much copy-pasted from Dylan
			 * 
			 * 
			 */
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Add Own Bags");
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
	
			JTextField bagCount = new JTextField("0");
			bagCount.setEditable(false);
			bagCount.setFont(NORMAL);
			

			



			// make a button for moving to add item selection
			placeBagButton = new JButton("Place Bag");
			placeBagButton.setBackground(GREEN);
			placeBagButton.setForeground(Color.WHITE);
			placeBagButton.setFont(MEDIUM);
			// Set the preferred size of the button
			placeBagButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			placeBagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					++system.numberBagsOwned;
					//int count = Integer.parseInt(bagCount.getText());
					bagCount.setText(String.format("%d", system.numberBagsOwned));
				}
			});
			
			selectPanel.add(placeBagButton);			
			
			// make a button for moving to add item selection
			doneButton = new JButton("Done");
			doneButton.setBackground(GREEN);
			doneButton.setForeground(Color.WHITE);
			doneButton.setFont(MEDIUM);
			// Set the preferred size of the button
			doneButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			doneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						system.addOwnBags();
						previousScene.launch();
						system.getStation().baggingArea.deregisterAll();
					} catch (OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			selectPanel.add(doneButton, BorderLayout.SOUTH);
			selectPanel.add(bagCount);
			selectPanel.add(doneButton, BorderLayout.SOUTH);
			selectPanel.add(bagCount);
			
			
			
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			

			window.add(selectPanel, BorderLayout.CENTER);
			

			

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public class StationDisabledScene extends GUIScene {

		public StationDisabledScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		
		@Override 
		public void launch() {
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Station Disabled");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			JLabel message = new JLabel("<html><center>Station disabled...<br> notifying attendant</center></html>");

			message.setFont(new Font("Sans-serif", Font.BOLD, 36));
			JPanel selectPanel = new JPanel(new GridBagLayout());

			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);
			
			
			selectPanel.add(message);

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
	}
	
	
	

	public class PayWithCreditDebitScene extends GUIScene {
		public PayWithCreditDebitScene(GUIScene previousScene) {
			super(previousScene);
			
			SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
			String string_date = "10-10-2025 00:00:00";
			Date date = null;
			try {
				date = date_s.parse(string_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar expiry = Calendar.getInstance(Locale.CANADA);
			expiry.setTime(date);
			try {
			SelfCheckoutSystemLogic.interac.addCardData("100", "G. Raymond", expiry, "123", BigDecimal.valueOf(100000f));
			SelfCheckoutSystemLogic.interac.addCardData("101", "G. Raymond", expiry, "123", BigDecimal.valueOf(100000f));
			}
			catch (Exception e) {
				// just skip they are already added after first transaction
			}

		}
		public JButton button0 = new JButton();
		public JButton button1 = new JButton();
		public JButton button2 = new JButton();
		public JButton button3 = new JButton();
		public JButton button4 = new JButton();
		public JButton button5 = new JButton();
		public JButton button6 = new JButton();
		public JButton button7 = new JButton();
		public JButton button8 = new JButton();
		public JButton button9 = new JButton();
		public JButton buttonBack = new JButton();
		public JButton buttonEnter = new JButton();
		public JButton removeCardButton = new JButton();
		public JTextField numField;
		public JButton debitButton;
		public JButton creditButton;
		public JButton giftCardButton;
		public JButton backButton;
		public JButton tapButton;
		public JButton swipeButton;
		public JButton insertButton;
		public JButton removeButton;
		public String pinCode = "";
		public boolean showOptions = false;
		public DebitCard debit = new DebitCard("Visa", "100", "G. Raymond", "123", "1234", true, true);
		public CreditCard credit = new CreditCard("Visa", "101", "G. Raymond", "123", "1234", true, true);
		public String debitOrCredit;
		public boolean enterPin = false;
		public boolean removeCard = false;
		
		public JButton printReceiptButton;


		@Override 
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			
			String[] columns = {"Description", "Price"};
			
			// current items
			
			// Object array to build table contents, based on system's bill list
			Object[][] dataCurrentItems = new Object[system.getBillList().size()][2];
			
			// iterate through each item and add description and price to table
			int iter = 0;
			for (Product product : system.getBillList()) {
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

			// create a table, disable it so cant edit
			JTable table = new JTable(model);
			table.setEnabled(false);

			// slap table in a scroll pane and add to main panel
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);
			
			
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Pay With Credit / Debit");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			if (showOptions && !removeCard && !enterPin) {
			tapButton = new JButton("<html><center>Tap</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			tapButton.setFocusable(false);
			// Set colors
			tapButton.setBackground(GREEN);
			tapButton.setForeground(Color.WHITE);
			// Set font
			tapButton.setFont(NORMAL);
			// Make the button change scenes
			tapButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					system.setCardPaymentAmount(BigDecimal.valueOf(system.getAmountDue()));
					if (debitOrCredit.equals("credit")) {
						system.payWithCredit();
						try {
							system.getStation().cardReader.tap(credit);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {
						system.payWithDebit();
						try {
							system.getStation().cardReader.tap(debit);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					system.getStation().cardReader.deregisterAll();
					showOptions = false;
					checkTransactionDone();
					launch();
				}
			});
			selectPanel.add(tapButton);
			JPanel numPanel = new JPanel(new GridLayout(4, 3));
			numPanel.setBackground(Color.WHITE);
			selectPanel.add(numPanel);

			}
			
			JPanel numPanel = new JPanel(new GridLayout(4, 3));
			numPanel.setBackground(Color.WHITE);
			selectPanel.add(numPanel);
			
			if (enterPin) {

			
			
			JButton[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonBack, buttonEnter};
			for (int i = 0; i <= 11; i++) {
				final int buttonVal = i;
				JButton numButton = buttons[i];
				
				if (buttonVal == 10) {
					numButton.setText("<");
				} else if (buttonVal == 11) {
					numButton.setText("E");
					numButton.setEnabled(false);
				} else {
					numButton.setText("" + buttonVal);
				}
				
				numButton.setFocusable(false);
				numButton.setBackground(GREY);
				numButton.setForeground(Color.white);
				numButton.setFont(MEDIUM);
				numButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1), (int) (WINDOW_WIDTH * 0.1)));
				if (numButton.getActionListeners().length == 0) {
				numButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// If the user pressed the backspace button
						if (buttonVal == 10) {
							if (pinCode == null || pinCode == "") {
								pinCode = "";
							} else {
								// Remove the last character from the PLU code
								pinCode = pinCode.substring(0, pinCode.length() - 1);
								
								// If this backspace has reduced the length of the code to less than 4 digits, disable the enter button
								if (pinCode.length() < 4) {
									buttonEnter.setEnabled(false);
								}
							}
						// If the user pressed the enter button
						} else if (buttonVal == 11) {
							if (pinCode == null) {
								pinCode = "";
							}
							
							// The user can only submit a PLU code with 4 or 5 digits
							if (pinCode.length() == 4 || pinCode.length() == 5) {
								if (pinCode == null) {
									pinCode = "";
								}
					
								if (debitOrCredit.equals("credit")) {
									try {
										system.getStation().cardReader.insert(credit, pinCode);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								else {
									try {
										system.getStation().cardReader.insert(debit, pinCode);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
			
							}
							System.out.println(pinCode);
							system.getStation().cardReader.deregisterAll();
							pinCode = "";
							removeCard = true;
							launch();
						// If the user pressed a regular number button
						} else {
							System.out.println(buttonVal);
							if (pinCode == null) {
								pinCode = "" + buttonVal;
							} else {
								if (pinCode.length() < 5) {
									// Add the new character to the PLUCode, but only if we haven't yet 
									pinCode += buttonVal;
									
									// If the user has entered at least 4 digits, enable the enter button
									if (pinCode.length() >= 4) {
										buttonEnter.setEnabled(true);
									}
								}
							}
						}
						
					}
				});
				}

				numPanel.add(numButton);
			}
			}
			

			if (removeCard) {
				removeCardButton = new JButton("<html><center>removeCard</center></html>");

				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				removeCardButton.setFocusable(false);
				// Set colors
				removeCardButton.setBackground(GREEN);
				removeCardButton.setForeground(Color.WHITE);
				// Set font
				removeCardButton.setFont(NORMAL);
				// Make the button change scenes

				removeCardButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							system.getStation().cardReader.remove();
						}
						catch (Throwable e2) {
				
						}
						
						enterPin = false;
						removeCard = false;
						system.getStation().cardReader.deregisterAll();
						showOptions = false;
						checkTransactionDone();
						launch();
					}
				});
				selectPanel.add(removeCardButton);
				
			}
			

			if (showOptions && !removeCard && !enterPin) {
			swipeButton = new JButton("<html><center>Swipe</center></html>");

			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			swipeButton.setFocusable(false);
			// Set colors
			swipeButton.setBackground(GREEN);
			swipeButton.setForeground(Color.WHITE);
			// Set font
			swipeButton.setFont(NORMAL);
			// Make the button change scenes

			swipeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					system.setCardPaymentAmount(BigDecimal.valueOf(system.getAmountDue()));
					if (debitOrCredit.equals("credit")) {
						system.payWithCredit();
						try {
							system.getStation().cardReader.swipe(credit, null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {
						system.payWithDebit();
						try {
							system.getStation().cardReader.swipe(debit, null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					enterPin = false;
					showOptions = false;
					removeCard = true;
					system.getStation().cardReader.deregisterAll();
					showOptions = false;
					checkTransactionDone();
					launch();
				}
			});
			selectPanel.add(swipeButton);
			}
			

			if (showOptions && !removeCard && !enterPin) {
			insertButton = new JButton("<html><center>Insert</center></html>");

			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			insertButton.setFocusable(false);
			// Set colors
			insertButton.setBackground(GREEN);
			insertButton.setForeground(Color.WHITE);
			// Set font
			insertButton.setFont(NORMAL);
			// Make the button change scenes

			
			// TODO: IMPLEMENT SO THAT PIN CAN BE ENTERED
			insertButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					system.setCardPaymentAmount(BigDecimal.valueOf(system.getAmountDue()));
					if (debitOrCredit.equals("credit")) {
						system.payWithCredit();

					}
					else {
						system.payWithDebit();
					}
					//removeCard = true;
					enterPin = true;
	
					showOptions = false;
					checkTransactionDone();
					launch();

				}
			});
			selectPanel.add(insertButton);
			}
			
			


			

			if (!showOptions && !removeCard && !enterPin) {
			creditButton = new JButton("<html><center>Credit Card</center></html>");

			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			creditButton.setFocusable(false);
			// Set colors
			creditButton.setBackground(GREEN);
			creditButton.setForeground(Color.white);
			// Make the button change scenes
			creditButton.setFont(MEDIUM);
			// Set the preferred size of the button
			creditButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes

			creditButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					system.setCurrentCreditCard(credit);
					showOptions = true;
					debitOrCredit = "credit";
					launch();

				}
			});
			selectPanel.add(creditButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			}
			
			
			if (!showOptions && !removeCard && !enterPin) {
				giftCardButton = new JButton("<html><center>Gift Card</center></html>");

				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				giftCardButton.setFocusable(false);
				// Set colors
				giftCardButton.setBackground(GREEN);
				giftCardButton.setForeground(Color.white);
				// Make the button change scenes
				giftCardButton.setFont(MEDIUM);
				// Set the preferred size of the button
				giftCardButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
				// Make the button change scenes

				giftCardButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						system.setCurrentCreditCard(credit);
						showOptions = true;
						debitOrCredit = "credit";
						launch();

					}
				});
				selectPanel.add(giftCardButton);
				selectPanel.add(Box.createHorizontalStrut(10));
			}
			

			if (!showOptions && !removeCard && !enterPin) {
			debitButton = new JButton("<html><center>Debit Card</center></html>");

			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			debitButton.setFocusable(false);
			// Set colors
			debitButton.setBackground(GREEN);
			debitButton.setForeground(Color.white);
			// Make the button change scenes
			debitButton.setFont(MEDIUM);
			// Set the preferred size of the button
			debitButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			debitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					system.setCurrentDebitCard(debit);
					showOptions = true;
					debitOrCredit = "debit";
					launch();
				}
			});
			selectPanel.add(debitButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			}

			

			JLabel totalDue = new JLabel(String.format("Remaining Balance: %s", getTotalPrice()));
			totalDue.setFont(new Font("Sans-serif", Font.BOLD, 24));
			selectPanel.add(totalDue);
			
			if (system.getAmountDue() <= 0.01) {


				printReceiptButton = new JButton("Print Receipt");
				printReceiptButton.setBackground(GREEN);
				printReceiptButton.setForeground(Color.WHITE);
				printReceiptButton.setFont(new Font("Sans-serif", Font.BOLD, 24));
				// Set the preferred size of the button
				printReceiptButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
				// Make the button change scenes
				PrintReceiptScene printReceipt = new PrintReceiptScene(this);
				printReceiptButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						printReceipt.launch();
					}
				});
				selectPanel.add(printReceiptButton);
			}
			
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	
	
	}
	
	
	
	public class ChoosePaymentMethodScene extends GUIScene {
		public PayWithCreditDebitScene payCard = new PayWithCreditDebitScene(this);
		public PayWithCashScene payCash = new PayWithCashScene(this);
		public JButton backButton;
		public JButton payWithCardButton;
		public JButton payWithCashButton;
		
		
		public ChoosePaymentMethodScene(GUIScene previousScene) {
			super(previousScene);

		}
		
		
		@Override 
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Choose Method of Payment");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			//selectPanel.CENTER_ALIGNMEN
			// createEmptyBorder(top, left, bottom, right)
			//selectPanel.setBorder(BorderFactory.createEmptyBorder((int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255),
				//												 (int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255)));
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);

			
			
			payWithCashButton = new JButton("<html><center>Pay<br>With<br>Cash</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			payWithCashButton.setFocusable(false);
			// Set colors
			payWithCashButton.setBackground(GREEN);
			payWithCashButton.setForeground(Color.white);
			// Make the button change scenes
			payWithCashButton.setFont(MEDIUM);
			// Set the preferred size of the button
			payWithCashButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			payWithCashButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					payCash.launch();
				}
			});
			selectPanel.add(payWithCashButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			
			payWithCardButton = new JButton("<html><center>Pay<br>With<br>Card</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			payWithCardButton.setFocusable(false);
			// Set colors
			payWithCardButton.setBackground(GREEN);
			payWithCardButton.setForeground(Color.white);
			// Make the button change scenes
			payWithCardButton.setFont(MEDIUM);
			// Set the preferred size of the button
			payWithCardButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			payWithCardButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					payCard.launch();
				}
			});
			selectPanel.add(payWithCardButton);
			selectPanel.add(Box.createHorizontalStrut(10));

			
			
			
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
		
		
		
	
	public class DoNotBagItemScene extends GUIScene {

		JButton yesButton;
		JButton noButton;
		TransactionSummaryScene transaction;
		
		public DoNotBagItemScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		@Override 
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			transaction = new TransactionSummaryScene(new WelcomeScene(null));
			
			// Create a text box
			JLabel titleMessage = new JLabel("Please Select An Option");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);

			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			

			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			//selectPanel.CENTER_ALIGNMEN
			// createEmptyBorder(top, left, bottom, right)
			//selectPanel.setBorder(BorderFactory.createEmptyBorder((int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255),
				//												 (int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255)));
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);

			JLabel scanMessage = new JLabel("Would you like to bag this item?      ");
			scanMessage.setFont(HEADER);
			// Set the color of the text

			// Add the text to the panel
			selectPanel.add(scanMessage);
			
			yesButton = new JButton("<html><center>Yes</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			yesButton.setFocusable(false);
			// Set colors
			yesButton.setBackground(GREEN);
			yesButton.setForeground(Color.white);
			// Make the button change scenes
			yesButton.setFont(MEDIUM);
			// Set the preferred size of the button
			yesButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			yesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					transaction.launch();
				}
			});
			selectPanel.add(yesButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			noButton = new JButton("<html><center>No</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			noButton.setFocusable(false);
			// Set colors
			noButton.setBackground(GREEN);
			noButton.setForeground(Color.white);
			// Make the button change scenes
			noButton.setFont(MEDIUM);
			// Set the preferred size of the button
			noButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			noButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						system.DoNotPlaceInBaggingArea();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					transaction.launch();
				}
				
			});
			selectPanel.add(noButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			

			

			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Scene for choosing method of adding an item
	 * 
	 * this function just makes 4 buttons and places them in a row on the screen
	 * 
	 * @author desmo
	 *
	 */
	public class ChooseAddItemScene extends GUIScene {
		
		public JButton backButton;
		public JButton scanningButton;
		public JButton pluButton;
		public JButton searchButton;
		public JButton browseButton;
		
		public AddItemByBrowsingScene browseForItem;
		public AddItemByPLUScene addItemByPLUScene;
		public AddItemByTypingScene addItemByTypingScene;
		public AddItemByScanningScene addItemByScanningScene;
		
		public ChooseAddItemScene(GUIScene previous) {
			super(previous);
		}
		
		@Override 
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Choose Method of Adding Item");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			//selectPanel.CENTER_ALIGNMEN
			// createEmptyBorder(top, left, bottom, right)
			//selectPanel.setBorder(BorderFactory.createEmptyBorder((int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255),
				//												 (int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255)));
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);


			scanningButton = new JButton("<html><center>Scan<br>Item</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			scanningButton.setFocusable(false);
			// Set colors
			scanningButton.setBackground(GREEN);
			scanningButton.setForeground(Color.white);
			// Make the button change scenes
			scanningButton.setFont(MEDIUM);
			// Set the preferred size of the button
			scanningButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addItemByScanningScene = new AddItemByScanningScene(this);
			scanningButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					addItemByScanningScene.launch();
					
				}
			});
			selectPanel.add(scanningButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			pluButton = new JButton("<html><center>Enter<br>PLU</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			pluButton.setFocusable(false);
			// Set colors
			pluButton.setBackground(GREEN);
			pluButton.setForeground(Color.white);
			pluButton.setFont(MEDIUM);
			// Set the preferred size of the button
			pluButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addItemByPLUScene = new AddItemByPLUScene(this);
			pluButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					addItemByPLUScene.launch();
				}
			});
			selectPanel.add(pluButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			searchButton = new JButton("<html><center>Search<br>By<br>Name</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			searchButton.setFocusable(false);
			// Set colors
			searchButton.setBackground(GREEN);
			searchButton.setForeground(Color.white);
			searchButton.setFont(MEDIUM);
			// Set the preferred size of the button
			searchButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addItemByTypingScene = new AddItemByTypingScene(this);
			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					addItemByTypingScene.launch();
				}
			});
			selectPanel.add(searchButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			
			browseForItem = new AddItemByBrowsingScene(this);
			browseButton = new JButton("<html><center>Browse<br>Item<br>List</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			browseButton.setFocusable(false);
			// Set colors
			browseButton.setBackground(GREEN);
			browseButton.setForeground(Color.white);
			browseButton.setFont(MEDIUM);
			// Set the preferred size of the button
			browseButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			browseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					browseForItem.launch();
				}
			});
			selectPanel.add(browseButton);
			

			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
	}
	
	public class AddItemByScanningScene extends GUIScene {
		
		public BarcodedUnit milk = new BarcodedUnit(Main.MILK_CODE, Main.MILK_WEIGHT);
		public BarcodedUnit bread = new BarcodedUnit(Main.BREAD_CODE, Main.BREAD_WEIGHT - 500);
		public BarcodedProduct milkProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(Main.MILK_CODE);
		public BarcodedProduct breadProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(Main.BREAD_CODE);

		public JButton milkButton = new JButton();
		public JButton breadButton = new JButton();
		
		public DoNotBagItemScene noBag;
		
		public JButton backButton = new JButton();
		public AddItemByScanningScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		@Override 
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			noBag = new DoNotBagItemScene(null);
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Please Scan Your Item");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);

			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel selectPanel = new JPanel(new GridBagLayout());
			//selectPanel.CENTER_ALIGNMEN
			// createEmptyBorder(top, left, bottom, right)
			//selectPanel.setBorder(BorderFactory.createEmptyBorder((int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255),
				//												 (int) (WINDOW_HEIGHT * 0.255),
				//												 (int) (WINDOW_WIDTH * 0.255)));
			selectPanel.setBackground(Color.WHITE);
			window.add(selectPanel, BorderLayout.CENTER);

			JLabel scanMessage = new JLabel("Please scan your desired item!      ");
			scanMessage.setFont(HEADER);
			// Set the color of the text

			// Add the text to the panel
			selectPanel.add(scanMessage);
			
			milkButton = new JButton("<html><center>Scan Milk<br>(No Discrepancy)</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			milkButton.setFocusable(false);
			// Set colors
			milkButton.setBackground(GREEN);
			milkButton.setForeground(Color.white);
			// Make the button change scenes
			milkButton.setFont(MEDIUM);
			// Set the preferred size of the button
			milkButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			milkButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					try {
						system.setCurrentSelectableUnit(milk);
						system.addItemByScanning();
						noBag.launch();
						system.getStation().baggingArea.deregisterAll();
						
					} catch (Exception e1) {
					
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.getStation().mainScanner.deregisterAll();
					//new TransactionSummaryScene(null).launch();
				}
			});
			selectPanel.add(milkButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			
			breadButton = new JButton("<html><center>Scan bread<br>(Discrepancy)</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			breadButton.setFocusable(false);
			// Set colors
			breadButton.setBackground(GREEN);
			breadButton.setForeground(Color.white);
			// Make the button change scenes
			breadButton.setFont(MEDIUM);
			// Set the preferred size of the button
			breadButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			breadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					try {
						system.setCurrentSelectableUnit(bread);
						system.addItemByScanning();
						system.getStation().baggingArea.deregisterAll();
						noBag.launch();
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.getStation().mainScanner.deregisterAll();
					//new TransactionSummaryScene(null).launch();
				}
			});
			selectPanel.add(breadButton);
			selectPanel.add(Box.createHorizontalStrut(10));
			

			

			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	
		
		
	}
	
	
	public class AddItemByPLUScene extends GUIScene {
		public boolean showWrongPLUMessage = false;
		public String PLUCode = "";
		
		public DoNotBagItemScene noBag;
		
		public JButton backButton;
		public JButton button0 = new JButton();
		public JButton button1 = new JButton();
		public JButton button2 = new JButton();
		public JButton button3 = new JButton();
		public JButton button4 = new JButton();
		public JButton button5 = new JButton();
		public JButton button6 = new JButton();
		public JButton button7 = new JButton();
		public JButton button8 = new JButton();
		public JButton button9 = new JButton();
		public JButton buttonBack = new JButton();
		public JButton buttonEnter = new JButton();
		
		public JTextField numField;
		
		public TransactionSummaryScene transaction;
		
		public AddItemByPLUScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			noBag = new DoNotBagItemScene(null);
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Enter PLU code");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);

			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			JPanel typePanel = new JPanel(new GridBagLayout());
			typePanel.setBackground(Color.WHITE);
			window.add(typePanel, BorderLayout.CENTER);
			
			JPanel holdPanel = new JPanel(new BorderLayout((int) (WINDOW_WIDTH * 0.01), (int) (WINDOW_WIDTH * 0.01)));
			holdPanel.setBackground(Color.WHITE);
			typePanel.add(holdPanel);
			typePanel.add(Box.createHorizontalStrut(10));
			
			// Add the text field
			// ----------------------------------------------------------------------
			numField = new JTextField();
			numField.setEditable(false);
			numField.setFont(NORMAL);
			holdPanel.add(numField, BorderLayout.NORTH);
			
			// Add the number pad
			// ----------------------------------------------------------------------
			JPanel numPanel = new JPanel(new GridLayout(4, 3));
			numPanel.setBackground(Color.WHITE);
			holdPanel.add(numPanel, BorderLayout.CENTER);
			
			JButton[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonBack, buttonEnter};
			for (int i = 0; i <= 11; i++) {
				final int buttonVal = i;
				JButton numButton = buttons[i];
				
				if (buttonVal == 10) {
					numButton.setText("<");
				} else if (buttonVal == 11) {
					numButton.setText("E");
					numButton.setEnabled(false);
				} else {
					numButton.setText("" + buttonVal);
				}
				numButton.setFocusable(false);
				numButton.setBackground(GREY);
				numButton.setForeground(Color.white);
				numButton.setFont(MEDIUM);
				numButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1), (int) (WINDOW_WIDTH * 0.1)));
				AddItemByPLUScene thisScene = this;
				numButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// If the user pressed the backspace button
						if (buttonVal == 10) {
							if (thisScene.PLUCode == null || thisScene.PLUCode == "") {
								thisScene.PLUCode = "";
							} else {
								// Remove the last character from the PLU code
								thisScene.PLUCode = thisScene.PLUCode.substring(0, thisScene.PLUCode.length() - 1);
								
								// If this backspace has reduced the length of the code to less than 4 digits, disable the enter button
								if (thisScene.PLUCode.length() < 4) {
									buttonEnter.setEnabled(false);
								}
							}
						// If the user pressed the enter button
						} else if (buttonVal == 11) {
							if (thisScene.PLUCode == null) {
								thisScene.PLUCode = "";
							}
							
							// The user can only submit a PLU code with 4 or 5 digits
							if (thisScene.PLUCode.length() == 4 || thisScene.PLUCode.length() == 5) {
								if (thisScene.PLUCode == null) {
									thisScene.PLUCode = "";
								}
								
								// Turn the string into a list of numerals
								Numeral[] PLUNumerals = new Numeral[thisScene.PLUCode.length()];
								for (int i = 0; i < thisScene.PLUCode.length(); i++){
								    char c = thisScene.PLUCode.charAt(i);
								    
								    // Convert each character to a numeral and insert it into the array
								    if (c == '0') {
								    	PLUNumerals[i] = Numeral.zero;
								    } else if (c == '1') {
								    	PLUNumerals[i] = Numeral.one;
								    } else if (c == '2') {
								    	PLUNumerals[i] = Numeral.two;
								    } else if (c == '3') {
								    	PLUNumerals[i] = Numeral.three;
								    } else if (c == '4') {
								    	PLUNumerals[i] = Numeral.four;
								    } else if (c == '5') {
								    	PLUNumerals[i] = Numeral.five;
								    } else if (c == '6') {
								    	PLUNumerals[i] = Numeral.six;
								    } else if (c == '7') {
								    	PLUNumerals[i] = Numeral.seven;
								    } else if (c == '8') {
								    	PLUNumerals[i] = Numeral.eight;
								    } else if (c == '9') {
								    	PLUNumerals[i] = Numeral.nine;
								    }
								}
								
								// Convert the PLU string into a PLU code object
								PriceLookUpCode PLU = new PriceLookUpCode(PLUNumerals);
								
								// Create a new PLU coded unit
								PriceLookUpCodedUnit PLUUnit = new PriceLookUpCodedUnit(PLU, 1.0);
								
								// Notify the system that the user has selected a hypothetical item with this PLU
								system.setCurrentSelectableUnit(PLUUnit);
								
								try {
									// Attempt to add the item by PLU code
									if (system.addItemByPLU()) {
										noBag.launch();
										system.getStation().baggingArea.deregisterAll();
										// If there is a product matching the given PLU
										System.out.println("Typed PLU Code: " + thisScene.PLUCode);
										
										// Return to the transaction screen
										//transaction = new TransactionSummaryScene(null);
										//transaction.launch();
									} else {
										// There is no product matching the given PLU code
										// Re-launch the current scene,
										// but display a message indicating that there is no product matching the PLU the user entered
										AddItemByPLUScene addItemByPLUScene = new AddItemByPLUScene(previousScene);
										addItemByPLUScene.showWrongPLUMessage = true;
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						// If the user pressed a regular number button
						} else {
							if (thisScene.PLUCode == null) {
								thisScene.PLUCode = "" + buttonVal;
							} else {
								if (thisScene.PLUCode.length() < 5) {
									// Add the new character to the PLUCode, but only if we haven't yet 
									thisScene.PLUCode += buttonVal;
									
									// If the user has entered at least 4 digits, enable the enter button
									if (thisScene.PLUCode.length() >= 4) {
										buttonEnter.setEnabled(true);
									}
								}
							}
						}
						
						numField.setText(thisScene.PLUCode);
					}
				});
				numPanel.add(numButton);
			}
			// ----------------------------------------------------------------------
			

			// Add a text box to report to the user that no membership card was found with the entered number,
			// which only shows up if it was set while calling
			// ----------------------------------------------------------------------
			JLabel wrongPLUMessage = new JLabel("No matching PLU code found. Please try again!");
			if (showWrongPLUMessage) {
				wrongPLUMessage.setForeground(PINK);
			} else {
				wrongPLUMessage.setForeground(holdPanel.getBackground());
			}
			wrongPLUMessage.setFont(NORMAL);
			holdPanel.add(wrongPLUMessage, BorderLayout.SOUTH);
			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	
	public class AddItemByTypingScene extends GUIScene {
		public ArrayList<Product> productResults = new ArrayList<Product>();
		public String search = "";
		public boolean showPlaceItemOnScaleMessage = false;
		public AddItemByTypingScene thisScene = this;
		public DoNotBagItemScene noBag;
		
		public JButton backButton;
		
		public JButton buttonQ = new JButton();
		public JButton buttonW = new JButton();
		public JButton buttonE = new JButton();
		public JButton buttonR = new JButton();
		public JButton buttonT = new JButton();
		public JButton buttonY = new JButton();
		public JButton buttonU = new JButton();
		public JButton buttonI = new JButton();
		public JButton buttonO = new JButton();
		public JButton buttonP = new JButton();
		
		public JButton buttonA = new JButton();
		public JButton buttonS = new JButton();
		public JButton buttonD = new JButton();
		public JButton buttonF = new JButton();
		public JButton buttonG = new JButton();
		public JButton buttonH = new JButton();
		public JButton buttonJ = new JButton();
		public JButton buttonK = new JButton();
		public JButton buttonL = new JButton();
		public JButton buttonBack = new JButton();
		
		public JButton buttonZ = new JButton();
		public JButton buttonX = new JButton();
		public JButton buttonC = new JButton();
		public JButton buttonV = new JButton();
		public JButton buttonSpace = new JButton();
		public JButton buttonB = new JButton();
		public JButton buttonN = new JButton();
		public JButton buttonM = new JButton();
		
		public JButton selectItemButton;
		
		public JTextField numField;
		public JTable table;
		
		public TransactionSummaryScene transaction;
		
		public AddItemByTypingScene(GUIScene previous) {
			super(previous);
		}

		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			window.getContentPane().removeAll();
			window.repaint();
			
			noBag = new DoNotBagItemScene(null);
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			JPanel bigPanel = new JPanel(new BorderLayout());
			bigPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
			bigPanel.setBackground(Color.WHITE);
			
			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Search for Items");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);

			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(GREEN);
			backButton.setForeground(Color.WHITE);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			// get column headers ready
			String[] columns = {"Description", "Price"};
			
			// get all item data from database into 2d array
			Object[][] productData = new Object[productResults.size()][columns.length];
			int iter = 0;
			for (Product product : productResults) {
				Object[] row = new Object[columns.length];
				// Add the product description to the first column of the row
				if (product instanceof BarcodedProduct) {
					BarcodedProduct productCopy = (BarcodedProduct) product;
					row[0] = productCopy.getDescription();
				} else if (product instanceof PLUCodedProduct) {
					PLUCodedProduct productCopy = (PLUCodedProduct) product;
					row[0] = productCopy.getDescription();
				} else {
					row[0] = "";
				}
				// Add the product price to the first column of the row
				row[1] = product.getPrice().toString();
				// Add the row to the array
				productData[iter] = row;
				++iter;
			}

			// make a table model with the data
			DefaultTableModel model = new DefaultTableModel(productData, columns);
			
			// create a table from the model
			table = new JTable(model);
			
			// set non-editable, but enabled, so we can target selected rows/cells/columns
			table.setDefaultEditor(Object.class, null);

			// slap table into a view pane
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);

			/*
			 * Add a button to allow the user
			 * to add the selected item
			 * 
			 * 
			 */
				
			
			selectItemButton = new JButton("Select Item");
			selectItemButton.setBackground(GREEN);
			selectItemButton.setForeground(Color.WHITE);
			selectItemButton.setFont(MEDIUM);
			// Set the preferred size of the button
			// selectItemButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			selectItemButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// If there is at least one product in the table
					if (productResults.size() > 0) {
						// get the selected row
						int activeRow = table.getSelectedRow();
						
						// If the user actually selected a row
						if (activeRow != -1) {
							// Get the selected product
							Product activeProduct = productResults.get(activeRow);
							
							try {
								double unitWeight = system.getStation().scale.getCurrentWeight();
								if (Math.abs(unitWeight) < system.getStation().scale.getSensitivity()) {
									unitWeight = 5.0;
								}
								
								if (activeProduct instanceof BarcodedProduct) {
									BarcodedProduct productCopy = (BarcodedProduct) activeProduct;
									Barcode barcode = productCopy.getBarcode();
									// Get a specific unit from the barcoded product
									BarcodedUnit unit = new BarcodedUnit(barcode, unitWeight);
									
									system.setCurrentSelectableUnit(unit);
								} else if (activeProduct instanceof PLUCodedProduct) {
									PLUCodedProduct productCopy = (PLUCodedProduct) activeProduct;
									PriceLookUpCode PLU = productCopy.getPLUCode();
									// Get a specific unit from the PLU coded product
									PriceLookUpCodedUnit unit = new PriceLookUpCodedUnit(PLU, unitWeight);
									
									system.setCurrentSelectableUnit(unit);
								}
								
								try {
									system.addItemByTyping();
									noBag.launch();
									system.getStation().baggingArea.deregisterAll();


								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								// Return to the transaction summary scene
								transaction = new TransactionSummaryScene(null);

								//transaction.launch();

								
							} catch (OverloadException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
					}
				}
			});
			
			selectPanel.add(selectItemButton, BorderLayout.SOUTH);
			
			window.add(bigPanel, BorderLayout.CENTER);
			bigPanel.add(selectPanel, BorderLayout.NORTH);
			
			// Add a text box telling the customer to place an item on the scale
			JLabel placeItemOnScaleMessage = new JLabel("Please place item on scale.");
			if (showPlaceItemOnScaleMessage) {
				placeItemOnScaleMessage.setForeground(PINK);
			} else {
				placeItemOnScaleMessage.setForeground(bigPanel.getBackground());
			}
			placeItemOnScaleMessage.setFont(MEDIUM);
			bigPanel.add(placeItemOnScaleMessage, BorderLayout.CENTER);

			// Add on screen keyboard and text field for searching for products
			JPanel holdPanel = new JPanel(new BorderLayout((int) (WINDOW_WIDTH * 0.01), (int) (WINDOW_WIDTH * 0.01)));
			holdPanel.setBackground(Color.WHITE);
			bigPanel.add(holdPanel, BorderLayout.SOUTH);
			
			// Add the text field
			// ----------------------------------------------------------------------
			numField = new JTextField(search);
			numField.setEditable(false);
			numField.setFont(NORMAL);
			holdPanel.add(numField, BorderLayout.NORTH);
			
			// Add the number pad
			// ----------------------------------------------------------------------
			JPanel numPanel = new JPanel(new GridLayout(3, 10));
			numPanel.setBackground(Color.WHITE);
			holdPanel.add(numPanel, BorderLayout.CENTER);
			
			String[] buttonLabels = {
		            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
		            "A", "S", "D", "F", "G", "H", "J", "K", "L", "Backspace",
		            "Z", "X", "C", "V", "Space", "B", "N", "M"
		    };
			JButton[] buttons = {
				buttonQ, buttonW, buttonE, buttonR, buttonT, buttonY, buttonU, buttonI, buttonO, buttonP,
				buttonA, buttonS, buttonD, buttonF, buttonG, buttonH, buttonJ, buttonK, buttonL, buttonBack,
				buttonZ, buttonX, buttonC, buttonV, buttonSpace, buttonB, buttonN, buttonM
			};
			int i = 0;
			for (String buttonLabel : buttonLabels) {
				JButton alphaButton = buttons[i];
				
				alphaButton.setText(buttonLabel);
				alphaButton.setFocusable(false);
				alphaButton.setBackground(GREY);
				alphaButton.setForeground(Color.white);
				alphaButton.setFont(MEDIUM);
				// alphaButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1), (int) (WINDOW_WIDTH * 0.1)));
				
				// Remove all existing action listeners from this button
				// (I have no idea why extra action listeners are being added, but this fixes it so it stays)
				for( ActionListener al : alphaButton.getActionListeners() ) {
			        alphaButton.removeActionListener( al );
			    }
				
				alphaButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// If the user pressed the backspace button
						if (buttonLabel == "Backspace") {
							if (thisScene.search == null || thisScene.search == "") {
								thisScene.search = "";
							} else {
								// Remove the last character from the search prompt
								thisScene.search = thisScene.search.substring(0, thisScene.search.length() - 1);
							}
						// If the user pressed the space button
						} else if (buttonLabel == "Space") {
							if (thisScene.search == null) {
								thisScene.search = " ";
							} else {
								// Remove the last character from the search prompt
								thisScene.search += " ";
							}
						// If the user pressed a regular button
						} else {
							if (thisScene.search == null) {
								thisScene.search = "" + buttonLabel;
							} else {
								// Add the new character to the search prompt
								thisScene.search += buttonLabel;
							}
						}
						
						numField.setText(thisScene.search);
						
						// Update the search results
						SearchItems searchItems = new SearchItems();
						productResults = searchItems.search(thisScene.search);
						thisScene.launch();
					}
				});
				numPanel.add(alphaButton);
				i++;
			}
			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	
	/**
	 * Method for allowing the use to add item by browsing databases
	 * 
	 * 
	 * @author desmo
	 *
	 */
	public class AddItemByBrowsingScene extends GUIScene {
		private boolean item_selected = false;
		
		public JButton backButton;
		public JButton bananaButton;
		public JButton appleButton;
		public JButton potatoButton;
		public JButton selectItemButton;
		

		DoNotBagItemScene noBag;

		public JTable table;
		
		TransactionSummaryScene transaction;

		
		public AddItemByBrowsingScene(GUIScene previous) {
			super(previous);
		}
		
		
		
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// get column headers ready
			String[] columns = {"Item Code", "Description", "Price", "Expected Weight"};
			BarcodedUnit potato = new BarcodedUnit(Main.POTATO_CODE, Main.POTATO_WEIGHT);
			BarcodedUnit apple = new BarcodedUnit(Main.APPLE_CODE, Main.APPLE_WEIGHT);
			BarcodedUnit banana = new BarcodedUnit(Main.BANANA_CODE, Main.BANANA_WEIGHT);
			
			
			Object[][] productData = new Object[ProductDatabases.BARCODED_PRODUCT_DATABASE.keySet().size()][4];
			
			
			// get all item data from database into 2d array
			int iter = 0;
			for (Barcode barcode : ProductDatabases.BARCODED_PRODUCT_DATABASE.keySet()) {
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				Object[] row = new Object[4];
				row[0] = barcode.toString();
				row[1] = product.getDescription();
				row[2] = product.getPrice().toString();
				row[3] = String.valueOf(product.getExpectedWeight());
				productData[iter] = row;
				++iter;
			}
			
			noBag = new DoNotBagItemScene(null);
			// make a table model with the data
			DefaultTableModel model = new DefaultTableModel(productData, columns);
			
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Browse Items");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			

				backButton = new JButton("<html><center>Back</center></html>");
				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				backButton.setFocusable(false);
				// Set colors
				backButton.setBackground(GREEN);
				backButton.setForeground(Color.WHITE);
				// Set font
				backButton.setFont(NORMAL);
				// Make the button change scenes
				backButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Launch a new scene
						previousScene.launch();
						
					}
				});
				bottomPanel.add(backButton);
			
			if (item_selected) {
				bananaButton = new JButton("<html><center>Banana</center></html>");
				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				bananaButton.setFocusable(false);
				// Set colors
				bananaButton.setBackground(GREEN);
				bananaButton.setForeground(Color.WHITE);
				// Set font
				bananaButton.setFont(NORMAL);
				// Make the button change scenes
				transaction = new TransactionSummaryScene(null);
				bananaButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						system.setCurrentSelectableUnit(banana);
						try {
							system.addItemByBrowsing();
							noBag.launch();
							system.getStation().baggingArea.deregisterAll();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println(system.getDiscrepancyActive());

			

					}
				});
				selectPanel.add(bananaButton);
				
				
				
				appleButton = new JButton("<html><center>Apple</center></html>");
				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				appleButton.setFocusable(false);
				// Set colors
				appleButton.setBackground(GREEN);
				appleButton.setForeground(Color.WHITE);
				// Set font
				appleButton.setFont(NORMAL);
				// Make the button change scenes
				transaction = new TransactionSummaryScene(null);
				appleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						system.setCurrentSelectableUnit(apple);
						try {
							system.addItemByBrowsing();
							noBag.launch();
							system.getStation().baggingArea.deregisterAll();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println(system.getDiscrepancyActive());


					}
				});
				selectPanel.add(appleButton);
			
			
			
			potatoButton = new JButton("<html><center>Potato</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			potatoButton.setFocusable(false);
			// Set colors
			potatoButton.setBackground(GREEN);
			potatoButton.setForeground(Color.WHITE);
			// Set font
			potatoButton.setFont(NORMAL);
			// Make the button change scenes
			transaction = new TransactionSummaryScene(null);
			potatoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					system.setCurrentSelectableUnit(potato);
					try {
						system.addItemByBrowsing();
						noBag.launch();
						system.getStation().baggingArea.deregisterAll();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(system.getDiscrepancyActive());

				}
			});
			selectPanel.add(potatoButton);
			}
			

		
			window.add(bottomPanel, BorderLayout.SOUTH);

			
			// create a table from the model above
			table = new JTable(model);
			
			// set non-editable, but enabled, so we can target selected rows/cells/columns
			table.setDefaultEditor(Object.class, null);

			// slap table into a view pane
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);

			/*
			 * Add a button to allow the user
			 * to add the selected item
			 * 
			 * 
			 */
			
			if (!item_selected) {
				
			
				selectItemButton = new JButton("Select Item");
				selectItemButton.setBackground(GREEN);
				selectItemButton.setForeground(Color.WHITE);
				selectItemButton.setFont(MEDIUM);
				// Set the preferred size of the button
				selectItemButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
				// Make the button change scenes
				selectItemButton.addActionListener(new ActionListener() {
					// TODO: Only works for barcoded products atm, will change to include PLU coded
					@Override
					public void actionPerformed(ActionEvent e) {
						// get the selected row
						int activeRow = table.getSelectedRow();
						
						// get the barcode and expected weight of row
						String barcode = (String) table.getValueAt(activeRow,0);
						String expectedWeight = (String) table.getValueAt(activeRow,3);
					
		
						// build a numeral array with the string representation of the barcode
						Numeral[] numerals = new Numeral[ barcode.length()];
						for (int i = 0; i < numerals.length; ++i) {
							System.out.println(barcode.charAt(i));
							numerals[i] = Numeral.valueOf((byte) (barcode.charAt(i) - 48));
						}
						
						
						// set the system's current selected product to the item with that barcode from the database
						// NOTE: NEW FIELD WAS CREATED IN SELFCHECKOUTSYSTEMLOGIC FOR THIS
						
	
						system.setCurrentSelectedProduct(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(new Barcode(numerals)));
						item_selected = true;
						launch();
						

					}
				});
				
				selectPanel.add(selectItemButton, BorderLayout.SOUTH);
			}
			window.add(selectPanel, BorderLayout.CENTER);

			

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
	}
	
	
	public class ThankYouScene extends GUIScene {
		
		public JButton startButton;

		//public WelcomeScene welcomeScene = new WelcomeScene(this);
		
		public ThankYouScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// Clear the window
			// MUST GO AT THE BEGINNING OF EVERY SCENE
			window.getContentPane().removeAll();
			window.repaint();
			
			// Add the welcome message at the top
			// ----------------------------------------------------------------------
			// Create a new panel
			JPanel welcomePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			welcomePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(welcomePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel welcomeMessage = new JLabel("Thank you!");
			// Set the font
			welcomeMessage.setFont(HEADER);
			// Set the color of the text
			welcomeMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			welcomePanel.add(welcomeMessage);
			// ----------------------------------------------------------------------
			
			// Add start button
			// ----------------------------------------------------------------------
			JPanel startPanel = new JPanel();
			// createEmptyBorder(top, left, bottom, right)
			startPanel.setBorder(BorderFactory.createEmptyBorder((int) (WINDOW_HEIGHT * 0.255),
																 (int) (WINDOW_WIDTH * 0.255),
																 (int) (WINDOW_HEIGHT * 0.255),
																 (int) (WINDOW_WIDTH * 0.255)));
			startPanel.setBackground(Color.WHITE);
			window.add(startPanel, BorderLayout.CENTER);
			
			JLabel thanksLabel = new JLabel(String.format("<html>Thank you for shopping with us!<br>Please remember to take your items<br>and have a great day!<html>", getTotalPrice()));
			thanksLabel.setFont(new Font("Sans-serif", Font.BOLD, 48));
				
			startPanel.add(thanksLabel);
			// Create a new button
			startButton = new JButton("START");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			startButton.setFocusable(false);
			// Set colors
			startButton.setBackground(GREEN);
			startButton.setForeground(Color.WHITE);
			// Set font
			startButton.setFont(new Font("Sans-serif", Font.BOLD, 24));
			// Set the preferred size of the button
			startButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1875), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch the new scene
					
					//TODO: STATION IS RESET ON THIS CLICK
					system.resetStation();
					transactionPaidFor = false;
					DoItYourselfStation.rootScene.launch();
					if (system.attendant != null) {
						system.attendant.updateGUI();
					}
			

				}
			});
			// Add the button to the panel
			startPanel.add(startButton);
			// ----------------------------------------------------------------------
			
			// Add language select and membership buttons
			// ----------------------------------------------------------------------
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			


			// ----------------------------------------------------------------------
			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
		
	}
	
	
	
	
	public class PrintReceiptScene extends GUIScene {
		
		public JButton exitButton;
		public JButton takeReceiptButton;
		public JButton backButton;

		public ThankYouScene thankYou;
		
		public PrintReceiptScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// columns for table contents

			
			window.getContentPane().removeAll();
			window.repaint();
			
			/*
			 * This is all pretty much copy-pasted from Dylan
			 * 
			 * 
			 */
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Please take your receipt");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			
			exitButton = new JButton("<html><center>Exit<br>Station</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			exitButton.setFocusable(false);
			// Set colors
			exitButton.setBackground(GREEN);
			exitButton.setForeground(Color.WHITE);
			// Make the button change scenes
			thankYou = new ThankYouScene(this);
			exitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					thankYou.launch();
					
				}
			});
			
			

		
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
	

			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			JTextArea receipt = new JTextArea();
			receipt.setFont(new Font("Sans-serif", Font.BOLD, 24));
			receipt.setForeground(Color.BLACK);
			receipt.setBorder(BorderFactory.createBevelBorder(10, Color.BLACK, Color.BLACK));
			receipt.setEditable(false);
			
			
			selectPanel.add(receipt);
			
			takeReceiptButton = new JButton("<html><center>Remove<br>Receipt</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			takeReceiptButton.setFocusable(false);
			// Set colors
			takeReceiptButton.setBackground(GREEN);
			takeReceiptButton.setForeground(Color.WHITE);
			// Make the button change scenes
			takeReceiptButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					system.setPrinting(transactionPaidFor);
					try {
						system.startPrinting(system.getBillList());
					} catch (EmptyException | OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					receipt.setText(system.getStation().printer.removeReceipt());
				}
			});
			
			selectPanel.add(receipt);
			selectPanel.add(takeReceiptButton);
			selectPanel.add(exitButton);
			


		
			

			
			
			
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			window.add(selectPanel, BorderLayout.CENTER);
			
			
			

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
		
		
	}
	
	
	public class PurchaseBagsScene extends GUIScene {

		public JButton plusButton;
		public JButton minusButton;
		public JButton addButton;
		public JButton backButton;
		public TransactionSummaryScene transaction;
		
		public PurchaseBagsScene(GUIScene previousScene) {
			super(previousScene);
			// TODO Auto-generated constructor stub
		}
		
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.isBlocked()) {
				renderDisabled();
				return;
			}
			// columns for table contents
			String[] columns = {"Description", "Price"};
			
			// current items
			
			// Object array to build table contents, based on system's bill list
			Object[][] dataCurrentItems = new Object[system.getBillList().size()][2];
			
			// iterate through each item and add description and price to table
			int iter = 0;
			for (Product product : system.getBillList()) {
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
			transaction = new TransactionSummaryScene(null);
			
			window.getContentPane().removeAll();
			window.repaint();
			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			
			/*
			 * This is all pretty much copy-pasted from Dylan
			 * 
			 * 
			 */
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Purchase Bags");
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
	
			JTextField bagCount = new JTextField("0");
			bagCount.setEditable(false);
			bagCount.setFont(NORMAL);
			

			
			// create a table, disable it so cant edit
			JTable table = new JTable(model);
			table.setEnabled(false);

			// slap table in a scroll pane and add to main panel
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);

			// make a button for moving to add item selection
			plusButton = new JButton("+");
			plusButton.setBackground(GREEN);
			plusButton.setForeground(Color.WHITE);
			plusButton.setFont(MEDIUM);
			// Set the preferred size of the button
			plusButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			plusButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int count = Integer.parseInt(bagCount.getText());
					bagCount.setText(String.format("%d", ++count));
				}
			});
			
			
			// make a button for moving to add item selection
			minusButton = new JButton("-");
			minusButton.setBackground(GREEN);
			minusButton.setForeground(Color.WHITE);
			minusButton.setFont(MEDIUM);
			// Set the preferred size of the button
			minusButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			minusButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!bagCount.getText().equals("0")) {
						int count = Integer.parseInt(bagCount.getText());
						bagCount.setText(String.format("%d", --count));
					}
				}
			});
			selectPanel.add(minusButton, BorderLayout.SOUTH);
			selectPanel.add(bagCount);
			selectPanel.add(plusButton, BorderLayout.SOUTH);
			selectPanel.add(bagCount);
			
			
			// make a button for moving to add item selection
			addButton = new JButton("Add Bags");
			addButton.setBackground(GREEN);
			addButton.setForeground(Color.WHITE);
			addButton.setFont(MEDIUM);
			// Set the preferred size of the button
			addButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					system.setNumberBagsPurchased(Integer.parseInt(bagCount.getText()));
					try {
						system.purchaseBags();
						system.getStation().baggingArea.deregisterAll();
					} catch (OverloadException | EmptyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					transaction.launch();
				}
			});
			selectPanel.add(addButton, BorderLayout.SOUTH);
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			

			window.add(selectPanel, BorderLayout.CENTER);
			
			// add a lable with total due, formatting is scuffed AF as evident on the line below, will fix when i figure out layouts better
			JLabel totalDue = new JLabel(String.format("                                                 Total amount due: %s", getTotalPrice()));
			totalDue.setFont(new Font("Sans-serif", Font.BOLD, 24));
			selectPanel.add(totalDue);

			

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
	}
	
	public class PayWithCashScene extends GUIScene {
		
		private final Color FIVE_COLOR = new Color(0x7694b9);
		private final Color TEN_COLOR = new Color(0xad93b6);
		private final Color TWENTY_COLOR = new Color(0x9fbd9d);
		private final Color FIFTY_COLOR = new Color(0xbe6a67);
		private final Color HUNDRED_COLOR = new Color(0xd9c38a);
		private final Color COIN_COLOR = new Color(0xc5c5c5);
		private final Color LOONIE_COLOR = new Color(0xa57d34);
		
		private boolean billDangling;
		
		public JButton fiveBillButton;
		public JButton tenBillButton;
		public JButton twentyBillButton;
		public JButton fiftyBillButton;
		public JButton hundredBillButton;
		
		public JButton nickelcoinButton;
		public JButton dimecoinButton;
		public JButton quartercoinButton;
		public JButton looniecoinButton;
		public JButton tooniecoinButton;
		
		public JButton printReceiptButton;
		
		public JButton backButton;
		
		public PayWithCashScene(GUIScene previousScene) {
			super(previousScene);
		}
		
		
		@Override
		public void launch() {
			system.setCurrentScene(this);
			if (system.getBlocked()) {
				renderDisabled();
				return;
			}
			// columns for table contents
			String[] columns = {"Description", "Price"};
			
			// current items
			
			// Object array to build table contents, based on system's bill list
			Object[][] dataCurrentItems = new Object[system.getBillList().size()][2];
			
			// iterate through each item and add description and price to table
			int iter = 0;
			for (Product product : system.getBillList()) {
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
			
			window.getContentPane().removeAll();
			window.repaint();
			
			/*
			 * This is all pretty much copy-pasted from Dylan
			 * 
			 * 
			 */
			JPanel titlePanel = new JPanel();
			// Set a 10 pixel border around the edge of the panel
			titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Set the color of the panel
			titlePanel.setBackground(GREEN);
			// Add the panel to the window
			window.add(titlePanel, BorderLayout.NORTH);
			
			// Create a text box
			JLabel titleMessage = new JLabel("Insert Bills and Coins");
			// Set the font
			titleMessage.setFont(HEADER);
			// Set the color of the text
			titleMessage.setForeground(Color.WHITE);
			// Add the text to the panel
			titlePanel.add(titleMessage);
			
			
			JPanel bottomPanel = new JPanel(new FlowLayout());
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			bottomPanel.setBackground(GREEN);
			window.add(bottomPanel, BorderLayout.SOUTH);
			
	

			
			JPanel selectPanel = new JPanel(new FlowLayout());
			selectPanel.setBackground(Color.WHITE);
			System.out.println(billDangling);
			if (billDangling) {
				JPanel removeBillPanel = new JPanel();
				JButton removeDangling = new JButton("Bill is stuck! Click to remove!");

				// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
				removeDangling.setFocusable(false);
				removeDangling.setFont(MEDIUM);
				// Set the preferred size of the button
				removeDangling.setPreferredSize(new Dimension((int) (WINDOW_WIDTH), (int) (WINDOW_HEIGHT * 0.167)));
				removeDangling.setBackground(GREEN);
				removeDangling.setForeground(Color.WHITE);
				// Make the button change scenes
				removeDangling.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Launch a new scene
						billDangling = false;
						Bill removedBill = system.getStation().billInput.removeDanglingBill();		
						launch();
						//system.setAmountDue(system.getAmountDue() + );
					}
				});
				removeBillPanel.add(removeDangling);
				window.add(removeBillPanel, BorderLayout.SOUTH);
			}
			
			// create a table, disable it so cant edit
			JTable table = new JTable(model);
			table.setEnabled(false);

			// slap table in a scroll pane and add to main panel
			JScrollPane tableView = new JScrollPane();
			tableView.setViewportView(table);
			selectPanel.add(tableView);

			// make a button for moving to add item selection
			fiveBillButton = new JButton("$5 Bill");
			fiveBillButton.setBackground(FIVE_COLOR);
			fiveBillButton.setForeground(Color.WHITE);
			fiveBillButton.setFont(MEDIUM);
			// Set the preferred size of the button
			fiveBillButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			fiveBillButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Bill fiver = new Bill(5, Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException | SimulationException e1) {
						e1.printStackTrace();					
					}
					try {
						system.getStation().billInput.accept(fiver);
					} catch (DisabledException e1) {
						e1.printStackTrace();
					}
					catch (OverloadException e1) {
						billDangling = true;
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			tenBillButton = new JButton("$10 Bill");
			tenBillButton.setBackground(TEN_COLOR);
			tenBillButton.setForeground(Color.WHITE);
			tenBillButton.setFont(MEDIUM);
			// Set the preferred size of the button
			tenBillButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			tenBillButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Bill tener = new Bill(10, Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException | SimulationException e1) {
						e1.printStackTrace();					
					}
					try {
						system.getStation().billInput.accept(tener);
					} catch (DisabledException e1) {
						e1.printStackTrace();
					}
					catch (OverloadException e1) {
						billDangling = true;
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
				
			});
			
			twentyBillButton = new JButton("$20 Bill");
			twentyBillButton.setBackground(TWENTY_COLOR);
			twentyBillButton.setForeground(Color.WHITE);
			twentyBillButton.setFont(MEDIUM);
			// Set the preferred size of the button
			twentyBillButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			twentyBillButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Bill twenty = new Bill(20, Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException | SimulationException e1) {
						e1.printStackTrace();					
					}
					try {
						system.getStation().billInput.accept(twenty);
					} catch (DisabledException e1) {
						e1.printStackTrace();
					}
					catch (OverloadException e1) {
						billDangling = true;
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			fiftyBillButton = new JButton("$50 Bill");
			fiftyBillButton.setBackground(FIFTY_COLOR);
			fiftyBillButton.setForeground(Color.WHITE);
			fiftyBillButton.setFont(MEDIUM);
			// Set the preferred size of the button
			fiftyBillButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			fiftyBillButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Bill fifty = new Bill(50, Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException | SimulationException e1) {
						e1.printStackTrace();					
					}
					try {
						system.getStation().billInput.accept(fifty);
					} catch (DisabledException e1) {
						e1.printStackTrace();
					}
					catch (OverloadException e1) {
						billDangling = true;
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			hundredBillButton = new JButton("$100 Bill");
			hundredBillButton.setBackground(HUNDRED_COLOR);
			hundredBillButton.setForeground(Color.WHITE);
			hundredBillButton.setFont(MEDIUM);
			// Set the preferred size of the button
			hundredBillButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			hundredBillButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Bill hundred = new Bill(100, Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException | SimulationException e1) {
						e1.printStackTrace();					
					}
					try {
						system.getStation().billInput.accept(hundred);
					} catch (DisabledException e1) {
						e1.printStackTrace();
					}
					catch (OverloadException e1) {
						billDangling = true;
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			
			nickelcoinButton = new JButton("$0.05");
			nickelcoinButton.setBackground(COIN_COLOR);
			nickelcoinButton.setForeground(Color.WHITE);
			nickelcoinButton.setFont(MEDIUM);
			// Set the preferred size of the button
			nickelcoinButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			nickelcoinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Coin nickel = new Coin(BigDecimal.valueOf(0.05f), Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (SimulationException | OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						system.getStation().coinSlot.accept(nickel);
					} catch (DisabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			dimecoinButton = new JButton("$0.10");
			dimecoinButton.setBackground(COIN_COLOR);
			dimecoinButton.setForeground(Color.WHITE);
			dimecoinButton.setFont(MEDIUM);
			// Set the preferred size of the button
			dimecoinButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			dimecoinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Coin dime = new Coin(BigDecimal.valueOf(0.05f), Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (SimulationException | OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						system.getStation().coinSlot.accept(dime);
					} catch (DisabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			quartercoinButton = new JButton("$0.25");
			quartercoinButton.setBackground(COIN_COLOR);
			quartercoinButton.setForeground(Color.WHITE);
			quartercoinButton.setFont(MEDIUM);
			// Set the preferred size of the button
			quartercoinButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			quartercoinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Coin quarter = new Coin(BigDecimal.valueOf(0.25f), Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (OverloadException e1) {
						// TODO Auto-generated catch block
						
					}
					catch (SimulationException e1) {
							e1.printStackTrace();
					}
					try {
						system.getStation().coinSlot.accept(quarter);
					} catch (DisabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			looniecoinButton = new JButton("$1");
			looniecoinButton.setBackground(LOONIE_COLOR);
			looniecoinButton.setForeground(Color.WHITE);
			looniecoinButton.setFont(MEDIUM);
			// Set the preferred size of the button
			looniecoinButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			looniecoinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Coin loonie = new Coin(BigDecimal.valueOf(1f), Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (SimulationException | OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						system.getStation().coinSlot.accept(loonie);
					} catch (DisabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			tooniecoinButton = new JButton("$2");
			tooniecoinButton.setBackground(LOONIE_COLOR);
			tooniecoinButton.setBorder(BorderFactory.createLineBorder(COIN_COLOR));
			tooniecoinButton.setForeground(Color.WHITE);
			tooniecoinButton.setFont(MEDIUM);
			// Set the preferred size of the button
			tooniecoinButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
			// Make the button change scenes
			tooniecoinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Coin toonie = new Coin(BigDecimal.valueOf(2f), Currency.getInstance(Locale.CANADA));
					try {
						system.payWithCash();
					} catch (SimulationException | OverloadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						system.getStation().coinSlot.accept(toonie);
					} catch (DisabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					system.clearPayWithCashObservers();
					checkTransactionDone();
					launch();
				}
			});
			
			
			JPanel billPanel = new JPanel();
			billPanel.setBackground(Color.WHITE);
			billPanel.setPreferredSize(new Dimension(window.getWidth() / 6, window.getHeight()));
			window.add(billPanel, BorderLayout.WEST);
			

			
			//billPanel.add(Box.createVerticalStrut(window.getHeight() / 8));
			billPanel.add(fiveBillButton);
			//billPanel.add(Box.createVerticalStrut(window.getHeight() / 8));
			billPanel.add(tenBillButton);
			//billPanel.add(Box.createVerticalStrut(window.getHeight() / 8));
			billPanel.add(twentyBillButton);
			//billPanel.add(Box.createVerticalStrut(window.getHeight() / 8));
			billPanel.add(fiftyBillButton);
			//billPanel.add(Box.createVerticalStrut(window.getHeight() / 8));
			billPanel.add(hundredBillButton);
			
			
			
			JPanel coinPanel = new JPanel();
			coinPanel.setBackground(Color.WHITE);
			coinPanel.setPreferredSize(new Dimension(window.getWidth() / 6, window.getHeight()));
			window.add(coinPanel, BorderLayout.EAST);
			
			coinPanel.add(nickelcoinButton);
			coinPanel.add(dimecoinButton);
			coinPanel.add(quartercoinButton);
			coinPanel.add(looniecoinButton);
			coinPanel.add(tooniecoinButton);
			
			if (system.getAmountDue() <= 0.01) {

				printReceiptButton = new JButton("Print Receipt");
				printReceiptButton.setBackground(GREEN);
				printReceiptButton.setForeground(Color.WHITE);
				printReceiptButton.setFont(new Font("Sans-serif", Font.BOLD, 24));
				// Set the preferred size of the button
				printReceiptButton.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.375), (int) (WINDOW_HEIGHT * 0.167)));
				// Make the button change scenes
				PrintReceiptScene printReceipt = new PrintReceiptScene(this);
				printReceiptButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						printReceipt.launch();
					}
				});
				selectPanel.add(printReceiptButton);
			}
			
			
			backButton = new JButton("<html><center>Back</center></html>");
			// Set the button to not be focusable (which prevents a little dotted box around the text on the button)
			backButton.setFocusable(false);
			// Set colors
			backButton.setBackground(Color.WHITE);
			backButton.setForeground(GREEN);
			// Set font
			backButton.setFont(NORMAL);
			// Make the button change scenes
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Launch a new scene
					previousScene.launch();
					
				}
			});
			bottomPanel.add(backButton);
			
			window.add(selectPanel, BorderLayout.CENTER);
			
			// add a lable with total due, formatting is scuffed AF as evident on the line below, will fix when i figure out layouts better
			JLabel totalDue = new JLabel(String.format("Remaining Balance: %s", getTotalPrice()));
			totalDue.setFont(new Font("Sans-serif", Font.BOLD, 24));
			selectPanel.add(totalDue);

			

			
			// Validate the new contents of the window
			// MUST GO AT THE END OF EVERY SCENE
			window.validate();
		}
		
	}
	
	
	public void show() {
		window.setVisible(true);
	}
	
	public void hide() {
		window.setVisible(false);
	}
}
