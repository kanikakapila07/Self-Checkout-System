package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.MembershipCard;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;

import java.io.IOException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

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

public class EnterMembershipByScanning extends EnterMembershipNumberController implements AbstractDeviceObserver, BarcodeScannerObserver {

    /**
     * New Exception for if mem_DB is null when trying to enter member number
     */
    public class InvalidMemberException extends IOException {
        private static final long serialVersionUID = 1610489564122006410L;
    }

    public EnterMembershipByScanning(SelfCheckoutSystemLogic system, SelfCheckoutStation station) {
    	super(system, station);
    	this.system.getStation().mainScanner.register(this);
    	this.system.getStation().handheldScanner.register(this);
    }
    
    @Override
    public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
    	// system.setMembershipNumber(barcode.toString());
    	// super.checkMembershipNumber();
    	
    	if (super.userExists(barcode.toString())) {
			MembershipCard someCard = new MembershipCard("membership", barcode.toString(), "User", true);
			this.system.setCardSwipe(someCard, barcode.toString());
			
			system.setMembershipActive(true, barcode.toString());
		}
    }


	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}
}
