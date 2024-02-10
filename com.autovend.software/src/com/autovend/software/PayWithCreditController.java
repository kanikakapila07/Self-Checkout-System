package com.autovend.software;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import com.autovend.BlockedCardException;
import com.autovend.Card;
import com.autovend.Card.CardData;
import com.autovend.Card.CardInsertData;
import com.autovend.Card.CardSwipeData;
import com.autovend.Card.CardTapData;
import com.autovend.ChipFailureException;
import com.autovend.InvalidPINException;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.external.CardIssuer;

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


public class PayWithCreditController implements CardReaderObserver {
	private SelfCheckoutSystemLogic system;
	private SelfCheckoutStation station;
	private CardData data;

	
	/**
	 * NOTE: This class assumes that ALL EXCEPTIONS DUE WITH INSERTING OR TAPPING OR SWIPING CARDS
	 * ARE HANDLED IN THE GUI
	 * 
	 * 
	 * 
	 * @param checkoutStation
	 * @param system
	 */
   
    

    public PayWithCreditController(SelfCheckoutStation checkoutStation, SelfCheckoutSystemLogic system) {
    	this.station = checkoutStation;
    	this.system = system;



    }


	 /*
	if (type.equals("insert")) {
		data = this.station.cardReader.insert(card,  this.system.getCurrentPin());
	} else if (type.equals("tap")) {
			data = this.station.cardReader.tap(card);
	}
	 else {
		data = this.station.cardReader.swipe(card, image);
	}
	*/
    

    
    
    public void payWithCredit() {

    		
			 int holdCode = SelfCheckoutSystemLogic.interac.authorizeHold(data.getNumber(), this.system.getCardPaymentAmount());
			 
			 if (holdCode != - 1) {
				 SelfCheckoutSystemLogic.interac.postTransaction(data.getNumber(), holdCode, this.system.getCardPaymentAmount());
				 SelfCheckoutSystemLogic.interac.releaseHold(data.getNumber(), holdCode);
				 this.system.setAmountDue(this.system.getAmountDue() - this.system.getCardPaymentAmount().doubleValue());
				 
				
				 this.system.setCardPaymentStatus(true);
				 return;
			 }
			 this.system.setCardPaymentStatus(false);
    	}

    


		

/*
if (this.system.getCreditPaymentMethod().equals(SelfCheckoutSystemLogic.PAY_BY_PIN)) {
	insertData = (CardInsertData) data;
}
else if (this.system.getCreditPaymentMethod().equals(SelfCheckoutSystemLogic.PAY_BY_TAP)) {
	tapData = (CardTapData) data;
}
else if (this.system.getCreditPaymentMethod().equals(SelfCheckoutSystemLogic.PAY_BY_SWIPE)) {
	swipeData = (CardSwipeData) data;
}



if (insertData != null) {
	
	 int holdCode = SelfCheckoutSystemLogic.interac.authorizeHold(insertData.getNumber(), this.system.getCardPaymentAmount());
	 
	 if (holdCode != - 1) {
		 SelfCheckoutSystemLogic.interac.postTransaction(insertData.getNumber(), holdCode, this.system.getCardPaymentAmount());
		 SelfCheckoutSystemLogic.interac.releaseHold(insertData.getNumber(), holdCode);
		 this.system.setAmountDue(this.system.getAmountDue() - this.system.getCardPaymentAmount().doubleValue());
		 
		 this.system.setCardPaymentStatus(true);
	 }
}
else if (tapData != null) {
	 int holdCode = SelfCheckoutSystemLogic.interac.authorizeHold(tapData.getNumber(), this.system.getCardPaymentAmount());
	 
	 if (holdCode != - 1) {
		 SelfCheckoutSystemLogic.interac.postTransaction(tapData.getNumber(), holdCode, this.system.getCardPaymentAmount());
		 SelfCheckoutSystemLogic.interac.releaseHold(tapData.getNumber(), holdCode);
		 this.system.setAmountDue(this.system.getAmountDue() - this.system.getCardPaymentAmount().doubleValue());
		 
		 this.system.setCardPaymentStatus(true);
	 }
}

else if (swipeData != null) {

		// for some reason testing pin validity is only done in this constructor? so try constructing
		// and then if no error thrown, just typecast after to get actual data
	CardInsertData insertData;
	try {
		insertData = system.getCurrentCredtiCard().createCardInsertData(this.system.getCurrentPin());
	} catch (InvalidPINException e) {
		this.system.getCardPaymentStatus(false);
		return;
	}
	
	
	insertData = (CardInsertData) this.data;
			 int holdCode = SelfCheckoutSystemLogic.interac.authorizeHold(swipeData.getNumber(), this.system.getCardPaymentAmount());
		 
			 if (holdCode != - 1) {
				 SelfCheckoutSystemLogic.interac.postTransaction(swipeData.getNumber(), holdCode, this.system.getCardPaymentAmount());
				 SelfCheckoutSystemLogic.interac.releaseHold(swipeData.getNumber(), holdCode);
				 this.system.setAmountDue(this.system.getAmountDue() - this.system.getCardPaymentAmount().doubleValue());
				 
				 this.system.setCardPaymentStatus(true);
			 }
	}
	
 	*/
    	

    	

        

		 
    

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCardInsertedEvent(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCardRemovedEvent(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCardTappedEvent(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCardSwipedEvent(CardReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCardDataReadEvent(CardReader reader, CardData data) {
		this.data = data;
		payWithCredit();

		
	}
}
