package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.*;

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


public class AttendantStationLogic implements PrintReceiptObserver {
	public ArrayList<SelfCheckoutSystemLogic> stations = new ArrayList<SelfCheckoutSystemLogic>();
	public Map<Integer, SelfCheckoutSystemLogic> blockedStations = new HashMap<Integer, SelfCheckoutSystemLogic>();
	public Map<Integer, SelfCheckoutSystemLogic> weightDiscrepancy = new HashMap<Integer, SelfCheckoutSystemLogic>();
	private Map<String, String> passwords = new HashMap<String, String>();
	public Map<String, Product> products = new HashMap<String, Product>();
	private AttendantGUI GUI;
	
	PrintReceipt observer;
	ReceiptPrinter printer;
	List<BarcodedProduct> duplicateBill;
	private boolean maintainenceRequired;
	public String loggedInAttendant = null;
	public boolean stationOn = false;
	SelfCheckoutStation station;

	public boolean billsRequired;
	private Map<Integer, BillDispenser> lowBillDispensers = new HashMap<Integer, BillDispenser>();
	public boolean coinsRequired;
	private Map<BigDecimal, CoinDispenser> lowCoinDispensers = new HashMap<BigDecimal, CoinDispenser>();
	private SelfCheckoutSystemLogic system;

	private boolean discrepancyApproved;
	private boolean noBaggingRequestApproved;
	
	public AttendantStationLogic() {
	}
	
	public AttendantStationLogic(PrintReceipt ob) {
		PrintReceipt observer = ob;
		printer = observer.getPrinter();

	}

	public AttendantStationLogic(SelfCheckoutSystemLogic system) {
		this.system = system;
		station = system.getStation();
		for(int key : station.billDispensers.keySet()) {
			if (station.billDispensers.get(key).size() < 20) {
				lowBillDispensers.put(key, station.billDispensers.get(key));
				billsRequired = true;
			}
		}
		for(BigDecimal key : station.coinDispensers.keySet()) {
			if (station.coinDispensers.get(key).size() < 20) {
				lowCoinDispensers.put(key, station.coinDispensers.get(key));
				coinsRequired = true;
			}
		}
	}

	
	
	// A method to add ink to every station currently blocked
	
	public void addInkToBlockedStations(int id, int quantity) {
		SelfCheckoutSystemLogic station = blockedStations.get(id);
		try {
			station.getStation().printer.addInk(quantity);
			station.unBlock();
			blockedStations.remove(id);
		}
		catch(OverloadException e) {
			System.out.println(e);
			System.out.println("Ink add failed.");
		}
		catch(SimulationException e) {
			System.out.println(e);
			System.out.println("Ink add failed.");
		}	
	}
	

	// A method to add paper to every station currently blocked
	
	public void addPaperToBlockedStations(int id, int quantity) {
		SelfCheckoutSystemLogic station = blockedStations.get(id);
		try {
			station.getStation().printer.addPaper(quantity);
			station.unBlock();
			blockedStations.remove(id);
			blockedStations.get(station);
		}
		catch(OverloadException e) {
			System.out.println(e);
			System.out.println("Paper add failed.");
		}
		catch(SimulationException e) {
			System.out.println(e);
			System.out.println("Paper add failed.");
		}
		
	}

	// Requests help for blocked stations to receive assistance
	
	public void requestHelp(SelfCheckoutSystemLogic checkout) {
		blockedStations.put(checkout.id, checkout);
		int id = checkout.id;
		System.out.println("Station "+ id +  " needs assistance!");
	}
	
	
	// Confirms maintenance is required
		
	public void requiresMaintainence() {
		maintainenceRequired = true;
	}
	
	// Indicates whether or not maintenance is required
	
	public boolean isMaintainenceRequired() {
		return maintainenceRequired;
	}

	public void setDiscrepancyApproved(boolean bool) {
		this.discrepancyApproved = bool;
	}
	
	public boolean getDiscrepancyApproved() {
		return this.discrepancyApproved;
	}
	

	public void addLowBillDispenser(BillDispenser dispenser, Bill bill) {
		lowBillDispensers.put(bill.getValue(), dispenser);
		billsRequired = true;
	}
	
	public void removeLowBillDispenser(BillDispenser dispenser, Bill bill) {
		lowBillDispensers.remove(bill.getValue());
		if (lowBillDispensers.isEmpty() == true) {
			billsRequired = false;
		}
	}
	
	public void addLowCoinDispenser(CoinDispenser dispenser, Coin coin) {
		lowCoinDispensers.put(coin.getValue(), dispenser);
		coinsRequired = true;
	}
	
	public void removeLowCoinDispenser(CoinDispenser dispenser, Coin coin) {
		lowCoinDispensers.remove(coin.getValue());
		if (lowCoinDispensers.isEmpty() == true) {
			coinsRequired = false;
		}
	}

	public boolean isBillsRequired() {
		return billsRequired;
	}
	
	public boolean isCoinsRequired() {
		return coinsRequired;
	}
	
	public Map<Integer, BillDispenser> getLowBillDispensers() {
		return lowBillDispensers;
	}
	
	public Map<BigDecimal, CoinDispenser> getLowCoinDispensers() {
		return lowCoinDispensers;
	}
	
	public void setNoBaggingRequestApproved(boolean bool) {
		this.noBaggingRequestApproved = bool;
	}
	
	public boolean getNoBaggingRequestApproved() {
		return this.noBaggingRequestApproved;
	}
	
	 public void notifyBlocked(SelfCheckoutSystemLogic checkout, String output) {
		 String number = "Self Checkout #";
		 int num = stations.indexOf(checkout);
		 num+=1;
		 blockedStations.put(num, checkout);
		 String message = output;
		 if (GUI !=null) {
			 GUI.updateStationBranch();
			 GUI.updateBlockedList(blockedStations);
		 }
		 
	 }
	
	 public void overrideBlock(int id) {
		 SelfCheckoutSystemLogic station = blockedStations.get(id);
		 if(station != null) {
			 System.out.println(station.getErrorMessage());
			 blockedStations.remove(id);
			 station.unBlock();
			 if(GUI != null) {
				 GUI.updateBlockedList(blockedStations);
			 }
		 }
	 }
	 

	public boolean login(String username, String password) {
		String pass = passwords.get(username);
		if (pass != null && pass.equals(password)) {
			return true;
		} else {
			return false;
		}
	}


	public void addUser(String username, String password) {
		passwords.put(username, password);
		
	}
	
	public void addStation(SelfCheckoutSystemLogic station) {
		stations.add(station);
		
	}
	
	public void registerGUI(AttendantGUI gui) {
		GUI = gui;
	}

	public void updateGUI() {
		if (GUI!= null) {
			GUI.updateStationBranch();
		}
	}

	public void addItem(String itemToAdd, SelfCheckoutSystemLogic system) {
		Product toAdd = products.get(itemToAdd);
		if(toAdd != null) {
			system.addBillList(toAdd);
			system.setAmountDue(system.getAmountDue()+toAdd.getPrice().doubleValue());
			system.currentScene.launch();
		}
		
		
		
	}


	public boolean startUp(SelfCheckoutStation station) {
		if (loggedInAttendant != null) {
			station.scale.enable();
			station.screen.enable();
			station.printer.enable();
			station.baggingArea.enable();
			station.cardReader.enable();
			station.mainScanner.enable();
			station.handheldScanner.enable();
			station.billInput.enable();
			station.billValidator.enable();
			station.billStorage.enable();
			station.coinSlot.enable();
			station.coinValidator.enable();
			station.coinStorage.enable();
			station.coinTray.enable();

			for (CoinDispenser coinDispenser : station.coinDispensers.values()) {
				coinDispenser.enable();
			}

			for (BillDispenser dispenser : station.billDispensers.values()) {
				dispenser.enable();
			}

			stationOn = true;
		} else {
			//throws error
			stationOn = false;
		}
		return stationOn;
	}

	public boolean shutDown(SelfCheckoutStation station) {
		if(loggedInAttendant == null) {
			station.scale.disable();
			station.screen.disable();
			station.printer.disable();
			station.baggingArea.disable();
			station.cardReader.disable();
			station.mainScanner.disable();
			station.handheldScanner.disable();
			station.billInput.disable();
			station.billValidator.disable();
			station.billStorage.disable();
			station.coinSlot.disable();
			station.coinValidator.disable();
			station.coinStorage.disable();
			station.coinTray.disable();
			for (CoinDispenser coinDispenser : station.coinDispensers.values()) {
				coinDispenser.disable();
			}

			for (BillDispenser dispenser : station.billDispensers.values()) {
				dispenser.disable();
			}
			stationOn = false;
		} else {
			//throws error
			stationOn = true;
		}
		return stationOn;

	}
	/*
	public void startUp(SelfCheckoutStation station) {
		station.scale.enable();
		station.screen.enable();
		station.printer.enable();
		station.baggingArea.enable();
		station.cardReader.enable();
		station.mainScanner.enable();
		station.handheldScanner.enable();
		station.billInput.enable();
		station.billValidator.enable();
		station.billStorage.enable();
		station.coinSlot.enable();
		station.coinValidator.enable();
		station.coinStorage.enable();
		station.coinTray.enable();

		for (CoinDispenser coinDispenser : station.coinDispensers.values()) {
			coinDispenser.enable();
		}

		for (BillDispenser dispenser : station.billDispensers.values()) {
			dispenser.enable();
		}
	}
	
	// Shuts down the hardware for a self checkout station

	public void shutDown(SelfCheckoutStation station) {
		station.scale.disable();
		station.screen.disable();
		station.printer.disable();
		station.baggingArea.disable();
		station.cardReader.disable();
		station.mainScanner.disable();
		station.handheldScanner.disable();
		station.billInput.disable();
		station.billValidator.disable();
		station.billStorage.disable();
		station.coinSlot.disable();
		station.coinValidator.disable();
		station.coinStorage.disable();
		station.coinTray.disable();
		for (CoinDispenser coinDispenser : station.coinDispensers.values()) {
			coinDispenser.disable();
		}

		for (BillDispenser dispenser : station.billDispensers.values()) {
			dispenser.disable();
		}
		
	}

	 */
	
	//keep
	@Override
	public void sessionComplete(SelfCheckoutStation station) {
	}

	@Override
	public void requiresMaintainence(SelfCheckoutStation station, String message) {		
	}
	
	
}
