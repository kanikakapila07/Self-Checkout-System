package com.autovend.software;

import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;


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

public class AdjustBanknotesController implements BillDispenserObserver {
	private SelfCheckoutSystemLogic system;
	private SelfCheckoutStation station;
	public AttendantStationLogic as;
	
	
	public AdjustBanknotesController(SelfCheckoutSystemLogic system) {
		
		this.system = system;
		this.as = this.system.attendant;
		
		//Register all dispensers (denominations) with observer
		for(BillDispenser dispenser : system.getStation().billDispensers.values()) {
			dispenser.register(this);
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

	@Override
	public void reactToBillsFullEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsEmptyEvent(BillDispenser dispenser) {
		
	}

	@Override
	public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
		if (dispenser.size() < 20) {
			as.addLowBillDispenser(dispenser, bill);
			as.billsRequired = true;
		}
	}

	@Override
	public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {
		if (dispenser.size() >= 20) {
			as.removeLowBillDispenser(dispenser, bills[0]);
		}
	}

	@Override
	public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {
		try {
			if (dispenser.size() < 20) {
			this.as.addLowBillDispenser(dispenser, bills[0]);
			this.as.billsRequired = true;
			}
		} catch (Exception e) {}
	}
}

