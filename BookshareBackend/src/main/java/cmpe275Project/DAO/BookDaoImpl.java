package cmpe275Project.DAO;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import cmpe275Project.Model.Book;
import cmpe275Project.Model.PostBook;
import cmpe275Project.Model.RentOrBuy;
import cmpe275Project.MyExceptions.Exceptions;
import cmpe275Project.config.SpringMongoConfig;

public class BookDaoImpl implements BookDao {

	private static MongoOperations mongoOps;
	private static final String Book_COLLECTION = "books";  
	private static final String PostBook_COLLECTION = "postbooks";
	
	
	public BookDaoImpl(){
		if(mongoOps == null){
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	    	mongoOps = (MongoOperations)context.getBean("mongoTemplate");
		}
	}

	@Override
	public void createBook(Book book) {
		// TODO Auto-generated method stub
		if(!isExistingBook(book.getBookId())){
			book.setBookId(book.getBookId() + 1);
		}
		
		System.out.println("mongoOps " + mongoOps);
		mongoOps.insert(book, Book_COLLECTION);
	}
	
	@Override
	public void postBook(PostBook postBook) {
		// TODO Auto-generated method stub
		if(!isExistingPostBook(postBook.getBookId())){
			postBook.setBookId(postBook.getBookId() + 1);
		}
		
		System.out.println("mongoOps " + mongoOps);
		mongoOps.insert(postBook, PostBook_COLLECTION);
	}
	
	@Override
	public Book readBook(Integer b_id) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("_id").is(b_id));
		Book book = mongoOps.findOne(query, Book.class, Book_COLLECTION);
		return book;
	}

	private boolean isExistingBook(Integer bookId) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("id").is(bookId));
		Book existingId = mongoOps.findOne(query, Book.class, Book_COLLECTION);

		if(existingId != null){
			return true;
		}
		
		return false;
	}
	
	private boolean isExistingPostBook(Integer bookId) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("id").is(bookId));
		Book existingId = mongoOps.findOne(query, Book.class, PostBook_COLLECTION);

		if(existingId != null){
			return true;
		}
		
		return false;
	}

	@Override
	public Book deleteBook(Integer b_id) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("_id").is(b_id));
		Book book = mongoOps.findOne(query, Book.class, Book_COLLECTION);
		
		if(book == null){
			throw new Exceptions.BookNotFoundException();
		}
		
		mongoOps.remove(query, Book.class);
		
		return book;
	}

	@Override
	public List<Book> searchBook(String searchkey) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("bookTitle").is(searchkey).andOperator(Criteria.where("available").is(true)));
		List<Book> book = mongoOps.find(query, Book.class, Book_COLLECTION);
		if(book.size() != 0){
			return book;
		}
		Query query2 = new Query(Criteria.where("bookAuthor").is(searchkey));
		List<Book> book2 = mongoOps.find(query2, Book.class, Book_COLLECTION);
		if(book2.size() != 0){
			return book2;
		}
		Query query3 = new Query(Criteria.where("bookISBN").is(searchkey));
		List<Book> book3 = mongoOps.find(query3, Book.class, Book_COLLECTION);
		if(book3.size() != 0){
			return book3;
		}
		return null;
	}

	@Override
	public List<Book> listUserBooks(String email) {
		Query query = new Query(Criteria.where("ownerId").is(email));
		List<Book> book = mongoOps.find(query, Book.class, Book_COLLECTION);
		return book;
	}
	
	@Override
	public List<Book> listAllBooks() {
		List<Book> book = mongoOps.findAll( Book.class, Book_COLLECTION);
		return book;
	}
	
	//update book when bid is accepted or is bought directly
	@Override
	public String changeBookStatus(Integer bookId, String bookTitle) {
	
		Query query = new Query(Criteria.where("bookId").is(bookId).andOperator(Criteria.where("bookTitle").is(bookTitle)));
		Book book = mongoOps.findOne(query, Book.class);
		if(book != null){
			Update update= new Update();
			update.set("available", !book.isAvailable());
			mongoOps.findAndModify(query, update, Book.class, "books");
		}
		return "success";
	}
}
