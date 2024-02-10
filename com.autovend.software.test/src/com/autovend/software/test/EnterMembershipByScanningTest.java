package com.autovend.software.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.MembershipCard;
import com.autovend.Numeral;
import com.autovend.SellableUnit;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.EnterMembershipByScanning;
import com.autovend.software.MemberDatabase;
import com.autovend.software.SelfCheckoutSystemLogic;
import com.autovend.software.EnterMembershipByScanning.InvalidMemberException;
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
public class EnterMembershipByScanningTest {

    private SelfCheckoutSystemLogic system;
    private SelfCheckoutStation station;
    private EnterMembershipByScanning enterMembershipByScanningController;
    MembershipCard memberCard;
    @Before
    public void setUp() {
    	 memberCard = new MembershipCard("Membership", "123456", "Bob", false);
    	 //MemberDatabase.MEMBERSHIP_DATABASE.add("123456"); // bob is in database
    	 int[] billDenominations = {5, 10 , 15, 20, 50, 100};
    	 Currency currency = Currency.getInstance(Locale.CANADA);
         BigDecimal fiveCent = new BigDecimal("0.05");
         BigDecimal tenCent = new BigDecimal("0.10");
         BigDecimal twentyFiveCent = new BigDecimal("0.25");
         BigDecimal loonie = new BigDecimal("1");
         BigDecimal toonie = new BigDecimal("2");
         BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
         station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);
         // Initialize the station's system
         system = new SelfCheckoutSystemLogic(station);
    }
    
 
    @Test
    public void testValidMembershipNumberScan() {  // this will test a valid membership in database
    	MemberDatabase.MEMBERSHIP_DATABASE.add("123456"); // bob is added into the database
    	 BarcodeScanner scanner = new BarcodeScanner();
    	 enterMembershipByScanningController = new EnterMembershipByScanning(system, station);
         scanner.register(enterMembershipByScanningController);
         scanner.enable();
         scanner.scan(memberCard);
         assertEquals(true, system.getMembershipActive());
         assertEquals ("123456", system.getMembershipNumber());
         scanner.disable();
    }

    @Test
    public void testInvalidMembershipNumberScan() {
   	 BarcodeScanner scanner = new BarcodeScanner();
	 enterMembershipByScanningController = new EnterMembershipByScanning(system, station);
     scanner.register(enterMembershipByScanningController);
     scanner.enable();
     MembershipCard memberCardFalse = new MembershipCard("Membership", "5565321", "Alice", false); // not registered in database
     scanner.scan(memberCardFalse);
     assertEquals(false, system.getMembershipActive());
     assertEquals (null, system.getMembershipNumber());
     scanner.disable();
    }
    
    
    /*
    //was just experimenting with how to deal w showing invalidmemberexception, not sure tho
   @Test (expected = EnterMembershipByScanning.InvalidMemberException.class)
   public void testInvalidMembershipNumberWithNullMembershipDatabase() throws IOException {
   	MemberDatabase.MEMBERSHIP_DATABASE = null;
   	 BarcodeScanner scanner = new BarcodeScanner();
   	 enterMembershipByScanningController = new EnterMembershipByScanning(system, station);
        scanner.register(enterMembershipByScanningController);
        scanner.enable();
        MembershipCard memberCardFalse = new MembershipCard("Membership", "5565321", "Alice", false); // not registered in database
        scanner.scan(memberCardFalse);
        scanner.disable();
    
       assertEquals("123456", system.getMembershipNumber());
       // The above line should throw InvalidMemberException
   }
   */

    


}