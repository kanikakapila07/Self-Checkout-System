package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.ReusableBag;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;

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

public class Main {
	
	public static final Barcode APPLE_CODE = new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.zero});
	public static final Barcode BANANA_CODE = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one});
	public static final Barcode POTATO_CODE = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero, Numeral.zero});
	public static final Barcode MILK_CODE = new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.one});
	public static final Barcode BREAD_CODE = new Barcode(new Numeral[] {Numeral.one, Numeral.zero, Numeral.zero});
	public static final double BREAD_WEIGHT = 1000f;
	public static final double MILK_WEIGHT = 4000f;
	
	
	public static final double POTATO_WEIGHT = 100f;
	public static final double BANANA_WEIGHT = 200f;
	public static final double APPLE_WEIGHT = 300f;
	public static void main(String[] args) throws Exception {
		/**
		 * In this file i set up a sample station and barcoded product database for prototyping
		 * 
		 * 
		 */
		UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();
	    try{
	        //0-Swing, 1-Mac, 2-?, 3-Windows, 4-Old Windows
	        UIManager.setLookAndFeel(looks[1].getClassName());
	    }catch(Exception e){
	        System.out.println(e.getMessage());
	    }
		


		Currency curr = Currency.getInstance(Locale.CANADA);
		
		int[] billDenoms = {5,10,20,50,100};
		
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}
		
		int scaleMax = 100000;
		int scaleSensitivity = 1;
		
		SelfCheckoutStation station = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		SelfCheckoutSystemLogic system = new SelfCheckoutSystemLogic(station);
		station.coinSlot.enable();
		system.setStation(station);
		SelfCheckoutStation station2 = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		SelfCheckoutSystemLogic system2 = new SelfCheckoutSystemLogic(station2);
		
		// Add an example membership card for manual testing purposes
		MemberDatabase.MEMBERSHIP_DATABASE.add("0123456789");
		
		

		
		BarcodedProduct p1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.zero, Numeral.zero}),
				"Potato", BigDecimal.valueOf(100f), POTATO_WEIGHT);
		
		BarcodedProduct p2 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.zero, Numeral.one}),
				"Banana", BigDecimal.valueOf(200f), BANANA_WEIGHT);
		
		BarcodedProduct p3 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.zero}),
				"Apple", BigDecimal.valueOf(300f), APPLE_WEIGHT);
		
		BarcodedProduct p4 = new BarcodedProduct(MILK_CODE, "Milk", BigDecimal.valueOf(400f), 4000f);
		
		BarcodedProduct p5 = new BarcodedProduct(BREAD_CODE, "Bread", BigDecimal.valueOf(500f), BREAD_WEIGHT);
		
		BarcodedUnit u1 = new BarcodedUnit(p1.getBarcode(), 1f);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p1.getBarcode(), p1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p2.getBarcode(), p2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p3.getBarcode(), p3);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p4.getBarcode(), p4);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p5.getBarcode(), p5);
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBagsController.PURCHASEDBAGBARCODE, "Purchased bag", 
				PurchaseBagsController.PURCHASED_BAG_PRICE, 5f);
		
		
		// Add an example PLU coded item for manual testing purposes
		PriceLookUpCode PLU = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four});
		PLUCodedProduct PLUProduct = new PLUCodedProduct(PLU, "Broccoli", new BigDecimal("299")); 
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLU, PLUProduct);
		
		// Place a 5 gram apple on the scale so that add item by typing doesn't panic that there's nothing on the scale
		// station.scale.add(new BarcodedUnit(p3.getBarcode(), 5));
		
		/*
		system.setCurrentSelectableUnit(u1);
		system.addItemByScanning();
	
		
		system.payWithCash();
		
		Bill fiver = new Bill(5, Currency.getInstance(Locale.CANADA));
		system.getStation().coinSlot.enable();
		
		Coin loonie = new Coin(BigDecimal.valueOf(1f), Currency.getInstance(Locale.CANADA));
		 */
		

		
		//let's add it to the database
		
		System.out.println(system.getBillList());
		System.out.println(system.getAmountDue());
		System.out.println(system.getStation().baggingArea.getCurrentWeight());
		
	//	system.getStation().billInput.accept(fiver);
	//	system.getStation().coinSlot.accept(loonie);
		
		System.out.println(system.getAmountDue());
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p1.getBarcode(), p1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p2.getBarcode(), p2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p3.getBarcode(), p3);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBagsController.PURCHASEDBAGBARCODE, productBag);
		
		
		ArrayList<String[]> products = new ArrayList<>();
		
		for (Barcode barcode : ProductDatabases.BARCODED_PRODUCT_DATABASE.keySet()) {
			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			String[] row = new String[4];
			row[0] = barcode.toString();
			row[1] = product.getDescription();
			row[2] = product.getPrice().toString();
			row[3] = String.valueOf(product.getExpectedWeight());
			products.add(row);
		}
		
		
		for (String[] row : products) {
			for (String s : row) {
				System.out.print(s + " ");
			}
			System.out.println();
		}
		
		System.out.println("SCALE: " + system.getStation().scale.getCurrentWeight());
		
		
		// Instantiate a new customer station GUI window
		DoItYourselfStation customer_station = new DoItYourselfStation(system, 1);
		system.setCustomerGUI(customer_station);
		
		AttendantStationLogic attend = new AttendantStationLogic();
		attend.products.put("POTATO", p1);
		attend.products.put("BANANA", p2);
		attend.products.put("APPLE", p3);
		attend.addUser("RYAN", "1234");
		attend.addStation(system);
		system.registerAttendant(attend);
		attend.addStation(system2);
		system2.registerAttendant(attend);
		KeyboardLogin window = new KeyboardLogin(attend);
		window.frame.setVisible(true);

		/**
		 * 	TODO: SHOULD PROBABLY BE IMPLEMENTED IN CONSTRUCTOR BUT DIDNT DO NOW IN CASE OF TEST
		 * 
		 */

		
		
		// Run the GUI(s)
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				customer_station.show();
				
			}
		});
		
	}
}
