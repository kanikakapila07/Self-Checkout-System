package com.autovend.software.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
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
@RunWith(Suite.class)
@SuiteClasses({ AddItemByBrowsingTest.class, AddItemByPLUTest.class, AddItemByScanningTest.class, AddItemByTypingTest.class, AddOwnBagsTest.class, AdjustBillsAndCoinsTest.class, AttendantGUITest.class,	DoItYourselfStationTest.class, DoNotPlaceItemInBaggingAreaTest.class, EnterMembershipByScanningTest.class, 
 EnterMembershupNumberControllerTest.class, PayWithCashTest.class, PayWithCreditTest.class, PurchaseBagsTest.class, SearchItemsTest.class, 
	StationUseTest.class, SwipeMembershipTest.class, testLowInkPaper.class, testStartUpShutDown.class, PayWithDebitTest.class, WeightDiscrepancyTest.class, PayWithGiftCardTest.class, EnterMembershipByTypingTest.class})


public class FullTestSuite {

}
