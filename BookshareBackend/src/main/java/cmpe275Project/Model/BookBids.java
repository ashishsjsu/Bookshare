package cmpe275Project.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bids")
public class BookBids {
	private static int counter = 1;
	private Integer bidId;
	private String bidderId;
	private Integer bookId;
	private String bookTitle;
	private float bidPrice;
	private float basePrice;
	private String ownerEmail;
	
	public BookBids(){
		
	}
	
	public BookBids(String bidderId, Integer bookId, String bookTitle, float bidPrice, float basePrice, String ownerEmail){
		super();
		bidId = counter++;
		this.bidderId = bidderId;
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.bidPrice = bidPrice;
		this.basePrice = basePrice;
		this.ownerEmail = ownerEmail;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public float getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(float bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	/**
	 * @return the ownerEmail
	 */
	public String getOwnerEmail() {
		return ownerEmail;
	}

	/**
	 * @param ownerEmail the ownerEmail to set
	 */
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
}
