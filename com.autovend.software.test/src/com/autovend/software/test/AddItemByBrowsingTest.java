package com.autovend.software.test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.AddItem;
import com.autovend.software.AddItemByBrowsing;
import com.autovend.software.AddItemByScanningController;

import java.util.Currency;
import java.util.Locale;

import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;


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
public class AddItemByBrowsingTest extends BaseTestCase {
	AddItemByBrowsing useCase;
	SellableUnit unit0;
	SellableUnit unit1;
	SellableUnit unit2;
	SellableUnit unit3;
	
	BarcodedProduct product0;
	BarcodedProduct product1;
	BarcodedProduct product2;
	BarcodedProduct product3;
	
	static final Barcode BARCODE_0 = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero});
	static final Barcode BARCODE_1 = new Barcode(new Numeral[] {Numeral.zero, Numeral.one});
	static final Barcode BARCODE_2 = new Barcode(new Numeral[] {Numeral.one, Numeral.zero});
	static final Barcode BARCODE_3 = new Barcode(new Numeral[] {Numeral.one, Numeral.one});

	@Before
	public void setUp() throws Exception {
		super.initializeStation();
		unit0 = new BarcodedUnit(BARCODE_0, 1f);
		unit1 = new BarcodedUnit(BARCODE_1, 2f);
		unit2 = new BarcodedUnit(BARCODE_2, 3f);
		unit3 = new BarcodedUnit(BARCODE_3, 4f);
		
		product0 = new BarcodedProduct(BARCODE_0, "Item 0", BigDecimal.valueOf(1f), unit0.getWeight());
		product1 = new BarcodedProduct(BARCODE_1, "Item 1", BigDecimal.valueOf(2f), unit1.getWeight());
		product2 = new BarcodedProduct(BARCODE_2, "Item 2", BigDecimal.valueOf(3f), unit2.getWeight());
		product3 = new BarcodedProduct(BARCODE_3, "Item 3", BigDecimal.valueOf(4f), unit3.getWeight());
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_0, product0);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_1, product1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_2, product2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_3, product3);

	}

	@After
	public void tearDown() throws Exception {
		super.deinitializeStation();
		unit0 = null;
		unit1 = null;
		unit2 = null;
		unit3 = null;
		
		product0 = null;
		product1 = null;
		product2 = null;
		product3 = null;
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_0);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_3);
		
	}
	
	/**
	 * Test to ensure totalWeight variable is incremented properly 
	 * when items are added concurrently
	 * @throws Exception
	 */
	@Test
	public void totalWeightTest() throws Exception {
		
		//testing when 1 item has been added
		this.system.setCurrentSelectableUnit(unit0);
		this.system.setCurrentSelectedProduct(product0);

		system.addItemByBrowsing();
		double expectedTotalWeight = unit0.getWeight();
		assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);

		// testing when 2 items have been added
		this.system.setCurrentSelectableUnit(unit1);
		system.addItemByBrowsing();
		expectedTotalWeight += unit1.getWeight();
		assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
	}
	
	/**
	 * test to see if station is successfully re-enabled after item has been added
	 * @throws Exception
	 */
	@Test
	public void enabledTest() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		this.system.setCurrentSelectedProduct(product0);
		system.addItemByBrowsing();
		
		assertFalse(system.getStation().mainScanner.isDisabled());
		assertFalse(system.getStation().handheldScanner.isDisabled());
		assertFalse(system.getStation().billInput.isDisabled());
	}
}
