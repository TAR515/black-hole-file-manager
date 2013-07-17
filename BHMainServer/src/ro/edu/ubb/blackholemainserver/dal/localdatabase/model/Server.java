package ro.edu.ubb.blackholemainserver.dal.localdatabase.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

/**
 * This is the model class of the Server table from the database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class Server implements Serializable {

	/**
	 * Default serialization value.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the server.
	 */
	private String serverName = null;

	/**
	 * Server's password.
	 */
	private byte[] password = null;

	/**
	 * Email of the user.
	 */
	private String email = null;

	/**
	 * The IP address of the server.
	 */
	private String ip = null;

	/**
	 * The port of the server.
	 */
	private int port = -1;

	/**
	 * Time when the server were online at last time.
	 */
	private Calendar lastOnline = null;

	/**
	 * Indicates if the server is online or not at the current moment.
	 */
	private boolean isOnline = false;

	public Server() {
		super();

		this.serverName = "";
		this.password = new byte[0];
		this.email = "";
		this.ip = "";
		this.port = 0;
		this.lastOnline = Calendar.getInstance();
		this.isOnline = false;
	}

	public Server(String serverName, byte[] password, String email, String ip, int port, Calendar lastOnline,
			boolean isOnline) {
		super();
		this.serverName = serverName;
		this.password = password;
		this.email = email;
		this.ip = ip;
		this.port = port;
		this.lastOnline = lastOnline;
		this.isOnline = isOnline;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public Calendar getLastOnline() {
		return lastOnline;
	}

	public void setLastOnline(Calendar lastOnline) {
		this.lastOnline = lastOnline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + (isOnline ? 1231 : 1237);
		result = prime * result + ((lastOnline == null) ? 0 : lastOnline.hashCode());
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + port;
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (isOnline != other.isOnline)
			return false;
		if (lastOnline == null) {
			if (other.lastOnline != null)
				return false;
		} else if (!lastOnline.equals(other.lastOnline))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		if (port != other.port)
			return false;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Server [serverName=" + serverName + ", password=" + Arrays.toString(password) + ", email="
				+ email + ", ip=" + ip + ", port=" + port + ", lastOnline=" + lastOnline + ", isOnline="
				+ isOnline + "]";
	}

}
