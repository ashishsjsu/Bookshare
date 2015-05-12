package cmpe275Project.DAO;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import cmpe275Project.Model.BookBids;
import cmpe275Project.Model.RentOrBuy;
import cmpe275Project.config.SpringMongoConfig;

public class BookBidsDaoImpl implements BookBidsDao {
	
	private static MongoOperations mongoOps;
	
	public BookBidsDaoImpl() {
		// TODO Auto-generated constructor stub
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try{
		if(mongoOps == null){
			
	    	mongoOps = (MongoOperations)context.getBean("mongoTemplate");
		}
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in BookBidsDaoImpl with message " + e.getMessage());
		}
		finally
		{
			context.close();
		}
	}
	@Override
	public void addBid(BookBids bookBids) {
		// TODO Auto-generated method stub
			
		System.out.println("mongoOps " + mongoOps);
		mongoOps.insert(bookBids);
	}
	@Override
	public List<BookBids> listBidsbyUser(String email) {
		
		Query query = new Query(Criteria.where("bidderId").is(email));
		query.with(new Sort(Sort.Direction.ASC, "bookTitle"));
		List<BookBids> bids = mongoOps.find(query, BookBids.class);
		return bids;
	}
	@Override
	public boolean bidIdExist(Integer bidId) {
		Query query = new Query(Criteria.where("bidId").is(bidId));
		BookBids bids = mongoOps.findOne(query, BookBids.class);
		if(bids != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public BookBids getBid(Integer bidId) {
		Query query = new Query(Criteria.where("bidId").is(bidId));
		BookBids bid = mongoOps.findOne(query, BookBids.class);
		return bid;
	}
	
	@Override
	public RentOrBuy getRentOrBuyRecord(Integer bookId) {
		Query query = new Query(Criteria.where("bookId").is(bookId));
		RentOrBuy rentOrBuy = mongoOps.findOne(query, RentOrBuy.class);
		
		return rentOrBuy;
	}
	
	@Override
	public List<BookBids> listBidsforBook(String title) {
		
		Query query = new Query(Criteria.where("bookTitle").is(title));
		List<BookBids> bookBids = mongoOps.find(query, BookBids.class);
		return bookBids;
	}
	
}

