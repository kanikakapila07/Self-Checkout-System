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
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.DoNotPlaceItemInBaggingAreaController;
import com.autovend.software.SelfCheckoutSystemLogic;



public class DoNotPlaceItemInBaggingAreaTest extends BaseTestCase{
	//private DoNotPlaceItemInBaggingAreaController controller;

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
	
	/**
	 * Setup, partially inherited from BaseTestCase
	 * Creates several test units to be added
	 * 
	 * @throws Exception
	 */
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
	
	/**
	 * Tear down, partially inherited from BaseTestCase
	 * Removes test items
	 * @throws Exception
	 */
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
	 * Test case to ensure that nothing happens when customer does not select
	 * that they would like to not place item in bagging area. Item should be added
	 * normally.
	 * 
	 * @throws Exception, OverloadException, SimulationException
	 */

	@Test
	public void testAddItem() throws Exception, OverloadException, SimulationException {
		this.system.setCurrentSelectableUnit(unit0);

		system.addItemByTyping();
		double weight = system.getBaggingAreaWeight();
		System.out.println(system.getBaggingAreaWeight());
		
		this.system.DoNotPlaceInBaggingArea();

		assertEquals(1f, weight, 0.00001);
	}
	
	/**
	 * Test case to ensure that when customer does select
	 * that they would like to not place item in bagging area
	 * and approval is received from the attendant
	 * the DoNotPlaceItemInBaggingArea test case will work as intended
	 * 
	 * @throws Exception, OverloadException, SimulationException
	 */
	@Test
	public void testDoNotAddItemAttendantApproves() throws Exception, OverloadException, SimulationException {
		//set product and item
		this.system.setCurrentSelectableUnit(unit0);
		this.system.setCurrentSelectedProduct(product0);
		double expectedTotalWeight = system.getBaggingAreaWeight();

		this.system.addItemByTyping();

		//this.system.getCustomerIO().placeInBaggingArea(false);

		this.system.attendantNoBagResponse = true;
		//this.system.getCustomerIO().placeInBaggingArea(false);

		this.system.DoNotPlaceInBaggingArea();

		assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.0001);
    }
	
	/**
	 * Test case to ensure that when customer does select
	 * that they would like to not place item in bagging area
	 * and approval is not received from the attendant
	 * the DoNotPlaceItemInBaggingArea test case will work as intended
	 * where no weight will be subtracted from the bagging area.
	 * 
	 * @throws Exception, OverloadException, SimulationException
	 */
	@Test
	public void testDoNotAddItemAttendantDoesNotApprove() throws Exception, OverloadException, SimulationException {
		//set product and item
		this.system.setCurrentSelectableUnit(unit0);
		this.system.setCurrentSelectedProduct(product0);

		this.system.addItemByTyping();
		double expectedTotalWeight = system.getBaggingAreaWeight();

		this.system.getAttendantStub().setNoBaggingRequestApproved(false);
		this.system.getCustomerIO().placeInBaggingArea(false);
		this.system.DoNotPlaceInBaggingArea();

		assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.0001);
    }	
	
	/**
	 * test to see if station is successfully re-enabled after this function has been called
	 * @throws Exception
	 */
	@Test
	public void enabledTest() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		this.system.setCurrentSelectedProduct(product0);
		system.addItemByTyping();
		this.system.attendantNoBagResponse = true;
		this.system.DoNotPlaceInBaggingArea();
		
		assertFalse(system.getStation().mainScanner.isDisabled());
		assertFalse(system.getStation().handheldScanner.isDisabled());
		assertFalse(system.getStation().billInput.isDisabled());
	}
	
}
