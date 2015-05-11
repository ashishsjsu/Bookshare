package cmpe275Project.Controller;

import java.security.InvalidParameterException;
import java.util.*;

import javax.validation.Valid;

import org.json.simple.JSONArray;
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
                
                Book bookLocal = new Book(email, book.getBookTitle(), book.getBookAuthor(), book.getBookISBN(), book.getBookDesc(), book.getBookCondition(), book.getRentPrice(), book.getSellPrice(), book.isForBuy(), book.isForRent(), book.getRentDuration());
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
    	
    	List<Book> books = bookdao.listUserBooks(email);
    	JSONArray jsonArray = new JSONArray();
    	for(Book book : books){
    		jsonArray.add(book);
    	}
    	
    	System.out.println("1. All for Listing Found : ");
    	//return jsonArray;
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
    }
    
    // List of all available Book.
    @RequestMapping( method = RequestMethod.GET, value = "/listallbooks")
    public ResponseEntity<JSONArray> listallBooks() {
    	
    	List<Book> books = bookdao.listAllBooks();
    	JSONArray jsonArray = new JSONArray();
    	for(Book book : books){
    		jsonArray.add(book);
    	}
    	
    	System.out.println("1. All for Listing Found : ");
    	//return jsonArray;
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
    }
    
    // Search for a Book.
    @RequestMapping( method = RequestMethod.GET, value = "/books/{key}")
    public @ResponseBody ResponseEntity<JSONArray> searchBook(@PathVariable(value = "key")String key) {
    	List<Book> books = bookdao.searchBook(key);
	System.out.println("1. Search for a book : " + books);
		
	JSONArray jsonArray = new JSONArray();
    	for(Book book : books){
    		jsonArray.add(book);
    	}
    	
    	System.out.println("1. All for Listing Found : ");
    	//return jsonArray;
    	return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.ACCEPTED);
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
