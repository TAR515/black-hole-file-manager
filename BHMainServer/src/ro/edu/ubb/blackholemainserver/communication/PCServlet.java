package ro.edu.ubb.blackholemainserver.communication;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholemainserver.bll.Dispatcher;

/**
 * This servlet handle all the requests from the PC Server and the Phone.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
@WebServlet("/PCServlet")
public class PCServlet extends HttpServlet {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(PCServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		logger.warn("GET requests are not supported!");
		System.out.println("GET requests are not supported!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// Get the message from the request.
		String requestMessage = request.getParameter("message");
		logger.info("RequestMessage: " + requestMessage);
		System.out.println("RequestMessage: " + requestMessage);

		// TODO Change the request.getHeader("host") to getRemoteAdr()
		// String[] tokens = request.getHeader("host").split(":");
		// Dispatcher dispatcher = new Dispatcher(requestMessage, tokens[0]);
		Dispatcher dispatcher = new Dispatcher(requestMessage, request.getRemoteAddr());
		PrintWriter responsePrintWriter = response.getWriter();

		String responseXML = dispatcher.getResult();
		logger.info("ResponseMessage: " + responseXML);
		System.out.println("ResponseMessage: " + responseXML);

		responsePrintWriter.write(responseXML);
	}

}
