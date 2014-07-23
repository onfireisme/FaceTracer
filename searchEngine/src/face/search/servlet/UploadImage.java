package face.search.servlet;


import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import config.ConfigConstant;
import face.search.bean.Photo;
import face.search.db.MongoDBUtil;
import face.search.util.FacePrediction;

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
						String fileName = ConfigConstant.testPic;
						int index = item.getName().indexOf(".");
						String uploadFormat = item.getName().substring(index+1, item.getName().length());
						File file = new File(uploadPath + "/" + fileName);
						if (ConfigConstant.PicFormat.equals(uploadFormat)){
							item.write(file);
						} else {
							File uploadFile = new File(uploadPath + "/test." + uploadFormat);
							item.write(uploadFile);
							convertImageFormat(uploadFile, ConfigConstant.PicFormat, file);
						}
						
						if (checkoutDetectPoints()) {
							Map<String, String> predictRes = FacePrediction.predictFace();
							List<Photo> res = MongoDBUtil.findSimilarPhoto(predictRes);
							Gson gson = new Gson();
							String jsonRes = gson.toJson(res);
							out.println(jsonRes);
						}
						
						// 1. compute the hash code of this image
//						String sourceFingerprint = SimilarImageSearch.produceFingerPrint(uploadPath + "/" + fileName);
//						System.out.println(uploadPath + fileName+" : " + sourceFingerprint);
						
						// 2. compare the hash code with the images in the database, and return the path of the close images
//						List<Photo> res = MongoDBUtil.findTop3SimilarPhoto(sourceFingerprint); 
						
						// 3. create instance of json parser
//						Gson gson = new Gson();
//						String jsonRes = gson.toJson(res);
						
						// 4. return json result
//						System.out.println("jsonRes = " + jsonRes);
//						out.println(jsonRes);
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
	
	private void convertImageFormat(File imgFile,String format,File formatFile) throws Exception {
		BufferedImage bIMG =ImageIO.read(imgFile);
        ImageIO.write(bIMG, format, formatFile);
	}
	
	private boolean checkoutDetectPoints() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /c E:/WorkSpaces/searchEngine/facedetect/minimal.exe");
			  
			InputStreamReader input =new InputStreamReader(p.getInputStream(),"GBK");
			char[] buf = new char[1024];
			int size;
			StringBuilder sb = new StringBuilder();
			while((size = input.read(buf)) != -1) {
			    sb.append(buf,0,size);
			}
			System.out.println(sb.toString());
			if ("success!".equals(sb.toString())) {
				return true;
			}
			return false;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
