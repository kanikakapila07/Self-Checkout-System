package com.autovend.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
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
public class StationUseTest {
	Currency c;
	SelfCheckoutStation station;
	SelfCheckoutSystemLogic stationlogic;
	AttendantStationLogic AttendantStationLogic;
	
	
	
	@Before
	
	public void setup() {
		c = Currency.getInstance(Locale.CANADA);
		station = new SelfCheckoutStation(c, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		stationlogic = new SelfCheckoutSystemLogic(station); // creates instance of AttendantStationLogic
		AttendantStationLogic = stationlogic.getAttendantStub();
		AttendantStationLogic.blockedStations.put(0, stationlogic);
		stationlogic.setID(stationlogic, 1);
	}
	
	
	@Test
	
	public void testDisable() {
		stationlogic.setBlocked(true);
		assertTrue(stationlogic.getBlocked());
	}
	
	@Test
	
	public void testEnable() {
	
		stationlogic.setBlocked(true);
		stationlogic.attendant.blockedStations.put(1, stationlogic);
		stationlogic.attendant.overrideBlock(1);
		assertFalse(stationlogic.getBlocked());
	}

}
