package mediSmart.Service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mediSmart.Bean.EmergencyBean;
import mediSmart.Bean.RegisterBean;
import mediSmart.Dao.abstractdao;
import mediSmart.Dao.controller;
import mediSmart.Util.ConstantValues;

/**
 * Servlet implementation class EmergencyServlet
 */
@WebServlet("/EmergencyServlet")
public class EmergencyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmergencyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		abstractdao dao = new abstractdao();
		RegisterBean rbean = (RegisterBean) dao.getById(ConstantValues.USERID, userId, RegisterBean.class);

		EmergencyBean ebean = new EmergencyBean();

		ebean.setEmergencyDate(new Date());
		ebean.setUserId(userId);

		dao.add(ebean);
		
		new controller().sendSMS(rbean.getName()+" has met with emergency at "+new Date().toString().substring(0,16), rbean.getCareTakerMobile());
		new controller().sendSMS(rbean.getName()+" has met with emergency at "+new Date().toString().substring(0,16), rbean.getDoctorMobile());
		new controller().sendSMS(rbean.getName()+" has met with emergency at "+new Date().toString().substring(0,16), rbean.getRelativeMobile());
		System.out.println(rbean.getRelativeMobile());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
