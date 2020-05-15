package mediSmart.Bean;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import mediSmart.Dao.DataInterface;
import mediSmart.Util.ConstantValues;

@Entity
@Table(name=ConstantValues.USERINFO)
public class RegisterBean implements DataInterface {

	@Id
	@GeneratedValue(generator=ConstantValues.INCREMENT)
	@GenericGenerator(name=ConstantValues.INCREMENT,strategy=ConstantValues.INCREMENT)
	private int userId;
	
	private String name;

	private String email;
	
	private String password;
	
	private String city;
	
	private String mobile;
		
	private String doctorMobile;


	private String careTakerMobile;


	private String relativeMobile;
	

	public String getDoctorMobile() {
		return doctorMobile;
	}

	public void setDoctorMobile(String doctorMobile) {
		this.doctorMobile = doctorMobile;
	}


	public String getCareTakerMobile() {
		return careTakerMobile;
	}

	public void setCareTakerMobile(String careTakerMobile) {
		this.careTakerMobile = careTakerMobile;
	}


	public String getRelativeMobile() {
		return relativeMobile;
	}

	public void setRelativeMobile(String relativeMobile) {
		this.relativeMobile = relativeMobile;
	}

	public RegisterBean ()
	{
		
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}	
}
