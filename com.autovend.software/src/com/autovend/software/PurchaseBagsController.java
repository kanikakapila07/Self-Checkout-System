package com.autovend.software;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.ReusableBag;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
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
 * Class that implements the "Purchase Bags" use case
 * 
 * 
 * @author desmo
 *
 */
public class PurchaseBagsController {
// some public constants that define the properties of purchased bags, as well as the default bag dispenser capacity
public static final int DEFAULT_NUMBER_OF_BAGS = 100;
public static final double BAG_WEIGHT = 5f;
public static final Barcode PURCHASEDBAGBARCODE = new Barcode(new Numeral[] {Numeral.six,Numeral.nine});
public static final BigDecimal PURCHASED_BAG_PRICE = BigDecimal.valueOf(0.5);

// user's desired number of bags

private int desiredNumberOfBags;
// system's self checkout station

private SelfCheckoutStation station;
 
// rolling count of current bags available in dispenser
public int currentBagsAvailable;

private SelfCheckoutSystemLogic system;

	


	/**
	 * Constructor
	 * 
	 * @param station - station in use
	 * @param numOfBags - customer's desired number of purchased bags
	 * @throws OverloadException - in case of a baggiage area overload
	 */
	public PurchaseBagsController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		this.station = station;
		this.system = system;
		
	}

	
	/**
	 * 	Method to fulfill "Purchase Bags" use case
	 * 
	 * @return - ArrayList of bags to add to bill. Calling code (System) can
	 * iterate over bags, adding each as an item to bill. Bags are added to the bagging
	 * area during this method call, so do not need to add bags again
	 * @throws OverloadException  - If weight is overloaded in weight discrepancy
	 * @throws SimulationException  - If attendand does not approve weight discrepancy
	 */
	public void addBagsToBill() throws OverloadException, EmptyException {
		
		if (this.station.baggingArea.isDisabled()) {
			throw new DisabledException();
		}
		// instantiate an ArrayList of bags to be returned by method
		
		// dispense desired number of bags
		for (int i = 0; i < this.system.getNumberBagsPurchased(); ++i) {
			
			// signal to bag dispenser number of bags to dispense
			ReusableBag bag = system.getStation().bagDispenser.dispense();
			
			
			BarcodedProduct bagProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(PURCHASEDBAGBARCODE);
			// add bag to weight, check weight discrepancy by registering weightDiscrepancy listener
			double expectedWeight = this.system.getStation().baggingArea.getCurrentWeight() + bagProduct.getExpectedWeight();
			system.setExpectedWeight(expectedWeight);
			this.system.weightDiscrepency(expectedWeight);
			this.station.baggingArea.add(bag);
			
			if (!system.getDiscrepancyActive()) {
				// add price of bag to ammount due
				this.system.setAmountDue(this.system.getAmountDue() + bagProduct.getPrice().doubleValue());
				
				// add item to bill
				this.system.addBillList(bagProduct);
				
			}

			

		}
	}



	


	




}
