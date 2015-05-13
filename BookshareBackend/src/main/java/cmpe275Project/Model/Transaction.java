package cmpe275Project.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonView;

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
	String endDate;
	int duration;
	double sellingPrice;
	
	public Transaction()
	{
		
	}
	
	public Transaction(Book book, String buyer, String transactionType, String transactionDate)
	{
		this.transactionId = counter++;
		this.bookTitle = book.getBookTitle();
		this.seller = book.getOwnerId();
		this.sellingPrice = book.getSellPrice();
		this.buyer = buyer;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
		this.duration = book.getRentDuration();
	}
	
	public Transaction(String bookTitle, String buyer, String seller, String transactionType, double sellingPrice, String transactionDate){
		super();
		this.transactionId = counter++;
		this.bookTitle = bookTitle;
		this.buyer = buyer;
		this.seller = seller;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
		this.sellingPrice = sellingPrice;
		this.transactionDate = transactionDate;
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

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

}
