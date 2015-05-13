package cmpe275Project.DAO;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import cmpe275Project.Model.RentOrBuy;
import cmpe275Project.MyExceptions.Exceptions;
import cmpe275Project.config.SpringMongoConfig;

public class RentOrBuyDaoImpl implements RentOrBuyDao {

	private static MongoOperations mongoOps;
	//private static final String RentOrBuy_COLLECTION = "rentOrBuy";

	public RentOrBuyDaoImpl() {
		if (mongoOps == null) {
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
					SpringMongoConfig.class);
			mongoOps = (MongoOperations) context.getBean("mongoTemplate");
		}
	}

	@Override
	public String getBookStatus(int bookId) {
		// TODO Auto-generated method stub

		Query query = new Query(Criteria.where("bookId").is(bookId));
		RentOrBuy rentOrBuy = mongoOps.findOne(query, RentOrBuy.class);
		String status = "";
		if (rentOrBuy == null) {
			throw new Exceptions.BookNotFoundException();
		} else {
			status = rentOrBuy.getStatus();
		}

		return status;
	}

	@Override
	public String changeBookStatus(int id, String status) {
	
		Query query = new Query(Criteria.where("bookId").is(id));
		RentOrBuy rentOrBuy = mongoOps.findOne(query, RentOrBuy.class);
		Update update = new Update();
		if(rentOrBuy.getStatus()!= null)
		{
		update.set("status", status);
		mongoOps.findAndModify(query, update, RentOrBuy.class);
		}
		return "success";
	}
}
