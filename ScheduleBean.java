package mediSmart.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import mediSmart.Dao.DataInterface;
import mediSmart.Util.ConstantValues;

@Entity
@Table(name="schedules")
public class ScheduleBean implements DataInterface {

	@Id
	@GeneratedValue(generator=ConstantValues.INCREMENT)
	@GenericGenerator(name=ConstantValues.INCREMENT,strategy=ConstantValues.INCREMENT)
	private int scheduleId;
	
	private int timeTableId;
	
	private String medicineTime;

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getTimeTableId() {
		return timeTableId;
	}

	public void setTimeTableId(int timeTableId) {
		this.timeTableId = timeTableId;
	}

	public String getMedicineTime() {
		return medicineTime;
	}

	public void setMedicineTime(String medicineTime) {
		this.medicineTime = medicineTime;
	}

	public ScheduleBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
