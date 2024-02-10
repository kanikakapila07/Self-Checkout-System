package com.autovend.software;
import java.util.ArrayList;
/**
Desmond O'Brien: 30064340
Matthew Cusanelli: 30145324
Saadman Rahman: 30153482
Tanvir Ahamed Himel: 30148868
Victor campos: 30106934
Sean Tan: 30094560
Sahaj Malhotra: 30144405 
Caleb Cavilla: 30145972
Muhtadi Alam: 30150910
Omar Tejada: 31052626
Jose Perales: 30143354
Anna Lee: 30137463
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
public class MemberDatabase {

/**
 * Represents a cheap and dirty version of a set of databases that the
 * simulation can interact with.
 */
 
	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private MemberDatabase() {}
	
	/**
	 * The known membership numbers of users.
	 */
	public static ArrayList<String> MEMBERSHIP_DATABASE = new ArrayList<String>();
	
	
	public static boolean userExists(String number) {
		for (String key: MEMBERSHIP_DATABASE) {
			if (number.equals(key)) {
				return true;
			} 
		}
		return false;
	}
	

//	/**
//	 * A count of the items of the given product that are known to exist in the
//	 * store. Of course, this does not account for stolen items or items that were
//	 * not correctly recorded, but it helps management to track inventory.
//	 */
//	public static final Map<Product, Integer> INVENTORY = new HashMap<>();
	

}
