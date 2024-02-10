package com.autovend.software;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

import com.autovend.BlockedCardException;
import com.autovend.Card;
import com.autovend.Card.CardData;
import com.autovend.Card.CardInsertData;
import com.autovend.Card.CardSwipeData;
import com.autovend.Card.CardTapData;
import com.autovend.ChipFailureException;
import com.autovend.GiftCard;
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
public class PayWithGiftCardController implements CardReaderObserver {

	private SelfCheckoutSystemLogic system;
	private SelfCheckoutStation station; 
	private CardData data;


	
	
	public PayWithGiftCardController(SelfCheckoutStation checkoutStation, SelfCheckoutSystemLogic system){
	   	this.station = checkoutStation;
    		this.system = system;
	}
	
	
	public void payWithGiftCard() throws IOException {
		
		int holdCode  = SelfCheckoutSystemLogic.interac.authorizeHold(data.getNumber(), this.system.getCardPaymentAmount());
		
		if(holdCode != 1) {
			SelfCheckoutSystemLogic.interac.postTransaction(data.getNumber(), holdCode, this.system.getCardPaymentAmount());
			SelfCheckoutSystemLogic.interac.releaseHold(data.getNumber(), holdCode);
			this.system.setAmountDue(this.system.getAmountDue() - this.system.getCardPaymentAmount().doubleValue());
			this.system.setCardPaymentStatus(true);
			return;
		}
		this.system.setCardPaymentStatus(false);
	}
	
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
		// TODO Auto-generated method stub
		
	}
	
}
