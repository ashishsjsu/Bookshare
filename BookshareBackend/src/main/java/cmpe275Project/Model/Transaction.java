package cmpe275Project.Model;

import org.springframework.data.mongodb.core.mapping.Document;
import cmpe275Project.Controller.DateParser;


@Document(collection = "transactions")
public class Transaction {
	private static int counter = 1;
	int transactionId;
	String bookTitle;
	String buyer;
	String seller;
	String transactionType;
	String transactionDate;
	double sellingPrice;
	
	public Transaction(String bookTitle, String buyer, String seller, String transactionType, double sellingPrice){
		super();
		this.transactionId = counter++;
		this.bookTitle = bookTitle;
		this.buyer = buyer;
		this.seller = seller;
		this.transactionType = transactionType;
		this.setTransactionDate();
		this.sellingPrice = sellingPrice;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate() {
		DateParser dparser = new DateParser();
		this.transactionDate = dparser.getDate();
	}
	
	public double getSellingPrice() {
		return sellingPrice;
	}
	
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * @return the bookTitle
	 */
	public String getBookTitle() {
		return bookTitle;
	}

	/**
	 * @param bookTitle the bookTitle to set
	 */
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	/**
	 * @return the buyer
	 */
	public String getBuyer() {
		return buyer;
	}

	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	/**
	 * @return the seller
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * @param seller the seller to set
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}
}
