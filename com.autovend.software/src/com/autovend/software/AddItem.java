package com.autovend.software;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.BarcodedUnit;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;


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
public class AddItem<T extends Product> implements BarcodeScannerObserver{
	//private BarcodedUnit barcodedUnit;
	//private BarcodedProduct barcodedProduct;
	protected double totalWeight = 0;
	protected BigDecimal totalPrice = BigDecimal.ZERO;
	//protected ArrayList<T> products;
	protected SellableUnit unit;
	protected SelfCheckoutStation station;
	protected SelfCheckoutSystemLogic system;
	
	protected boolean placeInbaggingArea;
	protected boolean attendantApprovedNoBagging;

	/**
	 * 
	 * 
	 * 
	 * @param staion - the station being used by the system
	 * @param currentUnit - Unit to be added (PLU or barcoded)
	 */
	public AddItem(SelfCheckoutStation station, SelfCheckoutSystemLogic system){
		this.station = station;
		this.system = system;
	}

	public static BarcodedProduct getProductFromBarcode(Barcode barcode) {
		return ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
	}
	

	public PLUCodedProduct getProductFromPLU(PriceLookUpCode plu) {
		return ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
	}

	protected void addItemStationDisable() {
		station.mainScanner.disable();
		station.handheldScanner.disable();
		station.billInput.disable();
	}
	
	protected void addItemStationEnable() {
		station.mainScanner.enable();
		station.handheldScanner.enable();
		station.billInput.enable();
	}
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	// Reacts to scanner and adds item which is detected
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) throws SimulationException{

	}

}
