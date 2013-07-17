package ro.edu.ubb.blackholepcserver.servicelayer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet uploads a file to the client.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String filePath = request.getHeader("file");

		// Getting the file which we would upload.
		File file = new File(filePath);
		ServletOutputStream op = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(filePath);

		// Setting the header informations.
		response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + getFileName(filePath) + "\"");

		// Upload to the file.
		byte[] buffer = new byte[100];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		int length = 0;
		while ((in != null) && ((length = in.read(buffer)) != -1)) {
			op.write(buffer, 0, length);
		}

		in.close();
		op.flush();
		op.close();
	}

	/**
	 * Gets the name of the file from a it's path.
	 * @param filePath Path of the file.
	 * @return Name of the file.
	 */
	private String getFileName(String filePath) {
		if (filePath != null) {
			int lastIndOfSlash = filePath.lastIndexOf("/");
			int lastIndOfBackSlash = filePath.lastIndexOf("\\");

			int lastSpecialChar = (lastIndOfSlash > lastIndOfBackSlash) ? lastIndOfSlash : lastIndOfBackSlash;
			return filePath.substring(lastSpecialChar + 1, filePath.length());
		}
		return null;
	}
}
