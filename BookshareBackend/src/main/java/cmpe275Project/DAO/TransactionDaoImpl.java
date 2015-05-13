
package cmpe275Project.DAO;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import cmpe275Project.Model.Transaction;
import cmpe275Project.config.SpringMongoConfig;

public class TransactionDaoImpl implements TransactionDao {
	
private static MongoOperations mongoOps;
	
	public TransactionDaoImpl() {
		// TODO Auto-generated constructor stub
		if(mongoOps == null){
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	    	mongoOps = (MongoOperations)context.getBean("mongoTemplate");
		}
	}

	@Override
	public void createTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		if(transaction.getTransactionId() > 0)
		{
			System.out.println("mongoOps " + mongoOps);
			mongoOps.insert(transaction);
		}
		else
		{
			System.out.println("Invalid Transaction id, trouble persisting in Transaction Dao");
		}
		
	}

	@Override
	public List<Transaction> getTransactions(String email) {

		Query query = new Query(Criteria.where("buyer").is(email));
		List<Transaction> transactions = mongoOps.find(query, Transaction.class);
		return transactions;
	}	

}
