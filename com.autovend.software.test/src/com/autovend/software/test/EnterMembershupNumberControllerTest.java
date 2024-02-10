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

package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.EnterMembershipNumberController;
import com.autovend.software.MemberDatabase;
import com.autovend.software.SelfCheckoutSystemLogic;

public class EnterMembershupNumberControllerTest {

	SelfCheckoutSystemLogic system;
	SelfCheckoutStation station;
	String number = "1234";
	EnterMembershipNumberController controller;
	
	@Before
	public void setUp() throws Exception {
		
		int[] billDenominations = {5, 10 , 15, 20, 50, 100};
   	 	Currency currency = Currency.getInstance(Locale.CANADA);
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);
        
		system = new SelfCheckoutSystemLogic(station);
		
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void MemberExists() {
		
		MemberDatabase.MEMBERSHIP_DATABASE.add(number);
		
		system.setMembershipNumber(number);
		
		controller = new EnterMembershipNumberController(system, station);
		
		controller.checkMembershipNumber();
		
		assertEquals(true, system.getMembershipActive());
		assertEquals ("1234", system.getMembershipNumber());
		
		
	}
	
	@Test
	public void MemberNoExists() {
		
		
		controller = new EnterMembershipNumberController(system, station);
		
		boolean error = false;
		
		try {
			controller.checkMembershipNumber();
			
		}catch(NullPointerException er){
			
			error = true;
			
		}
		assertTrue(error);
		
	}
	
	@Test
	public void userExist() {
		
		MemberDatabase.MEMBERSHIP_DATABASE.add(number);
		
		controller = new EnterMembershipNumberController(system, station);
		

		assertTrue(controller.userExists(number));
		
		
	}

}
