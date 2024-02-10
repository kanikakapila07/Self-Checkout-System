
package com.autovend.software;
import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;

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
 * 
 * Class to implement the "Weight Discrepancy" use case

 *
 */

public class WeightDiscrepancyController implements ElectronicScaleObserver {
	
	// declare fields
	private SelfCheckoutStation station;
	private SelfCheckoutSystemLogic system;
	
	/**
	 * Constructor
	 * 
	 * 
	 * @param station - SelfCheckoutStation used by system
	 * @param system - System that controls the selfcheckout station's logic
	 */
	public WeightDiscrepancyController (SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		
		this.station = station;
		this.system = system;
		
		//disable the selfcheckout system since a weight discrepancy is detected
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		this.system.checkDiscrepancy(weightInGrams); //every time there is a weight change, check for a weight discrepancy
		
		
	}


	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}
	
}
