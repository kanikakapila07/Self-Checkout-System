package com.autovend.software;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.autovend.MagneticStripeFailureException;
import com.autovend.devices.SelfCheckoutStation;
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
public class CustomerIO implements PrintReceiptObserver {
    private Boolean thanks = false; // Status for if the customer needs to be thanked
    private Boolean ready = false; // Status if ready for new customer session
    
    private String message = "";
//    private String name;
//    private String password;
    

    private boolean customerNoBag;
    private boolean placeInBaggingArea = true;

    private int numberOfBagsPurchased;
    private int numberOfPersonalBags;

//    private String name;
//    private String password;
    
    public String printToDisplay() {
    	return message;
    }

    // Customer IO returns thank you message to screen ("Thank you for shopping with us today!") or
    // empty string if they shouldn't be thanked yet
    public void thankCustomer(){
        if(thanks){
        	message = "Thank you for shopping with us today!";
        }
        printToDisplay();
    }

    // Sets if ready for new customer (at the start of new customer set to false, and once finished set to true)
    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    // Returns status about customer session
    public Boolean getReady() {
        return ready;
    }

    // System signals Customer I/O through thanks boolean
    public void setThanks(Boolean thanks){
        this.thanks = thanks;
    }

    // Returns if customer should be thanked
    public Boolean getThanks() {
        return thanks;
    }
    
    public void errorCall(String error) {
    	message = error;
        printToDisplay();
    }
    
    public boolean enterMembership(String number) {
    	displayKeyboard();
    	if (MemberDatabase.userExists(number)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public String swipeMembership(SelfCheckoutStation scs, com.autovend.Card userCard, 	BufferedImage userImage) {
    	swipeCardDisplay();
    	try {
			scs.cardReader.swipe(userCard, userImage); //process: register the SwipeMembershipController, then call this method
			message = "Membership card scanned.";
			return message;   
		} 
    	catch (MagneticStripeFailureException e)
    	{
			message = "Magnetic strip failure";
			return message;   
    	}
    	catch (IOException e) 
    	{
			message = "Error scanning card";
			return message;
		} 
    }
    
    public String swipeCardDisplay() {
    	message = "Please swipe card now.";
    	return printToDisplay();
    }
    
    public String displayKeyboard() {
    	message = "Please type in your membership number.";
    	return printToDisplay();
    }
    
    public String placeScannedItemInBaggingArea() {
    	message = "Please place your item in the bagging area";
    	return printToDisplay();
    }
    
    public String DoNotPlaceItemInBaggingAreaPrompt() {
    	message = "Would you like to not place item in bagging area?";
    	return printToDisplay();
    }
    
    public String sessionComplete() {
    	 message = "Payment Session Complete";
    	 return printToDisplay();
     }

	@Override
	public void requiresMaintainence(SelfCheckoutStation station, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionComplete(SelfCheckoutStation station) {
		// TODO Auto-generated method stub
		
	}
     
    public boolean getCustomerNoBag() {
    	return this.customerNoBag;
    }
    
    public void setCustomerNoBag(boolean val) {
    	this.customerNoBag = val;
    }
    
    public int getNumberOfBagsPurchased() {
    	return this.numberOfBagsPurchased;
    }
    
    public void setNumberOfBagsPurchased(int val) {
    	this.numberOfBagsPurchased = val;
    }
    
    public int getNumberOfPersonalBags() {
    	return this.numberOfPersonalBags;
    }
    
    public void setNumberOfPersonalBags(int val) {
    	this.numberOfPersonalBags = val;
    }
    
    //ask customer if they would like to place item in bagging area or not, default is always true
    public void placeInBaggingArea(boolean val) {
    	this.placeInBaggingArea = val;
    }
    
    public boolean getPlaceInBaggingArea() {
    	return this.placeInBaggingArea;
    }

	public void signalPayWithGiftCard(double amount) {
		// TODO Auto-generated method stub
		
	}

}
