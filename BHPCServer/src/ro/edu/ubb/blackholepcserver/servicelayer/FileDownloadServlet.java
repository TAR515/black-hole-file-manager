package ro.edu.ubb.blackholepcserver.servicelayer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * This servlet handle the file downloading requests. The client uploads a file which is downloaded by this servlet.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
@WebServlet("/FileDownloadServlet")
public class FileDownloadServlet extends HttpServlet {
	
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Maximum memory threshold.
	 */
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; 
	
	/**
	 * Maximum file size which the servlet can download.
	 */
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 50000;
	
	/**
	 * Maximum size of the request.
	 */
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50000;

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
		String destinationPath = request.getHeader("DestinationPath");
		
		// Checking the destination path.
		if (destinationPath == null) {
			PrintWriter writer = response.getWriter();
			writer.println("Incorrect destination path!");
			writer.flush();
			return;
		}

		// Checks if the request is an upload file request.
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Incorrect request!");
			writer.flush();
			
			return;
		}

		// Configures the downloading settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);

		// Creates the destination directory if it does not exist
		File uploadDir = new File(destinationPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		try {
			List<FileItem> fileItems = upload.parseRequest(request);

			if (fileItems != null && fileItems.size() > 0) {
				// Getting all files contained in the request.
				for (FileItem item : fileItems) {
					if (!item.isFormField()) { // If this field is not a form field.
						String fileName = new File(item.getName()).getName();
						String filePath = destinationPath + File.separator + fileName;
						File storeFile = new File(filePath);

						// Saves the file on disk
						item.write(storeFile);
						request.setAttribute("message", "Upload has been done successfully!");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			request.setAttribute("message", "There was an error: " + ex.getMessage());
		}
		
		PrintWriter writer = response.getWriter();
		writer.println("OK");
		writer.flush();
	}

}
