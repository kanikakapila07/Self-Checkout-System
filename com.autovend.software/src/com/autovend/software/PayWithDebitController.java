package com.autovend.software;

import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
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
	public class PayWithDebitController implements CardReaderObserver {
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
	   
	    

	    public PayWithDebitController(SelfCheckoutStation checkoutStation, SelfCheckoutSystemLogic system) {
	    	this.station = checkoutStation;
	    	this.system = system;



	    }


	    

	    
	    
	    public void payWithDebit() {

	    		
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
			payWithDebit();

			
		}
	}


