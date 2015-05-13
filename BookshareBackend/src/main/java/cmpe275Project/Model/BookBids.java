package cmpe275Project.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonView;

@Document(collection = "bids")
public class BookBids {
	private static int counter = 1;
	private Integer bidId;
	private String bidderId;
	private String bidDate;
	private Integer bookId;
	private String bookTitle;
	private float bidPrice;
	private float basePrice;
	private String ownerEmail;
	private String status;
	public BookBids(){
		
	}
	
	public BookBids(String bidderId, Integer bookId, String bidDate, String bookTitle, float bidPrice, float basePrice, String ownerEmail, String status){
		super();
		bidId = counter++;
		this.bidderId = bidderId;
		this.bookId = bookId;
		this.bidDate = bidDate;
		this.bookTitle = bookTitle;
		this.bidPrice = bidPrice;
		this.basePrice = basePrice;
		this.ownerEmail = ownerEmail;
		this.status = status;
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

	/**
	 * @return the bidDate
	 */
	public String getBidDate() {
		return bidDate;
	}

	/**
	 * @param bidDate the bidDate to set
	 */
	public void setBidDate(String bidDate) {
		this.bidDate = bidDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
