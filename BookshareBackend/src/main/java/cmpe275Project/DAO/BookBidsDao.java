package cmpe275Project.DAO;

import java.util.List;

import cmpe275Project.Model.BookBids;
import cmpe275Project.Model.RentOrBuy;

public interface BookBidsDao {
	
	public void addBid(BookBids bookBids);
	public List<BookBids> listBids(String email);
	public boolean bidIdExist(Integer bidId);
	public BookBids getBid(Integer bidId);
	public RentOrBuy getRentOrBuyRecord(Integer bookId);
	
}


