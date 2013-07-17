package ro.edu.ubb.blackholepcserver.servicelayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholepcserver.bll.PhoneDispatcher;

/**
 * This servlet handle all requests which are require some data about the system. 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DataProviderServlet extends HttpServlet {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(DataProviderServlet.class);

	/**
	 * @see #doGet(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		logger.info("GET request is not supported!");
	}

	/**
	 * @see #doPost(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		// Get the message from the request.
		String requestMessage = req.getParameter("message");
		logger.info("RequestMessage: " + requestMessage);
		System.out.println("RequestMessage: " + requestMessage);

		// Creating the response message and write it into the response.
		PhoneDispatcher dispatcher = new PhoneDispatcher(requestMessage, req.getRemoteAddr());
		PrintWriter responsePrintWriter = resp.getWriter();

		String responseXML = dispatcher.getResult();
		responsePrintWriter.write(responseXML);

		logger.info("ResponseMessage: " + responseXML);
		System.out.println("ResponseMessage: " + responseXML);
	}

}
