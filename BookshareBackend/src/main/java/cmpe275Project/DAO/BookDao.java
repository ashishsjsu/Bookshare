package cmpe275Project.DAO;

import java.util.List;

import cmpe275Project.Model.Book;
import cmpe275Project.Model.PostBook;

public interface BookDao {
	public void createBook(Book book);
	public Book readBook(Integer b_id);
	public Book deleteBook(Integer b_id);
	public List<Book> searchBook(String key);
	void postBook(PostBook postBook);
	List<Book> listAllBooks();
	List<Book> listUserBooks(String id);
	public String changeBookStatus(Integer bookId, String bookTitle);
	
}
