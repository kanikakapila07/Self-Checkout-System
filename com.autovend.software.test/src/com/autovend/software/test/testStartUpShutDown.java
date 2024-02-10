package com.autovend.software.test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class testStartUpShutDown {
    SelfCheckoutStation station;
    private Currency currency;

    private SelfCheckoutSystemLogic system;



    private CustomerIO customer;

    CardIssuer bank;

    private BarcodedUnit barcodedUnit;

    private BarcodedProduct barcodedProduct;

    Numeral[] numerals = new Numeral[]{Numeral.three, Numeral.one, Numeral.two};

    Barcode barcode = new Barcode(numerals);

   



    AttendantStationLogic testCase = new AttendantStationLogic();

    @Before
    public void setup() {
        // Set the currency to be used
        currency = Currency.getInstance(Locale.CANADA);

        bank = new CardIssuer("RBC");

        // Initialize a self-checkout station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

        // Initialize the station's system
        system = new SelfCheckoutSystemLogic(station);


        // set a product's description
        String description = "Description for item";

        // set a product's weight
        double weight = 13.0;

        // set a product's price
        BigDecimal price = new BigDecimal("5.50");

        // initialize a barcoded unit
        barcodedUnit = new BarcodedUnit(barcode, weight);

        // initialize a barcoded product
        barcodedProduct = new BarcodedProduct(barcode, description, price, weight);

        // add product to product's databases for accessing
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodedProduct);


        // TODO: register observers

        station.mainScanner.enable();
        station.handheldScanner.enable();

        customer = new CustomerIO();

    }

    @Test
    public void testStartUpStation() {

        //login here
        testCase.loggedInAttendant = "Attendant1";
        testCase.startUp(station);
        assertEquals(true, testCase.stationOn);

        //logout
    }

    @Test
    public void testShutDownStation() {

        //login here
        testCase.loggedInAttendant = null;
        testCase = new AttendantStationLogic();
        testCase.shutDown(station);
        assertFalse(testCase.stationOn);

        //logout
    }



}
