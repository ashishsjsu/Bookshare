package cmpe275Project.Model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonView;

@Document(collection = "books")
public class Book {
	private static Integer counter = 1;
	@JsonView
	private Integer bookId;
	@JsonView(String.class)
	private String ownerId;
	@JsonView(String.class)
	@NotNull
	private String bookTitle;
	@JsonView(String.class)
	@NotNull
	private String bookAuthor;
	@JsonView(String.class)
	private String bookDesc;
	@JsonView(String.class)
	@NotNull
	@Id
	private String bookISBN;
	@JsonView(String.class)
	@NotNull
	private String bookCondition;
	@JsonView(Integer.class)
	@NotNull
	private Integer rentPrice;
	@JsonView(Integer.class)
	@NotNull
	private Integer sellPrice;
	@JsonView(Boolean.class)
	@NotNull
	private boolean forRent;
	@JsonView(Boolean.class)
	@NotNull
	private boolean forBuy;
	@JsonView(Integer.class)
	private Integer rentDuration;
	@JsonView(Boolean.class)
	private boolean available;
	
	public Book(String ownerId, String bookTitle, String bookAuthor, String bookISBN, String bookDesc, String bookCondition, Integer rentPrice, Integer sellPrice, Boolean forBuy, Boolean forRent, Integer rentDuration, Boolean available){
		super();
		bookId = counter++;
		this.ownerId = ownerId;
		this.bookTitle = bookTitle;
		this.bookAuthor = bookAuthor;
		this.bookDesc = bookDesc;
		this.bookISBN = bookISBN;
		this.bookCondition = bookCondition;
		this.rentPrice = rentPrice;
		this.sellPrice = sellPrice;
		this.forBuy = forBuy;
		this.forRent = forRent;
		this.rentDuration = rentDuration;
		this.available = available;
	}
	
	public Book()
	{
		
	}
	
	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getBookDesc() {
		return bookDesc;
	}

	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}

	public String getBookISBN() {
		return bookISBN;
	}

	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}

	public String getBookCondition() {
		return bookCondition;
	}

	public void setBookCondition(String bookCondition) {
		this.bookCondition = bookCondition;
	}

	/**
	 * @return the rentPrice
	 */
	public Integer getRentPrice() {
		return rentPrice;
	}

	/**
	 * @param rentPrice the rentPrice to set
	 */
	public void setRentPrice(Integer rentPrice) {
		this.rentPrice = rentPrice;
	}

	/**
	 * @return the sellPrice
	 */
	public Integer getSellPrice() {
		return sellPrice;
	}

	/**
	 * @param sellPrice the sellPrice to set
	 */
	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * @return the forRent
	 */
	public boolean isForRent() {
		return forRent;
	}

	/**
	 * @param forRent the forRent to set
	 */
	public void setForRent(boolean forRent) {
		this.forRent = forRent;
	}

	/**
	 * @return the forBuy
	 */
	public boolean isForBuy() {
		return forBuy;
	}

	/**
	 * @param forBuy the forBuy to set
	 */
	public void setForBuy(boolean forBuy) {
		this.forBuy = forBuy;
	}

	/**
	 * @return the rentDuration
	 */
	public Integer getRentDuration() {
		return rentDuration;
	}

	/**
	 * @param rentDuration the rentDuration to set
	 */
	public void setRentDuration(Integer rentDuration) {
		this.rentDuration = rentDuration;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
}
