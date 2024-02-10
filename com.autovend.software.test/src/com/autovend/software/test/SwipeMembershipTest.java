package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import com.autovend.software.*;
import org.junit.Before;
import org.junit.Test;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.SwipeMembershipCardController;


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


public class SwipeMembershipTest {
    SelfCheckoutStation scs;
    Currency c1 = Currency.getInstance(Locale.CANADA);
    Currency c2 = Currency.getInstance(Locale.ITALY);
    int[] billdenominations = {5, 10, 15, 20, 50};
    BigDecimal[] coindenominations = {BigDecimal.ONE};
	
    SwipeMembershipCardController swipeMember;
    com.autovend.MembershipCard someCard;

    AttendantStationLogic attendantStub;
    CustomerIO customerStub;
    SelfCheckoutSystemLogic logic;

    ArrayList<String> membershipTrys;


    @Before
    public void Setup(){
        someCard = new com.autovend.MembershipCard("Membership", "123456", "Customer", true);
        scs = new SelfCheckoutStation(c1, billdenominations, coindenominations, 20, 1);
        customerStub = new CustomerIO();
        logic = new SelfCheckoutSystemLogic(scs);
        swipeMember = new SwipeMembershipCardController(logic, scs);
        MemberDatabase.MEMBERSHIP_DATABASE.add("123456");
        MemberDatabase.MEMBERSHIP_DATABASE.add("234567");
        MemberDatabase.MEMBERSHIP_DATABASE.add("345678");
        MemberDatabase.MEMBERSHIP_DATABASE.add("456789");
        membershipTrys = new ArrayList<String>();
    }
    
    @Test
    public void testValidMembershipNumberSwipe() throws IOException{
    	scs.cardReader.register(swipeMember);
		BufferedImage userImage = new BufferedImage(1, 1, 1);
    	customerStub.swipeMembership(scs, someCard, userImage);
    	
    }

    @Test
    public void testInvalidMembershipNumberSwipe() throws IOException{
    	scs.cardReader.register(swipeMember);
        com.autovend.MembershipCard fakeCard = new com.autovend.MembershipCard("Membership", "213134", "Customer", true);
		BufferedImage userImage = new BufferedImage(1, 1, 1);
    	customerStub.swipeMembership(scs, fakeCard, userImage);
    	assertTrue(swipeMember.getError());
    	
    }


}
