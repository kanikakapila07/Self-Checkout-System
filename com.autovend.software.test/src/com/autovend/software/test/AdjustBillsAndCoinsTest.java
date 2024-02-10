package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.software.AttendantStationLogic;
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
public class AdjustBillsAndCoinsTest {
    private Currency currency;
    private SelfCheckoutStation station;
    private SelfCheckoutSystemLogic system;
    private AttendantStationLogic as;
    private Bill bill_5;
    private Bill bill_10;
	private Coin coin_2;
	private Coin coin_025;
	private Coin coin_005;
	private Coin coin_1;
	
    @Before
    public void setup() throws SimulationException, OverloadException {

    	currency = Currency.getInstance(Locale.CANADA);
    	
        //Create self checkout station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);
       
        system = new SelfCheckoutSystemLogic(station);
        system.regiserDispensers();
        this.as = system.attendant;
      //  as = system.getAttendantStub();
        
        //Create a 5$ bill
        bill_5 = new Bill(5, currency);
        bill_10 = new Bill(10, currency);
        //Create a toonie 
        coin_2 = new Coin(toonie, currency);
        coin_1 = new Coin(loonie, currency);
        coin_025 = new Coin(twentyFiveCent, currency);
        coin_005 = new Coin(fiveCent, currency);
        
        for(int x : station.billDispensers.keySet()) {
			for (int i = 0; i < 20; i++) {
				Bill bill = new Bill(x, Currency.getInstance("CAD"));
				station.billDispensers.get(x).load(bill);
			}
		}

        for(BigDecimal x : station.coinDispensers.keySet()) {
        	for (int i = 0; i < 20; i++) {
        		Coin coin = new Coin(x, Currency.getInstance("CAD"));
        		station.coinDispensers.get(x).load(coin);
        	}
        }
    }

    //Reset the self checkout station and bill to null
    @After
    public void teardown() {
        station = null;
    }
    
    //****************************
    //TESTS FOR ADJUSTBANKNOTES
    //****************************
    
    //Test for when a bill is emitted from a single dispenser and dips below the threshold (20 -> 17)
   	@Test
   	public void emitBill_SingleDispenserBelowThreshold() throws DisabledException, EmptyException, OverloadException {
   		station.billDispensers.get(5).emit();
   		station.billOutput.removeDanglingBill();
   		station.billDispensers.get(5).emit();
   		station.billOutput.removeDanglingBill();
   		station.billDispensers.get(5).emit();
   		station.billOutput.removeDanglingBill();
   		assertEquals(true, as.isBillsRequired());
   		assertEquals(1, as.getLowBillDispensers().size());
   	}

    //Test for when a bill is emitted from a single dispenser and dips below the threshold boundary (20 -> 19)
	@Test
	public void emitBill_SingleDispenserBelowThresholdBoundary() throws DisabledException, EmptyException, OverloadException {
		station.billDispensers.get(5).emit();
		station.billOutput.removeDanglingBill();
		assertEquals(true, as.isBillsRequired());
		assertEquals(1, as.getLowBillDispensers().size());
	}
	
    //Test for when bills are unloaded from a single dispenser
   	@Test
   	public void unloadBills_SingleDispenserBelowThreshold() throws DisabledException, EmptyException, OverloadException {
   		station.billDispensers.get(5).unload();
   		assertEquals(true, as.isBillsRequired());
   		System.out.println(as.isBillsRequired());
   		assertEquals(1, as.getLowBillDispensers().size());
   	}
   	
    //Test for when bills are unloaded from multiple dispensers
   	@Test
   	public void unloadBills_MultipleDispensersBelowThreshold() throws DisabledException, EmptyException, OverloadException {
   		system.getStation().billDispensers.get(5).unload();
   		system.getStation().billDispensers.get(50).unload();
   		system.getStation().billDispensers.get(100).unload();
   		assertEquals(true, as.isBillsRequired());
   		assertEquals(3, as.getLowBillDispensers().size());
   	}
	
	//Test for when a bill is emitted from a single dispenser and stays above the threshold (22 -> 21)
	@Test
	public void emitBill_SingleDispenserAboveThreshold() throws DisabledException, EmptyException, OverloadException {
		Bill bill = new Bill(5, Currency.getInstance("CAD"));
		station.billDispensers.get(5).load(bill);
		station.billDispensers.get(5).load(bill);
		station.billDispensers.get(5).emit();
		station.billOutput.removeDanglingBill();
		assertEquals(false, as.isBillsRequired());
		assertEquals(0, as.getLowBillDispensers().size());
	}
	
	//Test for when a bill is emitted from multiple dispensers and multiple dispensers dip below the threshold (20 -> 19)
	@Test
	public void emitMultipleBills_MultipleDispensersBelowThreshold() throws DisabledException, EmptyException, OverloadException {
		station.billDispensers.get(5).emit();
		station.billOutput.removeDanglingBill();
		station.billDispensers.get(20).emit();
		station.billOutput.removeDanglingBill();
		assertEquals(true, as.isBillsRequired());
		assertEquals(2, as.getLowBillDispensers().size());
	}
	
	//Test for when a bill is emitted from multiple dispensers and multiple dispensers dip below the threshold (20 -> 19)
	@Test
	public void emitMultipleBills_SingleDispenserBelowThreshold() throws DisabledException, EmptyException, OverloadException {
		station.billDispensers.get(5).emit();
		station.billOutput.removeDanglingBill();
		station.billDispensers.get(20).emit();
		station.billOutput.removeDanglingBill();
		assertEquals(true, as.isBillsRequired());
		assertEquals(2, as.getLowBillDispensers().size());
	}
	
	//Test for when multiple dispensers are below threshold and one dispenser rises above threshold
	@Test
	public void loadBills_SingleDispenserRisesAboveThreshold() throws DisabledException, EmptyException, OverloadException {
		station.billDispensers.get(5).emit();
		station.billOutput.removeDanglingBill();
		station.billDispensers.get(20).emit();
		station.billOutput.removeDanglingBill();
		station.billDispensers.get(100).emit();
		station.billOutput.removeDanglingBill();
		Bill bill = new Bill(20, Currency.getInstance("CAD"));
		station.billDispensers.get(20).load(bill);
		assertEquals(true, as.isBillsRequired());
		assertEquals(2, as.getLowBillDispensers().size());
	}
	
	//Test for when unloading from an empty dispenser
	@Test
	public void unloadBills_EmptyDispenser() throws DisabledException, EmptyException, OverloadException {
		station.billDispensers.get(5).unload();
   		station.billDispensers.get(5).unload();
	}
	
    //****************************
    //TESTS FOR ADJUSTCOINS
    //****************************
	



	
	//Test for when a coin is emitted from a single dispenser and stays above the threshold (22 -> 21)
	@Test
	public void emitCoin_SingleDispenserAboveThreshold() throws DisabledException, EmptyException, OverloadException {
		Coin coin = new Coin(coin_1.getValue(), Currency.getInstance("CAD"));
		station.coinDispensers.get(coin_1.getValue()).load(coin);
		station.coinDispensers.get(coin_1.getValue()).load(coin);
		station.coinDispensers.get(coin_1.getValue()).emit();
		assertEquals(false, as.isCoinsRequired());
		assertEquals(0, as.getLowCoinDispensers().size());
	}
	



	//Test for when unloading from an empty dispenser
	@Test
	public void unloadCoins_EmptyDispenser() throws DisabledException, EmptyException, OverloadException {
   		station.coinDispensers.get(coin_1.getValue()).unload();
   		station.coinDispensers.get(coin_1.getValue()).unload();
	}
}
