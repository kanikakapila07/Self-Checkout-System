package com.autovend.software;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
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
public class AddItemByScanningController extends AddItem<BarcodedProduct> implements BarcodeScannerObserver {

	public AddItemByScanningController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
		this.station.mainScanner.enable();
	
	}
	
	
	public BarcodedProduct add(SellableUnit currentSelectedUnit) throws Exception {
		

		
		if(station.mainScanner.scan((BarcodedUnit) currentSelectedUnit)) {
			BarcodedUnit copyOfUnit = (BarcodedUnit) currentSelectedUnit;
			BarcodedProduct barcodedProduct = getProductFromBarcode(this.system.getCurrentBarcode());

			//disable station
			addItemStationDisable();
			
			
	
			//notify customer

			double expectedWeight = 0;
			// NOTE ONLY IMPLEMENDTED FOR BARCODED ITEMS ATM
			expectedWeight = system.getStation().baggingArea.getCurrentWeight() + ((BarcodedProduct) barcodedProduct).getExpectedWeight();
	
			
			system.weightDiscrepency(expectedWeight);
			system.setExpectedWeight(expectedWeight);
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
			
			//increment weight total
			this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight() + copyOfUnit.getWeight());
			
			
			system.addBillList(barcodedProduct);

			System.out.println("ITEM ADDED");
			
			system.setAmountDue(system.getAmountDue() + barcodedProduct.getPrice().doubleValue());
			//if (copyOfProduct instanceof PLUCodedProduct) {
			//	this.system.weightDiscrepency(this.station.baggingArea.getCurrentWeight() + ((PLUCodedProduct) copyOfProduct).getPrice());
			//}

			//add item to the baggingArea ElectronicScale
			
			addItemStationEnable();
			
		}
		
		return null;
		
		
	
		
	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		this.system.setCurrentBarcode(barcode);
	}
	
	















}


