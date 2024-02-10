package com.autovend.software.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Card;
import com.autovend.Card.CardData;
import com.autovend.Card.CardTapData;
import com.autovend.ChipFailureException;
import com.autovend.CreditCard;
import com.autovend.GiftCard;
import com.autovend.GiftCard.GiftCardInsertData;
import com.autovend.InvalidPINException;
import com.autovend.MagneticStripeFailureException;
import com.autovend.devices.SimulationException;
import com.autovend.devices.TouchScreen;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.CustomerIO;
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
public class PayWithGiftCardTest extends BaseTestCase{

	@Before
	public void setup() throws Exception{
		this.initializeStation();
		SelfCheckoutSystemLogic.interac = new CardIssuer("Interac");
	}
	
	
	@After
	public void teardown() {
		this.deinitializeStation();
	}
	
	
	@Test
	public void testEverything() throws Exception {
		try {
			Currency x = Currency.getInstance(Locale.CANADA);
			GiftCard giftCard = new GiftCard("insert", "123412341234", "1234", x, BigDecimal.TEN);
			GiftCardInsertData data = giftCard.createCardInsertData("1234");
			system.payWithGiftCard();
			system.getStation().cardReader.insert(giftCard, "1234");
			assertFalse(system.getCardPaymentStatus());
			
			teardown();
			setup();
			
			system.payWithGiftCard();
			BufferedImage image = new BufferedImage(12, 10, BufferedImage.TYPE_USHORT_555_RGB);
			system.getStation().cardReader.swipe(giftCard, image);
			assertFalse(system.getCardPaymentStatus());
			
			teardown();
			setup();
			
			system.payWithGiftCard();
			system.getStation().cardReader.tap(giftCard);
			assertFalse(system.getCardPaymentStatus());
		}
		catch (MagneticStripeFailureException e) {
			
		}
	}
	
	@Test
	public void anotherTest() throws Exception{
		
		SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String string_date = "10-10-2025 00:00:00";
		Date date = date_s.parse(string_date);
		
		Calendar expiry = Calendar.getInstance(Locale.CANADA);
		expiry.setTime(date);
		
		Currency x = Currency.getInstance(Locale.CANADA);
		GiftCard giftCard = new GiftCard("insert", "123412341234", "1234", x, BigDecimal.TEN);
		SelfCheckoutSystemLogic.interac.addCardData("123412341234", "Mr.x", expiry, "123", BigDecimal.TEN);
		
		system.setAmountDue(100f);
		system.setCardPaymentAmount(BigDecimal.TEN);
		system.payWithGiftCard();
		system.getStation().cardReader.insert(giftCard, "1234");
		assertFalse(system.getCardPaymentStatus());
		assertEquals(system.getAmountDue(), 100f, 0.000001);
	}
	
	@Test
	public <T> void finaltest() throws Exception {
		try {
		SimpleDateFormat date_s = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		String string_date = "10-10-2025 00:00:00";
		Date date = date_s.parse(string_date);
		
		Calendar expiry = Calendar.getInstance(Locale.CANADA);
		expiry.setTime(date);

		Currency x = Currency.getInstance(Locale.CANADA);
		GiftCard giftCard = new GiftCard("insert", "123412341234", "1234", x, BigDecimal.TEN);
		SelfCheckoutSystemLogic.interac.addCardData("123412341234", "Mr.x", expiry, "123", BigDecimal.TEN);
		
		system.setAmountDue(100f);
		system.setCardPaymentAmount(BigDecimal.TEN);
		system.payWithGiftCard();
		system.getStation().cardReader.insert(giftCard, "1234");
		assertEquals(false, system.getCardPaymentStatus());
		assertEquals(system.getAmountDue(), 100f, 0.000001);
		}
		catch (ChipFailureException e) {
			
		}
		
	}
	
	
	
}
