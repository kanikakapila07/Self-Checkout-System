package com.autovend.software;
import java.io.IOException;
import java.util.ArrayList;

import com.autovend.MembershipCard;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.DoItYourselfStation.WelcomeScene;

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

public class EnterMembershipByTyping extends EnterMembershipNumberController {
	
	public EnterMembershipByTyping(SelfCheckoutSystemLogic system, SelfCheckoutStation station) {
		super(system, station);
	}
	
	public void enterMembershipByTyping(String membershipNumber) {
		if (super.userExists(membershipNumber)) {
			MembershipCard someCard = new MembershipCard("membership", membershipNumber, "User", true);
			this.system.setCardSwipe(someCard, membershipNumber);
			

		}
	}

	/**
	 * method to get membership number as string and check if it exists in DB
	 * @return 
	 * @throws InvalidMemberException if number is not in MemberDatabase
	 */
//	public EnterMembershipByTyping(String number) throws InvalidMemberException {
//		if(MemberDatabase.userExists(number) == false) {
//			throw new InvalidMemberException();
//		}
//	}
	
//	/**
//	 * A count of the items of the given product that are known to exist in the
//	 * store. Of course, this does not account for stolen items or items that were
//	 * not correctly recorded, but it helps management to track inventory.
//	 */
//	public static final Map<Product, Integer> INVENTORY = new HashMap<>();
	

}
