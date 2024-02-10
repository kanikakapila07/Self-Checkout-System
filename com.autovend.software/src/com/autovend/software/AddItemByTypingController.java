package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
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

public class AddItemByTypingController extends AddItem<BarcodedProduct> {

    public AddItemByTypingController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
    }

    public void add(SellableUnit currentSelectedUnit) throws DisabledException, OverloadException {
        if (currentSelectedUnit instanceof BarcodedUnit) {
        	BarcodedUnit copyOfUnit = (BarcodedUnit) currentSelectedUnit;
            BarcodedProduct barcodedProduct = getProductFromBarcode(copyOfUnit.getBarcode());

            // disable station
            addItemStationDisable();
            
            // notify customer
            System.out.println("Please place your item in the bagging area");
            
            // get product/unit info
    		double expectedWeight = system.getStation().baggingArea.getCurrentWeight() + barcodedProduct.getExpectedWeight();
    		
    		system.setExpectedWeight(expectedWeight);
    		this.system.weightDiscrepency(expectedWeight);
            this.system.setBaggingAreaWeight(expectedWeight);
            this.system.getStation().baggingArea.add(copyOfUnit);

    		if (!system.getDiscrepancyActive()) {
				// add price to amount due
				this.system.setAmountDue(this.system.getAmountDue() + barcodedProduct.getPrice().doubleValue());
				
				// add item to bill
				this.system.addBillList(barcodedProduct);
			}

            addItemStationEnable();
        } else {
        	PriceLookUpCodedUnit copyOfUnit = (PriceLookUpCodedUnit) currentSelectedUnit;
            PLUCodedProduct PLUProduct = getProductFromPLUCode(copyOfUnit.getPLUCode());

            // disable station
            addItemStationDisable();

            // notify customer
            System.out.println("Please place your item in the bagging area");
            
            // get product/unit info
    		double expectedWeight = system.getStation().baggingArea.getCurrentWeight() + copyOfUnit.getWeight();
    		
    		system.setExpectedWeight(expectedWeight);
    		this.system.weightDiscrepency(expectedWeight);
            this.system.setBaggingAreaWeight(expectedWeight);
            this.system.getStation().baggingArea.add(copyOfUnit);

    		if (!system.getDiscrepancyActive()) {
				// add price to amount due
				this.system.setAmountDue(this.system.getAmountDue() + PLUProduct.getPrice().doubleValue());
				
				// add item to bill
				this.system.addBillList(PLUProduct);
			}

            addItemStationEnable();
        }


    }



//    private BarcodedProduct getProductFromBarcode(Barcode barcode) {
//        return (BarcodedProduct) ProductDatabases.PRODUCT_DATABASE.findProduct(barcode);
//    }
    
    private PLUCodedProduct getProductFromPLUCode(PriceLookUpCode PLUCode) {
    	return (PLUCodedProduct) ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);
    }
}
