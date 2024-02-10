package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Numeral;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AttendantStationLogic;
import com.autovend.software.AttendantStationLogic;

import com.autovend.software.CustomerIO;
import com.autovend.software.PrintReceipt;
import com.autovend.software.SelfCheckoutSystemLogic;
/**
Desmond O'Brien: 30064340
Matthew Cusanelli: 30145324
Saadman Rahman: 30153482
Tanvir Ahamed Himel: 30148868
Victor campos: 30106934
Sean Tan: 30094560
Sahaj Malhotra: 30144405 
Caleb Cavilla: 30145972
Muhtadi Alam: 30150910
Omar Tejada: 31052626
Jose Perales: 30143354
Anna Lee: 30137463
 */

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

public class testLowInkPaper {

	SelfCheckoutStation scs;
	Currency c1 = Currency.getInstance(Locale.CANADA);
	Currency c2 = Currency.getInstance(Locale.ITALY);
	int[] billdenominations = {5, 10, 15, 20, 50};
	BigDecimal[] coindenominations = {BigDecimal.ONE};
	
	Barcode barcode = new Barcode(Numeral.eight,Numeral.one,Numeral.two,Numeral.three);
	BarcodedProduct product = new BarcodedProduct(barcode, "Milk", new BigDecimal("20"), 2.5);
	BarcodedUnit unit = new BarcodedUnit(barcode, 2.5);
	
	Barcode barcode2 = new Barcode(Numeral.nine,Numeral.one,Numeral.two,Numeral.three);
	BarcodedProduct product2 = new BarcodedProduct(barcode, "Eggs", new BigDecimal("5"), 2.5);
	BarcodedUnit unit2 = new BarcodedUnit(barcode2, 2.5);
	
	ArrayList<Product> shoppingCart;
	
	Bill bill5 = new Bill(5, c1);
	Bill bill20 = new Bill(20, c1);
	Bill bill50 = new Bill(50, c1);
	Bill bill5DiffCurrency = new Bill(5, c2);

	AttendantStationLogic attendantStub;
	CustomerIO customerStub;
	SelfCheckoutSystemLogic logic;
	
	PrintReceipt printercontroller;

	// Setup: AttendantStub and CustomerStub are created, and two products are added to the product database.
	// SelfCheckoutStation object is created.
	@Before
	public void Setup(){
		scs = new SelfCheckoutStation(c1, billdenominations, coindenominations, 20, 1);

		printercontroller = new PrintReceipt(scs, logic);
		
			
		attendantStub = new AttendantStationLogic(printercontroller);
		customerStub = new CustomerIO();
		logic = new SelfCheckoutSystemLogic(scs);

		printercontroller.registerAttendent(attendantStub);
		

		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
		
		shoppingCart = new ArrayList<Product>();
		shoppingCart.add(product);
		shoppingCart.add(product2);

	}
	
	//tests a valid case with enough ink and paper to print the given receipt
	@Test
	public void testValidReceipt() throws EmptyException, OverloadException {
		scs.printer.addInk(1<<9);
		scs.printer.addPaper(1<<9);
		
		boolean expected = true;
		boolean actual = logic.startPrinting(shoppingCart);
		assertEquals("this test case should pass", expected, actual);
	}
	
	//tests an invalid case where printer has low paper
	@Test
	public void testlowpaper() throws EmptyException, OverloadException {
		scs.printer.addInk(1<<4);
		
		
		boolean expected = false;
		
		// printer starts with a lot of paper by default, so let's print a ton of stuff then test
		// TODO: maybe change when we know how much paper printer should start with? i have no idea as of now
		boolean actual = logic.startPrinting(shoppingCart);
		for (int i = 0; i < 5000; ++i) {
			actual = logic.startPrinting(shoppingCart);
		}
		assertEquals("this test case should pass", expected, actual);
	}
	
	//tests an invalid case where printer has low ink
	@Test
	public void testLowInk() throws EmptyException, OverloadException {
		
	    scs.printer.addPaper(1<<9);
		
	    
		boolean expected = false;
		boolean actual = logic.startPrinting(shoppingCart);
		for (int i = 0; i < 5000; ++i) {
			actual = logic.startPrinting(shoppingCart);
		}
		assertEquals("this test case should pass", expected, actual);
	}
}
