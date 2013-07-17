package ro.edu.ubb.blackhole.dal.pcfileservice;

import java.io.BufferedReader;
import java.io.IOException;
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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import ro.edu.ubb.blackhole.bll.Log;

/**
 * This thread handled object realize the communication with the Black Hole main server.
 * 
 * @author Turdean Arnold Robert
 * 
 */
public class ServerCommunicator extends Thread {

	/**
	 * Server URL.
	 */
	private String URL = null;

	/**
	 * XML formated string.
	 */
	private String XMLMessage = null;

	/**
	 * Timeout of the connection and the response.
	 */
	private static int TIMEOUT = 5000;

	private ResponseStringContainer response = null;

	public ServerCommunicator(String URL, String message, ResponseStringContainer response) {
		this.URL = URL;
		this.XMLMessage = message;
		this.response = response;
	}

	/**
	 * Send a message to server.
	 */
	public void run() {
		try {
			this.response.setResponse(sendHttpRequest(this.URL, this.XMLMessage));
		} catch (Exception e) {
			Log.w("run() - No response!");
			this.response.setResponse(null);
		}
	}

	/**
	 * Send a message to server and return with the response.
	 * 
	 * @param URL
	 *            Server URL.
	 * @param method
	 *            HTML request method. (eg. POST/GET)
	 * @param message
	 *            Message to server
	 * @return Server response or a timeout error message if the server is offline.
	 * @throws IOException
	 */
	public static String sendHttpRequest(String URL, String message) throws Exception {
		System.out.println("Request message: " + message);

		// Creating a new HTTP client.
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpClientParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpClientParams, ServerCommunicator.TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClientParams, ServerCommunicator.TIMEOUT);

		// Creating a new Post request
		HttpPost httpPost = new HttpPost(URL);

		// Creating the request parameters.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("message", message));

		// Setting up the request parameters.
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		// Sending the HTTP request and waiting for the response.
		HttpResponse httpResponse = httpClient.execute(httpPost);

		// Getting the response.
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String response = rd.readLine();

		// Shut down the client.
		httpClient.getConnectionManager().shutdown();
		System.out.println("Response message: " + response);
		return response;
	}
}
