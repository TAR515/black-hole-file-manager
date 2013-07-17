package ro.edu.ubb.blackhole.datastructures;

/**
 * This data structure contains all informations about a server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class Server {

	/**
	 * Name of the server.
	 */
	private String serverName = null;

	/**
	 * Server's password.
	 */
	private String password = null;

	/**
	 * Server's IP address.
	 */
	private String ip = null;

	/**
	 * Server's port number.
	 */
	private int port = -1;

	public Server() {
		super();
		this.serverName = "";
		this.setPassword("");
		this.ip = "";
		this.port = -1;
	}

	public Server(String serverName, String password) {
		super();
		this.serverName = serverName;
		this.setPassword(password);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	/**
	 * @see #hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
		return result;
	}

	/**
	 * @see #equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
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

	/**
	 * @see #toString()
	 */
	@Override
	public String toString() {
		return "Server [serverName=" + serverName + ", password=" + password + ", ip=" + ip + ", port="
				+ port + "]";
	}

}
