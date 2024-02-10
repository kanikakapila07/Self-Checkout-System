package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.SearchItems;
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
public class SearchItemsTest {

	@Test
	public void testSearch() {
		   SellableUnit barcodeUnit1;
		    SellableUnit bardcodeUnit2;
		    SellableUnit barcodeUnit3;
		    
		    PriceLookUpCodedUnit pluUnit1;
			PriceLookUpCodedUnit pluUnit2;
	
		    BarcodedProduct barcodeProduct1;
		    BarcodedProduct barcodeProduct2;
		    BarcodedProduct barcodeProduct3;
		    
		    PLUCodedProduct pluProduct1;
			PLUCodedProduct pluProduct2;
		
		    final PriceLookUpCode PLUCODE_1 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four}); 
			final PriceLookUpCode PLUCODE_2 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
			
			pluUnit1 = new PriceLookUpCodedUnit(PLUCODE_1, 1f);
			pluUnit2 = new PriceLookUpCodedUnit(PLUCODE_2, 2f);
		    
			pluProduct1 = new PLUCodedProduct(PLUCODE_1, "beans", BigDecimal.valueOf(1f));
			pluProduct2 = new PLUCodedProduct(PLUCODE_2, "pear", BigDecimal.valueOf(2f));
			
			ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_1, pluProduct1);
			
			
		    final Barcode BARCODE_1 = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero});
		    final Barcode BARCODE_2 = new Barcode(new Numeral[] {Numeral.zero, Numeral.one});
		    final Barcode BARCODE_3 = new Barcode(new Numeral[] {Numeral.zero, Numeral.two});
		   
		    
		    barcodeUnit1 = new BarcodedUnit(BARCODE_1, 1f);
	        bardcodeUnit2 = new BarcodedUnit(BARCODE_2, 2f);
	        barcodeUnit3 = new BarcodedUnit(BARCODE_3, 3f);
	      

	        barcodeProduct1 = new BarcodedProduct(BARCODE_1, "apple", BigDecimal.valueOf(1f), barcodeUnit1.getWeight());
	        barcodeProduct2 = new BarcodedProduct(BARCODE_2, "banana", BigDecimal.valueOf(2f), bardcodeUnit2.getWeight());
	        barcodeProduct3 = new BarcodedProduct(BARCODE_3, "orange", BigDecimal.valueOf(3f), barcodeUnit3.getWeight());
	    

	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_1, barcodeProduct1); // apple added to database
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_2, barcodeProduct2); // banana added to database
	      

		
		// Test a search for "apple", which should append the apple product to the returned list
		ArrayList<Product> results = new SearchItems().search("apple");
		assertEquals(0, results.indexOf(barcodeProduct1));
		assertEquals(true, results.contains(barcodeProduct1));
		
		
		
		// Test a search for "banana" in barcode database (appending to apple)
		ArrayList<Product> moreResults = new SearchItems().search("banana");
		results.addAll(moreResults);
		assertEquals(2, results.size());
		assertEquals(true, results.contains(barcodeProduct2));  // product 2 is the banana
		
		
		
		// Test a search for "orange" (which is not in the barcode database)
		ArrayList<Product> badResult = new SearchItems().search("orange");
		assertEquals(0, badResult.size());
		assertEquals(false, results.contains(barcodeProduct3));  
		
		// Now we'll do some PLU testing:
		// Beans are in the PLU database, so should be detected
		ArrayList<Product> pluResults = new SearchItems().search("beans");
		assertEquals(0, pluResults.indexOf(pluProduct1));
		assertEquals(1, pluResults.size());
		assertEquals(true, pluResults.contains(pluProduct1));
		
		
		ArrayList<Product> badPluResult = new SearchItems().search("pear"); // pear is not in the PLU database
		assertEquals(0, badPluResult.size());
		assertEquals(false, badPluResult.contains(pluProduct2));  
		
		
	}

}

