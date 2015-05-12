package cmpe275Project.Controller;

import org.json.simple.*;

import cmpe275Project.Model.BookBids;
import cmpe275Project.Model.Student;


public class JSONObj {
	
	public JSONObject getStudentJSON(Student student) {
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("studentId", student.getStudentId());
		jsonObj.put("firstName", student.getFirstName());
		jsonObj.put("lastName", student.getLastName());
		jsonObj.put("email", student.getEmail());
		jsonObj.put("phone", student.getPhone());
		jsonObj.put("password", student.getPassword());
		jsonObj.put("university", student.getUniversity());
		
		return jsonObj;
	}
	
	public JSONObject getBookbidJSON(BookBids bid) {
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("bidId", bid.getBidId());
		jsonObj.put("bidderId", bid.getBidderId());
		jsonObj.put("bookId", bid.getBookId());
		jsonObj.put("bookTitle", bid.getBookTitle());
		jsonObj.put("bidPrice", bid.getBidPrice());
		jsonObj.put("bookOwner", bid.getOwnerEmail());
		jsonObj.put("basePrice", bid.getBasePrice());
		
		return jsonObj;
	}
	
}
