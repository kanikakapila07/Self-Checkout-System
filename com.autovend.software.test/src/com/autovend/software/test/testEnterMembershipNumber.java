package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.AttendantStationLogic;
import com.autovend.software.CustomerIO;
import com.autovend.software.MemberDatabase;
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

public class testEnterMembershipNumber {

	SelfCheckoutStation scs;
	Currency c1 = Currency.getInstance(Locale.CANADA);
	Currency c2 = Currency.getInstance(Locale.ITALY);
	int[] billdenominations = {5, 10, 15, 20, 50};
//	BigDecimal[] coindenominations = {new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("2")};
	BigDecimal[] coindenominations = {BigDecimal.ONE};
	
	AttendantStationLogic attendantStub;
	CustomerIO customerStub;
	SelfCheckoutSystemLogic logic;
	
	ArrayList<String> membershipTrys;
	
	
		@Before
	public void Setup(){
		scs = new SelfCheckoutStation(c1, billdenominations, coindenominations, 20, 1);

		
		customerStub = new CustomerIO();
		logic = new SelfCheckoutSystemLogic(scs);
		
		MemberDatabase.MEMBERSHIP_DATABASE.add("123456");
		MemberDatabase.MEMBERSHIP_DATABASE.add("234567");
		MemberDatabase.MEMBERSHIP_DATABASE.add("345678");
		MemberDatabase.MEMBERSHIP_DATABASE.add("456789");
		
		membershipTrys = new ArrayList<String>();
	}
	
		//tests a valid membership attempt
	@Test
	public void testValidMembershipOneTry() {
		String number = "123456";
		boolean expected = true;
		boolean actual = logic.takeMembership(number);
		assertEquals(expected, actual);
	}
	
	//tests a valid membership attempt after multiple incorrect attempts
	@Test
	public void testValidMembershipMultipleTries() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		membershipTrys.add("123456");
		boolean expected = true;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}

		assertEquals(expected, actual);
	}

	

	//tests an invalid attempt in one try
	@Test
	public void testInvalidMembershipOneTry() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		boolean expected = false;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}

		assertEquals(expected, actual);
	}
	
	//tests an invalid attempt for multiple tries, then cancel
	@Test
	public void testInvalidMembershipMultipleTries() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		membershipTrys.add("765432");
		membershipTrys.add("876543");
		boolean expected = false;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}
		assertEquals(expected, actual);
	}

}

