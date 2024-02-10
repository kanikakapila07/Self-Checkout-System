package com.autovend.software.test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.Numeral;



import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.autovend.Barcode;

import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
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
public class WeightDiscrepancyTest extends BaseTestCase {
	


	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
	}

	@Test
	public void itemWeightNotAsExpectedNoApproval() throws SimulationException, OverloadException {
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 5f);
		BarcodedProduct product1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.one}), "Test item 1", BigDecimal.valueOf(100f), 10f);
		//ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(new Numeral[] {Numeral.one}), product1);
		
		this.system.weightDiscrepency(10f);
		this.system.setCustomerNoBag(true);
		this.system.setAttendantDisapproval(true);
		this.station.baggingArea.add(unit1);
		System.out.println(this.station.baggingArea.getCurrentWeight());
		
		assertEquals(true, this.system.getUnApprovedDiscrepancy());
		assertEquals(true, this.system.getDiscrepancyActive());
		
		
	}
	
	@Test
	public void checkDiscrepancy_noWeightDiscrepancy_stationEnabled() throws OverloadException {
		//barcoded unit with weight 50f
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 50f);
		this.system.weightDiscrepency(50f); //create a weight discrepancy with expected weight equal to actual weight
		this.station.baggingArea.add(unit1); //add item to bagging area
		
		//barcoded unit with weight 30f
		BarcodedUnit unit2 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 30f);
		this.system.weightDiscrepency(80f); //create a weight discrepancy with expected weight equal to actual weight of unit 2 plus, unit 1 that was previously added
		this.station.baggingArea.add(unit2); //add item to bagging area
		
		
		//check if station is enabled
		assertEquals(false, station.handheldScanner.isDisabled());
		assertEquals(false, station.mainScanner.isDisabled());
		assertEquals(false, station.billInput.isDisabled());
		
		assertEquals(false, system.getDiscrepancyActive());
	}
	
	@Test
	public void checkDiscrepancy_weightDiscrepancyAfterRemovingItem_stationDisabled() throws OverloadException {
		//STEP1: ITEM SCANNED AND PLACED IN BAGGING AREA
		//barcoded unit with weight 34f
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 34f);
		this.system.weightDiscrepency(34f); //create a weight discrepancy with expected weight equal to actual weight
		this.station.baggingArea.add(unit1); //add item to bagging area
		
		//check if station is enabled
		assertEquals(false, station.handheldScanner.isDisabled());
		assertEquals(false, station.mainScanner.isDisabled());
		assertEquals(false, station.billInput.isDisabled());
		
		assertEquals(false, system.getDiscrepancyActive());
		
		//STEP2: SAME ITEM SCANNED ABOVE REMOVED FROM BAGGING AREA
		this.system.weightDiscrepency(34f); //since unit1 was scanned, the expected weight of the scale is the weight of unit1 
		this.station.baggingArea.remove(unit1); //remove unit1 from the bagging area
		
		//check if station is disabled
		assertEquals(false, station.handheldScanner.isDisabled());
		assertEquals(false, station.mainScanner.isDisabled());
		assertEquals(false, station.billInput.isDisabled());
		
		assertEquals(false, system.getDiscrepancyActive());
	}
	
	@Test
	public void weightDiscrepancyOptions_weightDiscrepancyTrue_AttendantApprovalTrue() throws OverloadException {
		//barcoded unit with weight 15f
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 15f);
		
		this.system.weightDiscrepency(10f); //create a weight discrepancy with expected weight not equal to actual weight
		this.system.setCustomerNoBag(false);
		this.system.attendantNoBagResponse = true;
		this.station.baggingArea.add(unit1);
		
		//check if station is enabled
		assertEquals(false, station.handheldScanner.isDisabled());
		assertEquals(false, station.mainScanner.isDisabled());
		assertEquals(false, station.billInput.isDisabled());
		
		assertEquals(false, system.getUnApprovedDiscrepancy());
		assertEquals(false, system.getDiscrepancyActive());
	}
	
	@Test
	public void weightDiscrepancyOptions_weightDiscrepancyTrue_CustomerNoBagTrue() throws OverloadException {
		//barcoded unit with weight 15f
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 15f);
		
		this.system.weightDiscrepency(5f); //create a weight discrepancy with expected weight not equal to actual weight
		this.system.setCustomerNoBag(true);
		this.system.attendantNoBagResponse = true;
		this.station.baggingArea.add(unit1);
		
		//check if station is disabled
		assertEquals(true, station.handheldScanner.isDisabled());
		assertEquals(true, station.mainScanner.isDisabled());
		assertEquals(true, station.billInput.isDisabled());
		
		assertEquals(false, system.getUnApprovedDiscrepancy());
		assertEquals(true,system.getDoNotBagItemActive());
		assertEquals(true, system.getDiscrepancyActive());
	}
	
}
