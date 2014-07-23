package face.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import face.search.bean.PhotoEvalInfo;
import face.search.db.MongoDBUtil;

/**
 * Servlet implementation class UpdateImageInfo
 */
@WebServlet("/UpdateImageInfo")
public class UpdateImageInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateImageInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		PhotoEvalInfo photoEvalInfo = new PhotoEvalInfo(); 
		photoEvalInfo.setAge(request.getParameter("age"));
		photoEvalInfo.setBlurry(request.getParameter("blurry"));
		photoEvalInfo.setEnvironment(request.getParameter("environment"));
		photoEvalInfo.setEye_wear(request.getParameter("eye_wear"));
		photoEvalInfo.setGender(request.getParameter("gender"));
		photoEvalInfo.setHair_color(request.getParameter("hair_color"));
		photoEvalInfo.setLighting(request.getParameter("lighting"));
		photoEvalInfo.setMustache(request.getParameter("mustache"));
		photoEvalInfo.setRace(request.getParameter("race"));
		photoEvalInfo.setSmiling(request.getParameter("smiling"));
		photoEvalInfo.setName(request.getParameter("pid"));
		
		try {
			int updateRowCount = MongoDBUtil.updatePhoto(photoEvalInfo);
			out.println(updateRowCount);
		} catch (Exception e) {
			out.println(0);
		}
	}

}
