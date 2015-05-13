package cmpe275Project.Controller;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cmpe275Project.DAO.*;
import cmpe275Project.Model.*;
import cmpe275Project.MyExceptions.Exceptions;
import cmpe275Project.services.LoginService;
import cmpe275Project.services.LoginServiceImpl;

@RestController
@RequestMapping("/")
public class BookController {
	Integer student_id = 1;
	private BookDao bookdao = new BookDaoImpl();
    private LoginService loginService = new LoginServiceImpl();
    private TransactionDao transactionDao = new TransactionDaoImpl();
    
    // Create Book
    @RequestMapping( method = RequestMethod.POST, value = "/{email}/book")
    public @ResponseBody Book createBook(@Valid @RequestBody Book book, @PathVariable String email, BindingResult result) {
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails details = (UserDetails)principal;
            Login loggedIn = loginService.findByAccountName(details.getUsername());
            String currEmail = loggedIn.getEmail();
            if(loggedIn.getEmail().equals(email))
            {
                if(result.hasErrors())
                {
                    throw new Exceptions.InvalidRequestBodyException();
                }
                
                Book bookLocal = new Book(email, book.getBookTitle(), book.getBookAuthor(), book.getBookISBN(), book.getBookDesc(), book.getBookCondition(), book.getRentPrice(), book.getSellPrice(), book.isForBuy(), book.isForRent(), book.getRentDuration(), book.isAvailable());
                checkValidBook(bookLocal);
                        
                bookdao.createBook(bookLocal);
                System.out.println("1. Book added : " + bookLocal);
                return bookLocal;
            }
        }
        
        return null;
    }

    
	// Post for a required Book.
    @RequestMapping( method = RequestMethod.POST, value = "/postbook")
    public @ResponseBody PostBook postForBook(@RequestBody PostBook postBook) {
    	
    	PostBook postBookLocal = new PostBook(student_id, postBook.getBookTitle(), postBook.getBookAuthor(), postBook.getBookISBN(), postBook.getBookDesc(), postBook.getBookCondition(), postBook.getPrice());
    	checkValidPostBook(postBookLocal);
	bookdao.postBook(postBookLocal);
	System.out.println("1. Book required Posted : " + postBookLocal);
	return postBookLocal;
    }

        // List of user Books.
    @PreAuthorize("permitAll")
    @RequestMapping( method = RequestMethod.GET, value = "/{email}/books")
    public ResponseEntity<JSONArray> listUserBooks(@PathVariable(value = "email")String email) {
    	
    	List<Book> books = null;
    	JSONArray jsonArray = null;
    	
    	if(email.equals("all"))
    	{
    		books = bookdao.listAllBooks();        	
    	}
    	else
    	{
    		books = bookdao.listUserBooks(email);
    	}
    	
    	if(books.size() > 0){
    		jsonArray = new JSONArray();
        	for(Book book : books){
        		jsonArray.add(book);
        	}
    	}
    	
    	System.out.println("1. All for Listing Found : ");
    	//return jsonArray;
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
    }
    
    // List of all available Book.
    @RequestMapping( method = RequestMethod.GET, value = "/books")
    public ResponseEntity<JSONArray> listallBooks() {
    	
    	List<Book> books = bookdao.listAllBooks();
    	JSONArray jsonArray = null;
    	
    	if(books.size() > 0)
    	{
    		jsonArray = new JSONArray();
    		for(Book book : books){
        		jsonArray.add(book);
        	}
    	}
    	
    	System.out.println("1. All for Listing Found : ");
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
    }
    
    // Search for a Book.
    @RequestMapping( method = RequestMethod.GET, value = "/books/{key}")
    public @ResponseBody ResponseEntity<JSONArray> searchBook(@PathVariable(value = "key")String key) {
    	List<Book> books = bookdao.searchBook(key);
    	JSONArray jsonArray = null;
    	if(books.size() > 0)
    	{
    		jsonArray = new JSONArray();
	    	for(Book book : books){
	    		jsonArray.add(book);
	    	}
    	}
		
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
    }
    
    
    //buy a book
    @RequestMapping( method = RequestMethod.POST, value = "student/{email}/book/{bookTitle}/buy")
    public @ResponseBody ResponseEntity<JSONObject> buyBook(@RequestBody Book book, @PathVariable(value="email") String email){
    
    	Transaction transaction = null;
    	JSONObj jsonObj = null;
    	JSONObject transactionJSON = null;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails details = (UserDetails)principal;
            Login loggedIn = loginService.findByAccountName(details.getUsername());
            String currEmail = loggedIn.getEmail();
            if(loggedIn.getEmail().equals(email))
            {
            	transaction = new Transaction(book.getBookTitle(), email, book.getOwnerId(), "BUY", book.getSellPrice(), dateFormat.format(date).toString());
            	//transaction = new Transaction(book.getBookTitle(), email, book.getSeller(), book.getTransactionType(), book.getSellingPrice(), book.getTransactionDate());
            	transactionDao.createTransaction(transaction);
            	bookdao.changeBookStatus(book.getBookId(), book.getBookTitle());
            }
            jsonObj = new JSONObj();
            transactionJSON = jsonObj.getTransactionJSON(transaction);
        }

    	return new ResponseEntity<JSONObject>(transactionJSON, HttpStatus.ACCEPTED);
    }
    
    //rent a book
    @RequestMapping( method = RequestMethod.POST, value = "student/{email}/book/{bookTitle}/rent")
    public @ResponseBody ResponseEntity<JSONObject> rentBook(@RequestBody Book book, @PathVariable(value="email") String email){
    
    	Transaction transaction = null;
    	JSONObject transactionJSON = null;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails details = (UserDetails)principal;
            Login loggedIn = loginService.findByAccountName(details.getUsername());
            String currEmail = loggedIn.getEmail();
            if(loggedIn.getEmail().equals(email))
            {
            	transaction = new Transaction(book, email,"BUY", dateFormat.format(date).toString());
            	
            	try {
            			//parse string to date object
						Date startDate = dateFormat.parse(transaction.getTransactionDate());
						//get calender instance
						Calendar c = Calendar.getInstance();
						c.setTime(startDate); 
						c.add(Calendar.DATE, transaction.getDuration());//add duration to start date					
						Date endDate = c.getTime();
						transaction.setEndDate(dateFormat.format(endDate).toString());//finally set end date
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
            	
            	
            	transactionDao.createTransaction(transaction);
            	bookdao.changeBookStatus(book.getBookId(), book.getBookTitle());
            }
            JSONObj jsonObj = new JSONObj();
            transactionJSON = jsonObj.getTransactionJSON(transaction);
        }

    	return new ResponseEntity<JSONObject>(transactionJSON, HttpStatus.ACCEPTED);
    }
    
    
    // Delete a Book.
    @RequestMapping( method = RequestMethod.DELETE, value = "/deletebook/{id}")
    public @ResponseBody Book deleteBook(@PathVariable(value = "id")String id) {
    	int idInt = Integer.parseInt(id);
    	Book book = bookdao.readBook(idInt);
    	if(book.getBookId() != null){
    		bookdao.deleteBook(idInt);
    	}
	System.out.println("1. Search for a book : " + book);
	return book;
    }
    
    private void checkValidBook(Book book) {
		// TODO Auto-generated method stub
    	if(book.getBookTitle() == null || book.getBookAuthor() == null || book.getBookISBN() == null){
    		throw new InvalidParameterException();
    	}
    }
    
    private void checkValidPostBook(PostBook postBook) {
		// TODO Auto-generated method stub
    	if(postBook.getBookTitle() == null || postBook.getBookAuthor() == null || postBook.getBookISBN() == null){
    		throw new InvalidParameterException();
    	}
    }
	
}
