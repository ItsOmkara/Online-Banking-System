package LM10;

import java.util.ArrayList;
import java.util.List;

public class CustomerRegisters {
	int customer_id;
	String firstName;
	String lastName;
	String dateofBirth;
	String mobileNumber;
	String email;
	String gender;
	String branch;
	String password;
	String Paddress;
	String PrAddress;
	
	
	public CustomerRegisters( int customer_id,String firstName, String lastName, String dateofBirth, String mobileNumber, String email,
			String gender, String branch, String password, String paddress, String prAddress) {
		super();
		this.customer_id = customer_id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateofBirth = dateofBirth;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.gender = gender;
		this.branch = branch;
		this.password = password;
		Paddress = paddress;
		PrAddress = prAddress;
	}

	
	public int getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
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

	public String getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPaddress() {
		return Paddress;
	}

	public void setPaddress(String paddress) {
		Paddress = paddress;
	}

	public String getPrAddress() {
		return PrAddress;
	}

	public void setPrAddress(String prAddress) {
		PrAddress = prAddress;
	}
}
