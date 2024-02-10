package com.autovend.software;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.BarcodeScannerObserver;
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

/**
 * This class represents the test case "Do Not Place Item in Bagging Area".
 * It is called by the DoNotPlaceInBaggingAreaActivation() method in SelfCheckoutSystemLogic
 * DoNotPlaceInBaggingAreaActivation() should be called when an individual signals to customer IO
 * that they would not like to place item in bagging area through placeInBaggingArea method in
 * the CustomerIO class.
 */
public class DoNotPlaceItemInBaggingAreaController extends AddItem<BarcodedProduct> implements BarcodeScannerObserver{
	private boolean attendantApprovedNoBagging;

	/**
	 * Creates a DoNotPlaceItemInBaggingAreaController that will reduce expected weight in bagging area
	 * 
	 * @param station
	 *            station is the SelfCheckoutStation currently in use.
	 * @param system
	 * 			  system is the SelfCheckoutSystemLogic currently being used by station.   
	 */
	public DoNotPlaceItemInBaggingAreaController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
	}
	
	/**
	 * Method which reduces the expected weight in the bagging area by the expected weight of an item.
	 * 
	 * @param product
	 *            product is the product whose weight will be subtracted from the total expected weight
	 *            in the bagging area
	 * @throws SimulationException
	 * 			  If is the item/product is not found on the scale.
	 * @throws OverloadException
	 * 			  If the current weight has overloaded the weight limit of the baggingArea scale.
	 */
	public void signalToNotPlaceItemInBaggingArea(SellableUnit currentSelectedUnit) throws SimulationException, OverloadException{
		SellableUnit copyOfUnit = system.getCurrentSelectedUnit();
		Product copyOfProduct = system.getCurrentSelectedProdcut();
		//disable station
		addItemStationDisable();
		
		//get product/unit info
		double weightInGrams = copyOfUnit.getWeight();
				
		//get attendant approval
		//if attendant I/O approves
		if (system.attendantNoBagResponse == true) {

			//Set expected weight
			double expectedWeight = 0;
			expectedWeight = system.getStation().baggingArea.getCurrentWeight() + ((BarcodedProduct) copyOfProduct).getExpectedWeight();
			system.setExpectedWeight(this.system.getBaggingAreaWeight() - expectedWeight);
			
			//Set weight discrepancy
			this.system.weightDiscrepency(this.station.baggingArea.getCurrentWeight() - weightInGrams);		
					
			station.baggingArea.remove(copyOfUnit);
			
			//decrement weight total
			this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight() - weightInGrams);
			
			//reset to default
			this.system.customerNoBag = false;
			//unblock station
			addItemStationEnable();
		}
			
		else {
			//reset to default
			this.system.customerNoBag = false;

			//unblock station
			addItemStationEnable();
		}	
	}
}

