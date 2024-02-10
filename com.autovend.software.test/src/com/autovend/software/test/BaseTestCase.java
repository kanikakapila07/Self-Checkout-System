package com.autovend.software.test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.DoItYourselfStation;
import com.autovend.software.GUIScene;
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
public class BaseTestCase {
	protected SelfCheckoutStation station;
	protected SelfCheckoutSystemLogic system;
	protected int weightLimit = 1000000; 

	protected void initializeStation() throws OverloadException {
		Currency curr = Currency.getInstance(Locale.CANADA);
		
		int[] billDenoms = {5,10,20,50,100};
		
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}
		
		int scaleMax = weightLimit;
		int scaleSensitivity = 1;
		
		this.station = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		this.system = new SelfCheckoutSystemLogic(this.station);
	}
	
	
	protected void deinitializeStation() {
		this.station = null;
		this.system = null;
	}



}
