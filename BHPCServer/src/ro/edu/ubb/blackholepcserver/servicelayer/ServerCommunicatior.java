package ro.edu.ubb.blackholepcserver.servicelayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import ro.edu.ubb.blackholepcserver.bll.PCOperator;

/**
 * This thread handled object realize the communication with the Black Hole main server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ServerCommunicatior extends Thread {

	/**
	 * Reference to the controller.
	 */
	private PCOperator controller = null;

	/**
	 * Server URL.
	 */
	private String URL = null;

	/**
	 * XML formated string.
	 */
	private String XMLMessage = null;

	/**
	 * Standard error message.
	 */
	private static String NO_RESPONSE = "The server is offline!";

	public ServerCommunicatior(String URL, String message, PCOperator controller) {
		this.URL = URL;
		this.XMLMessage = message;
		this.controller = controller;
	}

	/**
	 * Send a message to server.
	 */
	public void run() {
		try {
			String response = sendHttpRequest(this.URL, this.XMLMessage);
			this.controller.responseHandler(response);
		} catch (Exception e) {
			this.controller.responseHandler(NO_RESPONSE);
		}
	}

	/**
	 * Send a message to server and returns the server's response.
	 * 
	 * @param URL
	 *            Server's URL.
	 * @param message
	 *            Message to server
	 * @return Server response or a timeout error message if the server is offline.
	 * @throws Exception
	 */
	public static String sendHttpRequest(String URL, String message) throws Exception {
		// Creating a new HTTP client.
		HttpClient httpClient = new DefaultHttpClient();

		// Creating a new Post request
		HttpPost httpPost = new HttpPost(URL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("message", message));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		// Sending the HTTP request and waiting for the response.
		HttpResponse httpResponse = httpClient.execute(httpPost);

		// Getting the response.
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String response = rd.readLine();

		// Shut down the client.
		httpClient.getConnectionManager().shutdown();

		return response;
	}
}
