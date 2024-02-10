package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Card.CardData;
import com.autovend.Card.CardTapData;
import com.autovend.ChipFailureException;
import com.autovend.CreditCard;
import com.autovend.MagneticStripeFailureException;
import com.autovend.TapFailureException;
import com.autovend.external.CardIssuer;
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
public class PayWithCreditTest extends BaseTestCase {



	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		SelfCheckoutSystemLogic.interac = new CardIssuer("Interac");
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
	}

	@Test
	public void testCardNotRegistered() throws Exception {
		try {
		CreditCard badCard = new CreditCard("test", "test", "test", "test", "test", true, true);
		
		system.payWithCredit();
		system.getStation().cardReader.tap(badCard);
		
		assertEquals(false, system.getCardPaymentStatus());
		
		tearDown();
		setUp();
		
		system.payWithCredit();
		system.getStation().cardReader.swipe(badCard, null);
		
		assertEquals(false, system.getCardPaymentStatus());
		
		
		tearDown();
		setUp();
		
		system.payWithCredit();
		system.getStation().cardReader.tap(badCard);
		
		assertEquals(false, system.getCardPaymentStatus());
		}
		catch (MagneticStripeFailureException e) {
			
		}
		
	
	}
	
	//The method addCardData(String, String, Calendar, String, BigDecimal)
	@Test
	public void testCardDeductingRightAmountTap() throws Exception {
		
		SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String string_date = "10-10-2025 00:00:00";
		Date date = date_s.parse(string_date);
		
		Calendar expiry = Calendar.getInstance(Locale.CANADA);
		expiry.setTime(date);
		
		
		CreditCard goodCard = new CreditCard("test", "1000", "test", "test", "100", true, true);
		SelfCheckoutSystemLogic.interac.addCardData("1000", "test", expiry, "100", BigDecimal.valueOf(1000f));
		
		
		system.setAmountDue(100f);
		system.setCardPaymentAmount(BigDecimal.valueOf(100f));
		system.payWithCredit();
		try {
			system.getStation().cardReader.tap(goodCard);
			assertEquals(true, system.getCardPaymentStatus());
			assertEquals(system.getAmountDue(), 0f, 0.000001);
		}
		catch (ChipFailureException | TapFailureException e) {
			
		}
	
	}
	
	@Test
	public void cannotPayMoreThanAllowed() throws Exception {
		
		SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String string_date = "10-10-2025 00:00:00";
		Date date = date_s.parse(string_date);
		
		Calendar expiry = Calendar.getInstance(Locale.CANADA);
		expiry.setTime(date);
		
		
		CreditCard goodCard = new CreditCard("test", "1000", "test", "test", "100", true, true);
		SelfCheckoutSystemLogic.interac.addCardData("1000", "test", expiry, "100", BigDecimal.valueOf(100f));
		
		
		system.setAmountDue(1000f);
		system.setCardPaymentAmount(BigDecimal.valueOf(1000f));
		system.payWithCredit();
		system.getStation().cardReader.tap(goodCard);
		assertEquals(false, system.getCardPaymentStatus());
		assertEquals(system.getAmountDue(), 1000f, 0.000001);
	
	}
	
	@Test
	public void cannotPayIfBlocked() throws Exception {
		
		SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String string_date = "10-10-2025 00:00:00";
		Date date = date_s.parse(string_date);
		
		Calendar expiry = Calendar.getInstance(Locale.CANADA);
		expiry.setTime(date);
		
		
		CreditCard goodCard = new CreditCard("test", "1000", "test", "test", "100", true, true);
		SelfCheckoutSystemLogic.interac.addCardData("1000", "test", expiry, "100", BigDecimal.valueOf(10000f));
		SelfCheckoutSystemLogic.interac.block("1000");
		
		
		system.setAmountDue(1000f);
		system.setCardPaymentAmount(BigDecimal.valueOf(1000f));
		system.payWithCredit();
		system.getStation().cardReader.tap(goodCard);
		assertEquals(false, system.getCardPaymentStatus());
		assertEquals(system.getAmountDue(), 1000f, 0.000001);
	
	}
	

	
	
	

}
