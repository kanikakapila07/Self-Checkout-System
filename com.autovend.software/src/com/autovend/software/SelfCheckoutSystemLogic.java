
package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;


import java.util.Currency;
import java.util.Calendar;

import java.util.List;

import com.autovend.Barcode;

import com.autovend.Bill;
import com.autovend.Card;
import com.autovend.CreditCard;
import com.autovend.DebitCard;

import com.autovend.GiftCard;
import com.autovend.ReusableBag;

import com.autovend.SellableUnit;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.DoItYourselfStation.StationDisabledScene;

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
public class SelfCheckoutSystemLogic {	
	
	public static final String PAY_BY_PIN = "pin";
	public static final String PAY_BY_SWIPE = "swipe";
	public static final String PAY_BY_TAP = "tap";


	public boolean attendantNoBagResponse = false;
	AdjustCoinsController adjustCoinsController;
	AdjustBanknotesController adjustBanknotesController;
	private CreditCard currentCreditCard;
	private DebitCard currentDebitCard;
	private GiftCard currentGiftCard;

	public static CardIssuer interac = new CardIssuer("Interac");
	private PrintReceipt receiptController;
	private boolean blocked = false;
	// TODO: THERE IS NO BAG DISPENSER IN SELFCHECKOUTSTATION, SO I WILL DECLARE ONE HERE
	private ReusableBagDispenser bagDispenser;
	private static final int DEFAULT_BAG_CAPACITY = 100;
	private SelfCheckoutStation station;
	private List<Product> billList = new ArrayList<Product>();
	private double changeDispensed = 0;
	private double amountDue = 0;
	private boolean paymentProcess = false;
	private boolean loggedInAttendant = true;

	public boolean attendantRespondNoBag = false;
	
	
	private DoItYourselfStation customerGUI;
	public GUIScene currentScene;
	public boolean waitingForAttendantNoBag = false;

	private String cardPin;
	private BigDecimal cardPaymentAmount;
	private double baggingAreaWeight;
	private double expectedBagginAreaWeight;
    CardIssuer visa = new CardIssuer("Visa");
    CardIssuer masterCard = new CardIssuer("MasterCard");
    Calendar expiry;
    BigDecimal amountPaid;
    BigDecimal totalBill;
    CustomerIO customer;
	private boolean printing;
	private String creditPaymentMethod;
	private boolean cardPaymentStatus;
	private boolean unhandledDiscrepancy;
	private boolean unApprovedDiscrepancy;
	private boolean discrepancyActive = false;
	private boolean doNotBagItemActive = false;
	private boolean changeLow = false;
	private Product currentSelectedProduct;
	private int numberBagsPurchased;
	public int numberBagsOwned;

	private boolean stationOn;
	private boolean attendantApprovedOwnBags;
	public AttendantStationLogic attendant;
	// FROM PLU
	private SellableUnit currentSelectedUnit;
	// FROM SCANNER
	private Barcode currentBarcode;
	private boolean membershipActive = false;
	private String memberNumber;
	//private PrintReceipt receiptController;
	private CustomerIO customerio;

	public AttendantStationLogic attendantlog;

	private final int BAG_DISPENSER_DEFAULT_LIMIT = 100;
	public int id; // Added in for AttendantStationLogic
	private com.autovend.MembershipCard userCard;
	private String errorMessage = "";
	//private PayWithCashController payWithCashController = new PayWithCashController(station, this);
	/*
	public SelfCheckoutSystemLogic(SelfCheckoutStation station, CustomerIO cs, AttendantStationLogic att) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		
		customerio = cs;
		attendantlog = att;
		
		AdjustBanknotesController adjustBanknotesController = new AdjustBanknotesController(this);
		AdjustCoinsController adjustCoinsController = new AdjustCoinsController(this);
		receiptController = new PrintReceipt(station,this);
		receiptController.registerAttendent(attendantlog);
		this.bagDispenser = new ReusableBagDispenser(DEFAULT_BAG_CAPACITY);
		
		ReusableBag[] defaultBags = new ReusableBag[DEFAULT_BAG_CAPACITY ];
		
		for (int i = 0; i < defaultBags.length; ++i) {
			defaultBags[i] = new ReusableBag();
		}
		
		try {
			this.bagDispenser.load(defaultBags);
		} catch (OverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			this.station.printer.addInk(5000);
			this.station.printer.addPaper(100);
		}
		catch (OverloadException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		station.printer.register(receiptController);
		
		memberNumber = "0";


		membershipActive = false;
		blocked = false;


	}
	*/
	
	public SelfCheckoutSystemLogic(SelfCheckoutStation station) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		this.station.coinSlot.disable();
		this.customerio = new CustomerIO();
		this.attendantlog = new AttendantStationLogic();
		this.attendant = new AttendantStationLogic();
		this.bagDispenser = new ReusableBagDispenser(DEFAULT_BAG_CAPACITY);

		ReusableBag[] defaultBags = new ReusableBag[100];
		for (int i = 0; i < defaultBags.length; ++i) {
			defaultBags[i] = new ReusableBag();
		}

		try {
			this.station.bagDispenser.load(defaultBags);
		} catch (OverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			this.station.printer.addInk(5000);
			this.station.printer.addPaper(100);
		}
		catch (OverloadException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		receiptController = new PrintReceipt(station,this);
		receiptController.registerAttendent(attendantlog);

		membershipActive = false;
		blocked = false;
		station.printer.register(receiptController);
	}
	
	
	public void regiserDispensers() {
		adjustBanknotesController = new AdjustBanknotesController(this);
		adjustCoinsController = new AdjustCoinsController(this);
	}
	
	
	public void resetStation() {
		billList = new ArrayList<Product>();
		changeDispensed = 0;
		amountDue = 0;
		paymentProcess = false;
		
		baggingAreaWeight = 0;
		expectedBagginAreaWeight = 0;

		printing = false;
		
		unhandledDiscrepancy = false;
		unApprovedDiscrepancy = false;
		discrepancyActive = false;
		doNotBagItemActive = false;
		currentSelectedProduct = null;
		
		attendantApprovedOwnBags = false;
		
		// FROM PLU
		currentSelectedUnit = null;
		
		// FROM SCANNER
		currentBarcode = null;
		
		//isMember = false;
		membershipActive = false;
		memberNumber = null;
		
		//private PrintReceipt receiptController;
		customerio = null;

		bagDispenser = null;
	}

	public void setID(SelfCheckoutSystemLogic logic, int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * Startup use case
	 * 
	 * 
	 * @param station
	 * @return
	 */
	public boolean startUp(SelfCheckoutStation station) {
		if (loggedInAttendant) {
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

	
	/**
	 * Shutdown use case
	 * 
	 * 
	 * @param station
	 * @return
	 */
	public boolean shutDown(SelfCheckoutStation station) {
		if(loggedInAttendant) {
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
	
	// sets the system to be ready to take payment, simulates customer indicating they want to pay cash for their bill
		public void payWithCash() throws SimulationException, OverloadException {
			if (blocked != true) {
				paymentProcess = true;
				PayWithCashController payWithCashController = new PayWithCashController(station, this);
				station.billInput.register(payWithCashController);
				station.billInput.enable();
				station.billValidator.enable();
				station.billValidator.register(payWithCashController);
				station.coinSlot.register(payWithCashController);
				station.coinSlot.enable();
				station.billInput.enable();
				station.coinValidator.register(payWithCashController);
				for(BillDispenser dispenser : station.billDispensers.values())
					dispenser.register(payWithCashController);
				for(CoinDispenser dispenser : station.coinDispensers.values())
					dispenser.register(payWithCashController);
			}
		}
		
		public void PayWithCard() {
			if(blocked != true) {
				paymentProcess = true;
				station.cardReader.enable();
				customerio.getReady();
			}
			
		}
		
		public void changeAmountDue(double d, PayWithCashController controller) {
			amountDue -= d;
			if (amountDue <= 0 && paymentProcess) {
				paymentProcess = false; // exits the system out of payment
				station.billInput.disable();
				station.billValidator.disable();
				station.coinSlot.disable();
				station.coinValidator.disable();
				controller.deliverChange(amountDue);
				printing = true; // Set boolean to signal receipt printer to print
			}

			try {
				this.currentScene.launch();
			}
			catch (NullPointerException e) {
				// should only happen in testing

			}


			
		}
		
		// Poor workarounds, but avoid reimplementing the controllers
		public void clearPayWithCashObservers() {
			station.billInput.deregisterAll();
			station.billValidator.deregisterAll();
			station.coinSlot.deregisterAll();
			station.billInput.deregisterAll();
			station.coinValidator.deregisterAll();
		}
		
		public void clearWeightDiscrepancyObservers() {
			station.baggingArea.deregisterAll();
		}
		
		public void clearAddItemByScanningObservers() {
			station.mainScanner.deregisterAll();
		}

	
	/**
	 * Prints the receipt and notifies attendant of problems
	 * @throws EmptyException 
	 * @throws OverloadException 
	 */
	public boolean startPrinting(List<Product> billList) throws EmptyException, OverloadException{
		return receiptController.print(billList);
	}
	
	public boolean takeMembership(String number) {
		if (customerio.enterMembership(number)) {
			memberNumber = number;
			return true;
		} else {
			return false;
		}
	}
	
	public void addItemByBrowsing() throws Exception {
		if(blocked != true) {
			AddItemByBrowsing stub = new AddItemByBrowsing(this.station, this);
			stub.addItem(currentSelectedProduct);
		}
	}
	
	public void addItemByTyping() throws Exception {
		if(blocked != true) {
			AddItemByTypingController controller = new AddItemByTypingController(this.station, this);
			controller.add(currentSelectedUnit);
		}
	}
	

	public void addItemByScanning() throws Exception {
		if (blocked != true) {
			AddItemByScanningController controller = new AddItemByScanningController(this.station, this);
			this.station.mainScanner.register(controller);
			controller.add(this.currentSelectedUnit);
		}
	}
	



	

	
	
	public void payWithCredit() {
		PayWithCreditController controller = new PayWithCreditController(this.station, this);
		this.station.cardReader.register(controller);
    	this.station.cardReader.enable();
	}
	
	
	public void payWithDebit() {
		PayWithDebitController controller = new PayWithDebitController(this.station, this);
		this.station.cardReader.register(controller);
    	this.station.cardReader.enable();
	}
	

	public void payWithGiftCard() {
		PayWithGiftCardController controller = new PayWithGiftCardController(this.station, this);
		this.station.cardReader.register(controller);
		this.station.cardReader.enable();
	}
	

	public boolean addItemByPLU() throws Exception {
		if (blocked != true) {
			AddItemByPLUController controller = new AddItemByPLUController(this.station, this);
			controller.add(this.currentSelectedUnit);
			return true;
		}
		return false;
	}

	public void DoNotPlaceInBaggingArea() throws Exception{
		DoNotPlaceItemInBaggingAreaController controller = new DoNotPlaceItemInBaggingAreaController(this.station, this);
		block("Non-Bagged Item");

		this.waitingForAttendantNoBag = true;
		this.discrepancyActive = true;
		try {
			currentScene.launch();
		}
		catch (NullPointerException e) {
			// Do nothing, should only happen in testing
		}
		controller.signalToNotPlaceItemInBaggingArea(this.currentSelectedUnit);
		

	}
	
	public void addOwnBags() throws OverloadException {
		AddOwnBagsController controller = new AddOwnBagsController(this.station, this);
		controller.addOwnBags();
		block("Personal Bags");
		this.discrepancyActive = true;
		try {
			this.currentScene.launch();
		}
		catch (NullPointerException e) {
			// should only happen in testing
		}
	}
	
	
	public void purchaseBags() throws OverloadException, EmptyException {
		PurchaseBagsController pb = new PurchaseBagsController(this.station, this);
		this.baggingAreaWeight = this.station.baggingArea.getCurrentWeight();
		pb.addBagsToBill();
	}
	
	public void addMembershipByScanning( ) {
		EnterMembershipByScanning observer = new EnterMembershipByScanning(this, this.station);
		this.station.handheldScanner.register(observer);
	}
	
	
	
	
	public void clearHandheldScannerObservers() {
		this.station.handheldScanner.deregisterAll();
	}
	
	
	public void weightDiscrepency(double expectedWeight) throws OverloadException {
		// weithDiscrep = true
		WeightDiscrepancyController wd = new WeightDiscrepancyController(this.station, this);
		this.station.baggingArea.register(wd);
	}
	

	public boolean customerNoBag = false;

	public void checkDiscrepancy(double actualWeight) {
		// if no discrepancy is found, program can continue
		if (actualWeight == this.expectedBagginAreaWeight) {
			station.handheldScanner.enable();
			station.mainScanner.enable();
			station.billInput.enable();
			unBlock();
			return;
		}
		System.out.println(this.expectedBagginAreaWeight);
		try {
			System.out.println(this.station.baggingArea.getCurrentWeight());
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		block("Weight discrepancy");
		this.discrepancyActive = true;
		System.out.println("DISCREPANCY FOUND");
		
		// otherwise discrepancy found, disable station
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		
		this.weightDiscrepancyOptions();
	}
	
	/**
	 * Method to determine and decide how to handle weight discrepancy
	 * based on attendant and customer decisions
	 * 
	 * 
	 * @return - true if no exception was found
	 * @throws OverloadException - if weight limit in bagging area is exceeded
	 * @throws SimulationException - if attendant does not approve of discrepancy
	 */
	private void weightDiscrepancyOptions() {

		// TODO: TBH ATTENDANT SHOULD JUST SET WHATEVER THEY WANT TO SET HERE AND REENABLE THE STATION
		
		// attendant is not approving of discrepancy, will have to go take a look manually
		if (this.unApprovedDiscrepancy) {
			this.unApprovedDiscrepancy = true;
			this.discrepancyActive = true;
			// TODO: IF ATTENDANT BUGGED DELETE THESE DISABLES
			station.handheldScanner.disable();
			station.mainScanner.disable();
			station.billInput.disable();
			return;
		}
		
		// this case will have to react with the "Do Not Bag Item" use case in the future,
		// so let's leave this here for now but just pretend it is resolved by returning true
		if (this.customerNoBag) {

			this.doNotBagItemActive = true;
			this.discrepancyActive = true;
			return;
		}
		
		// otherwise if the attendant approved of the weight discrepancy, let's enable the station
		// and get ready for another transaction
		station.handheldScanner.enable();
		station.mainScanner.enable();
		station.billInput.enable();
		this.discrepancyActive = false;
	}
	
	
	/**
	 * Adds an item to the end of the current bill list
	 */
	public void addBillList(Product product) {
		billList.add(product);
		if(attendant!= null) {
			attendant.updateGUI();
		}
	}
	/**
	 * removes the item in the bill list at the specified index
	 */
	public void removeBillList(int index) {
		Product p = billList.get(index);
		BigDecimal price = p.getPrice();
		amountDue = amountDue - price.doubleValue();
		billList.remove(index);
		this.currentScene.launch();
		if(attendant!= null) {
			attendant.updateGUI();
		}
		
	}

	/**
	 * gets the current bill List
	 */
	public List<Product> getBillList() {
		return billList;
	}

	/**
	 * sets the current bill list
	 */
	public void setBillList(List<Product> billList) {
		this.billList = billList;
	}

	/**
	 * gets the total amount currently due based on the bill list
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * sets the total amount due
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}
	
	/**
	 * gets the total amount currently due based on the bill list
	 */
	public SelfCheckoutStation getStation() {
		return station;
	}

	public void setPrinting(boolean printing) {
		this.printing = printing;
	}

	public double getChangeDispensed() {
		return changeDispensed;
	}

	public void setChangeDispensed(double changeDispensed) {
		this.changeDispensed = changeDispensed;
	}
	
	public void setCustomerNoBag(boolean value) {
		this.customerNoBag = value;
	}
	

	public void setAttendantDisapproval(boolean value) {
		this.unApprovedDiscrepancy = value;


	}
	
	
	public boolean getUnhandledDiscrepancy() {
		return unhandledDiscrepancy;
	}
	
	public boolean getUnApprovedDiscrepancy() {
		return unApprovedDiscrepancy;
	}
	
	public boolean getDiscrepancyActive() {
		return discrepancyActive;
	}
	
	public boolean getDoNotBagItemActive() {
		return this.doNotBagItemActive;
	}
	

	public CustomerIO getCustomerIO() {
		return this.customerio;
	}
	
	public AttendantStationLogic getAttendantStub() {
		return this.attendantlog;
	}
	
	public ReusableBagDispenser getBagDispenser() {
		return this.bagDispenser;
	}
	
	public double getBaggingAreaWeight() {
		return this.baggingAreaWeight;
	}
	
	public  void setBaggingAreaWeight(double val) {
		this.baggingAreaWeight = val;
	}
	
	public SellableUnit getCurrentSelectedUnit() {
		return this.currentSelectedUnit;
	}
	
	public void setCurrentSelectableUnit(SellableUnit unit) {
		this.currentSelectedUnit = unit;
	}
	
	public void setCurrentBarcode(Barcode val) {
		this.currentBarcode	 = val;
	}
	
	public Barcode getCurrentBarcode() {
		return this.currentBarcode;
	}
	
	public void setDiscrepancyActive(boolean val) {
		this.discrepancyActive = val;
	}
	
	public void setCurrentSelectedProduct(Product val) {
		this.currentSelectedProduct = val;
	}
	
	public Product getCurrentSelectedProdcut() {
		return this.currentSelectedProduct;
	}
	
	public void setStation(SelfCheckoutStation stat) {
		this.station = stat;
	}
	
	public void setCardSwipe(com.autovend.MembershipCard userCard, String number)
	{
		this.userCard = userCard;
		this.memberNumber = number;
	}
	

	public void setExpectedWeight(double val) {
		this.expectedBagginAreaWeight = val;
	}
	

	
	public String getErrorMessage() {
		return errorMessage;
	}
	

	public int getNumberBagsPurchased() {
		return this.numberBagsPurchased;
	}
	
	
	public void setNumberBagsPurchased(int val) {
		this.numberBagsPurchased = val;
	}
	
	
	public void setChangeLow(boolean val) {
		this.changeLow = val;
	}

	public void block(String msg) {
		blocked = true;
		errorMessage = msg;
		if (attendant!=null) {
			attendant.notifyBlocked(this, msg);
		}
	}


	public boolean isBlocked(){
		return blocked;
	}
	
	public void unBlock() {
		blocked = false;
		errorMessage = "";
		try {
			this.currentScene.launch();
		}
		catch (NullPointerException e) {
			// should only happen in testing
		}
	}
	

	
	
	public String getCreditPaymentMethod() {
		return this.creditPaymentMethod;
	}
	
	
	public void setCreditPaymentMethod(String type) {
		this.creditPaymentMethod = type;
	}
	
	
	public void setCurrentPin(String pin) {
		this.cardPin = pin;
	}
	
	
	
	public String getCurrentPin() {
		return this.cardPin;
	}
	
	public BigDecimal getCardPaymentAmount() {
		return this.cardPaymentAmount;
		
	}
	
	
	public void setCardPaymentAmount(BigDecimal amount) {
		this.cardPaymentAmount = amount;
		
	}
	
	
	public void setCardPaymentStatus(boolean val) {
		this.cardPaymentStatus = val;
	}
	
	
	public boolean getCardPaymentStatus() {
		return this.cardPaymentStatus;
	}
	
	
	public void setCurrentCreditCard(CreditCard card) {
		this.currentCreditCard = card;
	}
	
	public CreditCard getCurrentCredtiCard() {
		return this.currentCreditCard;
	}

	public void setCurrentGiftCard(GiftCard card) {
		this.currentGiftCard = card;
	}
	
	public CreditCard getCurrentGiftCard() {
		return this.currentCreditCard;
	}

	
	public void setCurrentDebitCard(DebitCard card) {
		this.currentDebitCard = card;
	}
	
	public DebitCard getDebitCredtiCard() {
		return this.currentDebitCard;
	}
	

	public void registerAttendant(AttendantStationLogic attend) {
		this.attendant = attend;

	}
	
	public void setMembershipActive(boolean val, String number) {
		this.membershipActive = val;
		this.memberNumber = number;
	}
	
	public void setMembershipNumber(String number) {
		this.memberNumber = number;
	}
	
	public String getMembershipNumber() {
		return this.memberNumber;
	}

	
	public boolean getMembershipActive() {
		return this.membershipActive;
	}
	
	public void setCustomerGUI(DoItYourselfStation customerGUI) {
		this.customerGUI = customerGUI;
	}
	
	public DoItYourselfStation getCustomerGUI() {
		return this.customerGUI;
	}
	
	public void setCurrentScene(GUIScene currentScene) {
		this.currentScene = currentScene;
	}
	
	public GUIScene getCurrentScene() {
		return this.currentScene;
	}
	
	public void setBlocked(boolean val) {
		this.blocked = val;
		if (!this.blocked) {
			try {
				this.currentScene.launch();
			}
			catch (NullPointerException e) {
				// only in testing
			}
		}
	}
	
	public boolean getBlocked() {
		return this.blocked;	
	}
	
}

