package com.autovend.software;

import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;

import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;

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

public class AddItemByBrowsing extends AddItem {

	public AddItemByBrowsing(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
		}
	
	
	public void addItem(Product product) throws SimulationException, OverloadException {
		SellableUnit copyOfUnit = system.getCurrentSelectedUnit();
		Product copyOfProduct = system.getCurrentSelectedProdcut();
		
	

		//disable station
		addItemStationDisable();
		
		
		//get product/unit info
		double weightInGrams = copyOfUnit.getWeight();
		
		//notify customer

		double expectedWeight = 0;
		System.out.println("Please place your item in the bagging area");
		if (copyOfProduct instanceof BarcodedProduct) {
			expectedWeight = system.getStation().baggingArea.getCurrentWeight() + ((BarcodedProduct) copyOfProduct).getExpectedWeight();
		}
		
		system.setExpectedWeight(expectedWeight);
		
		if (copyOfProduct instanceof BarcodedProduct) {
			this.system.weightDiscrepency(expectedWeight);
		}

		//add item to the baggingArea ElectronicScale
		station.baggingArea.add(copyOfUnit);
		
	
		
		//increment weight total
		this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight() + weightInGrams);
		
		
		system.addBillList(product);

		system.setAmountDue(system.getAmountDue() + product.getPrice().doubleValue());
		addItemStationEnable();
		
	}
	
}
