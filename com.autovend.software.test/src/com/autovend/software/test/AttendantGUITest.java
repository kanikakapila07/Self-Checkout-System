package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.awt.event.MouseEvent;

/** Iternation #3 
Muhammad Asjad Zubair â€“ 30147898,  
Rohit Nair â€“ 30142471, 
Desmond Oâ€™Brien - 30064340 
Grace Kim - 30062591 
Ryan Chrumka â€“ 30144174   
Paige Soâ€™Brien - 30046397  
Pratham Pandey â€“ 30133275 
Rylan Laplante â€“ 30700936  
Mohammad Ibrahim Khan â€“ 30103764 
Dylan Tuttle â€“ 30038835 
Ben Foster â€“ 30094638 
Robert Engel â€“ 30119708 
Diane Doan â€“ 30052326 
Justin Chu â€“ 30162809 
Theodore Lun â€“ 10184905 
Jeremy Thomas â€“ 30149098 
Lucy OuYang â€“ 30140886 
Kanika Kapila â€“ 30153349  
Gaurav Gulati â€“ 30121866 
Jinsu An â€“ 30086178 
Karanjot Bassi â€“ 30094007 
Akib Hasan Aryan- 30141456 
Sean Robertson â€“ 10065949 
Smitkumar Saraiya â€“ 30151834 
Muhtadi Alam- 30150910 
*/ 

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AttendantGUI;
import com.autovend.software.AttendantStationLogic;
import com.autovend.software.DoItYourselfStation;
import com.autovend.software.KeyboardLogin;
import com.autovend.software.SelfCheckoutSystemLogic;
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
public class AttendantGUITest {

	String label;
	
	SelfCheckoutStation station;
	SelfCheckoutSystemLogic system;
	AttendantStationLogic attend;
	AttendantGUI gui;
	KeyboardLogin login;
	DoItYourselfStation customer;
	
	@Before
	public void setUp() {
		
		int[] billDenoms = {5,10,20,50,100};
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}			
		
		station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenoms, coinDenoms, 100, 1);
		system = new SelfCheckoutSystemLogic(station);
		customer = new DoItYourselfStation(system, 1);
		system.setCustomerGUI(customer);
		
		attend = new AttendantStationLogic();
		attend.addUser("admin", "admin");
		attend.addStation(system);
		system.registerAttendant(attend);
		login = new KeyboardLogin(attend);
			
	}
	
	@After
	public void tearDown() {
		gui = null;
		login = null;
		attend = null;
		station = null;
		system = null;
		
	}
	
	/**
	 * Test clicking the buttons on the attendantGUI window
	 */
	@Test
	public void testAttendantGUIButtons() {
		
		gui = new AttendantGUI(attend);
		gui.initialize();
		//gui.approveButton.doClick();
		gui.fillInk.doClick();
		gui.fillPaper.doClick();
		gui.preventUse.doClick();
		gui.printReceipt.doClick();
		gui.removeButton.doClick();
		gui.shutdowonStatupButton.doClick();
		gui.button.doClick();
		gui.logoutButton.doClick();
		
	}	
	

	

	@Test
    public void testUpdateBlockedList() {
		gui = new AttendantGUI(attend);
		gui.initialize();
        Map<Integer, SelfCheckoutSystemLogic> blockedStations = new HashMap<>();
        SelfCheckoutSystemLogic selfCheckoutSystemLogic = new SelfCheckoutSystemLogic(station);
        blockedStations.put(1, selfCheckoutSystemLogic);
        gui.updateBlockedList(blockedStations);
        String expectedText = "Station #1\n";
        String actualText = gui.blockedPane.getText();
        assertEquals(expectedText, actualText);
    }
	
}
