package mediSmart.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mediSmart.Bean.ScheduleBean;
import mediSmart.Bean.SendBean;
import mediSmart.Bean.TimeTableBean;
import mediSmart.Dao.DataInterface;
import mediSmart.Dao.abstractdao;

/**
 * Servlet implementation class AlertAPI
 */
@WebServlet("/AlertAPI")
public class AlertAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlertAPI() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("In response API");
		PrintWriter out = response.getWriter();

		abstractdao dao = new abstractdao();
		Date currentTime = new Date();
		String hwResponse = "";
		List<DataInterface> la = dao.listByQuery("from TimeTableBean where userId=1");
		try {
			for (DataInterface da : la) {
				TimeTableBean element = (TimeTableBean) da;

				List<DataInterface> laInner = dao
						.listByQuery("from ScheduleBean where timeTableId=" + element.getTimeTableId());

				for (DataInterface daInner : laInner) {
					ScheduleBean elementInner = (ScheduleBean) daInner;

					String medicineTime = elementInner.getMedicineTime();

					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

					Date d = new Date();

					d = dateFormat.parse(medicineTime + ":00");

					d.setDate(currentTime.getDate());
					d.setMonth(currentTime.getMonth());
					d.setYear(currentTime.getYear());

					Date dLater = new Date();
					if (currentTime.getMinutes() > 0) {
						dLater.setMinutes(currentTime.getMinutes() - 1);
					} else {
						dLater.setHours(currentTime.getHours() - 1);
						dLater.setMinutes(58);
					}

					System.out.println("Parsed Date and Time = " + d);
					System.out.println("Current Date and Time = " + currentTime);
					/*System.out.println("Later Date and Time = " + dLater);*/

					if (d.before(currentTime) && d.after(dLater)) {

						System.out.println("In if of boolean");
						Date dateBefore = new Date();
						Date dateAfter = new Date();

						dateBefore.setHours(00);
						dateBefore.setMinutes(00);
						dateBefore.setSeconds(00);

						dateAfter.setHours(23);
						dateAfter.setMinutes(59);
						dateAfter.setSeconds(59);

						DateFormat dateFormatInner = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateBeforeStr = dateFormatInner.format(dateBefore);
						String dateAfterStr = dateFormatInner.format(dateAfter);

						System.out.println(dateBeforeStr + " before  and after " + dateAfterStr);
						List<DataInterface> laCheck = dao
								.listByQuery("from SendBean where scheduleId=" + elementInner.getScheduleId()
										+ " and entryDate between '" + dateBeforeStr + "' and '" + dateAfterStr + "'");

						System.out.println("Taken size is " + laCheck.size());
						if (laCheck.size() > 0) {
							System.out.println(element.getTabletName() + " medicines taken");
						}

						else {
							SendBean sbean = new SendBean();

							sbean.setEntryDate(currentTime);
							sbean.setScheduleId(elementInner.getScheduleId());

							dao.add(sbean);

							System.out.println("Time to take medicine " + element.getTabletName());
							if (element.getTabletName().equalsIgnoreCase("crosin"))
								hwResponse = "B";
							else
								hwResponse = "A";
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Response send is " + hwResponse);
		out.print("*" + hwResponse + "$");

	}

}
