package mediSmart.Service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mediSmart.Bean.RegisterBean;
import mediSmart.Bean.ScheduleBean;
import mediSmart.Bean.SendBean;
import mediSmart.Bean.TimeTableBean;
import mediSmart.Dao.DataInterface;
import mediSmart.Dao.abstractdao;
import mediSmart.Dao.controller;
import mediSmart.Util.ConstantValues;

/**
 * Servlet implementation class GetEventServlet
 */
@WebServlet("/GetEventServlet")
public class GetEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetEventServlet() {
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

		System.out.println("In Slot API");

		int slotId = Integer.parseInt(request.getParameter("slotId"));

		System.out.println(slotId);

		abstractdao dao = new abstractdao();
		RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, 1, RegisterBean.class);

		List<DataInterface> la = dao.listByQuery("from TimeTableBean where userId=1 and slotNo=" + slotId);

		if (la.size() > 0) {
			TimeTableBean ttbean = (TimeTableBean) la.get(0);

			if (ttbean.getNoOfTablets() > 0) {

				ttbean.setNoOfTablets(ttbean.getNoOfTablets() - 1);

				dao.update(ttbean);

				if (ttbean.getNoOfTablets() == 0) {
					new controller().sendSMS("Your tablet '" + ttbean.getTabletName() + "' has been finished.",
							rbean.getMobile());
					dao.delete(ttbean);

					List<DataInterface> laInner = dao
							.listByQuery("from ScheduleBean where timeTableId=" + ttbean.getTimeTableId());

					for (DataInterface da : laInner) {
						ScheduleBean elementInner = (ScheduleBean) da;

						List<DataInterface> laSche = dao
								.listByQuery("from SendBean where scheduleId=" + elementInner.getScheduleId());

						for (DataInterface daSch : laSche) {
							SendBean sbean = (SendBean) daSch;

							dao.delete(sbean);
						}

						dao.delete(elementInner);

					}
				}
			} 
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
