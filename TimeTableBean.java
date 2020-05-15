package mediSmart.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import mediSmart.Dao.DataInterface;
import mediSmart.Util.ConstantValues;

@Entity
@Table(name="meditimetable")
public class TimeTableBean implements DataInterface{

	@Id
	@GeneratedValue(generator=ConstantValues.INCREMENT)
	@GenericGenerator(name=ConstantValues.INCREMENT,strategy=ConstantValues.INCREMENT)
	private int timeTableId;
	
	private int userId; 
	
	private int noOfTimes;
		
	private int noOfTablets;
	
	private int slotNo;

	private String tabletName;

	public int getTimeTableId() {
		return timeTableId;
	}

	public void setTimeTableId(int timeTableId) {
		this.timeTableId = timeTableId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNoOfTimes() {
		return noOfTimes;
	}

	public void setNoOfTimes(int noOfTimes) {
		this.noOfTimes = noOfTimes;
	}

	public int getNoOfTablets() {
		return noOfTablets;
	}

	public void setNoOfTablets(int noOfTablets) {
		this.noOfTablets = noOfTablets;
	}

	public String getTabletName() {
		return tabletName;
	}

	public void setTabletName(String tabletName) {
		this.tabletName = tabletName;
	}

	
	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	public TimeTableBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}