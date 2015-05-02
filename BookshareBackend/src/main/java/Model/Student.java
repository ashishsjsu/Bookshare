package Model;

public class Student {
	int count = 0;
	
	int studentId;
	String firstName;
	String lastName;
	String email;
	String phone;
	String university;
	
	Student(){
	}
	
	public Student(String firstname,String lastname,String email, String phone, String university){
		super();
		studentId = count++;
		this.firstName = firstname;
		this.lastName = lastname;
		this.email = email;
		this.phone = phone;
		this.university = university;
	}
	
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUniversity() {
		return university;
	}
	public void setUniversity(String university) {
		this.university = university;
	}
	
}
