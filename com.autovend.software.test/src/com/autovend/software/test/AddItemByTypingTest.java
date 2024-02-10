package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.DisabledException;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.AddItem;
import com.autovend.software.AddItemByTypingController;


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

public class AddItemByTypingTest extends BaseTestCase {
    AddItemByTypingController useCase;

    BarcodedUnit unit0;
    BarcodedUnit unit1;
    PriceLookUpCodedUnit unit2;
    PriceLookUpCodedUnit unit3;

    BarcodedProduct product0;
    BarcodedProduct product1;
    PLUCodedProduct product2;
    PLUCodedProduct product3;
    
    

    static final Barcode BARCODE_0 = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero});
    static final Barcode BARCODE_1 = new Barcode(new Numeral[] {Numeral.zero, Numeral.one});
    static final PriceLookUpCode PLUCODE_2 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.zero, Numeral.one, Numeral.zero});
    static final PriceLookUpCode PLUCODE_3 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.one, Numeral.one, Numeral.zero});

    @Before
    public void setUp() throws Exception {
        super.initializeStation();
        unit0 = new BarcodedUnit(BARCODE_0, 10f);
        unit1 = new BarcodedUnit(BARCODE_1, 20f);
        unit2 = new PriceLookUpCodedUnit(PLUCODE_2, 30f);
        unit3 = new PriceLookUpCodedUnit(PLUCODE_3, 40f);

        product0 = new BarcodedProduct(BARCODE_0, "Item 0", BigDecimal.valueOf(1f), unit0.getWeight());
        product1 = new BarcodedProduct(BARCODE_1, "Item 1", BigDecimal.valueOf(2f), unit1.getWeight());
        product2 = new PLUCodedProduct(PLUCODE_2, "Item 2", BigDecimal.valueOf(3f));
        product3 = new PLUCodedProduct(PLUCODE_3, "Item 3", BigDecimal.valueOf(4f));
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_0, product0);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_1, product1);
        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_2, product2);
        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_3, product3);

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
        ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_2);
        ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_3);
    }



    @Test
    public void totalWeightTest() throws Exception {
        this.system.setCurrentSelectableUnit(unit0);
        system.addItemByTyping();
        double expectedTotalWeight = unit0.getWeight();
        assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);

  
        this.system.setCurrentSelectableUnit(unit1);
        system.addItemByTyping();
        expectedTotalWeight += unit1.getWeight();
        assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
        
        this.system.setCurrentSelectableUnit(unit2);
        system.addItemByTyping();
        expectedTotalWeight += unit2.getWeight();
        assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
        
        this.system.setCurrentSelectableUnit(unit3);
        system.addItemByTyping();
        expectedTotalWeight += unit3.getWeight();
        assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
    }

    @Test
    public void returnCorrectProductTest() throws Exception {
    	// try for barcoded items
        this.system.setCurrentSelectableUnit(unit0);
        system.addItemByTyping();
        BarcodedProduct expectedProduct = product0;
        assertEquals(expectedProduct, ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedUnit) this.system.getCurrentSelectedUnit()).getBarcode()));
        
        // try for PLUcoded items
        this.system.setCurrentSelectableUnit(unit2);
        system.addItemByTyping();
        PLUCodedProduct expectedProduct2 = product2;
        assertEquals(expectedProduct2, ProductDatabases.PLU_PRODUCT_DATABASE.get(((PriceLookUpCodedUnit) this.system.getCurrentSelectedUnit()).getPLUCode()));
    }

    @Test
    public void reEnabledTest() throws Exception {
        this.system.setCurrentSelectableUnit(unit0);
        system.addItemByTyping();
        assertFalse(system.getStation().mainScanner.isDisabled());
        assertFalse(system.getStation().handheldScanner.isDisabled());
        assertFalse(system.getStation().billInput.isDisabled());
    }

    @Test
    public void productNotFoundTest() throws Exception {
        Barcode invalidBarcode = new Barcode(new Numeral[] {Numeral.two, Numeral.two});
        // in this case the current selected unit should be null, causing a null pointer exception
        assertThrows(NullPointerException.class, () -> system.addItemByTyping());
    }
}
