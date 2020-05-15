package mediSmart.Service;

import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mediSmart.Dao.DataInterface;
import mediSmart.Dao.abstractdao;
import mediSmart.Dao.controller;
import mediSmart.Bean.EmergencyBean;
import mediSmart.Bean.RegisterBean;
import mediSmart.Bean.ScheduleBean;
import mediSmart.Bean.TimeTableBean;
import mediSmart.Util.AES;
import mediSmart.Util.ConstantValues;

@Path(ConstantValues.APPSERVCIE)
public class AppService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(ConstantValues.REGISTRATION)
	public String registration(@FormParam(ConstantValues.NAME) String name,
			@FormParam(ConstantValues.EMAIL) String email, @FormParam(ConstantValues.PASSWORD) String password,
			@FormParam(ConstantValues.CITY) String city, @FormParam(ConstantValues.MOBILE) String mobile,
			@FormParam("doctorMobile") String doctorMobile, @FormParam("careTakerMobile") String careTakerMobile,
			@FormParam("relativeMobile") String relativeMobile) throws Exception {

		JSONObject result = new JSONObject();
		RegisterBean rbean = new RegisterBean();

		rbean.setName(name);
		rbean.setEmail(email);
		rbean.setPassword(AES.encrypt(password));
		rbean.setMobile(mobile);
		rbean.setCity(city);
		rbean.setCareTakerMobile(careTakerMobile);
		rbean.setDoctorMobile(doctorMobile);
		rbean.setRelativeMobile(relativeMobile);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ConstantValues.EMAIL, rbean.getEmail());
		String qstatus = new abstractdao().addByCriteria(map, rbean, RegisterBean.class);

		if (qstatus.equalsIgnoreCase(ConstantValues.ADDED)) {
			result.put(ConstantValues.RESULT, ConstantValues.SUCCESS);
		} else if (qstatus.equalsIgnoreCase(ConstantValues.EXIST)) {
			result.put(ConstantValues.RESULT, ConstantValues.EMAIL_EXISTS);
		} else {
			result.put(ConstantValues.RESULT, ConstantValues.FAIL);
		}

		return result.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(ConstantValues.LOGIN)
	public String login(@FormParam(ConstantValues.EMAIL) String email,
			@FormParam(ConstantValues.PASSWORD) String password) {
		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();
		System.out.println(email);
		try {
			int i = new controller().search(email, password);

			if (i == -1 || i == -2) {
				result.put(ConstantValues.RESULT, ConstantValues.LOGIN_FAIL);
			} else {
				RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, i, RegisterBean.class);

				result.put(ConstantValues.NAME, rbean.getName());
				result.put(ConstantValues.EMAIL, rbean.getEmail());
				result.put(ConstantValues.USERID, rbean.getUserId());
				result.put(ConstantValues.MOBILE, rbean.getMobile());
				result.put(ConstantValues.CITY, rbean.getCity());

				result.put(ConstantValues.RESULT, ConstantValues.SUCCESS);
			}

		} catch (Exception e) {
			result.put(ConstantValues.RESULT, ConstantValues.LOGIN_FAIL);
		}
		return result.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addTimeTable")
	public String addTimeTable(@FormParam("userId") int userId, @FormParam("noOfTimes") int noOfTimes,
			@FormParam("noOfTablets") int noOfTablets, @FormParam("tabletName") String tabletName,
			@FormParam("slotNo") int slotNo) {

		JSONObject result = new JSONObject();

		TimeTableBean ttbean = new TimeTableBean();

		ttbean.setNoOfTablets(noOfTablets);
		ttbean.setNoOfTimes(noOfTimes);
		ttbean.setTabletName(tabletName);
		ttbean.setUserId(userId);
		ttbean.setSlotNo(slotNo);

		HashMap map = new HashMap<String, String>();
		map.put("slotNo", ttbean.getSlotNo());
		map.put("userId", ttbean.getUserId());
		String qstatus = new abstractdao().addByCriteria(map, ttbean, TimeTableBean.class);

		if (qstatus.equalsIgnoreCase(ConstantValues.ADDED)) {
			result.put(ConstantValues.RESULT, ConstantValues.SUCCESS);
		} else if (qstatus.equalsIgnoreCase(ConstantValues.EXIST)) {
			result.put(ConstantValues.RESULT, "Slot already occupied");
		} else {
			result.put(ConstantValues.RESULT, ConstantValues.FAIL);
		}
		System.out.println(result.toString());

		return result.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/myMedicines")
	public String myMedicines(@FormParam("userId") int userId) {

		System.out.println(userId);
		JSONObject result = new JSONObject();
		JSONArray medicinesJarray = new JSONArray();
		abstractdao dao = new abstractdao();

		List<DataInterface> la = dao.listByQuery("from TimeTableBean where userId=" + userId);

		for (DataInterface da : la) {
			TimeTableBean element = (TimeTableBean) da;

			JSONObject jobj = new JSONObject();

			jobj.put("timeTableId", element.getTimeTableId());
			jobj.put("noOfTablets", element.getNoOfTablets());
			jobj.put("noOfTimes", element.getNoOfTimes());
			jobj.put("userId", element.getUserId());
			jobj.put("slotNo", element.getSlotNo());
			jobj.put("tabletName", element.getTabletName());

			medicinesJarray.put(jobj);
		}

		result.put("medicinesJarray", medicinesJarray);
		result.put("result", "success");

		return result.toString();
	}

	@POST
	@Path("/addSchedule")
	public String addSchedule(@FormParam("timeTableId") int timeTableId,
			@FormParam("medicineTime") String medicineTime) {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();

		TimeTableBean tbean = (TimeTableBean) dao.getById("timeTableId", timeTableId, TimeTableBean.class);

		List<DataInterface> la = dao.listByQuery("from ScheduleBean where timeTableId=" + timeTableId);

		if (tbean.getNoOfTimes() > la.size()) {
			ScheduleBean scbean = new ScheduleBean();

			scbean.setMedicineTime(medicineTime);
			scbean.setTimeTableId(timeTableId);

			String qstatus = dao.add(scbean);

			if (qstatus.equalsIgnoreCase("added")) {
				result.put("result", "success");
			} else {
				result.put("result", "fail");
			}
		} else {
			result.put("result", "This medicine should be consumed only " + tbean.getNoOfTimes() + " times a day");
		}

		return result.toString();
	}

	@POST
	@Path("/mySchedule")
	public String mySchedule(@FormParam("userId") int userId) {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();
		JSONArray timeTableJarray = new JSONArray();

		List<DataInterface> la = dao.listByQuery("from TimeTableBean where userId=" + userId);

		for (DataInterface da : la) {
			JSONObject jobj = new JSONObject();
			JSONArray scheduleJarray = new JSONArray();

			TimeTableBean element = (TimeTableBean) da;

			jobj.put("tabletName", element.getTabletName());
			jobj.put("noOfTablets", element.getNoOfTablets());
			jobj.put("timeTableId", element.getTimeTableId());
			jobj.put("noOfTimes", element.getNoOfTimes());
			jobj.put("slotNo", element.getSlotNo());

			jobj.put("userId", element.getUserId());

			List<DataInterface> laInner = dao
					.listByQuery("from ScheduleBean where timeTableId=" + element.getTimeTableId());

			for (DataInterface daInner : laInner) {
				ScheduleBean elementInner = (ScheduleBean) daInner;

				JSONObject jobjInner = new JSONObject();

				jobjInner.put("medicineTime", elementInner.getMedicineTime());

				scheduleJarray.put(jobjInner);
			}

			jobj.put("scheduleJarray", scheduleJarray);

			timeTableJarray.put(jobj);

		}
		result.put("result", "success");
		result.put("timeTableJarray", timeTableJarray);

		System.out.println(result.toString());

		return result.toString();
	}

	@POST
	@Path("/emergency")
	public String emergency(@FormParam("userId") int userId) {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();

		EmergencyBean ebean = new EmergencyBean();

		ebean.setEmergencyDate(new Date());
		ebean.setUserId(userId);

		String qstatus = dao.add(ebean);

		if (qstatus.equalsIgnoreCase("added")) {
			RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, userId, RegisterBean.class);

			new controller().sendSMS(
					rbean.getName() + " has met with emergency at " + new Date().toString().substring(0, 16),
					rbean.getCareTakerMobile());
			new controller().sendSMS(
					rbean.getName() + " has met with emergency at " + new Date().toString().substring(0, 16),
					rbean.getDoctorMobile());
			new controller().sendSMS(
					rbean.getName() + " has met with emergency at " + new Date().toString().substring(0, 16),
					rbean.getRelativeMobile());

			result.put("result", "success");
		} else {
			result.put("result", "fail");
		}

		return result.toString();
	}

	@POST
	@Path("/updateDetails")
	public String updateDetails(@FormParam("userId") int userId, @FormParam(ConstantValues.NAME) String name,
			@FormParam(ConstantValues.PASSWORD) String password, @FormParam(ConstantValues.CITY) String city,
			@FormParam(ConstantValues.MOBILE) String mobile, @FormParam("doctorMobile") String doctorMobile,
			@FormParam("careTakerMobile") String careTakerMobile, @FormParam("relativeMobile") String relativeMobile) throws Exception {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();
		RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, userId, RegisterBean.class);

		rbean.setCareTakerMobile(careTakerMobile);
		rbean.setCity(city);
		rbean.setDoctorMobile(doctorMobile);
		rbean.setMobile(mobile);
		rbean.setName(name);
		rbean.setPassword(AES.encrypt(password));
		rbean.setRelativeMobile(relativeMobile);

		String qstatus = dao.update(rbean);

		if (qstatus.equalsIgnoreCase("updated")) {
			result.put("result", "success");
		} else {
			result.put("result", "fail");
		}

		return result.toString();
	}

	@POST
	@Path("/getDetails")
	public String getDetails(@FormParam("userId") int userId) throws JSONException, Exception {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();

		RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, userId, RegisterBean.class);

		result.put(ConstantValues.NAME, rbean.getName());
		result.put(ConstantValues.EMAIL, rbean.getEmail());
		result.put(ConstantValues.USERID, rbean.getUserId());
		result.put(ConstantValues.MOBILE, rbean.getMobile());
		result.put(ConstantValues.CITY, rbean.getCity());

		result.put("password", AES.decrypt(rbean.getPassword()));
		result.put("careTakerMobile", rbean.getCareTakerMobile());
		result.put("doctorMobile", rbean.getDoctorMobile());
		result.put("relativeMobile", rbean.getRelativeMobile());

		result.put(ConstantValues.RESULT, ConstantValues.SUCCESS);

		return result.toString();
	}

	@POST
	@Path("/demo")
	public String demo() {

		JSONObject result = new JSONObject();
		abstractdao dao = new abstractdao();
		return result.toString();
	}

}
