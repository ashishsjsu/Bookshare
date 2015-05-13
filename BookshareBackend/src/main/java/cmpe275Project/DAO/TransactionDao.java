package cmpe275Project.DAO;


import java.util.List;

import cmpe275Project.Model.Transaction;

public interface TransactionDao {
	
	public void createTransaction(Transaction transaction);
	public List<Transaction> getTransactions(String email);
}
