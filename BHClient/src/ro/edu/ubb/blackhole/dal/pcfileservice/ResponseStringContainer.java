package ro.edu.ubb.blackhole.dal.pcfileservice;

public class ResponseStringContainer {

	private String response = null;

	public ResponseStringContainer(String response) {
		super();

		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
