package cmpe275Project.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cmpe275Project.DAO.RentOrBuyDao;
import cmpe275Project.DAO.RentOrBuyDaoImpl;
import cmpe275Project.DAO.TransactionDao;
import cmpe275Project.DAO.TransactionDaoImpl;
import cmpe275Project.Model.RentOrBuy;
import cmpe275Project.Model.Transaction;

@RestController
@RequestMapping("/")
public class RentOrBuyController {
	
	private RentOrBuyDao rentOrBuyDao = new RentOrBuyDaoImpl();
	private TransactionDao transactionDao = new TransactionDaoImpl();
	private int studentId = 101;
	
		// Buy a Book. --- >>> Merge Transaction --> Hardcoding sold and rent
/*	 	@RequestMapping(method = RequestMethod.POST, value = "/buybook")
	 	public @ResponseBody String buyBook(@RequestBody RentOrBuy rentOrBuy)
	 			 {

	 		// Validate book
	 		
	 		if (!(rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()).equalsIgnoreCase("sold") || rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()).equalsIgnoreCase("rented"))) {

	 			// Change status in DB
	 			if ((rentOrBuyDao.changeBookStatus(rentOrBuy.getBookId(), "Sold").equalsIgnoreCase("success"))) //Changes status
	 			{
	 				//Check the value of type
	 				Transaction transaction = new Transaction(rentOrBuy.getBookId(), studentId ,rentOrBuy.getOwnerId(), rentOrBuy.getType(), rentOrBuy.getSellingPrice());
	 				transactionDao.createTransaction(transaction);
	 				
	 				System.out.println("Book with id " + rentOrBuy.getBookId() + " bought");
	 				System.out.println("Transaction id is: " + transaction.getTransactionId());
	 			} else {
	 				System.out.println("Book buying failed, could not update");
	 			}
	 		} else {
	 			System.out.println("Book you are trying to buy has status "
	 					+ rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()));
	 		}

	 		return "Success";
	 	}

	 	// Rent a Book.  --- >>> Merge Transaction
	 	@RequestMapping(method = RequestMethod.POST, value = "/rentbook")
	 	public @ResponseBody String rentBook(@RequestBody RentOrBuy rentOrBuy) {
	 		
	 		//Method to validate book
	 		if (!(rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()).equalsIgnoreCase("Sold") || rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()).equalsIgnoreCase("Rented"))) {

	 			// Change status in DB
	 			if ((rentOrBuyDao.changeBookStatus(rentOrBuy.getBookId(), "Rented").equalsIgnoreCase("success"))) {
	 				Transaction transaction = new Transaction(rentOrBuy.getBookId(), studentId ,rentOrBuy.getOwnerId(), rentOrBuy.getType(), rentOrBuy.getRentPrice());
	 				transactionDao.createTransaction(transaction);
	 				System.out.println("Book with id " + rentOrBuy.getBookId() + " rented for " + rentOrBuy.getDuration() + " days");
	 				System.out.println("Transaction id is: " + transaction.getTransactionId());
	 			} else {
	 				System.out.println("Book buying failed, could not update");
	 			}
	 		} else {
	 			System.out.println("Book you are trying to buy has status "
	 					+ rentOrBuyDao.getBookStatus(rentOrBuy.getBookId()));
	 		}

	 		
	 		return "Success";
	 	}
*/

}
