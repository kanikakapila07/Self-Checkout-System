package com.autovend.software;

import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinDispenserObserver;

public class AdjustCoinsController implements CoinDispenserObserver {
	private SelfCheckoutSystemLogic system;
	private SelfCheckoutStation station;
	private AttendantStationLogic as;
	
	
	public AdjustCoinsController(SelfCheckoutSystemLogic system) {
		
		this.system = system;
		station = system.getStation();
		as = system.getAttendantStub();
		
		//Register all (denomination) dispensers with observer
		for(CoinDispenser dispenser : station.coinDispensers.values()) {
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
	public void reactToCoinsFullEvent(CoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
		if (dispenser.size() < 20) {
			as.addLowCoinDispenser(dispenser, coin);
			as.coinsRequired = true;
		}
	}


	@Override
	public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {
		if (dispenser.size() >= 20) {
			as.removeLowCoinDispenser(dispenser, coins[0]);
		}
	}


	@Override
	public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {
		try {
			if (dispenser.size() < 20) {
				as.addLowCoinDispenser(dispenser, coins[0]);
				as.coinsRequired = true;
			}
		} catch (Exception e) {}
	}
}
