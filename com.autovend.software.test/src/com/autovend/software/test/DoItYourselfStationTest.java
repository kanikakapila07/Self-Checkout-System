package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.DoItYourselfStation;
import com.autovend.software.DoItYourselfStation.AddItemByBrowsingScene;
import com.autovend.software.DoItYourselfStation.AddItemByPLUScene;
import com.autovend.software.DoItYourselfStation.AddItemByScanningScene;
import com.autovend.software.DoItYourselfStation.AddItemByTypingScene;
import com.autovend.software.DoItYourselfStation.ChooseAddItemScene;
import com.autovend.software.DoItYourselfStation.ChooseLanguageScene;
import com.autovend.software.DoItYourselfStation.ChooseMembershipScene;
import com.autovend.software.DoItYourselfStation.ChoosePaymentMethodScene;
import com.autovend.software.DoItYourselfStation.PayWithCashScene;
import com.autovend.software.DoItYourselfStation.PayWithCreditDebitScene;
import com.autovend.software.DoItYourselfStation.PrintReceiptScene;
import com.autovend.software.DoItYourselfStation.ScanMembershipScene;
import com.autovend.software.DoItYourselfStation.StationDisabledScene;
import com.autovend.software.DoItYourselfStation.SwipeMembershipScene;
import com.autovend.software.DoItYourselfStation.ThankYouScene;
import com.autovend.software.DoItYourselfStation.TransactionSummaryScene;
import com.autovend.software.DoItYourselfStation.TypeMembershipScene;
import com.autovend.software.DoItYourselfStation.WelcomeScene;
import com.autovend.software.MemberDatabase;
import com.autovend.software.SelfCheckoutSystemLogic;
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
public class DoItYourselfStationTest {
	DoItYourselfStation useCase;
	SelfCheckoutStation station;
	SelfCheckoutSystemLogic system;
	
	@Before
	public void setUp() throws Exception {
		Currency curr = Currency.getInstance(Locale.CANADA);
		
		MemberDatabase.MEMBERSHIP_DATABASE.add("0123");
		
		PriceLookUpCode PLU = new PriceLookUpCode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.two, Numeral.three});
		PLUCodedProduct PLUProduct = new PLUCodedProduct(PLU, "Broccoli", new BigDecimal("1")); 
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLU, PLUProduct);
		
		BarcodedProduct p1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.zero, Numeral.zero}),
				"Potato", BigDecimal.valueOf(100f), 100f);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(p1.getBarcode(), p1);
		
		int[] billDenoms = {5,10,20,50,100};
		
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}
		
		int scaleMax = 100;
		int scaleSensitivity = 1;
		
		station = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		system = new SelfCheckoutSystemLogic(station);
		station.coinSlot.enable();
		system.setStation(station);
		
		useCase = new DoItYourselfStation(system, 1);
	}

	@After
	public void tearDown() throws Exception {
		useCase = null;
	}
	
	@Test
	public void WelcomeScene() {
		WelcomeScene welcomeScene = useCase.new WelcomeScene(null);
		welcomeScene.launch();
		// Test clicking the language button and seeing if the choose language scene is launched
		welcomeScene.languageButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseLanguageScene);
		
		// Test clicking the membership button and seeing if the choose membership scene is launched
		welcomeScene.membershipButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseMembershipScene);
		
		// Test clicking the start button and seeing if the transaction summary scene is launched
		welcomeScene.startButton.doClick();
		assertTrue(system.getCurrentScene() instanceof TransactionSummaryScene);
	}
	
	@Test
	public void ChooseLanguageScene() {
		ChooseLanguageScene chooseLanguageScene = useCase.new ChooseLanguageScene(useCase.new WelcomeScene(null));
		chooseLanguageScene.launch();

		chooseLanguageScene.englishButton.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);

		chooseLanguageScene.alsoEnglishButton.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);

		chooseLanguageScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);
	}
	
	@Test
	public void ChooseMembershipScene() {
		ChooseMembershipScene chooseMembershipScene = useCase.new ChooseMembershipScene(useCase.new WelcomeScene(null));
		chooseMembershipScene.launch();

		chooseMembershipScene.scanningButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ScanMembershipScene);

		chooseMembershipScene.numberButton.doClick();
		assertTrue(system.getCurrentScene() instanceof TypeMembershipScene);

		chooseMembershipScene.swipeButton.doClick();
		assertTrue(system.getCurrentScene() instanceof SwipeMembershipScene);
		
		chooseMembershipScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);
	}
	
	@Test
	public void TypeMembershipScene() {
		TypeMembershipScene typeMembershipScene = useCase.new TypeMembershipScene(useCase.new ChooseMembershipScene(null));
		typeMembershipScene.launch();
		
		typeMembershipScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseMembershipScene);
		
		typeMembershipScene.button0.doClick();
		assertEquals(typeMembershipScene.numField.getText(), "0");
		
		typeMembershipScene.buttonBack.doClick();
		assertEquals(typeMembershipScene.numField.getText(), "");
		
		typeMembershipScene.buttonEnter.doClick();
		assertTrue(system.getCurrentScene() instanceof TypeMembershipScene);
		
		typeMembershipScene.button0.doClick();
		typeMembershipScene.buttonEnter.doClick();
		assertTrue(system.getCurrentScene() instanceof TypeMembershipScene);
		
		typeMembershipScene.buttonBack.doClick();
		typeMembershipScene.button0.doClick();
		typeMembershipScene.button1.doClick();
		typeMembershipScene.button2.doClick();
		typeMembershipScene.button3.doClick();
		typeMembershipScene.buttonEnter.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);
	}
	
	@Test
	public void TransactionSummaryScene() {
		TransactionSummaryScene transactionSummaryScene = useCase.new TransactionSummaryScene(null);
		transactionSummaryScene.launch();
		
		transactionSummaryScene.addItemsButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseAddItemScene);

		transactionSummaryScene.payButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChoosePaymentMethodScene);
	}
	
	@Test
	public void StationDisabledScene() {
		StationDisabledScene stationDisabledScene = useCase.new StationDisabledScene(null);
		stationDisabledScene.launch();
	}
	
	@Test
	public void PayWithCreditDebitScene() {
		PayWithCreditDebitScene payWithCreditDebitScene = useCase.new PayWithCreditDebitScene(useCase.new ChoosePaymentMethodScene(null));
		payWithCreditDebitScene.launch();
		
		payWithCreditDebitScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChoosePaymentMethodScene);
		
		payWithCreditDebitScene.creditButton.doClick();
		payWithCreditDebitScene.tapButton.doClick();
		payWithCreditDebitScene.swipeButton.doClick();
		payWithCreditDebitScene.insertButton.doClick();
		
		payWithCreditDebitScene.button0.doClick();
		assertEquals(payWithCreditDebitScene.pinCode, "0");
		
		payWithCreditDebitScene.buttonBack.doClick();
		assertEquals(payWithCreditDebitScene.pinCode, "");
		payWithCreditDebitScene.button1.doClick();
		payWithCreditDebitScene.button2.doClick();
		payWithCreditDebitScene.button3.doClick();
		payWithCreditDebitScene.button4.doClick();
		// payWithCreditDebitScene.removeButton.doClick();
		
		payWithCreditDebitScene.buttonBack.doClick();
		payWithCreditDebitScene.buttonBack.doClick();
		payWithCreditDebitScene.buttonBack.doClick();
		payWithCreditDebitScene.buttonBack.doClick();
		
		payWithCreditDebitScene.debitButton.doClick();
		payWithCreditDebitScene.tapButton.doClick();
		payWithCreditDebitScene.swipeButton.doClick();
		payWithCreditDebitScene.insertButton.doClick();
		
		payWithCreditDebitScene.button0.doClick();
		assertEquals(payWithCreditDebitScene.pinCode, "0");
		
		payWithCreditDebitScene.buttonBack.doClick();
		assertEquals(payWithCreditDebitScene.pinCode, "");
		payWithCreditDebitScene.button1.doClick();
		payWithCreditDebitScene.button2.doClick();
		payWithCreditDebitScene.button3.doClick();
		payWithCreditDebitScene.button4.doClick();
		// payWithCreditDebitScene.removeButton.doClick();
		
		payWithCreditDebitScene.printReceiptButton.doClick();
		assertTrue(system.getCurrentScene() instanceof PrintReceiptScene);
	}
	
	@Test
	public void ChoosePaymentMethodScene() {
		ChoosePaymentMethodScene choosePaymentMethodScene = useCase.new ChoosePaymentMethodScene(useCase.new TransactionSummaryScene(null));
		choosePaymentMethodScene.launch();
		
		choosePaymentMethodScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof TransactionSummaryScene);
		
		choosePaymentMethodScene.payWithCardButton.doClick();
		assertTrue(system.getCurrentScene() instanceof PayWithCreditDebitScene);
		
		choosePaymentMethodScene.payWithCashButton.doClick();
		assertTrue(system.getCurrentScene() instanceof PayWithCashScene);
	}
	
	@Test
	public void ChooseAddItemScene() {
		ChooseAddItemScene chooseAddItemScene = useCase.new ChooseAddItemScene(useCase.new TransactionSummaryScene(null));
		chooseAddItemScene.launch();
		
		chooseAddItemScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof TransactionSummaryScene);
		
		chooseAddItemScene.pluButton.doClick();
		assertTrue(system.getCurrentScene() instanceof AddItemByPLUScene);
		
		chooseAddItemScene.browseButton.doClick();
		assertTrue(system.getCurrentScene() instanceof AddItemByBrowsingScene);
		
		chooseAddItemScene.scanningButton.doClick();
		assertTrue(system.getCurrentScene() instanceof AddItemByScanningScene);
		
		chooseAddItemScene.searchButton.doClick();
		assertTrue(system.getCurrentScene() instanceof AddItemByTypingScene);
	}
	
	@Test
	public void AddItemByPLUScene() {
		AddItemByPLUScene addItemByPLUScene = useCase.new AddItemByPLUScene(useCase.new ChooseAddItemScene(null));
		addItemByPLUScene.launch();
		
		addItemByPLUScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseAddItemScene);
		
		addItemByPLUScene.button0.doClick();
		assertEquals(addItemByPLUScene.numField.getText(), "0");
		
		assertFalse(addItemByPLUScene.buttonEnter.isEnabled());
		
		addItemByPLUScene.buttonBack.doClick();
		assertEquals(addItemByPLUScene.numField.getText(), "");
		
		assertFalse(addItemByPLUScene.buttonEnter.isEnabled());
		
		addItemByPLUScene.button0.doClick();
		assertFalse(addItemByPLUScene.buttonEnter.isEnabled());
		addItemByPLUScene.button1.doClick();
		assertFalse(addItemByPLUScene.buttonEnter.isEnabled());
		addItemByPLUScene.button2.doClick();
		assertFalse(addItemByPLUScene.buttonEnter.isEnabled());
		addItemByPLUScene.button3.doClick();
		assertTrue(addItemByPLUScene.buttonEnter.isEnabled());
		
		addItemByPLUScene.button4.doClick();
		addItemByPLUScene.button5.doClick();
		assertEquals(addItemByPLUScene.numField.getText(), "01234");
		
		addItemByPLUScene.buttonBack.doClick();
		addItemByPLUScene.buttonEnter.doClick();
	}
	
	@Test
	public void AddItemByTypingScene() {
		AddItemByTypingScene addItemByTypingScene = useCase.new AddItemByTypingScene(useCase.new ChooseAddItemScene(null));
		addItemByTypingScene.launch();
		
		addItemByTypingScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseAddItemScene);
		
		addItemByTypingScene.buttonP.doClick();
		assertEquals(addItemByTypingScene.numField.getText(), "P");
		assertEquals(addItemByTypingScene.productResults.size(), 1);
		
		addItemByTypingScene.table.setRowSelectionInterval(0, 0);
		
		addItemByTypingScene.selectItemButton.doClick();
	}
	
	@Test
	public void AddItemByBrowsingScene() {
		AddItemByBrowsingScene addItemByBrowsingScene = useCase.new AddItemByBrowsingScene(useCase.new ChooseAddItemScene(null));
		addItemByBrowsingScene.launch();
		
		addItemByBrowsingScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ChooseAddItemScene);
		
		addItemByBrowsingScene.table.setRowSelectionInterval(0, 0);
		
		addItemByBrowsingScene.selectItemButton.doClick();
		addItemByBrowsingScene.potatoButton.doClick();
	}
	
	@Test
	public void ThankYouScene() {
		ThankYouScene thankYouScene = useCase.new ThankYouScene(null);
		thankYouScene.launch();
		
		thankYouScene.startButton.doClick();
		assertTrue(system.getCurrentScene() instanceof WelcomeScene);
	}
	
	@Test
	public void PrintReceiptScene() {
		PrintReceiptScene printReceiptScene = useCase.new PrintReceiptScene(useCase.new PayWithCreditDebitScene(null));
		printReceiptScene.launch();
		
		printReceiptScene.backButton.doClick();
		assertTrue(system.getCurrentScene() instanceof PayWithCreditDebitScene);
		
		printReceiptScene.takeReceiptButton.doClick();
		printReceiptScene.exitButton.doClick();
		assertTrue(system.getCurrentScene() instanceof ThankYouScene);
	}
}
