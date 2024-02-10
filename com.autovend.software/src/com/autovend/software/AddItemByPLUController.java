package com.autovend.software;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

import java.math.BigDecimal;
import java.math.MathContext;


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
public class AddItemByPLUController extends AddItem {
	
	public AddItemByPLUController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
	}
	
	

	public void add(SellableUnit currentSelectedItem) throws Exception {
		  PriceLookUpCodedUnit copyOfUnit = (PriceLookUpCodedUnit) currentSelectedItem;
		  PLUCodedProduct pluProduct = getProductFromPLU(copyOfUnit.getPLUCode());

		// check if PLU code is in database (have to change from copyOfUnit.PLUCode() to the users raw
		  // input then convert that to a PLU code)
      if (ProductDatabases.PLU_PRODUCT_DATABASE.containsKey(copyOfUnit.getPLUCode())) {
          // disable station
          addItemStationDisable();

          // get product/unit info
          double weightInGrams = copyOfUnit.getWeight();
          BigDecimal pricePerKg = pluProduct.getPrice();
          
          

          // notify customer
          System.out.println("Please place your item in the bagging area");

          system.weightDiscrepency(this.station.baggingArea.getCurrentWeight() + weightInGrams);
          
          // add item to the baggingArea ElectronicScale
          station.baggingArea.add(copyOfUnit);
          
          this.system.addBillList(pluProduct);


          // increment weight total
          this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight()+copyOfUnit.getWeight());
          
         
          // mathcontext object to specify decimal precision
          MathContext mc = new MathContext(2);

          // calculate price based on weight
          BigDecimal price = pricePerKg.multiply(BigDecimal.valueOf(weightInGrams), mc);
          
         

          // increment price total
          this.totalPrice = price.plus();
          this.system.setAmountDue(this.system.getAmountDue() + totalPrice.doubleValue());
          
          //System.out.println(this.totalPrice);
          
          // *maybe need different event for adding PLU unit since no barcode*
          // react to barcode scanned event (adds PLUCodedUnit to BillList)
          // reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getPLUCode());
          
          addItemStationEnable();
          

      }
      

  }

}
