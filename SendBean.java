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
@Table(name="sendtohardware")
public class SendBean implements DataInterface {

	@Id
	@GeneratedValue(generator=ConstantValues.INCREMENT)
	@GenericGenerator(name=ConstantValues.INCREMENT,strategy=ConstantValues.INCREMENT)
	private int sendId;
	
	private Date entryDate;
	
	private int scheduleId;

	public int getSendId() {
		return sendId;
	}

	public void setSendId(int sendId) {
		this.sendId = sendId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public SendBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
