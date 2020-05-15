package mediSmart.Bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import mediSmart.Dao.DataInterface;
import mediSmart.Util.ConstantValues;


@Entity
@Table(name="emergencies")
public class EmergencyBean implements DataInterface {

	@Id
	@GeneratedValue(generator=ConstantValues.INCREMENT)
	@GenericGenerator(name=ConstantValues.INCREMENT,strategy=ConstantValues.INCREMENT)
	private int emergencyId;
	
	private int userId;
	
	private Date emergencyDate;
	
	

	public Date getEmergencyDate() {
		return emergencyDate;
	}

	public void setEmergencyDate(Date emergencyDate) {
		this.emergencyDate = emergencyDate;
	}

	public int getEmergencyId() {
		return emergencyId;
	}

	public void setEmergencyId(int emergencyId) {
		this.emergencyId = emergencyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public EmergencyBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
