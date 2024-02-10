package com.autovend.software;

import java.util.ArrayList;

import com.autovend.external.ProductDatabases;
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
public class SearchItems {
	public ArrayList<Product> search(String prompt) {
		ArrayList<Product> productResults = new ArrayList<Product>();
		
		// Split the search prompt by whitespace
		String[] tokens = prompt.split("\\s+");
		
		// Loop through each token
		for (String token : tokens) {
			// Convert the token to lowercase to ensure case insensitivity
			token = token.toLowerCase();
			
			// Loop through each product in the database of barcoded products
			for (BarcodedProduct product : ProductDatabases.BARCODED_PRODUCT_DATABASE.values()) {
				// Get the description of the product, converted to lowercase to ensure case insensitivity
				String productDescription = product.getDescription().toLowerCase();
				
				// If the token can be found anywhere in the description
				if (productDescription.contains(token)) {
					// Add the product to the list of results, but only if it isn't already in it
					if (!productResults.contains(product)) {
						productResults.add(product);
					}
				}
			}
			
			// Loop through each product in the database of PLU products
			for (PLUCodedProduct product : ProductDatabases.PLU_PRODUCT_DATABASE.values()) {
				// Get the description of the product, converted to lowercase to ensure case insensitivity
				String productDescription = product.getDescription().toLowerCase();
				
				// If the token can be found anywhere in the description
				if (productDescription.contains(token)) {
					// Add the product to the list of results, but only if it isn't already in it
					if (!productResults.contains(product)) {
						productResults.add(product);
					}
				}
			}
		}
		
		// Return the list of products that match the search prompt
		return productResults;
	}
}
