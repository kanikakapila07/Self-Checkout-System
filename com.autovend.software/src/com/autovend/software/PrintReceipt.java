package com.autovend.software;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;

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
/**
 * Class to print receipts once customer's session is complete
 */
public class PrintReceipt implements ReceiptPrinterObserver {

	/**
	 * Prints customer's bill record once payment has been received in full where, the bill record contains
	 * BarcodedProducts. The receipt is formatted as follows:
	 * 						Item ______Barcode______ $Price
	 *
	 * 						Total: $......
	 * @param station The self-checkout station the customer is using
	 * @param billList The list of items the customer has paid for in full
	 * @return A string representing the receipt
	 * @throws EmptyException Receipt printer contains no ink
	 */
	
	private ArrayList<PrintReceiptObserver> observers = new ArrayList<>();
	private SelfCheckoutStation station;
	private String currentMessage;
	private SelfCheckoutSystemLogic system;
	private AttendantStationLogic attendent;
	private boolean noPaper = true;
	private boolean noInk = true;

	public PrintReceipt(SelfCheckoutStation stn, SelfCheckoutSystemLogic sys) {
		station = stn;
		system = sys;
	}
	
	/**
	 * registers any external observers to the list of observers
	 * @param observer: Observer to be registered to the list
	 */
	public void registerObserver(PrintReceiptObserver observer) {
		observers.add(observer);
	}
	
	
	/**
	 * registers an attendant as an observer 
	 * @param att: attendant to be registered 
	 */
	public void registerAttendent(AttendantStationLogic att) {
		attendent = att;
		observers.add(att);
	}
	
	/**
	 * returns the printer used for receipt printing
	 * @return
	 */
	public ReceiptPrinter getPrinter() {
		return station.printer;
	}
	
	/**
	 * Prints a receipt given a bill list. If receipt cannot be printed, attendant is called, and all observers are informed
	 * @param billList: List of items in the final bill
	 * @return true if a full receipt has been successfully printed, false otherwise
	 * @throws EmptyException
	 * @throws OverloadException
	 */
	public boolean print(List<Product> billList) throws EmptyException, OverloadException{

		// Tracks the total cost of the customers purchase
		BigDecimal total = new BigDecimal(0);

		// Build the receipt to print
		StringBuilder receiptOutput = new StringBuilder();
		System.out.println(billList.getClass());
		for (Product bp : billList) {
			
			if (bp instanceof BarcodedProduct) {
				receiptOutput.append(((BarcodedProduct)bp).getDescription());
				receiptOutput.append("      ");
				receiptOutput.append(((BarcodedProduct)bp).getBarcode());
				receiptOutput.append("      ");
				receiptOutput.append("$");
				receiptOutput.append(bp.getPrice());
				receiptOutput.append("\n");
				total = total.add(bp.getPrice());
			}
			else {
				receiptOutput.append(((PLUCodedProduct)bp).getDescription());
				receiptOutput.append("      ");
				receiptOutput.append(((PLUCodedProduct)bp).getPLUCode());
				receiptOutput.append("      ");
				receiptOutput.append("$");
				receiptOutput.append(bp.getPrice());
				receiptOutput.append("\n");
				total = total.add(bp.getPrice());
			}
		}
		receiptOutput.append("\nTotal: $");
		receiptOutput.append(total);

		// Convert String to char and use receipt printer to print character by character
		char[] receipt = receiptOutput.toString().toCharArray();
		for (char c : receipt) {
			try{
				station.printer.print(c);
			} catch (OverloadException oe){
				return false;
			} catch (EmptyException e) {
				if (e.getMessage().contains("There is no paper in the printer.")) {
					reactToOutOfPaperEvent(station.printer);
					station.printer.cutPaper(); // Cut the paper

					currentMessage = e.getMessage();
					reactToOutOfPaperEvent(station.printer);
					return false;
				} else {
					reactToOutOfInkEvent(station.printer);
					station.printer.cutPaper(); // Cut the paper

					currentMessage = e.getMessage();
					reactToOutOfInkEvent(station.printer);
					return false;
				}

			}
		}
		System.out.println(receiptOutput.toString());
		station.printer.cutPaper(); // Cut the paper
		return true;
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
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		system.block("Out of Paper");
		noPaper = true;
		
	}


	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		system.block("Out of Ink");
		noInk = true;
	
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		if (noPaper == true) {
			noPaper = false;
			if(system.attendant != null) {
				int num = system.attendant.stations.indexOf(system);
				 num+=1;
				system.unBlock();
				system.attendant.overrideBlock(num);
			}else {
				system.unBlock();
			}
		}
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		if (noInk == true) {
			noInk = false;
			if(system.attendant != null) {
				int num = system.attendant.stations.indexOf(system);
				 num+=1;
				 system.unBlock();
				system.attendant.overrideBlock(num);
			}else {
				system.unBlock();
			}
		}
		
		
	}
	
}
