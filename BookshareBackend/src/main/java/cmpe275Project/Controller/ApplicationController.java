package cmpe275Project.Controller;

import java.security.InvalidParameterException;
import java.util.*;

import javax.validation.Valid;
import org.json.simple.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cmpe275Project.DAO.*;
import cmpe275Project.Model.*;
import cmpe275Project.MyExceptions.Exceptions;
import cmpe275Project.MyExceptions.Exceptions.UserRegistrationFailedExeption;

@RestController
@RequestMapping("/")
public class ApplicationController {
	Integer student_id = 1;
	JSONObj jsonObj = new JSONObj();
	private LoginDao loginDao = new LoginDaoImpl(); 
	private StudentDao studentdao = new StudentDaoImpl();
	private BookDao bookdao = new BookDaoImpl();
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
    public Student getCurrentUser() {
        
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
        
        Student student = studentdao.getStudentDetails(userEmail);
        return student;
    }

    // Register Student
    @RequestMapping( method = RequestMethod.POST, value = "/student")
    public @ResponseBody Student createStudent(@Valid @RequestBody Student student, BindingResult result) {

    	if(result.hasErrors())
    	{
    		throw new Exceptions.InvalidRequestBodyException();
    	}
    		Login regStudent = new Login(student.getEmail(), student.getPassword());
    		if(loginDao.registerNewStudent(regStudent) == 1)
    		{
    			Student studentLocal = new Student(student.getFirstName(),student.getLastName(),student.getEmail(),student.getPhone(),student.getUniversity());
    			checkValidStudent(studentLocal);
    		    
    			// Call StudentDao Class method to create Student.
    			studentdao.createStudent(studentLocal);
    			System.out.println("1. Student added : " + studentLocal);
    			return studentLocal;
    		}
    		else
    		{
    			throw new UserRegistrationFailedExeption();
    		}
    }
    
 // Update Student Info
    @RequestMapping( method = RequestMethod.POST, value = "/editStudent")
    public @ResponseBody Student updateStudent(@RequestParam(value = "firstname", required = true) String firstname,
    		@RequestParam(value = "lastname", required = true) String lastname,
    		@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "university", required = false) String university) {

    		Student student = new Student(firstname,lastname,email,phone,university);
			checkValidStudent(student);
		    
			// Call StudentDao Class method to create Student.
			studentdao.updateStudent(student, student_id);
			System.out.println("1. Student Data Updated : " + student);
			return student;
    }
    
    //HELPER METHODS
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
		
	private void checkValidStudent(Student student) {
		// TODO Auto-generated method stub
		if(student.getFirstName() == null || student.getLastName() == null || student.getEmail() == null || student.getUniversity() == null){
    		throw new InvalidParameterException();
    	}
	}
	
	
    public boolean checkValidLogin(String email, String password)
    {
    	if(email != null && password != null)
    	{
    		return true;
    	}
    	return false;
    }
    
}
