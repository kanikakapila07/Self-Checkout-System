package com.autovend.software;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import com.autovend.*;
import com.autovend.products.*;
import com.autovend.devices.SelfCheckoutStation;

import javax.swing.JButton;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Currency;

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

public class KeyboardLogin {
	private AttendantStationLogic system;
	public JFrame frame;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JPanel keyboardPanel;
	private String selected = "user";
	private String pressed;
	private JLabel invalidLabel;
	private Color GREEN = new Color(135, 240, 132, 255);
	private Color GREY = new Color(57, 61, 63, 255);
	private Color BEIGE = new Color(213, 187, 177, 255);
	private Color PEACH = new Color(201, 140, 167, 255);
	private Color PINK = new Color(231, 109, 131, 255);
	
	private static int WINDOW_WIDTH = 800;
	private static int WINDOW_HEIGHT = 600;
	

	/**
	 * Launch the application.
	 *
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int[] billDenoms = {5,10,20,50,100};
					
					BigDecimal[] coinDenominations = new BigDecimal[] { BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),
							BigDecimal.valueOf(0.25), BigDecimal.valueOf(1), BigDecimal.valueOf(2) };
					Currency currency = Currency.getInstance("CAD");
					
					int scaleMax = 100;
					int scaleSensitivity = 1;
					Barcode marsBarBarcode = new Barcode(Numeral.zero,Numeral.zero,Numeral.one);
					BigDecimal mbPrice = new BigDecimal(1.25);
					BarcodedProduct marsBar = new BarcodedProduct(marsBarBarcode,"Mars Chocolate Bar",mbPrice,15);
				
					SelfCheckoutStation station = new SelfCheckoutStation(currency, billDenoms, coinDenominations, scaleMax, scaleSensitivity);
					SelfCheckoutStation station2 = new SelfCheckoutStation(currency, billDenoms, coinDenominations, scaleMax, scaleSensitivity);
					SelfCheckoutSystemLogic system = new SelfCheckoutSystemLogic(station);
					system.addBillList(marsBar);
					SelfCheckoutSystemLogic system2 = new SelfCheckoutSystemLogic(station2);
					AttendantStationLogic attend = new AttendantStationLogic();
					attend.addUser("RYAN", "1234");
					attend.addStation(system);
					attend.addStation(system2);
					KeyboardLogin window = new KeyboardLogin(attend);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	
	/**
	 * Create the application.
	 */
	public KeyboardLogin(AttendantStationLogic attendant) {
		system = attendant;
		initialize();
	}
	
	//Is called when a keyboard button is pressed
	public void react(String label) {
		if (label.equals("Enter")) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (system.login(username,password) == true) {
            	
            	frame.dispose();
            	AttendantGUI window = new AttendantGUI(system);
            	window.frame.setVisible(true);
            }else {
            	invalidLabel.setText("Invalid. Try again");
            	usernameField.setText("");
            	passwordField.setText("");
            	
            }
		}else if (selected == "user") {
			if (label.equals("Backspace")) {
	            String text = usernameField.getText();
	            if (text.length() > 0) {
	                text = text.substring(0, text.length() - 1);
	                usernameField.setText(text);
	            }
	        }else {
	            String text = usernameField.getText() + label;
	            usernameField.setText(text);
	        }
		}else if (selected == "password") {
			if (label.equals("Backspace")) {
	            String text = passwordField.getText();
	            if (text.length() > 0) {
	                text = text.substring(0, text.length() - 1);
	                passwordField.setText(text);
	            }
	        }else {
	            String text = passwordField.getText() + label;
	            passwordField.setText(text);
	        }
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(GREEN);
		
		passwordField = new JPasswordField();
		passwordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selected = "password";
			}
		});
		passwordField.setBounds(146, 121, 142, 26);
		frame.getContentPane().add(passwordField);
		passwordField.setEchoChar('*');
		
		usernameField = new JTextField();
		usernameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selected = "user";
			}
		});
		usernameField.setBounds(146, 68, 142, 26);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		
		
		JLabel lblUserID = new JLabel("User ID:");
		lblUserID.setBounds(146, 54, 61, 16);
		frame.getContentPane().add(lblUserID);
		
		JLabel lblNewLabel = new JLabel("Password:");
		lblNewLabel.setBounds(146, 106, 82, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel keyboardPanel = new JPanel();
		keyboardPanel.setBounds(46, 160, 700, 200);
		frame.getContentPane().add(keyboardPanel);
		keyboardPanel.setLayout(new GridLayout(4, 10, 0, 0));
		
		invalidLabel = new JLabel(" ");
		invalidLabel.setBounds(146, 26, 142, 16);
		frame.getContentPane().add(invalidLabel);
		String[] buttonLabels = {
	            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
	            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
	            "A", "S", "D", "F", "G", "H", "J", "K", "L", "Backspace",
	            "Z", "X", "C", "V", "B", "N", "M", ".", "@", "Enter"
	        };
		for (String label : buttonLabels) {
            JButton button = new JButton(label);
           button.addMouseListener(new MouseAdapter() {
        	   @Override
   			public void mousePressed(MouseEvent e) {
   				pressed = label;
   				react(label);
   			}
           });
            keyboardPanel.add(button);
        }
	}
	
	public JLabel getInvalidLabel() {
		return invalidLabel;
	}
	
	public void setUsername(String username) {
		usernameField.setText(username);

	}
	
	public void setPassword(String pw) {
		passwordField.setText(pw);
	}
	
}
