package face.search.servlet;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import face.search.bean.Photo;
import face.search.db.MongoDBUtil;
import face.search.util.SimilarImageSearch;

/**
 * Servlet implementation class UploadImage
 */
@WebServlet("/UploadImage")
public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public UploadImage() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected synchronized void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		final String uploadPath = request.getSession().getServletContext().getRealPath("temp");
		PrintWriter out = response.getWriter();
		
		DataInputStream in = null;
		FileOutputStream fileOut = null;

		// get the data type which client upload
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		//System.out.println("isMultipart = " + isMultipart);
		
		try {
			if (isMultipart) {
				// configuration information
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// instance to decode file
				ServletFileUpload sfu = new ServletFileUpload(factory);
				List<FileItem> items = sfu.parseRequest(request);
				
				//System.out.println("items.size() = " + items.size());

				for (int i = 0; i < items.size(); i++) {
					FileItem item = items.get(i);
					if (!item.isFormField()) {
						checkDirectory(uploadPath);
						// get file name
						String suffix = item.getName().substring(item.getName().lastIndexOf("."));
						String fileName = UUID.randomUUID().toString()+suffix;
						System.out.println(fileName);
						
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
						File file = new File(uploadPath + "/" + fileName);
						System.out.println("exit: " +file.exists()+" "+uploadPath + "/" + fileName);
						if (!file.exists()) item.write(file);
						
						
						// 1. compute the hash code of this image
						String sourceFingerprint = SimilarImageSearch.produceFingerPrint(uploadPath + "/" + fileName);
						System.out.println(uploadPath + fileName+" : " + sourceFingerprint);
						
						// 2. compare the hash code with the images in the database, and return the path of the close images
						List<Photo> res = MongoDBUtil.findTop3SimilarPhoto(sourceFingerprint); 
						
						// 3. create instance of json parser
						Gson gson = new Gson();
						String jsonRes = gson.toJson(res);
						
						// 4. return json result
						System.out.println("jsonRes = " + jsonRes);
						out.println(jsonRes);
					}
				}

			} else {
				out.println("Not image type");
				out.flush();
				return;
			}
		} catch (Exception error) {
			out.println("Exception occureed：" + error.getMessage());
		} finally {
			if (in != null)
				in.close();
			if (fileOut != null)
				fileOut.close();
		}
		
	}

	/**
	 * check whether the specified directory is existed, if not create it
	 * 
	 * @param path
	 */
	private void checkDirectory(String path) {
		// TODO Auto-generated method stub
		File fileDir = new File(path);
		if (!fileDir.exists()) fileDir.mkdirs();
	}

}
